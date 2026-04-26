import os
from contextlib import closing
from dataclasses import dataclass
from typing import Any

from flask import Flask, abort, render_template, request
import mysql.connector
from mysql.connector import Error


app = Flask(__name__)


@dataclass
class DbConfig:
    host: str
    port: int
    user: str
    password: str
    database: str


def load_db_config() -> DbConfig:
    return DbConfig(
        host=os.getenv("MYSQL_HOST", "127.0.0.1"),
        port=int(os.getenv("MYSQL_PORT", "3306")),
        user=os.getenv("MYSQL_USER", "root"),
        password=os.getenv("MYSQL_PASSWORD", ""),
        database=os.getenv("MYSQL_DATABASE", "test"),
    )


def open_connection():
    cfg = load_db_config()
    return mysql.connector.connect(
        host=cfg.host,
        port=cfg.port,
        user=cfg.user,
        password=cfg.password,
        database=cfg.database,
    )


def fetch_tables(cursor) -> list[str]:
    cursor.execute(
        """
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = DATABASE()
          AND table_type = 'BASE TABLE'
        ORDER BY table_name
        """
    )
    return [row[0] for row in cursor.fetchall()]


def fetch_table_stats(cursor, tables: list[str]) -> list[dict[str, Any]]:
    stats: list[dict[str, Any]] = []
    for table_name in tables:
        cursor.execute(f"SELECT COUNT(*) FROM `{table_name}`")
        row_count = cursor.fetchone()[0]
        stats.append({"table_name": table_name, "row_count": row_count})
    return stats


def fetch_columns(cursor, table_name: str) -> list[dict[str, Any]]:
    cursor.execute(
        """
        SELECT
            column_name,
            data_type,
            is_nullable,
            column_key,
            extra
        FROM information_schema.columns
        WHERE table_schema = DATABASE()
          AND table_name = %s
        ORDER BY ordinal_position
        """,
        (table_name,),
    )
    return [
        {
            "column_name": row[0],
            "data_type": row[1],
            "is_nullable": row[2],
            "column_key": row[3],
            "extra": row[4],
        }
        for row in cursor.fetchall()
    ]


@app.route("/")
def dashboard():
    selected_table = request.args.get("table")

    try:
        with closing(open_connection()) as conn, closing(conn.cursor()) as cursor:
            tables = fetch_tables(cursor)

            if selected_table and selected_table not in tables:
                abort(404, description="Tabella non trovata")

            table_stats = fetch_table_stats(cursor, tables)
            columns = fetch_columns(cursor, selected_table) if selected_table else []

        return render_template(
            "dashboard.html",
            db_name=load_db_config().database,
            tables=tables,
            table_stats=table_stats,
            selected_table=selected_table,
            columns=columns,
        )
    except Error as exc:
        return render_template("dashboard.html", db_error=str(exc), tables=[], table_stats=[])


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=int(os.getenv("PORT", "5000")), debug=True)

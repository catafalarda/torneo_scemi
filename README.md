# Dashboard MySQL (Flask)

Piccola webapp in Flask che mostra una dashboard del contenuto di un database MySQL:

- elenco tabelle
- conteggio righe per tabella
- dettaglio colonne per tabella selezionata

## Avvio rapido

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
# modifica .env con i tuoi parametri
export $(cat .env | xargs)
python app.py
```

Apri `http://localhost:5000`.

## Variabili ambiente

- `MYSQL_HOST`
- `MYSQL_PORT`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `MYSQL_DATABASE`
- `PORT`

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



/**
 * 
 */

/**
 * @author Vieste
 *
 */
public class TorneoGUI extends JFrame implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	JFrame frame;
	JComboBox squadreA,squadreB;
	String squadra1="",squadra2="";
	JLabel s;
	JButton piuA=new JButton("");
	JButton menoA=new JButton("");
	JButton piuB=new JButton("");
	JButton menoB=new JButton("");
	JButton play=new JButton("");
	JButton pausa=new JButton("");
	JButton stop=new JButton("");
	int puntiA=0;
	int puntiB=0;
	JLabel punteggioA=new JLabel(puntiA+" - "+puntiB);
	JLabel punteggioB=new JLabel("0");
	JLabel tempo=new JLabel("0:0");
	int sec=0;
	int min=0;
	ClockThread cl=new ClockThread();
	boolean partito=false;
	boolean pausat=false;
	boolean stopt=false;
	GraphicsDevice gd; 
	Color colore;
	public TorneoGUI(boolean schermointero)
	{
		super("Torneo di calcio");
		setFrameProperties();
		pack();
	}
	public void setFrameProperties()
	{
		Dimension d = getToolkit().getScreenSize();
		setSize((int)d.getWidth(),(int)d.getHeight());
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(setMenuProperties());
		setContentPane(getMainPanel());
		addMouseListener(this);
		colore=this.getJMenuBar().getBackground();
		setVisible(true);
	}
	private JMenuBar setMenuProperties(){/*Setta tutti i componenti presenti nel menu*/
		
		/*
		 * Crea un barra dei Menu.
		 */
		JMenuBar mb=new JMenuBar();
		
		JMenu file=new JMenu("File");
		JMenu info=new JMenu("Help");
		
		URL url = ClassLoader.getSystemResource("icone/squadra.png");
		ImageIcon img = new ImageIcon(url);
		JMenuItem squadra=new JMenuItem("Inserisci squadra",img);
		url = ClassLoader.getSystemResource("icone/gara.gif");
		img = new ImageIcon(url);
		JMenuItem partita=new JMenuItem("Scegli le squadre",img);
		url = ClassLoader.getSystemResource("icone/schermo.gif");
		img = new ImageIcon(url);
		JMenuItem schermo=new JMenuItem("Schermo intero",img);
		url = ClassLoader.getSystemResource("icone/inverti.png");
		img = new ImageIcon(url);
		JMenuItem inverti=new JMenuItem("Inverti squadre",img);
		url = ClassLoader.getSystemResource("icone/exit.png");
		img = new ImageIcon(url);
		JMenuItem esci=new JMenuItem("Esci",img);
		
		url = ClassLoader.getSystemResource("icone/info.png");
		img = new ImageIcon(url);
		JMenuItem about=new JMenuItem("About...",img);
		
		squadra.addActionListener(this);
		partita.addActionListener(this);
		schermo.addActionListener(this);
		inverti.addActionListener(this);
		about.addActionListener(this);
		esci.addActionListener(this);
		
		file.add(squadra);
		file.add(partita);
		file.add(inverti);
		file.add(schermo);
		file.addSeparator();
		file.add(esci);

		info.add(about);
		
		mb.add(file);
		mb.add(info);
		return mb;
	}
	private JPanel getMainPanel() {
		
		JPanel p=new JPanel();
		p.setLayout(new BorderLayout());
		Dimension d = this.getToolkit().getScreenSize();
		//System.out.println(d.width+" , "+d.height);
		URL url = ClassLoader.getSystemResource("icone/calcio1.png");
		ImageIcon img = new ImageIcon(url);
		JLabel l=new JLabel(img);
		l.setBounds(0,0,1000,1000);
		
		s=new JLabel(squadra1+" - "+squadra2);
		s.setHorizontalAlignment(SwingConstants.CENTER);
		s.setBounds(0,0,(int)d.getWidth(),150);
		s.setFont(new Font("Serif", Font.BOLD,100));//73 ok va bene
		s.setForeground(Color.white);
		
		url = ClassLoader.getSystemResource("icone/meno.png");
		img = new ImageIcon(url);
		menoA.setIcon(img);
		menoA.setHorizontalTextPosition(SwingConstants.CENTER);
		menoA.setBounds(370,400,50,50);
		
		url = ClassLoader.getSystemResource("icone/piu.png");
		img = new ImageIcon(url);
		piuA.setIcon(img);
		piuA.setHorizontalTextPosition(SwingConstants.CENTER);
		piuA.setHorizontalAlignment(SwingConstants.CENTER);
		piuA.setBounds(460,400,50,50);
		
		url = ClassLoader.getSystemResource("icone/meno.png");
		img = new ImageIcon(url);
		menoB.setIcon(img);
		menoB.setHorizontalTextPosition(SwingConstants.CENTER);
		menoB.setBounds(770,400,50,50);
		
		url = ClassLoader.getSystemResource("icone/piu.png");
		img = new ImageIcon(url);
		piuB.setIcon(img);
		piuB.setHorizontalTextPosition(SwingConstants.CENTER);
		piuB.setBounds(860,400,50,50);
		
		punteggioA=new JLabel(puntiA+" - "+puntiB);
		punteggioA.setHorizontalAlignment(SwingConstants.CENTER);
		punteggioA.setBounds(0,120,(int)d.getWidth(),300);//250,300
		punteggioA.setFont(new Font("Serif", Font.BOLD, 300));
		punteggioA.setForeground(Color.GREEN);
		
		/*punteggioB.setBounds(250,120,500,300);//250
		punteggioB.setHorizontalTextPosition(SwingConstants.CENTER);
		punteggioB.setBounds(900,120,300,300);
		punteggioB.setFont(new Font("Serif", Font.BOLD, 250));
		punteggioB.setForeground(Color.GREEN);*/
		
		piuA.addActionListener(this);
		menoA.addActionListener(this);
		piuB.addActionListener(this);
		menoB.addActionListener(this);
		
		tempo.setHorizontalAlignment(SwingConstants.CENTER);
		tempo.setBounds(0,420,(int)d.getWidth(),300);
		tempo.setFont(new Font("Serif", Font.BOLD, 300));
		tempo.setForeground(Color.YELLOW);
		
		url = ClassLoader.getSystemResource("icone/play.png");
		img = new ImageIcon(url);
		play.setIcon(img);
		play.setHorizontalTextPosition(SwingConstants.CENTER);
		play.setBounds(500,690,100,30);
		if(partito==true)
		{
			try
			{
				cl.start();
			}
			catch(IllegalThreadStateException exc)
			{
				System.out.println("Nuovo thread");
			}
		}
		
		url = ClassLoader.getSystemResource("icone/pausa.png");
		img = new ImageIcon(url);
		pausa.setIcon(img);
		pausa.setHorizontalTextPosition(SwingConstants.CENTER);
		pausa.setBounds(600,690,100,30);
		
		url = ClassLoader.getSystemResource("icone/stop.png");
		img = new ImageIcon(url);
		stop.setIcon(img);
		stop.setHorizontalTextPosition(SwingConstants.CENTER);
		stop.setBounds(700,690,100,30);
		
		play.addActionListener(this);
		pausa.addActionListener(this);
		stop.addActionListener(this);
		
		p.add(s);
		p.add(menoA);
		p.add(piuA);
		p.add(menoB);
		p.add(piuB);
		p.add(punteggioA);
		//p.add(punteggioB);
		p.add(tempo);
		p.add(play);
		p.add(pausa);
		p.add(stop);
		p.add(l);
		return p;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TorneoGUI(false);
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Inserisci squadra"))
		{
			String[] options={"OK"};
			Object[] message=new Object[2];
			message[0]="Inserisci il nome della squdra";
			message[1]=new JTextField("");
			JOptionPane.showOptionDialog(frame,message,"Nuova squadra",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
		    String squadra=((JTextField)message[1]).getText();
		    if(!squadra.equals(""))
		    	metodi.inserisciSqudra(squadra,frame);
		}
		if(e.getActionCommand().equals("Scegli le squadre"))
		{
			String[] options={"OK"};
			Object[] message=new Object[4];
			squadreA=metodi.caricaSquadre(frame,squadreA);
			squadreB=metodi.caricaSquadre(frame,squadreB);
			message[0]="Seleziona la squadra";
			message[1]=squadreA;
			message[2]="Seleziona la squadra";
			message[3]=squadreB;
			JOptionPane.showOptionDialog(frame,message,"Scegli squadra",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
		    squadra1=((JComboBox)message[1]).getSelectedItem().toString();
		    squadra2=((JComboBox)message[3]).getSelectedItem().toString();
		    s.setText(squadra1+" - "+squadra2);
		    FontAdapter fontadapter=new FontAdapter();
		    fontadapter.computePixPoint(s);
		    s.setFont(new Font("Serif", Font.BOLD,fontadapter.getBestFontSize(s.getWidth())));
		}
		if(e.getActionCommand().equals("Schermo intero"))
		{
			this.getJMenuBar().setBackground(Color.BLACK);
			this.getJMenuBar().setBorderPainted(false);
		}
		if(e.getActionCommand().equals("Inverti squadre"))
		{
			puntiA=puntiA^puntiB;
			puntiB=puntiA^puntiB;
			puntiA=puntiA^puntiB;
			String temp=squadra1;
			squadra1=squadra2;
			squadra2=temp;
			s.setText(squadra1+" - "+squadra2);
			FontAdapter fontadapter=new FontAdapter();
			fontadapter.computePixPoint(s);
			s.setFont(new Font("Serif", Font.BOLD,fontadapter.getBestFontSize(s.getWidth())));
			punteggioA.setText(puntiA+" - "+puntiB);
			
		}
		if(e.getActionCommand().equals("About..."))
		{
			JOptionPane.showMessageDialog(frame,"Programma realizzato da Pecorelli Mattia","About...",JOptionPane.INFORMATION_MESSAGE);
		}
		if(e.getActionCommand().equals("Esci"))
		{
			System.exit(0);
		}
		if(e.getSource()==piuA)
		{
			puntiA++;
			//punteggioA.setText(String.valueOf(puntiA));
			punteggioA.setText(puntiA+" - "+puntiB);
		}
		if(e.getSource()==menoA)
		{
			if(puntiA>0)
				puntiA--;
			punteggioA.setText(puntiA+" - "+puntiB);
		}
		if(e.getSource()==piuB)
		{
			puntiB++;
			punteggioA.setText(puntiA+" - "+puntiB);
		}
		if(e.getSource()==menoB)
		{
			if(puntiB>0)
				puntiB--;
			punteggioA.setText(puntiA+" - "+puntiB);
		}
		if(e.getSource()==play)
		{
			pausa.setEnabled(true);
			stop.setEnabled(true);
			play.setEnabled(false);
			if(pausat==true)
				pausat=false;
			if(stopt==true)
			{
				stopt=false;
				partito=false;
			}
			if(partito==false)
			{
				try
				{
					cl.start();
					partito=true;
				}
				catch(IllegalThreadStateException exc)
				{
					System.out.println("Nuovo thread");
				}
			}
		}
		if(e.getSource()==pausa)
		{
			play.setEnabled(true);
			pausa.setEnabled(false);
			if(stopt==false)
				pausat=true;
		}
		if(e.getSource()==stop)
		{
			int n = JOptionPane.showConfirmDialog(frame," Vuoi azzerare il tempo?","Azzeramento tempo",JOptionPane.YES_NO_OPTION);
			if(n==0)
			{
				play.setEnabled(true);
				pausa.setEnabled(false);
				stop.setEnabled(false);
				cl.interrupt();
				stopt=true;
				tempo.setText("0:0");
				sec=0;
				min=0;
			}
		}
	}
	class ClockThread extends Thread 
	{
		public void run() 
		{
			tempo();
			try
			{
				sleep(1000);
			}
			catch(InterruptedException ie) 
			{
				System.out.println(ie.getMessage());
			}
			new ClockThread().start();
		}
		private void tempo()
		{
			if((pausat==false)&&(stopt==false))
			{
				sec++;
				if(sec==60)
				{
					min++;
					sec=0;
				}
				tempo.setText(min+":"+sec);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()==3)
		{
			int n = JOptionPane.showConfirmDialog(frame," Vuoi chiudere lo schermo intero?","Schermo intero",JOptionPane.YES_NO_OPTION);
			if(n==0)
			{
				this.getJMenuBar().setBackground(colore);
			}
		}
	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public class FontAdapter extends ComponentAdapter {
		private double pixPoint = 0;

		public void componentResized(ComponentEvent e) {
			JLabel label = (JLabel)e.getComponent();
			if(pixPoint == 0) {
				computePixPoint(label);
			}
			Insets margin = label.getInsets();
			int width = label.getWidth() - (margin.left + margin.right);
			Font font = label.getFont().deriveFont((float)getBestFontSize(width));
			label.setFont(font);
		}
		private int getBestFontSize(int width) {
			return (int)(pixPoint * width);
		}
		private void computePixPoint(JLabel label) {
			Font font = label.getFont();
			FontRenderContext ctx = ((Graphics2D)label.getGraphics()).getFontRenderContext();
			String text = label.getText();
			double stringWidth = font.getStringBounds(text, ctx).getWidth();
			stringWidth += font.getStringBounds("w", ctx).getWidth();
			pixPoint = font.getSize() / stringWidth;
		}
	}
}

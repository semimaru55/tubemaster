package Capture;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import Capture.CaptureSystem;
import Capture.PanelCapture;
import Main.MainForm;

public class CaptureLeds extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private PanelCapture panelCap = null;
	
	private Image imgGreen 		= Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"green.png"));
	private Image imgRed 		= Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"red.png"));
	private Image imgYellow 	= Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"yellow.png"));
	private Image imgGray 		= Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"gray.png"));
	
	private Timer timerState 	= new Timer (2000, this);
	
	private enum State
	{
		WARN, GOOD, BAD, DISABLED
	}
	
	private long captured 		= 0;
	private long dropped 		= 0;

	private JLabel lblState = new JLabel(MainForm.lang.lang_table[84]);
	private State currentState = State.DISABLED;
	
	public CaptureLeds(PanelCapture panelCap)
	{
		super();
		this.setLayout(null);
		this.setSize(new Dimension(694,26));
		this.setBackground(Color.decode("#676767"));
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		
		this.lblState.setBounds(28, 2, 662, 20);
		this.lblState.setForeground(Color.white.darker());
		this.lblState.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.add(this.lblState);
			
		this.panelCap = panelCap;
		this.timerState.start();
	}
	

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		
		switch (this.currentState)
		{
		case BAD :
			g.drawImage(this.imgRed, 7, 4, this);
			this.lblState.setText(MainForm.lang.lang_table[81]);
			break;
		case GOOD :
			g.drawImage(this.imgGreen, 7, 4, this);
			this.lblState.setText(MainForm.lang.lang_table[82]);
			break;
		case WARN :
			g.drawImage(this.imgYellow, 7, 4, this);
			this.lblState.setText(MainForm.lang.lang_table[83]);
			break;		
		case DISABLED :
			g.drawImage(this.imgGray, 7, 4, this);
			this.lblState.setText(MainForm.lang.lang_table[84]);
			break;
		}
	}
		

	public void actionPerformed(ActionEvent e) 
	{

		CaptureSystem cap = this.panelCap.getCaptureSystem();
		if (cap != null)
		{
			this.captured = cap.getNbCapturedPackets();
			this.dropped = cap.getNbDroppedPackets();
		}

		if ((cap == null) || (!this.panelCap.isCapAlive())) this.currentState = State.DISABLED;
		else
		if (this.captured == 0) this.currentState = State.BAD;
		else
		if (this.dropped > 0) this.currentState = State.WARN;
		else
		this.currentState = State.GOOD;	
			
		this.repaint();
		
	}

}

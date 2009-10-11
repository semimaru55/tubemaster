/*
 * TubeMaster++ - An Internet Multimedia Capture Tool.
 * Copyright (C) 2009 GgSofts
 * Contact: admin@tubemaster.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class FenAbout extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panFen = new JPanel()	//Panel de la fenetre.
	{
		
		private static final long serialVersionUID = 1L;
		
		//private Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/"+"about.png"));

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			//g.drawImage(this.img, 290, 143, this);
			g.drawLine(95,62,300,62);
		}
		
		
	}; 

	private File f = new File("ChangeLog.txt");
	
	private JLabel lblTitle = new JLabel();
	private JLabel lblCopyVersion = new JLabel();
	private JLabel lblContSite = new JLabel();
	private JLabel lblConfig = new JLabel();
	private JLabel lblGreets = new JLabel();
	private JLabel lblChange = new JLabel();
	
	private JTextArea areaConfig = new JTextArea();
	private JTextArea areaGreets = new JTextArea();
	private JScrollPane scrollGreets = new JScrollPane(this.areaGreets);
	private JTextArea areaChange = new JTextArea();
	private JScrollPane scrollChange = new JScrollPane(this.areaChange);
	
	
	public FenAbout()
	{
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#FFFFFF"));
		this.setTitle("About TubeMaster++");
		this.setSize(410, 423);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		ImageIcon monIcon = new ImageIcon(getClass().getResource("images/"+"icon.jpg"));
		this.setIconImage(monIcon.getImage());
		
		
		this.lblTitle.setText("TubeMaster++");
		this.lblTitle.setHorizontalAlignment(JLabel.CENTER);
		this.lblTitle.setFont(new java.awt.Font("Default_tm", Font.BOLD, 15));
		this.lblTitle.setBounds(45,4,315,20);
		

		this.lblCopyVersion.setText("Version "+MainForm.tm_version+" - (c) 2009 GgSofts");
		this.lblCopyVersion.setHorizontalAlignment(JLabel.CENTER);
		this.lblCopyVersion.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblCopyVersion.setBounds(45,21,315,20);
		
		this.lblContSite.setText("Contact ggsofts@gmail.com - http://www.tubemaster.net");
		this.lblContSite.setHorizontalAlignment(JLabel.CENTER);
		this.lblContSite.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblContSite.setBounds(25,36,355,20);
		
		this.lblConfig.setText("TubeMaster++ uses the following components :");
		this.lblConfig.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblConfig.setBounds(10,70,315,20);
		
		
		this.areaConfig.setText("- JAVA Runtime Environment v"+System.getProperty("java.version")+"\n"+
								"- JPcap 0.7");
		
		
		this.areaConfig.setBounds(10,87,385,40);
		this.areaConfig.setOpaque(false);
		this.areaConfig.setFont(new java.awt.Font("Default_tm", 0, 10));
		this.areaConfig.setEditable(false);
		this.areaConfig.setLineWrap(true);
		this.areaConfig.setWrapStyleWord(true);
		this.areaConfig.setBorder(BorderFactory.createEtchedBorder());
		this.areaConfig.setForeground(Color.green.darker());
		
		
		this.lblGreets.setText("Greetings :");
		this.lblGreets.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblGreets.setBounds(10,129,315,20);

		this.areaGreets.setText("- WinPcap and LibPcap contributors.\n" +
				"- Sly Technologies : JNetPcap library.\n" +
				"- Keita Fujii : JPcap library.\n" +
				"- FFMpeg Project : FFMpeg & FFPlay tools.\n" +
				"- Iconspedia.com : Some nice icons.\n" +
				"- Smaxe Ltd : RTMP Protocol Support.\n" + 
				"- TubeMaster++ contributors (Donators, Bugs reporters ...).\n" + 
				"- Cocodidou : Unix version (help, tests).\n" +
				"- Kiseong Kim : Korean Translation.\n" +
				"- Dr.M.ReDa & Mr On Line : Arabic Translation.\n" +
				"- JohannesTN : Danish Translation.\n" +
				"- Juan Pablo Zapata : Spanish (Argentina) Translation.\n" + 
				"- Oscar Nicolas : Catalan Translation.\n" +  
				"- Enzo Musella & Dav91 : Italian Translation.\n" +
				"- BeGe : Swedish Translation.\n" +
				"- Joakim Lazakis : Greek Translation.\n" +
				"- Diego Fernando Munoz Delgado &  Juan Calderon R. : Spanish Translation.\n" + 
				"- Li Lindun : Chinese (Traditional & Simplified) Translation.\n" +
				"- Matheus Lutkmeier : Brazilian Translation.\n" + 
				"- Dejan Omasta : Bosnian Translation.\n" +
				"- Gabor Karikas : Hungarian Translation.\n" +
				"- Artem Kashkanov : Russian Translation.\n" +
				"- Nguyen Quang Vinh : Vietnamese Translation.\n" + 
				"- Jakub Gal : Slovak Translation.\n" +
				"- Mollux67 : Elsassisch Translation.\n" +
				"- Enver Can Cilingir : Turkish Translation.\n" +
				"- Artur Penedo : Portuguese Translation.\n" + 
				"- Arjan Lindeboom : Dutch Translation.\n" + 
				"- Markus Grasel & Fabian K. : German Translation.\n"
				);
		
		
		
		this.scrollGreets.getViewport().setOpaque(false);
		this.scrollGreets.setOpaque(false);
		this.scrollGreets.setBounds(10,147,385,110);
		
		this.areaGreets.setOpaque(false);
		this.areaGreets.setCaretPosition(0);
		this.areaGreets.setFont(new java.awt.Font("Default_tm", 0, 10));
		this.areaGreets.setEditable(false);
		this.areaGreets.setLineWrap(true);
		this.areaGreets.setWrapStyleWord(true);

		
		
		this.lblChange.setText("Change Log :");
		this.lblChange.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblChange.setBounds(10,256,315,20);

		try
		{
			BufferedReader lecteur = null;
		    String ligne;
	    	lecteur = new BufferedReader(new FileReader(this.f));
	    	while ((ligne = lecteur.readLine()) != null) this.areaChange.setText(this.areaChange.getText()+ligne+"\n");
	   	    lecteur.close();
		}catch (Exception e) {Commun.logError(e);}
		
		
		
		
		this.scrollChange.setBounds(10,274,385,110);
		this.scrollChange.getViewport().setOpaque(false);
		this.scrollChange.setOpaque(false);
		this.areaChange.setOpaque(false);
		this.areaChange.setCaretPosition(0);
		this.areaChange.setFont(new java.awt.Font("Default_tm", 0, 10));
		this.areaChange.setEditable(false);
		this.areaChange.setLineWrap(true);
		this.areaChange.setWrapStyleWord(true);		
		
		
		
		this.panFen.add(this.lblTitle);
		this.panFen.add(this.lblCopyVersion);
		this.panFen.add(this.lblContSite);
		this.panFen.add(this.areaConfig);
		this.panFen.add(this.lblConfig);
		this.panFen.add(this.scrollGreets);
		this.panFen.add(this.lblGreets);
		this.panFen.add(this.scrollChange);
		this.panFen.add(this.lblChange);
		
		this.setVisible(true);
	
	}
	
	

}

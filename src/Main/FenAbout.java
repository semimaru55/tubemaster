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
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Graphique.TMButton;



public class FenAbout extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panFen = new JPanel()	//Panel de la fenetre.
	{
		
		private static final long serialVersionUID = 1L;
		
		private Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/"+"about.png"));
		private Image imgback = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/"+"back.png"));
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			g.drawImage(this.imgback, 0, 0, this);
			g.drawImage(this.img, 5, 5, this);
			
		}
		
		
	}; 

	
	private JLabel lblTitle = new JLabel();
	private JLabel lblVersion = new JLabel();
	private JLabel lblCopy = new JLabel();
	private JLabel lblGreets = new JLabel();
	
	private JTextArea areaGreets = new JTextArea();
	private JScrollPane scrollGreets = new JScrollPane(this.areaGreets);
	
	private TMButton btnLicense = new TMButton(this,6,4,"GNU License","license.png",20,2,94);
	private TMButton btnWebsite = new TMButton(this,7,5,"Website","website.png",20,2,94);
	private TMButton btnChanges = new TMButton(this,6,4,"Change Log","changelog.png",20,2,94);
	
	public FenAbout()
	{
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#D1D1D1"));
		this.setTitle("About TubeMaster++");
		this.setSize(418, 445);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		ImageIcon monIcon = new ImageIcon(getClass().getResource("images/"+"icon.jpg"));
		this.setIconImage(monIcon.getImage());
		
		
		this.lblTitle.setText("TubeMaster++");
		this.lblTitle.setFont(new java.awt.Font("Tahoma", 0, 18));
		this.lblTitle.setBounds(105,25,150,20);

		this.lblVersion.setText("Version "+MainForm.tm_version);
		this.lblVersion.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.lblVersion.setBounds(107,47,310,20);
		
		this.lblCopy.setText("(c) GgSofts 2007-2011 | admin@tubemaster.net");
		this.lblCopy.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.lblCopy.setBounds(107,66,310,20);

		
		
		this.lblGreets.setText("Contributors :");
		this.lblGreets.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.lblGreets.setBounds(15,99,315,20);

		this.areaGreets.setText(this.getContributors());

		this.scrollGreets.getViewport().setOpaque(false);
		this.scrollGreets.setOpaque(false);
		this.scrollGreets.setBounds(15,118,385,243);
		
		this.areaGreets.setOpaque(false);
		this.areaGreets.setCaretPosition(0);
		this.areaGreets.setFont(new java.awt.Font("Tahoma", 0, 10));
		this.areaGreets.setEditable(false);
		this.areaGreets.setLineWrap(true);
		this.areaGreets.setWrapStyleWord(true);
		
		
		this.btnLicense.setBounds(15,373,116,27);
		this.btnWebsite.setBounds(148,373,116,27);	
		this.btnChanges.setBounds(282,373,116,27);


		
		this.panFen.add(this.lblTitle);
		this.panFen.add(this.lblVersion);
		this.panFen.add(this.lblCopy);
		this.panFen.add(this.scrollGreets);
		this.panFen.add(this.lblGreets);
		this.panFen.add(this.btnLicense);
		this.panFen.add(this.btnWebsite);
		this.panFen.add(this.btnChanges);
		
		this.setVisible(true);
	
	}


	public void actionPerformed(ActionEvent e) 
	{
		
		if (e.getSource().equals(this.btnChanges))
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("http://www.tubemaster.net/changelog.html"));
			} catch (Exception e1) {Commun.logError(e1);} 			
		}
		else
		if (e.getSource().equals(this.btnLicense))
		{
			if (this.btnLicense.getText().equals("GNU License"))
			{
				this.btnLicense.changeText("Contributors");
				this.btnLicense.changeImage("contributors.png");
				this.areaGreets.setText(this.getGNULicense());
				this.lblGreets.setText("GNU General Public License : ");
				this.areaGreets.setCaretPosition(0);
			}
			else
			{
				this.btnLicense.changeText("GNU License");
				this.btnLicense.changeImage("license.png");			
				this.areaGreets.setText(this.getContributors());
				this.lblGreets.setText("Contributors : ");
				this.areaGreets.setCaretPosition(0);
			}
			
		}
		else
		if (e.getSource().equals(this.btnWebsite))
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("http://www.tubemaster.net"));
			} catch (Exception e1) {Commun.logError(e1);} 
			
		}	
	}
	
	
	public String getContributors()
	{
		return "- WinPcap and LibPcap contributors.\n" +
		"- Keita Fujii : JPcap library.\n" +
		"- FFMpeg Project : FFMpeg & FFPlay tools.\n" +
		"- Iconspedia.com : Some nice icons.\n" +
		"- Smaxe Ltd : RTMP Protocol Support.\n" + 
		"- Paul Grebenc : JID3 Library.\n" + 
		"- WinFF Project : Conversion presets.\n" +
		"- Other Contributions (Donators, Bug reporters ...).\n" + 
		"- Worm00 : Graphic and Code contribution.\n" + 
		"- Cocodidou : Unix version (help, tests).\n" +
		"- Kiseong Kim : Korean Translation.\n" +
		"- Dr.M.ReDa & Mr On Line : Arabic Translation.\n" +
		"- JohannesTN : Danish Translation.\n" +
		"- Juan Pablo Zapata : Spanish (Argentina) Translation.\n" + 
		"- Oscar Nicolas : Catalan Translation.\n" +  
		"- Enzo Musella & Dav91 : Italian Translation.\n" +
		"- BeGe : Swedish Translation.\n" +
		"- Joakim Lazakis : Greek Translation.\n" +
		"- Diego F. M. Delgado &  Juan Calderon R. : Spanish Translation.\n" + 
		"- Li Lindun : Chinese (Traditional & Simplified) Translation.\n" +
		"- Matheus Lutkmeier : Brazilian Translation.\n" + 
		"- Dejan Omasta : Bosnian Translation.\n" +
		"- Gabor Karikas : Hungarian Translation.\n" +
		"- Artem Kashkanov & Ihor Polishchuk : Russian Translation.\n" +
		"- Nguyen Quang Vinh : Vietnamese Translation.\n" + 
		"- Jakub Gal : Slovak Translation.\n" +
		"- Mollux67 : Elsassisch Translation.\n" +
		"- Enver Can Cilingir : Turkish Translation.\n" +
		"- Artur Penedo : Portuguese Translation.\n" + 
		"- Arjan Lindeboom : Dutch Translation.\n" + 
		"- Markus Grasel & Fabian K. : German Translation.\n" +
		"- Nicolas Claverie : Japanese Translation.\n" +
		"- Ihor Polishchuk (Amigor) : Ukrainian Translation.\n" +
		"- Junio Soares Nascimento : Português (Brasil) Translation.\n" +
		"- Peter Korman : Czech & Slovak Translations.\n" +
		"- Pentti Virmavaara : Finnish Translation.";
	}
	
	public String getGNULicense()
	{
		return "GNU GENERAL PUBLIC LICENSE\n\n"+
				"Version 3, 29 June 2007\n\n"+
				"Copyright © 2007 Free Software Foundation, Inc. <http://fsf.org/>\n\n"+
				"Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is not allowed.\n\n"+
				"*Preamble*\n\n"+
				"The GNU General Public License is a free, copyleft license for software and other kinds of works.\n\n"+
				"The licenses for most software and other practical works are designed to take away your freedom to share and change the works. By contrast, the GNU General Public License is intended to guarantee your freedom to share and change all versions of a program--to make sure it remains free software for all its users. We, the Free Software Foundation, use the GNU General Public License for most of our software; it applies also to any other work released this way by its authors. You can apply it to your programs, too.\n\n"+
				"When we speak of free software, we are referring to freedom, not price. Our General Public Licenses are designed to make sure that you have the freedom to distribute copies of free software (and charge for them if you wish), that you receive source code or can get it if you want it, that you can change the software or use pieces of it in new free programs, and that you know you can do these things.\n\n"+
				"To protect your rights, we need to prevent others from denying you these rights or asking you to surrender the rights. Therefore, you have certain responsibilities if you distribute copies of the software, or if you modify it: responsibilities to respect the freedom of others.\n\n"+
				"For example, if you distribute copies of such a program, whether gratis or for a fee, you must pass on to the recipients the same freedoms that you received. You must make sure that they, too, receive or can get the source code. And you must show them these terms so they know their rights.\n\n"+
				"Developers that use the GNU GPL protect your rights with two steps: (1) assert copyright on the software, and (2) offer you this License giving you legal permission to copy, distribute and/or modify it.\n\n"+
				"For the developers\' and authors\' protection, the GPL clearly explains that there is no warranty for this free software. For both users' and authors' sake, the GPL requires that modified versions be marked as changed, so that their problems will not be attributed erroneously to authors of previous versions.\n\n"+
				"Some devices are designed to deny users access to install or run modified versions of the software inside them, although the manufacturer can do so. This is fundamentally incompatible with the aim of protecting users' freedom to change the software. The systematic pattern of such abuse occurs in the area of products for individuals to use, which is precisely where it is most unacceptable. Therefore, we have designed this version of the GPL to prohibit the practice for those products. If such problems arise substantially in other domains, we stand ready to extend this provision to those domains in future versions of the GPL, as needed to protect the freedom of users.\n\n"+
				"Finally, every program is threatened constantly by software patents. States should not allow patents to restrict development and use of software on general-purpose computers, but in those that do, we wish to avoid the special danger that patents applied to a free program could make it effectively proprietary. To prevent this, the GPL assures that patents cannot be used to render the program non-free.";
	}

}

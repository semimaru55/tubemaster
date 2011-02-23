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

package Graphique;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Main.Commun;
import Main.FenAbout;
import Main.FenOptions;
import Main.MainForm;


public class Header extends JPanel implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private JLabel lblVersion = new JLabel();
	
	private TabbedButton tab1 = new TabbedButton(MainForm.lang.lang_table[3],"tabCapture.png");
	private TabbedButton tab2 = new TabbedButton(MainForm.lang.lang_table[4],"tabVideo.png");
	private TabbedButton tab3 = new TabbedButton(MainForm.lang.lang_table[5],"tabMusic.png");
	private JPanel pan1;
	private JPanel pan2;
	private JPanel pan3;
	
	private TMButton btnOptions = new TMButton(this,45,6,MainForm.lang.lang_table[0],"options.png",2,38,116);
	private TMButton btnDonate = new TMButton(this,45,6,MainForm.lang.lang_table[1],"donate.png",2,38,116);
	private TMButton btnAbout = new TMButton(this,45,6,MainForm.lang.lang_table[2],"about.png",2,38,116);
	
	private Image fond = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"Header.png"));
	
	private FenOptions opts = new FenOptions();

	
	public Header(JPanel pan1 ,JPanel pan2, JPanel pan3)
	{
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(705,110));
		this.setDoubleBuffered(true);
		this.setBackground(Color.decode("#676767"));

		this.pan1 = pan1;
		this.pan2 = pan2;
		this.pan3 = pan3;
		
		this.lblVersion.setFont(Commun.tm_font11i);
		this.lblVersion.setText("Version "+MainForm.tm_version+" - OpenSource");
		this.lblVersion.setForeground(Color.LIGHT_GRAY);
		
		this.tab1.setLocation(190,79);
		this.tab1.setActive(true);
		this.tab2.setLocation(360,79);
		this.tab3.setLocation(530,79);
		this.lblVersion.setBounds(105,43,150,25);
		
		
		this.btnOptions.setBounds(331,10,120,60);
		this.btnOptions.setFlat();
		
		this.btnAbout.setBounds(575,10,120,60);
		this.btnAbout.setFlat();
		
		this.btnDonate.setBounds(453,10,120,60);
		this.btnDonate.setFlat();
		
		
		this.add(this.lblVersion);
		this.add(this.tab1);
		this.add(this.tab2);
		this.add(this.tab3);
		this.add(this.btnOptions);
		this.add(this.btnAbout);	
		this.add(this.btnDonate);

	}
	
	
	
	//=====================================================================================================

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.black);		
		g.drawImage(this.fond, 0, 0, this);
		g.drawLine(0, 109, 705, 109);
	}

	//=====================================================================================================
	
	
	public void refreshTabs(TabbedButton clicked)
	{
		if (clicked.equals(this.tab1))
		{
			this.tab1.setActive(true);
			this.tab2.setActive(false);
			this.tab3.setActive(false);
			this.pan1.setVisible(true);
			this.pan2.setVisible(false);
			this.pan3.setVisible(false);
		}
		else
		if (clicked.equals(this.tab2))
		{
			this.tab1.setActive(false);
			this.tab2.setActive(true);
			this.tab3.setActive(false);
			this.pan1.setVisible(false);
			this.pan2.setVisible(true);
			this.pan3.setVisible(false);
		}
		else
		if (clicked.equals(this.tab3))
		{
			this.tab1.setActive(false);
			this.tab2.setActive(false);
			this.tab3.setActive(true);
			this.pan1.setVisible(false);
			this.pan2.setVisible(false);
			this.pan3.setVisible(true);
		}
	}


	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals((this.btnOptions))) this.opts.setVisible(true);
		else
		if (e.getSource().equals(this.btnDonate))
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ggsofts%40gmail%2ecom&item_name=TM%2B%2B%20Developpement&item_number=Don&no_shipping=0&no_note=1&tax=0&currency_code=EUR&lc=FR&bn=PP%2dDonationsBF&charset=UTF%2d8"));
			} catch (Exception e1) {Commun.logError(e1);} 
		}
		else
		if (e.getSource().equals((this.btnAbout))) new FenAbout();
		
	}
	
	

}

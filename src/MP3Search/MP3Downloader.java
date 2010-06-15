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

package MP3Search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import Main.MainForm;



public class MP3Downloader extends JFrame implements ActionListener, WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private static final int LIST_WIDTH = 500;					
	private static final int LIST_HEIGHT = 186;
	
	private JPanel panFen = new JPanel(); //Panel de la fenetre.
	private JPanel laListe = new JPanel();
	
	private JScrollPane leScroll = new JScrollPane(this.laListe);
	private Timer timerRefresh = new Timer(500,this);
	
	public MP3Downloader()
	{
		
		this.panFen.setLayout(new BorderLayout());
		this.setTitle("MP3 Downloader");
		this.setMinimumSize(new Dimension(LIST_WIDTH-100, LIST_HEIGHT));
		this.setSize(new Dimension(LIST_WIDTH, LIST_HEIGHT));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon monIcon= new ImageIcon(getClass().getResource("images/"+"mp3icon.jpg"));
		this.setIconImage(monIcon.getImage());	
		this.setContentPane(this.panFen);
		this.addWindowListener(this);

		this.leScroll.setOpaque(false);
		this.leScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


		this.laListe.setLayout(new BoxLayout(this.laListe,1));
		this.laListe.setBackground(Color.white);
		
		this.timerRefresh.setRepeats(true);

		this.panFen.add(this.leScroll);	
		
	}
	
	public void bigRefresh()
	{
		this.laListe.revalidate();
		this.laListe.repaint();
		
		if (this.laListe.getComponentCount()==0) 
		{
			this.timerRefresh.stop();
			this.setVisible(false);
		}		
	}
	
	public void ajoutItem(ListMP3Item item)
	{
		this.laListe.add(item);
		this.bigRefresh();
		this.setVisible(true);
		this.timerRefresh.start();
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(this.timerRefresh))
		{
			boolean needRefresh = false;
			//On cherche les éléments à supprimer.
			for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
				if (!((ListMP3Item)this.laListe.getComponent(i)).isAlive()) 
				{
					this.laListe.remove(i);
					needRefresh = true;
				}
			if (needRefresh) this.bigRefresh();
			
			
		}
		
	}
	
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}


	public void windowClosing(WindowEvent e) 
	{
		if (this.laListe.getComponentCount()>0)
		{
		
			if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table[42], MainForm.lang.lang_table[37],
					JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			{
	
				this.setVisible(false);
				this.timerRefresh.stop();
				for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
				{
					((ListMP3Item)this.laListe.getComponent(i)).stopDown(); 
					this.laListe.remove(i); 
				}
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								
			}	else this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		}
			
	}


	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	
	

}

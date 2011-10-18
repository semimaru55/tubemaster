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

package VideoSearch;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Graphique.TMButton;
import Main.Commun;
import Main.MainForm;


public class PanelVideoPresent extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private JTextArea edtVideo = new JTextArea();
	private JScrollPane paneEdit = new JScrollPane(this.edtVideo);
	private TMButton btnPlay = new TMButton(this,13,10,MainForm.lang.lang_table[36],"preview.png",32,9,115);
	
	private String currentURL = new String("");


	public PanelVideoPresent()
	{
		super();
		this.setLayout(null);
		this.setOpaque(false);
		//this.setBackground(Color.decode("#FFFFE1"));
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		this.paneEdit.setBounds(115,0,417,81);
		this.paneEdit.setBorder(BorderFactory.createLineBorder(Color.black));
		this.edtVideo.setLineWrap(true);
		this.edtVideo.setWrapStyleWord(true);
		this.edtVideo.setBackground(Color.decode("#FFFFE1"));
		this.edtVideo.setEditable(false);
		this.edtVideo.setForeground(Color.black);
		this.paneEdit.setOpaque(false);
		
		this.btnPlay.setBounds(537,40,150,40);

		//this.btnPlay.setVisible(false);
		
		
		
		
		
		this.add(this.paneEdit);
		this.add(this.btnPlay);
		
		
		
		this.setVisible(true);
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, 110, 80, this);
	}
	
	public void setImage(Image i)
	{
		this.image = i;
	}



	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			Desktop.getDesktop().browse(new URI(this.currentURL));
		} catch (Exception e1) {Commun.logError(e1);}		
	}
	
	public void refresh(String url, String desc, String img)
	{
		this.currentURL = url;
		this.edtVideo.setText(desc);
		this.edtVideo.setCaretPosition(0);
		this.btnPlay.setVisible(true);
		
		
		class ThreadImg extends Thread 
		{
			private String img = "";
			public ThreadImg(String img) {this.img = img;}

		    public void run() 
		    {
		    	URL url;
				try 
				{
					url = new URL(this.img);
					Image img = createImage((java.awt.image.ImageProducer)url.getContent());
					setImage(img);
					repaint();
				} catch (MalformedURLException e) {Commun.logError(e);	} 
				catch (IOException e) {Commun.logError(e);}
		    }    
		}
				
		ThreadImg t = new ThreadImg(img);
		t.start();	
	}
	
	

}

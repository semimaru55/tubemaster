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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JLabel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


import Graphique.ItemMP3Closer;
import Main.MainForm;



public class ListMP3Item extends JPanel
{

	private static final long serialVersionUID = 1L;
			
	private static final int ITEM_HEIGHT = 85;
	
	
	private String url = "";
	private String filetitle = "";
	private String filedir = "";
	
	private JProgressBar pbar = new JProgressBar();
	private JLabel lblTitle = new JLabel();
	private JLabel lblDir = new JLabel();
	private JLabel lblStatus = new JLabel();
	private JPanel pnlText = new JPanel();
	private JPanel pnlHaut = new JPanel();
	private JPanel pnlBas = new JPanel();
	
	private FileDownloader fdown;
	private ItemMP3Closer closer = new ItemMP3Closer(this);
	
	private JPanel dessin = new JPanel()
	{
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"mp3item.png")), 0, 0, this);
		}
	};

	public ListMP3Item(String url, String filetitle, String filedir, boolean isFromYoutube)
	{
		super();
		this.setLayout(new BoxLayout(this,1));
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
		this.setBackground(Color.white);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, ITEM_HEIGHT));
		
		this.url = url;
		this.filetitle = filetitle;
		this.filedir = filedir;
		
		
		this.placeComposants();
		
		this.fdown = new FileDownloader(this.url,this.filedir,this.filetitle,this.pbar,this.lblStatus,isFromYoutube);
		Thread threadManager = new Thread(this.fdown);
		threadManager.start();
	
	}
	
	public void placeComposants()
	{

		this.dessin.setPreferredSize(new Dimension(50,50));
		this.dessin.setMaximumSize(new Dimension(50,50));
		this.dessin.setBackground(Color.white);
		
		this.lblTitle.setForeground(Color.black);
		this.lblTitle.setFont(new Font("Default_tm", Font.BOLD, 13));
		this.lblTitle.setText(this.filetitle);
				
		this.lblDir.setForeground(Color.black);
		this.lblDir.setFont(new Font("Default_tm", 0, 11));
		this.lblDir.setText(this.filedir);


		this.lblStatus.setForeground(Color.black);
		this.lblStatus.setFont(new Font("Default_tm", Font.BOLD, 10));
		this.lblStatus.setText(MainForm.lang.lang_table.get(52)+" ...");
		

		this.closer.setPreferredSize(new Dimension(14,14));
		this.closer.setMaximumSize(new Dimension(14,14));
		this.pbar.setValue(0);
		
		this.pnlText.setLayout(new GridLayout(3,1));
		this.pnlText.setBackground(Color.white);
		this.pnlText.add(this.lblTitle);
		this.pnlText.add(this.lblDir);
		this.pnlText.add(this.lblStatus);
		
		this.pnlHaut.setLayout(new BoxLayout(this.pnlHaut,0));
		this.pnlHaut.setBackground(Color.white);
		this.pnlHaut.add(this.dessin);
		this.pnlHaut.add(this.pnlText);

		
		this.pnlBas.setLayout(new BoxLayout(this.pnlBas,0));
		this.pnlBas.setBackground(Color.white);
		this.pnlBas.add(this.pbar);
		this.pnlBas.add(this.closer);

		this.add(this.pnlHaut);
		this.add(this.pnlBas);

	}
	
	public int getHauteur() {return ITEM_HEIGHT;}
	
	public boolean isAlive() {return !this.fdown.isFinished();};
	
	public void stopDown() {this.fdown.stopDownload();}

}

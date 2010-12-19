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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Graphique.TMButton;
import Main.Commun;
import Main.MainForm;



public class PanelVideoSearch extends JPanel implements ActionListener, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	
	private String[] tabSites = {MainForm.lang.lang_table[11],"YouTube","Dailymotion","MySpace","Yahoo! Video","Google Video","ABC News","Amazon Unbox","Bloomberg","CBS News","CBS Sportsline","CHBN","CNBC","Comedy Central","Crackle","C-SPAN","CSTV","CW","AOL Music","ESPN","FOX News","FT.com","Funny or Die","KDKA Pittsburgh","KUTV Salt Lake City","MovieLink","MSN Video","MSNBC","NBA.com","NECN","NFL.com","ON Networks","Reuters","ROOTV","Seattle Seahawks","Sports Illustrated","The Sun","The Weather Channel","Warner Bros.","Zipidee","Vimeo","IFILM","VideoRigolo","metacafe","Veoh","56.com","Bebo","Megavideo","My Video","PANDORA.TV","soapbox","TU.tv","Tudou","YoQoo","RTVE.es","Revver","Forbes.com","NY1","sevenload","WCBS New York","CTV.ca","Sport1.de","sportal.de","blip.tv","Handelsblatt","KCAL/KCBS Los Angeles","MarketWatch","blogTV","Gametrailers.com","Jujunation","Myspace","ultimateGuitar","The Daily Show","AP Video","FORA.tv","France 24","Infolive.tv","KCNC Denver","Le Monde","MediaScrape","Moblogic","New York Times Video","Daily Star","U.S.News","USA TODAY","BBC News","UnCut Video","tu.tv","Nico Video","MSN France","South Park Studios","TMZ.com","Clipta","IGN","Hulu","CBS"};
	private JLabel lblTitle = new JLabel(MainForm.lang.lang_table[10] + " :");
	private JTextField edtSearch = new JTextField(); 
	private JTable gridResults;
	private DefaultTableModel tabModele = new DefaultTableModel();
	private JScrollPane paneList;
	
	private static final String enteteTableau[]= {MainForm.lang.lang_table[12],MainForm.lang.lang_table[13],MainForm.lang.lang_table[14],MainForm.lang.lang_table[15],"","",""};
	private static final int tailleColTableau[]= {320,5,40,40,0,0,0};
	
	private TMButton btnSearch = new TMButton(this,6,3,"","search.png",0,0,0);
	private PanelVideoPresent vidPres = new PanelVideoPresent();

	private JComboBox comboSites = new JComboBox(this.tabSites);
	
	private Icon loadIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/loading.gif")));
	private JLabel lblLoad = new JLabel(this.loadIcon);
	
	

	public PanelVideoSearch()
	{
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(700,493));
		this.setBackground(Color.decode("#676767"));
		this.setVisible(false);
	
		this.lblTitle.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.lblTitle.setBounds(8,2,200,20);
		this.lblTitle.setForeground(Color.white);
		
		this.edtSearch.setBounds(8,21,200,24);
		this.edtSearch.addKeyListener(this);
		
		this.btnSearch.setBounds(215,20,30,26);
		
		this.comboSites.setFont(new java.awt.Font("Tahoma", 0, 11));
		this.comboSites.setBounds(252,22,150,22);
		this.comboSites.setMaximumRowCount(20);
		
		
		this.vidPres.setBounds(8,404,688,81);
		this.vidPres.setVisible(false);
		
		this.lblLoad.setBounds(410,17,32,32);
		this.lblLoad.setVisible(false);
		
		this.installGrid();
		

		this.add(this.vidPres);
		this.add(this.lblTitle);
		this.add(this.paneList);
		this.add(this.edtSearch);
		this.add(this.btnSearch);
		this.add(this.comboSites);
		this.add(this.lblLoad);
		
	
	}

	
	public void installGrid()
	{
		this.tabModele.setColumnIdentifiers(enteteTableau);
		this.gridResults = new JTable(this.tabModele)
		{
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int vColIndex) 
			{
	            return false;
	        }
	    };

		this.gridResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.gridResults.addMouseListener(this);
		this.paneList = new JScrollPane(this.gridResults);	
		
		for (int i=0; i<tailleColTableau.length; i++) 
		{	TableColumn col = this.gridResults.getColumnModel().getColumn(i);
			col.setPreferredWidth(tailleColTableau[i]);
			
			if ((i==4) || (i==5) || (i==6))
			{
				col.setMinWidth(0);
				col.setMaxWidth(0);
			}	
			
		}

		this.paneList.setBounds(8,53,688,431);
		
		
	}


	public void actionPerformed(ActionEvent e) 
	{
		
		if (e.getSource().equals(this.btnSearch))
		{
			for(int i=this.tabModele.getRowCount()-1;i>=0;i--) this.tabModele.removeRow(i);
			new DoSearch().start();
		}
	
	}

	
	private class DoSearch extends Thread
	{
	
		public void run()
		{
			edtSearch.setEnabled(false);
			btnSearch.setEnabled(false);
			vidPres.setVisible(false);
			paneList.setSize(688,431);
			
			String query = edtSearch.getText().replaceAll(" ", "+").replaceAll("&", "%26");
	
			
			String channel = "";
			if (!((String)comboSites.getSelectedItem()).equals(MainForm.lang.lang_table[11]))
					channel = "%20channel:\"" +(String)comboSites.getSelectedItem()+'"';
			
			
			String[] appids = {"ddd6c322b47839290","1a1cdc10a489172a5",
							   "f750b2bce21b3b645","eb2878d5ba2559a30",
							   "c6d253dfddb9ecdf7","22314468a76c407b9",
							   "ae6a7e306503e03bb","d2c23d487bd7ff733",
							   "52241d91273dd8a76","257cca8378384e02f",
							   "0fd85d09cabc261c8","490aacc5dca94eb5f"};
			
			
			int sec_count = 0;		
			
			Random randomGenerator = new Random();
			int nbr = 200;
			int found=0;
			while(found<nbr)
			{
				CountDownLatch sema = new CountDownLatch(1);
				int ran = randomGenerator.nextInt(12);	
				XMLVideoWebSearch search = new XMLVideoWebSearch("http://xml.truveo.com/apiv3?appid="+appids[ran]+"&method=truveo.videos.getVideos&query="+query+channel+"&results=50&start="+found+"&formats=flash&showAdult=1",tabModele,lblLoad,sema);
				Thread threadManager = new Thread(search);
				threadManager.start();
				found += 50;
	
				try 
				{
					sema.await(2, TimeUnit.SECONDS);
				} catch (InterruptedException e) {Commun.logError(e);}
				
				//Check 403 error.
				if (search.is_error()) 
				{
					found -= 50;
					sec_count++;
				}
				
				if (sec_count > 10) break;
				
			}
	
			btnSearch.setEnabled(true);
			edtSearch.setEnabled(true);
		}
	}

	
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) 
	{
	
		if (e.getSource().equals(this.edtSearch))
		{
			if (e.getKeyCode()==10) 
			{
				for(int i=this.tabModele.getRowCount()-1;i>=0;i--) this.tabModele.removeRow(i);
				new DoSearch().start();			
			}
		}
		
	}
	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}


	public void mouseReleased(MouseEvent e) 
	{
		if (e.getButton()==MouseEvent.BUTTON1)
		{
			this.paneList.setSize(688,343);
			this.vidPres.setVisible(true);
			this.vidPres.refresh(this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 5).toString(), 					//Le titre
								 "Description : "+this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 4).toString(), //La description
								 gridResults.getValueAt(gridResults.getSelectedRow(), 6).toString());							//Le thumb

		}
		
	}
	
	

	
}

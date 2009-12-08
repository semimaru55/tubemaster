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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;


import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


import Capture.PanelCapture;
import Graphique.TMButton;
import Main.Commun;
import Main.MainForm;


public class PanelMP3Search extends JPanel implements ActionListener, KeyListener, FocusListener
{
	private static final long serialVersionUID = 1L;
	
	
	private JLabel lblTitle = new JLabel(MainForm.lang.lang_table.get(16)+" :");
	private JTextField edtSearch = new JTextField();; 
	private JTable gridResults;
	private DefaultTableModel tabModele = new DefaultTableModel();
	private JScrollPane paneList;
	
	private static final String enteteTableau[]= {MainForm.lang.lang_table.get(17),MainForm.lang.lang_table.get(66),""};
	private static final int tailleColTableau[]= {200,200,0};
	
	private TMButton btnSearch = new TMButton(this,6,3,"","search.png",0,0,0);
	private TMButton btnDown = new TMButton(this,6,4,MainForm.lang.lang_table.get(18),"downbutton.png",38,10,117);
	//private TMButton btnPlay = new TMButton(this,5,4,"","playmp3.png",0,0,0);
	
	
	
	private Icon loadIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/loading.gif")));
	private JLabel lblLoad = new JLabel(this.loadIcon);
	
	private PanelCapture capture;
	private boolean searchActive = false;
	

	private XMLMP3WebSearch search;

	public PanelMP3Search(PanelCapture capture)
	{
		super();
		this.setLayout(null);
		this.setBounds(2,110,700,441);
		this.setBackground(Color.decode("#676767"));
		this.setVisible(false);
		
		this.capture = capture;
	
		this.lblTitle.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.lblTitle.setBounds(8,2,200,20);
		this.lblTitle.setForeground(Color.white);
		
		this.edtSearch.setBounds(8,21,200,24);
		this.edtSearch.addKeyListener(this);
		
		this.btnSearch.setBounds(215,20,30,26);
		
		this.btnDown.setBounds(533,8,160,40);
		this.btnDown.setVisible(false);
		
		//this.btnPlay.setBounds(485,8,42,40);
		//this.btnPlay.setVisible(false);
	
		
		this.lblLoad.setBounds(250,17,32,32);
		this.lblLoad.setVisible(false);
		

		this.installGrid();
		

		this.add(this.lblTitle);
		this.add(this.paneList);
		this.add(this.edtSearch);
		this.add(this.btnSearch);
		this.add(this.btnDown);
		//this.add(this.btnPlay);
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

	    this.gridResults.getTableHeader().setReorderingAllowed(false);
		this.gridResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.gridResults.addFocusListener(this);
		this.paneList = new JScrollPane(this.gridResults);	
		
		for (int i=0; i<tailleColTableau.length; i++) 
		{	TableColumn col = this.gridResults.getColumnModel().getColumn(i);
			col.setPreferredWidth(tailleColTableau[i]);
			
			if (i==2)
			{
				col.setMinWidth(0);
				col.setMaxWidth(0);
			}
			else
			if (i==1)
			{
				col.setMinWidth(0);
				col.setMaxWidth(100);
			}
			
		}

		this.paneList.setBounds(8,53,685,390);	
	}
	

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(this.btnSearch))
		{
			if (this.searchActive) this.search.shutup();
			else
			{
				for(int i=this.tabModele.getRowCount()-1;i>=0;i--) this.tabModele.removeRow(i);
				this.doSearch();
			}	
		}	
		else
		{
			if (this.capture.isCapAlive())
			{
				if (JOptionPane.showConfirmDialog(this, MainForm.lang.lang_table.get(53), MainForm.lang.lang_table.get(54),
						JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
				{
					this.capture.capManager(true);
				}
			}
			
			if (e.getSource().equals(this.btnDown))
			{
				
				String url = (String) this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 2);
				String title = (String) this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 0);
				
				boolean isFromYoutube = false;
				if (url.startsWith("http://www.youtube.com")) 
				{
					isFromYoutube = true;
					url = this.getYoutubeUrl(url);
					
				}
				String rep = MainForm.opts.defRep; 
				if (rep.equals("")) rep = this.dirChooser();
				if (!rep.equals("")) MainForm.mp3down.ajoutItem(new ListMP3Item(url,title+".mp3",rep,isFromYoutube));
			}
		}
	}

	public void doSearch()
	{		
		String query = this.edtSearch.getText();
		if (!query.equals(""))
		{
			this.search = new XMLMP3WebSearch(query,this.tabModele,this);		
			Thread threadManager = new Thread(search);
			threadManager.start();
		}
	}

	public void keyTyped(KeyEvent arg0) {}
	public void keyPressed(KeyEvent arg0) {}
	public void keyReleased(KeyEvent e) 
	{
		if (e.getSource().equals(this.edtSearch))
		{
			if (e.getKeyCode()==10) 
			{
				for(int i=this.tabModele.getRowCount()-1;i>=0;i--) this.tabModele.removeRow(i);
				this.doSearch();			
			}
		}	
	}


	public void focusGained(FocusEvent e) 
	{
		this.btnDown.setVisible(true);
	}

	public void focusLost(FocusEvent e)
	{
		this.btnDown.setVisible(false);
	}

	public String dirChooser()
	{
		String ret = "";
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
            File file = fc.getSelectedFile();
            ret = file.getAbsolutePath();    	
		}		
		return ret;		
	}

	public void startSearch()
	{
		this.searchActive = true;
		this.lblLoad.setVisible(true);
		this.btnSearch.changeImage("stopconv.png");
			
	}
	
	public void stopSearch()
	{
		this.searchActive = false;
		this.lblLoad.setVisible(false);
		this.btnSearch.changeImage("search.png");		
	}
		
	public String getYoutubeUrl(String addr)
	{
		try
		{
			URL link = new URL(addr);
			HttpURLConnection yc = (HttpURLConnection) link.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String total = "";

			while ((inputLine = in.readLine()).indexOf("SWF_GAM_URL")==-1) total += inputLine;
			total += inputLine;
			
			String urls = Commun.parse(total, "\"fmt_url_map\"", "\"keywords\"");
			urls = URLDecoder.decode(urls, "UTF-8");
			
			String deb = "";
			if (urls.indexOf("18|")>-1) deb = "18|";
			else if (urls.indexOf("34|")>-1) deb = "34|";
			else if (urls.indexOf("5|")>-1) deb = "5|";
						
			urls = Commun.parse(urls, deb, ",");
			urls = urls.replaceAll("\"", "");
				
			return urls;
		}
		catch (Exception e) {Commun.logError(e); return "";}
	}


	
	
	

	
}
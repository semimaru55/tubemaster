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
import java.net.URL;
import java.net.URLConnection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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


public class PanelMP3Search extends JPanel implements ActionListener, KeyListener, FocusListener
{
	private static final long serialVersionUID = 1L;
	
	
	private JLabel lblTitle = new JLabel(MainForm.lang.lang_table[16]+" :");
	private JTextField edtSearch = new JTextField();; 
	private JTable gridResults;
	private DefaultTableModel tabModele = new DefaultTableModel();
	private JScrollPane paneList;
	
	private static final String enteteTableau[]= {MainForm.lang.lang_table[17],MainForm.lang.lang_table[66],""};
	private static final int tailleColTableau[]= {200,200,0};
	
	private TMButton btnSearch = new TMButton(this,6,3,"","search.png",0,0,0);
	private TMButton btnDown = new TMButton(this,6,4,MainForm.lang.lang_table[18],"downbutton.png",38,10,117);

	
	
	private Icon loadIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/loading.gif")));
	private JLabel lblLoad = new JLabel(this.loadIcon);

	private boolean searchActive = false;
	

	private XMLMP3WebSearch search;

	public PanelMP3Search()
	{
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(700,493));
		this.setBackground(Color.decode("#676767"));
		this.setVisible(false);
	
		this.lblTitle.setFont(Commun.tm_font11);
		this.lblTitle.setBounds(8,2,200,20);
		this.lblTitle.setForeground(Color.white);
		
		this.edtSearch.setBounds(8,21,200,24);
		this.edtSearch.addKeyListener(this);
		
		this.btnSearch.setBounds(215,20,30,26);
		
		this.btnDown.setBounds(533,8,160,40);
		this.btnDown.setVisible(false);
	
		
		this.lblLoad.setBounds(250,17,32,32);
		this.lblLoad.setVisible(false);
		

		this.installGrid();
		

		this.add(this.lblTitle);
		this.add(this.paneList);
		this.add(this.edtSearch);
		this.add(this.btnSearch);
		this.add(this.btnDown);
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

		this.paneList.setBounds(8,53,688,431);	
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
			
			if (e.getSource().equals(this.btnDown))
			{
				
				String url = (String) this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 2);
				String title = (String) this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 0);
				
				if (url.contains("wrzuta.pl")) 
				{
					url = this.get_wrzuta_url(url);
					if (url.contains("CDATA")) url = Commun.parse(url, "<![CDATA[", "]]>");
				}

				String rep = MainForm.opts.defRep; 
				if (rep.equals("")) rep = this.dirChooser();
				if (!rep.equals("")) MainForm.mp3down.ajoutItem(new ListMP3Item(url,title+".mp3",rep));
			}
		}
	}
	
	
	private String get_wrzuta_url(String xml)
	{
		try
		{
			URL go = new URL(xml);
			URLConnection yc = go.openConnection();
			yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; fr; rv:1.9.2) Gecko/20100115 Firefox/3.6");
						
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String total = "";
			
			while ((inputLine = in.readLine()) != null) total += inputLine;	
			
			return Commun.parse(total, "<fileId>", "</fileId>");
		
		} catch (Exception e) {return "";}
		
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
		

	

	
}
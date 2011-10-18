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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

	private JLabel lblTitle = new JLabel(MainForm.lang.lang_table[10] + " :");
	private JTextField edtSearch = new JTextField(); 
	private JTable gridResults;
	private DefaultTableModel tabModele = new DefaultTableModel();
	private JScrollPane paneList;
	
	private static final String enteteTableau[]= {MainForm.lang.lang_table[12],MainForm.lang.lang_table[13],MainForm.lang.lang_table[15],"","",""};
	private static final int tailleColTableau[]= {320,5,40,0,0,0};
	
	private TMButton btnSearch = new TMButton(this,6,3,"","search.png",0,0,0);
	private PanelVideoPresent vidPres = new PanelVideoPresent();


	private Icon loadIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/loading.gif")));
	private JLabel lblLoad = new JLabel(this.loadIcon);
	
	

	public PanelVideoSearch()
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
		
		JPopupMenu rightMenu = new JPopupMenu();
		rightMenu.add(MainForm.lang.lang_table[85]).addActionListener(new ActionListener() 
		{
		         public void actionPerformed(ActionEvent e) 
		         {
		        	 edtSearch.copy();
		         }
		      });
		rightMenu.add(MainForm.lang.lang_table[86]).addActionListener(new ActionListener() 
		{
		         public void actionPerformed(ActionEvent e) 
		         {
		        	 edtSearch.paste();
		         }
		 });
		this.edtSearch.setComponentPopupMenu(rightMenu);
		
		this.btnSearch.setBounds(215,20,30,26);
		
		
		this.vidPres.setBounds(8,404,688,81);
		this.vidPres.setVisible(false);
		
		this.lblLoad.setBounds(248,17,32,32);
		this.lblLoad.setVisible(false);
		
		this.installGrid();
		

		this.add(this.vidPres);
		this.add(this.lblTitle);
		this.add(this.paneList);
		this.add(this.edtSearch);
		this.add(this.btnSearch);
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
	    
	    this.gridResults.setAutoCreateRowSorter(true);
	    this.gridResults.getTableHeader().setReorderingAllowed(false);
		this.gridResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.gridResults.addMouseListener(this);
		this.paneList = new JScrollPane(this.gridResults);	
		
		for (int i=0; i<tailleColTableau.length; i++) 
		{	TableColumn col = this.gridResults.getColumnModel().getColumn(i);
			col.setPreferredWidth(tailleColTableau[i]);
			
			if ((i==3) || (i==4) || (i==5))
			{
				col.setMinWidth(0);
				col.setMaxWidth(0);
			}	
			
		}

		this.paneList.setBounds(8,53,688,431);
	
	}

	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getSource());
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
	
			int found=0;
			for (int i=0;i<10;i++)
			{
				String page = ""+i+1;
				CountDownLatch sema = new CountDownLatch(1);					
				XMLVideoWebSearch search = new XMLVideoWebSearch("http://www.tubemaster.net/video_search.php?q="+query+"&p="+page,tabModele,lblLoad,sema);
				Thread threadManager = new Thread(search);
				threadManager.start();
				found += 20;
	
				try 
				{
					sema.await(2, TimeUnit.SECONDS);
				} catch (InterruptedException e) {Commun.logError(e);}
							
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
			this.vidPres.refresh(this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 4).toString(), 					//Le titre
								 "Description : "+this.gridResults.getValueAt(this.gridResults.getSelectedRow(), 3).toString(), //La description
								 gridResults.getValueAt(gridResults.getSelectedRow(), 5).toString());							//Le thumb

		}
		
	}


	
}

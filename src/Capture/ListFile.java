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

package Capture;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import Main.MainForm;



public class ListFile extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	
	private static final int LIST_WIDTH = 698;					//Largeur de la liste.
	private static final int LIST_HEIGHT = 403;					//Hauteur de la liste.	
	
	private JPanel laListe = new JPanel()						//Panel de la liste.
	{
		private static final long serialVersionUID = 1L;

		private Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"how.png"));
		
		
		
		public void paintComponent(Graphics g)
		{

			super.paintComponent(g);
			if (this.getComponentCount()==0) 
			{
				g.drawImage(this.img, 60, 45, this);
				g.setColor(Color.gray.darker().darker());
				g.drawString(MainForm.lang.lang_table[57], 3, 392);
				
			}
		}
			
	};

	private JScrollPane leScroll = new JScrollPane(laListe);	//Scroll de la liste.
	

	
	
	//=====================================================================================================
	
	public ListFile()
	{
		super();
		this.setLayout(null);
		Border bord = BorderFactory.createLineBorder(Color.darkGray);
		this.leScroll.setBorder(bord);
		this.setSize(new Dimension((LIST_WIDTH),LIST_HEIGHT));
		
		this.setOpaque(false);
		this.leScroll.setOpaque(false);
	
		
		this.leScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.leScroll.setBounds(2,2,LIST_WIDTH-4,LIST_HEIGHT-4);
		
		this.laListe.setPreferredSize(new Dimension(LIST_WIDTH-30,LIST_HEIGHT-30));
		this.laListe.setBackground(Color.decode("#9D9D9D"));

		this.setTransferHandler(new MyTransferHandler());
		
		this.add(this.leScroll);	

	}
	
	//=====================================================================================================
	

	//=====================================================================================================
	
	public void bigRefresh()
	{
		//On cherche les éléments à supprimer.
		for (int i=0; i<this.laListe.getComponentCount();i++)
			if (!((ListFileItem)this.laListe.getComponent(i)).isAlive()) this.laListe.remove(i);
	
		//On rafraichit et calcule les bonnes dimensions.
		this.laListe.revalidate();
		this.laListe.repaint();
		int size = 0;
		for (int i=0; i<this.laListe.getComponentCount();i++)
			size += ((ListFileItem)this.laListe.getComponent(i)).getHauteur()+5;

		size += 7;
		
		if (size <= LIST_HEIGHT-30) size = LIST_HEIGHT-30;
		this.laListe.setPreferredSize(new Dimension(LIST_WIDTH-30,size));
		
	}
	
	//=====================================================================================================
	
	public void ajoutItem(ListFileItem item)
	{
		this.laListe.add(item);
		this.bigRefresh();
		this.revalidate();
	}
	
	//=====================================================================================================
	
	public void checkAll()
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
			((ListFileItem)this.laListe.getComponent(i)).checkIt();		
	}
	
	public void unCheckAll()
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
			((ListFileItem)this.laListe.getComponent(i)).unCheckIt();		
	}
	
	public void clearAll()
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
			((ListFileItem)this.laListe.getComponent(i)).toDestroy();		
	}
	
	public void reduceAll()
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
			((ListFileItem)this.laListe.getComponent(i)).reduceFromAll();		
	}
	
	public void maximizeAll()
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
			((ListFileItem)this.laListe.getComponent(i)).maximizeFromAll();		
	}
	
	public void saveAll(String dir)
	{		
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
		{
			ListFileItem item = ((ListFileItem)this.laListe.getComponent(i));
			if ((item.isFullReady()) && (item.isChecked()))
			{
				if (dir.equals("")) dir = this.dirChooser();
				if (!dir.equals("")) item.runConversionSave(dir);
			}
				
		}
					
	}
	
	public void convertAllToPreset(String dir, String preset)
	{
		for (int i=0; i<this.laListe.getComponentCount();i++)
		{
			ListFileItem item = ((ListFileItem)this.laListe.getComponent(i));
			if ((item.isFullReady()) && (item.isChecked()))
			{
				if (dir.equals("")) dir = this.dirChooser();
				if (!dir.equals("")) item.runConversionPreset(preset, dir);
			}
				
		}
					
	}
	
	public void extractAllMP3(String dir)
	{
		for (int i=this.laListe.getComponentCount()-1; i>=0;i--)
		{
			ListFileItem item = ((ListFileItem)this.laListe.getComponent(i));
			if ((item.isFullReady()) && (item.isChecked()))	
			{
				if (dir.equals("")) dir = this.dirChooser();
				if (!dir.equals("")) item.runConversionExtractMP3(dir);
			}
				
		}				
	}
	
	public void convertAllClassic(String dir, String preset)
	{
		for (int i=0; i<this.laListe.getComponentCount();i++)
		{
			ListFileItem item = ((ListFileItem)this.laListe.getComponent(i));
			if ((item.isFullReady()) && (item.isChecked()))
			{
				if (dir.equals("")) dir = this.dirChooser();
				if (!dir.equals("")) item.runConversionClassic(dir, preset);
			}
				
		}				
	}
	
	
	
	//=====================================================================================================
	
	public ListFileItem getItem(int indice) {return (ListFileItem) this.laListe.getComponent(indice);}
	public int getItemsCount() {return this.laListe.getComponentCount();}
	
	public String dirChooser()
	{
		String ret = "";
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
            File file = fc.getSelectedFile();
            ret = file.getAbsolutePath();    	
		}		
		return ret;		
	}

	//=====================================================================================================
	
	
	public void fileDragged(File file)
	{
		 String extension;
         int dotPos = file.getAbsolutePath().lastIndexOf(".")+1;
         extension = file.getAbsolutePath().substring(dotPos).toUpperCase();
         
         if ((extension.equals("FLV")) || 
             (extension.equals("MP3")) || 
             (extension.equals("MP4")) ||
             (extension.equals("MOV")))
         {
         	CapturedFile capfile = new CapturedFile(new FileFormat(extension),-1);
         	capfile.setFilename(file.getAbsolutePath());
         	capfile.setCap_FileSize((int)file.length());
         	capfile.setCap_Size((int)file.length());
         	capfile.processToEnd();
         	ListFileItem item = new ListFileItem(this,capfile,file.getAbsolutePath());
         	item.setDragged(true);
         	item.setTitle(file.getName().substring(0, file.getName().lastIndexOf(".")));
         	this.ajoutItem(item);
         	
         }
		
	}
	

	private class MyTransferHandler extends TransferHandler 
	{
	    private static final long serialVersionUID = 1L;

	    public boolean canImport(JComponent cp, DataFlavor[] df) {
	        for (int i = 0; i < df.length; i++) {
	        	if ((df[i].equals(DataFlavor.javaFileListFlavor)) 
	    	        	|| df[i].equals(DataFlavor.stringFlavor))
	        	{
	            return true;
	          }
	        }
	        return false;
	      }

		public boolean importData(JComponent cp, Transferable tr) 
	    {
	        try 
	        {
	          List<File> files = getFilesFromTransferable(tr);
	          for (int i = 0; i < files.size(); i++) 
	          {
	            fileDragged(files.get(i));
	          }
	          return true;
	        }
	        catch (Exception e) {}
	      return false;
	    }  
	  }
	

		@SuppressWarnings("unchecked")
		private static List<File> getFilesFromTransferable(Transferable t) 
		{

	       try 
	       {
	           if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
	           {
	               return (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
	           }
	           if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) 
	           { // Linux

	               String urls = (String) t.getTransferData(DataFlavor.stringFlavor);
	               List<File> files = new LinkedList();
	               StringTokenizer tokens = new StringTokenizer(urls);
	               while (tokens.hasMoreTokens()) 
	               {
	                   String urlString = tokens.nextToken();
	                   URL url = new URL(urlString);
	                   files.add(new File(URLDecoder.decode(url.getFile(), "UTF-8")));
	               }
	               return files;
	           }
	       } catch (Exception e) {}
	       return null;
	   }
	

}

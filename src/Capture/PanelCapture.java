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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Conversion.ConvertMenu;
import Graphique.TMButton;
import Main.Commun;
import Main.MainForm;



public class PanelCapture extends JPanel implements ActionListener
{
	private ListFile laListe = new ListFile();
	private CaptureSystem capture;
	
	private static final long serialVersionUID = 1L;

	
	private boolean capAlive;
	private boolean reduState = false;
	private boolean checkState = false;
	
	private TMButton btnCapture;
	private TMButton btnSaveAll = new TMButton(this,7,4,MainForm.lang.lang_table[8],"saveall.png",41,9,140);
	private TMButton btnConvertAll = new TMButton(this,7,4,MainForm.lang.lang_table[9],"convertall.png",41,9,140);
	private TMButton btnCheckAll = new TMButton(this,8,12,"","unchecked.png",0,0,0);
	private TMButton btnClearAll = new TMButton(this,8,12,"","close.png",0,0,0);
	private TMButton btnReduceAll = new TMButton(this,8,12,"","reduce.png",0,0,0);
	private ConvertMenu convMenu = new ConvertMenu(this);
	private CaptureLeds capLeds = new CaptureLeds(this);
	
	
	

	public PanelCapture()
	{
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(700,493));
		this.setBackground(Color.decode("#676767"));


		if (MainForm.opts.autoCapture)
		{
			this.btnCapture = new TMButton(this,7,4,MainForm.lang.lang_table[6],"stop_cap.png",41,9,140);
			this.capture = new CaptureSystem(this.laListe);
			this.capAlive = true;
		} else 
		{
			this.btnCapture = new TMButton(this,7,4,MainForm.lang.lang_table[7],"start_cap.png",41,9,140);
			this.capAlive = false;
		}


		this.laListe.setLocation(4, 77);
		this.btnReduceAll.setBounds(634,35,30,40);
		this.btnClearAll.setBounds(670,35,30,40);
		this.btnCapture.setBounds(6,35,185,40);
		this.btnConvertAll.setBounds(397,35,194,40);
		this.btnSaveAll.setBounds(197,35,194,40);
		this.btnCheckAll.setBounds(598,35,30,40);
		this.capLeds.setLocation(6, 5);
		
		
		
		this.add(this.laListe);
		this.add(this.btnCapture);
		this.add(this.btnReduceAll);
		this.add(this.btnClearAll);
		this.add(this.btnSaveAll);
		this.add(this.btnConvertAll);
		this.add(this.btnCheckAll);
		this.add(this.capLeds);
		
	}


	public void actionPerformed(ActionEvent e) 
	{
		
		if (e.getSource().equals(this.btnCapture))
		{
			this.capManager(this.capAlive);	
		}	
		else
		if (e.getSource().equals(this.btnReduceAll))
		{
			if(this.reduState)
			{
				this.btnReduceAll.changeImage("reduce.png");
				this.reduState = false;
				this.laListe.maximizeAll();
			}
			else
			{
				this.btnReduceAll.changeImage("maximize.png");
				this.reduState = true;
				this.laListe.reduceAll();
			}
		}	
		else
		if (e.getSource().equals(this.btnCheckAll))
		{
			if(this.checkState)
			{
				this.btnCheckAll.changeImage("unchecked.png");
				this.checkState = false;
				this.laListe.unCheckAll();
			}
			else
			{
				this.btnCheckAll.changeImage("checked.png");
				this.checkState = true;
				this.laListe.checkAll();
			}
		}	
		else
		if (e.getSource().equals(this.btnClearAll))
		{
			if (this.laListe.getItemsCount()>0)
			{
				if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table[39], MainForm.lang.lang_table[37],
				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
					this.laListe.clearAll();
			}
		}	
		else
		if (e.getSource().equals(this.btnSaveAll))
		{
			if (this.laListe.getItemsCount()>0) this.laListe.saveAll(MainForm.opts.defRep);

		}
		else
		if (e.getSource().equals(this.btnConvertAll))
		{
			if (this.laListe.getItemsCount()>0)
			{
				this.convMenu.show(this.btnConvertAll,this.btnConvertAll.getLastX(),this.btnConvertAll.getLastY());	
			}
		}
		else
		if (e.getActionCommand().equals("*Menu_ConvPreset*")) 
			this.laListe.convertAllToPreset(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
		else
		if (e.getActionCommand().equals("*Menu_ExtractMP3*"))
			this.laListe.extractAllMP3(MainForm.opts.defRep);
		else
		if (e.getActionCommand().equals("*Menu_Classic*")) 
			this.laListe.convertAllClassic(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
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
	
	
	
	public boolean isCapAlive() {return this.capAlive;}
	
	
	public class ThCapSwitcher extends Thread 
	{
		private boolean stop;
		private CountDownLatch sema;
		public ThCapSwitcher(boolean stop, CountDownLatch sema) {this.stop = stop; this.sema = sema;}

	    public void run() 
	    {
	    	
	    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    	try
	    	{
		    	btnCapture.setEnabled(false);
		    	
		    	if(stop)
		    	{
		    		if (capture != null) capture.shutUp();
		    		btnCapture.changeImage("start_cap.png");
		    		btnCapture.changeText(MainForm.lang.lang_table[7]);
					capAlive = false;
		    	}
		    	else
		    	{
		    		capture = new CaptureSystem(laListe);
		    		btnCapture.changeImage("stop_cap.png");
		    		btnCapture.changeText(MainForm.lang.lang_table[6]);
					capAlive = true;
		    	}
	    	
		    	btnCapture.setEnabled(true);
					
	    	}
			catch(Exception e) {Commun.logError(e);}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));			
			this.sema.countDown();	
	    }    
	}
	
	public void capManager(boolean stop)
	{
		CountDownLatch sema = new CountDownLatch(1);
		ThCapSwitcher t = new ThCapSwitcher(stop,sema);
		t.start();
		try 
		{
			sema.await(5,TimeUnit.SECONDS);
		} catch (InterruptedException e) {Commun.logError(e);}	
	}
	
	public ListFile getListe()
	{
		return this.laListe;
	}
	
	public void changeButtons(boolean b)
	{
		if (b) 
		{
			this.btnSaveAll.setEnabled(true);
		}
		else
		{
			this.btnSaveAll.setEnabled(false);
		}
	}
	
	public CaptureSystem getCaptureSystem() { return this.capture; }
	
}

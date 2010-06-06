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

package Main;



import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;




import VideoSearch.PanelVideoSearch;

import Capture.PanelCapture;

import Conversion.ConvItemManager;
import Conversion.ConversionPresets;
import Graphique.Header;
import MP3Search.PanelMP3Search;
import MP3Search.MP3Downloader;



public class MainForm extends JFrame implements WindowListener, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public static Options opts = new Options();
	public static Languages lang = new Languages();
	
	
	private JPanel panFen = new JPanel(); 										//Panel de la fenetre.
	private PanelCapture panCap = new PanelCapture();							//Panel de capture
	private PanelVideoSearch panSVideo = new PanelVideoSearch();				//Panel de recherche video
	private PanelMP3Search panSMP3 = new PanelMP3Search(this.panCap);			//Panel de recherche mp3
	

	private Header header = new Header(panCap,this.panSVideo,this.panSMP3);

	public static ConversionPresets convPresets = new ConversionPresets();
	public static ConvItemManager convManager = new ConvItemManager();
	public static MP3Downloader mp3down;
	
	public static TrayIcon trayIcon = null;
	
	public static NetworkInterface[] interfaces;
	
	public static String tm_version = "1.7";
	
	
	
	//=====================================================================================================

	public MainForm() throws IOException
	{
		super();
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#676767"));
		this.setTitle("TubeMaster++ | GgSofts");
		this.setSize(710, 594);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		this.addWindowListener(this);

		Thread threadManager = new Thread(MainForm.convManager);
		threadManager.start();
		
		
		if (opts.autoUpdate)
		{
			Thread threadManager2 = new Thread(new Updater());
			threadManager2.start();	
		}
		
		this.placeComposants();
		this.setVisible(true);

				
	}

	
	//=====================================================================================================
	
	public void placeComposants()
	{
		ImageIcon monIcon = new ImageIcon(getClass().getResource("images/icon.jpg"));
		this.setIconImage(monIcon.getImage());
		
		this.panFen.add(panCap);
		this.panFen.add(this.panSVideo);
		this.panFen.add(this.panSMP3);


		this.header.setLocation(0,0);
		this.panFen.add(this.header);

	
	}
	
	//=====================================================================================================
	

	public boolean deleteDir(File dir) 
	{
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    } 
	
	
	//=====================================================================================================
	

	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	

	
	
	public void windowClosing(WindowEvent e) 
	{
		boolean vaFermer = true;
		
		
		if (opts.closeBox)
		if (!(JOptionPane.showConfirmDialog(this, MainForm.lang.lang_table.get(47), MainForm.lang.lang_table.get(37),
				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION))
		{	
			vaFermer = false;
		}
		
		
		if (vaFermer)
		{
			this.setVisible(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			MainForm.convManager.shutUp();
			panCap.capManager(true);
			panCap.getListe().clearAll();
			File dossier = new File("temp");
			if (dossier.exists()) this.deleteDir(dossier);
			File f = new File ("Errors.log");
			if ((f.exists()) && (f.length() > 1000000)) f.delete();	
		} else this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		

		
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) 
	{
		if ((opts.trayIcon) && (SystemTray.isSupported()))
		{
			this.setVisible(false);
			ImageIcon n = new ImageIcon(getClass().getResource("images/icon.jpg"));
			trayIcon = new TrayIcon(n.getImage(),"TubeMaster++ | GgSofts");
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(this);
			trayIcon.setPopupMenu(new TrayMenu(this));
			((TrayMenu) trayIcon.getPopupMenu()).set_state(this.panCap.isCapAlive());

			SystemTray systemTray = SystemTray.getSystemTray();
			try 
			{
				systemTray.add(trayIcon);
			} catch (AWTException e) {Commun.logError(e);}
		}
	}
	public void windowOpened(WindowEvent arg0) {}

	//=====================================================================================================
	
	
	public static void main(String[] args) throws IOException
	{
		String os = System.getProperty("os.name");
		System.out.println("TubeMaster++ detected operating system : "+os);
		
		if (os.startsWith("Windows"))
			try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {} //Windows
		else	
			try {UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");} catch (Exception e) {} //Linux 
		
		try 
	    {
	    	interfaces = JpcapCaptor.getDeviceList();
	    	
	    }
	    catch (Error e)
	    {
    	
		    if (JOptionPane.showConfirmDialog(null, 
		    		MainForm.lang.lang_table.get(44)+"\n"+MainForm.lang.lang_table.get(45) 
	    			, MainForm.lang.lang_table.get(46),
	    			JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
	    	{
	    		try
	    		{
	    			Desktop.getDesktop().browse(new URI("http://www.tubemaster.net"));
	    		} catch (Exception e2) {System.exit(0);}
	    		 
	    		 
	    	}
	    	System.exit(0);  	
	    }

	   try
	   { 
		   
		   new MainForm();
		   mp3down = new MP3Downloader();
	   }
	   catch (Exception e) {Commun.logError(e);};	
	}


	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("START_STOP_TRAY"))
		{
			this.panCap.capManager(this.panCap.isCapAlive());
			((TrayMenu) trayIcon.getPopupMenu()).set_state(this.panCap.isCapAlive());

		}	
	}


	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	
	public void mouseReleased(MouseEvent e) 
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{

	    	this.setVisible(true);
	    	this.setState(JFrame.NORMAL);
	    	SystemTray systemTray = SystemTray.getSystemTray();
			systemTray.remove(trayIcon);

		}
	}

}

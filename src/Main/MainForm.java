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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

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
		
	public static Options 				opts;
	public static Languages 			lang;
	public static ConversionPresets 	convPresets;
	public static ConvItemManager 		convManager;
	public static MP3Downloader 		mp3down;
	public static TrayIcon 				trayIcon;
	public static NetworkInterface[] 	interfaces;
	public static String 				tm_version;
	public static String				tm_path = ".";
	
	private JPanel 						panFen; 	
	private PanelCapture 				panCap;							
	private PanelVideoSearch 			panSVideo;				
	private PanelMP3Search 				panSMP3;	
	private Header 						header;
	
	private static boolean				start_min = false;


	//=====================================================================================================

	public MainForm() throws IOException
	{
		super();
		
		this.setTitle("TubeMaster++ | GgSofts");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.addWindowListener(this);

		this.initComposants();
		this.placeComposants();
		this.setLocationRelativeTo(null);
	
		this.setVisible(true);
		
		if (start_min) this.setState(ICONIFIED);

		if (opts.autoUpdate)
		{
			Thread threadManager2 = new Thread(new Updater());
			threadManager2.start();	
		}
		
	}
	
	//=====================================================================================================
	
	public void check_jre_arch()
	{
		boolean good = false;
		int count = 0;
		
		while (!good)
		{
			try 
			{ 
				System.loadLibrary("jpcap");
				good = true;
			}
		    catch (Error e)
		    {	  
		    	if ((e.getMessage().contains("Jpcap.dll")) && (e.getMessage().contains("64")) && (count < 2))
		    	{
		    		File f32 = new File(tm_path+File.separator+"Jpcap_x32.dll");
		    		File f64 = new File(tm_path+File.separator+"Jpcap_x64.dll");
		    		File fJP = new File(tm_path+File.separator+"Jpcap.dll");
		    		
		    		if (f32.exists() && fJP.exists())
		    		{
		    			fJP.renameTo(f64);
		    			f32.renameTo(new File(tm_path+File.separator+"Jpcap.dll"));
		    		}
		    		else
		    		if (f64.exists() && fJP.exists())
		    		{
		    			fJP.renameTo(f32);
		    			f64.renameTo(new File(tm_path+File.separator+"Jpcap.dll"));
		    		}
		    	}
		    	else
	    		/*if ((e.getMessage().contains("dependant")) && (e.getMessage().contains("libraries")) && (count < 2))
		    	{
	    			File f32 = new File(tm_path+File.separator+"Jpcap_x32.dll");
		    		File f64 = new File(tm_path+File.separator+"Jpcap_x64.dll");
		    		File fJP = new File(tm_path+File.separator+"Jpcap.dll");
		    	}
	    		else*/
		    	{
				    JOptionPane.showMessageDialog(null,"Error : "+e.getMessage()+"\n\n"+
				    		"Be sure to : \n"+
				    		" - Run TubeMaster++ as Administrator.\n"+
				    		" - TubeMaster++ was launch from the installation folder.\n"+
				    		" - The file Jpcap.dll is present in the TubeMaster++ installation directory.\n"
				    		,"TubeMaster++ Error",JOptionPane.ERROR_MESSAGE);
			    	System.exit(0);  
		    	}
		    }
		    
		    count++;
		}	
	}
	
	//=====================================================================================================

	public void initComposants()
	{
		tm_version = "2.4";
		tm_path = System.getProperty("user.dir");
		
		File test_file = new File(tm_path+File.separator+"lang");
		if (!test_file.exists())
		{
			JOptionPane.showMessageDialog(null,"Error : TubeMaster++ was launched from : \""+tm_path+"\"\n\n"+
    		"If this is not the TubeMaster++ installation folder, you have to\n"+
    		"run directly the \"tm++.exe\" file and make yourself a shortcut."
    		,"TubeMaster++ Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);  	
		}
		
		//Windows check JRE x32|x64;
		this.check_jre_arch();
		
		try { interfaces = jpcap.JpcapCaptor.getDeviceList(); }
	    catch (Error e)
	    {	  
	    	if ((e.getMessage().contains("Jpcap.dll")) && (e.getMessage().contains("64")))
	    	{
	    		JOptionPane.showMessageDialog(null,"Error : "+e.getMessage()+"\n\n"+
			    		"To correct this problem, you must re-install TubeMaster++ by selecting the correct \nedition of your Java Runtime Environment (x32 or x64) !"
			    		,"TubeMaster++ Error",JOptionPane.ERROR_MESSAGE);
	    		System.exit(0);
	    	}
	    	else
	    	{
			    JOptionPane.showMessageDialog(null,"Error : "+e.getMessage()+"\n\n"+
			    		"To correct this problem, be sure to : \n"+
			    		" - Run TubeMaster++ as Administrator.\n"+
			    		" - TubeMaster++ was launch from the installation folder.\n"+
			    		" - The file Jpcap.dll is present in the TubeMaster++ installation directory.\n"
			    		,"TubeMaster++ Error",JOptionPane.ERROR_MESSAGE);
		    	System.exit(0);  
	    	}
	    }
	    
	    
	    
		opts 			= new Options();
		lang 			= new Languages();
		convPresets 	= new ConversionPresets();
		convManager 	= new ConvItemManager();
		mp3down			= new MP3Downloader();
		panCap 			= new PanelCapture();
		panSVideo 		= new PanelVideoSearch();
		panSMP3			= new PanelMP3Search();
		header 			= new Header(panCap,panSVideo,panSMP3);
		
		panFen 	= new JPanel();
		panFen.setLayout(new BoxLayout(this.panFen, BoxLayout.Y_AXIS));
		this.setContentPane(panFen);
		panFen.setBackground(Color.decode("#676767"));
		
		
		Thread threadManager = new Thread(MainForm.convManager);
		threadManager.start();
	
	}
	
	//=====================================================================================================
	
	public static void main(String[] args) throws IOException
	{

		for(int i=0;i<args.length;i++)
		{
			if (args[i].equals("-min")) start_min = true;
		}
		

		String os = System.getProperty("os.name");
		System.out.println("TubeMaster++ detected operating system : "+os);
		
		if (os.startsWith("Windows"))
			try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {} //Windows
		else	
			try {UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");} catch (Exception e) {} //Linux 
		

			
	   try
	   { 		   
		   new MainForm();   
	   }
	   catch (Exception e) {Commun.logError(e);};	
	}

	//=====================================================================================================
	
	public void placeComposants()
	{
		ImageIcon monIcon = new ImageIcon(getClass().getResource("images/icon.jpg"));
		this.setIconImage(monIcon.getImage());

		this.panFen.add(this.header);
		this.panFen.add(this.panCap);
		this.panFen.add(this.panSVideo);
		this.panFen.add(this.panSMP3);
		
		this.pack();
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
	
	private void exit_properly()
	{
		MainForm.convManager.shutUp();
		panCap.capManager(true);
		panCap.getListe().clearAll();
		File dossier = new File("temp");
		if (dossier.exists()) this.deleteDir(dossier);
		File f = new File ("Errors.log");
		if ((f.exists()) && (f.length() > 1000000)) f.delete();
	}
		
	
	//=====================================================================================================
	
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent e) 
	{
		boolean vaFermer = true;
		
		
		if (opts.closeBox)
		if (!(JOptionPane.showConfirmDialog(this, MainForm.lang.lang_table[47], MainForm.lang.lang_table[37],
				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION))
		{	
			vaFermer = false;
		}
		
		
		if (vaFermer)
		{
			this.setVisible(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.exit_properly();
		} else this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) 
	{
		//Force disable tray icon on linux systems.
		if ((opts.trayIcon) && (SystemTray.isSupported()) && (!System.getProperty("os.name").startsWith("Linux")))
		{
			this.setVisible(false);
			ImageIcon n = new ImageIcon(getClass().getResource("images/icon.jpg"));
			trayIcon = new TrayIcon(n.getImage(),"TubeMaster++ | GgSofts");
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(this);
			trayIcon.setPopupMenu(new TrayMenu(this));
			((TrayMenu) trayIcon.getPopupMenu()).set_state(this.panCap.isCapAlive());

			
			try 
			{
				SystemTray systemTray = SystemTray.getSystemTray();
				systemTray.add(trayIcon);
			} catch (AWTException e) {Commun.logError(e);}
		}
	}
	public void windowOpened(WindowEvent arg0) {}

	//=====================================================================================================
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("START_STOP_TRAY"))
		{
			this.panCap.capManager(this.panCap.isCapAlive());
			((TrayMenu) trayIcon.getPopupMenu()).set_state(this.panCap.isCapAlive());

		}
		else
		if (e.getActionCommand().equals("EXIT_TRAY"))
		{
			this.exit_properly();
			System.exit(0);

		}	
	}

	//=====================================================================================================
	
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

	//=====================================================================================================
	
	public JFrame get_frame() { return this; }
	
	
	

}

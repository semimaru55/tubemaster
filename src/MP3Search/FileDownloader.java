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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import Capture.CaptureSystem;
import Conversion.CommandRunner;
import Conversion.ConvCommandGen;
import Main.Commun;
import Main.MainForm;



public class FileDownloader implements Runnable, ActionListener
{

	private String addr = "";
	private String fileDir = "";
	private String fileTitle = "";
	private int fileSize = 0;
	private int downSize = 0;
	private int sizePrec = 0;
	private String speed = "0.0 KB/s";
	private boolean stop = false;
	private boolean isFinished = false;
	private boolean isFromYoutube = false;

	
	
	private JProgressBar pbar;
	private JLabel lblStatus;
	private Timer timerRefresh = new Timer(1000,this);
	

	
	public FileDownloader(String addr, String fileDir, String fileTitle, JProgressBar pbar, JLabel lblStatus, boolean isFromYoutube)
	{	
		this.addr = addr;
		this.fileDir = fileDir;
		this.fileTitle = fileTitle.replaceAll("[\\\\/:*?\"<>|]", "_"); 
		this.pbar = pbar;
		this.lblStatus = lblStatus;
		this.timerRefresh.start();
		this.timerRefresh.setRepeats(true);
		this.isFromYoutube = isFromYoutube;	
	}

	

	public void run() 
	{
		try 
		{
			
			URL url = new URL(this.addr);
			
			URLConnection connection = url.openConnection();

			this.fileSize = connection.getContentLength(); //Taille du fichier.
			CaptureSystem.custSize = this.fileSize; //On exclu de la capture.
			
			try 
			{
				Thread.sleep(1000); //Sleep pour laisser le temps a l'attribution de la taille.
			} catch (InterruptedException e1) {Commun.logError(e1);}
			
			
			this.pbar.setMaximum(this.fileSize);
			
			
			if (this.fileSize == -1) //Lien Invalide.
            {
                this.lblStatus.setText(MainForm.lang.lang_table.get(50));
                this.isFinished = true;
                return;
            }
			
			String dir = this.fileDir + File.separator + this.fileTitle;	
			if (this.isFromYoutube) dir = "temp"+File.separator+this.fileTitle+".tmdl"; //Si c'est un MP3 de YouTube.
			
			try
			{
				InputStream input = connection.getInputStream();

				File fic = new File("temp");
				if (!fic.exists()) fic.mkdir();
									
				FileOutputStream writeFile = new FileOutputStream(dir);

				byte[] buffer = new byte[2048];
	            int read;
	            
 
	            //Telechargement.
	            while (((read = input.read(buffer)) > 0) && (!this.stop))
	            {
	            	writeFile.write(buffer, 0, read);
	            	this.downSize += read;
	            	this.pbar.setValue(this.downSize);
	            	this.lblStatus.setText(MainForm.lang.lang_table.get(51)+" ... ("+Commun.sizeConvert(this.downSize) +" / "+Commun.sizeConvert(this.fileSize)+") at "+this.speed);
	            }
	                	           
	            writeFile.flush();
	            writeFile.close();
                input.close();
                
                if ((this.isFromYoutube) && (!this.stop)) //Si c'est du MP3Search alors on Encode.
                {
                	ConvCommandGen mc = new ConvCommandGen(this.fileTitle,dir,this.fileDir);
                	String command = mc.ConvertClassic("-acodec libmp3lame -ab "+MainForm.opts.bitrate+"kb -ac 2 -ar 44100");
                	if (!command.equals(""))
            		{
            			CountDownLatch sema = new CountDownLatch(1);
            			CommandRunner cmd = new CommandRunner(command,this.lblStatus,this.pbar,null,sema);
            			Thread threadManager = new Thread(cmd);
            			threadManager.start();
            			try //Sémaphore pour attendre la fin de la conversion.
            			{
							sema.await();
						} catch (InterruptedException e) {Commun.logError(e);}
						
						File f = new File(dir);
	                	f.delete();
						
            		}              	
                }
                
                
                
			} catch (Exception e) 
			{
				 this.lblStatus.setText(MainForm.lang.lang_table.get(50));
				 this.isFinished = true;
	             return;
			}

            this.isFinished = true;
            if (this.stop)
            {
            	File f = new File(dir);
            	f.delete();
            }	

		} catch (IOException e) 
		{
			this.isFinished = true;
			this.lblStatus.setText(MainForm.lang.lang_table.get(50));
		}
		

	}
	
	public void actionPerformed(ActionEvent e) 
	{
		this.speed = Commun.sizeConvert(this.downSize - this.sizePrec)+"/s";
		this.sizePrec = this.downSize;	
	}
	
	public void stopDownload()
	{
		this.stop = true;
	}
	
	public boolean isFinished() {return this.isFinished;}

} 

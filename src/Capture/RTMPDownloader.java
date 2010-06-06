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


import java.io.File;
import java.util.concurrent.Future;

import com.smaxe.app.uv.downloader.RtmpDownloader;

public class RTMPDownloader implements Runnable
{
	
	private String host = "";
	private String app = "";
	private RtmpDownloader downloader = new RtmpDownloader();
	private boolean isFinished = false;
	private CapturedFile capFile = new CapturedFile(new FileFormat("FLV"),-1);
	private Future<?> task;
	
	
	public RTMPDownloader(String host, String app)
	{
		this.host = host;
		this.app = app;	
		
		this.capFile.setCap_Size(0);
		this.capFile.setCap_FileSize(3000000);
		this.capFile.setFichier(new File(this.capFile.retFilename()));
	}
	

	public void run() 
	{

		Thread tm = new Thread(new DownTask());
		tm.start();	
		File f = new File(this.capFile.retFilename());
		while (!this.isFinished)
		{
			this.capFile.setCap_Size((int)f.length());
			this.capFile.setCap_FileSize((int)f.length()+1000000);
			try 
			{
				Thread.sleep(2000);
			} catch (Exception e) {}
			
			 if (this.capFile.isDestroyed()) this.task.cancel(true);
			
		}
		
		if (this.capFile.isDestroyed()) 
		{
			this.capFile.deleteFile();
		}
		else
		{
			this.capFile.setCap_Size((int)f.length());
			this.capFile.setCap_FileSize((int)f.length());
			this.capFile.processToEnd();
		}
		
	}
	
	public CapturedFile toCapturedFile()
	{
		return this.capFile;
	}
	
	
	
	public class DownTask implements Runnable
	{

		public void run() 
		{
			try
			{
				downloader.setDebugMode(true);
				task = downloader.download(host,null,null,app,capFile.retFilename());
				task.get();
			} catch (Exception e) {}
			
			isFinished = true;

			
		}
		
	}
	
	
	

}

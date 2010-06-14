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

package Conversion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import Capture.ListFileItem;
import Main.Commun;
import Main.MainForm;






public class CommandRunner implements Runnable
{

	private String command;
	private int progress = 0;
	private ListFileItem fileItem = null;
	private JLabel lblStatus;
	private JProgressBar pbar;
	
	private CountDownLatch sema;
	private boolean isFFPLAY = false;
	
	private boolean convFailed = true;
	private String lastLine = "";
	private boolean stopped = false;
	
	private Process process;
	
	public CommandRunner(String command) //Constructeur pour FFPLAY.
	{
		this.command = command;
		this.isFFPLAY = true;
	}
	
	public CommandRunner(String command, JLabel lblStatus, JProgressBar pbar, ListFileItem fileItem, CountDownLatch sema) //Constructeur pour FFMPEG.
	{
		this.command = command;
		this.lblStatus = lblStatus;
		this.pbar = pbar;
		this.pbar.setMaximum(100);
		this.fileItem = fileItem;
		this.sema = sema;
	}

	public void run() 
	{
		try 
		{	
			if (this.fileItem != null) this.fileItem.setConverting();
			boolean finish = false;
			

			this.process = Runtime.getRuntime().exec(this.commandCutter(this.command));
			InputHandler errorHandler = new InputHandler(process.getErrorStream(), "Error Stream");
	        errorHandler.start();
	        InputHandler inputHandler = new InputHandler(process.getInputStream(), "Output Stream");
	        inputHandler.start();
	           
			while(!finish)
			{
				try
				{
					Thread.sleep(1);
					process.exitValue();
					finish = true;

				} catch (Exception e1) {}
			}

			if (this.sema != null) this.sema.countDown();
			if (this.fileItem != null) this.fileItem.setFinished();
			
            if ((!this.isFFPLAY) && (convFailed)) 
            {
            	JOptionPane.showMessageDialog(null, MainForm.lang.lang_table.get(74)+"\n"+lastLine, "FFMPEG Error", 0);
            }
			
	
		} catch (IOException e) 
		{
			if (this.isFFPLAY)
			{
				JOptionPane.showMessageDialog(null, MainForm.lang.lang_table.get(73), MainForm.lang.lang_table.get(72), 0);
			} 
			else
			{
				Commun.logError(e);
			}
			
		
		}
	}
	
	public void stopProcess()
	{
		this.stopped = true;
		this.process.destroy();	
	}
	
	public void setSemaphore(CountDownLatch sema)
	{
		this.sema = sema;
	}
	

	
	class InputHandler extends Thread 
	{

	    InputStream input_;

	   public InputHandler(InputStream input, String name) 
	   {
	        super(name);
	        input_ = input;

	    }

	    public void run() 
	    {
	        try 
	        {        	
	            int c;
	            String line = "";
	            int duree = 0;
	    		String res = "";
	            while ((!stopped) && ((c = input_.read()) != -1)) 
	            {	 	            	
	            	if (c == 13) 
	            	{
	            		if ((line.indexOf("Duration")>-1) && (duree==0))
	    				{
	    					res = parse(line,"Duration: ",",");	
	    					duree = (Integer.parseInt(res.substring(0,2))*3600) +
	    							(Integer.parseInt(res.substring(3,5))*60) +
	    							(Integer.parseInt(res.substring(6,8)));	
	    				}
	    				
	    				
	    				if ((duree!=0) && (line.indexOf("time=")>-1))
	    				{		
	    					 convFailed = false;
	    				     res = parse(line,"time=",".");	     
	    				     progress = (Integer.parseInt(res)*100) / duree;
	    				     lblStatus.setText("Encoding ... ["+progress+"%]");
	    				     pbar.setValue(progress);
	    				}
	
	    				lastLine = line;
	            		line = "";
	            	}
	            	else line += ((char)c);
	            }
	            
	        } catch (Throwable t) {Commun.logError(t);}
	    }
	    
	    public String parse(String chaine, String deb, String fin)
		{			
			chaine = chaine.substring(chaine.indexOf(deb));		
			return chaine.substring(chaine.indexOf(deb)+deb.length(),chaine.indexOf(fin));
		}

	}

	
	public String[] commandCutter(String command)
	{
		ArrayList<String> tabCmd = new ArrayList<String>();

		for (int i=0;i<command.length();i++) //On boucle sur toute la commande.
		{
			String word = ""; //Le mot a recuperer.
			
			if (command.charAt(i)=='\"') //Si c'est un guillement on prends entre.
			{
				i++;
				while (command.charAt(i)!='\"') //Le mot entre guillemets.
				{
					word += command.charAt(i);
					i++;
					if (i==command.length()) break; //On arrive a la fin, on sort.
				}
				i++; //On passe au mot suivant.
			}
			else
			{
				while (command.charAt(i)!=' ') //Le mot entre espaces.
				{
					word += command.charAt(i);
					i++;
					if (i==command.length()) break; //On arrive a la fin, on sort.
				}
			}
			tabCmd.add(word);
		}
		
		String[] ret = new String[tabCmd.size()];
		for (int i=0;i<tabCmd.size();i++) ret[i] = tabCmd.get(i);
		
		return ret;
	}

}

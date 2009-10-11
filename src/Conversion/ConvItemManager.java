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

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import Capture.ListFileItem;
import Main.Commun;
import Main.MainForm;

public class ConvItemManager implements Runnable
{

	private ArrayList<ListFileItem> lesItems = new ArrayList<ListFileItem>();
	
	private boolean stop = false;
	
	
	
	public ConvItemManager()
	{
		
	}
	
	
	
	
	
	public void run() 
	{
		System.out.println("* Conversion Thread Running*");
		while (!this.stop)
		{
			if (!MainForm.opts.defRep.equals(""))
			{
				File f = new File(MainForm.opts.defRep);
				if (!f.exists())
					try 
					{
						f.mkdir();
					} catch (Exception e) {Commun.logError(e);}
				
				
				
			}
			
			
			if (this.lesItems.size()>0)
			{
				if (this.lesItems.get(0) != null)
				{
					CountDownLatch sema = new CountDownLatch(1);
					CommandRunner cmd = this.lesItems.get(0).getCommandRunner();
					cmd.setSemaphore(sema);
					
					Thread threadManager = new Thread(cmd);
					threadManager.start();
					
					try 
					{
						sema.await();
					} catch (InterruptedException e) {Commun.logError(e);}
					
					if (MainForm.opts.delConv) this.lesItems.get(0).toDestroy();
					else
					this.lesItems.get(0).setFinished();
					
				}
				this.lesItems.remove(0);	
			}
					
			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) {Commun.logError(e);}	
		
		}
		System.out.println("* Conversion Thread Stopped*");
	}

	
	public void addItem(ListFileItem item)
	{
		this.lesItems.add(item);
		item.setWaitingFor();
	}

	public void shutUp() {this.stop = true;}


}

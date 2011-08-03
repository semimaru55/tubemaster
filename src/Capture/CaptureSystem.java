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


import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import jpcap.NetworkInterface;


import Main.Commun;
import Main.MainForm;



public class CaptureSystem 
{
	//Liste Generale des Paquets Captures.
	public static ConcurrentLinkedQueue<TMPacket> packetList = new ConcurrentLinkedQueue<TMPacket>();	
	
	private PacketsManager pMan;

	//Titre de fichier customise.
	public static String custTitle = "";
	
	//Taille du prochain MP3 a telecharger (pour exclusion de la capture).
	public static int filterSize = 0;
	
	public static int unNamedId = 0;
	
	private EcouteInterface ecoutInter;
	private ArrayList<EcouteInterface> tabEcoute = new ArrayList<EcouteInterface>();
	

	//=====================================================================================================
		
	public CaptureSystem(ListFile laListe)
	{
		this.pMan = new PacketsManager(laListe);				
		Thread threadMan = new Thread(this.pMan);
		threadMan.start();

		if (MainForm.interfaces.length == 0)
		 {
			 JOptionPane.showMessageDialog(null,  "TubeMaster++ cannot find any suitable network interface to capture data ! Please make sure you are running the program as administrator." 
					 , "Error - No Network Interface available !", 0);
			 System.exit(0);
		 }

		
		for (int i=0;i<MainForm.interfaces.length;i++)
		{
			if (!this.forbiddenInterface(MainForm.interfaces[i]))
			{
				CountDownLatch sema = new CountDownLatch(1);
				this.ecoutInter = new EcouteInterface(laListe,MainForm.interfaces[i],sema);
				this.tabEcoute.add(this.ecoutInter);
				Thread threadManager = new Thread(this.ecoutInter);
				threadManager.start();
				try 
				{
					sema.await(2, TimeUnit.SECONDS);
					
				} catch (InterruptedException e) {Commun.logError(e);}		
			}
		}
	}
	
	//Check for known bugs on these network interfaces.
	private boolean forbiddenInterface(NetworkInterface interf)
	{
		boolean ret = false;
		if (interf.description != null)
		{
			//Ubuntu problematic interface.
			if (interf.description.startsWith("USB bus number")) ret = true;
		}
		return ret;
	}
	
	
	//=====================================================================================================
	
	public void shutUp()
	{
		this.pMan.shutUp();
		
		for (int i=0;i<this.tabEcoute.size();i++)
			this.tabEcoute.get(i).shutUp();
	
		this.tabEcoute.clear();
	}
	
	//=====================================================================================================
	
	public long getNbCapturedPackets()
	{
		long nb = 0;
		for (int i=0;i<this.tabEcoute.size();i++) 
		{
			nb += this.tabEcoute.get(i).getNbCapturedPackets();
		}
		return nb;
	}
	
	public long getNbDroppedPackets()
	{
		long nb = 0;
		for (int i=0;i<this.tabEcoute.size();i++) 
		{
			nb += this.tabEcoute.get(i).getNbDroppedPackets();
		}
		return nb;
	}
	
	
}

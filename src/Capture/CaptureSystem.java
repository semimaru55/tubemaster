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

import javax.swing.JOptionPane;


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
			 JOptionPane.showMessageDialog(null,  "You must run TubeMaster++ as Administrator ! \n" +
				   		"- Windows : Right click on the icon > Properties > Compatibility > Run As Administrator.\n"+
				   		"- UNIX & Others : You must be root to run TubeMaster++ properly."
					 , "Error - Admin Rights", 0);
			 System.exit(0);
		 }

		
		for (int i=0;i<MainForm.interfaces.length;i++)
		{
			CountDownLatch sema = new CountDownLatch(1);
			this.ecoutInter = new EcouteInterface(laListe,MainForm.interfaces[i],sema);
			this.tabEcoute.add(this.ecoutInter);
			Thread threadManager = new Thread(this.ecoutInter);
			threadManager.start();
			try 
			{
				sema.await();
			} catch (InterruptedException e) {Commun.logError(e);}
			
		}
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
		for (int i=0;i<this.tabEcoute.size();i++) nb += this.tabEcoute.get(i).getNbCapturedPackets();
		return nb;
	}
	
	public long getNbDroppedPackets()
	{
		long nb = 0;
		for (int i=0;i<this.tabEcoute.size();i++) nb += this.tabEcoute.get(i).getNbDroppedPackets();
		return nb;
	}
	
	
}

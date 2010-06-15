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



import Main.Commun;
import Main.MainForm;




public class PacketsManager implements Runnable
{
	
	private ListFile fileList;										//Liste des fichiers.

	private ArrayList<TMPacket> packetCache;						//Cache dynamique des paquets.	
	private SeqTracker tracker = new SeqTracker();
		
	private boolean isActive = true;
	

	
	//=====================================================================================================

	public PacketsManager(ListFile fileList) 
	{
		this.fileList = fileList;
		this.packetCache = new ArrayList<TMPacket>();
	}
	
	//=====================================================================================================
	

	
	public void run() //Coeur de TubeMaster.
	{
		
		System.out.println("*TubeMaster++ Core Started*");
		
		while (this.isActive) //Boucle pour le moment infinie.
		{
			TMPacket p = CaptureSystem.packetList.poll();			
			
			if (p!=null)
			{
				if (p.isValid())
				{
					boolean trouve = false;
					for (int i=0;i<this.fileList.getItemsCount();i++) //Parcours de la liste des fichiers pour trouver l'actif.
					{
						CapturedFile f = this.fileList.getItem(i).getFichier();
						if(f.getCap_Ident()==p.getAck())
						{		
							f.addDatas(p,false);
							trouve = true;
							break;
						}	
					}

					if ((!trouve) && (this.tracker.isValidSeq(p.getAck())))
					{	
						if (p.contient("FLV"+(char)1)) this.file_detected("FLV", p);
						else
						if (p.contient("ID3"+(char)2) || p.contient("ID3"+(char)3) || p.contient("ID3"+(char)4) || 
							p.contient("Content-Type: audio/mpeg") || (p.contient("LAME3.") && p.contient("Xing"))
							|| (p.contient("X-Powered-By: PHP/5.2.6-pl7-gentoo"))) 
								this.file_detected("MP3", p);
						else
						if (p.contient("ftypisom") || p.contient("ftypmp4") || p.contient("isomavc1"))
							this.file_detected("MP4", p);
						else
						if (p.contient("ftypqt") || p.contient("moov")) this.file_detected("MOV", p);
						
						
						else
						{
							if (p.contient("Content-Length: ") || (p.contient("GET /")))
							{	
								if (this.packetCache.size() >= 100) this.packetCache.clear();
								this.packetCache.add(p); //Ajout du paquet dans le cache.
							}		
							this.tracker.addSequence(p.getAck()); //Ajoute dans la poubelle probable.
						}
							
							
					}
				}
			}
			else
			{
				try 
				{
					Thread.sleep(1);
					
				} catch (Exception e) {Commun.logError(e);}
			}	
		}
		System.out.println("*TubeMaster++ Core Closed*");
	}

	//=====================================================================================================
	
	
	
	public int content_length(TMPacket p)
	{
		
		String s = new String(p.getDatas());
		
		//Recherche dans le paquet actuel.
		int size = -250289;
		if (s.indexOf("Content-Length: ") > -1) 
			size = Integer.parseInt(Commun.parse(s,"Content-Length: ",""+(char)13));	
		else
		if (s.indexOf("Content-length: ") > -1) 
			size = Integer.parseInt(Commun.parse(s,"Content-length: ",""+(char)13));
			
		
		//Si on trouve pas alors on va chercher dans le cache.
		if (size==-250289)
		{
			int len = this.packetCache.size();
			for (int i=0;i<len;i++)
			{
				if (p.getAck()==this.packetCache.get(i).getAck())
				{					
					String s1 = new String(this.packetCache.get(i).getDatas());
					if (s1.indexOf("Content-Length: ") > -1) 
					{
						size = Integer.parseInt(Commun.parse(s1,"Content-Length: ",""+(char)13));						
						break;
					}
				}	
			}	
		}	

		return size;		
	}
	
	//=====================================================================================================
	
	public String get_real_url(TMPacket p)
	{
		String s = new String(p.getDatas());
		String url = "";
		String host = "";
		
		//Recherche dans le paquet actuel.
		if (s.indexOf("GET /") == 0) 
		{
			url = Commun.parse(s, "GET /", " HTTP/");
			host = Commun.parse(s, "Host: ", ""+(char)13);	
		}
			
		
		
		//Si on trouve pas alors on va chercher dans le cache. (Utilisation des Ports).
		if (url.equals(""))
		{
			int len = this.packetCache.size();
			for (int i=0;i<len;i++)
			{
				if (p.getPorts()==this.packetCache.get(i).getPorts())
				{					
					String s1 = new String(this.packetCache.get(i).getDatas());
					if ((s1.indexOf("GET /") == 0) && (s1.indexOf("crossdomain.xml") == -1))
					{
						url = Commun.parse(s1, "GET /", " HTTP/");
						host = Commun.parse(s1, "Host: ", ""+(char)13);	
						this.packetCache.remove(i);
						break;
					}
				}	
			}	
		}			
		return "http://"+host+"/"+url;
	}

	//=====================================================================================================	

	public void file_detected(String format, TMPacket p)
	{
		
		CapturedFile newFile = new CapturedFile(new FileFormat(format),p.getAck());

		String real_url = this.get_real_url(p);
		int size = this.content_length(p);

		
		if (size == CaptureSystem.custSize)
		{
			size = 0;
			CaptureSystem.custSize = 0;		
		}
		

		if (size > Integer.parseInt(MainForm.opts.minimal));
		{
			newFile.setCap_FileSize(size);
			newFile.addDatas(p, true);	
			this.fileList.ajoutItem(new ListFileItem(this.fileList,newFile,real_url));
			
			//SAUVETAGE TEMP
			int len = this.packetCache.size();
			for (int i=0;i<len;i++)
			{
				if (p.getAck()==this.packetCache.get(i).getAck())
				{		
						newFile.addDatas(this.packetCache.get(i),false);
				}	
			}		
		}
		this.packetCache.clear();
	}
	
	//=====================================================================================================
		
	
	public void shutUp()
	{
		this.isActive = false;
		
	}
	
	//=====================================================================================================
	



}





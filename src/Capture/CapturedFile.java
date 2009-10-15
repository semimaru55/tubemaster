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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import Conversion.CommandRunner;
import Main.Commun;


public class CapturedFile
{
	private boolean isFull = false;											//Fichier Complet.										//Fichier en attente.
	private int cap_size = 0; 												//Taille de capture actuelle.
	private int cap_filesize = 0;											//Taille totale du fichier.
	private long cap_ident = 0;											    //Identité du fichier.	
	private FileFormat format;												//Format fu fichier.
	private long firstSeqNum = 0;											//Premier Num. de Sequence.
	private long latestSeq = -1;
	private Date laDate = new Date();
	private long idFile = laDate.getTime();
	private long random = (long) (Math.random()*100000);
	private String filename = "";
	private boolean isDestroyed = false;	
	
	private File fichier;
	private RandomAccessFile fw;
	
	private TCPMap tcpmap = new TCPMap();


	//=====================================================================================================
	
	public CapturedFile(FileFormat format, long newIdent)
	{
		this.format = format;
		this.cap_ident = newIdent;	
		new File("temp").mkdir(); 
		this.filename = "temp"+File.separator+"tm++_capture_"+this.idFile+this.random+"."+this.format.retFormat().toLowerCase();

	}
	
	//=====================================================================================================
	
	public void addDatas(TMPacket p, boolean isFirst)
	{
		if(!this.isFull)
		{
			if (isFirst) //Test du premier paquet du fichier pour enlever éventuellement le header HTTP.
			{
				this.firstSeqNum = p.getSeq();
				
				String str = new String(p.getDatas());
				if (str.indexOf("HTTP")==0)
				{
					int pos = 0;
					for(int i=0;i<p.getDatas().length-3;i++)
					{
						if ((p.getDatas()[i]==13)&&(p.getDatas()[i+1]==10)&&(p.getDatas()[i+2]==13)&&(p.getDatas()[i+3]==10))
						{
							pos = i + 4;
							break;	
						}
					}
					
					this.firstSeqNum += pos;
					p.setSeq(this.firstSeqNum);
					int size = p.getDatas().length-pos;
					byte[] newArray = new byte[size];
					for(int i=0;i<newArray.length;i++) newArray[i] = p.getDatas()[pos+i];
					p.setDatas(newArray);	
				}
				
				try //On crée le fichier de sortie.
				{
					new File("temp").mkdir(); 
					this.fichier = new File(this.retFilename());
					this.fichier.deleteOnExit();
					this.fw = new RandomAccessFile(this.fichier,"rw");
							
				} catch (FileNotFoundException e) {Commun.logError(e);}	
			}

			if (this.fichier.exists())
			{
				try //On écrit dans le fichier de sortie (par offset avec les sequence numbers).
				{
					if (p.getSeq()>=this.firstSeqNum)
					{						
						//Procede anti-retransmission.
						if (p.getSeq()>this.latestSeq) //Normal
						{
							this.latestSeq = p.getSeq();
							this.cap_size += p.getDatas().length;
						}
						else //Retransmission
						{
							long toWrite = p.getDatas().length; //Taille a ajouter.
							int mapPos = this.tcpmap.seqPos(p.getSeq());
							if (mapPos>-1)
							{
								long oldSize = this.tcpmap.getSize(mapPos);
								if (toWrite >= oldSize) toWrite = p.getDatas().length - oldSize;
								else toWrite = 0;
								
							}
							
							this.cap_size += toWrite;												
						}
						
						this.tcpmap.addSeq(p.getSeq(),p.getDatas().length); //Ajout dans la tcp map.
						
						//On ecrit les donnees.
						this.fw.seek(p.getSeq()-this.firstSeqNum);	
						this.fw.write(p.getDatas());

	
					}
	
				} catch (Exception e) 
				{
					Commun.logError(e);
				}
			}

			if (this.cap_size>=this.cap_filesize) this.processToEnd();
		}
	}

	//=====================================================================================================
	
	public void playFile()
	{		
		CommandRunner cmd = new CommandRunner("ffplay -x 300 -y 225 \""+this.retFilename()+"\"");
		Thread threadManager = new Thread(cmd);
		threadManager.start();
	}
	
	//=====================================================================================================

	public void processToEnd()
	{
		this.tcpmap.clearMap();
		this.isFull = true;
		this.cap_ident = -1;	
		try {if (this.fw != null) this.fw.close();} catch (IOException e) {Commun.logError(e);}
			
	}
	
	//=====================================================================================================
	
	public String findID3()
	{
		String artiste = "";
		String name = "";
		
		if (this.fichier!=null)
		{
			MediaFile oMediaFile = new MP3File(this.fichier);
	        try 
	        {
				ID3Tag[] aoID3Tag = oMediaFile.getTags();
	
				for(int i=0;i<aoID3Tag.length;i++)
				{
					
					if (aoID3Tag[i] instanceof ID3V1_0Tag)
		            {
		                ID3V1_0Tag oID3V1_0Tag = (ID3V1_0Tag)aoID3Tag[i];
		                if (oID3V1_0Tag.getTitle() != null)
		                {
		                	name = oID3V1_0Tag.getTitle();
		                }
		                if (oID3V1_0Tag.getArtist() != null)
		                {
		                	artiste = oID3V1_0Tag.getArtist();
		                }
		            }
					else if (aoID3Tag[i] instanceof ID3V2_3_0Tag)
		            {
		                ID3V2_3_0Tag oID3V2_3_0Tag = (ID3V2_3_0Tag)aoID3Tag[i];
		                if (oID3V2_3_0Tag.getTitle() != null)
		                {
		                	name = oID3V2_3_0Tag.getTitle();
		                }
		                if (oID3V2_3_0Tag.getArtist() != null)
		                {
		                	artiste = oID3V2_3_0Tag.getArtist();
		                }
		            }
	
					
				}		
		
			} catch (ID3Exception e) {Commun.logError(e);}
		
		}		
		name.replaceAll(""+(char)0, "");
		artiste.replaceAll(""+(char)0, "");
		
		return name + " - " + artiste;
	}
	
	//=====================================================================================================
	
	public void deleteFile() 
	{
		try 
		{
			this.isDestroyed = true;
			if (this.fw != null) this.fw.close();
			if (this.fichier != null) this.fichier.delete();
		} catch (Exception e) {Commun.logError(e);}
		
	}
	
	//=====================================================================================================
	
	public long getCap_Ident() { return this.cap_ident; }
	public void setCap_Size(int newSize) { this.cap_size = newSize; }
	public int getCap_Size() { return this.cap_size; }
	public void setCap_FileSize(int newFileSize) { this.cap_filesize = newFileSize; }
	public int getCap_FileSize() { return this.cap_filesize; }
	public boolean is_Full() { return this.isFull; }	
	public FileFormat getFileFormat() {return this.format;}
	public String retFilename() {return this.filename;}
	public void setFilename(String name) {this.filename = name;}
	public void setFichier(File f) {this.fichier = f;}
	public boolean isDestroyed() {return this.isDestroyed;}
	
	//=====================================================================================================


}

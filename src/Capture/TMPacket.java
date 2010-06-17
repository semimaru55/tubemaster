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

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;


import Main.Commun;


public class TMPacket 
{
	private boolean 	isValid;
	private long 		ack;
	private long 		seq;
	private byte[] 		byteArray;
	private int 		srcPort;
	private int 		dstPort;

	
	public TMPacket(Packet p)
	{
		this.isValid = false;
		if (p instanceof TCPPacket)
		{
			TCPPacket pack = (TCPPacket) p;
			
			this.isValid 	= true;
			this.ack 		= pack.ack_num; 
			this.seq 		= pack.sequence;
			this.srcPort 	= pack.src_port;
			this.dstPort 	= pack.dst_port;
			this.byteArray 	= pack.data;
			
		}
		else this.isValid = false;				
	}

	
	public long 	getAck() 			{return this.ack;}	
	public long 	getSeq() 			{return this.seq;}	
	public byte[] 	getDatas() 			{return this.byteArray;}		
	public boolean 	isValid() 			{return this.isValid;}	
	public int 		getPorts() 			{return this.srcPort+this.dstPort;}
	
	
	public boolean contains(String str)
	{
		return (Commun.arrayPos(this.byteArray, str.getBytes(),1)>-1);
	}
	
	//=====================================================================================================
	
	public boolean searchFLV()
	{
		return (this.contains("FLV"+(char)1));
	}
	
	
	public boolean searchMP3()
	{
		return (this.contains("ID3"+(char)2) || 
				this.contains("ID3"+(char)3) || 
				this.contains("ID3"+(char)4) || 
				this.contains("Content-Type: audio/mpeg") ||
				(this.contains("LAME3.") && this.contains("Xing")));
	}
	
	
	public boolean searchMP4()
	{
		return (this.contains("ftypisom") || 
				this.contains("ftypmp4") || 
				this.contains("isomavc1"));
	}
	
	
	public boolean searchMOV()
	{
		return (this.contains("ftypqt") || 
				this.contains("moov"));
	}
	
	
	public boolean searchFileHeader()
	{
		return (this.contains("Content-Length: ") ||
				this.contains("GET /"));
	}
	
	//=====================================================================================================	
	
	public void removeHTTPHeader()
	{		
		String str = new String(this.byteArray);
		if (str.indexOf("HTTP")==0)
		{
			int pos = 0;
			byte[] datas = this.byteArray;
			for(int i=0;i<datas.length-3;i++)
			{
				if ((datas[i]==13)&&(datas[i+1]==10)&&(datas[i+2]==13)&&(datas[i+3]==10))
				{
					pos = i + 4;
					break;	
				}
			}
			
			this.seq += pos;
			int size = datas.length-pos;
			byte[] newArray = new byte[size];
			for(int i=0;i<newArray.length;i++) newArray[i] = datas[pos+i];
			this.byteArray = newArray;	
		}
	}
	
	//=====================================================================================================	
	
	public long search_content_length()
	{
		String s = new String(this.byteArray);
		long size = -1;
		if (s.indexOf("Content-Length: ") > -1) 
			size = Integer.parseInt(Commun.parse(s,"Content-Length: ",""+(char)13));	
		else
		if (s.indexOf("Content-length: ") > -1) 
			size = Integer.parseInt(Commun.parse(s,"Content-length: ",""+(char)13));
		
		return size;
	}
	
	//=====================================================================================================	
	
	public String search_url()
	{
		String s = new String(this.byteArray);
		String url = "";
		String host = "";
		
		if ((s.indexOf("GET /") == 0) && (s.indexOf("crossdomain.xml") == -1))
		{
			url = Commun.parse(s, "GET /", " HTTP/");
			host = Commun.parse(s, "Host: ", ""+(char)13);	
		}
				
		return "http://"+host+"/"+url;	
	}
	
	//=====================================================================================================	

}

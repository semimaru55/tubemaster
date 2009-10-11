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
	private boolean isValid = false;
	private long ack;
	private long seq;
	private byte[] byteArray;
	
	private int srcPort;
	private int dstPort;

	
	public TMPacket(Packet p)
	{
		if (p instanceof TCPPacket)
		{
			TCPPacket pack = (TCPPacket) p;
	
					this.isValid = true;
					this.ack = pack.ack_num; 
					
					this.seq = pack.sequence;
					this.srcPort = pack.src_port;
					this.dstPort = pack.dst_port;

					this.byteArray = pack.data;
		}
		else this.isValid = false;				
	}

	
	public long getAck() {return this.ack;}
	
	public long getSeq() {return this.seq;}
	
	public void setSeq(long s) {this.seq = s;}
	
	public byte[] getDatas() {return this.byteArray;}
	
	public void setDatas(byte[] d) {this.byteArray = d;}
	
	public boolean isValid() {return this.isValid;}
	
	public int getPorts() {return this.srcPort+this.dstPort;}
	
	public boolean contient(String str)
	{
		return Commun.arrayPos(this.byteArray, str.getBytes(),1)>-1;
	}
	
	
	

}

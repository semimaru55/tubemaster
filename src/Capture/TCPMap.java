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

public class TCPMap 
{

	private ArrayList<Long> tabseq = new ArrayList<Long>(); 
	private ArrayList<Integer> tabsize = new ArrayList<Integer>(); 
	
	
	public TCPMap() {}
	
	
	public void addSeq(long seq, int size)
	{
		int pos = this.tabseq.indexOf(seq);
		if (pos > -1) //Test existance. Pas de doublons dans la map.
		{
			this.tabseq.remove(pos);
			this.tabsize.remove(pos);
		}
	
		this.tabseq.add(seq); //Ajout dans la map.
		this.tabsize.add(size);	
		
		if (tabseq.size()>90) //90 sequences max. in the map.
		{
			tabseq.remove(0);
			tabsize.remove(0);	
		}
		
	}
	
	public void clearMap()
	{
		this.tabseq.clear();
		this.tabsize.clear();		
	}
	
	public int seqPos(long seq) {return this.tabseq.indexOf(seq);}
	public int getSize(int pos) {return this.tabsize.get(pos);}
	
	
}

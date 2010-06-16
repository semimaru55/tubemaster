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


public class StreamsFilter 
{
	
	
	private ArrayList<Long> seqTable;
	private ArrayList<Integer> seqOccur;
	

	public StreamsFilter() 
	{
		this.seqTable = new ArrayList<Long>();
		this.seqOccur = new ArrayList<Integer>();
	}

	
	public void addToFilter(long seq)
	{
		if (this.seqTable.size() > 300)
		{
			this.seqTable.clear();
			this.seqOccur.clear();
		}
			 	
		int pos = this.seqTable.indexOf(seq);
		if (pos==-1)
		{
			this.seqTable.add(seq);
			this.seqOccur.add(1);
		}
		else this.seqOccur.set(pos, this.seqOccur.get(pos)+1); 
		
	}
	
	
	public boolean isNotFiltered(long seq)
	{
		boolean ret = true;
	 	int pos = this.seqTable.indexOf(seq);
	  	if (pos!=-1)
	 	{
	  		if (this.seqOccur.get(pos) > 10) ret = false;
	 	}
	 	return ret; 
	}

}

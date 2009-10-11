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


public class SeqTracker 
{
	
	private ArrayList<Long> seqTable = new ArrayList<Long>();
	private ArrayList<Long> seqTableTracker = new ArrayList<Long>();
	
	
	public SeqTracker() {}
	
	
	public void addSequence(long seq)
	{
		if (this.seqTable.size()>100) 
		{
			this.seqTable.clear();
			this.seqTableTracker.clear();
		}
		
		int pos = this.seqTable.indexOf(seq);
		if (pos==-1)
		{
			this.seqTable.add(seq);
			this.seqTableTracker.add((long)1);
		}
		else this.seqTableTracker.set(pos, this.seqTableTracker.get(pos)+1);	
	}
	
	public boolean isValidSeq(long seq)
	{
		
		boolean ret = true;
		int pos = this.seqTable.indexOf(seq);
		if (pos!=-1)
		{			
			if (this.seqTableTracker.get(pos)>10) ret = false;	
		}
		return ret;
	}
	
	
	
	

}

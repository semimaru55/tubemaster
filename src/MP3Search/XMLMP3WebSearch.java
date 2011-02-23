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

package MP3Search;

import javax.swing.table.DefaultTableModel;




public class XMLMP3WebSearch implements Runnable
{
	
	private String query;
	private DefaultTableModel model;
	private PanelMP3Search panel;
	private boolean stopped = false;
	
	private MP3Wrzuta wrzuta_search;
	
	public XMLMP3WebSearch(String query, DefaultTableModel model,PanelMP3Search panel)
	{
		this.query = query;
		this.model = model;
		this.panel = panel;
	}


	public void run() 
	{
		this.panel.startSearch();

		this.wrzuta_search = new MP3Wrzuta(this.query);

		if (!this.stopped) this.wrzuta_search.doSearch(this.model);

		this.panel.stopSearch();
	}
	

	public void shutup()
	{
		this.stopped = true;
		if (this.wrzuta_search!=null) this.wrzuta_search.shutup();
		
	}
	

}

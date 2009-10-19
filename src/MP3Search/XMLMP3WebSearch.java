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
	
	MP3Dizzler dizzler_search;
	MP3Sideload sideload_search;
	MP3Youtube youtube_search;
	MP3Playlist playlist_search;
	
	public XMLMP3WebSearch(String query, DefaultTableModel model,PanelMP3Search panel)
	{
		this.query = query;
		this.model = model;
		this.panel = panel;
	}


	public void run() 
	{
		this.panel.startSearch();
		
		this.dizzler_search = new MP3Dizzler(this.query);
		this.sideload_search = new MP3Sideload(this.query);
		this.youtube_search = new MP3Youtube(this.query);
		this.playlist_search = new MP3Playlist(this.query);
		
		
		if (!this.stopped) this.dizzler_search.doSearch(this.model);	
		if (!this.stopped) this.sideload_search.doSearch(this.model);
		if (!this.stopped) this.playlist_search.doSearch(this.model);
		if (!this.stopped) this.youtube_search.doSearch(this.model);
		

		this.panel.stopSearch();
	}
	

	public void shutup()
	{
		this.stopped = true;
		if (this.dizzler_search!=null) this.dizzler_search.shutup();
		if (this.sideload_search!=null) this.sideload_search.shutup();
		if (this.playlist_search!=null) this.playlist_search.shutup();
		
	}
	

}

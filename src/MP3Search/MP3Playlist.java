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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import Main.Commun;


public class MP3Playlist
{
	
	private String query = "";
	private boolean ended = false;
	
	public MP3Playlist(String query)
	{
		this.query = query;
		this.query = this.query.replaceAll(" ", "+");
	}
	
	
	public void doSearch(DefaultTableModel model)
	{
		
		try
		{
			int page = 1;
			while (!ended)
			{

				URL go = new URL("http://www.playlist.com/async/searchbeta/tracks?searchfor="+this.query+"&page="+page);
				URLConnection yc = go.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String inputLine;
				String total = "";
				while ((inputLine = in.readLine()) != null)
				{
					total += inputLine;	
				}
				
				page++;
				if (total.indexOf("</li>")==-1) ended = true;
				
				if (total.indexOf("<li id=")>-1)
				{

					while (total.indexOf("<li id=")>-1)
					{
						total = total.substring(total.indexOf("<li id="));
						String chaine = Commun.parse(total, "<li id=", "</li>");
						String titre = Commun.parse(chaine, "class=\"song\">", "</a>");
						String artist = Commun.parse(chaine, "class=\"artist\">", "</a>");
						String url = Commun.parse(chaine, "<span class=\"url\" title=\"", "\">");
						

						
						Vector<String> newRow = new Vector<String>();
						newRow.add(artist + " - " +titre);
						newRow.add("Playlist.com");
						newRow.add(url);
						model.addRow(newRow);
	
						
						total = total.substring(total.indexOf("</li>"));		
						
					}	
				}
				in.close();		
				Thread.sleep(10);
			}
				
		} 
		catch(Exception e) {Commun.logError(e);}
	
	}

	
	public void shutup()
	{
		this.ended = true;
	}
	

}

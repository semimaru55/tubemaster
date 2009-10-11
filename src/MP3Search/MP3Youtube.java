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

public class MP3Youtube 
{
	
	private String query = "";
	
	public MP3Youtube(String query)
	{
		this.query = query;
		this.query = this.query.replaceAll(" ", "+");
	}

	public void doSearch(DefaultTableModel model)
	{
		try
		{
			URL google = new URL("http://gdata.youtube.com/feeds/api/videos?q="+this.query+"&category=Music&client=ytapi-GgSofts-TubeMaster-reo5u8hm-1&key=AI39si5gRU6WEKM07_Vy0bqmpAI0M38OQrXr9P0PzVkQmqXbteA7LP8gJ63YZMN79FpRP5q_0JaFn34LMeQ0oCYadMJ6JB_jNw");
			URLConnection yc = google.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
			{

				while (inputLine.indexOf("</entry>")!=-1)
				{
					String vid = Commun.parse(inputLine, "<entry>", "</entry>");
					inputLine = inputLine.substring(inputLine.indexOf("</entry>")+7);

					String t = "n/a";
					if (vid.indexOf("<title type='text'>")>-1) t = Commun.parse(vid, "<title type='text'>", "</title>");
					String id = "n/a";
					if (vid.indexOf("watch?v=")>-1) id = Commun.parse(vid, "watch?v=", "'");
					
					t = t.replaceAll("/", "").replaceAll(":", "")
				         .replaceAll("\"", "").replaceAll("<", "").replaceAll(">", "")
				         .replaceAll("|", "").replaceAll("Ã©", "é").replaceAll("Ã ", "à")
				         .replaceAll("Ãª", "ê").replaceAll("Ã¨", "è").replaceAll("  ", " ").replaceAll("Ã®", "î").replaceAll("amp;", "");
											
					Vector<String> newRow = new Vector<String>();
					newRow.add(t);
					newRow.add("http://www.youtube.com/watch?v="+id);
					model.addRow(newRow);
				}
	
			}
			in.close();
		} 
		catch(Exception e) {Commun.logError(e);}
	}
	

	
	
}

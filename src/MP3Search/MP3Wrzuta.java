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
import java.net.URLDecoder;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import Main.Commun;


public class MP3Wrzuta
{
	
	private String query = "";
	private boolean ended = false;
	
	public MP3Wrzuta(String query)
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

				URL go = new URL("http://www.wrzuta.pl/szukaj/audio/"+this.query+"/"+page);
				URLConnection yc = go.openConnection();
				yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; fr; rv:1.9.2) Gecko/20100115 Firefox/3.6");
							
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String inputLine;
				String total = "";
				
				page++;
				
				while ((inputLine = in.readLine()) != null)
				{
					total += inputLine;	
				}

	
				if (total.indexOf("file audio")>-1)
				{

					while (total.indexOf("file audio")>-1)
					{
						total = total.substring(total.indexOf("file audio"));
						String url = Commun.parse(total, "<a href=\"", "\">");
						url = url.replace("/audio/", "/xml/plik/");
						url = url.substring(0, url.lastIndexOf('/'));
						
						String titre = "";
						try{titre = Commun.parse(total, "alt=\"", "\" ");}
						catch (Exception e) { titre= "Unknown";}
						
						titre = titre.replace("&#039;", "'");
						titre = titre.replace("&amp;", "&");
						
						Vector<String> newRow = new Vector<String>();
						
						titre = URLDecoder.decode(titre, "UTF-8"); 
						url = URLDecoder.decode(url, "UTF-8");
						
						url += "/wrzuta.pl/sa/10000";
						
						newRow.add(titre);
						newRow.add("Wrzuta.pl");
						newRow.add(url);
						model.addRow(newRow);
	
						
						total = total.substring(total.indexOf("</div>"));		
						
					}	
				} else ended = true;
				
				
				
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


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

package VideoSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import Main.Commun;


public class XMLVideoWebSearch implements Runnable
{
	
	private String addr;
	private DefaultTableModel model;
	private JLabel lblLoad;
	private CountDownLatch sema;
	private boolean error;
	
	public XMLVideoWebSearch(String addr, DefaultTableModel model, JLabel lblLoad, CountDownLatch sema)
	{
		this.addr = addr;
		this.model = model;
		this.lblLoad = lblLoad;
		this.sema = sema;
		this.error = false;
	}
	
	public boolean is_error() { return this.error; }


	public void run() 
	{
		this.lblLoad.setVisible(true);
		try
		{
			URL addr = new URL(this.addr);
			URLConnection yc = addr.openConnection();
			BufferedReader in = null;
			
			try 
			{
				in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			} 
			catch (IOException e) {error = true;}

			if (in != null)
			{
			
				String inputLine;
				while ((inputLine = in.readLine()) != null)
				{
					while (inputLine.indexOf("</Video>")!=-1)
					{
						String vid = new String(Commun.parse(inputLine, "<Video>", "</Video>").getBytes(),"UTF-8");
						inputLine = inputLine.substring(inputLine.indexOf("</Video>")+7);
	
						String t = "n/a";
						if (vid.indexOf("<title>")>-1) t = Commun.parse(vid, "<title>", "</title>");					
						String url = "n/a";
						if (vid.indexOf("<videoUrl>")>-1) url = Commun.parse(vid, "<videoUrl>", "</videoUrl>");
						String ch = "n/a";
						if (vid.indexOf("<channel>")>-1) ch = Commun.parse(vid, "<channel>", "</channel>");
						String d = "n/a";
						if (vid.indexOf("<description>")>-1) d = Commun.parse(vid, "<description>", "</description>");
						String c = "n/a";
						if (vid.indexOf("<category>")>-1) c = Commun.parse(vid, "<category>", "</category>");
						String time ="n/a";
						if (vid.indexOf("<runtime>")>-1) time = ""+(Integer.parseInt(Commun.parse(vid, "<runtime>", "</runtime>"))/60)+" Min.";
						String thumb ="n/a";
						if (vid.indexOf("<thumbnailUrl>")>-1) thumb = Commun.parse(vid, "<thumbnailUrl>", "</thumbnailUrl>");
						
						
						Vector<String> newRow = new Vector<String>();
						newRow.add(t.replaceAll("&#039;", "").replaceAll("&amp;", "&").replaceAll("&quot;", ""));
						newRow.add(time);
						newRow.add(c);
						newRow.add(ch);
						newRow.add(d);
						newRow.add(url);
						newRow.add(thumb);
						this.model.addRow(newRow);
	
					}
		
				}
				in.close();
			}
		} 
		catch (Exception e) {Commun.logError(e);}
		
		sema.countDown();

		this.lblLoad.setVisible(false);
	}
	

}

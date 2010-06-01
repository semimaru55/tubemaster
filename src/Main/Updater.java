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

package Main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.swing.JOptionPane;

public class Updater implements Runnable
{

	
	public Updater() {}
	
	
	public void run() 
	{
		try
		{
		
			URL link = new URL("http://tubemaster.free.fr/tm_version.php");
			HttpURLConnection yc = (HttpURLConnection) link.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String total = "";
			while ((inputLine = in.readLine()) != null) total += inputLine;
			in.close();	
			
			
			if (!total.equals(MainForm.tm_version))
			{
				
				if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table.get(49), MainForm.lang.lang_table.get(48),
						JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
				{

					try 
					{
						Desktop.getDesktop().browse(new URI("http://www.tubemaster.net/down.html"));
						System.exit(0);
					} catch (Exception e1) {Commun.logError(e1);} 				
				}			
			}
		
		} catch (Exception e) {Commun.logError(e);}
	
	}
	
	
	
	
	
	
	

}

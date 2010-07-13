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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class Languages 
{
	private File f = new File(System.getProperty("user.dir")+File.separator+"lang"+File.separator+MainForm.opts.langFile);
	
	public String[] lang_table = new String[81]; 
	
	
	public Languages()
	{
	
		 try
	      {
		    String ligne;
		    String total = "";

		    BufferedReader lecteur = new BufferedReader(new InputStreamReader(new FileInputStream(this.f),"UTF-8"));
	    	while ((ligne = lecteur.readLine()) != null) total += ligne;
	   	    lecteur.close();

	   	    for (int i=0;i<this.lang_table.length;i++)
	   	    	this.lang_table[i] = Commun.parse(total,"<"+i+">","</"+i+">");
	   	    			
	   	    	
	      } catch(Exception e) 
	      {
	    	  JOptionPane.showMessageDialog(null,e.getLocalizedMessage()+"\n(You must have the adminitrator's rights !)","Error",JOptionPane.ERROR_MESSAGE);
	    	  System.exit(0);	    	  
	      }
		
		
		
	}

	
	

}

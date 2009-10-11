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

package Conversion;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ConversionPresets 
{
	
	private ArrayList<Preset> lesPresets = new ArrayList<Preset>();
	private ArrayList<String> lesCats = new ArrayList<String>();
	
	
	
	public ConversionPresets()
	{
		InputStream presetsFile = this.getClass().getResourceAsStream("presets.xml");
		InputStreamReader streamReader = new InputStreamReader(presetsFile);
		BufferedReader buffer = new BufferedReader(streamReader);
		String line="";
		try 
		{
			while ((line=buffer.readLine()) != null) //Parcours du fichier de presets
			{
				if (line.indexOf("<label>")>-1)
				{
					String l = this.parse(line, "<label>", "</label>");
					line=buffer.readLine();
					String p = this.parse(line, "<params>", "</params>");
					line=buffer.readLine();
					String e = this.parse(line, "<extension>", "</extension>");
					line=buffer.readLine();
					String c = this.parse(line, "<category>", "</category>");					
					this.lesPresets.add(new Preset(l,p,e,c)); //Ajout d'un preset
				}
			}
			
		} catch (IOException e) {}

		//Tri a bulles alphabétique pour les groupes
		for(int i=0;i<this.lesPresets.size();i++)
		{
			for(int j=0;j<this.lesPresets.size()-i-1;j++)
			{
				if (this.lesPresets.get(j).getCategory().toLowerCase().compareTo(this.lesPresets.get(j+1).getCategory().toLowerCase())>0)
				{
					Preset temp = this.lesPresets.get(j); 
					this.lesPresets.set(j, this.lesPresets.get(j+1));
					this.lesPresets.set(j+1, temp);
				}	
			}	
		}

		//Création du tableau de catégories triées.
		for (int i=0;i<this.lesPresets.size();i++)
		{
			if (this.lesCats.indexOf(this.lesPresets.get(i).getCategory())==-1)
				this.lesCats.add(this.lesPresets.get(i).getCategory());	
		}
	
	}
	
	
    public String parse(String chaine, String deb, String fin)
	{			
		chaine = chaine.substring(chaine.indexOf(deb));		
		return chaine.substring(chaine.indexOf(deb)+deb.length(),chaine.indexOf(fin));
	}
    
    
    public ArrayList<String> getCats()
    {
    	return this.lesCats;
    }
    
    
    public ArrayList<String> getItemsFromCat(String cat)
    {
    	ArrayList<String> list = new ArrayList<String>();
    	for (int i=0;i<this.lesPresets.size();i++)
    	{
    		if (this.lesPresets.get(i).getCategory().equals(cat))
    			list.add(this.lesPresets.get(i).getLabel());
    	}  	
    	return list;
    }
    
    public Preset getPreset(String label)
    {
    	Preset toRet = null;
    	for (int i=0;i<this.lesPresets.size();i++)
    	{
    		if (this.lesPresets.get(i).getLabel().equals(label))
    		{
    			toRet = this.lesPresets.get(i);
    			break;
    		}
    	} 
    	return toRet;   		
    }
    
	

}

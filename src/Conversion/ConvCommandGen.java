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

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import Graphique.TMButton;
import Main.FileCopier;
import Main.MainForm;



public class ConvCommandGen
{
	private String 		outTitle;
	private String 		inFile;
	private String 		choosenDir = "";
	private Component 	parent;
	

	public ConvCommandGen(String outTitle, String inFile, String choosenDir, Component parent)
	{
		this.outTitle = outTitle.replaceAll("[\\\\/:*?\"<>|]", "_");
		this.parent = parent;
					
		this.inFile = inFile;		
		if (choosenDir.equals("")) this.choosenDir = this.dirChooser();
		else this.choosenDir = choosenDir;
	}
	
	
	
	public String dirChooser()
	{
		String ret = "";
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(this.parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
            File file = fc.getSelectedFile();
            ret = file.getAbsolutePath();    	
		}		
		return ret;		
	}
	
	public boolean SaveNoConvert(boolean copy, JProgressBar pb, TMButton btn)
	{	
		if (!this.choosenDir.equals("")) 
		{
			File f_out = new File(this.choosenDir + File.separator + this.outTitle);
			
			if (f_out.exists())
			{
				JOptionPane.showMessageDialog(null, MainForm.lang.lang_table[41], MainForm.lang.lang_table[40],1);
				return false;
			}
			else
			{
				File f_in = new File(this.inFile);
				if (copy)
				{
					new FileCopier(f_in, f_out, pb, btn).start();
					return true;
				}
				else return f_in.renameTo(f_out);
			}	
		}
		else return false;
	}
	
	public boolean FLVtoMP3()
	{
		if (!this.choosenDir.equals(""))
		{
			new FLVtoMP3(this.inFile,this.choosenDir + File.separator + this.outTitle);
			return true;
		}
		else return false;
			
	}
	
	public String ConvertToPreset(Preset p)
	{
		String command = "";
		String params = p.getParams();
		String fileOut = this.outTitle;
		if (!this.choosenDir.equals(""))
		{
			fileOut = this.choosenDir + File.separator + fileOut;
			command = "ffmpeg -y -i \""+this.inFile+"\" "+params+" \""+fileOut+"\"";
		}
		return command;	
	}
	
	public String ConvertClassic(String params)
	{
		String command = "";
		String fileOut = this.outTitle;
		if (!this.choosenDir.equals(""))
		{
			fileOut = this.choosenDir + File.separator + fileOut;
			command = "ffmpeg -y -i \""+this.inFile+"\" "+params+" \""+fileOut+"\"";
		}
		return command;	
	}
}

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

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import Main.MainForm;


public class ConvertMenu extends JPopupMenu
{

	private static final long serialVersionUID = 1L;
	
	ActionListener listener;
	

	public ConvertMenu(ActionListener listener)
	{
		this.listener = listener;
		
		
		///// Items pour la conversion Classique
		JMenuItem avi1 = this.buildItem("Video AVI (320x240)",      "*Menu_Classic*", "convertvideo.png");
		JMenuItem avi2 = this.buildItem("Video AVI (640x480)",      "*Menu_Classic*", "convertvideo.png");
		JMenuItem avidivx = this.buildItem("Video AVI DiVX",        "*Menu_Classic*", "convertvideo.png");
		JMenuItem mp4 = this.buildItem("Video MP4 (PSP, Ipod ...)", "*Menu_Classic*",  "convertvideo.png");
		JMenuItem mp3 = this.buildItem("Audio MP3", 		        "*Menu_Classic*",  "convertmp3.png");

		///// Item pour l'extraction MP3
		JMenuItem extractMP3 = this.buildItem("Extract MP3 (Experts Only!)", "*Menu_ExtractMP3*", "convertmp3.png");
		

		
		this.add(avi1);
		this.add(avi2);
		this.add(avidivx);
		this.add(mp4);
		this.add(mp3);
		
		this.add(extractMP3);
		this.add(new JSeparator());
		
		///// Item pour le menu des Presets
		JMenu menuMore = new JMenu("More Presets");
		menuMore.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"moremenu.png"))));
		
		for(int i=0;i<MainForm.convPresets.getCats().size();i++)
		{
			String cat = MainForm.convPresets.getCats().get(i);
				
			JMenu subMenu = new JMenu(cat);
			for(int j=0;j<MainForm.convPresets.getItemsFromCat(cat).size();j++)
			{
				JMenuItem subitem = new JMenuItem(MainForm.convPresets.getItemsFromCat(cat).get(j));
				subitem.setActionCommand("*Menu_ConvPreset*");
				subitem.addActionListener(this.listener);
				subMenu.add(subitem);
			}
			
			menuMore.add(subMenu);
		}
		
		this.add(menuMore);
				
	}
	
	
	public JMenuItem buildItem(String caption, String cmd, String img)
	{
		JMenuItem item = new JMenuItem(caption);
		item.setActionCommand(cmd);
		item.addActionListener(this.listener);
		item.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+img)), ""));
		return item;		
	}
	
	
	
	
	

}

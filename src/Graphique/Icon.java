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

package Graphique;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.GrayFilter;
import javax.swing.JPanel;


public class Icon extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Image img;	//Image de l'icone.
	private Image original;


	
	//=====================================================================================================
	
	public Icon(Image img, boolean isGray)
	{
		this.original = img;
		this.img = img;
		if (isGray) this.img = GrayFilter.createDisabledImage(this.img);

	}
	
	//=====================================================================================================

	
	
	
	//=====================================================================================================
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this);
	}
	
	//=====================================================================================================
	
	
	
	//=====================================================================================================
	
	public void editImage(Image img)
	{
		this.img = img;
		this.revalidate();
		this.repaint();
	}
	
	//=====================================================================================================
	
	
	public void setGray(boolean b)
	{
		if (b) this.img = GrayFilter.createDisabledImage(this.original);
		else this.img = this.original;
		this.repaint();	
	}
	
	
}
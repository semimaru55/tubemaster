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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JPanel;

import Capture.ListFileItem;



public class ItemReducer extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	
	private ListFileItem item;	//Element de la liste.
	
	//Les différentes images
		private Image image;
		private Image imgReduce = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"reduce.png"));
		private Image imgMaximize = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"maximize.png"));
	/////////////////////////
	
		
		
	//=====================================================================================================	
		
	public ItemReducer(ListFileItem item)
	{
		this.item = item;
		this.image = imgReduce;
		this.addMouseListener(this);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	//=====================================================================================================
	
	
	
	
	//=====================================================================================================
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, this);
	}
	
	//=====================================================================================================

	
	
	
	//=====================================================================================================
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) 
	{
		if (this.image.equals(this.imgReduce)) this.reduce();
		else this.maximize();	
		this.repaint();	
	}

	//=====================================================================================================
	
	public void reduce()
	{
		this.image = this.imgMaximize;
		this.item.reduce();		
	}
	
	public void maximize()
	{
		this.image = this.imgReduce;
		this.item.maximize();	
	}

}

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
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MP3Search.ListMP3Item;
import Main.MainForm;

public class ItemMP3Closer extends JPanel implements MouseListener
{
	
private static final long serialVersionUID = 1L;
	
	private ListMP3Item item;	//Element de la liste.
	
	
	//=====================================================================================================
	
	public ItemMP3Closer(ListMP3Item item)
	{
		this.item = item;
		this.addMouseListener(this);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	//=====================================================================================================

	
	
	
	//=====================================================================================================

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"close.png")), 0, 0, this);
	}

	//=====================================================================================================

	
	
	
	//=====================================================================================================
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) 
	{
		if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table.get(43), MainForm.lang.lang_table.get(37),
												JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
		{

			this.item.stopDown();
			this.repaint();
		
		}	
	}
	
	//=====================================================================================================
	
	

}

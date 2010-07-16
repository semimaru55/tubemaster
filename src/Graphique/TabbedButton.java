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


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class TabbedButton extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	
	private boolean active = false;
	private boolean over = false;
	private JLabel lblTitle;
	private Icon icone;
	
	public TabbedButton(String title, String img)
	{
		super();
		this.setLayout(null);
		this.setSize(new Dimension(165,30));
		this.setBackground(Color.decode("#515151"));
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.decode("#85878C")));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.addMouseListener(this);
		
		this.lblTitle = new JLabel(title);
		this.lblTitle.setFont(new java.awt.Font("Default_tm", 0, 12));
		this.lblTitle.setForeground(Color.decode("#85878C"));
		this.lblTitle.setBounds(2,3,130,27);
		this.lblTitle.setHorizontalAlignment(JLabel.CENTER);
		
		
		this.icone = new Icon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+img)),true);
		this.icone.setBounds(130,1,32,32);
		this.icone.setOpaque(false);
		this.icone.setEnabled(false);
		
		
		this.add(this.lblTitle);
		this.add(this.icone);

	}
	
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public void setActive(boolean state)
	{
		if (this.active != state)
		{
			this.active = state;
			
			if (this.active)
			{
				this.setLocation(this.getLocation().x, this.getLocation().y+1);
				this.setBackground(Color.decode("#676767"));
				this.lblTitle.setForeground(Color.white);
				this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.decode("#000000")));
				this.icone.setGray(false);
				
			}
			else
			{
				this.setLocation(this.getLocation().x, this.getLocation().y-1);	
				this.setBackground(Color.decode("#515151"));
				this.lblTitle.setForeground(Color.decode("#85878C"));
				this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.decode("#85878C")));
				this.over = false;
				this.icone.setGray(true);
			}
		}
	}
	

	
	//=====================================================================================================

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if ((this.over) && (!this.active))
			g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"tab_MOver.png")), 1, 1, this);
		else
		if(!this.active)
			g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"tab_MOut.png")), 1, 1, this);

	}

	//=====================================================================================================
	
	
	
	

	public void mouseClicked(MouseEvent arg0) 
	{
		
	}

	public void mouseEntered(MouseEvent arg0) 
	{
		this.over = true;
		this.lblTitle.setForeground(Color.white);
		this.icone.setGray(false);
		this.repaint();	
	}


	public void mouseExited(MouseEvent arg0) 
	{
		if (!this.active) 
		{
			this.over = false;
			this.lblTitle.setForeground(Color.decode("#85878C"));
			this.icone.setGray(true);
			this.repaint();
		}
	}


	public void mousePressed(MouseEvent arg0) {}


	public void mouseReleased(MouseEvent arg0)
	{
		if (!this.active) 
		{
			((Header)this.getParent()).refreshTabs(this);
		}
	}
	
	

}

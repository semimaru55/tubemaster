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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Main.Commun;



public class TMButton extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private int state = 0; 						//0:Normal/1:Down/2:Up/3:Over
	private ActionListener listener;			//Ecouter les actions
	private int imgX;							//Abscisse de l'image
	private int imgY;							//Ordonnee de l'image
	private int lblX;
	private int lblY;
	private int lblW;
	private String caption;						//Caption du bouton
	private Image img;							//Image du bouton
	private JLabel lblCaption = new JLabel();	//Label du caption
	private boolean isOut = true;
	private boolean isFlat = false;
	private int mouseX = 0;
	private int mouseY = 0;

	
	
	
	
	public TMButton(ActionListener listener, int imgX, int imgY, String caption, String img, int lblX, int lblY, int lblW)
	{
		super();
		this.setLayout(null);
		this.setBorder(BorderFactory.createLineBorder(Color.gray.darker().darker()));
		this.setOpaque(false);
		this.addMouseListener(this);
		this.listener = listener;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		
		

		this.imgX = imgX;
		this.imgY = imgY;
		this.lblX = lblX;
		this.lblY = lblY;
		this.lblW = lblW;
		
		
		this.caption = caption;
		this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+img));
		
		this.lblCaption.setText(this.caption);
		this.lblCaption.setForeground(Color.white);
		this.lblCaption.setBounds(lblX,lblY,lblW,20);
		this.lblCaption.setHorizontalAlignment(JLabel.CENTER);
		this.lblCaption.setFont(Commun.tm_font12);
		//this.lblCaption.setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.add(this.lblCaption);
		
	
	}
	
	public void paintComponent(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D)g;
		Paint old = g2.getPaint();
		GradientPaint grad = null;

		if (this.state==1) grad = new GradientPaint(0, 0, Color.decode("#676767").darker().darker(), 0, getHeight(),Color.white.darker() );
		else grad = new GradientPaint(0, 0, Color.white.darker(), 0, getHeight(), Color.decode("#676767").darker().darker());
		
		if (!this.isFlat)
		{
			g2.setPaint(grad);
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setPaint(old);
		}
		
		if (this.state==1) g.drawImage(this.img, this.imgX, this.imgY+2, this);
		else g.drawImage(this.img, this.imgX, this.imgY, this);

		super.paintComponent(g);
	}

	public void mouseClicked(MouseEvent arg0) 
	{
		
	}


	public void mouseEntered(MouseEvent arg0) 
	{
		if (this.isEnabled())
		{
			this.isOut = false;
			if (!this.isFlat) this.setBorder(BorderFactory.createLineBorder(Color.white.darker().darker()));
		}
		
	}

	public void mouseExited(MouseEvent arg0) 
	{
		if (this.isEnabled())
		{
			this.isOut = true;
			if (!this.isFlat) this.setBorder(BorderFactory.createLineBorder(Color.gray.darker().darker()));
			this.state = 0;
			this.repaint();
			this.lblCaption.setBounds(lblX,lblY,lblW,20);
		}
		
	}


	public void mousePressed(MouseEvent arg0) 
	{
		if (this.isEnabled())
		{
			this.state = 1;
			this.repaint();
			this.lblCaption.setBounds(lblX,lblY+2,lblW,20);
		}
		
	}


	public void mouseReleased(MouseEvent e) 
	{
		if ((this.isEnabled()) && (!this.isOut))
		{
			this.mouseX = e.getX();
			this.mouseY = e.getY();
			this.state = 2;
			this.repaint();
			this.lblCaption.setBounds(lblX,lblY,lblW,20);
			ActionEvent ev = new ActionEvent(this,0,"");
			this.listener.actionPerformed(ev);
			
		}	
	}
	
	public void changeImage(String img)
	{
		this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+img));;
		this.repaint();
	}
	
	public void changeText(String txt)
	{
		this.lblCaption.setText(txt);
	}
	
	public String getText() {return this.lblCaption.getText();}
	
	public int getLastX() {return this.mouseX;}
	public int getLastY() {return this.mouseY;}
	
	public void setFlat() 
	{
		this.isFlat = true;
		this.setBorder(BorderFactory.createEmptyBorder());
	}
	public void setTextColor(Color clr) {this.lblCaption.setForeground(clr);}
	

}

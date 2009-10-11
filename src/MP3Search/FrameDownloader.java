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

package MP3Search;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import Main.MainForm;



public class FrameDownloader extends JFrame implements WindowListener
{

	private static final long serialVersionUID = 1L;

	private JPanel panFen = new JPanel() //Panel de la fenetre.
	{

		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"downloader.png")), -10, -10, this);
		}
	
	}; 				
	
	
	private String url = "";
	private String filetitle = "";
	private String filedir = "";
	
	private JProgressBar pbar = new JProgressBar();
	private JLabel lblTitle = new JLabel();
	private JTextField edtDir = new JTextField();
	private JLabel lblStatus = new JLabel();
	
	private FileDownloader fdown;
	

	public FrameDownloader(String url, String filetitle, String filedir, boolean isFromYoutube)
	{
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#676767"));
		this.setTitle("TM++ Downloader");
		this.setSize(500, 170);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		ImageIcon monIcon= new ImageIcon(getClass().getResource("images/"+"icon.jpg"));
		this.setIconImage(monIcon.getImage());
		
		this.url = url;
		this.filetitle = filetitle;
		this.filedir = filedir;
		
		
		this.placeComposants();
		
		this.fdown = new FileDownloader(this.url,this.filedir,this.filetitle,this.pbar,this.lblStatus,this,isFromYoutube);
		Thread threadManager = new Thread(this.fdown);
		threadManager.start();
		
		this.setVisible(true);
		
	}
	
	public void placeComposants()
	{

		this.pbar.setBounds(14,109,464,20);
		this.pbar.setValue(0);
		

		this.lblTitle.setBounds(110,16,375,20);
		this.lblTitle.setForeground(Color.white);
		this.lblTitle.setFont(new Font("Default_tm", Font.BOLD, 13));
		this.lblTitle.setText(this.filetitle);
		
		this.edtDir.setBounds(110,44,369,20);
		this.edtDir.setBackground(Color.gray.brighter());
		this.edtDir.setBorder(BorderFactory.createLineBorder(Color.black));
		this.edtDir.setEditable(false);
		this.edtDir.setText(" "+this.filedir);
		
		this.lblStatus.setBounds(110,69,375,20);
		this.lblStatus.setForeground(Color.white);
		this.lblStatus.setFont(new Font("Default_tm", Font.BOLD, 10));
		this.lblStatus.setText(MainForm.lang.lang_table.get(52)+" ...");

		
		this.panFen.add(this.pbar);
		this.panFen.add(this.lblTitle);
		this.panFen.add(this.edtDir);
		this.panFen.add(this.lblStatus);
		
	}
	



	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}


	public void windowClosing(WindowEvent e) 
	{
		if (!this.fdown.isFinished())
		{
		
			if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table.get(42), MainForm.lang.lang_table.get(37),
					JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			{
	
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				this.setVisible(false);
				this.fdown.stopDownload();
								
			}	else this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		}
			
	}


	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

}

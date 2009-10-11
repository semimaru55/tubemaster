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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Graphique.TMButton;

public class FenOptions extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panFen = new JPanel(); //Panel de la fenetre.

	private File rep_lang = new File("lang");
	
	
	private JCheckBox chkAutoCapture = new JCheckBox();
	private JCheckBox chkUpdate = new JCheckBox();
	private JCheckBox chkTray = new JCheckBox();
	private JCheckBox chkDelConv = new JCheckBox();
	private JCheckBox chkCloseBox = new JCheckBox();
	
	
	private JPanel grpDefRep = new JPanel();
	private JTextField edtDefRep = new JTextField();
	private TMButton btnDefRep = new TMButton(this,0,0,"...","",0,0,35);
	private JCheckBox chkAutoConv = new JCheckBox();
	private JComboBox cmbAutoConv = new JComboBox();
	
	private JPanel grpLang = new JPanel();
	private JComboBox cmbLang = new JComboBox(this.rep_lang.list());
	private TMButton btnLang = new TMButton(this,0,0,"Can you translate in your own language ?","",0,0,268);

	private JPanel grpBitrate = new JPanel();
	private String[] bitrates = {"64","128","224","192","320"};
	private JComboBox cmbBitrate = new JComboBox(this.bitrates);
	private TMButton btnClose = new TMButton(this,0,0,MainForm.lang.lang_table.get(56),"",0,4,100);
	
	public FenOptions()
	{
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#676767"));
		this.setTitle("TubeMaster++ Configuration");
		this.setSize(471, 358);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		ImageIcon monIcon= new ImageIcon(getClass().getResource("images/"+"icon.jpg"));
		this.setIconImage(monIcon.getImage());
			
		
		this.chkAutoCapture.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkAutoCapture.setBounds(5,5,455,20);
		this.chkAutoCapture.setText(MainForm.lang.lang_table.get(19));
		this.chkAutoCapture.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkAutoCapture.setOpaque(false);
		this.chkAutoCapture.setForeground(Color.white);
		this.chkAutoCapture.setSelected(MainForm.opts.autoCapture);
		this.chkAutoCapture.addActionListener(this);
		
		this.chkUpdate.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkUpdate.setBounds(5,25,455,20);
		this.chkUpdate.setText(MainForm.lang.lang_table.get(20));
		this.chkUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkUpdate.setOpaque(false);
		this.chkUpdate.setForeground(Color.white);
		this.chkUpdate.setSelected(MainForm.opts.autoUpdate);
		this.chkUpdate.addActionListener(this);
		
		this.chkTray.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkTray.setBounds(5,45,455,20);
		this.chkTray.setText(MainForm.lang.lang_table.get(21));
		this.chkTray.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkTray.setOpaque(false);
		this.chkTray.setForeground(Color.white);
		this.chkTray.setSelected(MainForm.opts.trayIcon);
		this.chkTray.addActionListener(this);
		
		this.chkDelConv.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkDelConv.setBounds(5,65,455,20);
		this.chkDelConv.setText(MainForm.lang.lang_table.get(22));
		this.chkDelConv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkDelConv.setOpaque(false);
		this.chkDelConv.setForeground(Color.white);
		this.chkDelConv.setSelected(MainForm.opts.delConv);
		this.chkDelConv.addActionListener(this);
		
		this.chkCloseBox.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkCloseBox.setBounds(5,85,455,20);
		this.chkCloseBox.setText(MainForm.lang.lang_table.get(55));
		this.chkCloseBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkCloseBox.setOpaque(false);
		this.chkCloseBox.setForeground(Color.white);
		this.chkCloseBox.setSelected(MainForm.opts.closeBox);
		this.chkCloseBox.addActionListener(this);
		
		this.grpLang.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpLang.setBounds(5,110,455,50);
		this.grpLang.setOpaque(false);
		this.grpLang.setForeground(Color.white);
		TitledBorder b1 = BorderFactory.createTitledBorder(" "+MainForm.lang.lang_table.get(23)+" ");
		b1.setTitleColor(Color.white);
		b1.setTitleFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpLang.setBorder(b1);
		this.grpLang.setLayout(null);
		this.cmbLang.setBounds(10,19,160,20);
		this.btnLang.setBounds(178,19,268,20);
		this.btnLang.setFlat();
		
		this.grpLang.add(this.cmbLang);
		this.grpLang.add(this.btnLang);
		
		this.grpDefRep.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpDefRep.setBounds(5,165,455,100);
		this.grpDefRep.setOpaque(false);
		this.grpDefRep.setForeground(Color.white);
		TitledBorder b2 = BorderFactory.createTitledBorder(" "+MainForm.lang.lang_table.get(24)+" ");
		b2.setTitleColor(Color.white);
		b2.setTitleFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpDefRep.setBorder(b2);
		this.grpDefRep.setLayout(null);
		this.edtDefRep.setBounds(10,19,393,21);
		this.edtDefRep.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.edtDefRep.setEditable(false);
		this.edtDefRep.setText(MainForm.opts.defRep);
		this.btnDefRep.setBounds(410,19,35,20);
		
		this.chkAutoConv.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.chkAutoConv.setBounds(5,45,455,20);
		this.chkAutoConv.setText(MainForm.lang.lang_table.get(25));
		this.chkAutoConv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.chkAutoConv.setOpaque(false);
		this.chkAutoConv.setForeground(Color.white);
		this.chkAutoConv.setSelected(MainForm.opts.autoConv);
		this.chkAutoConv.addActionListener(this);
		this.cmbAutoConv.setBounds(9,70,436,20);
		if (this.edtDefRep.getText().equals(""))
		{
			this.chkAutoConv.setEnabled(false);
			this.chkAutoConv.setSelected(false);	
		}
		
		this.cmbAutoConv.setEnabled(this.chkAutoConv.isSelected());
		this.cmbAutoConv.setMaximumRowCount(12);
		
		this.cmbAutoConv.addItem("Save Original File");
		this.cmbAutoConv.addItem("Video AVI (320x240)");
		this.cmbAutoConv.addItem("Video AVI (640x480)");
		this.cmbAutoConv.addItem("Video AVI DiVX");
		this.cmbAutoConv.addItem("Video MP4 (PSP, Ipod ...)");
		this.cmbAutoConv.addItem("Audio MP3");
		this.cmbAutoConv.addItem("Extract MP3 (Experts Only!)");
		
		for(int i=0;i<MainForm.convPresets.getCats().size();i++)
		{
			String cat = MainForm.convPresets.getCats().get(i);	
			for(int j=0;j<MainForm.convPresets.getItemsFromCat(cat).size();j++)
				this.cmbAutoConv.addItem(MainForm.convPresets.getItemsFromCat(cat).get(j));	
		}
		
		this.cmbAutoConv.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.cmbAutoConv.addActionListener(this);
		this.cmbAutoConv.setSelectedItem(MainForm.opts.autoConvPreset);
		
		this.cmbLang.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.cmbLang.setSelectedItem(MainForm.opts.langFile);
		this.cmbLang.addActionListener(this);
		this.cmbLang.setMaximumRowCount(20);
		
		
		this.grpBitrate.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpBitrate.setBounds(5,270,130,55);
		this.grpBitrate.setOpaque(false);
		this.grpBitrate.setForeground(Color.white);
		TitledBorder b3 = BorderFactory.createTitledBorder(" MP3 Bitrate (Kbps) ");
		b3.setTitleColor(Color.white);
		b3.setTitleFont(new java.awt.Font("Default_tm", 0, 11));
		this.grpBitrate.setBorder(b3);
		this.grpBitrate.setLayout(null);
		
		this.cmbBitrate.setFont(new java.awt.Font("Default_tm", 0, 11));
		this.cmbBitrate.addActionListener(this);
		this.cmbBitrate.setBounds(19,20,90,20);
		
		this.grpBitrate.add(this.cmbBitrate);
		this.cmbBitrate.setSelectedItem(MainForm.opts.bitrate);
		
		this.grpDefRep.add(this.btnDefRep);
		this.grpDefRep.add(this.edtDefRep);
		this.grpDefRep.add(this.chkAutoConv);
		this.grpDefRep.add(this.cmbAutoConv);
		
		this.btnClose.setBounds(350,290,100,30);
		
		
						
		this.panFen.add(this.chkAutoCapture);
		this.panFen.add(this.chkUpdate);
		this.panFen.add(this.chkTray);
		this.panFen.add(this.chkDelConv);
		this.panFen.add(this.chkCloseBox);
		this.panFen.add(this.grpDefRep);
		this.panFen.add(this.grpLang);
		this.panFen.add(this.grpBitrate);
		this.panFen.add(this.btnClose);
	}



	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(this.chkAutoCapture)) MainForm.opts.autoCapture = this.chkAutoCapture.isSelected();
		else
		if (e.getSource().equals(this.chkUpdate)) MainForm.opts.autoUpdate = this.chkUpdate.isSelected();
		else
		if (e.getSource().equals(this.chkTray)) MainForm.opts.trayIcon = this.chkTray.isSelected();
		else
		if (e.getSource().equals(this.chkDelConv)) MainForm.opts.delConv = this.chkDelConv.isSelected();
		else
		if (e.getSource().equals(this.chkCloseBox)) MainForm.opts.closeBox = this.chkCloseBox.isSelected();
		else
		if(e.getSource().equals(this.btnDefRep))
		{
			String rep = this.dirChooser();
			if (!rep.equals(""))
			{
				this.edtDefRep.setText(rep);
				MainForm.opts.defRep = rep;
				this.chkAutoConv.setEnabled(true);
			}
			
		}
		else
		if (e.getSource().equals(this.chkAutoConv)) 
		{
			MainForm.opts.autoConv = this.chkAutoConv.isSelected();
			this.cmbAutoConv.setEnabled(this.chkAutoConv.isSelected());	
		}
		else
		if (e.getSource().equals(this.cmbAutoConv)) MainForm.opts.autoConvPreset = (String) this.cmbAutoConv.getSelectedItem();
		else
		if (e.getSource().equals(this.cmbLang)) 
		{
			MainForm.opts.langFile = (String) this.cmbLang.getSelectedItem();
			this.cmbBitrate.requestFocus();
			JOptionPane.showMessageDialog(this, "Now, you must restart TubeMaster++ to change the application language.","Please Restart",1);
		}
		else
		if (e.getSource().equals(this.btnLang))
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("http://tubemaster.free.fr/contact.html"));
			} catch (Exception e1) {Commun.logError(e1);}
		}
		else
		if (e.getSource().equals(this.cmbBitrate)) 
			MainForm.opts.bitrate = (String) this.cmbBitrate.getSelectedItem();
		else
		if (e.getSource().equals(this.btnClose)) this.dispose();

		
	
		MainForm.opts.WriteFile();	
	}
	
	
	public String dirChooser()
	{
		String ret = "";
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
            File file = fc.getSelectedFile();
            ret = file.getAbsolutePath();    	
		}		
		return ret;		
	}
	

}

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

package Capture;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

import Conversion.CommandRunner;
import Conversion.ConversionClassic;
import Conversion.ConvertMenu;
import Conversion.ConvCommandGen;
import Conversion.Preset;
import Graphique.Icon;
import Graphique.ItemCloser;
import Graphique.ItemReducer;
import Graphique.TMButton;
import Main.Commun;
import Main.FenID3;
import Main.MainForm;
import java.awt.TrayIcon;
import java.io.File;
import java.util.Calendar;



public class ListFileItem extends JPanel implements ActionListener, FocusListener, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private static final int ITEM_WIDTH 	= 668;				
	private static final int ITEM_HEIGHT 	= 75;
	
	
	private int currentHeight = ITEM_HEIGHT;
	private ListFile parentList;
	private Icon icone;										
	private Icon icoState;
	private ItemReducer imgReduce = new ItemReducer(this);	
	private ItemCloser imgClose = new ItemCloser(this);		
	private JProgressBar pbProgress = new JProgressBar();
	private JProgressBar pbTemp = new JProgressBar();
	private JTextField edtTitle = new JTextField();		
	private JLabel lblState = new JLabel();				
	private JCheckBox chkSel = new JCheckBox();				
	private TMButton btnStop = new TMButton(this,7,2,"","stopconv.png",0,0,0);
	private TMButton btnSave = new TMButton(this,6,4,MainForm.lang.lang_table[26],"save_as.png",26,2,89);				
	private TMButton btnConvert = new TMButton(this,6,4,MainForm.lang.lang_table[27],"convert.png",26,2,89);
	private TMButton btnPlay = new TMButton(this,6,4,MainForm.lang.lang_table[28],"preview.png",26,2,89);
	private TMButton btnTags = new TMButton(this,5,2,"MP3 Tags","id3.png",26,2,89);
	private TMButton btnPrev = new TMButton(this,7,2,"","preview.png",0,0,0);
	private TMButton btnSaveTemp = new TMButton(this,8,3,"","save_as.png",0,0,0);
	private JTextField edtUrl = new JTextField();
	private ConvertMenu menuConvert = new ConvertMenu(this);	
	
	
	private boolean 		alive = true;							
	private StreamFile 		file;									
	private Timer 			timerRefresh = new Timer(1000,this);		
	private CommandRunner 	cmd = null;						
	private boolean 		isFullReady = false;					
	private boolean 		isWaitingFor = false;					
	private boolean 		autoConverted = false;					
	private boolean 		wasDragged = false;
	private boolean 		wasAutoPlayed = false;
	private String 			url;
	private int				real_speed 			= 0;
	private int 			prev_speed 			= 0;
	private float[] 		tab_real_speed	 	= new float[5];
	private int 			speed_index 		= 0;
	private Timer 			timerTimeout 		= this.init_timer();
	private int 			timeout 			= Integer.parseInt(MainForm.opts.timeout);
	private String 			extension;
	
	
	//=====================================================================================================
	
	public ListFileItem(ListFile parentList, StreamFile file, String url)
	{	
		super();
		this.setLayout(null);
		this.setBackground(Color.decode("#FFFFE1"));
		this.setPreferredSize(new Dimension (ITEM_WIDTH,ITEM_HEIGHT));
		Border grayline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		this.setBorder(grayline);
		this.addMouseListener(this);
		this.parentList = parentList;
		this.file = file;
		this.url = url;
		this.extension = "." + this.file.get_format().retFormat().toLowerCase();
		this.icone = new Icon(this.file.get_format().retLogo(),false);
		this.placeComposants();
		this.timerTimeout.start();	
	}

	//=====================================================================================================
	
	public void reduce()
	{
		this.setPreferredSize(new Dimension (ITEM_WIDTH,25));
		this.currentHeight = 25;
		this.parentList.bigRefresh();		
	}
	
	//=====================================================================================================
	
	public void maximize()
	{
		this.setPreferredSize(new Dimension (ITEM_WIDTH,ITEM_HEIGHT));
		this.currentHeight = ITEM_HEIGHT;
		this.parentList.bigRefresh();
	}

	//=====================================================================================================

	private Timer init_timer()
	{
		ActionListener act = new ActionListener()
		{
			public void actionPerformed (ActionEvent event)
			{
				if (timeout > 0) timeout--;
				else
				if (timeout == 0) toDestroy();  
			}
		};
		return new Timer (1000, act);
	}

	//=====================================================================================================
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(this.btnStop))
		{
			if (JOptionPane.showConfirmDialog(null, MainForm.lang.lang_table[38], MainForm.lang.lang_table[37],
					JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)	
					this.cmd.stopProcess();
		}	
		else
		if (e.getSource().equals(this.timerRefresh)) this.refreshDown();		
		else
		if (e.getSource().equals(this.btnSave)) this.runConversionSave(MainForm.opts.defRep,false,"");	
		else
		if (e.getSource().equals(this.btnConvert))
			this.menuConvert.show(this.btnConvert,this.btnConvert.getLastX(),this.btnConvert.getLastY());					
		else
		if (e.getSource().equals(this.btnPlay)) this.file.play();	
		else
		if (e.getActionCommand().equals("*Menu_ExtractMP3*")) this.runConversionExtractMP3(MainForm.opts.defRep);		
		else
		if (e.getActionCommand().equals("*Menu_ConvPreset*")) 
			this.runConversionPreset(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
		else
		if (e.getActionCommand().equals("*Menu_Classic*")) 
			this.runConversionClassic(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
		else
		if (e.getSource().equals(this.btnTags)) new FenID3(this.file.get_filepath(),this.edtTitle,this.btnTags);	
		else
		if (e.getSource().equals(this.btnPrev)) this.file.play();
		else
		if (e.getSource().equals(this.btnSaveTemp))
		{
			
			if (this.edtTitle.getText().equals(MainForm.lang.lang_table[29]))
			{
				CaptureSystem.unNamedId++;
				this.edtTitle.setText("Untitled_"+CaptureSystem.unNamedId);
			}
			
			Calendar laDate = Calendar.getInstance();
			String d = "_"+laDate.get(Calendar.HOUR_OF_DAY)+"."
							   +laDate.get(Calendar.MINUTE)+"."
							   +laDate.get(Calendar.SECOND)+"-"
							   +laDate.get(Calendar.DAY_OF_MONTH)+"."
							   +(laDate.get(Calendar.MONTH)+1)+"."
							   +laDate.get(Calendar.YEAR);
			runConversionSave(MainForm.opts.defRep, true, d);
		}
	}
	
	//=====================================================================================================
	
	public void runConversionSave(String dir, boolean isTempSave, String salt)
	{
		String fileName = this.edtTitle.getText()+salt;		
		File f = new File(dir + File.separator + fileName + this.extension);
		if (f.exists()) fileName += " - New";
		fileName += this.extension;
		
		ConvCommandGen conv = new ConvCommandGen(fileName,this.file.get_filepath(),dir,this);
		if ((conv.SaveNoConvert(isTempSave, this.pbTemp, this.btnSaveTemp) && (!isTempSave))) this.toDestroy();  
	}

	public void runConversionExtractMP3(String dir)
	{
		String fileName = this.edtTitle.getText();
		File f = new File(dir + File.separator + fileName + ".mp3");
		if (f.exists()) fileName += " - New";
		fileName += ".mp3";
		
		ConvCommandGen conv = new ConvCommandGen(fileName,this.file.get_filepath(),dir,this);
		if (conv.FLVtoMP3())
			if (MainForm.opts.delConv) this.toDestroy();
	}
	
	public void runConversionClassic(String dir, String preset)
	{
		ConversionClassic classic = new ConversionClassic(preset);
		String fileName = this.edtTitle.getText();	
		File f = new File(dir + File.separator + fileName + "."+classic.getExtension());
		if (f.exists()) fileName += " - New";
		fileName += "."+classic.getExtension();
		
		ConvCommandGen conv = new ConvCommandGen(fileName,this.file.get_filepath(),dir,this);
		String command = conv.ConvertClassic(classic.getCommand());
		if (!command.equals(""))
		{
			this.cmd = new CommandRunner(command,this.lblState,this.pbProgress,this,null);
			MainForm.convManager.addItem(this);
		}
	}
	
	public void runConversionPreset(String dir, String preset)
	{
		Preset p = MainForm.convPresets.getPreset(preset);
		String fileName = this.edtTitle.getText();		
		File f = new File(dir + File.separator + fileName + "." + p.getExtension());
		if (f.exists()) fileName += " - New";
		fileName += "." + p.getExtension();
		
		
		ConvCommandGen conv = new ConvCommandGen(fileName,this.file.get_filepath(),dir,this);
		String command = conv.ConvertToPreset(p);
		if (!command.equals(""))
		{
			this.setConverting();
			this.cmd = new CommandRunner(command,this.lblState,this.pbProgress,this,null);
			MainForm.convManager.addItem(this);
			
		}
	}

	//=====================================================================================================
	
	public void toDestroy()
	{
		if (this.cmd != null) this.cmd.stopProcess();
		this.timerRefresh.stop();
		this.timerTimeout.stop();
		this.file.cancel();		
		this.alive = false;
		this.parentList.bigRefresh();		
	}
	
	//=====================================================================================================
	
	public void refreshDown()
	{
		if (this.file.is_cancelled()) this.toDestroy();
		else
		if (this.file.is_complete())
		{
			this.timerRefresh.stop();
			this.timerTimeout.stop();
			this.setFinished(); 		
		}
		else
		{
			long totalSize = this.file.get_content_length();
			long curSize = this.file.get_current_length();
			
			this.pbProgress.setMaximum((int) totalSize);
			this.pbProgress.setValue((int) curSize);	
			String nfoSize = Commun.sizeConvert((int) curSize) + " / " +  Commun.sizeConvert((int) totalSize);
			
			this.real_speed = (int) (curSize - this.prev_speed);
			
			
			String speed = "";
			float avg_speed = this.get_average_speed();	
			if ((avg_speed > 0) || (this.timeout == -1))
			{
				this.timeout = Integer.parseInt(MainForm.opts.timeout);
				speed = "at " + Commun.sizeConvert(Math.round(avg_speed)) + "/s";
			}
			else speed = "Timeout = " + this.timeout + "s";
			

			this.prev_speed = (int) curSize;		
			this.lblState.setText(nfoSize + " (" + speed + ").");
			
			if ((this.edtTitle.getText().equals(MainForm.lang.lang_table[29])) && (!this.edtTitle.hasFocus())
				&& (this.file.get_format().retFormat().equals("MP3")))
			{
				String title = this.file.find_ID3();
				if (title.equals(" - ")) this.edtTitle.setText(MainForm.lang.lang_table[29]);
				else this.edtTitle.setText(title);
			}
			
			if ((MainForm.opts.autoPlay) && (!this.wasAutoPlayed) && (curSize>400000))
			{
				this.wasAutoPlayed = true;
				this.file.play();	
			}	
		}	
	}
	
	//=====================================================================================================
	
	private float get_average_speed()
	{
		float average_speed = 0;
		this.tab_real_speed[this.speed_index] = this.real_speed;  	
		for (int i=0; i<this.tab_real_speed.length; i++) average_speed += this.tab_real_speed[i];  		
		average_speed = (average_speed / this.tab_real_speed.length);
		this.speed_index = (this.speed_index + 1) % (this.tab_real_speed.length-1);  	
		return average_speed;
	}
	
	//=====================================================================================================
	
	public void setFinished()
	{
		if (this.edtTitle.getText().equals(MainForm.lang.lang_table[29]))
		{
			CaptureSystem.unNamedId++;
			this.edtTitle.setText("Untitled_"+CaptureSystem.unNamedId);
		}
			
		
		this.isWaitingFor = false;
		this.icoState.editImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"download_done.png")));
		this.lblState.setText(MainForm.lang.lang_table[30]+" ("+Commun.sizeConvert(this.file.get_content_length())+").");
		this.pbProgress.setVisible(false);
		this.pbProgress.setBounds(7,28,617,24);
		this.btnSave.setVisible(true);
		this.btnPlay.setVisible(true);
		if (this.file.get_format().retFormat().equals("MP3")) this.btnTags.setVisible(true);
		this.btnPrev.setVisible(false);
		this.btnSaveTemp.setVisible(false);
		this.btnConvert.setVisible(true);
		this.btnStop.setVisible(false);
		this.isFullReady = true;
		this.chkSel.setVisible(true);
		this.imgReduce.setVisible(true);
		this.pbTemp.setVisible(false);
	
		
		if ((MainForm.opts.autoConv) && (!this.autoConverted) && (!this.wasDragged))
		{
			this.autoConverted = true;
			String preset = MainForm.opts.autoConvPreset;
			ConversionClassic classic = new ConversionClassic(preset);
			
			if (classic.isClassic()) this.runConversionClassic(MainForm.opts.defRep,preset);
			else			
			if (preset.equals("Save Original File")) this.runConversionSave(MainForm.opts.defRep,false,"");
			else
			if (preset.equals("Extract MP3 (Experts Only!)")) this.runConversionExtractMP3(MainForm.opts.defRep);	
			else
				this.runConversionPreset(preset,MainForm.opts.defRep);	
		}
	}
	
	public void setConverting()
	{
		this.icoState.editImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"converting.png")));
		this.pbProgress.setMaximum(100);
		this.pbProgress.setVisible(true);
		this.pbProgress.setValue(0);
		this.btnSave.setVisible(false);
		this.btnPlay.setVisible(false);
		this.btnTags.setVisible(false);
		this.btnConvert.setVisible(false);
		this.btnStop.setVisible(true);
		this.isFullReady = false;
		this.chkSel.setVisible(false);
		this.imgReduce.setVisible(true);
		this.maximize();
	}
	
	public void setWaitingFor()
	{
		this.isWaitingFor = true;
		this.isFullReady = false;
		this.reduce();
		this.lblState.setText(MainForm.lang.lang_table[31]);
		this.imgReduce.setVisible(false);
		this.chkSel.setVisible(false);
	}
	
	//=====================================================================================================
	
	public void focusGained(FocusEvent e) 
	{
		if (e.getSource().equals(this.edtTitle))
		{
			this.edtTitle.setOpaque(true);
			this.edtTitle.setBorder(BorderFactory.createLineBorder(Color.black));
			this.edtTitle.setFont(new Font("Tahoma", 0, 12));
			this.edtTitle.selectAll();
		}
	
	}

	//=====================================================================================================
	
	public void focusLost(FocusEvent e) 
	{
		if (e.getSource().equals(this.edtTitle))
		{
			this.edtTitle.setOpaque(false);
			this.edtTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
			this.edtTitle.setFont(new Font("Tahoma", Font.BOLD, 12));

		}	
	}

	//=====================================================================================================
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) 
	{
		if (e.getSource().equals(this.edtTitle))
		{
			if (e.getKeyCode()==10)	
			{
				this.lblState.requestFocus();
			}
		}	
	}
	
	//=====================================================================================================
	
	public void placeComposants()
	{
		
		this.icoState = new Icon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/"+"download_mini.png")),false);
		
		this.icone.setBounds(25,6,37,13);
		this.icoState.setBounds(3,3,18,18);
		this.imgReduce.setBounds(629,5,14,14);
		this.imgClose.setBounds(648,5,14,14);
		this.pbProgress.setBounds(7,28,579,24);
		this.edtUrl.setBounds(7,54,655,18);
		this.edtTitle.setBounds(68,3,310,19);
		this.lblState.setBounds(384,2,220,20);
		this.btnStop.setBounds(629,28,33,23);
		this.chkSel.setBounds(607,2,20,20);
		this.btnPrev.setBounds(629,28,33,23);
		this.btnSaveTemp.setBounds(592,28,33,23);
		this.pbTemp.setBounds(7,54,655,18);
		
		this.pbTemp.setVisible(false);
		
		
		int decal = 0;
		if (this.file.get_format().retFormat().equals("MP3")) decal = 75;
		
		this.btnSave.setBounds(108-decal,26,120,27);
		this.btnConvert.setBounds(270-decal,26,120,27);
		this.btnPlay.setBounds(432-decal,26,120,27);
		this.btnTags.setBounds(594-decal,26,120,27);
		
	
		this.chkSel.setOpaque(false);
		this.chkSel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				
		this.btnStop.setVisible(false);
		this.btnStop.setToolTipText(MainForm.lang.lang_table[33]);
		
		this.lblState.setText(MainForm.lang.lang_table[34]);
		this.lblState.setFont(new java.awt.Font("Tahoma", 0, 11));
		
		this.pbProgress.setMaximum((int) this.file.get_content_length());
		this.pbProgress.setValue(0);
		
		
		this.timerRefresh.start();
		this.timerRefresh.setRepeats(true);
		
		this.edtTitle.addFocusListener(this);
		this.edtTitle.addKeyListener(this);
		this.edtTitle.setOpaque(false);
		
		this.edtUrl.setEditable(false);
		this.edtUrl.setText(this.url);
		this.edtUrl.setFont(new java.awt.Font("Tahoma", 0, 10));
		this.edtUrl.setOpaque(false);
		this.edtUrl.setBorder(null);
		this.edtUrl.setForeground(Color.gray);
		this.edtUrl.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		
		if (CaptureSystem.custTitle.equals(""))	this.edtTitle.setText(MainForm.lang.lang_table[29]);
		else 
		{
			if (this.file.get_format().retFormat().equals("FLV")) 
			{
				this.edtTitle.setText(CaptureSystem.custTitle);			
				CaptureSystem.custTitle = "";
			}
		}
		


		this.edtTitle.setFocusable(false);
		this.edtTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
		this.edtTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.edtTitle.addMouseListener(this);
		
		this.btnSave.setVisible(false);


		this.btnConvert.setVisible(false);
		this.btnConvert.addMouseListener(this);

		this.btnPlay.setVisible(false);	
		this.btnTags.setVisible(false);	
		
		

		this.add(this.icone);
		this.add(this.icoState);
		this.add(this.imgReduce);
		this.add(this.imgClose);
		this.add(this.pbProgress);
		this.add(this.edtUrl);
		this.add(this.edtTitle);
		this.add(this.lblState);
		this.add(this.btnSave);
		this.add(this.btnPlay);
		this.add(this.btnConvert);
		this.add(this.btnStop);
		this.add(this.chkSel);
		this.add(this.btnTags);
		this.add(this.btnPrev);
		this.add(this.btnSaveTemp);
		this.add(this.pbTemp);

		if (MainForm.trayIcon != null) MainForm.trayIcon.displayMessage("TubeMaster++", MainForm.lang.lang_table[35], TrayIcon.MessageType.INFO);

	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) 
	{
		if ((e.getSource().equals(this.edtTitle)) && (!this.edtTitle.hasFocus()))
		{
			this.edtTitle.setFocusable(true);
			this.edtTitle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray));
		}
		
	}
	
	public void mouseExited(MouseEvent e) 
	{
		if ((e.getSource().equals(this.edtTitle)) && (!this.edtTitle.hasFocus()))
		{
			this.edtTitle.setFocusable(false);
			this.edtTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
		}
	}
	public void mousePressed(MouseEvent e) 
	{
		if (!e.getSource().equals(this.edtTitle))
		{
			this.lblState.requestFocus();
		}
		
	}
	public void mouseReleased(MouseEvent e) {}
	
	//=====================================================================================================
	
	public void reduceFromAll()
	{
		if (!this.isWaitingFor) this.imgReduce.reduce();
	}
	
	public void maximizeFromAll()
	{
		if (!this.isWaitingFor) this.imgReduce.maximize();
	}
	
	//=====================================================================================================
	
	public boolean 			isChecked() 					{ return this.chkSel.isSelected(); }
	public void 			setChecked(boolean b) 			{ this.chkSel.setSelected(b); }
	public StreamFile 		getFile() 						{ return this.file; }
	public int 				getHauteur()					{ return this.currentHeight; }
	public boolean 			isAlive()						{ return this.alive; }
	public boolean 			isFullReady() 					{ return this.isFullReady; }
	public CommandRunner 	getCommandRunner() 				{ return this.cmd; }
	public void			 	setTitle(String title) 			{ this.edtTitle.setText(title); }
	public void 			setDragged(boolean drag) 		{ this.wasDragged = drag; }

	//=====================================================================================================

}
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
import Graphique.Icon;
import Graphique.ItemCloser;
import Graphique.ItemReducer;
import Graphique.TMButton;
import Main.Commun;
import Main.FenID3;
import Main.MainForm;
import Main.TextTransfer;
import java.awt.TrayIcon;
import java.io.File;




public class ListFileItem extends JPanel implements ActionListener, FocusListener, KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	
	private static final int ITEM_WIDTH = 668;				//Largeur de l'element.
	private static final int ITEM_HEIGHT = 60;				//Hauteur de l'element.
	private int currentHeight = ITEM_HEIGHT;				//Hauteur actuelle.
	private ListFile parentList;							//La liste parente.
	private boolean alive = true;							//Element a supprimer ou non.
	private CapturedFile leFichier;							//Fichier de cet element.	
	private Timer timerRefresh = new Timer(1000,this);		//Timer de rafraichissement du download.
	private int sizePrec = 0;								//Taille precedente pour le calcul du speed.
	private CommandRunner cmd = null;						//Execution de conversion.
	private String realUrl = "";							//Url directe du fichier capture.
	private boolean isFullReady = false;					//Si l'item est idle.
	private boolean isWaitingFor = false;					//Si l'item est en attente de conversion.
	private boolean autoConverted = false;					//A deja ete auto converted une fois.
	

	private Icon icone;										//Icone du format.
	private Icon icoState;									//Icone de l'etat du download.
	private ItemReducer imgReduce = new ItemReducer(this);	//Systeme de reduction.
	private ItemCloser imgClose = new ItemCloser(this);		//Systeme de fermeture.
	private JProgressBar pbProgress = new JProgressBar();	//Barre de progression.
	private JTextField edtTitle = new JTextField();			//Zone de texte du titre.
	private JLabel lblState = new JLabel();					//Label de l'etat du download.
	private JCheckBox chkSel = new JCheckBox();				//Checkbox de s�lection.
	
	private TMButton btnCopy = new TMButton(this,6,1,"","copy.png",0,0,0);
	private TMButton btnStop = new TMButton(this,7,2,"","stopconv.png",0,0,0);
	private TMButton btnSave = new TMButton(this,6,4,MainForm.lang.lang_table[26],"save_as.png",26,2,89);				
	private TMButton btnConvert = new TMButton(this,6,4,MainForm.lang.lang_table[27],"convert.png",26,2,89);
	private TMButton btnPlay = new TMButton(this,6,4,MainForm.lang.lang_table[28],"play.png",26,2,89);
	private TMButton btnTags = new TMButton(this,5,2,"Tags","id3.png",26,2,34);
	private TMButton btnPrev = new TMButton(this,6,2,"","preview.png",0,0,0);
	
	private ConvertMenu menuConvert = new ConvertMenu(this);		//Menu de conversion.
	
	private boolean wasDragged = false;
	private boolean wasAutoPlayed = false;

	
	
	
	
	//=====================================================================================================
	
	public ListFileItem(ListFile parentList, CapturedFile leFichier, String realUrl)
	{
		
		super();
		this.setLayout(null);
		this.setBackground(Color.decode("#FFFFE1"));
		this.setPreferredSize(new Dimension (ITEM_WIDTH,ITEM_HEIGHT));
		Border grayline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		this.setBorder(grayline);
		this.addMouseListener(this);
		this.parentList = parentList;
		this.leFichier = leFichier;
		this.icone = new Icon(this.leFichier.getFileFormat().retFormatLogo(),false);
		this.realUrl = realUrl;
		
		
		this.placeComposants();
	
	}

	
	//=====================================================================================================
	
	public void reduce()
	{
		this.setPreferredSize(new Dimension (ITEM_WIDTH,25));
		this.currentHeight = 25;
		this.parentList.bigRefresh();		
	}
	
	//=====================================================================================================
	
	
	
	
	//=====================================================================================================
	
	public void maximize()
	{
		this.setPreferredSize(new Dimension (ITEM_WIDTH,ITEM_HEIGHT));
		this.currentHeight = ITEM_HEIGHT;
		this.parentList.bigRefresh();
	}
	
	//=====================================================================================================
	

	
	
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
		if (e.getSource().equals(this.btnCopy)) new TextTransfer().setClipboardContents(this.realUrl);
		else
		if (e.getSource().equals(this.timerRefresh)) this.refreshDown();		
		else
		if (e.getSource().equals(this.btnSave)) this.runConversionSave(MainForm.opts.defRep);	
		else
		if (e.getSource().equals(this.btnConvert))
			this.menuConvert.show(this.btnConvert,this.btnConvert.getLastX(),this.btnConvert.getLastY());					
		else
		if (e.getSource().equals(this.btnPlay)) this.leFichier.playFile();	
		else
		if (e.getActionCommand().equals("*Menu_ExtractMP3*")) this.runConversionExtractMP3(MainForm.opts.defRep);		
		else
		if (e.getActionCommand().equals("*Menu_ConvPreset*")) 
			this.runConversionPreset(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
		else
		if (e.getActionCommand().equals("*Menu_Classic*")) 
			this.runConversionClassic(MainForm.opts.defRep,((JMenuItem)e.getSource()).getText());
		else
		if (e.getSource().equals(this.btnTags)) new FenID3(this.leFichier.retFilename(),this.edtTitle,this.btnTags);	
		else
		if (e.getSource().equals(this.btnPrev)) this.leFichier.playFile();
	}
	
	//=====================================================================================================
	
	
	public void runConversionPreset(String dir, String preset)
	{
		ConvCommandGen conv = new ConvCommandGen(this.edtTitle.getText(),this.leFichier.retFilename(),dir);
		String command = conv.ConvertToPreset(preset);
		if (!command.equals(""))
		{
			this.setConverting();
			this.cmd = new CommandRunner(command,this.lblState,this.pbProgress,this,null);
			MainForm.convManager.addItem(this);
			
		}
	}
	
	public void runConversionClassic(String dir, String preset)
	{
		ConversionClassic classic = new ConversionClassic(preset);
		String fileName = this.edtTitle.getText()+"."+classic.getExtension();
		ConvCommandGen conv = new ConvCommandGen(fileName,this.leFichier.retFilename(),dir);
		String command = conv.ConvertClassic(classic.getCommand());
		if (!command.equals(""))
		{
			this.cmd = new CommandRunner(command,this.lblState,this.pbProgress,this,null);
			MainForm.convManager.addItem(this);
		}
	}
	
	public void runConversionExtractMP3(String dir)
	{
		String fileName = this.edtTitle.getText()+".mp3";
		ConvCommandGen conv = new ConvCommandGen(fileName,this.leFichier.retFilename(),dir);
		if (conv.FLVtoMP3())
			if (MainForm.opts.delConv) this.toDestroy();
	}
	
	public void runConversionSave(String dir)
	{
		String fileName = this.edtTitle.getText();
		fileName += "."+this.leFichier.getFileFormat().retFormat().toLowerCase();
		ConvCommandGen conv = new ConvCommandGen(fileName,this.leFichier.retFilename(),dir);
		if (conv.SaveNoConvert()) this.toDestroy();  
	}

	//=====================================================================================================
	
	public void toDestroy()
	{
		if (this.cmd != null) this.cmd.stopProcess();
		this.timerRefresh.stop();
		this.leFichier.deleteFile();
		this.alive = false;
		this.parentList.bigRefresh();		
	}
	
	//=====================================================================================================
	
	public void refreshDown()
	{

		if (this.leFichier.is_Full())
		{
			this.timerRefresh.stop();			
			File f = new File(this.leFichier.retFilename());
			if (f.exists()) this.setFinished(); 
				else this.toDestroy();			
		}
		else
		{

			this.pbProgress.setMaximum(this.leFichier.getCap_FileSize());
			this.pbProgress.setValue(this.leFichier.getCap_Size());	
			String nfoSize = Commun.sizeConvert(this.leFichier.getCap_Size()) + " / " +  Commun.sizeConvert(this.leFichier.getCap_FileSize());
			String speed = Commun.sizeConvert(this.leFichier.getCap_Size() - this.sizePrec) + "/s";
			this.sizePrec = this.leFichier.getCap_Size();		
			this.lblState.setText(nfoSize + " (at " + speed+").");
			
			if ((this.edtTitle.getText().equals(MainForm.lang.lang_table[29])) && (!this.edtTitle.hasFocus())
				&& (this.leFichier.getFileFormat().retFormat().equals("MP3")))
			{
				String title = this.leFichier.findID3();
				if (title.equals(" - ")) this.edtTitle.setText(MainForm.lang.lang_table[29]);
				else this.edtTitle.setText(title);
			}
			
			if ((MainForm.opts.autoPlay) && (!this.wasAutoPlayed) && (this.leFichier.getCap_Size()>400000))
			{
				this.wasAutoPlayed = true;
				this.leFichier.playFile();
				
			}
			
			
		}	
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
		this.lblState.setText(MainForm.lang.lang_table[30]+" ("+Commun.sizeConvert(this.leFichier.getCap_FileSize())+").");
		this.pbProgress.setVisible(false);
		this.pbProgress.setSize(new Dimension(617,24));
		this.btnSave.setVisible(true);
		this.btnPlay.setVisible(true);
		if (this.leFichier.getFileFormat().retFormat().equals("MP3")) this.btnTags.setVisible(true);
		this.btnCopy.setVisible(false);
		this.btnPrev.setVisible(false);
		this.btnConvert.setVisible(true);
		this.btnStop.setVisible(false);
		this.isFullReady = true;
		this.chkSel.setVisible(true);
		this.imgReduce.setVisible(true);
	
		
		if ((MainForm.opts.autoConv) && (!this.autoConverted) && (!this.wasDragged))
		{
			this.autoConverted = true;
			String preset = MainForm.opts.autoConvPreset;
			ConversionClassic classic = new ConversionClassic(preset);
			
			if (classic.isClassic()) this.runConversionClassic(MainForm.opts.defRep,preset);
			else			
			if (preset.equals("Save Original File")) this.runConversionSave(MainForm.opts.defRep);
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
			this.edtTitle.setFont(new Font("Default_tm", 0, 12));
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
			this.edtTitle.setFont(new Font("Default_tm", Font.BOLD, 12));

		}	
	}
	
	//=====================================================================================================

	
	
	
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
		this.pbProgress.setBounds(7,28,577,24);
		this.edtTitle.setBounds(68,3,310,19);
		this.lblState.setBounds(384,2,220,20);
		this.btnSave.setBounds(108,26,120,27);
		this.btnConvert.setBounds(270,26,120,27);
		this.btnPlay.setBounds(430,26,120,27);
		this.btnStop.setBounds(629,28,33,23);
		this.btnCopy.setBounds(590,28,33,23);
		this.chkSel.setBounds(607,2,20,20);
		this.btnTags.setBounds(596,26,65,27);
		this.btnPrev.setBounds(629,28,33,23);
	
		this.chkSel.setOpaque(false);
		this.chkSel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.btnCopy.setToolTipText(MainForm.lang.lang_table[32]);
		
		this.btnStop.setVisible(false);
		this.btnStop.setToolTipText(MainForm.lang.lang_table[33]);
		
		this.lblState.setText(MainForm.lang.lang_table[34]);
		this.lblState.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.pbProgress.setMaximum(this.leFichier.getCap_FileSize());
		this.pbProgress.setValue(0);

		
		this.timerRefresh.start();
		this.timerRefresh.setRepeats(true);
		
		this.edtTitle.addFocusListener(this);
		this.edtTitle.addKeyListener(this);
		this.edtTitle.setOpaque(false);
		
		if (CaptureSystem.custTitle.equals(""))	this.edtTitle.setText(MainForm.lang.lang_table[29]);
		else 
		{
			if (this.leFichier.getFileFormat().retFormat().equals("FLV")) 
			{
				this.edtTitle.setText(CaptureSystem.custTitle);			
				CaptureSystem.custTitle = "";
			}
		}
		


		this.edtTitle.setFocusable(false);
		this.edtTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
		this.edtTitle.setFont(new Font("Default_tm", Font.BOLD, 12));
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
		this.add(this.edtTitle);
		this.add(this.lblState);
		this.add(this.btnSave);
		this.add(this.btnPlay);
		this.add(this.btnConvert);
		this.add(this.btnStop);
		this.add(this.btnCopy);
		this.add(this.chkSel);
		this.add(this.btnTags);
		this.add(this.btnPrev);
		
		if (MainForm.trayIcon != null) MainForm.trayIcon.displayMessage("TubeMaster++", MainForm.lang.lang_table[35]+"\n"+this.edtTitle.getText()+"."+this.leFichier.getFileFormat().retFormat().toLowerCase(), TrayIcon.MessageType.INFO);
		

	}

	public void mouseClicked(MouseEvent e){}
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
	
	public boolean isChecked() {return this.chkSel.isSelected();}
	public void checkIt() {this.chkSel.setSelected(true);}
	public void unCheckIt() {this.chkSel.setSelected(false);}
	public CapturedFile getFichier() {return this.leFichier;}
	public int getHauteur(){return this.currentHeight;}
	public boolean isAlive(){return this.alive;}
	public boolean isFullReady() {return this.isFullReady;}
	public CommandRunner getCommandRunner() {return this.cmd;}
	public void setTitle(String title) {this.edtTitle.setText(title);}
	public void setDragged(boolean drag) {this.wasDragged = drag;}
	public boolean getDragged() {return this.wasDragged;}

	//=====================================================================================================

}
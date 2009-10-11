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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v1.ID3V1Tag.Genre;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import Graphique.TMButton;

public class FenID3 extends JFrame implements ActionListener, WindowListener
{

	private static final long serialVersionUID = 1L;


	private JPanel panFen = new JPanel(); //Panel de la fenetre.
	
	
	private JLabel lblTitle = new JLabel();
	private JTextField edtTitle = new JTextField();
	
	private JLabel lblArtist = new JLabel();
	private JTextField edtArtist = new JTextField();
	
	private JLabel lblAlbum = new JLabel();
	private JTextField edtAlbum = new JTextField();
	
	private JLabel lblYear = new JLabel();
	private JTextField edtYear = new JTextField();
	
	private JLabel lblTrack = new JLabel();
	private JTextField edtTrack = new JTextField();
	
	private JLabel lblGenre = new JLabel();
	private JComboBox cmbGenre;
	
	private TMButton btnCancel = new TMButton(this,0,0,MainForm.lang.lang_table.get(64),"",0,4,100);
	private TMButton btnApply = new TMButton(this,0,0,MainForm.lang.lang_table.get(65),"",0,4,100);
	
	private JTextField edtParent = new JTextField();
	private TMButton btnParent;
	
	//Pour les Tags
	private String fichier = "";
	
	
	
	public FenID3(String fichier, JTextField edtParent, TMButton btnParent)
	{
		this.panFen.setLayout(null);
		this.panFen.setBackground(Color.decode("#676767"));
		this.setTitle("ID3 Tags Editor");
		this.setSize(288, 330);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(this.panFen);
		ImageIcon monIcon= new ImageIcon(getClass().getResource("images/"+"icon.jpg"));
		this.setIconImage(monIcon.getImage());
		
		this.addWindowListener(this);
		this.placeComposants();
		this.fichier = fichier;
		this.edtParent = edtParent;
		this.btnParent = btnParent;
		this.btnParent.setEnabled(false);
		this.readTags();
		
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(this.btnCancel)) 
		{
			this.btnParent.setEnabled(true);
			this.dispose();
		}
		else
		if (e.getSource().equals(this.btnApply))
		{
			this.writeTags();
			this.btnParent.setEnabled(true);
			this.dispose();
		}
		
	}
	

	public void readTags()
	{
		String title="",artist="",album="",year="",track="",genre2="";
		Genre genre = Genre.Undefined;
		File sourceFile = new File(this.fichier);
		MediaFile mediaFile = new MP3File(sourceFile);
		boolean v1 = false;
		try 
	    {
			
			ID3Tag[] lesTags = mediaFile.getTags();
			for(int i=0;i<lesTags.length;i++)
			{					
				if (lesTags[i] instanceof ID3V1_0Tag)
	            {
					v1 = true;
	                ID3V1_0Tag leTag = (ID3V1_0Tag)lesTags[i];
	                if (leTag.getTitle() != null) title = leTag.getTitle();
	    			if (leTag.getArtist()!=null) artist = leTag.getArtist();
	    			if (leTag.getAlbum()!=null) album = leTag.getAlbum();
	    			try {year = ""+leTag.getYear();} catch (Exception e) {}
	    			if (leTag.getGenre()!=null) genre = leTag.getGenre();

	            }
				else if (lesTags[i] instanceof ID3V2_3_0Tag)
	            {
	                ID3V2_3_0Tag leTag = (ID3V2_3_0Tag)lesTags[i];
	                if (leTag.getTitle() != null) title = leTag.getTitle();
	    			if (leTag.getArtist()!=null) artist = leTag.getArtist();
	    			if (leTag.getAlbum()!=null) album = leTag.getAlbum();
	    			try {year = ""+leTag.getYear();} catch (Exception e) {}
	    			try {track = ""+leTag.getTrackNumber();} catch (Exception e) {}
	    			if (leTag.getGenre()!=null) genre2 = leTag.getGenre();
	            }	
			}	
		
			if (v1) genre2 = genre.toString();
			
		} catch (Exception e) {Commun.logError(e);}
	
			if ((genre2.indexOf("(")==0) || (genre2.equals(""))) genre2 = "Undefined";

			this.edtTitle.setText(title);
 			this.edtArtist.setText(artist);
 			this.edtAlbum.setText(album);
 			this.edtYear.setText(year);
 			this.edtTrack.setText(track);

			boolean trouve = false;
			for(int i=0;i<148;i++) 
			{
				if (this.cmbGenre.getItemAt(i).equals(genre2))
				{
					trouve = true;
					this.cmbGenre.setSelectedIndex(i);
					break;					
				}
			}
			if (!trouve) 
			{
				this.cmbGenre.addItem(genre2);
				this.cmbGenre.setSelectedIndex(148);
			}
			
		
	}
	
	 public boolean isNumeric(String str)  
	 {  
	     try  {Integer.parseInt(str); }  
	     catch(NumberFormatException nfe)  {return false;}  
	     return true;  
	 }  
	
	
	public void writeTags()
	{
		
		try 
	    {
			File sourceFile = new File(this.fichier);
			MediaFile mediaFile = new MP3File(sourceFile);
			mediaFile.removeTags();

			String title="",artist="",album="",genre="";
			int year=0,track=0;
			
			if (this.edtTitle.getText().equals("")) title = "Undefined";
			else title = this.edtTitle.getText();
					
			if (this.edtArtist.getText().equals("")) artist = "Undefined";
			else artist = this.edtArtist.getText();
			
			if (this.edtAlbum.getText().equals("")) album = "Undefined";
			else album = this.edtAlbum.getText();
			
			if (((String) this.cmbGenre.getEditor().getItem()).equals("")) genre = "Undefined";
			else genre = (String) this.cmbGenre.getEditor().getItem();

			
			
			if (this.isNumeric(this.edtYear.getText()))
			{
				int i = Integer.parseInt(this.edtYear.getText());
				if ((i>=0) && (i<=9999)) year = i;
			}
			
			if (this.isNumeric(this.edtTrack.getText()))
			{
				int i = Integer.parseInt(this.edtTrack.getText());
				if (i>=0) track = i;
			}
			
			
			ID3V2_3_0Tag leTag = new ID3V2_3_0Tag();
			leTag.setTitle(title);
			leTag.setArtist(artist);
			leTag.setAlbum(album);
			leTag.setYear(year);
			leTag.setTrackNumber(track);
			leTag.setGenre(genre);

			mediaFile.setID3Tag(leTag);
			mediaFile.sync();
			
			this.edtParent.setText(title + " - " + artist);
	
			
	    } catch (Exception e) {Commun.logError(e);}
		
	}
	
	
	public void placeComposants()
	{
		
		this.lblTitle.setText(MainForm.lang.lang_table.get(58));
		this.lblTitle.setForeground(Color.white);
		this.lblTitle.setBounds(15,10,250,20);
		this.lblTitle.setHorizontalAlignment(JLabel.LEFT);
		this.lblTitle.setFont(new java.awt.Font("Default_tm", 0, 12));			
		this.edtTitle.setBounds(15,29,250,21);
		this.edtTitle.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.lblArtist.setText(MainForm.lang.lang_table.get(59));
		this.lblArtist.setForeground(Color.white);
		this.lblArtist.setBounds(15,59,250,20);
		this.lblArtist.setHorizontalAlignment(JLabel.LEFT);
		this.lblArtist.setFont(new java.awt.Font("Default_tm", 0, 12));			
		this.edtArtist.setBounds(15,78,250,21);
		this.edtArtist.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.lblAlbum.setText(MainForm.lang.lang_table.get(60));
		this.lblAlbum.setForeground(Color.white);
		this.lblAlbum.setBounds(15,108,250,20);
		this.lblAlbum.setHorizontalAlignment(JLabel.LEFT);
		this.lblAlbum.setFont(new java.awt.Font("Default_tm", 0, 12));			
		this.edtAlbum.setBounds(15,127,250,21);
		this.edtAlbum.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.lblYear.setText(MainForm.lang.lang_table.get(61));
		this.lblYear.setForeground(Color.white);
		this.lblYear.setBounds(15,157,95,20);
		this.lblYear.setHorizontalAlignment(JLabel.LEFT);
		this.lblYear.setFont(new java.awt.Font("Default_tm", 0, 12));			
		this.edtYear.setBounds(15,176,95,21);
		this.edtYear.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.lblTrack.setText(MainForm.lang.lang_table.get(62));
		this.lblTrack.setForeground(Color.white);
		this.lblTrack.setBounds(120,157,145,20);
		this.lblTrack.setHorizontalAlignment(JLabel.LEFT);
		this.lblTrack.setFont(new java.awt.Font("Default_tm", 0, 12));			
		this.edtTrack.setBounds(120,176,145,21);
		this.edtTrack.setFont(new java.awt.Font("Default_tm", 0, 11));
		
		this.lblGenre.setText(MainForm.lang.lang_table.get(63));
		this.lblGenre.setForeground(Color.white);
		this.lblGenre.setBounds(15,206,250,20);
		this.lblGenre.setHorizontalAlignment(JLabel.LEFT);
		this.lblGenre.setFont(new java.awt.Font("Default_tm", 0, 12));
		
		Vector<String> genres = new Vector<String>();
		try
		{
			for(int i=0;i<148;i++) genres.add(ID3V1Tag.Genre.lookupGenre(i).toString());
		} catch (Exception e) {Commun.logError(e);}
		Collections.sort(genres);
		this.cmbGenre = new JComboBox(genres);
		
		this.cmbGenre.setBounds(15,225,250,20);
		this.cmbGenre.setMaximumRowCount(12);
		this.cmbGenre.setEditable(true);
		
		
		this.btnCancel.setBounds(15,260,100,30);
		this.btnApply.setBounds(165,260,100,30);
		
		
		this.panFen.add(this.lblTitle);
		this.panFen.add(this.edtTitle);
		this.panFen.add(this.lblArtist);
		this.panFen.add(this.edtArtist);
		this.panFen.add(this.lblAlbum);
		this.panFen.add(this.edtAlbum);
		this.panFen.add(this.lblYear);
		this.panFen.add(this.edtYear);
		this.panFen.add(this.lblTrack);
		this.panFen.add(this.edtTrack);
		this.panFen.add(this.lblGenre);
		this.panFen.add(this.cmbGenre);
		this.panFen.add(this.btnApply);
		this.panFen.add(this.btnCancel);
		
	}


	public void windowActivated(WindowEvent arg0) {}


	public void windowClosed(WindowEvent arg0) {}


	public void windowClosing(WindowEvent arg0) 
	{
		this.btnParent.setEnabled(true);
	}


	public void windowDeactivated(WindowEvent arg0) {}


	public void windowDeiconified(WindowEvent arg0) {}


	public void windowIconified(WindowEvent arg0) {}

	public void windowOpened(WindowEvent arg0) {}
	
	


	
		
	

}

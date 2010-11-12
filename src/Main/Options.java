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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.PrintWriter;


public class Options 
{
	//Fichier de stockage des options.
	File f = new File(MainForm.tm_path + File.separator + "options.xml");
	
	//Liste des parametres
		public boolean autoCapture = true;						//Demarrage automatique de la capture.
		public boolean autoUpdate = true;						//Verification automatique des MAJs.	
		public boolean trayIcon = true;							//TrayIcon lors de la reduction.
		public boolean delConv = true;							//Suppression des fichiers apres conversion.
		public String defRep = "";								//Repertoire de sortie par defaut.
		public boolean autoConv = false;						//Conversion automatique.
		public String autoConvPreset = "Save Original File";	//Preset de conversion automatique.
		public String langFile = "English";						//Fichier de langue.
		public String bitrate = "224";							//MP3 Bitrate.
		public boolean closeBox = true;							//Affichage de la closebox.
		public String repFLV = "ffplay";						//FLV Player.
		public String repMP3 = "ffplay";						//MP3 Player.
		public String repMP4 = "ffplay";						//MP4 Player.
		public String repMOV = "ffplay";						//MOV Player.
		public boolean autoPlay = false;						//AutoPlay des fichier capturés.
		public String timeout = "20";							//Timeout de destruction si coupure.
		public String minimal = "100000";						//Taille minimale de capture.
		
	public Options()
	{
		
		if (this.f.exists()) this.ReadFile();

		this.WriteFile();			

	}
	
	
	
	public void WriteFile()
	{

		try 
		{
			//PrintWriter fichier = new PrintWriter(new BufferedWriter(new FileWriter(this.f)));
			
			PrintWriter fichier = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.f),"UTF-8")));
			
			
			//Ecriture des parametres.
			fichier.println("<autoCapture>"+this.boolToStr(this.autoCapture)+"</autoCapture>");
			fichier.println("<autoUpdate>"+this.boolToStr(this.autoUpdate)+"</autoUpdate>");
			fichier.println("<trayIcon>"+this.boolToStr(this.trayIcon)+"</trayIcon>");
			fichier.println("<delConv>"+this.boolToStr(this.delConv)+"</delConv>");
			fichier.println("<defRep>"+this.defRep+"</defRep>");
			fichier.println("<autoConv>"+this.boolToStr(this.autoConv)+"</autoConv>");
			fichier.println("<autoConvPreset>"+this.autoConvPreset+"</autoConvPreset>");
			fichier.println("<langFile>"+this.langFile+"</langFile>");
			fichier.println("<mp3Bitrate>"+this.bitrate+"</mp3Bitrate>");
			fichier.println("<closeBox>"+this.boolToStr(this.closeBox)+"</closeBox>");
			fichier.println("<repFLV>"+this.repFLV+"</repFLV>");
			fichier.println("<repMP3>"+this.repMP3+"</repMP3>");
			fichier.println("<repMP4>"+this.repMP4+"</repMP4>");
			fichier.println("<repMOV>"+this.repMOV+"</repMOV>");
			fichier.println("<autoPlay>"+this.boolToStr(this.autoPlay)+"</autoPlay>");
			fichier.println("<timeout>"+this.timeout+"</timeout>");
			fichier.println("<minimal>"+this.minimal+"</minimal>");
			
			fichier.close();	
				
		} catch (IOException e) {Commun.logError(e);}   
	
	}
	
	
	public void ReadFile()
	{
	    try
	      {
	    	BufferedReader lecteur = null;
		    String ligne;
		    String total = "";
	    	lecteur = new BufferedReader(new InputStreamReader(new FileInputStream(this.f),"UTF-8"));
	    	
	    	
	    	while ((ligne = lecteur.readLine()) != null) total += ligne;
	   	    lecteur.close();

	   	    //Chargement des parametres.
	   	    if (total.indexOf("<autoCapture>")>-1) 
	   	    	this.autoCapture = this.strToBool(this.parse(total, "<autoCapture>", "</autoCapture>"));
	   	    if (total.indexOf("<autoUpdate>")>-1) 
	   	    	this.autoUpdate = this.strToBool(this.parse(total, "<autoUpdate>", "</autoUpdate>"));
	   	    if (total.indexOf("<trayIcon>")>-1) 
	   	    	this.trayIcon = this.strToBool(this.parse(total, "<trayIcon>", "</trayIcon>")); 
	   	    if (total.indexOf("<delConv>")>-1) 
	   	    	this.delConv = this.strToBool(this.parse(total, "<delConv>", "</delConv>"));
	   	    if (total.indexOf("<defRep>")>-1) 
	   	    	this.defRep = this.parse(total, "<defRep>", "</defRep>");
	   	    if (total.indexOf("<autoConv>")>-1) 
	   	    	this.autoConv = this.strToBool(this.parse(total, "<autoConv>", "</autoConv>"));
	   	    if (total.indexOf("<autoConvPreset>")>-1) 
	   	    	this.autoConvPreset = this.parse(total, "<autoConvPreset>", "</autoConvPreset>"); 
	   	    if (total.indexOf("<langFile>")>-1) 
	   	    	this.langFile = this.parse(total, "<langFile>", "</langFile>"); 
	   	    if (total.indexOf("<mp3Bitrate>")>-1) 
	   	    	this.bitrate = this.parse(total, "<mp3Bitrate>", "</mp3Bitrate>"); 
	   	    if (total.indexOf("<closeBox>")>-1) 
	   	    	this.closeBox = this.strToBool(this.parse(total, "<closeBox>", "</closeBox>")); 
	   	    if (total.indexOf("<repFLV>")>-1) 
	   	    	this.repFLV = this.parse(total, "<repFLV>", "</repFLV>");
	   	    if (total.indexOf("<repMP3>")>-1) 
	   	    	this.repMP3 = this.parse(total, "<repMP3>", "</repMP3>");
	   	    if (total.indexOf("<repMP4>")>-1) 
	   	    	this.repMP4 = this.parse(total, "<repMP4>", "</repMP4>");
	   	    if (total.indexOf("<repMOV>")>-1) 
	   	    	this.repMOV = this.parse(total, "<repMOV>", "</repMOV>");
	   	    if (total.indexOf("<autoPlay>")>-1) 
	   	    	this.autoPlay = this.strToBool(this.parse(total, "<autoPlay>", "</autoPlay>")); 
	   	    if (total.indexOf("<timeout>")>-1) 
	   	    	this.timeout = this.parse(total, "<timeout>", "</timeout>");
	   	 if (total.indexOf("<minimal>")>-1) 
	   	    	this.minimal = this.parse(total, "<minimal>", "</minimal>");
	   	 
	      } catch(Exception e) {Commun.logError(e);}	
	}
	
	
	public String boolToStr(boolean b) {if (b) return "1"; else return "0";}
	public boolean strToBool(String s) {return (s.equals("1"));}
	
	
	public String parse(String chaine, String deb, String fin)
	{	
		try
		{
			chaine = chaine.substring(chaine.indexOf(deb));		
			return chaine.substring(chaine.indexOf(deb)+deb.length(),chaine.indexOf(fin));
		} catch (Exception e) {return "";}
		
	}
	
	

}

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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Languages 
{
	private File f = new File("lang/"+MainForm.opts.langFile);
	
	public ArrayList<String> lang_table = new ArrayList<String>(); 
	
	
	public Languages()
	{
		
		 try
	      {
		    String ligne;
		    String total = "";

		    BufferedReader lecteur = new BufferedReader(new InputStreamReader(new FileInputStream(this.f),"UTF-8"));
	    	while ((ligne = lecteur.readLine()) != null) total += ligne;
	   	    lecteur.close();

	   	    
	   	    //Header
	   	    	lang_table.add(this.parse(total,"<0>","</0>")); //Options
	   	    	lang_table.add(this.parse(total,"<1>","</1>")); //Donate
	   	    	lang_table.add(this.parse(total,"<2>","</2>")); //About
	   	    	lang_table.add(this.parse(total,"<3>","</3>")); //Media Capture
	   	    	lang_table.add(this.parse(total,"<4>","</4>")); //Video Search
	   	    	lang_table.add(this.parse(total,"<5>","</5>")); //MP3 Search	   	    
	   	    
	   	    //Panel Capture
	   	    	lang_table.add(this.parse(total,"<6>","</6>")); //Disable Capture
	   	    	lang_table.add(this.parse(total,"<7>","</7>")); //Enable Capture
	   	    	lang_table.add(this.parse(total,"<8>","</8>")); //Save Selected Files
	   	    	lang_table.add(this.parse(total,"<9>","</9>")); //Convert Selected Files
	   	    	
	   	    //Video Search Panel
	   	    	lang_table.add(this.parse(total,"<10>","</10>")); //Video Search Engine
	   	    	lang_table.add(this.parse(total,"<11>","</11>")); //Global Video Search
	   	    	lang_table.add(this.parse(total,"<12>","</12>")); //Name
	   	    	lang_table.add(this.parse(total,"<13>","</13>")); //Length
	   	    	lang_table.add(this.parse(total,"<14>","</14>")); //Category
	   	    	lang_table.add(this.parse(total,"<15>","</15>")); //Channel
	   	    	
	   	    		   	    	
	   	    //MP3 Search Panel
	   	    	lang_table.add(this.parse(total,"<16>","</16>")); //MP3 Search Engine
	   	    	lang_table.add(this.parse(total,"<17>","</17>")); //Title
	   	    	lang_table.add(this.parse(total,"<18>","</18>")); //Download !
	   	    	
	   	    //Options Panel
	   	    	lang_table.add(this.parse(total,"<19>","</19>")); //Enable capture...
	   	    	lang_table.add(this.parse(total,"<20>","</20>")); //Auto-update...
	   	    	lang_table.add(this.parse(total,"<21>","</21>")); //Taskbar icon...
	   	    	lang_table.add(this.parse(total,"<22>","</22>")); //Delete conversion..
	   	    	lang_table.add(this.parse(total,"<23>","</23>")); //Language selection..
	   	    	lang_table.add(this.parse(total,"<24>","</24>")); //Default folder...
	   	    	lang_table.add(this.parse(total,"<25>","</25>")); //Auto conversion...
	   	    	
	   	   //List File Item	
	   	    	
	   	    	lang_table.add(this.parse(total,"<26>","</26>")); //Save File
	   	    	lang_table.add(this.parse(total,"<27>","</27>")); //Convert File
	   	    	lang_table.add(this.parse(total,"<28>","</28>")); //Play File
	   	    	lang_table.add(this.parse(total,"<29>","</29>")); //Click here to rename
	   	    	lang_table.add(this.parse(total,"<30>","</30>")); //Done !
	   	    	lang_table.add(this.parse(total,"<31>","</31>")); //Waiting for conversion
	   	    	lang_table.add(this.parse(total,"<32>","</32>")); //Copy direct link
	   	    	lang_table.add(this.parse(total,"<33>","</33>")); //Stop conversion process
	   	    	lang_table.add(this.parse(total,"<34>","</34>")); //Loading ...
	   	    	lang_table.add(this.parse(total,"<35>","</35>")); //New file detected
	   	    	
	   	    //Oubli
	   	    	lang_table.add(this.parse(total,"<36>","</36>")); //Play Video
	   	    	
	   	    //MessageBox	
	   	    	lang_table.add(this.parse(total,"<37>","</37>")); //Sure ?
	   	    	lang_table.add(this.parse(total,"<38>","</38>")); //Stop conversion ?
	   	    	lang_table.add(this.parse(total,"<39>","</39>")); //Delete all captured ?
	   	    	lang_table.add(this.parse(total,"<40>","</40>")); //File Already Exists
	   	    	lang_table.add(this.parse(total,"<41>","</41>")); //Error. File Already Exists...
	   	    	lang_table.add(this.parse(total,"<42>","</42>")); //This operation will stop download...
	   	    	lang_table.add(this.parse(total,"<43>","</43>")); //Delete this file ?
	   	    	lang_table.add(this.parse(total,"<44>","</44>")); //Not Winpcap1
	   	    	lang_table.add(this.parse(total,"<45>","</45>")); //Not Winpcap2
	   	    	lang_table.add(this.parse(total,"<46>","</46>")); //Not Winpcap3
	   	    	lang_table.add(this.parse(total,"<47>","</47>")); //Do you want to close TM?
	   	    	lang_table.add(this.parse(total,"<48>","</48>")); //Update Available
	   	    	lang_table.add(this.parse(total,"<49>","</49>")); //An Update is Available.
	   	    	
	   	    	
	   	    //Rajouts
	   	    	lang_table.add(this.parse(total,"<50>","</50>")); //Error. No Response.
	   	    	lang_table.add(this.parse(total,"<51>","</51>")); //Downloading.
	   	    	lang_table.add(this.parse(total,"<52>","</52>")); //Loading.
	   	    	lang_table.add(this.parse(total,"<53>","</53>")); //Disable capture.
	   	    	lang_table.add(this.parse(total,"<54>","</54>")); //Disable capture title.
	   	    	lang_table.add(this.parse(total,"<55>","</55>")); //Option CloseBox.
	   	    	lang_table.add(this.parse(total,"<56>","</56>")); //Close FenOptions.
	   	    	lang_table.add(this.parse(total,"<57>","</57>")); //Drag&Drop Liste.
	   	    	
	   	     //ID3 Editor
	   	    	lang_table.add(this.parse(total,"<58>","</58>")); //Title.
	   	    	lang_table.add(this.parse(total,"<59>","</59>")); //Artist.
	   	    	lang_table.add(this.parse(total,"<60>","</60>")); //Album.
	   	    	lang_table.add(this.parse(total,"<61>","</61>")); //Year.
	   	    	lang_table.add(this.parse(total,"<62>","</62>")); //Track.
	   	    	lang_table.add(this.parse(total,"<63>","</63>")); //Genre.
	   	    	lang_table.add(this.parse(total,"<64>","</64>")); //Cancel.
	   	    	lang_table.add(this.parse(total,"<65>","</65>")); //Apply.
	   	    	
	   	    //Added 1.6 Version
	   	    	lang_table.add(this.parse(total,"<66>","</66>")); //Source MP3.
	   	    	lang_table.add(this.parse(total,"<67>","</67>")); //Default Multimedia Players.
	   	    	lang_table.add(this.parse(total,"<68>","</68>")); //Auto Start Playing..
	   	    	
	   	     //Added 1.7 Version
	   	    	lang_table.add(this.parse(total,"<69>","</69>")); //MP3 Bitrate.
	   	    	lang_table.add(this.parse(total,"<70>","</70>")); //Capture Timeout.
	   	    	lang_table.add(this.parse(total,"<71>","</71>")); //to disable.
	   	    	lang_table.add(this.parse(total,"<72>","</72>")); //Player Error.
	   	    	lang_table.add(this.parse(total,"<73>","</73>")); //Player not found...
	   	    	lang_table.add(this.parse(total,"<74>","</74>")); //FFMPeg terminated with errors..;
	   	    	lang_table.add(this.parse(total,"<75>","</75>")); //General
	   	    	lang_table.add(this.parse(total,"<76>","</76>")); //Capture
	   	    	lang_table.add(this.parse(total,"<77>","</77>")); //Save/Conversion
	   	    	lang_table.add(this.parse(total,"<78>","</78>")); //Multimedia Players
	   	    	lang_table.add(this.parse(total,"<79>","</79>")); //Capture Minimal Size
	   	    	lang_table.add(this.parse(total,"<80>","</80>")); //bytes
	   	    	
	      } catch(Exception e) {Commun.logError(e);}
		
		
		
	}
	
	
	public String parse(String chaine, String deb, String fin)
	{	
		try
		{
			chaine = chaine.substring(chaine.indexOf(deb));		
			return chaine.substring(chaine.indexOf(deb)+deb.length(),chaine.indexOf(fin));
		} catch (Exception e) {return "";}
		
	}
	
	

}

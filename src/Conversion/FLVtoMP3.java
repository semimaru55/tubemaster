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

package Conversion;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import Main.Commun;


public class FLVtoMP3 
{
	
	public FLVtoMP3(String input, String output)
	{
		convert(input, output);	 
	}
	
	 public static int convert(String input, String output)
	 {
		 // reading
		 FileOutputStream fos = null;
		 DataOutputStream dos = null;
		 // writting
		 FileInputStream fis = null;
		 DataInputStream dis = null;

		 int bufsize = 4096;

		 try
		 {
			 fis = new FileInputStream(input);
			 BufferedInputStream bis = new BufferedInputStream( fis, bufsize);
			 dis = new DataInputStream(bis);

			 File file = new File(output);
			 fos = new FileOutputStream(file);
			 dos = new DataOutputStream(fos);

			 int i = dis.readInt(); //FLV
			 if (i == 0x464C5601)
			 {
				 dis.readByte(); // read flags (5)
				 dis.readInt(); // read headers size (9)
				 dis.readInt(); //PrevTagSize

				boolean EOF = false; 
				 while (!EOF)
				 {  
					 try
					 {
						 int eTagType = dis.readByte(); // read tag type (0x12)
	 
						 int nLength = getUI24(dis); // read tag length (0x126 sur 3 bytes soit 24 bits (UI24))

						 dis.readInt(); // read time stamp (0)
						 getUI24(dis); // streamID
						 if (nLength > 0) 
						 {
							 dis.readByte(); // read MediaType (2)
							 
							 byte[] xbData = new byte[nLength - 1];
							 dis.read(xbData, 0, nLength -1);
							 // write only audio data
							 if (eTagType == 0x08)
							 {
								 dos.write(xbData, 0, nLength - 1);
							 }
						 }
						 else
						 {
							 break;
						 }
						 
						 try
						 {
							 dis.readInt(); //PrevTagSize
							 
						 }catch (Exception e) {
							 dos.close();
							 fos.close();
							 fis.close();
							 dis.close();
							 return 0;
						 }
					 } 
					 catch (EOFException e) {EOF = true;}
				 }
				 dos.close();
				 fos.close();
				 fis.close();
				 dis.close();
			 }else{
				 return -2; // not flv
			 }
		 } catch (IOException e) {
			 Commun.logError(e);
			 return -1;
		 }
		 return 0;
	 }

	 private static int getUI24(DataInputStream br)
	 {
		 byte[] cbuff = new byte[3];
		 try 
		 {
			 br.read(cbuff, 0, 3);
		 } catch (IOException e) {}
		 return bytesToInt(cbuff);
	 }
	 
	 private static int bytesToInt(byte[] bytes)
	 {
		 int value = 0;
		 for(int i=0; i<bytes.length; i++)
		 {
			 value = value << 8;
			 value += bytes[i] & 0xff;
		 }
		 return value;
	 }
	
	
	

}

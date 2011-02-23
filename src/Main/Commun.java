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

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Calendar;


public class Commun 
{
		public static Font tm_font10 = new java.awt.Font("Dialog", 0, 10);
		public static Font tm_font10b = new java.awt.Font("Dialog", Font.BOLD, 10);
		public static Font tm_font11 = new java.awt.Font("Dialog", 0, 11);
		public static Font tm_font11i = new java.awt.Font("Dialog", Font.ITALIC, 11);
		public static Font tm_font12 = new java.awt.Font("Dialog", 0, 12);
		public static Font tm_font12b = new java.awt.Font("Dialog", Font.BOLD, 12);
		public static Font tm_font13b = new java.awt.Font("Dialog", Font.BOLD, 13);
		public static Font tm_font18 = new java.awt.Font("Dialog", 0, 18);
	
		public synchronized static void logError(Throwable trace)
		{
			try
			{
				StringWriter sw = new StringWriter();
				trace.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
			
				
				File f = new File (MainForm.tm_path + File.separator + "Errors.log");
				
				FileOutputStream os = new FileOutputStream(f,true);
				
				Calendar laDate = Calendar.getInstance();
				String d = "-> Date "+laDate.get(Calendar.HOUR_OF_DAY)+":"
								   +laDate.get(Calendar.MINUTE)+":"
								   +laDate.get(Calendar.SECOND)+" - "
								   +laDate.get(Calendar.DAY_OF_MONTH)+"/"
								   +(laDate.get(Calendar.MONTH)+1)+"/"
								   +laDate.get(Calendar.YEAR)+"\n";
								   
									
				
				os.write(d.getBytes());
				if (stacktrace==null) os.write("Error but trace is NULL..".getBytes());	
				else os.write(stacktrace.getBytes());
				os.write("\n\n\n".getBytes());
				
				os.close();	
				
			} catch (Exception e) {e.printStackTrace();}	
		}
				
		
		public synchronized static void logDebugLine(String trace)
		{
			try
			{
				File f = new File (MainForm.tm_path + File.separator + "Debug.log");
				FileOutputStream os = new FileOutputStream(f,true);
				os.write(trace.getBytes());
				os.write("\n".getBytes());
				os.close();	
				
			} catch (Exception e) {e.printStackTrace();}	
		}
		
		
		public static int arrayPos(byte[] arr, byte[] tof, int itera)
		{
			int position = -1;
			int count = 0;
			for(int i=0;i<arr.length-tof.length;i++)
			{
				boolean equ = true;
				for(int j=0;j<tof.length;j++) if (arr[i+j]!=tof[j]) equ = false;
				if (equ)
				{
					count++;
					position = i;
					if(count==itera) break;
				}	
			}		
			return position;
		}
		
		public static String parse(String chaine, String deb, String fin)
		{			
			chaine = chaine.substring(chaine.indexOf(deb));		
			return chaine.substring(chaine.indexOf(deb)+deb.length(),chaine.indexOf(fin));
		}
	
		public static String sizeConvert(long l)
		{
			String nfoSize = "";
			double size = 0;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2) ; 
			if (l < 1000000)
			{
				size = ((double) l) / 1024;
				size = Math.round(size*100.0) / 100.0;
				nfoSize = ""+df.format(size)+" KB";
			}
			else
			{
				size = (((double) l) / 1024) / 1024;
				size = Math.round(size*100.0) / 100.0;
				nfoSize = ""+df.format(size)+" MB";		
			}
			
			return nfoSize;		
		}
		

}

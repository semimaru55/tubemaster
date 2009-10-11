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

import Main.MainForm;

public class ConversionClassic 
{
	
	private String command = "";
	private String extension = "";
	private boolean isClassic = true;
	
	public ConversionClassic(String id)
	{

		if (id.equals("Video AVI (320x240)"))
		{
			this.command = "-f avi -r 29.97 -vcodec libxvid -vtag XVID -s 320x240 -aspect 4:3 -maxrate 1800kb -b 1500kb -qmin 3 -qmax 5 -bufsize 4096 -mbd 2 -bf 2 -flags +4mv -trellis -aic -cmp 2 -subcmp 2 -g 300 -acodec libmp3lame -ar 48000 -ab 128kb -ac 2";
			this.extension = "avi";
		}
		else 
		if (id.equals("Video AVI (640x480)"))
		{
			this.command = "-f avi -r 29.97 -vcodec libxvid -vtag XVID -s 640x480 -aspect 4:3 -maxrate 1800kb -b 1500kb -qmin 3 -qmax 5 -bufsize 4096 -mbd 2 -bf 2 -flags +4mv -trellis -aic -cmp 2 -subcmp 2 -g 300 -acodec libmp3lame -ar 48000 -ab 128kb -ac 2";
			this.extension = "avi";
		}
		else 
		if (id.equals("Video AVI DiVX"))
		{
			this.command = "-vcodec mpeg4 -acodec libmp3lame -vtag DIVX -b 600000 -deinterlace";
			this.extension = "avi";
		}
		else 
		if (id.equals("Video MP4 (PSP, Ipod ...)"))
		{
			this.command = "-s 320x240 -vcodec mpeg4 -acodec libmp3lame -async 2 -sameq -ab 128";
			this.extension = "mp4";
		}
		else if (id.equals("Audio MP3"))
		{
			this.command = "-acodec libmp3lame -ab "+MainForm.opts.bitrate+"kb -ac 2 -ar 44100";
			this.extension = "mp3";
		}	
		else this.isClassic = false;
	}
	
	public String getCommand() {return this.command;}
	public String getExtension() {return this.extension;}
	public boolean isClassic() {return this.isClassic;}

}

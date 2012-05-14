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

 
import java.util.ArrayList;


import Main.Commun;
import Main.MainForm;


public class PacketsManager implements Runnable
{
	private ListFile 				fileList;									
	private ArrayList<TMPacket> 	packetCache;						
	private StreamsFilter 			streamsFilter;
	private boolean 				isActive;
		
	//RTMP Protocol
	private String 					rtmp_app;
	private String 					rtmp_stream;


	public PacketsManager(ListFile fileList) 
	{
		this.fileList 		= fileList;
		this.packetCache 	= new ArrayList<TMPacket>();
		this.streamsFilter	= new StreamsFilter();
		this.isActive 		= true;
		this.rtmp_app 		= "";
		this.rtmp_stream	= "";
	}
	
	//=====================================================================================================
	
	public void run()
	{		
		System.out.println("*TubeMaster++ Core Started*");
		
		while (this.isActive) 
		{
			TMPacket p = CaptureSystem.packetList.poll();			
			
			if ((p != null) && (p.isValid()))
			{

				boolean found = this.search_stream(p);

				if ((!found) && (this.streamsFilter.isNotFiltered(p.getAck())))
				{	
					//HTTP Protocol
					if (p.searchFLV()) this.new_stream("FLV", p);
					else if (p.searchMP3()) this.new_stream("MP3", p);
					else if (p.searchMP4()) this.new_stream("MP4", p);
					else if (p.searchM4A()) this.new_stream("M4A", p);
					else if (p.searchMOV()) this.new_stream("MOV", p);
						
					//RTMP Protocol
					else if (p.searchRTMP_Phase1()) this.rtmp_app = p.extractRTMPParameter(((char)5+"tcUrl"));
					else if ((!this.rtmp_app.equals("")) && (this.rtmp_stream.equals("")) && (p.searchRTMP_Phase2()))
					{
						this.rtmp_stream = p.extractRTMPParameter(((char)4+"play"));
						this.new_rtmp();
					}
		
						
					//Useless Packets
					else if (p.searchFileHeader()) this.addToCache(p); 	/* Cache Packets with Headers */
					else this.streamsFilter.addToFilter(p.getAck());	/* Useless Stream, Set Filter */ 		
				}
				
			}
			else try {Thread.sleep(1);} catch (Exception e) {Commun.logError(e);}

		}
		System.out.println("*TubeMaster++ Core Closed*");
	}
	
	//=====================================================================================================
	
	private void addToCache(TMPacket p)
	{
		if (this.packetCache.size() >= 100) this.packetCache.remove(0);
		this.packetCache.add(p);
	}
		
	//=====================================================================================================	
	
	private boolean search_stream(TMPacket p)
	{
		int len = this.fileList.getItemsCount();
		for (int i=0;i<len;i++)
		{
			StreamFile stream = this.fileList.getItem(i).getFile();
			if (stream.get_ack_number() == p.getAck())
			{		
				stream.add_datas(p);
				return true;
			}	
			else if (stream.has_brothers())
			{
				for(int j=0;j<stream.get_brothers().size();j++)
				{
					if (stream.get_brothers().get(j).get_ack_number() == p.getAck())
					{		
						stream.get_brothers().get(j).add_datas(p);
						return true;
					}
				}
			}	
		}
		
		return false;	
	}
	
	//=====================================================================================================	

	private void new_stream(String str_format, TMPacket p)
	{	
		/* Search content length */
		long size = this.stream_content_length(p);
				
		/* Check if not from MP3 downloader */
		if ((str_format.equals("MP3")) && (size == CaptureSystem.filterSize)) 
		{
			size = -1;
			CaptureSystem.filterSize = 0;		
		}
		
		/* Search url */
		String url = this.stream_url(p);
		
		/* Check if it's from Youtube */
		boolean from_youtube = false;
		if (url.contains(".youtube.com/") && url.contains("signature="))
		{
			from_youtube = true;
		}
		
		/* If not too small, let's go! */
		if (size > Integer.parseInt(MainForm.opts.minimal) || from_youtube)
		{
			
			/* Youtube start part */
			if (from_youtube && size == 1781747)
			{
				size += 1781747;
			}
			
			/* Youtube other parts */
			StreamFile parent_stream = null;
			if (from_youtube && size != 1781747)
			{
				parent_stream = search_youtube(url);
				
				if (parent_stream != null)
				{
					if (size < 1781747) parent_stream.red_content_length(1781747);
				}
			}

			FileFormat format = new FileFormat(str_format);
			StreamFile new_stream = new StreamFile(format, p, size, "", url, parent_stream, from_youtube);
			
			/* Search for datas in cache */
			for (int i=0;i<this.packetCache.size();i++)
			{
				if (p.getAck()==this.packetCache.get(i).getAck())
				{
					new_stream.add_datas(this.packetCache.get(i));
					this.packetCache.remove(i);
					i--;				
				}
			}
			
			if (parent_stream != null)
			{
				parent_stream.add_brother(new_stream);
			}
			else
			{
				/* Add to the list */
				this.fileList.ajoutItem(new ListFileItem(this.fileList,new_stream,url));	
			}		
		}
	}
	
	//=====================================================================================================	
	
	private long stream_content_length(TMPacket p)
	{
		/* Search content length */
		long size = p.search_content_length();
		if (size == -1)
		{
			/* Search in the cache */
			int len = this.packetCache.size();
			for (int i=0;i<len;i++)
			{
				if (p.getAck() == this.packetCache.get(i).getAck())
				{					
					size = this.packetCache.get(i).search_content_length();
					if (size > -1) 
					{
						this.packetCache.remove(i);
						break;					
					}
				}	
			}		
		}
		return size;
	}
	
	//=====================================================================================================	
	
	private String stream_url(TMPacket p)
	{
		/* Search stream url */
		String url = p.search_url();
		if (url.equals("http:///"))
		{
			
			/* Search in the cache */
			int len = this.packetCache.size();
			for (int i=0;i<len;i++)
			{
				if (p.getPorts() == this.packetCache.get(i).getPorts())
				{					
					url = this.packetCache.get(i).search_url();
					if (url.equals("http:///") == false) 
					{
						this.packetCache.remove(i);
						break;					
					}
				}	
			}		
		}
		return url;
	}
	
	//=====================================================================================================	
	
	public void new_rtmp()
	{
		
		if ((!this.rtmp_app.equals("")) && (!this.rtmp_stream.equals("")))
		{
			RTMPDownload rtmpdown = new RTMPDownload(this.rtmp_app, this.rtmp_stream);
			rtmpdown.start();
			this.fileList.ajoutItem(new ListFileItem(this.fileList,rtmpdown.get_stream_file(),this.rtmp_app+"/"+this.rtmp_stream));	
		}
		
		this.rtmp_app = "";
		this.rtmp_stream = "";
	}
	
	//=====================================================================================================
	
	private StreamFile search_youtube(String new_stream_url)
	{
		int len = this.fileList.getItemsCount();
		for (int i=0;i<len;i++)
		{
			StreamFile stream = this.fileList.getItem(i).getFile();
			String stream_url = stream.get_url();
			if (stream_url.contains(".youtube.com/") && stream_url.contains("signature="))
			{
				String stream_sign = Commun.parse(stream_url, "signature=", "&");
				String packet_sign = Commun.parse(new_stream_url, "signature=", "&");
				
				if (stream_sign.equals(packet_sign)) 
				{
					return stream;	
				}
			}
		}
		
		return null;
	}
	
	//=====================================================================================================	

	public void shutUp()
	{
		this.isActive = false;
		
	}
	
	//=====================================================================================================


}

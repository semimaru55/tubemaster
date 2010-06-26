package Capture;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;


import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import Conversion.CommandRunner;
import Main.Commun;

public class StreamFile 
{
	
	private long 				ack_number;
	private long 				content_length;
	private long 				current_length;
	private long 				first_sequence;
	private long 				current_sequence;
	private TCPMap 				tcp_map;
	private File				file;
	private boolean				complete;
	private boolean				cancelled;
	private String				filepath;
	private RandomAccessFile 	writer;
	private FileFormat			format;


	public StreamFile(FileFormat format, TMPacket p, long content_length, String filepath)
	{
		this.content_length 	= content_length;
		this.current_length		= 0;
		this.current_sequence	= -1;
		this.complete 			= false;
		this.cancelled			= false;
		this.tcp_map			= new TCPMap();
		this.filepath			= filepath;
		this.format				= format;

		this.create_file();
		
		if (p != null)
		{
			p.removeHTTPHeader();
			this.first_sequence = p.getSeq();
			this.add_datas(p);
		}
		
	}
	
	//=====================================================================================================
	
	private void create_file()
	{	
		try
		{
			if (this.filepath.equals(""))
			{
				/* Create File In temp dir */
				new File("temp").mkdir();
				long idFile = (new Date()).getTime();
				long random = (long) (Math.random()*100000);
				this.filepath = "temp"+File.separator+"tm++_capture_"+idFile+random+"."+this.format.retFormat().toLowerCase();
			
				/* Create file on the drive */
				this.file = new File(this.filepath);
				this.writer = new RandomAccessFile(this.file,"rw");

			}
			else
			{
				/* File loaded */
				this.current_length = this.content_length;
				this.finnish();
			}
			
		} catch (Exception e) {Commun.logError(e);}
	
	}

	//=====================================================================================================
	
	public void add_datas(TMPacket p)
	{
		this.ack_number = p.getAck();
		
		if (this.complete == false)
		{
			if (p.getSeq() >= this.first_sequence)
			{						
				this.add_packet_size(p);
				this.tcp_map.addSeq(p.getSeq(),p.getDatas().length); 
				
				try
				{
					this.writer.seek(p.getSeq()-this.first_sequence);	
					this.writer.write(p.getDatas());
			
				} catch (Exception e) {Commun.logError(e);}

			}

			if (this.current_length >= this.content_length) this.finnish();
		}	
	}
	
	//=====================================================================================================

	private void add_packet_size(TMPacket p)
	{
		if (p.getSeq() > this.current_sequence) //Normal
		{
			this.current_sequence = p.getSeq();
			this.current_length += p.getDatas().length;
		}
		else //Retransmission
		{
			long toWrite = p.getDatas().length;
			int mapPos = this.tcp_map.seqPos(p.getSeq());
			if (mapPos > -1)
			{
				long oldSize = this.tcp_map.getSize(mapPos);
				if (toWrite >= oldSize) toWrite = p.getDatas().length - oldSize;
				else toWrite = 0;	
			}
			
			this.current_length += toWrite;												
		}	
	}
	
	//=====================================================================================================
	
	public void finnish()
	{
		this.complete = true;
		this.close();	
	}
	
	//=====================================================================================================
	
	public void cancel()
	{
		this.cancelled = true;
		this.close();
		try {if (this.file != null) this.file.delete();} catch (Exception e) {Commun.logError(e);}
	}
	
	//=====================================================================================================
	
	public void close()
	{
		this.tcp_map.clearMap();
		this.ack_number = -1;	
		try {if (this.writer != null) this.writer.close();} catch (Exception e) {Commun.logError(e);}
	}
	
	//=====================================================================================================
	
	public String find_ID3()
	{
		String artiste = "";
		String name = "";
		
		if (this.file != null)
		{
			MediaFile oMediaFile = new MP3File(this.file);
	        try 
	        {
				ID3Tag[] aoID3Tag = oMediaFile.getTags();
	
				for(int i=0;i<aoID3Tag.length;i++)
				{					
					if (aoID3Tag[i] instanceof ID3V1_0Tag)
		            {
		                ID3V1_0Tag v1 = (ID3V1_0Tag)aoID3Tag[i];
		                if (v1.getTitle() != null) name = v1.getTitle();
		                if (v1.getArtist() != null) artiste = v1.getArtist();
		            }
					else if (aoID3Tag[i] instanceof ID3V2_3_0Tag)
		            {
		                ID3V2_3_0Tag v2 = (ID3V2_3_0Tag)aoID3Tag[i];
		                if (v2.getTitle() != null) name = v2.getTitle();
		                if (v2.getArtist() != null) artiste = v2.getArtist();   
		            }	
				}		
		
			} catch (ID3Exception e) {/*NoLog*/}
		
		}		
		name.replaceAll(""+(char)0, "");
		artiste.replaceAll(""+(char)0, "");
		
		return name + " - " + artiste;
	}
		
	//=====================================================================================================
	
	public void play()
	{		
		CommandRunner cmd = new CommandRunner(this.format.retPlayer()+" \""+this.filepath+"\"");
		Thread threadManager = new Thread(cmd);
		threadManager.start();
	}
	
	//=====================================================================================================
	
	public long 		get_ack_number()			{ return this.ack_number; }
	public FileFormat 	get_format() 				{ return this.format; }
	public String 		get_filepath() 				{ return this.filepath; }
	public boolean 		is_complete() 				{ return this.complete; }
	public boolean		is_cancelled()				{ return this.cancelled; }
	public long 		get_content_length() 		{ return this.content_length; }
	public long 		get_current_length() 		{ return this.current_length; }
	public void 		add_rtmp_datas(long l) 	
	{ 
		this.current_length = l;
		this.content_length = this.current_length + 1000000;
	}
		
	//=====================================================================================================
}

package Capture;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;


import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

import Conversion.CommandRunner;
import Main.Commun;
import Main.MainForm;

public class StreamFile 
{
	
	private long 					ack_number;
	private long 					content_length;
	private long 					current_length;
	private long 					first_sequence;
	private long 					current_sequence;
	private TCPMap 					tcp_map;
	private File					file;
	private boolean					complete;
	private boolean					cancelled;
	private String					filepath;
	private RandomAccessFile 		writer;
	private FileFormat				format;
	private String					url;
	private ArrayList<StreamFile>	brothers;
	private StreamFile				big_brother;
	private long					brother_decal;
	private boolean					is_youtube;
	
	public StreamFile(FileFormat format, TMPacket p, long content_length, String filepath, String url, StreamFile big_brother, boolean is_youtube)
	{
		this.content_length 	= content_length;
		this.current_length		= 0;
		this.current_sequence	= -1;
		this.complete 			= false;
		this.cancelled			= false;
		this.tcp_map			= new TCPMap();
		this.filepath			= filepath;
		this.format				= format;
		this.url				= url;
		this.brothers			= new ArrayList<StreamFile>();
		this.big_brother		= big_brother;
		this.brother_decal		= 0;
		this.is_youtube 		= is_youtube;
		
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
				if (this.big_brother == null)
				{
					/* Create File In temp dir */
					new File("temp").mkdir();
					long idFile = (new Date()).getTime();
					long random = (long) (Math.random()*100000);
					this.filepath = MainForm.tm_path + File.separator + "temp" + File.separator + "tm++_capture_"+idFile+random+"."+this.format.retFormat().toLowerCase();
				
					/* Create file on the drive */
					this.file = new File(this.filepath);
					this.writer = new RandomAccessFile(this.file,"rw");
					
					if (this.is_youtube)
					{
						byte data[] = {0x46, 0x4C, 0x56, 0x01, 0x04, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x00};
						try
						{
							this.writer.seek(0);	
							this.writer.write(data);
							this.brother_decal = 13;
					
						} catch (Exception e) {Commun.logError(e);}
					}
					
				}
				else
				{
					this.filepath = this.big_brother.get_filepath();
					this.file = this.big_brother.get_file();
					this.writer = this.big_brother.get_writer();	
				}

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
					this.writer.seek(this.brother_decal+p.getSeq()-this.first_sequence);	
					this.writer.write(p.getDatas());
			
				} catch (Exception e) {Commun.logError(e);}

			}

			if (this.get_current_length() >= this.get_content_length()) this.finnish();
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
		if (this.big_brother != null) 
		{
			this.big_brother.check_finished_from_brother();
		}
		else
		{
			this.close();	
		}
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
		
		return artiste + " - " + name;
	}
		
	//=====================================================================================================
	
	public void play()
	{		
		CommandRunner cmd = new CommandRunner(this.format.retPlayer()+" \""+this.filepath+"\"");
		Thread threadManager = new Thread(cmd);
		threadManager.start();
	}
	
	//=====================================================================================================
	
	public void add_brother(StreamFile stream)
	{
		this.brothers.add(stream);
		try { stream.set_decal(this.writer.length()); } catch (IOException e) {Commun.logError(e);}
	}
	
	//=====================================================================================================
	
	public long get_content_length() 		
	{
		long len = this.content_length;
		for(int i=0;i<this.brothers.size();i++)
		{
			len += this.brothers.get(i).get_content_length();
		}
		return len; 
	}
	
	//=====================================================================================================
	
	public long get_current_length() 		
	{ 
		long len = this.current_length;
		for(int i=0;i<this.brothers.size();i++)
		{
			len += this.brothers.get(i).get_current_length();
		}

		return len;
	} 
	
	//=====================================================================================================
	
	public void check_finished_from_brother()
	{
		if (this.complete == false)
		{
			if (this.get_current_length() >= this.get_content_length()) this.finnish();
		}
	}
	
	//=====================================================================================================
	
	public long 		get_ack_number()			{ return this.ack_number; }
	public void 		set_ack_number(long ack)	{ this.ack_number = ack; }
	public void 		red_content_length(long l)	{ this.content_length -= l; }
	public FileFormat 	get_format() 				{ return this.format; }
	public String 		get_filepath() 				{ return this.filepath; }
	public File 		get_file() 					{ return this.file; }
	public void			set_decal(long d)			{ this.brother_decal = d; }
	public RandomAccessFile get_writer() 			{ return this.writer; }
	public String 		get_url() 					{ return this.url; }
	public boolean		is_from_youtube()			{ return this.is_youtube; }
	public boolean 		is_complete() 				{ return this.complete; }
	public boolean		is_cancelled()				{ return this.cancelled; }
	public ArrayList<StreamFile> get_brothers() 	{ return this.brothers; }
	public boolean		has_brothers()				{ return this.brothers.size() > 0; }
	public void 		add_rtmp_datas(long l) 	
	{ 
		this.current_length = l;
		this.content_length = this.current_length + 1000000;
	}
	

		
	//=====================================================================================================
}

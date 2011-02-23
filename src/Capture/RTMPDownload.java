package Capture;

import java.io.File;
import java.util.concurrent.Future;

import Main.Commun;

import com.smaxe.app.uv.downloader.RtmpDownloader;

public class RTMPDownload extends Thread
{
	
	private String 			rtmp_app;
	private String			rtmp_stream;
	private RtmpDownloader 	downloader;
	private StreamFile 		file;
	
	

	public RTMPDownload(String app, String stream)
	{
		this.rtmp_app 		= app;
		this.rtmp_stream 	= stream;
		this.downloader 	= new RtmpDownloader();
		this.file 			= new StreamFile(this.find_format(), null, 3000000, "");
	}
	
	//=====================================================================================================
	
	public void run()
	{
		
		final Future<?> future = downloader.download(this.rtmp_app,null,null,this.rtmp_stream,this.file.get_filepath());
		
		File f = new File(this.file.get_filepath());

		while (!future.isDone())
        {
            try 
            {
				Thread.sleep(1000);
				this.file.add_rtmp_datas(f.length());	
				if (this.file.is_cancelled()) future.cancel(true); 
				
			} catch (Exception e) {Commun.logError(e);}

        }
		
		if (this.file.is_cancelled())
		{
			try {Thread.sleep(5000);} catch (InterruptedException e) {}
			f.delete();
		}
		else
		if (f.length() == 0) this.file.cancel();
		else
	  	{
    		this.file.finnish();
	  	} 
		
	}
	
	//=====================================================================================================
	
	private FileFormat find_format()
	{
		String url = (this.rtmp_app+this.rtmp_stream).toLowerCase();
		if (url.contains("mp4")) return new FileFormat("MP4");
		else return new FileFormat("FLV");	
	}
	
	//=====================================================================================================
	
	public StreamFile get_stream_file()	{ return this.file; }
	
	//=====================================================================================================

	
}

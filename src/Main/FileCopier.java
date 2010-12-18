package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JProgressBar;

import Graphique.TMButton;

public class FileCopier extends Thread
{
	
	private File f1;
	private File f2;
	private JProgressBar pb;
	private TMButton btn;

	public FileCopier(File f1, File f2, JProgressBar pb, TMButton btn)
	{
		this.f1 = f1;
		this.f2 = f2;
		this.pb = pb;
		this.btn = btn;
	}
	

	public void run() 
	{
	    try
	    {
	      InputStream in = new FileInputStream(f1); 
	      OutputStream out = new FileOutputStream(f2);

	      btn.setVisible(false);
	      pb.setVisible(true);
	      pb.setMaximum((int)f1.length());
	      pb.setMinimum(0);
	      pb.setValue(0);
	      int copied = 0;
	      int count = 1;
	      
	      byte[] buf = new byte[8192];
	      int len;
	      while ((len = in.read(buf)) > 0)
	      {
	        out.write(buf, 0, len);
	        copied += len;
	        pb.setValue(copied);
	        if ((count % 10) == 0) Thread.sleep(1);
	        count++;
	      }
	      
	      Thread.sleep(1000);
	      
	      pb.setVisible(false);
	      btn.setVisible(true);
	      
	      in.close();
	      out.close();
	      		      
	    }
	    catch(Exception ex) {Commun.logError(ex);}

		
	}

}

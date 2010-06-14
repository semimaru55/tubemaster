package Main;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;


public class TrayMenu extends PopupMenu 
{
	

	private static final long serialVersionUID = 1L;
	
	private MenuItem menu_exit = new MenuItem(MainForm.lang.lang_table.get(56));
	private MenuItem menu_start_stop = new MenuItem(MainForm.lang.lang_table.get(7));

	public TrayMenu(ActionListener listener)
	{
		super();
		
		this.menu_exit.setActionCommand("EXIT_TRAY");
		this.menu_exit.addActionListener(listener);
		
		this.menu_start_stop.setActionCommand("START_STOP_TRAY");
		this.menu_start_stop.addActionListener(listener);
		
		this.add(this.menu_exit);
		this.add(this.menu_start_stop);
	}
	
	
	public void set_state(boolean started)
	{
		if (started) this.menu_start_stop.setLabel(MainForm.lang.lang_table.get(6));
		else this.menu_start_stop.setLabel(MainForm.lang.lang_table.get(7));	
	}
	
	
	
	
	
	

}

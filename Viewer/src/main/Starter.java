package main;

import org.apache.pivot.wtk.*;
import org.apache.pivot.beans.*;
import org.apache.pivot.collections.*;

public class Starter implements Application	{

	@Override
	public void startup(Display display, Map<String,String> properties) throws Exception	{
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		ViewerWindow window = (ViewerWindow) bxmlSerializer.readObject(Starter.class,"ui.bxml");
		//DesktopApplicationContext.setFullScreen(true);
		window.open(display);	
	}	
	
	
	@Override
	public boolean shutdown(boolean optional)	{
//		if (window!=null)	{
//			window.close();
//		}
		return false;
	}
	
	@Override
	public void suspend()	{
		
	}
	
	@Override
	public void resume()	{
		
	}
	
	public static void main(String[] args)	{
		DesktopApplicationContext.main(Starter.class,args);
	}
}

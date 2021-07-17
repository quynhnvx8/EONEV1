package org.compiere.apps;

public interface IStatusBar 
{
	public void setStatusLine (String text);
	
	public void setStatusLine (String text, boolean error);
	
	public void setInfo (String text);
}

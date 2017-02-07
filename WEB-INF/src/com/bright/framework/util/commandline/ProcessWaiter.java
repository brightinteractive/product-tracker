package com.bright.framework.util.commandline;

/**
 * Bright Interactive,  Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * ProcessWaiter.java
 *
 * Contains the ProcessWaiter class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	14-Sep-2006		Martin Wilson		Created
 d2 22-May-2007		Kevin Bennett		Imported from image bank
 --------------------------------------------------------------------------------
 */


/**
 * Waits for a process to exit (in a new thread), but gives a calling thread a
 * chance to give up on it if necessary.
 * 
 * @author Bright Interactive
 * @version d1
 */
class ProcessWaiter extends Thread
{
	private Process m_process = null;
	private boolean m_bFinished = false;
	private int m_iExitValue = 0;
	
	/**
	 * @param a_is
	 * @param a_sbOutput
	 */
	public ProcessWaiter(Process a_process)
	/*
	---------------------------------------------------------------
	d1	14-Sep-2006		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		m_process = a_process;
	}
	
	/**
	 * Gets the output and stores it in m_sbOutput
	 * 
	 * @see java.lang.Runnable#run()
	 * 
	 */
	public void run()
	/*
	---------------------------------------------------------------
	d1	14-Sep-2006		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		try
		{
			m_iExitValue = m_process.waitFor();
			setFinished(true);
		} 
		catch (InterruptedException ioe)
		{
			ioe.printStackTrace(); 
			setFinished(true);
		}
	}
	
	
	public synchronized boolean hasFinished()
	{
		return m_bFinished;
	}
	public synchronized void setFinished(boolean a_sFinished)
	{
		m_bFinished = a_sFinished;
	}
	public int getExitValue()
	{
		return m_iExitValue;
	}
}


package com.bright.framework.util.commandline;

/**
 * Bright Interactive,  Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * StreamGobbler.java
 *
 * Contains the StreamGobbler class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	14-Sep-2006		Martin Wilson		Created
 d2 22-May-2007		Kevin Bennett		Imported from image bank
 --------------------------------------------------------------------------------
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Reads from an input stream (in another thread).
 * 
 * @author Bright Interactive
 * @version d1
 */
class StreamGobbler extends Thread
{
	private InputStream m_inputStream = null;
	private StringBuffer m_sbOutput = null;
	private boolean m_bFinished = false;
	
	/**
	 * @param a_is
	 * @param a_sbOutput
	 */
	public StreamGobbler(InputStream a_is,
								StringBuffer a_sbOutput)
	/*
	---------------------------------------------------------------
	d1	14-Sep-2006		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		m_inputStream = a_is;
		m_sbOutput = a_sbOutput;
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
			InputStreamReader isr = new InputStreamReader(m_inputStream);
			BufferedReader br = new BufferedReader(isr);
			String sLine = null;
			
			while ( (sLine = br.readLine()) != null)
			{
				if (m_sbOutput != null)
				{
					m_sbOutput.append(sLine); 
				}
			}
		} 
		catch (IOException ioe)
		{
			ioe.printStackTrace();  
		}
		
		setFinished(true);
	}
	
	private synchronized void setFinished(boolean a_bFinished)
	{
		m_bFinished = a_bFinished;
	}
	
	public synchronized boolean hasFinished()
	{
		return (m_bFinished);
	}
}


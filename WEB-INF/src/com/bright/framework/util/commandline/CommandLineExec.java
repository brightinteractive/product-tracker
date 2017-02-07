package com.bright.framework.util.commandline;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.GlobalApplication;
import com.bright.framework.image.exception.ImageException;

/**
 * Bright Interactive,  Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * CommandLineExec.java
 *
 * Contains the CommandLineExec class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1		14-Sep-2006			Martin Wilson		Created
 d2		16-Oct-2006			Adam Bones			Specify commands as arrays.
 d3		02-Nov-2006			Martin Wilson		Added execute override to put error text in exception 
 d4		24-Nov-2006			Martin Wilson		Updated execute to handle buffers more efficiently
 d5 	22-May-2007			Kevin Bennett		Imported from image bank
  --------------------------------------------------------------------------------
 */

/**
 * Used to safely execute a command line command.
 * 
 * @author Bright Interactive
 * @version d1
 */
public class CommandLineExec
{
	public static final long k_lWaitTimeInMillis = 50;
	
	/**
	 * Executes the command - without a timeout and in single thread.
	 * 
	 * @param a_sCommand
	 * @param a_sbOutput
	 * @param a_sbErrors
	 * @return
	 */
	public static int execute(String[] a_saCommand,
										StringBuffer a_sbOutput,
										StringBuffer a_sbErrors)
	throws Bn2Exception
	/*
	 ---------------------------------------------------------------
	 d1		14-Sep-2006		Martin Wilson		Created 
	 -----------------------------------------------------------------
	 */	
	{	
		return (execute(a_saCommand,
							 a_sbOutput,
							 a_sbErrors,
							 -1));
	}
	
	/**
	 * Executes the command.
	 * 
	 * @param a_sCommand
	 * @param a_sbOutput
	 * @param a_sbErrors
	 * @param a_lProcessTimeout - length of time to wait for the process to finish before giving up.
	 * 									If <0 then waits until finished.
	 */
	public static int execute(String[] a_saCommand,
										StringBuffer a_sbOutput,
										StringBuffer a_sbErrors,
										long a_lProcessTimeout)
	throws Bn2Exception
	/*
	 ---------------------------------------------------------------
	 d1		14-Sep-2006		Martin Wilson		Created
	 d2		02-Oct-2006		Adam Bones			Specify command as an array.
	 d4		24-Nov-2006		Martin Wilson		Updated to handle buffers more efficiently	 
	 -----------------------------------------------------------------
	 */
	{
		int iExitVal = -1;
		
		try
		{            
			
			Runtime rt = Runtime.getRuntime();
			
			Process proc = rt.exec(a_saCommand);
			
			// Want to get error messages:
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(),
																		  a_sbErrors);            
			
			// Want to get output:
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),
																			a_sbOutput);
			
			// Grab the error stream in a new thread:		
			errorGobbler.start();
				
			// Get the output stream (no need to do this is a separate thread):
			outputGobbler.run();		
			
			// See if we want to time out:
			if (a_lProcessTimeout > 0)
			{
				// Wait for the process in a new thread, as we may want to give up on it:
				ProcessWaiter processWaiter = new ProcessWaiter(proc);
				processWaiter.start();
				
				long lStartTime = System.currentTimeMillis();
				
				// Loop until finshed or timed out:
				while ( (!processWaiter.hasFinished() &&
							(lStartTime + a_lProcessTimeout) > System.currentTimeMillis()) )
				{
					// Free up the processor for a bit:
					Thread.sleep(k_lWaitTimeInMillis);
				}
				
				// See if the process finished:
				if (!processWaiter.hasFinished())
				{
					// Not finished: kill the process:
					proc.destroy();
				}	
				else
				{
					iExitVal = processWaiter.getExitValue();
				}
			}
			else
			{
				// No timeout, so wait for the process to finish:
				iExitVal = proc.waitFor();
			}
			
			// If error, check that the reader has finished reading from the buffers:
			if (iExitVal != 0)
			{
				while (!errorGobbler.hasFinished())
				{
					// Free up the processor for a bit:
					Thread.sleep(k_lWaitTimeInMillis);
				}
			}			
		} 
		catch (Throwable t)
		{
			throw new Bn2Exception(t.toString());
		}	
		
		return (iExitVal);
	}		
	
	/**
	 * Convenience method - calls execute without returning output/errors.
	 * If there's an error a Bn2Exception is thrown, which holds the error text.
	 * 
	 * @param a_saCommand
	 * @return
	 * @throws ImageException
	 */
	public static String execute(String[] a_saCommand)
	throws Bn2Exception
	/*
	---------------------------------------------------------------
	d3		02-Nov-2006			Martin Wilson		Added execute override to put error text in exception 
	-----------------------------------------------------------------
	*/	
	{
		StringBuffer sbOut = new StringBuffer();
		
		// Build the String for debug purposes:
		String sDebugCommand = "";
		for (int i=0; i<a_saCommand.length; i++)
		{
			sDebugCommand+= a_saCommand[i] + " ";
		}
		
		// Generate some debug ouput
		GlobalApplication.getInstance ().getLogger().debug("CommandLineExec.execute: about to run command " + sDebugCommand);
		
		// Run the process:
		int iCode = -1;

		try
		{		
			StringBuffer sbErrors = new StringBuffer();
			iCode = CommandLineExec.execute(a_saCommand,  sbOut, sbErrors);
			
			if (iCode != 0)
			{
				throw new Bn2Exception("Failed to run command: " + sDebugCommand + " returned code: " + iCode + ". Error output: " + sbErrors.toString());
			}
		}
		catch (Exception e)
		{
			throw new Bn2Exception("Failed to run command: " + sDebugCommand + " : " + e.getMessage());
		}
		
		return sbOut.toString();
	}	
}

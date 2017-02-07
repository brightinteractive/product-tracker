package com.bright.framework.application.servlet;

/**
 * Bright Interactive, Internal
 *
 * Copyright 2004 Bright Interactive,, All Rights Reserved.
 * ApplicationActionServlet.java
 * 
 * Contains the ApplicationActionServlet class.
 */
/*
 Ver  Date             Who		Comments
 --------------------------------------------------------------------------------
 d1   18-Mar-2005  James Home		Created.
 --------------------------------------------------------------------------------
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bn2web.common.service.GlobalApplication;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.util.FileUtil;

/**
 * Servlet for file downloads
 *
 * @author  bn2web
 * @version d1
 */
public class FileDownloadServlet extends HttpServlet implements FrameworkConstants
{
	
	/**
	 * Processes a get request
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * @param a_sArg0
	 * @param a_sArg1
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */	
	public void doGet(HttpServletRequest a_request, HttpServletResponse a_response)
	throws IOException, ServletException
	/*
	 ---------------------------------------------------------------
	 d1	18-May-2005		James Home		Created 
	 -----------------------------------------------------------------
	 */
	{			
		processDownload(a_request,a_response);
	}
	
	/**
	 * Processes a post request
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * @param a_sArg0
	 * @param a_sArg1
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */	
	public void doPost(HttpServletRequest a_request, HttpServletResponse a_response)
	throws IOException, ServletException
	/*
	 ---------------------------------------------------------------
	 d1	18-May-2005		James Home		Created 
	 -----------------------------------------------------------------
	 */
	{
		processDownload(a_request,a_response);
	}
	
	/**
	 * Does the download
	 * 
	 * @param a_request
	 * @param a_response
	 * @throws IOException
	 * @throws ServletException
	 */	
	private void processDownload(HttpServletRequest a_request, HttpServletResponse a_response)
	throws IOException
	/*
	---------------------------------------------------------------
	d1		18-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		File file = null;
		String sFile = null;
		String sFilename = null;
		String sFailureUrl = null;
		InputStream is = null; 
		boolean bDeleteDownloadedFile = false;
		
		try 
		{
			// Get and qualify the filename of the file
			sFile = (String)a_request.getAttribute(k_sAttributeName_DownloadFile);
			sFile = FileUtil.decryptFilepath(sFile);
			sFile = FrameworkSettings.getFilestoreRootFullPath() + File.separator + sFile;
			
			// Get the filename to send to the client
			sFilename = (String)a_request.getAttribute(k_sAttributeName_DownloadFilename);
			
			// Make sure the filename is safe
			sFilename = FileUtil.getSafeFilename(sFilename, true);
			
			// Get the failure url
			sFailureUrl = (String)a_request.getAttribute(k_sAttributeName_DownloadFailureUrl);
			
			// Get the delete flag
			if(a_request.getAttribute(k_sAttributeName_DeleteFileAfterUse)!=null)
			{
				bDeleteDownloadedFile = ((Boolean)a_request.getAttribute(k_sAttributeName_DeleteFileAfterUse)).booleanValue();
			}
					
			// Create the file object
			file = new File(sFile);
			
			if(file.exists() && file.canRead())
			{
				try
				{
					a_response.setContentType("application/x-download");
					a_response.addHeader("content-disposition", "attachment; filename=" + sFilename);
					
					is = new FileInputStream(sFile);         
		         
		         byte[] buffer = new byte[0xffff]; // buffer size = FFFF hex
		         int nbytes;
		         
		         // Copy all the bytes to the response:
		         while ((nbytes = is.read(buffer)) != -1)
		         {
		            a_response.getOutputStream().write(buffer, 0, nbytes);
		         }
				}
				catch(IOException ioe)
				{
					// Do nothing
				}
			}
			else
			{
				// Redirect to the error url
				GlobalApplication.getInstance().getLogger().error("FileDownloadServlet.processDownload() : File cannot be downloaded : " + sFile);
				a_response.sendRedirect(sFailureUrl);
			}
		}
		finally
		{
			try 
			{
				// Flush the output buffer 
				a_response.flushBuffer();								
			}
			finally
			{
				// Close file the input steam
				if(is!=null)
				{
					is.close();
				}
				
				// Delete the downloaded file if required
	         if(bDeleteDownloadedFile && file!=null)
	         {
	         	file.delete();
	         }
			}			
		}
	}
}
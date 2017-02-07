package com.bright.framework.util;

/**
 * Bright Interactive framework classes
 *
 * Copyright 2003 bn2web, All Rights Reserved.
 * HTMLPostRequest.java
 * 
 * Contains the HTMLPostRequest class.
 */
/*
 Ver  Date	    		Who					Comments
 --------------------------------------------------------------------------------
 d1   15-Sep-2004     Martin Wilson     Created.
 --------------------------------------------------------------------------------
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.GlobalApplication;

/**
 * Class to simplify making a POST request
 *
 * @author  Bright Interactive
 * @version d1
 */
public class HTMLPostRequest
{
   private String m_sUrl = null;
	private String m_sContent = "";

	/**
	 * Default constructor - forces use of HTMLPostRequest(String)
	 *
	 **/
	private HTMLPostRequest()
	/*
	 ------------------------------------------------------------------------     
	 d1   15-Sep-2004     Martin Wilson     Created.
	 ------------------------------------------------------------------------
	 */
	{
		/* Empty */
	}
	
	/**
	 * Constructor
	 *
	 * @param String a_sURL - the url
	 **/
	public HTMLPostRequest(String a_sUrl)
	/*
	 ------------------------------------------------------------------------     
	 d1   15-Sep-2004     Martin Wilson     Created.
	 ------------------------------------------------------------------------
	 */
	{
		m_sUrl = a_sUrl;
	}

	/**
	 * Constructor
	 *
	 * @param String a_sParamName - the name of the parameter
	 * @param String a_sValue - the value
	 **/
	public void addParameter(String a_sParamName, String a_sValue)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------     
	 d1   15-Sep-2004     Martin Wilson     Created.
	 ------------------------------------------------------------------------
	 */
	{
		if(a_sParamName != null)
		{
			if(m_sContent.length() > 0)
			{
				m_sContent += "&";
			}

			// Avoid npe:
			if(a_sValue == null)
			{
				a_sValue = "";
			}

			// Add the param:
			try
			{
				m_sContent += a_sParamName + "=" + URLEncoder.encode(a_sValue,"UTF-8");
			}
			catch(UnsupportedEncodingException uee)
			{
				GlobalApplication.getInstance().getLogger().error("HTMLPostRequest.addParameter() : problem encoding URL : " + uee);
				throw new Bn2Exception("HTMLPostRequest.addParameter() : problem encoding URL",uee);
			}
		}
	}

	/**
	 * Makes the request.
	 *
	 * @return String - the response from the server, as a String
	 * @throws Bn2Exception - on error
	 **/
	public String post()	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------     
	 d1   15-Sep-2004     Martin Wilson     Created.
	 ------------------------------------------------------------------------
	 */
	{
		DataOutputStream printout;
		DataInputStream input;
		URL url = null;
		URLConnection urlConn = null;
		String sResponse = "";
		
		if (m_sUrl == null)
		{
			throw new Bn2Exception("The URL is null in HTMLPostRequest.post");
		}

		try
		{
			url = new URL(m_sUrl);
		}
		catch (MalformedURLException e)
		{
			throw new Bn2Exception("Malformed URL in HTMLPostRequest.post: " + e.getMessage());
		}
		
		try
		{
			urlConn = url.openConnection();
	
			// Let the run-time system (RTS) know that we want input.
			urlConn.setDoInput(true);
			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);
			// No caching:
			urlConn.setUseCaches(false);
	
			// Specify the content type.
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	
			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());
	
			printout.writeBytes(m_sContent);
			printout.flush();
			printout.close();
	
			// Get response data.
			input = new DataInputStream(urlConn.getInputStream());
	
			// Get the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	
			String sNextLine = null;
	
			while((sNextLine = reader.readLine()) != null)
			{
				sResponse += sNextLine;
			}
		}
		catch (IOException e)
		{
			throw new Bn2Exception("IOException in HTMLPostRequest.post: " + e.getMessage());
		}
		
		return (sResponse);
	}

}
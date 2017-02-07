package com.bright.framework.util;

/**
 * Bright Interactive image manager classes
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * StringUtil.java
 *
 * Contains the StringUtil class.
 */
/*
	 Ver     Date            Who             Comments
	 --------------------------------------------------------------------------------
			14-Oct-2003     Matt Stevenson  Created from Img Mangr
			28-Oct-2003     Matt Stevenson  Added method for getting the index
										  	of the last index of a string in an
			 						  		array (should this be in a ArrayUtil class??)
			29-Oct-2003     Matt Stevenson  Added method to check if a string is formatted
									  		as a valid date (dd/mm/yyyy)
			29-Oct-2003     Martin Wilson   Added getIdsArray
			30-Mar-2004     Chris Preager   Copied isValidEmailAddr from James more recent string utils
											Added displayMoney()
	 d1     18-May-2004     Chris Preager   Copied from Reputable Trades
	 d2     22-Jun-2004		James Home		Added getHTMLBodyOnly
	 d3     23-Jun-2004		James Home      Created
	 d4     15-Jul-2004		Chris Preager   Strengthened dateIsString validation
	 d5     19-Jul-2004		Martin Wilson   Added replaceFirst
	 d6     22-Jul-2004		Martin Wilson   Changed getHTMLBodyOnly to use correct length for body tag
	 d7     01-Nov-2004		Chris Preager   Added stringIsPopulated()
	 d8   	12-Nov-2004   	Chris Preager   Added isValidUsername
	 d9   	22-Nov-2003   	Chris Preager	Created convertToVector
	 d10	07-Jan-2005		Martin Wilson	Added formatNewlineForHTML
	 d11    07-Jan-2005     James Home      Added addUrlParameter
	 d12    11-Jan-2005     James Home		Added geFirstPostcode 
	 d13	23-Mar-2005		Matt Stevenson  Changed stringIsDate
	 d14	24-Oct-2007		Matt Stevenson	Added containsAtLeastOneKeyword	
	 d15	26-Jul-2010		Kevin Bennett	Added getEmptyStringIfNull
											Added stringIsInteger
	

	 --------------------------------------------------------------------------------
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.GlobalApplication;


/**
 * Static methods for common string operations.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class StringUtil
{
	/**
	 * Within a passed string, replaces text encolsed in passed markup tags with
	 * a replacement string, which itself contains markup to indicate where the replaced
	 * string should be inserted (if at all). Does not require that all start tags are
	 * matched with end tags - ie, the string is returned with no Exception being raised.
	 * There are 2 placeholders that will be replaced by the marked-up text, one that
	 * replaces as-is and the other that replaces it with a javascipt safe version.
	 *
	 * eg, replaceMarkupText("apples are <*the best*> fruit","<*","*>","~","_~ of ~_")
	 * would return the string "apples are _the best of the best_ fruit"
	 *
	 * @param String a_sText - the input in which to replace the tags
	 * @param String a_sStartTag - the start tag
	 * @param String a_sEndTag - the end tag
	 * @param String a_sReplacementPattern - the pattern that will replace the tagged string
	 * @param String a_sReplacementMarker - the string in the replacement pattern that will
	 *                                      be replaced by the replaced (tagged) string
	 * @param String a_sReplacementMarkerJavascript - the string in the replacement pattern that will
	 *                                      be replaced by a javascript safe version of the replaced
	 *                                      (tagged) string
	 * @return String - the output with the tagged strings replaced
	 */
	public static String replaceMarkupText(String a_sText,
														String a_sStartTag,
														String a_sEndTag,
														String a_sReplacementPattern,
														String a_sReplacementMarkerHTML,
														String a_sReplacementMarkerJavascript)
	 /*
	 ------------------------------------------------------------------------
			 10-Apr-2003   James Home        Created from Img Mangr
			 07-May-2003   James Home        Added javascript-safe placeholder param
	  d1   14-Oct-2003   Matt Stevenson    Imported from HRHO
	 ------------------------------------------------------------------------
	  */
	{
		StringBuffer sbNewText = new StringBuffer("");;

		int iStartSearchPos = 0;
		int iStartTagPos = 0;
		int iEndTagPos = 0;
		String sReplacement = null;
		String sID = null;
		String sJavascriptSafeID = null;

		// Check that we have something to do
		if(a_sText == null)
		{
			return null;
		}

		while (iStartTagPos >= 0)
		{
			// Get the position of the next start tag:
			iStartTagPos = a_sText.indexOf(a_sStartTag, iStartSearchPos);

			if (iStartTagPos < 0)
			{
				// No more tags. Add rest of string:
				sbNewText.append(a_sText.substring(iStartSearchPos, a_sText.length()));
			}
			else
			{
				// Add what we've got so far, i.e. up to the tag:
				sbNewText.append(a_sText.substring(iStartSearchPos, iStartTagPos));

				// Find the end tag:
				iEndTagPos = a_sText.indexOf(a_sEndTag, iStartTagPos + a_sStartTag.length());

				// Check there is an end tag:
				if (iEndTagPos < 0)
				{
					// If not, return, as there are no more tagged substrings
					return (sbNewText.toString() + a_sText.substring(iStartTagPos));
				}

				// Get the id of the marked-up element (which is the text in between the tags):
				sID = a_sText.substring(iStartTagPos + a_sStartTag.length(), iEndTagPos);

				// Get the javascript safe version of the ID
				sJavascriptSafeID = getJavascriptLiteralString(sID);

				// Replace the standard (HTML) marker with the replacement text sID
				sReplacement = replaceString(a_sReplacementPattern,a_sReplacementMarkerHTML,sID);

				// Replace the javascript marker with the replacement text
				sReplacement = replaceString(sReplacement,a_sReplacementMarkerJavascript,sJavascriptSafeID);

				// Add this to the string:
				sbNewText.append(sReplacement);

				// Figure out where to start the search for the next tag:
				iStartSearchPos = iEndTagPos + a_sEndTag.length();
			}
		}



		return (sbNewText.toString());
	}

	/**
	 * Returns true if anything non-space is in the passed string
	 */
	public static boolean stringIsPopulated(String a_sStr)
	/*
	 -----------------------------------------------------------------------
	  d6		01-Nov-2004		Chris Preager		Created
	 -----------------------------------------------------------------------
	 */
	{
		if (a_sStr != null && a_sStr.trim().length() > 0)
		{
			return (true);
		}

		return (false);
	}
	
	/**
	 * Returns true if the passed string is a valid email address format, false otherwise
	 */
	public static boolean isValidEmailAddress(String a_sEmailAddress)
	/*
	 -----------------------------------------------------------------------
	  d5   30-Mar-2004    Chris Preager          Copied from James' more recent string utils
	 -----------------------------------------------------------------------
	 */
	{
		return a_sEmailAddress!=null && a_sEmailAddress.matches("([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z0-9_\\-\\.]+)");
	}
	
	/**
	 * Returns true if the passed string is a valid username format, false otherwise
	 */
	public static boolean isValidUsername(String a_sUsername)
	/*
	 -----------------------------------------------------------------------
	  d8   12-Nov-2004    Chris Preager          Simplified isValidEmailAddress
	 -----------------------------------------------------------------------
	 */
	{
		return a_sUsername!=null && a_sUsername.matches("([a-zA-Z0-9_\\-\\.]+)");
	}

	/**
	 * Returns a copy of the passed string with Javascript special characters
	 * escaped in order that its output could be read as a literal string in Javascript.
	 *
	 * Note: Backslash characters should also be escaped, but there are problems finding
	 * them in strings.
	 *
	 * @param String a_sText - the string to convert to be Javascript-literal safe
	 * @return String - the converted string
	 */
	public static String getJavascriptLiteralString(String a_sText)
	 /*
	 -----------------------------------------------------------------------
			 19-May-2003    James Home          Added version control header
	  d1   14-Oct-2003    Matt Stevenson      Imported from HRHO
	 -----------------------------------------------------------------------
	  */
	{
		String sOutput = null;

		sOutput = replaceString(a_sText,"'","\\'");

		return sOutput;
	}


	/**
	 * Returns a copy of a string with replacements made throughout
	 *
	 * @param String a_sSource - The string in which to make replacements
	 * @param String a_sToReplace - The string to replace
	 * @param String a_sReplacement - The string to use as a replacement
	 * @return String - A new string with the replacements made
	 */
	public static String replaceString(String a_sSource, String a_sToReplace, String a_sReplacement)
	 /*
	 -----------------------------------------------------------------------
			 03-Oct-2002    Martin Wilson       Created from Img Mangr.
			 19-May-2003    James Home          Fixed defect
			 29-May-2003    James Home          Another defect
	  d1   14-Oct-2003    Matt Stevenson      Imported from HRHO
	 -----------------------------------------------------------------------
	  */
	{
		String sNewString = "";
		int iIndex = 0, iOldIndex = 0 ;
		int iToReplaceLength = a_sToReplace.length();

		if (a_sSource != null)
		{
			iIndex = a_sSource.indexOf(a_sToReplace);

			while(iIndex >= 0)
			{
				sNewString += a_sSource.substring(iOldIndex,iIndex) + a_sReplacement;

				iOldIndex = iIndex+iToReplaceLength;

				iIndex = a_sSource.indexOf(a_sToReplace, iOldIndex);
			}

			sNewString += a_sSource.substring(iOldIndex,a_sSource.length());
		}

		return (sNewString);
	}

	/**
	 * Returns a copy of a string with the first replacement made
	 *
	 * @param String a_sSource - The string in which to make replacements
	 * @param String a_sToReplace - The string to replace
	 * @param String a_sReplacement - The string to use as a replacement
	 * @return String - A new string with the replacements made
	 */
	public static String replaceFirst(String a_sSource, String a_sToReplace, String a_sReplacement)
	 /*
	 -----------------------------------------------------------------------
	 d5      19-Jul-2004	   Martin Wilson       Created.
	 -----------------------------------------------------------------------
	  */
	{
		String sNewString = "";
		int iIndex = 0, iOldIndex = 0 ;
		int iToReplaceLength = a_sToReplace.length();

		if (a_sSource != null)
		{
			iIndex = a_sSource.indexOf(a_sToReplace);

			if (iIndex >= 0)
			{
				sNewString += a_sSource.substring(iOldIndex,iIndex) + a_sReplacement;

				iOldIndex = iIndex+iToReplaceLength;

				iIndex = a_sSource.indexOf(a_sToReplace, iOldIndex);
			}

			sNewString += a_sSource.substring(iOldIndex,a_sSource.length());
		}

		return (sNewString);
	}


	/**
	 * Takes a string of comma seperated values and returns an array of those
	 * values as strings
	 *
	 * @param String a_sText - the string to split into the array
	 * @param String a_sDelim - the string delimiter to use
	 * @return String[] - the array of strings from the comma seperated input
	 *
	 */
	public static String[] convertToArray(String a_sText, String a_sDelim)
	 /*
	 ------------------------------------------------------------------------
			 10-Apr-2003   Matt Stevenson    Created from Img Mangr.
			 11-Apr-2003   Matt Stevenson    Implemented
			 17-Apr-2003   Matt Stevenson    Modified to accept the delimiter to
														use
	  d1   14-Oct-2003   Matt Stevenson    Imported from HRHO
	  d9   22-Nov-2003   Chris Preager     Split in to two functions - to array and vector
	 ------------------------------------------------------------------------
	  */
	{
		//first fill a vector with the strings
		Vector vecStrings = convertToVector(a_sText, a_sDelim);

		//now convert the vector to an array of strings
		String[] aStrings = new String[vecStrings.size()];

		for (int i=0; i<aStrings.length; i++)
		{
			aStrings[i] = (String)vecStrings.elementAt(i);
		}

		return (aStrings);
	}

	/**
	 * Takes a string of comma seperated values and returns a vector of those
	 * values as strings
	 *
	 * @param String a_sText - the string to split into the array
	 * @param String a_sDelim - the string delimiter to use
	 * @return Vector - the vector of strings from the comma seperated input
	 *
	 */
	public static Vector convertToVector(String a_sText, String a_sDelim)
	 /*
	 ------------------------------------------------------------------------
	  d9   22-Nov-2003   Chris Preager		Created convertToVector from convertToArray
	 ------------------------------------------------------------------------
	  */
	{
		//first fill a vector with the strings
		Vector vecStrings = new Vector();

		if (a_sText != null)
		{
			//use a string tokenizer to split up the string
			StringTokenizer st = new StringTokenizer(a_sText, a_sDelim);

			while (st.hasMoreTokens())
			{
				vecStrings.add(st.nextToken());
			}
		}

		return (vecStrings);
	}

	/**
	 * Takes an array of longs and turns them in to a comma delimited list of
	 * numbers in a string, for use in a SQL query for example.
	 *
	 * @param String a_sText - the array of numbers to make the string from
	 * @param String a_sDelim - the string delimiter to use
	 * @return String[] - the string containing the list of numbers
	 *
	 */
	public static String convertNumbersToString(long[] a_alNumbers, String a_sDelim)
	 /*
	 ------------------------------------------------------------------------
	  d1   20-Oct-2003   Chris Preager    Created from Img Mangr
	 ------------------------------------------------------------------------
	  */
	{
		StringBuffer sb = new StringBuffer("");

		if ((a_alNumbers != null) && (a_alNumbers.length > 0))
		{
			sb.append(a_alNumbers[0]);

			if (a_alNumbers.length > 1)
			{
				for (int i = 1; i < a_alNumbers.length; i++)
				{
					sb.append(a_sDelim + a_alNumbers[i]);
				}
			}
		}

		return (sb.toString());
	}

	/**
	 * Takes an array of Strings and turns them in to a comma delimited list of
	 * strings in a string.
	 *
	 * @param String[] a_asStrings - the array of strings to make the string from
	 * @param String a_sDelim - the string delimiter to use
	 * @return String[] - the string containing the list of strings
	 *
	 */
	public static String convertStringArrayToString(String[] a_asStrings, String a_sDelim)
	 /*
	 ------------------------------------------------------------------------
	  d1   20-Oct-2003   Chris Preager    Created from Img Mangr
	 ------------------------------------------------------------------------
	  */
	{
		StringBuffer sb = new StringBuffer("");

		if ((a_asStrings != null) && (a_asStrings.length > 0))
		{
			sb.append(a_asStrings[0]);

			if (a_asStrings.length > 1)
			{
				for (int i = 1; i < a_asStrings.length; i++)
				{
					sb.append(a_sDelim + a_asStrings[i]);
				}
			}
		}

		return (sb.toString());
	}



	/**
	 * gets the given date as a string of the form dd/mm/yyyy.
	 *
	 * @param Date - the date to get.
	 */
	public static String getDateString(Date a_date)
	 /*
	 ------------------------------------------------------------------------
				30-Apr-2003       Matt Stevenson    Created from Img Mangr.
				30-Apr-2003       Martin Wilson     Fixed to handle nulls.
	  d1     14-Oct-2003       Matt Stevenson    Imported from HRHO
	 ------------------------------------------------------------------------
	  */
	{
		if (a_date == null)
		{
			return (null);
		}

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(a_date);

		//get the values from the calendar and construct into a
		//string of the form dd/mm/yyyy
		String sDay = String.valueOf(calendar.get(GregorianCalendar.DAY_OF_MONTH));
		if (sDay.length() == 1)
		{
			sDay = "0" + sDay;
		}

		//get and correct 0 start for month
		int iMonth = calendar.get(GregorianCalendar.MONTH);
		iMonth++;
		String sMonth = String.valueOf(iMonth);
		if (sMonth.length() == 1)
		{
			sMonth = "0" + sMonth;
		}

		String sYear = String.valueOf(calendar.get(GregorianCalendar.YEAR));

		return (sDay + "/" + sMonth + "/" + sYear);
	}


	/**
	 * gets the given date (dd/mm/yyyy) as a sql date string yyyy-mm-dd.
	 *
	 * @param String a_sDate - the dd/mm/yyyy date
	 * @return String - the yyyy-mm-dd date.
	 */
	public static String getSQLDateString(String a_sDate)
	 /*
	 ------------------------------------------------------------------------
				30-Apr-2003       Matt Stevenson    Created from Img Mangr.
	  d1     14-Oct-2003       Matt Stevenson    Imported from HRHO
	 ------------------------------------------------------------------------
	  */
	{
		try
		{
			String sDay = a_sDate.substring(0,2);
			String sMonth = a_sDate.substring(3,5);
			String sYear = a_sDate.substring(6,10);

			return (sYear + "-" + sMonth + "-" + sDay);
		}
		catch (StringIndexOutOfBoundsException e)
		{
			//return null to indicate a non valid date string
			return null;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	/**
	 * Returns true if the 2 Strings are equal or both null.
	 *
	 * @param String  - first string
	 * @param String - second string
	 * @return boolean
	 *
	 **/
	public static boolean equalOrNull(String a_s1, String a_s2)
	  /*
	 ------------------------------------------------------------------------
			 03-Jun-2003   Martin Wilson       Created from Img Mangr.
	  d1   14-Oct-2003   Matt Stevenson      Imported from HRHO
	 ------------------------------------------------------------------------
		*/
	{
		// Check the dates:
		if ( (a_s1 == null && a_s2 != null)
		|| (a_s1 != null && a_s2 == null))
		{
			return (false);
		}

		if (a_s1 != null && a_s2 != null)
		{
			if (!a_s1.equals(a_s2))
			{
				return (false);
			}
		}

		return (true);
	}



	/**
	 * Returns the last index of a given string from the array.
	 *
	 * @param String[]  - the array of strings to check
	 * @param String - the string to find
	 * @return int - the index of the last instance
	 *
	 **/
	public static int getLastIndexOf(String[] a_aStringArray, String a_sString)
	  /*
	 ------------------------------------------------------------------------
	  d2   28-Oct-2003   Matt Stevenson      Created from Img Mangr
	 ------------------------------------------------------------------------
		*/
	{
		int iLastIndex = 0;

		for (int i=0; i<a_aStringArray.length; i++)
		{
			if (a_aStringArray[i].equals(a_sString))
			{
				iLastIndex = i;
			}
		}

		return (iLastIndex);
	}



	/**
	 * Checks if the given string is a date of the format dd/mm/yyyy
	 *
	 * @param String - the string to check
	 * @return boolean - identifies whether the string is a date of the fomat dd/mm/yyyy
	 *
	 **/
	public static boolean stringIsDate(String a_sString)
	  /*
	 ------------------------------------------------------------------------
	  d3   29-Oct-2003   Matt Stevenson      Created from Img Mangr
	  d13	 23-Mar-2005	Matt Stevenson		  Changed to return false if input is null
	 ------------------------------------------------------------------------
		*/
	{
		if (a_sString == null)
		{
			return (false);
		}
		
		Date dtResult = null;
		boolean bDateIsValid = true;

		SimpleDateFormat sdfAlt1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfAlt2 = new SimpleDateFormat("d/M/yyyy");
		SimpleDateFormat sdfAlt3 = new SimpleDateFormat("dd/M/yyyy");
		SimpleDateFormat sdfAlt4 = new SimpleDateFormat("d/MM/yyyy");
		sdfAlt1.setLenient(false);

		try
		{
			//Will do basic check and throw ParseException if fails
			dtResult = sdfAlt1.parse(a_sString);

			//This still allows several odd dates, so compare result in acceptable formats with original string
			if(!sdfAlt1.format(dtResult).toString().equalsIgnoreCase(a_sString) &&
				!sdfAlt2.format(dtResult).toString().equalsIgnoreCase(a_sString) &&
				!sdfAlt3.format(dtResult).toString().equalsIgnoreCase(a_sString) &&
				!sdfAlt4.format(dtResult).toString().equalsIgnoreCase(a_sString) )
			{
				throw new ParseException (a_sString,0);
			}
			//still permits 15/07/20045, which is then stored in the database wrongly
			//so compare to max MySQL date (or MySQL will convert it to something odd)
			if (dtResult.after(sdfAlt1.parse("31/12/9999")))
			{
				throw new ParseException (a_sString,0);
			}

		}
		catch(ParseException pe)
		{
			bDateIsValid = false;
		}

		return (bDateIsValid);
	}

	/**
	 * Returns an array of ID's (long's) based upon an input comman delimited string.
	 *
	 * @param String a_sIds - the comma delimited category id's.
	 * @returns long[] an array of the ids
	 */
	public static long[] getIdsArray(String a_sIds) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	 d4   29-Oct-2003    Martin Wilson	Changed to allow different delimiters.
	 ------------------------------------------------------------------------
	  */
	{
		return (getIdsArray(a_sIds, ","));
	}

	/**
	 * Returns an array of ID's (long's) based upon an input string delimited by a_sDelimiter
	 *
	 * @param String a_sIds - the comma delimited  id's.
	 * @param String a_sDelimiter - the delimiter.
	 * @returns long[] an array of the  ids
	 */
	public static long[] getIdsArray(String a_sIds, String a_sDelimiter) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	 d4   29-Oct-2003    Martin Wilson	Changed to allow different delimiters.
	 ------------------------------------------------------------------------
	  */
	{
		if (a_sIds == null)
		{
			return (new long[0]);
		}

		long[] laIds = null;
		Vector vecCatIds = new Vector();

		//tokenise the string into the seperate category ids
		StringTokenizer st = new StringTokenizer(a_sIds, a_sDelimiter);
		String sToken = null;

		try
		{
			//loop through converting to Long's and adding to Vector
			while (st.hasMoreTokens())
			{
				sToken = st.nextToken();
				vecCatIds.add(new Long(Long.parseLong(sToken)));
			}
		}
		catch (NumberFormatException e)
		{
			throw new Bn2Exception("Error in StringUtil.getIdArray: one of the IDs specified in the param was not a valid number - unable to create array of Category ID's");
		}

		//create an array from the vec and return it
		laIds = new long[vecCatIds.size()];

		for (int i=0; i<laIds.length; i++)
		{
			laIds[i] = ((Long)vecCatIds.elementAt(i)).longValue();
		}

		return (laIds);
	}

	/**
	 * Returns just the contents of the <body></body> tag from a html string. Note
	 * that this will only work where the body tags match <body>...</body> - if
	 * more flexibility is required it'll have to be rewritten using reg exps.
	 *
	 * Because of pinEdit in particular, a body containing just a <br> is returned empty
	 */
	public static String getHTMLBodyOnly(String a_sSource)
	/*
	 --------------------------------------------------------------------------------
	 d2      22-Jun-2004    James Home     Created
	 d6      22-Jul-2004		Martin Wilson   Changed to use correct length for body tag
	 --------------------------------------------------------------------------------
	 */
	{
		String sBody = a_sSource;
		int iStart = 0;
		int iEnd = 0;


		iStart = a_sSource.indexOf("<BODY");

		if(iStart<0)
		{
			iStart = a_sSource.indexOf("<body");
		}

		if(iStart>0)
		{
			iStart+=5;

			iEnd = a_sSource.indexOf("</BODY>");

			if(iEnd<0)
			{
				iEnd = a_sSource.indexOf("</body>");
			}

			if(iEnd<0)
			{
				sBody = a_sSource.substring(iStart).trim();
			}

			sBody = a_sSource.substring(iStart,iEnd).trim();

			// Now clear up the remainder of the opening body tag
			sBody = sBody.replaceFirst("[^>]*?>","");

			// Remove a single br tag if one exists
			if(sBody.equalsIgnoreCase("<br>"))
			{
				sBody = "";
			}
		}

		return sBody;
	}

	/**
	 * Returns a copy of the source html string with taget="_blank" replacing any existing
	 * or missing target attributes in the <a> tags
	 */
	public static String getHTMLBlankTargetsOnly(String a_sSourceHTML)
	/*
	 --------------------------------------------------------------------------------
	 d3      23-Jun-2004    James Home     Created
	 --------------------------------------------------------------------------------
	 */
	{
		String sNewHTML = null;

		// Get rid of any existing target
		sNewHTML = a_sSourceHTML.replaceAll("target=[\"'_a-zA-Z]*>",">");
		sNewHTML = sNewHTML.replaceAll("target=[\"'_a-zA-Z]* "," ");

		// Add a new target attribute
		sNewHTML = sNewHTML.replaceAll("<[aA] ","<a target=\"_blank\" ");

		return sNewHTML;
	}
	
	/**
	 * Adds a parameter to the parameter string passed in the first oparameter
	 *
	 * @param String a_sParamName - the name of the parameter
	 * @param String a_sValue - the value
	 **/
	public static String addUrlParameter(String a_sParams, String a_sParamName, String a_sValue)
	/*
	 ------------------------------------------------------------------------     
	 d1   15-Sep-2004     Martin Wilson     Created.
	 ------------------------------------------------------------------------
	 */
	{
		if(a_sParamName != null)
		{
			if(a_sParams!=null && a_sParams.length() > 0)
			{
				a_sParams += "&";
			}
			else
			{
				a_sParams = "?";
			}

			// Avoid npe:
			if(a_sValue == null)
			{
				a_sValue = "";
			}

			try
			{
				// Add the param:
				a_sParams += a_sParamName + "=" + URLEncoder.encode(a_sValue,"UTF-8");
			}
			catch(UnsupportedEncodingException uee)
			{
				GlobalApplication.getInstance().getLogger().error("StringUtil.addUrlParameter : Error whilst encoding url parameter : " + uee);
			}
		}

		return a_sParams;
	}

	/**
	 * Replaces any carriage returns with the HTML for a new line
	 * 
	 * @param a_sSource
	 * @return the newly formatted String
	 */
	public static String formatNewlineForHTML(String a_sSource)
	/*
	 --------------------------------------------------------------------------------
	 d10		07-Jan-2005		 Martin Wilson	  Added
	 --------------------------------------------------------------------------------
	 */
	{
		String sNew = null;
	
		if (a_sSource != null)
		{
			sNew = a_sSource.replaceAll("\r\n", "<br>");
			sNew = sNew.replaceAll("\r", "<br>");
			sNew = sNew.replaceAll("\n", "<br>");	
		}

		return (sNew);
	}
	
	/**
	 * Returns the first matching UK post code from the input string
	 * 
	 * @param a_sInput
	 * @return
	 */	
	public static String getFirstPostcode(String a_sInput, boolean a_bFirstPartOnly)
	/*
	---------------------------------------------------------------
	d12		11-Jan-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sPostcode = null;	
		
		a_sInput = " " + a_sInput.toLowerCase() + " ";			
		
		// Try and get a full postcode
		if(!a_bFirstPartOnly && 
			a_sInput.matches("[a-z :\\-,\\.\\(\\)]*? ([a-z]{1,2}[0-9]{1,2}|[a-z]{3}|[a-z]{1,2}[0-9][a-z]) [0-9][a-z]{2} [0-9a-z :\\-,\\.\\(\\)]*"))
		{
			String[] asTmp = a_sInput.split(" ([a-z]{1,2}[0-9]{1,2}|[a-z]{3}|[a-z]{1,2}[0-9][a-z]) [0-9][a-z]{2} ",2);
			
			sPostcode = a_sInput.substring(asTmp[0].length(), a_sInput.length() - asTmp[1].length()).trim();
		}
		// If we cannot get a full postcode, try to get the first part only
		else if(a_sInput.matches("[a-z :\\-,\\.\\(\\)]*? ([a-z]{1,2}[0-9]{1,2}|[a-z]{3}|[a-z]{1,2}[0-9][a-z]) [0-9a-z :\\-,\\.\\(\\)]*"))
		{
			String[] asTmp = a_sInput.split(" ([a-z]{1,2}[0-9]{1,2}|[a-z]{3}|[a-z]{1,2}[0-9][a-z]) ",2);
			
			sPostcode = a_sInput.substring(asTmp[0].length(), a_sInput.length() - asTmp[1].length()).trim();
		}
		
		return sPostcode;
	}
	
	/**
	 * Returns a phone number without spaces or hyphens, and with + converted to 00
	 * 
	 * @param a_sPhoneNumber
	 * @return
	 */
	public static String getCleanPhoneNumber(String a_sPhoneNumber)
	/*
	---------------------------------------------------------------
	d12		12-Jan-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		a_sPhoneNumber = a_sPhoneNumber.replaceAll(" ", "");
		a_sPhoneNumber = a_sPhoneNumber.replaceAll("-", "");
		a_sPhoneNumber = a_sPhoneNumber.replaceFirst("\\+", "00");
		a_sPhoneNumber = a_sPhoneNumber.replaceAll("\\+", "");
		a_sPhoneNumber = a_sPhoneNumber.replaceAll("\\(0\\)", "");
		
		return a_sPhoneNumber;
	}
	
	
	public static boolean containsAtLeastOneKeyword (String a_sKeywordString, String a_sStringToCheck, String a_sDelimiter)
	/*
	---------------------------------------------------------------
	 d14	24-Oct-2007		Matt Stevenson	Created
	-----------------------------------------------------------------
	*/
	{
		String[] aKeywords = (a_sKeywordString.toLowerCase()).split(a_sDelimiter);
		
		if (aKeywords != null && a_sStringToCheck != null)
		{
			for (int i=0; i<aKeywords.length; i++)
			{
				if (a_sStringToCheck.toLowerCase().indexOf(aKeywords[i]) >= 0)
				{
					return (true);
				}
			}
		}
		
		return (false);
	}
	
	
	public static String getEmptyStringIfNull(String a_sValue)
	{
		if (a_sValue == null)
		{
			return ("");
		}
		
		return (a_sValue);
	}
	
	/**
	 * Returns true if string is a valid integer
	 */
	public static boolean stringIsInteger(String a_sStr)	
	{
		try
		{
			Integer.parseInt(a_sStr);
			return (true);
		}
		catch (NumberFormatException ne)
		{
			return (false);
		}
	}
	
	
}

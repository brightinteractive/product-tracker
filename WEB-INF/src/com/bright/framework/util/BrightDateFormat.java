package com.bright.framework.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Bright Interactive, image-bank
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * BrightDateFormat.java
 *
 * Contains the BrightDateFormat class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	Oct 8, 2004		James Home		Created
 --------------------------------------------------------------------------------
 */

/**
 * This is a SimpleDateFormat subclass that behaves as SimpleDateFormat, except when the
 * new property Validate4YearDate is set to true, in which case the parse() method throws
 * a parse exception if the date string passed does not actually have a 4 digit date. Eg,
 * parse("12/08/204") will throw an exception rather than returning a Date object representing
 * 12/08/0204, and likewise parse("12/08/20004") will throw an exception despite the fact
 * that one day this will be a valid date. 
 * 
 * @author Bright Interactive
 * @version d1
 */
public class BrightDateFormat extends SimpleDateFormat
{
	boolean m_bValidate4YearDate = false;
	SimpleDateFormat m_comparisonFormat = null;
	
	/**
	 * Default constructor
	 */
	public BrightDateFormat()
	{
		super();
		m_comparisonFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param a_sPattern
	 */
	public BrightDateFormat(String a_sPattern)
	{
		super(a_sPattern);
		m_comparisonFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param a_sPattern
	 * @param a_sFormatSymbols
	 */
	public BrightDateFormat(String a_sPattern, DateFormatSymbols a_sFormatSymbols)
	{
		super(a_sPattern, a_sFormatSymbols);
		m_comparisonFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * @param a_sPattern
	 * @param a_sLocale
	 */
	public BrightDateFormat(String a_sPattern, Locale a_sLocale)
	{
		super(a_sPattern, a_sLocale);
		m_comparisonFormat = new SimpleDateFormat(a_sPattern);
	}

	
	/**
	 * Parses a date to the given format. Part of the parsing process in this instance
	 * involves checking that the year field is within the given 
	 * 
	 * @see java.text.DateFormat#parse(java.lang.String)
	 * 
	 * @param a_sSource
	 * @return
	 * @throws java.text.ParseException
	 */
	/*
	 ---------------------------------------------------------------
	 d1	Oct 8, 2004		James Home		Created 
	 -----------------------------------------------------------------
	 */
	public Date parse(String a_sSource)
	throws ParseException
	{
		Date dtResult = super.parse(a_sSource);
		String sPattern = toPattern();
		
		if(sPattern!=null && sPattern.indexOf("yyyy")>=0 && m_bValidate4YearDate)
		{
			if(dtResult!=null && (dtResult.before(m_comparisonFormat.parse("01/01/1000")) ||
										 dtResult.after(m_comparisonFormat.parse("31/12/9999"))))
			{
				throw new ParseException("The date being parsed does not have a 4 digit year.",0);
			}
		}
		
		return dtResult;
	}
	
	/** @return Returns the validate4YearDate. */
	public boolean getValidate4YearDate()
	{
		return m_bValidate4YearDate;
	}

	/** @param a_sValidate4YearDate The validate4YearDate to set. */
	public void setValidate4YearDate(boolean a_sValidate4YearDate)
	{
		m_bValidate4YearDate = a_sValidate4YearDate;
	}
	
}

package com.bright.framework.util;

/**
 * Bright Interactive, ETA
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * TextFileChanger.java
 *
 * Contains the TextFileChanger class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
     07-Apr-2004     Martin Wilson	Created.
     07-May-2004     James Home     Added char encoding param to loadFromFile.
--------------------------------------------------------------------------------
d1   10-Jun-2004     Chris Preager  Copied from Debt Free Day
--------------------------------------------------------------------------------
*/

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bn2web.common.exception.Bn2Exception;

/**
 * Used to make changes to the contents of a String or text file, for example to change image paths.
 *
 * To use:
 * 1) Create the object
 * 2) Call loadFromFile or setContent to set the contents
 * 3) Call setPattern to set the regular expression to use
 * 4) Loop while matchNext() returns true - each time you have a match on a String that has not been seen before.
 *      - within the loop: call getCurrentMatch() to see the String that matched
 *                         call addReplacementForCurrentMatch(XX) to specify that you will want ALL OCCURRENCES
 *                           of that string to be replaced with XX (once matching is finished)
 * 5) Call makeReplacements to go through and replace all the matches with the Strings you added
 * 6) Call getContents or writeToFile to output the result.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class TextPatternReplacer
{
    private String m_sContents = null;
    private Matcher m_Matcher = null;
    private Vector m_vecReplacements = new Vector();

    // Inner class to store matches and their replacements:
    class Replacement
    {
        protected String sMatch = null;
        protected String sReplacement = null;
    }

    /*
     * Loads the file.
     *
     *@param String a_sFullFilePath - the full path to the HTML file.
     *@throws Bn2Exception - on error.
     */      
    public void loadFromFile(String a_sFullFilePath) throws IOException
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */       
    {
        // Read in the file:
        StringBuffer sb = FileUtil.readIntoStringBuffer(a_sFullFilePath);
        m_sContents = sb.toString();
    }   
    
    
	/*
     * Loads the file.
     *
     *@param String a_sFullFilePath - the full path to the HTML file.
     *@param String a_sCharEncoding - the character encoding string, null=default.
     *@throws Bn2Exception - on error.
     */
    public void loadFromFile(String a_sFullFilePath, String a_sCharEncoding) throws IOException
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
     d2   07-May-2004  James Home         Added char encoding param.
    ------------------------------------------------------------------------
    */
    {
        // Read in the file:
        StringBuffer sb = FileUtil.readIntoStringBuffer(a_sFullFilePath, a_sCharEncoding);
        m_sContents = sb.toString();
    }

    /*
     * Sets the contents.
     *
     *@param String a_sContents - the contents.
     */
    public void setContents(String a_sContents)
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        m_sContents = a_sContents;
    }

    /*
     * Sets the regular expression to use in the matching.
     *
     *@param String a_sPattern - the regular expression.
     */
    public void setPattern(String a_sPattern) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        if (m_sContents == null)
        {
            throw new Bn2Exception("You must set the contents in TextFileChanger before setting the pattern");
        }

        Pattern pattern = Pattern.compile(a_sPattern);
        m_Matcher = pattern.matcher(m_sContents);
    }

    /*
     * Attempts to find the next subsequence of the contents that matches the pattern.
     * If there is a match, returns true.
     *
     *@param boolean - true if we found a match, false if we have finished.
     */
    public boolean matchNext() throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        if (m_Matcher == null)
        {
            throw new Bn2Exception("You must set the pattern in TextFileChanger before calling matchNext");
        }

        boolean bFoundNewMatch = false;
        boolean bReachedEnd = false;

        // Loop until we find a match that is new (or we don't find any matches at all):
        while ( (!bFoundNewMatch) && (!bReachedEnd))
        {
            // Get the next match:
            if (m_Matcher.find())
            {
                String sNextMatch = m_Matcher.group();

                // See if this is a new string:
                bFoundNewMatch = !alreadyAddedMatch(sNextMatch);

                // Otherwise, try the next.
            }
            else
            {
                // We've reached the end:
                bReachedEnd = true;
            }
        }

        return (bFoundNewMatch);
    }

    /*
     * Returns the current match.
     *
     *@return String - the current match.
     */
    public String getCurrentMatch() throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        if (m_Matcher == null)
        {
            throw new Bn2Exception("You must set the pattern and then call matchNext before calling getCurrentMatchString");
        }

        String sCurrentMatch = null;

        try
        {
            sCurrentMatch = m_Matcher.group();
        }
        catch (IllegalStateException ise)
        {
            throw new Bn2Exception("Cannot call getCurrentMatch - either you have not called matchNext or matchNext return false");
        }

        return (sCurrentMatch);
    }

    /*
     * Sets a string that will be used to replace all occurences of the current match string.
     *
     *@param String a_sReplacement.
     */
    public void addReplacementForCurrentMatch(String a_sReplacement) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        try
        {
            // Create the object to store the current match and its replacement:
            Replacement replacement = new Replacement();
            replacement.sMatch = m_Matcher.group();
            replacement.sReplacement = a_sReplacement;

            // Add to the vector:
            m_vecReplacements.add(replacement);
        }
        catch (IllegalStateException ise)
        {
            throw new Bn2Exception("Cannot call addReplacementForCurrentMatch - either you have not called matchNext or matchNext return false");
        }
    }

    /*
     * Makes all the replacements added using addReplacementForCurrentMatch.
     *
     * This changes the contents.
     *
     */
    public void makeReplacements()
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        // Go through all the replacements:
        for (int i=0; i<m_vecReplacements.size(); i++)
        {
            Replacement replacement = (Replacement)m_vecReplacements.get(i);

            // Replace this string:
            m_sContents = StringUtil.replaceString(m_sContents, replacement.sMatch, replacement.sReplacement);
        }

    }

    /*
     * Returns the contents as a String.
     *
     *@param String a_sReplacement.
     */
    public String getContents()
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        return (m_sContents);
    }

    /*
     * Sets a string that will be used to replace all occurences of the current match string.
     *
     *@param String a_sReplacement.
     */
    public void saveToFile(String a_sFilepath) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        try
        {
            FileWriter fwContents = new FileWriter(a_sFilepath);
            fwContents.write(m_sContents);
            fwContents.close();
        }
        catch (IOException ioe)
        {
            throw new Bn2Exception("Error in TextFileChanger.saveToFile:", ioe);
        }
    }

    /*
     * Returns true if a_sMatch has already been matched and a replacement string set.
     *
     *@param String a_sMatch.
     */
    private boolean alreadyAddedMatch(String a_sMatch)
    /*
    ------------------------------------------------------------------------
     d1   07-Apr-2004  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
    {
        boolean bAlready = false;

        for (int i=0; i<m_vecReplacements.size(); i++)
        {
            Replacement replacement = (Replacement)m_vecReplacements.get(i);
            if (replacement.sMatch.equals(a_sMatch))
            {
                bAlready = true;
                break;
            }
        }

        return (bAlready);
    }
}
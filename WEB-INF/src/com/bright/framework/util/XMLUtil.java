package com.bright.framework.util;

/**
 * Bright Interactive
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * XMLUtil.java
 *
 * Contains the XMLUtil class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   23-Jul-2002     Martin Wilson	Created.
d2   09-Aug-2002     Martin Wilson	Added replaceFindOutMoreTags.
d3   09-Aug-2002     Martin Wilson	Merged with XMLManager.
d4   09-Dec-2002     Martin Wilson  Changed getRSCImage to set image height & width for jpegs.
d5   27-Oct-2003     James Home     Added getTextFromChildren
d6   20-Nov-2003     James Home     Added getXMLElementContentForHTML
--------------------------------------------------------------------------------
 */

import java.io.StringWriter;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.bright.framework.constant.FrameworkConstants;

/**
 * Static methods for manipulating XML
 *
 * @author  bn2web
 * @version d1
 */
public class XMLUtil implements FrameworkConstants
{
   
   /**
    * Returns the Element's content as a string, not including its tag, and attributes.
    *
    * @param Element a_element - the element.
    * @returns String - the element's content.
    */
   public static String getXMLElementContent(Element a_element)
    /*
    ------------------------------------------------------------------------
     d1   23-July-2002  Martin Wilson		Created.
    ------------------------------------------------------------------------
     */
   {
      String sFormattedContent = null;
      
      if (a_element == null)
      {
         return (null);
      }
      
      // Create a string writer to receive the content:
      StringWriter swWriter = new StringWriter();
      
      XMLOutputter outputter = new XMLOutputter();
      try
      {
         outputter.outputElementContent(a_element, swWriter);
         sFormattedContent = swWriter.toString();
      }
      catch (java.io.IOException ioe)
      {
         sFormattedContent = null;
      }
      
      
      return (sFormattedContent);
   }
   
   /**
    * Returns the Element's content as a string, not including its tag, and attributes.
    * The contents are filtered to replace HTML entities for tag parenthesis and string
    * delimiters with the HTML characters they escape so that the tags can be output on
    * a jsp page.
    *
    * @param Element a_element - the element.
    * @returns String - the element's content.
    */
   public static String getXMLElementContentForHTML(Element a_element)
    /*
    ------------------------------------------------------------------------
     d6   20-Nov-2003  James Home		Created.
    ------------------------------------------------------------------------
     */
   {
      
      
      String sFormattedContent = null;
      
      sFormattedContent = getXMLElementContent(a_element);
      
      if (a_element != null)
      {
         sFormattedContent = sFormattedContent.replaceAll("\\n", "<br>");
         sFormattedContent = sFormattedContent.replaceAll("&lt;", "<");
         sFormattedContent = sFormattedContent.replaceAll("&gt;", ">");
         sFormattedContent = sFormattedContent.replaceAll("&quot;", "\"");
         sFormattedContent = sFormattedContent.replaceAll("&apos;", "'");
         sFormattedContent = sFormattedContent.replaceAll("&amp;", "&");
      }
            
      return (sFormattedContent);
   }
   
   /**
    * Replaces a substring in a_sString.
    *
    * @param String a_sString the string
    * @param String a_sSubStringToReplace the substring to replace.
    * @param String a_sReplacement the replacement.
    * @returns String - the reformatted text.
    */
   public static String replaceString(String a_sString ,String a_sSubStringToReplace, String a_sReplacement )
    /*
    ------------------------------------------------------------------------
    d2 03-Oct-2002         Martin Wilson       Created.
    ------------------------------------------------------------------------
     */
   {
      StringBuffer sbNewText = new StringBuffer("");;
      int iStartPos = 0;
      int iStartSearchPos = 0;
      
      // Avoid npe:
      if (a_sString == null)
      {
         return (a_sString);
      }
      
      while (iStartPos >= 0)
      {
         // Get the position of the next start tag:
         iStartPos = a_sString.indexOf(a_sSubStringToReplace, iStartSearchPos);
         
         if (iStartPos < 0)
         {
            // No more tags. Add rest of string:
            sbNewText.append(a_sString.substring(iStartSearchPos, a_sString.length()));
         }
         else
         {
            // Add what we've got so far, i.e. up to the tag:
            sbNewText.append(a_sString.substring(iStartSearchPos, iStartPos));
            
            // Add the replacement text:
            sbNewText.append(a_sReplacement);
            
            // Find where to start the next search:
            iStartSearchPos = iStartPos + a_sSubStringToReplace.length();
            
         }
      }
      
      return (sbNewText.toString());
   }
   
   /**
    * Returns the trimmed text from all the children of a_xmlElement named
    * a_sChlidElementName
    */
   public static Vector getTextFromChildren(Element a_xmlElement, String a_sChlidElementName)
   /*
    ------------------------------------------------------------------------
    d5 27-Oct-2003         James Home       Created.
    ------------------------------------------------------------------------
    */
   {
      Vector vChildText = null;
      String sText = null;
      Element xmlChild = null;
      
      // Get the recipients
      List lstChildren = a_xmlElement.getChildren(a_sChlidElementName);
      ListIterator lstiChildren = (ListIterator)(lstChildren.iterator());
      
      vChildText = new Vector(lstChildren.size());
      
      while(lstiChildren.hasNext())
      {
         xmlChild = (Element)lstiChildren.next();
         
         if(xmlChild!=null)
         {
            sText = xmlChild.getTextTrim();
            
            if(sText!=null && sText.length()>0)
            {
               vChildText.add(sText);
            }
         }
      }
      
      return vChildText;
   }
}

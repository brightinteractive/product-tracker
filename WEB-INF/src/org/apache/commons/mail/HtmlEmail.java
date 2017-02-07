/**
 * Bright Interactive
 *
 * NOTE: THIS FILE HAS BEEN CHANGED BY BRIGHT INTERACTIVE.
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * HtmlEmail.java
 *
 * Contains the HtmlEmail class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   06-Apr-2004    Martin Wilson        	Created from Apache code.
d2   07-Apr-2004    Martin Wilson        	Added embed method to use html safe random id.
d3   13-Apr-2004    Martin Wilson        	Changed embed to use <> in content ids for embedded images.
d4   17-May-2004    James Home           	Updated to rectify problem with multiple text bodies being
                                         	added during subsequent sends.
d5   27-Oct-2006    Martin Wilson			Changed send to only set up 'body parts' if not already done                                           
--------------------------------------------------------------------------------
 */


/*
 * Copyright 2001-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.mail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * An HTML multipart email.
 *
 * <p>This class is used to send HTML formatted email.  A text message
 * can also be set for HTML unaware email clients, such as text-based
 * email clients.
 *
 * <p>This class also inherits from MultiPartEmail, so it is easy to
 * add attachents to the email.
 *
 * <p>To send an email in HTML, one should create a HtmlEmail, then
 * use the setFrom, addTo, etc. methods.  The HTML content can be set
 * with the setHtmlMsg method.  The alternate text content can be set
 * with setTextMsg.
 *
 * <p>Either the text or HTML can be omitted, in which case the "main"
 * part of the multipart becomes whichever is supplied rather than a
 * multipart/alternative.
 *
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @author <a href="mailto:sean@informage.net">Sean Legassick</a>
 * @version $Id: HtmlEmail.java,v 1.1 2007-10-29 16:52:14 matt Exp $
 */
public class HtmlEmail extends MultiPartEmail
{
   /**
    * Text part of the message.  This will be used as alternative text if
    * the email client does not support HTML messages.
    */
   private String text;
   
   /** Html part of the message */
   private String html;
   
   /** Embeded images */
   private List inlineImages = new ArrayList();
   
   private boolean m_bAlreadyPrepared = false;
   
   /**
    * Set the text content.
    *
    * @param text A String.
    * @return An HtmlEmail.
    */
   public HtmlEmail setTextMsg(String text)
   {
      this.text = text;
      return this;
   }
   
   /**
    * Set the HTML content.
    *
    * @param html A String.
    * @return An HtmlEmail.
    */
   public HtmlEmail setHtmlMsg(String html)
   {
      this.html = html;
      return this;
   }
   
   /**
    * Set the message.
    *
    * <p>This method overrides the MultiPartEmail setMsg() method in
    * order to send an HTML message instead of a full text message in
    * the mail body. The message is formatted in HTML for the HTML
    * part of the message, it is let as is in the alternate text
    * part.
    *
    * @param msg A String.
    * @return An Email.
    */
   public Email setMsg(String msg)
   {
      setTextMsg(msg);
      
      setHtmlMsg(new StringBuffer()
      .append("<html><body><pre>")
      .append(msg)
      .append("</pre></body></html>")
      .toString());
      
      return this;
   }
   
   
   /**
    * Embeds an URL in the HTML.
    *
    * <p>This method allows to embed a file located by an URL into
    * the mail body.  It allows, for instance, to add inline images
    * to the email.  Inline files may be referenced with a
    * <code>cid:xxxxxx</code> URL, where xxxxxx is the Content-ID
    * returned by the embed function.
    *
    * @param String a_sFilename - the full path of the file.
    * @param String name - the name to give the attachment
    * @return String - the cid of the embedded image
    * @throws MessagingException - on error.
    */
   public String embed(String a_sFilepath, String a_sName) throws MessagingException
    /*
    ------------------------------------------------------------------------
    d2  07-Apr-2004   Martin Wilson    Created
    d3  13-Apr-2004   Martin Wilson    Changed to use <> in content ids for embedded images.
    ------------------------------------------------------------------------
     */
   {
      // Create a URL:
      URL fileUrl = null;
      
      try
      {
         fileUrl = new URL("file:" + a_sFilepath);
      }
      catch (java.net.MalformedURLException me)
      {
         throw new MessagingException("Could not attach file: " + a_sFilepath + ". Error is: " + me.toString());
      }
      
      MimeBodyPart mbp = new MimeBodyPart();
      
      mbp.setDataHandler(new DataHandler(new URLDataSource(fileUrl)));
      mbp.setFileName(a_sName);
      mbp.setDisposition("inline");
      
      // Create a random id from safe strings:
      String cid = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
      mbp.addHeader("Content-ID", "<"+cid+">");
      
      inlineImages.add(mbp);
      return (cid);
   }
   
   /**
    * Does the work of actually sending the email.
    *
    * @exception MessagingException if there was an error.
    */
   public void send() throws MessagingException
   {
   	// See if we have already prepared the message:
   	if (!m_bAlreadyPrepared)
   	{
   	
	      MimeMultipart container = this.getContainer();
	      container.setSubType("related");
	      
	      BodyPart msgText = null;
	      BodyPart msgHtml = null;
	      
	      // Remove any body parts previously added
	      /*
	      for(int i=container.getCount()-1; i>=0; i--)
	      {
	         if(container.getBodyPart(i) != this.getPrimaryBodyPart())
	         {
	            container.removeBodyPart(i);
	         }
	      }
	      */
	      if (StringUtils.isNotEmpty(html))
	      {
	         msgHtml = this.getPrimaryBodyPart();
	         
	         if (charset != null)
	         {
	            msgHtml.setContent(html, TEXT_HTML + ";charset=" + charset);
	         }
	         else
	         {
	            msgHtml.setContent(html, TEXT_HTML);
	         }
	         
	         // Add the images
	         for (Iterator iter = inlineImages.iterator(); iter.hasNext();)
	         {
	            BodyPart image = (BodyPart)iter.next();
	            
	            container.addBodyPart(image);
	         }
	      }
	      
	      if (StringUtils.isNotEmpty(text))
	      {
	         String sContentType = TEXT_PLAIN;
	         
	         if (charset != null)
	         {
	            sContentType += ";charset=" + charset;
	         }
	         
	         // if the html part of the message was null, then the text part
	         // will become the primary body part
	         if (msgHtml == null)
	         {
	            msgText = getPrimaryBodyPart();
	            msgText.setContent(text, sContentType);
	         }
	         else
	         {
	            msgText = new MimeBodyPart();
	            msgText.setContent(text, sContentType);
	            container.addBodyPart(msgText);
	         }
	      }
	      
	      m_bAlreadyPrepared = true;
   	}
   	
      super.send();
   }
   
   /**
    * Validates that the supplied string is neither <code>null</code>
    * nor the empty string.
    *
    * @param foo The text to check.
    * @return Whether valid.
    * @deprecated use StringUtils.isNotEmpty instead
    */
   public static final boolean isValid(String foo)
   {
      return StringUtils.isNotEmpty(foo);
   }
}

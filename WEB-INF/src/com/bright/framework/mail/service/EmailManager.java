package com.bright.framework.mail.service;


/**
 * bn2web, framework
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * EmailManager.java
 *
 *
 */
/*
Ver  Date	        Who		    Comments
--------------------------------------------------------------------------------
d1  29-Apr-2003         Matt Stevenson    Created.
d2  30-Apr-2003         Matt Stevenson    Added missing exception check
                                          and changed to accept control parameter
                                          for where to send the email to
d3  22-Oct-2003         James Home        Added subject param version of sendFeedbackEmail
d4  27-Oct-2003         James Home        Added sendTemplatedEmail
d5  03-Mar-2004         James Home        Added getTemplatedEmailPreview
d6  29-Mar-2004         Martin Wilson     Added support for attachments
d7  05-Apr-2004         Martin Wilson     Added support for HTML emails
d8  08-Apr-2004         Martin Wilson     Added sendEmail
d9	 22-Jul-2004			Matt Stevenson		Added sendHtmlEmail method
d10	 26-Jul-2010			Kevin Bennett		Added smtpEmailPort option
--------------------------------------------------------------------------------
 */

import java.io.IOException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bn2web.common.xml.EmptyEntityResolver;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.mail.bean.BatchEmail;
import com.bright.framework.mail.bean.BatchRecipient;
import com.bright.framework.mail.bean.EmailPreview;
import com.bright.framework.util.StringUtil;
import com.bright.framework.util.TextPatternReplacer;

/**
 * EmailManager. Provides functionality to send emails, either constructed just from 
 * input parameters, or from parameters plus a template XMl file.
 *
 * Note: ++todo: The templates should really be cached so that their files don't 
 * have to be read and parsed each time an email is sent. This will mean that the
 * templates will have to be re-loaded when a template file is changed, however.
 *
 * @author  bn2web
 * @version d1
 */
public class EmailManager extends Bn2Manager implements FrameworkConstants
{
   
   /*
    * See sendTemplatedEmail(HashMap a_hmParams, String[] a_asAttachments)
    * for description.
    *
    * Sends emails without attachments.
    *
    *@param HashMap a_hmParams - the parameters
    *@throws Bn2Exception - on error
    */   
   public void sendTemplatedEmail(HashMap a_hmParams) throws Bn2Exception
   /*
    ------------------------------------------------------------------------
    d6  29-Mar-2004         Martin Wilson     Added for backward compatibility.
    ------------------------------------------------------------------------
    */
   {
        sendTemplatedEmail(a_hmParams, null);
   }
   
   /**
    * Constructs and sends one or more emails using templates found in a template
    * XML file. The name of the template specification is supplied as one of the
    * parameters contained in the HashMap a_hmParams - the parameter name is
    * defined by the constant k_sTemplateParam. This is turned into a filename
    * using app settings, and this file is read to get the templates fields and
    * addresses for sending the email(s). A sample XML file is provided in a comment
    * in this file below the class definition.
    *
    * In the template, parameter names can be given, enclosed in parenthesis
    * substrings k_sTemplateVariableStart and k_sTemplateVariableStart. Where
    * one of these parameter names is found in the template it will be replaced
    * with the value of the parameter in a_hmParams. Note that in the address
    * fields the field text should contain nothing but the parameter name and
    * its parenthesis.
    *
    * Email addresses supplied in the address fields of the template, and as 
    * parameter values, are assumed to be correct enough to make it through JavaMail.
    * If supplying address parameters directly from a publis html form the address
    * validation will have to be performed before reaching this method.
    *
    *@param HashMap a_hmParams - the parameters
    *@param String[]  - an array containing full paths to the attachments
    *@throws Bn2Exception - on error
    *
    */
   public void sendTemplatedEmail(HashMap a_hmParams, EmailAttachment[] a_attachments)
   throws Bn2Exception
   /*
    ------------------------------------------------------------------------
     d3  22-Oct-2003   James Home       Created
     d6  29-Mar-2004   Martin Wilson    Added support for attachments
     d7  05-Apr-2004   Martin Wilson    Added support for HTML emails
    ------------------------------------------------------------------------
    */
   {
      String sTemplateFilepath = null;
      String sBody = null;
      String sHtmlBody = null;
      String sHtmlFilePath = null;
      String sSubject = null;
      Set setParams = null;
      Iterator itParams = null;
      String sParam = null;
      String sParamValue = null;
      String sTemplateName = null;
      String sFromAddress = null;
      Vector vTO = null;
      Vector vCC = null;
      Vector vBCC = null;
      
      // Get the name of the template
      sTemplateName = (String)a_hmParams.get(k_sTemplateParam);
      
      // Get the email template directory
      String sTemplateDirectory = FrameworkSettings.getEmailTemplateDirectory();
      
      // Fully qualify the template directory  if necessary
      if(FrameworkSettings.getUseRelativeDirectories())
      {
         sTemplateDirectory = FrameworkSettings.getApplicationPath() +
                             "/" + FrameworkSettings.getCmsFileStoreRoot() +
                             FrameworkSettings.getEmailTemplateDirectory();
      }
      
      // Construct the filepath
      sTemplateFilepath = sTemplateDirectory + "/" + sTemplateName + k_sEmailTemplateFileSuffix;
      
      //-- Now read the XMl template file and build the fields we need --
      
      SAXBuilder saxBuilder = new SAXBuilder();
      
      // Don't worry about the dtd:
      saxBuilder.setDTDHandler(null);
      
      // Create an empty resolver as we do not need to resolve against the DTD:
      EmptyEntityResolver emptyResolver = new EmptyEntityResolver();
      saxBuilder.setEntityResolver(emptyResolver);
      
      try
      {
         // Read from the file:
         Document document = saxBuilder.build(sTemplateFilepath);
         
         Element root = document.getRootElement();
         
         // Get the email templates
         List lstTemplates = root.getChildren("email-template");
         ListIterator lstiTemplates = (ListIterator)(lstTemplates.iterator());
         
         if (lstTemplates.size() == 0)
         {
            m_logger.error("Email template file " + sTemplateFilepath + " has no template elements.");
            throw new Bn2Exception("Email template file " + sTemplateFilepath + " has no template elements.");
         }
         
         // Get the parameter names as a set
         setParams = a_hmParams.keySet();
      
         // Iterate through the templates, sending an email for each
         for (int iTemplate = 0; iTemplate <  lstTemplates.size(); iTemplate++)
         {
            Element xmlTemplate = (Element)(lstiTemplates.next());
            
            // Get the subject line
            Element subject = xmlTemplate.getChild("subject");
            if(subject!=null)
            {
               sSubject = subject.getTextTrim();
            }
            
            // Get the body
            Element body = xmlTemplate.getChild("body");
            if(body!=null)
            {
               sBody = body.getTextTrim();
            }
            
            // Get the HTML file:
            Element htmlFile = xmlTemplate.getChild("htmlbodyfile");
            if(htmlFile != null)
            {
               sHtmlFilePath = htmlFile.getTextTrim();
            }            
            
            // Get the address info
            Element xmlAddresses = xmlTemplate.getChild("addresses");
            vTO = com.bright.framework.util.XMLUtil.getTextFromChildren(xmlAddresses,"to");
            vCC = com.bright.framework.util.XMLUtil.getTextFromChildren(xmlAddresses,"cc");
            vBCC = com.bright.framework.util.XMLUtil.getTextFromChildren(xmlAddresses,"bcc");
            sFromAddress = xmlAddresses.getChild("from").getTextTrim();
            
            try
            {                        
                // Create an HTMLEmail object:
                HtmlEmail message = new HtmlEmail();
                message.setHostName(FrameworkSettings.getEmailSMTP());
                String sPort = FrameworkSettings.getEmailSMTPPort();
        		if(StringUtil.stringIsInteger(sPort))
        		{
        			message.setPort(Integer.parseInt(sPort));
        		}
           
                // Get the html body text from the html file (if applicable):
                if (sHtmlFilePath != null)
                {                    
                    // Get the html and attach any image files:
                    sHtmlBody = createHtmlText(message, sTemplateDirectory, sHtmlFilePath);
                }                
                        
                // Get an interator for the parameter names
                itParams = setParams.iterator();

                // Iterate over the parameters filling in the templated fields (subject, body 
                // and addresses)
                while(itParams.hasNext())
                {
                   sParam = (String)itParams.next();          
                   sParamValue = (String)a_hmParams.get(sParam);

                   // Add the variable delimiters to the param name
                   sParam = k_sTemplateVariableStart + sParam + k_sTemplateVariableEnd;                              

                   sSubject = StringUtil.replaceString(sSubject, sParam, sParamValue);               
                   sBody = StringUtil.replaceString(sBody, sParam, sParamValue);
                   sHtmlBody = StringUtil.replaceString(sHtmlBody, sParam, sParamValue);

                   // Replace and tokenised TO addresses with the parameter value
                   for(int i=0; i<vTO.size(); i++)
                   {
                      if(((String)vTO.elementAt(i)).equalsIgnoreCase(sParam))
                      {
                         if(sParamValue!=null && sParamValue.trim().length()>0)
                         {                     
                            vTO.setElementAt(sParamValue,i);
                         }
                         else
                         {
                            vTO.removeElementAt(i);
                         } 
                      }
                   }

                   // Replace and tokenised CC addresses with the parameter value               
                   for(int i=0; i<vCC.size(); i++)
                   {
                      if(((String)vCC.elementAt(i)).equalsIgnoreCase(sParam))
                      {
                         if(sParamValue!=null && sParamValue.trim().length()>0)
                         {                     
                            vCC.setElementAt(sParamValue,i);
                         }
                         else
                         {
                            vCC.removeElementAt(i);
                         } 
                      }
                   }

                   // Replace and tokenised BCC addresses with the parameter value               
                   for(int i=0; i<vBCC.size(); i++)
                   {
                      if(((String)vBCC.elementAt(i)).equalsIgnoreCase(sParam))
                      {
                         if(sParamValue!=null && sParamValue.trim().length()>0)
                         {                     
                            vBCC.setElementAt(sParamValue,i);
                         }
                         else
                         {
                            vBCC.removeElementAt(i);
                         } 
                      }
                   }

                   // Replace and tokenised FROM address with the parameter value               
                   if(sFromAddress.equalsIgnoreCase(sParam))
                   {
                      if(sParamValue!=null && sParamValue.trim().length()>0)
                      {                     
                         sFromAddress = sParamValue;
                      }                     
                   }
                }            
          
                message.setFrom(sFromAddress);

                //set the sent date to be the date now
                Date dateNow = new Date();
                message.setSentDate(dateNow);

                //-- set the recipients --

                // TO addresses
                for(int i=0; i<vTO.size(); i++)
                {
                  message.addTo((String)vTO.elementAt(i));
                }                        

                // CC addresses
                for(int i=0; i<vCC.size(); i++)
                {
                    message.addCc((String)vCC.elementAt(i));
                }


                // BCC addresses
                for(int i=0; i<vBCC.size(); i++)
                {
                    message.addBcc((String)vBCC.elementAt(i));
                }

                // Set the subject
                if(sSubject!=null)
                {
                    message.setSubject(sSubject);
                }

                // Set the text (if not empty):
                if (sBody != null && sBody.length() > 0)
                {
                    message.setTextMsg(sBody);
                }
                
                // Set the HTML body:
                if (sHtmlBody != null && sHtmlBody.length() > 0)
                {
                    message.setHtmlMsg(sHtmlBody);
                }
                
                // Add the attachments:
                if (a_attachments != null)
                {
                    for (int i=0; i<a_attachments.length; i++)
                    {
                        message.attach(a_attachments[i]);                        
                    }                
                }
                
                // Now send the message
                message.send();
            }
            catch (NoSuchProviderException e)
            {
               m_logger.error("FEEDBACK EMAIL ERROR : NO SUCH PROVIDER EXCEPTION : "+e);
               throw new Bn2Exception("FEEDBACK EMAIL ERROR : NO SUCH PROVIDER EXCEPTION : "+e);
            }
            catch (AuthenticationFailedException e)
            {
               m_logger.error("FEEDBACK EMAIL ERROR : AUTHENTICATION FAILED EXCEPTION : "+e);
               throw new Bn2Exception("FEEDBACK EMAIL ERROR : AUTHENTICATION FAILED EXCEPTION : "+e);
            }
            catch (SendFailedException e)
            {
               m_logger.error("FEEDBACK EMAIL ERROR : SEND FAILED EXCEPTION : "+e);
               
               if (e.getNextException() != null)
               {
                  throw new Bn2Exception(e.getNextException().getMessage());
               }
               throw new Bn2Exception(e.getMessage());
            }
            catch (MessagingException e)
            {
               m_logger.error("FEEDBACK EMAIL ERROR : MESSAGING EXCEPTION : "+e);
               throw new Bn2Exception("FEEDBACK EMAIL ERROR : MESSAGING EXCEPTION : "+e);
            }
              
         }
      }
      catch(JDOMException jdome)
      {
         m_logger.error("JDOM Exception caught during EmailManager.sendTemplatedEmail : " + jdome);
         throw new Bn2Exception("JDOM Exception caught during EmailManager.sendTemplatedEmail", jdome);
      }
   }
     
   
    /**
    * Used to send the same email to multiple recipients.
    * All reciepients will be in the 'to' field, and the subject and from address will be the same.
    * The body text can be personalised - the body text and contents of the html file can contain
    * fields (in the #value# format) which will be replaced with values from the HashMap.
    *
    *@param BatchEmail a_batchEmail
    *@throws Bn2Exception - on error
    *
    */
    public void sendBatchEmail(BatchEmail a_batchEmail)
    throws Bn2Exception
    /*
    ------------------------------------------------------------------------
    d8  08-Apr-2004         Martin Wilson     Created.
    ------------------------------------------------------------------------
    */
    {                
        // Create an HTMLEmail object:
        HtmlEmail message = new HtmlEmail();
        message.setHostName(FrameworkSettings.getEmailSMTP());
        String sPort = FrameworkSettings.getEmailSMTPPort();
		if(StringUtil.stringIsInteger(sPort))
		{
			message.setPort(Integer.parseInt(sPort));
		}
        

        // Get the html body text from the html file (if applicable):
        String sHtmlBody = null;
        if (a_batchEmail.getHtmlFilename() != null)
        {                    
            // Get the html and attach any image files:
            try
            {
                sHtmlBody = createHtmlText( message, 
                                            a_batchEmail.getDirectoryContainingHtmlFile(), 
                                            a_batchEmail.getHtmlFilename());
            }
            catch (MessagingException me)
            {
                throw new Bn2Exception("Error building batch email from HTML file:", me);
            }
        }                

        // Go through each of the recipients:
        Vector vecRecipients = a_batchEmail.getRecipients();
        for (int iRecipCount = 0; iRecipCount < vecRecipients.size(); iRecipCount++)
        {
            String sPersonalisedBody = a_batchEmail.getBody();
            String sPersonalisedHtmlBody = sHtmlBody;

            // Get the recipient:
            BatchRecipient recipient = (BatchRecipient)vecRecipients.get(iRecipCount);

            // Personalise the bodytext and html if applicable:
            if (recipient.getPersonalisation() != null)
            {

                // Get this recipient's parameter names as a set:
                Set setParams = recipient.getPersonalisation().keySet();

                // Get an interator for the parameter names
                Iterator itParams = setParams.iterator();

                // Iterate over the parameters filling in the templated fields:
                while(itParams.hasNext())
                {
                   String sParam = (String)itParams.next();          
                   String sParamValue = (String)recipient.getPersonalisation().get(sParam);

                   // Add the variable delimiters to the param name
                   sParam = k_sTemplateVariableStart + sParam + k_sTemplateVariableEnd;                              

                   // Replace the tags:            
                   sPersonalisedBody = StringUtil.replaceString(sPersonalisedBody, sParam, sParamValue);
                   sPersonalisedHtmlBody = StringUtil.replaceString(sPersonalisedHtmlBody, sParam, sParamValue);
                }            
            }
            
            try
            {
                message.setFrom(a_batchEmail.getFromAddress());

                //set the sent date to be the date now
                Date dateNow = new Date();
                message.setSentDate(dateNow);

                // Set this recipient:
                message.addTo(recipient.getEmailAddress());                 

                // Set the subject
                if(a_batchEmail.getSubject()!=null)
                {
                    message.setSubject(a_batchEmail.getSubject());
                }

                // Set the text:
                message.setTextMsg(sPersonalisedBody);

                // Set the HTML body:
                message.setHtmlMsg(sPersonalisedHtmlBody);                

                // Now send the message:  
                message.send();
            }
            catch (MessagingException e)
            {
               m_logger.error("Error sending email to batch recipient: " + recipient.getEmailAddress(), e);
            }            
        } // End recipient loop.
   }   
   
   
	 
	 /**
    * Used to send html email
    *
    *@param String a_sHtmlFileName
    *@throws Bn2Exception - on error
    *
    */
    public void sendHtmlEmail(String a_sHtmlFileDirectory, String a_sHtmlFileName, String a_sNoHtmlSupportMessage, 
									   String a_sFromAddress, String a_sToAddress, String a_sSubject) 
	 throws Bn2Exception
    /*
    ------------------------------------------------------------------------
    d9  22-Jul-2004         Matt Stevenson			Created
    ------------------------------------------------------------------------
    */
    {                
        // Create an HTMLEmail object:
        HtmlEmail message = new HtmlEmail();
        message.setHostName(FrameworkSettings.getEmailSMTP());
        String sPort = FrameworkSettings.getEmailSMTPPort();
		if(StringUtil.stringIsInteger(sPort))
		{
			message.setPort(Integer.parseInt(sPort));
		}

        // Get the html body text from the html file (if applicable):
        String sHtmlBody = null;
        
		  // Get the html and attach any image files:
        try
		  {
			 sHtmlBody = createHtmlText( message, a_sHtmlFileDirectory, a_sHtmlFileName);
		  }
		  catch (MessagingException me)
		  {
		    throw new Bn2Exception("Error building email from HTML file:", me);
		  }
                        
		  //try to send the message
        
		  try
        {
				message.setFrom(a_sFromAddress);

            //set the sent date to be the date now
            Date dateNow = new Date();
            message.setSentDate(dateNow);

            // Set this recipient:
            message.addTo(a_sToAddress);                 

            message.setSubject(a_sSubject);
            
				// Set the text:
            message.setTextMsg(a_sNoHtmlSupportMessage);

            // Set the HTML body:
            message.setHtmlMsg(sHtmlBody);                

            // Now send the message:  
            message.send();
			}
         catch (MessagingException e)
         {
				m_logger.error("Error sending html email: " + e);
         }            
   }
	 
	 
	 
    /**
    * Creates HTML text for an email from an HTML file.
    *
    *@param HtmlEmail - the email in which to set the text
    *@param String - the full path to the html file
    *@throws Bn2Exception - on error
    *
    */
    private String createHtmlText(HtmlEmail a_message, String a_sTemplateDirectory, String a_sHtmlFilename) 
                                    throws Bn2Exception, MessagingException
    /*
    ------------------------------------------------------------------------
    d7  05-Apr-2004   Martin Wilson    Created.
	 ------------------------------------------------------------------------
    */
    {
        m_logger.debug("Adding HTML body text from file: " + a_sHtmlFilename);
        
        // Create a TextPatternReplacer object to replace the image paths:
        TextPatternReplacer replacer = new TextPatternReplacer();
        
        // Load from the HTML file:
        String sFullHtmlFilePath = a_sTemplateDirectory + "/" + a_sHtmlFilename;
        
        try
        {
            replacer.loadFromFile(sFullHtmlFilePath);
        }
        catch(IOException ioe)
        {
           m_logger.error("HTML file not found while creating a HTML based email: " + sFullHtmlFilePath);
        }
        
        // Set the regular expression to use:
        replacer.setPattern(FrameworkSettings.getMatchImageFileRegularExpression());

        // Loop through all different file names:
        while (replacer.matchNext())
        {
            // Get the current image path:
            String sImagePath = replacer.getCurrentMatch();
            
            m_logger.debug("Image path found in html doc = " + sImagePath);
            
            String sFullImagePath = a_sTemplateDirectory + "/" + sImagePath;
            
            // Embed the file:
            String sCid = a_message.embed(sFullImagePath, sImagePath);            
            
            // Replace the filename with the cid in the html:
            replacer.addReplacementForCurrentMatch("cid:" + sCid);            
        }       
     
        // Replace all the filenames:
        replacer.makeReplacements();
        
        return (replacer.getContents());
   }
   
   /**
    * Returns a Vector of EmailPreview objects each containing the subject and body of a templated email
    * constructed from the templates xml file and the passed parameters.
    */
   public Vector getTemplatedEmailPreviews(HashMap a_hmParams)
   throws Bn2Exception
   /*
    ------------------------------------------------------------------------
     d3   03-Mar-2003   James Home     Created
    ------------------------------------------------------------------------
    */
   {
      String sTemplateFilepath = null;
      String sBody = null;
      String sSubject = null;
      Set setParams = null;
      Iterator itParams = null;
      String sParam = null;
      String sParamValue = null;
      String sTemplateName = null;
      EmailPreview email = null;
      Vector vEmails = new Vector();
      
      // Get the name of the template
      sTemplateName = (String)a_hmParams.get(k_sTemplateParam);
      
      // Get the email template directory
      sTemplateFilepath = FrameworkSettings.getEmailTemplateDirectory();
      
      // Fully qualify the template directory  if necessary
      if(FrameworkSettings.getUseRelativeDirectories())
      {
         sTemplateFilepath = FrameworkSettings.getApplicationPath() +
                             "/" + FrameworkSettings.getCmsFileStoreRoot() +
                             FrameworkSettings.getEmailTemplateDirectory();
      }
      
      // Construct the filepath
      sTemplateFilepath += "/" + sTemplateName + k_sEmailTemplateFileSuffix;
      
      //-- Now read the XMl template file and build the fields we need --
      
      SAXBuilder saxBuilder = new SAXBuilder();
      
      // Don't worry about the dtd:
      saxBuilder.setDTDHandler(null);
      
      // Create an empty resolver as we do not need to resolve against the DTD:
      EmptyEntityResolver emptyResolver = new EmptyEntityResolver();
      saxBuilder.setEntityResolver(emptyResolver);
      
      try
      {
         // Read from the file:
         Document document = saxBuilder.build(sTemplateFilepath);
         
         Element root = document.getRootElement();
         
         // Get the email templates
         List lstTemplates = root.getChildren("email-template");
         ListIterator lstiTemplates = (ListIterator)(lstTemplates.iterator());
         
         if (lstTemplates.size() == 0)
         {
            m_logger.error("Email template file " + sTemplateFilepath + " has no template elements.");
            throw new Bn2Exception("Email template file " + sTemplateFilepath + " has no template elements.");
         }
         
         // Get the parameter names as a set
         setParams = a_hmParams.keySet();
      
         // Iterate through the templates, sending an email for each
         for (int iTemplate = 0; iTemplate <  lstTemplates.size(); iTemplate++)
         {
            Element xmlTemplate = (Element)(lstiTemplates.next());
            
            // Get the subject line
            Element subject = xmlTemplate.getChild("subject");
            if(subject!=null)
            {
               sSubject = subject.getTextTrim();
            }
            
            // Get the body
            Element body = xmlTemplate.getChild("body");
            if(body!=null)
            {
               sBody = body.getTextTrim();
            }                        
            
            // Get an interator for the parameter names
            itParams = setParams.iterator();
            
            // Iterate over the parameters filling in the templated fields (subject, body 
            // and addresses)
            while(itParams.hasNext())
            {
               sParam = (String)itParams.next();          
               sParamValue = (String)a_hmParams.get(sParam);
               
               // Add the variable delimiters to the param name
               sParam = k_sTemplateVariableStart + sParam + k_sTemplateVariableEnd;                              
               
               sSubject = StringUtil.replaceString(sSubject, sParam, sParamValue);               
               sBody = StringUtil.replaceString(sBody, sParam, sParamValue);                              
            }            
          
            email = new EmailPreview();
            
            email.setBody(sBody);
            email.setSubject(sSubject);
            
            vEmails.add(email);
         }
      }
      catch(JDOMException jdome)
      {
         m_logger.error("JDOM Exception caught during EmailManager.sendTemplatedEmail : " + jdome);
         throw new Bn2Exception("JDOM Exception caught during EmailManager.sendTemplatedEmail", jdome);
      }
      
      return vEmails;
   }
   
   
   /**
    * Send a feedback email without a custom subject line. The email contains name/value
    * pairs as provided in the HashMap parameter.
    *
    * @param HashMap params - the name/value pairs that will make up the body of the email
    */
   public void sendFeedbackEmail(HashMap params) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d3   22-Oct-2003   James Home     Created
    ------------------------------------------------------------------------
     */
   {
      sendFeedbackEmail(params,null);
   }
   
   /**
    * Construct and send a feedback email with the subject line provided. The email contains 
    * name/value pairs as provided in the HashMap parameter.
    *
    * @param HashMap params - the name/value pairs that will make up the body of the email
    * @param String a_sSubject - the subject line of the email
    */
   public void sendFeedbackEmail(HashMap params, String a_sSubject) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   29-Apr-2003   Matt Stevenson		Created.
     d2   30-Apr-2003   Matt Stevenson          Added missing exception check
                                                and check to see if a to email
                                                has been provided
     d3   22-Oct-2003   James Home        Added subject param
    ------------------------------------------------------------------------
     */
   {
      try
      {
         String sEmailAddress = FrameworkSettings.getEmailAddress();
         
         //construct a body from all the input params in the hash
         String sBodyText = "";
         Set keys = params.keySet();
         Iterator keyIterate = keys.iterator();
         
         while (keyIterate.hasNext())
         {
            String sParamName = (String)keyIterate.next();
            //don't output format or destination params
            if ((!sParamName.equals("x")) && (!sParamName.equals("y")) && (!sParamName.equals(k_sTOEMAILParam)) && (!sParamName.equals(k_sFEEDBACKSUCCESSParam)) && (!sParamName.equals(k_sFEEDBACKFAILUREParam)))
            {
               String sParam = (String)params.get(sParamName);
               sBodyText = sBodyText + sParamName + " : " + sParam + "\n";
            }
            
            //if a too email address is supplied use this to send to
            if (sParamName.equals(k_sTOEMAILParam))
            {
               sEmailAddress = (String)params.get(sParamName);
            }
         }
         
         //construct the connection Properties
         Properties props = new Properties();
         props.put("mail.host",FrameworkSettings.getEmailSMTP());
         
         //use the properties to get a mail session
         Session mailSession = Session.getDefaultInstance(props, null);
         
         //construct the message to send
         Message newMessage = new MimeMessage(mailSession);
         
         //set the from value of the message
         InternetAddress fromAddress = new InternetAddress(sEmailAddress);
         newMessage.setFrom(fromAddress);
         
         //set the list of params to be the string just constructed
         newMessage.setText(sBodyText);
         
         //set the sent date to be the date now
         Date dateNow = new Date();
         newMessage.setSentDate(dateNow);
         
         //now set the recipient
         InternetAddress emailAddress = new InternetAddress(sEmailAddress);
         newMessage.setRecipient(Message.RecipientType.TO,emailAddress);
         
         if(a_sSubject!=null)
         {
            newMessage.setSubject(a_sSubject);
         }
         
         //now send the message
         Transport.send(newMessage);
      }
      catch (NoSuchProviderException e)
      {
         m_logger.error("FEEDBACK EMAIL ERROR : NO SUCH PROVIDER EXCEPTION : "+e);
         throw new Bn2Exception("FEEDBACK EMAIL ERROR : NO SUCH PROVIDER EXCEPTION : "+e);
      }
      catch (AuthenticationFailedException e)
      {
         m_logger.error("FEEDBACK EMAIL ERROR : AUTHENTICATION FAILED EXCEPTION : "+e);
         throw new Bn2Exception("FEEDBACK EMAIL ERROR : AUTHENTICATION FAILED EXCEPTION : "+e);
      }
      catch (SendFailedException e)
      {
         m_logger.error("FEEDBACK EMAIL ERROR : SEND FAILED EXCEPTION : "+e);
         
         if (e.getNextException() != null)
         {
            throw new Bn2Exception(e.getNextException().getMessage());
         }
         throw new Bn2Exception(e.getMessage());
      }
      catch (MessagingException e)
      {
         m_logger.error("FEEDBACK EMAIL ERROR : MESSAGING EXCEPTION : "+e);
         throw new Bn2Exception("FEEDBACK EMAIL ERROR : MESSAGING EXCEPTION : "+e);
      }
   } 
   
}



/*
<email-templates>
 
   <email-template>
      <addresses>
         <from>aaa@aaa.com</from>
         <to>aaa@aaa.com</to>
         <to>#this#</to>
         <cc>aaa@aaa.com</cc>
         <cc>#this#</cc>
         <bcc>aaa@aaa.com</bcc>
         <bcc>aaa@aaa.com</bcc>
      </addresses>
      <subject>
         [subject, with #this# being a parameter name to replace with its value]
      </subject>
      <body>
         [body, with #this# being a parameter name to replace with its value]
      </body>
   </email-template>
 
   <email-template>
      <addresses>
         <from>aaa@aaa.com</from>
         <to>aaa@aaa.com</to>
         <to>#this#</to>
         <cc>aaa@aaa.com</cc>
         <cc>#this#</cc>
         <bcc>aaa@aaa.com</bcc>
         <bcc>aaa@aaa.com</bcc>
      </addresses>
      <subject>
         [subject, with #this# being a parameter name to replace with its value]
      </subject>
      <body>
         [body, with #this# being a parameter name to replace with its value]
      </body>
   </email-template>
 
</email-templates>
 
 */

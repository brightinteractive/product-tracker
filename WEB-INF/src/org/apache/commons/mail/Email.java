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

/*
 * Bright Interactive changes
 * d1 09-Aug-2007			Steve Bryan			Use SMTP Port and authentication if set		
 * 											
*/

package org.apache.commons.mail;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * The base class for all email messages.  This class sets the
 * sender's email & name, receiver's email & name, subject, and the
 * sent date.  Subclasses are responsible for setting the message
 * body.
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:greg@shwoop.com">Greg Ritter</a>
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @author <a href="mailto:colin.chalmers@maxware.nl">Colin Chalmers</a>
 * @version $Id: Email.java,v 1.1 2007-10-29 16:52:14 matt Exp $
 */
public abstract class Email
{
    /** Constants used by Email classes. */
    public static final String SENDER_EMAIL = "sender.email";
    public static final String SENDER_NAME = "sender.name";
    public static final String RECEIVER_EMAIL = "receiver.email";
    public static final String RECEIVER_NAME = "receiver.name";
    public static final String EMAIL_SUBJECT = "email.subject";
    public static final String EMAIL_BODY = "email.body";
    public static final String CONTENT_TYPE = "content.type";

    public static final String MAIL_HOST = "mail.host";
    public static final String MAIL_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_FROM = "mail.smtp.from";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    public static final String SMTP = "smtp";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String ATTACHMENTS = "attachments";
    public static final String FILE_SERVER = "file.server";
    public static final String MAIL_DEBUG = "mail.debug";

    public static final String KOI8_R = "koi8-r";
    public static final String ISO_8859_1 = "iso-8859-1";
    public static final String US_ASCII = "us-ascii";

    /** The email message to send. */
    protected MimeMessage message = null;

    /** The charset to use for this message */
    protected String charset = null;

    /** The Address of the sending party, mandatory */
    private InternetAddress fromAddress = null;

    /** The Subject  */
    private String subject = null;

    /** An attachment  */
    private MimeMultipart emailBody = null;

    /** The content  */
    private Object content = null;

    /** The content type  */
    private String contentType = null;

    /** Set session debugging on or off */
    private boolean debug = false;

    /** Sent date */
    private Date sentDate;

    /**
     * Instance of an <code>Authenticator</code> object that will be used
     * when authentication is requested from the mail server.
     */
    private Authenticator authenticator;

   /**
    * The hostname of the mail server with which to connect. If null will try
    * to get property from system.properties. If still null, quit
    */
    private String hostName = null;
    
    // Optional port
    private int m_iPort = 0;
    
    /** List of "to" email adresses */
    private ArrayList toList = null;

    /** List of "cc" email adresses */
    private ArrayList ccList = null;

    /** List of "bcc" email adresses */
    private ArrayList bccList = null;

    /** List of "replyTo" email adresses */
    private ArrayList replyList = null;

    /**
     * Used to specify the mail headers.  Example:
     *
     * X-Mailer: Sendmail, X-Priority: 1(highest)
     * or  2(high) 3(normal) 4(low) and 5(lowest)
     * Disposition-Notification-To: user@domain.net
     */
    private Hashtable headers = null;

    /**
     * Setting to true will enable the display of debug information.
     *
     * @param d A boolean.
     */
    public void setDebug(boolean d)
    {
        this.debug = d;
    }

    /**
     * Sets the userName and password if authentication is needed.  If this
     * method is not used, no authentication will be performed.
     * <p>
     * This method will create a new instance of
     * <code>DefaultAuthenticator</code> using the supplied parameters.
     *
     * @param userName User name for the SMTP server
     * @param password password for the SMTP server
     * @see DefaultAuthenticator
     * @see #setAuthenticator
     */
    public void setAuthentication(String userName, String password)
    {
        Authenticator authenticator = new DefaultAuthenticator(
                userName, password);
        setAuthenticator(authenticator);
    }

    /**
     * Sets the <code>Authenticator</code> to be used when authentication
     * is requested from the mail server.
     * <p>
     * This method should be used when your outgoing mail server requires
     * authentication.  Your mail server must also support RFC2554.
     *
     * @param authenticator the <code>Authenticator</code> object.
     * @see Authenticator
     */
    public void setAuthenticator(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }

    /**
     * Set the charset of the message.
     *
     * @param charset A String.
     */
    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    /**
     * Set the emailBody to a MimeMultiPart
     *
     * @param   aMimeMultipart
     */
    public void setContent(MimeMultipart aMimeMultipart)
    {
        this.emailBody = aMimeMultipart;
    }

    /**
     * Set the content & contentType
     *
     * @param   aObject
     * @param   aContentType
     */
    public void setContent(Object aObject, String aContentType)
    {
        this.content = aObject;
        if (aContentType == null)
        {
            this.contentType = aContentType;
        }
        else
        {
            int charsetPos = aContentType.toLowerCase().indexOf("; charset=");
            if (charsetPos > 0)
            {
                aContentType.substring(0, charsetPos);
                charset = aContentType.substring(charsetPos + 10);
            }
            else
            {
                this.contentType = aContentType;
            }
        }
    }

    /**
     * Set the hostname of the outgoing mail server
     *
     * @param   aHostName
     */
    public void setHostName(String aHostName)
    {
        this.hostName = aHostName;
    }

    /**
     * Initialise a mailsession object
     *
     * @return A Session.
     */
    private Session getMailSession()
            throws MessagingException
    {
        Properties properties = System.getProperties();
        properties.setProperty (MAIL_TRANSPORT_PROTOCOL, SMTP);

        // host name
        if (hostName == null)
        {
            hostName = properties.getProperty(MAIL_HOST);
        }
        if (hostName == null)
        {
            throw new MessagingException(
                "Cannot find valid hostname for mail session");
        }
        properties.setProperty(MAIL_HOST, hostName);
        
        // smtp port
        if (m_iPort != 0)
        {
      	  properties.setProperty(MAIL_PORT, new Integer(m_iPort).toString());
        }
        
        properties.setProperty(MAIL_DEBUG,new Boolean(this.debug).toString());

        if (this.authenticator!= null)
        {
            properties.setProperty(MAIL_SMTP_AUTH, "true");
        }

        Session session = Session.getDefaultInstance(
                properties, this.authenticator);

        return session;
    }

    /**
     * Set the FROM field of the email.
     *
     * @param email A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email setFrom(String email)
            throws MessagingException
    {
        return setFrom(email, null);
    }

    /**
     * Set the FROM field of the email.
     *
     * @param email A String.
     * @param name A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email setFrom(String email, String name)
            throws MessagingException
    {
        try
        {
            if ((name == null) || (name.trim().equals("")))
            {
                name = email;
            }
            if (fromAddress == null)
            {
                if (charset != null)
                {
                    fromAddress = new InternetAddress(email, name, charset);
                }
                else
                {
                    fromAddress = new InternetAddress(email, name);
                }
            }
            else
            {
                fromAddress.setAddress(email);
                fromAddress.setPersonal(name);
            }
        }
        catch (Exception e)
        {
            throw new MessagingException("cannot set from", e);
        }
        return this;
    }

    /**
     * Add a recipient TO to the email.
     *
     * @param email A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addTo(String email)
            throws MessagingException
    {
        return addTo(email, null);
    }

    /**
     * Add a recipient TO to the email.
     *
     * @param email A String.
     * @param name A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addTo(String email, String name)
            throws MessagingException
    {
        try
        {
            if ((name == null) || (name.trim().equals("")))
            {
                name = email;
            }

            if (toList == null)
            {
                toList = new ArrayList();
            }

            if (charset != null)
            {
                toList.add(new InternetAddress(email, name, charset));
            }
            else
            {
                toList.add(new InternetAddress(email, name));
            }
        }
        catch (Exception e)
        {
            throw new MessagingException("cannot add to", e);
        }
        return this;
    }

    /**
     * Set a list of "TO" addresses
     *
     * @param   aCollection collection of InternetAddress objects
     * @return An Email.
     */
    public Email setTo(Collection aCollection)
    {
        toList = new ArrayList(aCollection);
        return this;
    }

    /**
     * Add a recipient CC to the email.
     *
     * @param email A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addCc(String email)
            throws MessagingException
    {
        return addCc(email, null);
    }

    /**
     * Add a recipient CC to the email.
     *
     * @param email A String.
     * @param name A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addCc(String email, String name)
            throws MessagingException
    {
        try
        {
            if ((name == null) || (name.trim().equals("")))
            {
                name = email;
            }

            if (ccList == null)
            {
                ccList = new ArrayList();
            }

            if (charset != null)
            {
                ccList.add(new InternetAddress(email, name, charset));
            }
            else
            {
                ccList.add(new InternetAddress(email, name));
            }
        }
        catch (Exception e)
        {
            throw new MessagingException("cannot add cc", e);
        }

        return this;
    }

    /**
     * Set a list of "CC" addresses
     *
     * @param   aCollection collection of InternetAddress objects
     * @return An Email.
     */
    public Email setCc(Collection aCollection)
    {
        ccList = new ArrayList(aCollection);
        return this;
    }

    /**
     * Add a blind BCC recipient to the email.
     *
     * @param email A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addBcc(String email)
            throws MessagingException
    {
        return addBcc(email, null);
    }

    /**
     * Add a blind BCC recipient to the email.
     *
     * @param email A String.
     * @param name A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addBcc(String email, String name)
            throws MessagingException
    {
        try
        {
            if ((name == null) || (name.trim().equals("")))
            {
                name = email;
            }

            if (bccList == null)
            {
                bccList = new ArrayList();
            }

            bccList.add(new InternetAddress(email, name));
        }
        catch (Exception e)
        {
            throw new MessagingException("cannot add bcc", e);
        }

        return this;
    }

    /**
     * Set a list of "BCC" addresses
     *
     * @param   aCollection collection of InternetAddress objects
     * @return  An Email.
     */
    public Email setBcc(Collection aCollection)
    {
        bccList = new ArrayList(aCollection);
        return this;
    }

    /**
     * Add a reply to address to the email.
     *
     * @param email A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addReplyTo(String email) throws MessagingException
    {
        return addReplyTo(email, null);
    }

    /**
     * Add a reply to address to the email.
     *
     * @param email A String.
     * @param name A String.
     * @return An Email.
     * @exception MessagingException Indicates an invalid email address
     */
    public Email addReplyTo(String email, String name)
            throws MessagingException
    {
        try
        {
            if ((name == null) || (name.trim().equals("")))
            {
                name = email;
            }

            if (replyList == null)
            {
                replyList = new ArrayList();
            }

            if (charset != null)
            {
                replyList.add(new InternetAddress(email, name, charset));
            }
            else
            {
                replyList.add(new InternetAddress(email, name));
            }
        }
        catch (Exception e)
        {
            throw new MessagingException("cannot add replyTo", e);
        }
        return this;
    }

    /**
     * Used to specify the mail headers.  Example:
     *
     * X-Mailer: Sendmail, X-Priority: 1(highest)
     * or  2(high) 3(normal) 4(low) and 5(lowest)
     * Disposition-Notification-To: user@domain.net
     *
     * @param h A Hashtable.
     */
    public void setHeaders(Hashtable h)
    {
        headers = h;
    }

    /**
     * Adds a header (name, value) to the headers Hashtable.
     *
     * @param name A String with the name.
     * @param value A String with the value.
     */
    public void addHeader(String name, String value)
    {
        if( name == null )
        {
            throw new IllegalArgumentException("name can not be null");
        }
        if( value == null )
        {
            throw new IllegalArgumentException("value can not be null");
        }
        if (headers == null)
        {
            headers = new Hashtable();
        }
        headers.put(name, value);
    }

    /**
     * Set the email subject.
     *
     * @param aSubject A String.
     * @return An Email.
     */
    public Email setSubject(String aSubject)
    {
        subject = aSubject;
        return this;
    }

    /**
     * Define the content of the mail.  It should be overidden by the
     * subclasses.
     *
     * @param msg A String.
     * @return An Email.
     * @exception MessagingException generic exception
     */
    public abstract Email setMsg(String msg) throws MessagingException;

    /**
     * Does the work of actually sending the email.
     *
     * @exception MessagingException if there was an error.
     */
    public void send()
            throws MessagingException
    {
        Session session = getMailSession();
        MimeMessage message = new MimeMessage(session);

        if (subject != null)
        {
            if (charset == null)
            {
                message.setSubject(subject);
            }
            else
            {
                message.setSubject(subject, charset);
            }
        }

        if (content != null)
        {
            String type = contentType;
            if (type != null && charset != null)
            {
                type += "; charset=" + charset;
            }
            message.setContent(content, type);
        }
        else if (emailBody != null)
        {
            message.setContent(emailBody);
        }
        else
        {
            message.setContent("",TEXT_PLAIN);
        }

        if (fromAddress != null)
        {
            message.setFrom(fromAddress);
        }
        else
        {
            throw new MessagingException("Sender address required");
        }

        if (toList == null && ccList == null && bccList == null)
        {
            throw new MessagingException(
                "At least one receiver address required");

        }
        if (toList != null)
        {
            message.setRecipients(Message.RecipientType.TO,
                toInternetAddressArray(toList));
        }

        if (ccList != null)
        {
            message.setRecipients(Message.RecipientType.CC,
                toInternetAddressArray(ccList));
        }

        if (bccList != null)
        {
            message.setRecipients(Message.RecipientType.BCC,
                toInternetAddressArray(bccList));
        }

        if (replyList != null)
        {
            message.setReplyTo(toInternetAddressArray(replyList));
        }

        if (headers != null)
        {
            Enumeration e = headers.keys();
            while (e.hasMoreElements())
            {
                String name = (String) e.nextElement();
                String value = (String) headers.get(name);
                message.addHeader(name, value);
            }
        }

        if (message.getSentDate() == null)
        {
            message.setSentDate(getSentDate());
        }

        Transport.send(message);
    }

    /**
     * Sets the sent date for the email.  The sent date will default to the
     * current date if not explictly set.
     *
     * @param date Date to use as the sent date on the email
     */
    public void setSentDate(Date date)
    {
        this.sentDate = date;
    }

    /**
     * Gets the sent date for the email.
     *
     * @return date to be used as the sent date for the email
     */
    public Date getSentDate()
    {
        return (this.sentDate == null ? new Date() : this.sentDate);
    }

    /**
     * Utility to copy ArrayList of known InternetAddress objects into an
     * array.
     *
     * @param aList A ArrayList.
     * @return An InternetAddress[].
     */
    private InternetAddress[] toInternetAddressArray(ArrayList aList)
    {
        InternetAddress[] ia =
            (InternetAddress[]) aList.toArray(new InternetAddress[0]);

        return ia;
    }

	public int getPort()
	{
		return m_iPort;
	}

	public void setPort(int a_sPort)
	{
		m_iPort = a_sPort;
	}    
    
}

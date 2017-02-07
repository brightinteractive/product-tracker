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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;

/**
 * A multipart email.
 *
 * <p>This class is used to send multi-part internet email like
 * messages with attachments.
 *
 * <p>To create a multi-part email, call the default constructor and
 * then you can call setMsg() to set the message and call the
 * different attach() methods.
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:unknown">Regis Koenig</a>
 * @version $Id: MultiPartEmail.java,v 1.1 2007-10-29 16:52:14 matt Exp $
 */
public class MultiPartEmail extends Email
{
    /** Body portion of the email. */
    private MimeMultipart container = null;

    /** The message container. */
    private MimeBodyPart primaryBodyPart = null;

    /** Indicates if the message has been initialized */
    private boolean initialized = false;

    /**
     * Initialize the multipart email.
     *
     * @exception MessagingException
     */
    private void init() throws MessagingException
    {
        if (initialized)
        {
            throw new IllegalStateException("Already initialized");
        }

        container = new MimeMultipart();
        super.setContent(container);

        // Add the first body part to the message.  The fist body part must be
        primaryBodyPart = new MimeBodyPart();
        container.addBodyPart(primaryBodyPart);

        initialized = true;
    }

    /**
     * Set the message of the email.
     *
     * @param msg A String.
     * @return An Email.
     * @exception MessagingException
     */
    public Email setMsg(String msg) throws MessagingException
    {
        if (charset != null)
        {
            getPrimaryBodyPart().setText(msg, charset);
        }
        else
        {
            getPrimaryBodyPart().setText(msg);
        }
        return this;
    }

    /**
     * Sends the mail message
     *
     * @throws MessagingException
     */
    public void send() throws MessagingException
    {
        // before a multipart message can be sent, we must make sure that
        // the content for the main body part was actually set.  If not,
        // an IOException will be thrown during super.send().

        MimeBodyPart body = this.getPrimaryBodyPart();
        Object content = null;
        try
        {
            content = body.getContent();
        }
        catch (IOException e)
        {
            // do nothing here.  content will be set to an empty string
            // as a result.
        }
        if(content == null)
        {
            body.setContent("", TEXT_PLAIN);
        }

        super.send();
    }

    /**
     * Attach an EmailAttachement.
     *
     * @param attachment An EmailAttachment.
     * @return A MultiPartEmail.
     * @exception MessagingException
     */
    public MultiPartEmail attach(EmailAttachment attachment)
            throws MessagingException
    {
        MultiPartEmail result = null;

        URL url = attachment.getURL();
        if (url == null)
        {
            String fileName = null;
            try
            {
                fileName = attachment.getPath();
                File file = new File(fileName);
                if (!file.exists())
                {
                    throw new IOException("\""+fileName + "\" does not exist");
                }
                result = attach(new FileDataSource(file), attachment.getName(),
                        attachment.getDescription(),
                        attachment.getDisposition());
            }
            catch (Exception e)
            {
                throw new MessagingException("Cannot attach file \""
                        +fileName+"\"", e);
            }
        }
        else
        {
            result = attach(url, attachment.getName(),
                    attachment.getDescription(), attachment.getDisposition());
        }

        return result;
    }

    /**
     * Attach a file located by its URL.  The disposition of the file
     * is set to mixed.
     *
     * @param url The URL of the file (may be any valid URL).
     * @param name The name field for the attachment.
     * @param description A description for the attachment.
     * @return A MultiPartEmail.
     * @exception MessagingException
     */
    public MultiPartEmail attach(URL url, String name, String description)
            throws MessagingException
    {
        return attach(url, name, description, EmailAttachment.ATTACHMENT);
    }

    /**
     * Attach a file located by its URL.
     *
     * @param url The URL of the file (may be any valid URL).
     * @param name The name field for the attachment.
     * @param description A description for the attachment.
     * @param disposition Either mixed or inline.
     * @return A MultiPartEmail.
     * @exception MessagingException
     */
    public MultiPartEmail attach(URL url, String name, String description,
                                 String disposition)
            throws MessagingException
    {
        return attach(
                new URLDataSource(url), name, description, disposition);
    }

    /**
     * Attach a file specified as a DataSource interface.
     *
     * @param ds A DataSource interface for the file.
     * @param name The name field for the attachment.
     * @param description A description for the attachment.
     * @return A MultiPartEmail.
     * @exception MessagingException
     */
    public MultiPartEmail attach(DataSource ds, String name, String description)
            throws MessagingException
    {
        return attach(ds, name, description, EmailAttachment.ATTACHMENT);
    }

    /**
     * Attach a file specified as a DataSource interface.
     *
     * @param ds A DataSource interface for the file.
     * @param name The name field for the attachment.
     * @param description A description for the attachement.
     * @param disposition Either mixed or inline.
     * @return A MultiPartEmail.
     * @exception MessagingException
     */
    public MultiPartEmail attach(DataSource ds, String name,
                                 String description, String disposition)
            throws MessagingException
    {
        MimeBodyPart mbp = new MimeBodyPart();
        getContainer().addBodyPart(mbp);

        mbp.setDisposition(disposition);
        if (StringUtils.isEmpty(name))
        {
            name = ds.getName();
        }
        mbp.setFileName(name);
        mbp.setDescription(description);
        mbp.setDataHandler(new DataHandler(ds));

        return this;
    }

    /**
     * Gets first body part of the message.
     *
     * @return The primary body part.
     * @throws MessagingException message could not be initialized
     */
    protected MimeBodyPart getPrimaryBodyPart() throws MessagingException
    {
        if(!initialized) {
            init();
        }
        return primaryBodyPart;
    }

    /**
     * Gets the message container.
     *
     * @return The message container.
     * @throws MessagingException message could not be initialized
     */
    protected MimeMultipart getContainer() throws MessagingException
    {
        if(!initialized) {
            init();
        }
        return container;
    }

}

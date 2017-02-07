package com.bright.framework.image.bean;

/**
 * bright interactive, BImage
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * BImage.java
 * 
 * Contains the BImage class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   08-Oct-2003    Matt Stevenson      Created
d2   13-Oct-2003    Matt Stevenson      Incorperated image meta data
d3   14-Oct-2003    Matt Stevenson      Added Viewing and Thumbnail image url
                                        variables and accessor methods
d4   15-Oct-2003    Matt Stevenson      Changed Viewing image to Medium image
d5   17-Oct-2003    Matt Stevenson      Added Height and Width params
d6   20-Oct-2003    Matt Stevenson      Added imageFormat id
d7   21-Oct-2003    Matt Stevenson      Added shortDescription (for use on search results)
d8   22-Oct-2003    Matt Stevenson      Added categories vector and access methods
d9   28-Oct-2003    Matt Stevenson      Added methods to get string version of dates
d10  16-Jan-2004    Matt Stevenson      Imported into merchandise manager, modified
                                        to suit
d11  17-Jan-2004    Martin Wilson       Moved into 'image' package.
d12  19-Jan-2004    Matt Stevenson      Changes due to directory restructuring
d13  26-Jan-2004    Matt Stevenson      Added large image url
d14  15-Oct-2004	  Matt Stevenson		 Imported into rt framework
d15  09-Nov-2004	  Matt Stevenson		 Added original image url
d16  07-Jan-2005    Martin Wilson       Added getUrlForPage.
--------------------------------------------------------------------------------
*/

import com.bright.framework.file.bean.BFile;

/**
 * Represents an Image Manager Image
 *
 * @author  bright interactive
 * @version d1
 */
public class BImage extends BFile
{
    //image metadata variables
    private long m_lParentImageId = 0;
    
    private int m_iHeight = 0;
    private int m_iWidth = 0;
    private String m_sOriginalImageUrl = null;
    private String m_sCaption = null;
    private int m_iBorderWidth = 0;
    private String m_sUrlForPage = null;
    
    /** @return Returns the parentImageId. */
	 public long getParentImageId()
	 {
		return m_lParentImageId;
	 }
	
	 /** @param a_lParentImageId The parentImageId to set. */
	 public void setParentImageId(long a_lParentImageId)
	 {
		m_lParentImageId = a_lParentImageId;
	 }
	
	 /** @return Returns the height. */
	 public int getHeight()
 	 {
		return m_iHeight;
	 }
	
	 /** @param a_iHeight The height to set. */
	 public void setHeight(int a_iHeight)
	 {
		m_iHeight = a_iHeight;
	 }
	
	 /** @return Returns the width. */
	 public int getWidth()
	 {
		return m_iWidth;
	 }
	
	 /** @param a_iWidth The width to set. */
	 public void setWidth(int a_iWidth)
	 {
	 	m_iWidth = a_iWidth;
	 }
	
	 /** @return Returns the originalImageUrl. */
	 public String getOriginalImageUrl()
	 {
	 	return m_sOriginalImageUrl;
	 }
	
	 /** @param a_sOriginalImageUrl The originalImageUrl to set. */
	public void setOriginalImageUrl(String a_sOriginalImageUrl)
	{
		m_sOriginalImageUrl = a_sOriginalImageUrl;
	}

	/** @return Returns the caption. */
	public String getCaption()
	{
		return m_sCaption;
	}

	/** @param a_sCaption The caption to set. */
	public void setCaption(String a_sCaption)
	{
		m_sCaption = a_sCaption;
	}

	/** @return Returns the borderWidth. */
	public int getBorderWidth()
	{
		return m_iBorderWidth;
	}

	/** @param a_sBorderWidth The borderWidth to set. */
	public void setBorderWidth(int a_sBorderWidth)
	{
		m_iBorderWidth = a_sBorderWidth;
	}
	/** @return Returns the urlForPage. */
	public String getUrlForPage()
	{
		return m_sUrlForPage;
	}
	/** @param a_sUrlForPage The urlForPage to set. */
	public void setUrlForPage(String a_sUrlForPage)
	{
		m_sUrlForPage = a_sUrlForPage;
	}
}
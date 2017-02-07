package com.bright.framework.image.constant;

/**
 * Bright Interactive, Image settings
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 *
 * Contains the Settings for images.
 */
/*
Ver  Date	    	Who                 Comments
--------------------------------------------------------------------------------
d1   17-Jan-2004    Martin Wilson		Created.
--------------------------------------------------------------------------------
*/

/**
 * Settings that are available to any class.
 *
 * @author  bright interactive
 * @version d1
 */
public interface ImageSettings
{
    public int getThumbnailImageWidth();
    public int getMediumImageWidth();   
    public float getJpgConversionQuality();
}
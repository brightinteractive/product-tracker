package com.bright.framework.image.constant;

/**
 * Bright Interactive, Merchandise Manager
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * ImageConstants.java
 *
 * Contains the constants for ImageManager and other image classes.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   17-Jan-2004    Martin Wilson       Created.
d2   26-Jan-2004    Matt Stevenson      Added necessary constants for large image url
--------------------------------------------------------------------------------
*/

/**
 * Contains global constants.
 * To use, implement this interface in your class.
 *
 * @author  bright interactive
 * @version d1
 */
public interface ImageConstants
{
    //image related constants
    public static final String k_sJpegFileExtension = ".jpg";
    public static final String k_sGIFFileExtension = ".gif";
    public static final String k_sPNGFileExtension = ".png";
    public static final String k_sMediumImageFileExtension = "-M" + k_sJpegFileExtension;
    public static final String k_sThumbnailFileExtension = "-T" + k_sJpegFileExtension;  
    public static final String k_sLargeFileExtension = "-L" + k_sJpegFileExtension;
    public static final String k_sResizedImageSuffix = "_R";
}
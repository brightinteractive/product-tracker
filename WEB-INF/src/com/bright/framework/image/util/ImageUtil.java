package com.bright.framework.image.util;

/**
 * bright interactive image classes
 *
 * Copyright 2003 bn2web, All Rights Reserved.
 * ApplicationUtil.java
 * 
 * Contains the ApplicationUtil class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   14-Oct-2003    Matt Stevenson      Created
d2   17-Oct-2003    Matt Stevenson      Added getDimensionsOfImage method
d3   24-Oct-2003    Matt Stevenson      Added gif to processable files
d4   16-Jan-2004    Matt Stevenson      Imported from image manager
d5   17-Jan-2004    Martin Wilson		Moved to image package.
d6	  15-Oct-2004	  Matt Stevenson		Imported into rt framework
--------------------------------------------------------------------------------
*/

import java.awt.Dimension;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.util.FileUtil;


/**
 * bright interactive image classes
 *
 * Copyright 2003 bn2web, All Rights Reserved.
 * ImageUtil.java
 * 
 * Contains the ImageUtil class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   14-Oct-2003    Matt Stevenson      Created
d2   17-Oct-2003    Matt Stevenson      Added getDimensionsOfImage method
d3   24-Oct-2003    Matt Stevenson      Added gif to processable files
d4   16-Jan-2004    Matt Stevenson      Imported from image manager
d5   17-Jan-2004    Martin Wilson		Moved to image package.
d6	 22-May-2007	Kevin Bennett		Removed all uses of JMagick 
										Instead uses command line ImageMagick calls via ImageMagick class
--------------------------------------------------------------------------------
*/

import java.util.Arrays;
import java.util.List;

import com.bright.framework.image.util.ImageMagick;
import com.bright.framework.image.util.ImageMagickOptionList;

/**
 * Static methods for common image operations.
 *
 * @author  bn2web
 * @version d1
 */
public class ImageUtil
{

    /**
     * Assesses whether an image can be processeced by this ImageUtil class, which in
     * turn depends on whether the underlying ImageMagick implementation it uses
     * can deal with the image type. The assessment is made by the file suffix, and
     * is done 'by hand' as there is no apparent way of determining the capabilities
     * of the ImageMagick API programatically.
     * 
     * @param String a_sFilename - the name of the file to assess
     * @return boolean - true if the file can be processed by jai, false otherwise
     */
    public static boolean isProcessableImageFile(String a_sFilename)
    /*   
    --------------------------------------------------------------------------------
         15-Apr-2003    James Home          Created
     d1  14-Oct-2003    Matt Stevenson      Imported from HRHO
     d3  24-Oct-2003    Matt Stevenson      Added gif to processable files
    --------------------------------------------------------------------------------
    */
    {
        String sSuffix = null;
        boolean bResult = false;
        
        sSuffix = FileUtil.getSuffix(a_sFilename);
        
        if(sSuffix != null && sSuffix.length() > 0)
        {        
            bResult = (sSuffix.compareToIgnoreCase("jpg")==0 || 
                       sSuffix.compareToIgnoreCase("jpeg")==0 ||
                       sSuffix.compareToIgnoreCase("tif")==0 ||
                       sSuffix.compareToIgnoreCase("tiff")==0 ||
                       sSuffix.compareToIgnoreCase("bmp")==0 ||
                       sSuffix.compareToIgnoreCase("png")==0 ||
                       sSuffix.compareToIgnoreCase("gif")==0);
        }
        
        return bResult;
    }
 
       
    /**
     * Scales an input image and saves it in a new file as specified by a_sDestFilename. 
     * The image is scaled so that it fits in a bounding rectange defined by a_iMaxHeight 
     * and a_iMaxWidth. The parameter a_fJpegQuality defines the quality/filessize of the 
     * output image (if applicable), and should be between 0 and 1.
     *
     * @param String a_sFilename - The filename, including path, of the input image
     * @param String a_sNewFilename - The filename, including path, of the output image
     * @param float a_fJpegQuality - The quality of the output file, from 0 to 1
     * @param int a_iMaxHeight - The height of the bounding rectangle
     * @param int a_iMaxWidth - The width of the bounding rectangle
     */
    public static void scaleImageToRectangle(String a_sFilename, 
                                            String a_sDestFilename,
                                            float a_fJpegQuality,
                                            int a_iMaxHeight,
                                            int a_iMaxWidth,
                                            boolean a_bMaintainAspectRatio)
    throws Bn2Exception
    /*   
    --------------------------------------------------------------------------------
         14-Apr-2003    James Home          Created
         01-May-2003    James Home          Re-written to use JMagick
         08-May-2003    James Home          Added cleanup code
     d1  14-Oct-2003    Matt Stevenson      Imported from HRHO
     d6	 22-May-2007	Kevin Bennett		Removed all uses of JMagick 
											Instead uses command line ImageMagick calls via ImageMagick class
    --------------------------------------------------------------------------------
    */
    {
        int iHeight = 0, iWidth = 0;
        float fVModifier = 0;
        float fHModifier = 0;
        
        try
        {        	
        	List tags = Arrays.asList(new String[] {"w", "h"});
			String[] data = ImageMagick.getInfo(tags, a_sFilename);
        	
			iWidth = Integer.parseInt(data[0]);
			iHeight = Integer.parseInt(data[1]); 

            fVModifier = a_iMaxHeight / (float) iHeight;
            fHModifier = a_iMaxWidth / (float) iWidth;

            if(a_bMaintainAspectRatio)
            {                    
                fHModifier = fVModifier = fHModifier<fVModifier ? fHModifier : fVModifier;
            }

            iHeight *= fVModifier;
            iWidth *= fHModifier;
            
            // Write the destination image
            ImageMagickOptionList imageMagickOptionList = new ImageMagickOptionList();
            imageMagickOptionList.addFilename(a_sFilename);
            imageMagickOptionList.addResize(iWidth, iHeight,null);
            if(a_fJpegQuality > 0 && a_fJpegQuality <= 1)
            {
            	imageMagickOptionList.addQuality((int) (a_fJpegQuality * 100));               
            }            
            imageMagickOptionList.addFilename(a_sDestFilename);
            
            ImageMagick.convert(imageMagickOptionList);
        }
        catch(Exception e)
        {        	
        	throw new Bn2Exception("Exeception in ImageUtil.scaleImageToRectangle: "+e);
        }        
    }
        
    
    /**
     * Scales an input image and saves the new image as a new file with the path 
     * specified. The image is scaled so that it has a width of a_iWidth pixels with 
     * the aspect ratio maintained.
     *
     * @param String a_sFilename - The filename, including path, of the input image
     * @param String a_sNewFilename - The filename, including path, of the output image
     * @param float a_fJpegQuality - The quality of the output Jpeg, from 0 to 1
     * @param int a_iWidth - The width of the output image
     */
    public static void scaleImageToWidth(String a_sFilename, 
                                        String a_sDestFilename,
                                        float a_fJpegQuality,
                                        int a_iWidth)
    throws Bn2Exception 
    /*   
    --------------------------------------------------------------------------------
         14-Apr-2003    James Home          Created
         01-May-2003    James HOme          Updated to call scaleImageToRectangle
    d1   14-Oct-2003    Matt Stevenson      Imported from HRHO
    --------------------------------------------------------------------------------
    */
    {
        scaleImageToRectangle(a_sFilename,a_sDestFilename,a_fJpegQuality,Integer.MAX_VALUE,a_iWidth,true);
    }
    
    
    /**
     * Scales an input image and saves the new image as a new file as specified by 
     * a_sNewFilename. The image is scaled so that it has a width of a_iHeight pixels 
     * with the aspect ratio maintained.
     *
     * @param String a_sFilename - The filename, including path, of the input image
     * @param String a_sNewFilename - The filename, including path, of the output image
     * @param float a_fJpegQuality - The quality of the output Jpeg, from 0 to 1
     * @param int a_iHeight - The height of the output image
     */
    public static void scaleImageToHeight(String a_sFilename, 
                                          String a_sDestFilename,
                                          float a_fJpegQuality,
                                          int a_iHeight)
    throws Bn2Exception
    /*   
    --------------------------------------------------------------------------------
         14-Apr-2003    James Home          Created
         01-May-2003    James Home          Updated to call scaleImageToRectangle
    d1   14-Oct-2003    Matt Stevenson      Imported from HRHO
    --------------------------------------------------------------------------------
    */
    {
        scaleImageToRectangle(a_sFilename,a_sDestFilename,a_fJpegQuality,a_iHeight,Integer.MAX_VALUE,true);        
    }
    
    		


    
    
    
    /**
     * gets a dimension object for the image specified with the given url
     *
     * @param String a_sFilename - The filename, including path, of the input image
     * @return Dimension - the dimensions of the image at the given url
     */
    public static Dimension getDimensionsOfImage(String a_sFilename) throws Bn2Exception
    /*   
    --------------------------------------------------------------------------------
     d2  17-Oct-2003    Matt Stevenson      Created
    --------------------------------------------------------------------------------
    */
    {
        Dimension dimension = null;
        
        String[] sArgs = new String[] {"w", "h"};
        String[] dim = ImageMagick.getInfo(Arrays.asList(sArgs), a_sFilename);
        dimension = new Dimension(Integer.parseInt(dim[0]),Integer.parseInt(dim[1]));
        return (dimension);
    }
    
    
} 

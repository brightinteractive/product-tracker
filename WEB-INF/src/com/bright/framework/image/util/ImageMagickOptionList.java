package com.bright.framework.image.util;

import java.util.List;
import java.util.Vector;

/**
 * Bright Interactive, Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * ImageMagickCommand.java
 *
 * Configure options for an ImageMagick command.
 * 
 * @see http://www.imagemagick.org/script/command-line-options.php
 */

/*
 Ver	Date					Who					Comments
 --------------------------------------------------------------------------------
 d1	16-Oct-2006			Adam Bones			Created
 d2	01-Feb-2007			James Home			Updated watermark to use dissolve option
 d3	27-Mar-2007			Martin Wilson		Added addFlatten option
 d4 22-May-2007			Kevin Bennett		Imported from image bank
 --------------------------------------------------------------------------------
 */

/**
* 
 * @author Bright Interactive
 * @version d1
 */
public class ImageMagickOptionList implements Cloneable
{
	
	protected Vector m_options;
	
	public ImageMagickOptionList()
	{
		m_options = new Vector();
	}
	
	public void addFilename(String a_sFilename)
	{
		m_options.add(a_sFilename + "[0]"); // use only the first frame of multi-frame images
	}
	
	/**
	 * 
	 * @param a_iWidth
	 * @param a_iHeight
	 * @param a_sOption (see http://www.imagemagick.org/script/command-line-options.php#resize)
	 */
	public void addResize(int a_iWidth, int a_iHeight, String a_sOption)
	/*
	---------------------------------------------------------------
	d1		17-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		String sVal = "";
		
		if (a_iWidth>=0)
		{
			sVal+=a_iWidth;
		}		
		
		if (a_iHeight>=0)
		{
			sVal+="x"+a_iHeight;
		}
		
		if (a_sOption != null)
		{
			sVal += a_sOption;
		}
		
		add("-resize", sVal);		
	}
	
	public void addFormat(String a_sValue)
	{
		add("-format", a_sValue);
	}
	
	public void addWatermark(float a_fBrightness)
	{
		add("-dissolve", "%" + ((int) (a_fBrightness * 100)));
	}
	
	public void addGravity(String a_sValue)
	{
		add("-gravity", a_sValue);
	}
	
	public void addCompression(String a_sValue)
	{
		add("-compress", a_sValue);
	}
	
	public void addCrop(int a_iWidth, int a_iHeight, int a_iXOffset, int a_iYOffset)
	{
		add("-crop", a_iWidth + "x" + a_iHeight + "+" + a_iXOffset + "+" + a_iYOffset);
	}
	
	/**
	 * 
	 * @param a_fValue - a value between 0 and 1
	 */
	public void addQuality(float a_fValue)
	{
		add("-quality", Integer.toString((int) (a_fValue * 100)));
	}	
	
	public void addColorspace(String a_sValue)
	{
		add("-colorspace", a_sValue);
	}	
	
	public void addStrip()
	{
		add("-strip");
	}
	
	public void addFlatten()
	{
		add("-flatten");
	}	
	
	public void addDensity(int a_iVal)
	{
		String sVal = Integer.toString(a_iVal);
		add("-units", "PixelsPerInch");
		add("-density", sVal);
	}	
	
	public void addDensity(int a_iWidth, int a_iHeight)
	{
		String sVal = a_iWidth + "x" + a_iHeight;
		add("-density", sVal);
	}	
	
	public void addUnits(String a_sValue)
	{
		add("-units", a_sValue);
	}		
	
	public void addType(String a_sValue)
	{
		add("-type", a_sValue);
	}	
	
	public void addDepth(String a_sValue)
	{
		add("-depth", a_sValue);
	}	
	
	public void addMatte(boolean a_bMatte)
	{
		String sKey;
		if (a_bMatte)
		{
			sKey = "-matte";
		}
		else // remove matte:
		{
			sKey = "+matte";
		}
		addFirst(sKey);
	}
	
	public void addTransform()
	{
		add("-transform");
	}
	
	/**
	 * @param a_iAngle - degress of rotation 
	 */
	public void addRotateClockwise(int a_iAngle)
	{		
		double rads =  a_iAngle * (Math.PI / 180);
		addAffine((int) Math.cos(rads), (int) Math.sin(rads), (int) -Math.sin(rads), (int) Math.cos(rads), 0, 0);
		
		addTransform();
	}
	
	public void addAffine(int a_iSx, int a_iRx, int a_iRy, int a_iSy, int a_iTx, int a_iTy)
	{
		add("-affine", a_iSx + "," + a_iRx + "," + a_iRy + "," + a_iSy + "," + a_iTx + "," + a_iTy);
	}
	
	public void addCompress(String a_sValue)
	{
		add("-compress", a_sValue);
	}
	
	public void addToImageStack(ImageMagickOptionList a_options, String a_sFilename)
	{
		add("(");
		m_options.addAll(a_options.toList());
		add(a_sFilename);
		add(")");
	}
	
	public List toList()
	{
		return (List) m_options.clone();
	}
	
	public Object clone()
	{
		ImageMagickOptionList clone = new ImageMagickOptionList();
		clone.m_options = (Vector) m_options.clone();		
		return clone;
	}
	
	protected void add(String a_flag, String a_sVal)
	{
		add(a_flag);
		add(a_sVal);
	}
	
	protected void add(String a_flag)
	{
		m_options.add(a_flag);
	}
	
	protected void addFirst(String a_flag)
	{
		m_options.add(0, a_flag);
	}
}

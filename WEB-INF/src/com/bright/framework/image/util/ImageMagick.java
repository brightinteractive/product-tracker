package com.bright.framework.image.util;

import java.util.*;

import com.bn2web.common.service.GlobalApplication;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.image.exception.ImageException;
import com.bright.framework.util.CollectionUtil;
import com.bright.framework.util.commandline.CommandLineExec;

/**
 * Bright Interactive, Merchandise Manager
 *
 * Copyright 2006 Bright Interactive, All Rights Reserved.
 * ImageMagickCommand.java
 *
 * An interface to the ImageMagick commandline tools
 * 
 * @see ImageMagickOptionList
 */

/*
 Ver	Date					Who					Comments
 --------------------------------------------------------------------------------
 d1	16-Oct-2006			Adam Bones			Created
 d2	22-Nov-2006			Martin Wilson		Added execute override to suppress errors
 d3 22-May-2007			Kevin Bennett		Imported from image bank
 d4 16-Sep-2008    		Kevin Bennett     	Changed to get image magick path from settings
 --------------------------------------------------------------------------------
 */

/**
 * An interface to the ImageMagick commandline tools
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ImageMagick 
{

	// The full set of valid EXIF/IPTC keywords
	// (According to http://www.imagemagick.org/script/command-line-options.php#format)
	
	public static final String[] c_kEXIF_FIELDS = new String[]
	{
		"Artist",		
		"BitsPerSample",	
		"Compression",	
		"ColorSpace",
		"ComponentsConfiguration",
		"CompressedBitsPerPixel",	
		"Contrast",		
		"Copyright",	
		"CustomRendered",		
		"DateTime",	
		"DateTimeDigitized",
		"DateTimeOriginal",	
		"DigitalZoomRatio",			
		"ExifVersion",	
		"ExifImageLength",		
		"ExifImageWidth",
		"ExifOffset",
		"ExposureBiasValue",
		"ExposureMode",
		"ExposureProgram",	
		"ExposureTime",	
		"FileSource",			
		"Flash",			
		"FlashpixVersion",	
		"FNumber",
		"FocalLength",
		"GainControl",
		"JPEGInterchangeFormat",
		"JPEGInterchangeFormatLength",		
		"ImageDescription",		
		"ImageLength",		
		"ImageWidth",
		"ImageUniqueID",
		"InteroperabilityOffset",
		"InteroperabilityIndex",
		"InteroperabilityVersion",
		"ISOSpeedRatings",			
		"Orientation",	
		"LightSource",		
		"Make",
		"MakerNote",	
		"MaxApertureValue",
		"MeteringMode",			
		"Model",		
		"PhotometricInterpretation",
		"PixelXDimension",
		"PixelYDimension",		
		"PlanarConfiguration",	
		"PrimaryChromaticities",
		"PrintImageMatching",	
		"ReferenceBlackWhite",	
		"RelatedSoundFile",		
		"ResolutionUnit",	
		"RowsPerStrip",		
		"SamplesPerPixel",
		"Saturation",
		"SceneCaptureType",	
		"Sharpness",	
		"Software",		
		"StripByteCounts",	
		"StripOffsets",
		"SubSecTime",
		"SubSecTimeDigitized",		
		"SubSecTimeOriginal",	
		"Tainted",	
		"TransferFunction",	
		"UserComment",		
		"WhitePoint",
		"WhiteBalance",
		"WinXP-Author",		
		"WinXP-Comments",
		"WinXP-Keywords",
		"WinXP-Subject",		
		"WinXP-Title",
		"XResolution",
		"YCbCrCoefficients",		
		"YCbCrSubSampling",
		"YCbCrPositioning",
		"YResolution",
	};
	
	public static final String[] c_kIPTC_FIELDS = new String[]
	{
		// This record will crash ImageMagick! bizarre...
		//"Record Version",
	      "Object Type Reference",
	      "Object Name (Title)",
	      "Edit Status",
	      "Editorial Update",
	      "Urgency",
	      "Subject Reference",
	      "Category",
	      "Supplemental Category",
	      "Fixture Identifier",
	      "Keywords",
	      "Content Location Code",
	      "Content Location Name",
	      "Release Date",
	      "Release Time",
	      "Expiration Date",
	      "Expiration Time",
	      "Special Instructions",
	      "Action Advised",
	      "Reference Service",
	      "Reference Date",
	      "Reference Number",
	      "Date Created",
	      "Time Created",
	      "Digital Creation Date",
	      "Digital Creation Time",
	      "Originating Program",
	      "Program Version",
	      "Object Cycle",
	      "By-Line",
	      "By-Line Title",
	      "City",
	      "Sub-Location",
	      "Province/State",
	      "Country/Primary Location Code",
	      "Country/Primary Location Name",
	      "Original Transmission Reference",
	      "Headline",
	      "Credit",
	      "Source",
	      "Copyright Notice",
	      "Contact",
	      "Caption/Abstract",
	      "Caption Writer/Editor",
	      "Rasterized Caption",
	      "Image Type",
	      "Image Orientation",
	      "Language Identifier",
	      "Audio Type",
	      "Audio Sampling Rate",
	      "Audio Sampling Resolution",
	      "Audio Duration",
	      "Audio Outcue",
	      "ObjectData Preview File Format",
	      "ObjectData Preview File Format Version",
	      "ObjectData Preview Data"
	};
	
	public static final HashMap c_kIPTC_MAP = getIPTCMap(); 
	
	public final static int c_kiUndefinedColorspace = 0;
	public final static int c_kiRGBColorspace = 1;
	public final static int c_kiGRAYColorspace = 2;
	public final static int c_kiTransparentColorspace = 3;
	public final static int c_kiOHTAColorspace = 4;
	public final static int c_kiLABColorspace = 5;
	public final static int c_kiXYZColorspace = 6;
	public final static int c_kiYCbCrColorspace = 7;
	public final static int c_kiYCCColorspace = 8;
	public final static int c_kiYIQColorspace = 9;
	public final static int c_kiYPbPrColorspace = 10;
	public final static int c_kiYUVColorspace = 11;
	public final static int c_kiCMYKColorspace = 12;
	public final static int c_kisRGBColorspace = 13;
	public final static int c_kiHSBColorspace = 14;
	public final static int c_kiHSLColorspace = 15;
	public final static int c_kiHWBColorspace = 16;
	public final static int c_kiRec601LumaColorspace = 17;
	public final static int c_kiRec601YCbCrColorspace = 18;
	public final static int c_kiRec709LumaColorspace = 19;
	public final static int c_kiRec709YCbCrColorspace = 20;
	public final static int c_kiLogColorspace = 21;
	
	public static final String c_ksPixelsPerInch = "PixelsPerInch";
	public static final String c_ksPixelsPerCentimeter = "PixelsPerCentimeter";
	
	private static String c_ksPath = FrameworkSettings.getImageMagickPath();
	
	// A random delimeter to use to tokenize output from the commandline:
	// (note that using more than 3 characters may make some commands too long)
	private static final String c_ksDELIMETER = "-@-";
	
	/**
	 * Specify the path to the ImageMagick tools
	 * 
	 * @param a_sPath
	 */
	public static void setPath(String a_sPath)
	{
		c_ksPath = a_sPath;
	}
	
	public static void convert(ImageMagickOptionList a_options)
	throws ImageException
	/*
	---------------------------------------------------------------
	d2		22-Nov-2006		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		convert(a_options, false);
	}
	
	public static void convert(ImageMagickOptionList a_options,
										boolean a_bSuppressErrors)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		17-Oct-2006		Adam Bones		Created
	d2		22-Nov-2006		Martin Wilson	Added 	a_bSuppressErrors
	-----------------------------------------------------------------
	*/	
	{
		execute("convert", a_options, a_bSuppressErrors);
	}	
	
	public static void composite(ImageMagickOptionList a_options)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		17-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		execute("composite", a_options);
	}
	
	public static String identify(ImageMagickOptionList a_options)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		17-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		return execute("identify", a_options);
	}
	
	/**
	 * Return a map containing all EXIF data.
	 * 
	 * @return
	 * @throws ImageException
	 */
	public static HashMap getEXIFInfo(String a_sFilename)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		18-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		return getEXIFInfo(a_sFilename, Arrays.asList(c_kEXIF_FIELDS));
	}
	
	/**
	 * Return a map containing the EXIF values for the specified keywords.
	 * 
	 * @return
	 * @throws ImageException
	 */
	public static HashMap getEXIFInfo(String a_sFilename, Collection a_fields)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		18-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		// Get the EXIF values:
		String[] data = getMetaInfo("EXIF", a_fields, a_sFilename);
		
		// Build the map:
		return buildInfoMap(a_fields, Arrays.asList(data));
	}
	
	/**
	 * Return a map containing all IPTC data.
	 * 
	 * @return
	 * @throws ImageException
	 */
	public static HashMap getIPTCInfo(String a_sFilename)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		18-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		return getIPTCInfo(a_sFilename, Arrays.asList(c_kIPTC_FIELDS));
	}
	
	/**
	 * Return a map containing all IPTC data for specific fields.
	 * 
	 * @return
	 * @throws ImageException
	 */
	public static HashMap getIPTCInfo(String a_sFilename, Collection a_fields)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		18-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		// Map field names to IPTC record numbers:
		Collection records = new Vector();
		Iterator it = a_fields.iterator();
		while (it.hasNext())
		{
			records.add(c_kIPTC_MAP.get(it.next()));
		}
		
		// Get the IPTC values:
		String[] data = getMetaInfo("IPTC", records, a_sFilename);
		
		// Build the map:
		return buildInfoMap(a_fields, Arrays.asList(data));
	}
	
	/**
	 * 
	 * 
	 * @param a_tags
	 * @param a_sFilename
	 * @return
	 * @throws ImageException
	 */
	public static String[] getInfo(Collection a_tags, String a_sFilename)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		16-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		ImageMagickOptionList options = new ImageMagickOptionList();
		
		options.addFormat("%" + CollectionUtil.join(a_tags, c_ksDELIMETER + "%"));
		options.addFilename( a_sFilename);

		// Run and tokenize the output with delimeter:
		return identify(options).split(c_ksDELIMETER);
	}
	
	public static int parseColorspace(String a_sColorspace)
	{
		// strip the object name:
		if (a_sColorspace.startsWith("DirectClass"))
		{
			a_sColorspace = a_sColorspace.substring(11);
		}
		else if (a_sColorspace.startsWith("SudoClass"))
		{
			a_sColorspace = a_sColorspace.substring(9);
		}
		
		int iColorspace = -1;
		
		if (a_sColorspace.equals("Undefined"))
		{
			iColorspace = c_kiUndefinedColorspace;
		}
		else if (a_sColorspace.equals("RGB"))
		{
			iColorspace = c_kiRGBColorspace;
		}
		else if (a_sColorspace.equals("GRAY"))
		{
			iColorspace = c_kiGRAYColorspace;
		}
		else if (a_sColorspace.equals("Transparent"))
		{
			iColorspace = c_kiTransparentColorspace;
		}
		else if (a_sColorspace.equals("OHTA"))
		{
			iColorspace = c_kiOHTAColorspace;
		}
		else if (a_sColorspace.equals("LAB"))
		{
			iColorspace = c_kiLABColorspace;
		}
		else if (a_sColorspace.equals("XYZ"))
		{
			iColorspace = c_kiXYZColorspace;
		}
		else if (a_sColorspace.equals("YCbCr"))
		{
			iColorspace = c_kiYCbCrColorspace;
		}
		else if (a_sColorspace.equals("YUV"))
		{
			iColorspace = c_kiYUVColorspace;
		}
		else if (a_sColorspace.equals("CMYK"))
		{
			iColorspace = c_kiCMYKColorspace;
		}
		else if (a_sColorspace.equals("sRGB"))
		{
			iColorspace = c_kisRGBColorspace;
		}
		else if (a_sColorspace.equals("CYMK"))
		{
			iColorspace = c_kiCMYKColorspace;
		}
		else if (a_sColorspace.equals("HSBC"))
		{
			iColorspace = c_kiHSBColorspace;
		}
		else if (a_sColorspace.equals("HSL"))
		{
			iColorspace = c_kiHSLColorspace;
		}
		else if (a_sColorspace.equals("HWB"))
		{
			iColorspace = c_kiHWBColorspace;
		}
		else if (a_sColorspace.equals("Rec601Luma"))
		{
			iColorspace = c_kiRec601LumaColorspace;
		}
		else if (a_sColorspace.equals("Rec601YCbCr"))
		{
			iColorspace = c_kiRec601YCbCrColorspace;
		}
		else if (a_sColorspace.equals("Rec709Luma"))
		{
			iColorspace = c_kiRec709LumaColorspace;
		}
		else if (a_sColorspace.equals("ec709YCbCr"))
		{
			iColorspace = c_kiRec709YCbCrColorspace;
		}
		else if (a_sColorspace.equals("Log"))
		{
			iColorspace = c_kiLogColorspace;
		}
	
		return iColorspace;
	}
	
	/**
	 * Run an ImageMagick command, using the path specified by ImageMagick.setPath.
	 * 
	 * Any errors reported by ImageMagick generate ImageExceptions
	 * 
	 * @param a_sName
	 * @param a_options
	 * @param a_files
	 * @return
	 * @throws ImageException
	 */
	private static String execute(String a_sName, 
											ImageMagickOptionList a_options)
	throws ImageException
	/*
	---------------------------------------------------------------
	d2		22-Nov-2006			Martin Wilson		Added execute override to suppress errors
	-----------------------------------------------------------------
	*/	
	{
		return (execute(a_sName, 
							 a_options,
							 false));
	}
	
	/**
	 * Run an ImageMagick command, using the path specified by ImageMagick.setPath.
	 * 
	 * Any errors reported by ImageMagick generate ImageExceptions, unless a_bSuppressErrors
	 * is true
	 * 
	 * @param a_sName
	 * @param a_options
	 * @param a_files
	 * @return
	 * @throws ImageException
	 */
	private static String execute(String a_sName, 
											ImageMagickOptionList a_options,
											boolean a_bSuppressErrors)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		16-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		StringBuffer sbOut = new StringBuffer();
		
		// Build the command as a list:
		List cmd = a_options.toList();

		// Use an absolute path to the IM tool if we have one:
		if (c_ksPath != null)
		{
			a_sName = c_ksPath + "/" + a_sName; 
		}
		
		cmd.add(0, a_sName);
	
		String[] aCmd = new String[cmd.size()];
		for (int i=0; i<aCmd.length; i++)
		{
			String s = (String) cmd.get(i);
			aCmd[i] = s;
		}

		// Build the command as a string for debug output:
		String sCmd = CollectionUtil.join(cmd, " ");
		
		// Generate some debug ouput
		GlobalApplication.getInstance ().getLogger().debug("ImageMagick.execute: about to run command " + sCmd);
		
		// Run the process:
		int iCode = -1;

		try
		{		
			StringBuffer sbErrors = new StringBuffer();
			iCode = CommandLineExec.execute(aCmd,  sbOut, sbErrors);
			
			if (iCode != 0 && !a_bSuppressErrors)
			{
				throw new Exception("Returned code: " + iCode + ", error output: " + sbErrors.toString());
			}
		}
		catch (Exception e)
		{
			throw new ImageException("ImageMagick.execute: " + sCmd + " : " + e.getMessage());
		}
		
		return sbOut.toString();
	}
	
	/**
	 * @param a_tags
	 * @param a_sFilename
	 * @return
	 * @throws ImageException
	 */
	private static String[] getMetaInfo(String a_sType, Collection a_tags, String a_sFilename)
	throws ImageException
	/*
	---------------------------------------------------------------
	d1		19-Oct-2006		Adam Bones		Created 
	-----------------------------------------------------------------
	*/	
	{
		Collection tags = new Vector();
		Iterator it = a_tags.iterator();
		
		// Build the tag list list:
		while (it.hasNext())
		{
			tags.add("[" + a_sType + ":" + it.next() + "]");
		}

		return getInfo(tags, a_sFilename);
	}
	
	/**
	 * A build a map: use fields as keys and skip entries with empty values.
	 *  
	 * @param a_fields
	 * @param a_values
	 * @return
	 */
	private static HashMap buildInfoMap(Collection a_fields, Collection a_values)
	{
		HashMap map = new HashMap();
		
		Iterator itF = a_fields.iterator();
		Iterator itV = a_values.iterator();
		
		String sField;
		String sValue;
		
		while (itF.hasNext() && itV.hasNext())
		{
			sField = (String) itF.next();
			sValue = (String) itV.next();
			
			if (sValue != null && !sValue.equals("") && !sValue.equals("unknown"))
			{
				// Clean up if a WinXP field:
				if (sField.startsWith("WinXP-"))
				{
					sValue = cleanupWinXPField(sValue);
				}						
				
				map.put(sField, sValue);
			}
		}

		return map;
	}
	
	/**
	 * Build the IPTC-field-name-to-record-map.
	 * 
	 * Values according to http://www.imagemagick.org/script/command-line-options.php#format
	 * @return
	 */
	private static HashMap getIPTCMap()
	{		
		HashMap map = new HashMap();
		
		//map.put("Record Version", "2:00");
		map.put("Object Type Reference", "2:03");
		map.put("Object Name (Title)", "2:05");
		map.put("Edit Status", "2:07");
		map.put("Editorial Update", "2:08");
		map.put("Urgency", "2:10");
		map.put("Subject Reference", "2:12");
		map.put("Category", "2:15");
		map.put("Supplemental Category", "2:20");
		map.put("Fixture Identifier", "2:22");
		map.put("Keywords", "2:25");
		map.put("Content Location Code", "2:26");
		map.put("Content Location Name", "2:27");
		map.put("Release Date", "2:30");
		map.put("Release Time", "2:35");
		map.put("Expiration Date", "2:37");
		map.put("Expiration Time", "2:35");
		map.put("Special Instructions", "2:40");
		map.put("Action Advised", "2:42");
		map.put("Reference Service", "2:45");
		map.put("Reference Date", "2:47");
		map.put("Reference Number", "2:50");
		map.put("Date Created", "2:255");
		map.put("Time Created", "2:60");
		map.put("Digital Creation Date", "2:62");
		map.put("Digital Creation Time", "2:63");
		map.put("Originating Program", "2:65");
		map.put("Program Version", "2:70");
		map.put("Object Cycle", "2:75");
		map.put("By-Line", "2:80");
		map.put("By-Line Title", "2:85");
		map.put("City", "2:90");
		map.put("Sub-Location", "2:92");
		map.put("Province/State", "2:95");
		map.put("Country/Primary Location Code", "2:100");
		map.put("Country/Primary Location Name", "2:101");
		map.put("Original Transmission Reference", "2:103");
		map.put("Headline", "2:105");
		map.put("Credit", "2:110");
		map.put("Source", "2:115");
		map.put("Copyright Notice", "2:116");
		map.put("Contact", "2:118");
		map.put("Caption/Abstract", "2:120");
		map.put("Caption Writer/Editor", "2:122");
		map.put("Rasterized Caption", "2:125");
		map.put("Image Type", "2:130");
		map.put("Image Orientation", "2:131");
		map.put("Language Identifier", "2:135");
		map.put("Audio Type", "2:150");
		map.put("Audio Sampling Rate", "2:151");
		map.put("Audio Sampling Resolution", "2:152");
		map.put("Audio Duration", "2:153");
		map.put("Audio Outcue", "2:154");		
		map.put("ObjectData Preview File Format", "2:200");		
		map.put("ObjectData Preview File Format Version", "2:201");			
		map.put("ObjectData Preview Data", "2:202");
		
		return map;
	}
	
	/**
	 * Takes out all the . characters that get added for the WinXP exif fields.
	 * Does this by using only the odd characters
	 * 
	 * @param a_sValue
	 * @return
	 */
	public static String cleanupWinXPField(String a_sValue)
	/*
	---------------------------------------------------------------
	d2		12-Jun-2006		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/	
	{
		if (a_sValue == null || a_sValue.length()==0)
		{
			return (a_sValue);
		}
	
		StringBuffer sbRetVal = new StringBuffer();
		
		// Go through the string - miss off the last 2 chars as well:
		for (int i=0; i< a_sValue.length()-2; i++)
		{
			// Only use if odd
			if (i % 2 == 0)
			{
				sbRetVal.append(a_sValue.charAt(i));
			}			
		}
		
		return (sbRetVal.toString());
	}	
}

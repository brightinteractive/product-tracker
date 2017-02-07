package com.bright.framework.util;

/**
 * Bright Interactive, news
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * FileUtil.java
 *
 * Contains the FileUtil class.
 */
/*
Ver  Date               Who			Comments
--------------------------------------------------------------------------------
d1   28-Mar-2003         Martin Wilson           Created for HRHO.
d2   07-Apr-2003         Matt Stevenson          Removed references to hrho constants
d3   29-Mar-2004         Martin Wilson           Added readIntoStringBuffer.
d3   07-May-2004         James Home              Added readIntoStringBuffer with encoding arg.
d5   14-May-2004         MartinWilson            Added createFileAndItsDirectories
--------------------------------------------------------------------------------
 */

import java.io.*;

import com.bn2web.common.exception.Bn2Exception;

/**
 * Contains useful utility methods for files.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class FileUtil
{
   // Get the file separator:
   static char k_cDirSep = System.getProperty("file.separator").charAt(0);
   private static final int k_iBlocksize = 1024;
   private static final String k_sPathSepRE =  "/|\\\\";

    /*
     * Returns a path in standard form - i.e. with separator as '/'.
     *
     *@param String a_sOriginalPath
     */
   public static String getStandardisedPath(String a_sOriginalPath)
    /*
    ------------------------------------------------------------------------
     d1   28-Mar-2003  Martin Wilson		Created.
    ------------------------------------------------------------------------
     */
   {
      // Avoid npe:
      if (a_sOriginalPath == null)
      {
         return (null);
      }

      return (a_sOriginalPath.replace(k_cDirSep, '/'));
   }

    /*
     * Returns the just the filename, stripping off the path.
     * NOTE: assumes the path separator is /.
     *
     *@param String a_sFullPath
     */
   public static String getFilename(String a_sFullPath)
    /*
    ------------------------------------------------------------------------
     d1   28-Mar-2003  Martin Wilson		Created.
    ------------------------------------------------------------------------
     */
   {
      // Avoid npe:
      if (a_sFullPath == null)
      {
         return (null);
      }

      String sFilename = null;

      // Get the suffix of this file:
      int iLastIndex = a_sFullPath.lastIndexOf('/');

      if(iLastIndex > 0 &&  iLastIndex < a_sFullPath.length() - 1)
      {
         sFilename = a_sFullPath.substring(iLastIndex + 1);
      }

      return (sFilename);
   }

    /*
     * Returns the suffix of a filename.
     *
     *@param String a_sFilename
     */
   public static String getSuffix(String a_sFilename)
    /*
    ------------------------------------------------------------------------
     d1   28-Mar-2003  Martin Wilson		Created.
    ------------------------------------------------------------------------
     */
   {
      // Avoid npe:
      if (a_sFilename == null)
      {
         return (null);
      }

      String sSuffix = null;

      // Get the suffix of this file:
      int iLastIndex = a_sFilename.lastIndexOf('.');

      if(iLastIndex > 0 &&  iLastIndex < a_sFilename.length() - 1)
      {
         sSuffix = a_sFilename.substring(iLastIndex + 1).toLowerCase();
      }

      return (sSuffix);
   }

    /*
     * Returns the filename without the suffix.
     *
     *@param String a_sFilename
     */
   public static String getFilenameWithoutSuffix(String a_sFilename)
    /*
    ------------------------------------------------------------------------
     d1   28-Mar-2003  Martin Wilson		Created.
    ------------------------------------------------------------------------
    */
   {
      // Avoid npe:
      if (a_sFilename == null)
      {
         return (null);
      }

      String sStartOfFilename = null;

      // Get the index of the . before the suffix of this file:
      int iLastIndex = a_sFilename.lastIndexOf('.');

      if(iLastIndex > 0 &&  iLastIndex < a_sFilename.length() - 1)
      {
         sStartOfFilename = a_sFilename.substring(0, iLastIndex);
      }

      return (sStartOfFilename);
   }

   /**
    * Returns a filename-safe version of the passed string, replacing illegal
    * characters with underscore
    *
    * Only replaces spaces with underscores if a_bReplaceSpaces is true
    */
   public static String getSafeFilename(String a_sFilenameCandidate, boolean a_bReplaceSpaces)
   /*
    ------------------------------------------------------------------------
     d1   21-Nov-2003  James Home		Created.
    ------------------------------------------------------------------------
    */
   {
      if(a_sFilenameCandidate!=null)
      {
         a_sFilenameCandidate = a_sFilenameCandidate.replace('<','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('>','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace(':','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('/','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('\\','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('\'','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('"','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('`','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('*','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('?','_');
         a_sFilenameCandidate = a_sFilenameCandidate.replace('|','_');

         if(a_bReplaceSpaces)
         {
            a_sFilenameCandidate = a_sFilenameCandidate.replace(' ','_');
         }
      }

      return a_sFilenameCandidate;
   }

    /*
     * Copies a file.
     *
     *@param String a_sFilename
     */
   public static void copyFile(String a_sSrc, String a_sDest) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   28-Mar-2003  Martin Wilson		Created.
    ------------------------------------------------------------------------
     */
   {
      try
      {
         InputStream is = new FileInputStream(a_sSrc);
         OutputStream os = new FileOutputStream(a_sDest);

         byte[] buffer = new byte[0xffff]; // buffer size = FFFF hex
         int nbytes;

         // Copy all the bytes:
         while ((nbytes = is.read(buffer)) != -1)
         {
            os.write(buffer, 0, nbytes);
         }

         is.close();
         os.close();
      }
      catch (IOException ioe)
      {
         throw new Bn2Exception("Exception in FileUtil.copyFile:", ioe);
      }
   }

   /**
    * Recursively check the folder a_sDir and returns true if one or more files have
    * been changed since a_lDateInMillis
    *
    * @param String a_sDir - the directory to check
    * @param long a_lDate - the date
    *
    * @throws Bn2Exception - on error.
    */
    public static boolean filesChanged(File a_fDirectory, long a_lLastChecked)
    /*
    ------------------------------------------------------------------------
    d1   11-Jun-2003    Martin Wilson        Created.
    ------------------------------------------------------------------------
    */
    {
        boolean bSomethingChanged = false;

        // List all the files (or sub directories):
        File[] aFiles = a_fDirectory.listFiles();

        if (aFiles == null)
        {
            return false;
        }

        // Go through each (while nothing has changed):
        for (int iFilenamePos=0; ( (iFilenamePos < aFiles.length) && (!bSomethingChanged)); iFilenamePos++)
        {
            File currentFile = aFiles[iFilenamePos];

            // If this is a directory then recursively get the files:
            if (currentFile.isDirectory())
            {
                bSomethingChanged = filesChanged(currentFile, a_lLastChecked);
            }
            else
            {
                if (currentFile.lastModified() > a_lLastChecked)
                {
                    bSomethingChanged = true;
                }
            }
        }

        return (bSomethingChanged);
    }

   /**
    * Reads the contents of a file into a StringBuffer
    *
    * @param File a_fFile - the file
    * @return StringBuffer - the buffer.
    *
    * @throws Bn2Exception - on error.
    */
    public static StringBuffer readIntoStringBuffer(String a_sFileLocation, String a_sCharEncoding) throws IOException
    /*
    ------------------------------------------------------------------------
    d3   29-Mar-2004    Martin Wilson        Created.
    ------------------------------------------------------------------------
    */
    {
       Reader rFile = null;
       StringBuffer sbFile = new StringBuffer();
       int iCharsRead = 0;
       char[] cbuf = new char[k_iBlocksize];

       if(a_sCharEncoding==null)
       {
         rFile = new FileReader(a_sFileLocation);
       }
       else
       {
          rFile =  new InputStreamReader(new FileInputStream(a_sFileLocation),a_sCharEncoding);


       }

        // Read the content of the file into a StringBuffer
       while( (iCharsRead = rFile.read(cbuf,0,k_iBlocksize)) != -1)
       {
          sbFile.append(cbuf, 0, iCharsRead);
       }

       rFile.close();

       return (sbFile);
    }

    /**
    * Reads the contents of a file into a StringBuffer
    *
    * @param File a_fFile - the file
    * @return StringBuffer - the buffer.
    *
    * @throws Bn2Exception - on error.
    */
    public static StringBuffer readIntoStringBuffer(String a_sFileLocation) throws IOException
    /*
    ------------------------------------------------------------------------
    d4   07-May-2004    James Home        Created.
    ------------------------------------------------------------------------
    */
    {
       return readIntoStringBuffer(a_sFileLocation,null);
    }

    /**
    * Goes through a_sFilePath, a path which can contain directory names and file separators
    * and then end with the filename, and creates all the directories necessary to create the file.
    *
    * @param String a_sStartingDir
    * @return String a_sFilePath.
    *
    * @throws Bn2Exception - on error.
    */
    public static void createFileAndItsDirectories( String a_sStartingDir,
                                                    String a_sFilePath) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
    d4   14-May-2004    Martin Wilson        Created.
    ------------------------------------------------------------------------
    */
    {
        try
        {
            // Split the path into directories and the filename:
            String[] aTokens = a_sFilePath.split(k_sPathSepRE);

            // Start in the current dir:
            String sCurrentDir = a_sStartingDir;

            // All but the last will be directories, so loop through length-1.
            // For each, see if the dir exists and create it if not.
            for (int i=0; i < aTokens.length-1; i++)
            {
                // Create the full path to the dir (add a \ if not there already):
                if (!sCurrentDir.endsWith("/"))
                {
                    sCurrentDir += "/";
                }

                sCurrentDir += aTokens[i];

                // See if the directory exists already:
                File fDir = new File(sCurrentDir);

                if (!fDir.exists())
                {
                    fDir.mkdir();
                }
            }

            // Get the full path of the actual file:
            String sFullFilePath = a_sStartingDir + "/" + a_sFilePath;

            // Now create the file:
            File fNewFile = new File(sFullFilePath);
            fNewFile.createNewFile();
        }
        catch (IOException ioe)
        {
            throw new Bn2Exception("IOException in FileUtil.createFileAndItsDirectories:", ioe);
        }
    }
    
    
    /**
	 * Decrypts a file path that was encrypted using encryptFilepath
	 * 
	 * @param a_sFilepath
	 * @return
	 */	
	public static String decryptFilepath(String a_sFilepath)
	/*
	---------------------------------------------------------------
	d6		20-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sResult = null;
		String sFilepathNoSuffix = null;
		String sSuffix = null;
		
		if(a_sFilepath!=null)
		{
			sFilepathNoSuffix = getFilenameWithoutSuffix(a_sFilepath);
			sSuffix = getSuffix(a_sFilepath);
		
			try
			{
				sResult = RC4CipherUtil.decryptFromHex(sFilepathNoSuffix);
				
				if(sSuffix!=null)
				{
					sResult += "." + sSuffix;
				}
			}
			catch(NumberFormatException nfe)
			{
				// If the hex is badly formed we cannot decrypt the filepath
				sResult = a_sFilepath;
			}
		}
				
		return sResult;
	}
	
	
	/**
	 * Encrypts a file path using RC4CipherUtil.encryptToHex() on all but the 
	 * file suffix.
	 * 
	 * @param a_sFilepath
	 * @return
	 */
	public static String encryptFilepath(String sFilepath)
	/*
	---------------------------------------------------------------
	d6		20-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sResult = null;
		String sFilepathNoSuffix = null;
		String sSuffix = null;
		
		if(sFilepath!=null)
		{
			sFilepathNoSuffix = getFilenameWithoutSuffix(sFilepath);
			sSuffix = getSuffix(sFilepath);
		
			sResult = RC4CipherUtil.encryptToHex(sFilepathNoSuffix);
			
			if(sSuffix!=null)
			{
				sResult += "." + sSuffix;
			}
		}
				
		return sResult;
	}

}


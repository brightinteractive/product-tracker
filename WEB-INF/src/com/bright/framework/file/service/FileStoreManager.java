package com.bright.framework.file.service;


/**
 * bright interactive, FileManager
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * FileManager.java
 * 
 *
 */
/*
Ver  Date	        Who		    Comments
--------------------------------------------------------------------------------
d1  08-Oct-2003         Matt Stevenson        Created.
d2  13-Oct-2003         Matt Stevenson        Initial Implementation work
d3  14-Oct-2003         Matt Stevenson        Minor changes
d4  17-Oct-2003         Matt Stevenson        Modified getUniqueFilename method
                                              Fixed other problems
d5  29-Oct-2003         Matt Stevenson        Added call to initialise stored-images
                                              and downloads directory
d6  16-Jan-2004         Matt Stevenson        Imported from image manager
d7  17-Jan-2004         Martin Wilson         Moved into 'file' package.
d8  19-Jan-2004         Matt Stevenson        Changes based on directory restructuring
d9	 15-Oct-2004			Matt Stevenson			 Imported into rt framework
d10 20-Jan-2004			James Home				 Added createNewFileCopy
d11 07-Feb-2005			Martin Wilson			 Added relativeFileStoreRoot property
--------------------------------------------------------------------------------
*/

import java.io.*;

import org.apache.struts.upload.FormFile;

import com.bn2web.common.constant.GlobalSettings;
import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.file.bean.BFile;
import com.bright.framework.file.constant.FileConstants;
import com.bright.framework.util.FileUtil;

 
/**
 *
 * FileManager
 *
 * @author  Bright Interactive
 * @version d1
 */
public class FileStoreManager extends Bn2Manager implements FileConstants
{
	
    private String m_sFileStoreRoot = null;
    private String m_sRelativeFileStoreRoot = null;    
    private String[] m_aStorageDirList = null;
    private int m_iNextDirectoryIndex = 0;
    private int m_iDirCount = 0;
    private Object m_oUniqueFilenameLock = new Object();
              
    /**
      * Initialise this component - check it has been composed and configured
      * and that it hasn't been disposed.
      * 
      * @param  
      * @return void
      * @throws Exception
      */
    public void initialize() throws Exception
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d2   13-Oct-2003   Matt Stevenson    Imported initialise method
                                                from HRHO - setup so 
                                                directories are always relative and
                                                no need for code that deletes temp files
                                                d3   17-Oct-2003   Matt Stevenson    Removed ref to relative roots
     d11  07-Feb-2005	Martin Wilson		Set relativeFileStoreRoot property     
    ------------------------------------------------------------------------
    */
    {
        super.initialize();
        
        // Keep the relative file store root (m_sFileStoreRoot is set as a property in 
        // components.xconf):
        m_sRelativeFileStoreRoot = m_sFileStoreRoot;
        
        // Add the app path to the paths: (m_sFileStoreRoot is set in conmponents.xconf)
        m_sFileStoreRoot = GlobalSettings.getApplicationPath() + m_sFileStoreRoot;      
        
        // Set up the subdirectories to use:
        initialiseStorageDirectories();
    }   
    
    
    
    /*
    * Checks to see if all the subdirectories to be used for storing the documents exist: if not,
    * creates them. Sets up an array to be used to determine which subdirectory a new doc will 
    * be stored in.
    * 
    */  
    public void initialiseStorageDirectories() throws Bn2Exception 
    /*
    ------------------------------------------------------------------------
            01-Mar-2003  Martin Wilson    Created.
     d2     13-Oct-2003  Matt Stevenson   Imported from HRHO
     d5     29-Oct-2003  Matt Stevenson   Added code to setup stored-images dir and
                                          downloads dir.
    ------------------------------------------------------------------------
    */    
    {
        // Go to the doc root for the DMS:
        File fileRootDir = new File(m_sFileStoreRoot);
        
        // Check it exists:
        if (!fileRootDir.exists())
        {
            m_logger.debug("Unable to get source list - Root dir ("+fileRootDir+") doesn't exist : Creating directory...");
            fileRootDir.mkdir();
            //throw new Bn2Exception("Unable to get source list - Root dir ("+fileRootDir+") doesn't exist");
        }                    
        
        if (m_iDirCount <=0)
        {
            m_logger.error("FileStoreManager.initialiseStorageDirectories: the number of subdirectories is 0. Make sure you specify a value for the number of directories in web.xml");
            throw new Bn2Exception("FileStoreManager.initialiseStorageDirectories: the number of subdirectories is 0. Make sure you specify a value for the number of directories in web.xml");            
        }
         
        // Create the list of subdirectories:
        m_aStorageDirList = new String[m_iDirCount];
        m_iNextDirectoryIndex = 0;
        
        for (int i=0; i < m_iDirCount; i++)
        {
            // Just call the directories numbers from 0 to count:
            m_aStorageDirList[i] = Integer.toString(i);
            
            // See if it exists:
            String sFullPath = m_sFileStoreRoot + "/" + m_aStorageDirList[i];
            File fStorageDir = new File(sFullPath);
            
            if (!fStorageDir.exists())
            {
                // Create the directory:
                fStorageDir.mkdir();
            }
        }
        
        //finally create the downloads directory - if it doesn't exist...
        File fDownloadDirectory = new File(m_sFileStoreRoot + "/" + k_sDownloadsDirectory);
        
        if (!fDownloadDirectory.exists())
        {
            //create the dir...
            fDownloadDirectory.mkdir();
        }
    }
    
    
    /*
    * 
    *@return String - the name of the storage directory to use when storing a new document. 
    */  
    public synchronized String getAStorageDirectory()
    /*
    ------------------------------------------------------------------------
          01-Apr-2003  Martin Wilson     Created
     d1   13-Oct-2003  Matt Stevenson    Imported from HRHO.
    ------------------------------------------------------------------------
    */    
    {
        String sDir = m_aStorageDirList[m_iNextDirectoryIndex];
        
        // Increment, so we cycle through them all:
        m_iNextDirectoryIndex++;
        
        // Check we're not back at the beginning:
        if (m_iNextDirectoryIndex >= m_iDirCount)
        {
            m_iNextDirectoryIndex = 0;
        }
        
        return (sDir);
    }
    
    
    
   /*
    * Saves a new file to the document management file system.
    * 
    * @param FormFile a_ffFile the file.
    * @param long a_lFileID = the id of the file to add
    * @return BFile - a representation of the file (contains the location / url 
    * of the file
    *
    */  
    public BFile addFile(FormFile a_ffDocFile) throws Bn2Exception 
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d2   13-Oct-2003   Matt Stevenson          Imported / modified code from HRHO
     d3   14-Oct-2003   Matt Stevenson          Minor changes
     d6   16-Jan-2004   Matt Stevenson          Imported and changed from image manager
    ------------------------------------------------------------------------
    */    
    {   
        BFile imFile = null;
        String sRelativePath = null;
        
        synchronized(m_oUniqueFilenameLock)
        {        
            // Get a unique name in the dms filestore
            sRelativePath = getUniqueFilename(a_ffDocFile.getFileName());

            //now call the method that actually uploads the file
            imFile = saveFile(sRelativePath, a_ffDocFile);        
        }
        
        return (imFile);
    }
    
    
    /**
     * Returns a relative filename that is unique in the dms filestore, based on
     * the original filename passed as a parameter.
     */
    public String getUniqueFilename(String a_sFilename)
    /*
    ------------------------------------------------------------------------
     d4   17-Oct-2003  Matt Stevenson   Created to deal with requests for unique
                                        filenames that don't specify a directory
    ------------------------------------------------------------------------
    */
    {
        return getUniqueFilename(a_sFilename, getAStorageDirectory());
    }
        
        
        
        
    /**
     * Returns a relative filename that is unique in the dms filestore, based on
     * the original filename passed as a parameter.
     */
    public String getUniqueFilename(String a_sFilename, String a_sStorageDirectory)
    /*
    ------------------------------------------------------------------------
          16-Apr-2003  James Home       Created from previous addFile() method
     d2   13-Oct-2003  Matt Stevenson   Imported code from HRHO 
     d4   17-Oct-2003  Matt Stevenson   Changed to incorporate a given directory 
    ------------------------------------------------------------------------
    */
    {
        // Build the filename/path for this document:
        boolean bFoundUniqueFilename = false;
        String sRelativePath = null;        
        int iFileCount = 0;
        String sFilename = null;
        String sNewDocPath = null;        
        
        sFilename = a_sFilename;
        
        while (!bFoundUniqueFilename)
        {            
            // Build the relative path:
            sRelativePath =  a_sStorageDirectory + "/" + sFilename;       

            sNewDocPath = m_sFileStoreRoot + "/" + sRelativePath;
            
            // Check that this file doesn't already exist in the DMS:
            File fTemp = new File(sNewDocPath);

            if (fTemp.exists())
            {
                // The following count is to try filenames based on a number added to the original name:
                iFileCount++;

                // Create a new filename:
                String sStartOfFilename = FileUtil.getFilenameWithoutSuffix(a_sFilename);
                String sSuffix = FileUtil.getSuffix(a_sFilename); 
                sFilename = "";

                // Add the start of the filename (if not null - might have files called .something!)
                if (sStartOfFilename != null)
                {
                    sFilename = sStartOfFilename;
                }

                // Add a number:
                sFilename += iFileCount;

                // Add the suffix if not null:
                if (sSuffix != null)
                {
                    sFilename += "." + sSuffix;
                }
            }
            else
            {
                bFoundUniqueFilename = true;
            }            
        }

        return sRelativePath;
    }
    
    
    
    /*
    * Saves a file that's been uploaded to the document management file system.
    * 
    *@param String a_sUrl the url to upload to.
    *@param FormFile a_ffDocFile the file.
    *@param int a_lFileId - the id of the image added
    *@return the file object
    */  
    public BFile saveFile(String a_sUrl, FormFile a_ffDocFile) throws Bn2Exception 
    /*
    ------------------------------------------------------------------------
          08-Apr-2003  Martin Wilson		Created.
     d2   13-Oct-2003  Matt Stevenson           Imported from HRHO
     d3   14-Oct-2003  Matt Stevenson           Removed id references
     d6   16-Jan-2004  Matt Stevenson           Imported and changed from image manager
    ------------------------------------------------------------------------
    */    
    {    
    
        // Retrieve the file name:
        String sOriginalFileName= a_ffDocFile.getFileName();

        m_logger.debug("FileStoreManager.saveFile: filename of uploaded file = " + sOriginalFileName);

        if (sOriginalFileName == null || sOriginalFileName.length() == 0)
        {
            throw new Bn2Exception("You must select a file to upload.");
        }             
        
        if (a_ffDocFile.getFileSize() == 0)
        {
            // Throw an exception:
            throw new Bn2Exception("The document is empty");
            
        }

        // Construct the path to the file, whether new or not:
        String sCompleteFilePath = m_sFileStoreRoot + "/" + a_sUrl;
        
        m_logger.debug("filename in DMS = " + sCompleteFilePath);        

        try
        {
            // Retrieve the file data:
            InputStream stream = a_ffDocFile.getInputStream();

            // Write the file to the right place:
            OutputStream bos = new FileOutputStream(sCompleteFilePath);
            int iBytesRead = 0;
            byte[] buffer = new byte[k_iBytesAtATime];

            // Read the bytes in in chunks of k_iBytesAtATime:
            while ((iBytesRead = stream.read(buffer, 0, k_iBytesAtATime)) != -1) 
            {
                // Write to the new file:
                bos.write(buffer, 0, iBytesRead);
            }

            bos.close();

            // Close the stream:
            stream.close();
            
            //finally construct a new file object 
            BFile bFile = new BFile();
            bFile.setUrl(a_sUrl);
            
            return bFile;
        }
        catch (IOException ioe)
        {
            throw (new Bn2Exception(ioe.getMessage()));
        }             
    }
    
    
    
    /*
    * Updates a file on the file system.
    * 
    *@param FormFile a_ffFile the new file to use.
    *@param long a_lFileId - the id of the file to update
    *@return BFile - the updated file
    *
    */  
    public BFile updateFile(String a_sUrl, FormFile a_ffDocFile) throws Bn2Exception 
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d2   13-Oct-2003   Matt Stevenson          Imported code from HRHO 
     d3   14-Oct-2003   Matt Stevenson          Removed refs to id
     d6   16-Jan-2004   Matt Stevenson          Imported and changed from image manager
    ------------------------------------------------------------------------
    */    
    {        
        //use the save file method to do the actual work of saving
        BFile bFile = saveFile(a_sUrl, a_ffDocFile);
        
        return (bFile);
    }
    
    
    
    /*
    * deletes a file from the file system.
    * 
    *@param long a_lFileId - the id of the file to remove
    *
    */  
    public void deleteFile(String a_sFileToDeleteUrl)
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d2   13-Oct-2003   Matt Stevenson          Imported from HRHO
    ------------------------------------------------------------------------
    */    
    {   
        // Delete the file:
        File fToDelete = new File(m_sFileStoreRoot + "/" + a_sFileToDeleteUrl);
        fToDelete.delete();
    }
    
    
    
    /*
     * Sets the root directory of the file store
     *
     * @param String a_sRootDir - the root directory of the file store
     */    
    public void setFileStoreRoot(String a_sRootDir)    
    /*
    ------------------------------------------------------------------------
          14-Apr-2003  James Home	       Created.    
    d2    13-Oct-2003  Matt Stevenson          Imported from HRHO
    ------------------------------------------------------------------------
    */
    {
        m_sFileStoreRoot = a_sRootDir;           
    }
    
   
    
    /*
     * gets the root directory of the file store
     *
     * @return String - the root directory of the file store
     */    
    public String getFileStoreRoot()    
    /*
    ------------------------------------------------------------------------
          14-Apr-2003  James Home	       Created.    
    d2    13-Oct-2003  Matt Stevenson          Imported from HRHO
    ------------------------------------------------------------------------
    */
    {
        return (m_sFileStoreRoot);           
    }
    
   
    
    /*
     * Sets the number of directories for the file store
     *
     * @param String a_iDirCount - the number of directories for the file store
     */    
    public void setDirectoryCount(int a_iDirCount)    
    /*
    ------------------------------------------------------------------------
           14-Apr-2003  James Home		Created.
     d2    13-Oct-2003  Matt Stevenson          Imported from HRHO
    ------------------------------------------------------------------------
    */
    {
        m_iDirCount = a_iDirCount;   
    }
    
    
    
    /*
    * Returns the absolute path of a file.
    * 
    * @param MMFile - the file.
    * @param String - the path.
    */  
    public String getAbsolutePath(BFile a_file)
    /*
    ------------------------------------------------------------------------
            17-Jun-2003     Martin Wilson        Created.
     d2     13-Oct-2003     Matt Stevenson       Imported from HRHO
     d6     16-Jan-2004     Matt Stevenson       Imported and changed from image manager
    ------------------------------------------------------------------------
    */    
    {
        return (m_sFileStoreRoot + "/" + a_file.getUrl());
    }
    
    /**
    * Copys a file in the filestore
    */
   public void createNewFileCopy(String a_sOriginalFilepath,
                                    String a_sTargetFilepath)
   throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d10   20-Jan-2005   James Home     Created
    ------------------------------------------------------------------------
     */
   {
      File fileSource = null;
      File fileTarget = null;
      
      // Fully qualify the paths
      a_sOriginalFilepath = m_sFileStoreRoot + "/" + a_sOriginalFilepath;
      a_sTargetFilepath = m_sFileStoreRoot + "/" + a_sTargetFilepath;
      
      // Create the source File object
      fileSource = new File(a_sOriginalFilepath);
      
      // Check it exists
      if(!fileSource.exists())
      {
         String sError = "FilestoreManager.createNewFileCopy() : Could not find source file: " + a_sOriginalFilepath;         
         m_logger.error(sError);
         throw new Bn2Exception(sError);
      }
      
      // Create the target File object
      fileTarget = new File(a_sTargetFilepath);
      
      // Check it doesn't exist
      if(fileTarget.exists())
      {
         String sError = "FileStoreManager.createNewFileCopy() : Target file already exists " + a_sTargetFilepath;
         m_logger.error(sError);
         throw new Bn2Exception(sError);
      }
      
      try
      {
      	FileUtil.copyFile(a_sOriginalFilepath, a_sTargetFilepath);
      }
      catch(Bn2Exception bn2e)
      {
         m_logger.error("FileStoreManager.createNewFileCopy() : exception whilst copying file : " + bn2e);
         throw bn2e;
      }      
   }
   
    /*
    * Returns the path of a file relative to the webapp.
    * 
    * @param MMFile - the file.
    * @param String - the path.
    */  
    public String getPathRelativeToWebapp(BFile a_file)
    /*
    ------------------------------------------------------------------------
    d11 07-Feb-2005	Martin Wilson			 Created
    ------------------------------------------------------------------------
    */    
    {
        return (m_sRelativeFileStoreRoot + "/" + a_file.getUrl());
    }
}

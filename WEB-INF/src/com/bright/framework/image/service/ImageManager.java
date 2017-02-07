
package com.bright.framework.image.service;


/**
 * bright interactive, ImageManager
 *
 * Copyright 2002 bn2web, All Rights Reserved.
 * ImageManager.java
 * 
 *
 */
/*
Ver  Date	        Who		    Comments
--------------------------------------------------------------------------------
d1  08-Oct-2003         Matt Stevenson        Created.
d2  13-Oct-2003         Matt Stevenson        Added getNewImageId method and
                                              datasource
d3  14-Oct-2003         Matt Stevenson        Started initial work on implementation
d4  15-Oct-2003         Matt Stevenson        Continued implementation work
d5  16-Oct-2003         Matt Stevenson        Changed prepareImageForDownload method
d6  17-Oct-2003         Matt Stevenson        Changed addImage and prepareImageForDownload
                                              methods
d7  17-Oct-2003         Martin Wilson         Added transaction support.
d8  20-Oct-2003         Matt Stevenson        Added commit and rollback to getImage
d9  21-Oct-2003         Matt Stevenson        Modified methods to incorporate 
                                              image shortDescription
d10 23-Oct-2003         Matt Stevenson        Modified getImage method and download method
d11 24-Oct-2003         Matt Stevenson        Modified updateImage and deleteImage
d12 27-Oct-2003         Matt Stevenson        Added height and width to update
d13 29-Oct-2003         Matt Stevenson        Modified format setting for images
d14 13-Jan-2004         Matt Stevenson        Added null check to date created
d15 16-Jan-2004         Matt Stevenson        Imported from image manager
d16 17-Jan-2004         Martin Wilson         Moved into 'image' package. Added methods for properties.
d17 19-Jan-2004         Matt Stevenson        Changes due to directory restructuring
d18 19-Jan-2004         Matt Stevenson        Changes to the addImage method
d19 23-Jan-2004         Matt Stevenson        Fixed minor problems
d20 26-Jan-2004         Matt Stevenson        Added largeimage url
d21 27-Jan-2004         Matt Stevenson        Fixing getImage problems
d22 15-Oct-2004			Matt Stevenson			 Imported into rt framework
d23 20-Oct-2004			Matt Stevenson			 Changed parent image id to be key or null
d24 28-Oct-2004			Matt Stevenson			 Fixed problem with adding and updating images
d25 09-Nov-2004			Matt Stevenson			 Added original image url
d26 04-Feb-2005			James Home				 Added caption and border width to image data
d27 07-Feb-2005			Martin Wilson			 Changed getImage to set urlForPage property of BImage
--------------------------------------------------------------------------------
*/

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.struts.upload.FormFile;

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.util.DBUtil;
import com.bright.framework.file.bean.BFile;
import com.bright.framework.file.service.FileStoreManager;
import com.bright.framework.image.bean.BImage;
import com.bright.framework.image.constant.ImageConstants;
import com.bright.framework.image.util.ImageUtil;

/**
 *
 * ImageManager
 *
 * @author  bn2web
 * @version d1
 */
public class ImageManager extends Bn2Manager implements ImageConstants
{

    private DataSourceComponent m_dataSource = null;
    private FileStoreManager m_fileStoreManager = null;
    
    private float m_fJpgConversionQuality = 0;     
    
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
    ------------------------------------------------------------------------
    */
    {
        super.initialize();
    }    
    
    
    
     /**
      * Get Image metadata for the image with the given id
      * 
      * @param a_lImageId 
      * @return BImage
      * @throws Exception
      */
    public BImage getImage(long a_lImageId) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d3   14-Oct-2003   Matt Stevenson          Initial Implementation Work 
     d4   15-Oct-2003   Matt Stevenson          Modified to retreive and set the
                                                URL's of the image
     d6   17-Oct-2003   Matt Stevenson          Added height and width
     d8   20-Oct-2003   Matt Stevenson          Added commit and rollback
     d9   21-Oct-2003   Matt Stevenson          Incorporated shortDescription
     d10  23-Oct-2003   Matt Stevenson          Corrected problems with SQL
     d15  16-Jan-2004   Matt Stevenson          Imported from image manager 
     d20  26-Jan-2004   Matt Stevenson          Added large image url to call
     d21  27-Jan-2004   Matt Stevenson          Fixing minor problems
    ------------------------------------------------------------------------
     d22	 15-Oct-2004	Matt Stevenson				Reworked select to suit rt 
     d25  09-Nov-2004	Matt Stevenson				Added original image url
     d27	 07-Feb-2005	Martin Wilson				Changed to set urlForPage property of BImage
    ------------------------------------------------------------------------
    */
    {
        BImage image = null;
        Connection con = null;
        
        try 
        {
            con = m_dataSource.getConnection();
            
            //prepare the sql to get the image information from the database
            String sSQL = "SELECT Id, ParentImageId, Url, OriginalImageUrl, Width, Height, Caption, BorderWidth " +
                          "FROM Image WHERE Id=?";
            
            //execute the sql via a prepared statement
            PreparedStatement psql = con.prepareStatement( sSQL );
            psql.setLong(1, a_lImageId);
         
            ResultSet rs = psql.executeQuery();
            
            //get the id from the result set
            rs.next();
            
            image = new BImage();
            
            //first fill the image object
            image.setId(a_lImageId);
            image.setParentImageId(rs.getLong("ParentImageId"));
            image.setUrl(rs.getString("Url"));
            image.setWidth(rs.getInt("Width"));
            image.setHeight(rs.getInt("Height"));
            image.setOriginalImageUrl(rs.getString("OriginalImageUrl"));
            image.setCaption(rs.getString("Caption"));
            image.setBorderWidth(rs.getInt("BorderWidth"));
            
            // Set the URL to use in a JSP page:
            image.setUrlForPage(m_fileStoreManager.getPathRelativeToWebapp(image));
            
            psql.close();
        }
        catch (SQLException e)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException sqle)
            {
                //ignore.
            }
            
            m_logger.error("SQL Exception whilst getting image with id "+a_lImageId+" : "+e);
            throw new Bn2Exception("SQL Exception whilst getting image with id "+a_lImageId+" : "+e);
        }
        finally
        {
            // Return the connection to the pool:
            if (con != null)
            {
                try
                {
                    con.commit();
                    con.close();
                }
                catch (SQLException sqle)
                {
                    m_logger.error("SQL Exception whilst trying to close connection " + sqle.getMessage());
                }                
            }
        }
        
        
        return (image);
    }
    
    
    
    /**
      * Add the metadata for the given image to the database
      * 
      * @param Image - DBTransaction a_dbTransaction - a transaction.
      * @param FormFile a_ffImageFile - the image file
      * @param BImage - the image holding the metadata for resizing and writing to db
      */
    public BImage addImage(DBTransaction a_dbTransaction,
                            FormFile a_ffImageFile, 
									 BImage a_image) 
    throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d3   14-Oct-2003   Matt Stevenson          Imported James' code from
                                                HRHO and modified to incorperate
                                                use of file store manager and
                                                adding of image metadata
     d6   17-Oct-2003   Matt Stevenson          Changed viewing to medium (image name)
                                                added code to set dimensions in db
     d7  17-Oct-2003    Martin Wilson           Added transaction support.
     d13 29-Oct-2003    Matt Stevenson          Changed way image formats are set
     d15 16-Jan-2004    Matt Stevenson          Imported from image manager
     d18 19-Jan-2004    Matt Stevenson          Changed to create and populate image
     d20 26-Jan-2004    Matt Stevenson          Added large image url code
    ------------------------------------------------------------------------
     d22 15-Oct-2004		Matt Stevenson				Imported and reworked for rt 
     d24	28-Oct-2004		Matt Stevenson				Fixed problem with images urls
     d25	09-Nov-2004		Matt Stevenson				Added original image urls
     d26 04-Feb-2005		James Home					Updated to only scale if necessary
    ------------------------------------------------------------------------
    */
    {
        BFile originalImageFile = null;
        String sOriginalFilenameFullPath = null;
        String sNewFilenameFullPath = null;
        float fImageQuality = 0;
        
        // check image file type
        if(!ImageUtil.isProcessableImageFile(a_ffImageFile.getFileName()))
        {
            throw new Bn2Exception("Cannot add image file '" + a_ffImageFile.getFileName() + 
                                   "' because file type cannot be processed as an image.");
        }
        
        fImageQuality = getJpgConversionQuality();
        
        // Store the original image file as a normal file
        originalImageFile = m_fileStoreManager.addFile(a_ffImageFile);
        
        // Get the file names we will use to created the resized image filenames
        sOriginalFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + originalImageFile.getUrl();
        String sNewFilename = (originalImageFile.getUrl()).substring(0, (originalImageFile.getUrl()).length()-4);
        sNewFilename = sNewFilename + k_sResizedImageSuffix;
        sNewFilename = sNewFilename + (originalImageFile.getUrl()).substring((originalImageFile.getUrl()).length()-4, (originalImageFile.getUrl()).length());
        sNewFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + sNewFilename;        
        
        a_image.setUrl(sNewFilename);
        a_image.setOriginalImageUrl(originalImageFile.getUrl());
        
        try
        {
        		m_logger.debug("ImageManager.addImage : About to scale image : FilePath : "+sOriginalFilenameFullPath+" : ImageQuality : "+fImageQuality+" : Width : "+a_image.getWidth());
        	        		
        		Dimension dimensions = ImageUtil.getDimensionsOfImage(sOriginalFilenameFullPath);
        		
        		// If only one of the dimensions has been set, make sure they are both set to the same
        		if(a_image.getHeight()==0)
        		{
        			a_image.setHeight(a_image.getWidth());
        		}
        		else if(a_image.getWidth()==0)
        		{
        			a_image.setWidth(a_image.getHeight());
        		}
        		
        		//	Now adjust the target image dimensions so that the image is not scaled up 
        		// Note that we will still 'scale' the image to convert it to the desired format
        		if(dimensions.height<a_image.getHeight() && dimensions.width<a_image.getWidth())
        		{
        			a_image.setHeight(dimensions.height);
        			a_image.setWidth(dimensions.width);
        		}        		        	
        		
            // Resize the image...
            ImageUtil.scaleImageToRectangle(sOriginalFilenameFullPath,
	                                        sNewFilenameFullPath,
	                                        fImageQuality,
														 a_image.getHeight(),
														 a_image.getWidth(),
														 true);   
            
            //get the dimensions of the newley added image and set the height in the bean...
            dimensions = ImageUtil.getDimensionsOfImage(sNewFilenameFullPath);
            a_image.setHeight((new Double(dimensions.getHeight())).intValue());
            a_image.setWidth((new Double(dimensions.getWidth())).intValue());
        }
        catch(Bn2Exception e)
        {            
            throw new Bn2Exception("There was a problem reading or creating a scaled image file : ", e);
        }
        
        //add the image meta data to the database...
        a_image = addImageToDatabase(a_dbTransaction, a_image);
               
        return a_image;
    }
    
    
    /**
      * Add the given images meta data to the database
      * 
      * @param DBTransaction a_dbTransaction - a database transaction.
      * @param BImage a_image
      * @return BImage a_image
      */
    public BImage addImageToDatabase (DBTransaction a_dbTransaction, BImage a_image) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d3   14-Oct-2003   Matt Stevenson		Created.
     d6   17-Oct-2003   Matt Stevenson    Added height and width
     d7   17-Oct-2003   Martin Wilson     Added transaction support.
     d9   21-Oct-2003   Matt Stevenson    Incorporated shortDescription
     d14  13-Jan-2004   Matt Stevenson    Added null check to date created
     d15  16-Jan-2004   Matt Stevenson    imported and changed from image manager 
     d19  23-Jan-2004   Matt Stevenson    Fixed problem with SQL
     d20  26-Jan-2004   Matt Stevenson    Added large image url
    ------------------------------------------------------------------------
     d22	 15-Oct-2004	Matt Stevenson		Imported and reworked for rt
     d23  20-Oct-2004	Matt Stevenson		Made parent image id key or null
     d25	 09-Nov-2004	Matt Stevenson		Added original image url
     d26	 04-Feb-2005	James Home			Added caption and border width to image data
    ------------------------------------------------------------------------
    */
    {
        Connection con = null;
        
        try 
        {
            con = a_dbTransaction.getConnection();
            
            //prepare the sql insert the image information
            String sSQL = "INSERT INTO Image (Url, ParentImageId, Width, Height, OriginalImageUrl, Caption, BorderWidth) " +
            					"VALUES (?,?,?,?,?,?,?)";
            
            //execute the sql via a prepared statement
            PreparedStatement psql = con.prepareStatement( sSQL );
         
            //set the values to be inserted by the insert statement
            psql.setString(1, a_image.getUrl());
            DBUtil.setFieldIdOrNull(psql, 2, a_image.getParentImageId());
            psql.setInt(3, a_image.getWidth());
            psql.setInt(4, a_image.getHeight());
            psql.setString(5, a_image.getOriginalImageUrl());
            psql.setString(6, a_image.getCaption());
            psql.setInt(7, a_image.getBorderWidth());
            
            psql.executeUpdate();
            
            //get the id of the image...
            sSQL = "SELECT LAST_INSERT_ID() imageId";
            psql = con.prepareStatement( sSQL );
            
            ResultSet rs = psql.executeQuery();
            
            rs.next();
            a_image.setId(rs.getLong("imageId"));
            
            psql.close();
        }
        catch (SQLException e)
        {
            m_logger.error("SQL Exception whilst adding image to the database : "+e);
            throw new Bn2Exception("SQL Exception whilst adding image to the database : "+e);
        }
        
        //return the image
        return a_image;
    }
    
    
     
    /**
      * update the metadata for the given image in the database
      * 
      * @param Image - the image to update the metadata for 
      * @param FormFile a_ffImageFile - the original image file to update if necessary
      * @return BImage - the image (either the original one or the newly updated one).
      */
    public BImage updateImage(DBTransaction a_dbTransaction,
                            BImage a_image, 
                            FormFile a_ffImageFile) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d3   14-Oct-2003   Matt Stevenson          Initial Implementation Work
     d7   17-Oct-2003   Martin Wilson           Added transaction support.
     d11  24-Oct-2003   Matt Stevenson          Modified to include switch to 
                                                identify whether file urls need
                                                to be updated.
     d12  27-Oct-2003   Matt Stevenson          Updated height and width if image is changed
     d13  29-Oct-2003   Matt Stevenson          Changed way imageFormats are set
     d15  16-Jan-2004   Matt Stevenson          Imported and changed from image manager
     d18  19-Jan-2004   Matt Stevenson          Added image return
     d20  26-Jan-2004   Matt Stevenson          Added large image url to task
    ------------------------------------------------------------------------
     d22	 15-Oct-2004	Matt Stevenson				Imported and reworked for rt
     d25	 09-Nov-2004	Matt Stevenson				Modified to cope with original images
    ------------------------------------------------------------------------
    */
    {
        //idenfity if the original file needs to be updated using the file
        //store manager, if it does then...
        if (a_ffImageFile.getFileSize() != 0)
        {
            m_logger.debug("ImageManager.updateImage : Updating Image File");
            
            //delete the original images
            m_fileStoreManager.deleteFile(a_image.getUrl());
            m_fileStoreManager.deleteFile(a_image.getOriginalImageUrl());
        
            //now use the file store manager to store the new original
            //image
            BFile tempFile = m_fileStoreManager.addFile(a_ffImageFile);
            a_image.setUrl(tempFile.getUrl());
            
            String sOriginalFilenameFullPath = null;
            String sNewFilenameFullPath = null;
            float fImageQuality = 0;

            // check image file type
            if(!ImageUtil.isProcessableImageFile(a_ffImageFile.getFileName()))
            {
                throw new Bn2Exception("Cannot update image file '" + a_ffImageFile.getFileName() + 
                                       "' because file type cannot be processed as an image.");
            }

            fImageQuality = getJpgConversionQuality();
            
            //resize the image
            // Get the file names we will use to created the resized image filenames
	         sOriginalFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + tempFile.getUrl();
	         String sNewFilename = (tempFile.getUrl()).substring(0, (tempFile.getUrl()).length()-4);
	         sNewFilename = sNewFilename + k_sResizedImageSuffix;
	         sNewFilename = sNewFilename + (tempFile.getUrl()).substring((tempFile.getUrl()).length()-4, (tempFile.getUrl()).length());
	         sNewFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + sNewFilename;        
	        
	         a_image.setUrl(sNewFilename);
	         a_image.setOriginalImageUrl(tempFile.getUrl());
        
        
            try
				{                       
	            // Resize the image...
	            ImageUtil.scaleImageToWidth(sOriginalFilenameFullPath,
	                                        sNewFilenameFullPath,
	                                        fImageQuality,
														 a_image.getWidth());     
	            
				    //get the dimensions of the newley added image and set the height in the bean...
	            Dimension dimensions = ImageUtil.getDimensionsOfImage(sNewFilenameFullPath);
	            a_image.setHeight((new Double(dimensions.getHeight())).intValue());
				}
		      catch(Bn2Exception e)
		      {            
		         throw new Bn2Exception("There was a problem reading or creating a scaled image file : ", e);
		      }
            
            //update the image meta data 
            updateImageInDatabase(a_dbTransaction, a_image);
        }  
        
        return (a_image);
    }
    
    
    
    /**
      * modifies the metadata for the given 
      * 
      * @param MMImage a_image - the image to add the metadata for 
      * @return MMImage - return the input image that has been added to the database
      * and now has a database id
      */
    public void updateImageInDatabase (DBTransaction a_dbTransaction, BImage a_image) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d3   14-Oct-2003   Matt Stevenson		Created.
     d6   17-Oct-2003   Matt Stevenson          Added height, widths and urls
     d7  17-Oct-2003    Martin Wilson           Added transaction support.
     d9  21-Oct-2003    Matt stevenson          Added shortDescription
     d11 24-Oct-2003    Matt Stevenson          Include check to see if Urls should be updated
                                                in database
     d14 13-Jan-2004    Matt Stevenson          Added null check to date created
     d15 16-Jan-2004    Matt Stevenson          imported and changed from Image Manager
     d20 26-Jan-2004    Matt Stevenson          Added large image url
    ------------------------------------------------------------------------
     d22 15-Oct-2004		Matt Stevenson				Imported and reworked for rt
     d23	20-Oct-2004		Matt Stevenson				Changed parent image id to use key or null
     d25	09-Nov-2004		Matt Stevenson				Added original image url
     d26	 04-Feb-2005	James Home					Added caption and border width to image data
    ------------------------------------------------------------------------
    */
    {
        Connection con = null;
        
        try 
        {
            con = a_dbTransaction.getConnection();
            
            //prepare the sql insert the image information
            String sSQL = "UPDATE Image " +
            					"SET " +
            						"Url=?, " +
            						"ParentImageId=?, " +
            						"Width=?, " +
            						"Height=?, " +
            						"OriginalImageUrl=?, " +
            						"Caption=?, " +
            						"BorderWidth=? " +
            					"WHERE Id=?";
            
            //execute the sql via a prepared statement
            PreparedStatement psql = con.prepareStatement( sSQL );
         
            //set the values to be inserted by the insert statement
            psql.setString(1, a_image.getUrl());
            DBUtil.setFieldIdOrNull(psql, 2, a_image.getParentImageId());
            psql.setInt(3, a_image.getWidth());
            psql.setInt(4, a_image.getHeight());
            psql.setString(5, a_image.getOriginalImageUrl());
            psql.setString(6, a_image.getCaption());
            psql.setInt(7, a_image.getBorderWidth());
            psql.setLong(8, a_image.getId());
            
            psql.executeUpdate();
            psql.close();
        }
        catch (SQLException e)
        {
            m_logger.error("SQL Exception whilst updating image in the database : "+e);
            throw new Bn2Exception("SQL Exception whilst updating image in the database : "+e);
        }

    }
    
    /**
      * modifies the metadata for the given 
      * 
      * @param MMImage a_image - the image to add the metadata for 
      * @return MMImage - return the input image that has been added to the database
      * and now has a database id
      */
    public void updateImageMetadata(DBTransaction a_dbTransaction, BImage a_image) 
    throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d26		03-02-2005	James Home			Created
    ------------------------------------------------------------------------
    */
    {
        Connection con = null;
        
        try 
        {
            con = a_dbTransaction.getConnection();
            
            //prepare the sql insert the image information
            String sSQL = "UPDATE Image " +
            					"SET " +
            						"Caption=?," +
            						"BorderWidth=? " +
            					"WHERE Id=?";
            
            //execute the sql via a prepared statement
            PreparedStatement psql = con.prepareStatement( sSQL );
    
            psql.setString(1, a_image.getCaption());
            psql.setInt(2, a_image.getBorderWidth());
            psql.setLong(3, a_image.getId());
            
            psql.executeUpdate();
            psql.close();
        }
        catch (SQLException e)
        {
            m_logger.error("SQL Exception whilst updating image metadata in the database : "+e);
            throw new Bn2Exception("SQL Exception whilst updating image metadata in the database : "+e);
        }

    }
    
    /**
      * resize the original image - resave
      * 
      * @param Transaction
      * @param BImage - the original image to rescale
      * @param int - the new width to resize to
      */
    public void resizeImage (DBTransaction a_dbTransaction, BImage a_originalImage) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d25	09-Nov-2004		Matt Stevenson				Created
    ------------------------------------------------------------------------
    */
    {
    	  m_logger.debug("ImageManager.resizeImage : About to resize image : "+a_originalImage.getOriginalImageUrl()+" : to new width of : "+a_originalImage.getWidth());
    	
        //delete the old (wrong sized) image
        m_fileStoreManager.deleteFile(a_originalImage.getUrl());
        
        //now resize the image
        float fImageQuality = getJpgConversionQuality();
            
        //resize the image
        String sOriginalFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + a_originalImage.getOriginalImageUrl();        
        String sNewFileFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + a_originalImage.getUrl();    
        
        try
		  {                       
        		// Resize the image...
	         ImageUtil.scaleImageToWidth(sOriginalFilenameFullPath,
	                                        sNewFileFullPath,
	                                        fImageQuality,
														 a_originalImage.getWidth());
	         
	         //get the dimensions of the newley added image and set the height in the bean...
	         Dimension dimensions = ImageUtil.getDimensionsOfImage(sNewFileFullPath);
	         a_originalImage.setHeight((new Double(dimensions.getHeight())).intValue());
	     }
		  catch(Bn2Exception e)
		  {            
		  		throw new Bn2Exception("There was a problem reading or creating a scaled image file : ", e);
		  }
            
        //update the image meta data 
        updateImageInDatabase(a_dbTransaction, a_originalImage);
    }
    
    /**
      * resize the original image so that it's maximum edge length is iMaxDimension pixels
      * 
      * @param Transaction
      * @param BImage - the original image to rescale
      * @param int - the new width to resize to
      */
    public void resizeImage (DBTransaction a_dbTransaction, BImage a_originalImage, int iMaxDimension) 
    throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d26	04-Feb-2005		James Home				Created
    ------------------------------------------------------------------------
    */
    {
    	  m_logger.debug("ImageManager.resizeImage : About to resize image : "+a_originalImage.getOriginalImageUrl()+" : to new width of : "+a_originalImage.getWidth());
    	
        //delete the old (wrong sized) image
        m_fileStoreManager.deleteFile(a_originalImage.getUrl());
        
        //now resize the image
        float fImageQuality = getJpgConversionQuality();
            
        //resize the image
        String sOriginalFilenameFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + a_originalImage.getOriginalImageUrl();        
        String sNewFileFullPath = m_fileStoreManager.getFileStoreRoot() + "/" + a_originalImage.getUrl();    
        
        try
		  {                       
        		// Resize the image...
	         ImageUtil.scaleImageToRectangle(sOriginalFilenameFullPath,
	                                        sNewFileFullPath,
	                                        fImageQuality,
														 iMaxDimension,
														 iMaxDimension,
														 true);
	         
	         //get the dimensions of the newley added image and set the height in the bean...
	         Dimension dimensions = ImageUtil.getDimensionsOfImage(sNewFileFullPath);
	         a_originalImage.setHeight((new Double(dimensions.getHeight())).intValue());
	         a_originalImage.setWidth((new Double(dimensions.getWidth())).intValue());
	     }
		  catch(Bn2Exception e)
		  {            
		  		throw new Bn2Exception("There was a problem reading or creating a scaled image file : ", e);
		  }
            
        //update the image meta data 
        updateImageInDatabase(a_dbTransaction, a_originalImage);
    }
    
    /**
      * delete an image 
      * 
      * @param long a_lImageId - the image to delete
      */
    public void deleteImage(DBTransaction a_dbTransaction, long a_lImageId) throws Bn2Exception
    /*
    ------------------------------------------------------------------------
     d1   08-Oct-2003   Matt Stevenson		Created.
     d3   14-Oct-2003   Matt Stevenson          Initial Implementation
     d4   15-Oct-2003   Matt Stevenson          Added code to delete the 
                                                medium and thumbnail image
     d5   17-Oct-2003   Martin Wilson           Added transaction support.  
     d11  24-Oct-2003   Matt Stevenson          changed image to image id     
     d20  26-Jan-2004   Matt Stevenson          Added call to delete large image  
    ------------------------------------------------------------------------
     d22	 15-Oct-2004	Matt Stevenson				Reworked for rt  
     d25	 09-Nov-2004	Matt Stevenson				Modified to delete original image                                 
    ------------------------------------------------------------------------
    */
    {
        
        // First delete the metadata:
        Connection con = null;
        
        try 
        {
            BImage a_image = getImage(a_lImageId);
            
            // As the first thing, delete the files:
            m_fileStoreManager.deleteFile(a_image.getOriginalImageUrl());
            m_fileStoreManager.deleteFile(a_image.getUrl());
        
            con = a_dbTransaction.getConnection();
            
            //prepare the sql to do the delete
            String sSQL = "DELETE FROM Image WHERE ID=?";
            
            //execute the sql via a prepared statement
            PreparedStatement psql = con.prepareStatement( sSQL );
         
            //set the value of the id to delete
            psql.setLong(1, a_lImageId);
            
            psql.executeUpdate();
        }
        catch (SQLException e)
        {
            m_logger.error("SQL Exception whilst deleting image from the database : "+e);
            throw new Bn2Exception("SQL Exception whilst deleting image from the database : "+e);
        }

    }
    
    public void setJpgConversionQuality(float a_fQuality)
    {
        m_fJpgConversionQuality = a_fQuality;
    }
    
    public float getJpgConversionQuality()
    {
        return (m_fJpgConversionQuality);
    }     

    /*
     * Links the data source component for use in DB pool retreival
     *
     * @param DataSourceComponent a_datasource - the datasource component to link
     */
    public void setDataSourceComponent(DataSourceComponent a_datasource)
    /*
    ------------------------------------------------------------------------
     d2   13-Oct-2003  Matt Stevenson		Created.
    ------------------------------------------------------------------------
    */    
    {
        m_dataSource = a_datasource;
    }



    /*
     * Links the file store manager component for use in this object
     *
     * @param FileStoreManager a_fileStoreManager - the FileStoreManager component to link
     */
    public void setFileStoreManager(FileStoreManager a_fileStoreManager)
    /*
    ------------------------------------------------------------------------
     d3   14-Oct-2003  Matt Stevenson		Created.
    ------------------------------------------------------------------------
    */    
    {
        m_fileStoreManager = a_fileStoreManager;
    }
    

       
}

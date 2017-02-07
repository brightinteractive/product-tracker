package com.bright.producttracker.product.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.service.DBTransactionManager;
import com.bright.framework.image.bean.BImage;
import com.bright.framework.image.service.ImageManager;
import com.bright.framework.util.BrightDateFormat;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.constant.ProductSettings;
import com.bright.producttracker.product.exception.DuplicateProductOldCodeException;
import com.bright.producttracker.product.exception.DuplicateProductNewCodeException;
import com.bright.producttracker.product.form.ProductForm;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ViewEventsAction.java
 *
 * Contains the ViewEventsAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	20-Jan-2005		Martin Wilson		Created
 d2	16-May-2005		Matt Stevenson		Modified to allow images to be deleted
 d3	19-Jul-2005		Matt Stevenson		Modified to restrict check for existing product codes
 d4	17-Feb-2006		Matt Stevenson		Modified to check code
 d5	25-Oct-2007		Matt Stevenson		Added status
 d6	26-Jul-2010		Kevin Bennett		Modified for old/new code fields
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SaveProductAction extends Bn2Action
{
	protected ProductManager m_productManager = null;
	protected ImageManager m_imageManager = null;
	protected DBTransactionManager m_transactionManager = null;
	protected ProductSegmentManager m_productSegmentManager = null;
	
	/**
	 * The execute method of the Action.
	 *
	 * @param ActionMapping a_mapping - the action mapping [explain it here].
	 * @param ActionForm a_form - the action form- in this case containing the login info entered by user.
	 * @param HttpServletRequest a_request - the request object.
	 * @param HttpServletResponse a_response - the response object.
	 * @return ActionForward - where to forward or redirect to when completed.
	 * @throws	ServletException    Servlet Exception
	 */
	public ActionForward execute(ActionMapping a_mapping,
										  ActionForm a_form,
										  HttpServletRequest a_request,
										  HttpServletResponse a_response)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1	20-Jan-2005	Martin Wilson		Created
	 d2	16-May-2005	Matt Stevenson		Modified to allow images to be deleted
	 d3	19-Jul-2005	Matt Stevenson		Modified to restrict check for existing product code
	 d4	17-Feb-2006	Matt Stevenson		Added check for code
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		DBTransaction dbTransaction = null;
		ProductForm form = (ProductForm)a_form;
		
		// See if the user cancelled:
		if (a_request.getParameter(FrameworkConstants.k_sCancelParam) != null
			&& a_request.getParameter(FrameworkConstants.k_sCancelParam).length() > 0)
		{
			if (form.getProduct().getId() > 0)
			{
				a_request.setAttribute(k_sIDParam, new Long(form.getProduct().getId()));
				return a_mapping.findForward(FrameworkConstants.CANCEL_KEY); 
			}
			return a_mapping.findForward(ProductConstants.k_sForward_AddCancel);
		}		
		
		try
		{						
			// Get a transaction:
			dbTransaction = m_transactionManager.getNewTransaction();
			
			// Get the product:
			Product product = form.getProduct();
			
			// Check for required fields:
			if (!StringUtil.stringIsPopulated(product.getDescription()))
			{
				form.addError("Please enter a Description");
			}
			
			if (!StringUtil.stringIsPopulated(product.getNewCode()))
			{
				form.addError("Please enter a code (new) for this product");
			}
			
			// Disable new code required validation until all the new codes are available 
			//if (!StringUtil.stringIsPopulated(product.getNewCode()))
			//{
			//	form.addError("Please enter a code (new) for this product");
			//}
			
			if (StringUtil.stringIsPopulated(form.getStatusLastTreatmentDateString()))
			{
				//make sure we have a valid date...
				try
				{
					BrightDateFormat format = new BrightDateFormat("dd/MM/yyyy", Locale.ENGLISH);
					form.getProduct().setStatusLastTreatmentDate(format.parse(form.getStatusLastTreatmentDateString()));
				}
				catch (ParseException e)
				{
					m_logger.error("Exception in SaveProductAction: "+e.getMessage());
					form.addError("You need to enter a valid last treatment date");
				}
			}
			
			if (StringUtil.stringIsPopulated(form.getLastUpdateString()))
			{
				//make sure we have a valid date...
				try
				{
					BrightDateFormat format = new BrightDateFormat("dd/MM/yyyy", Locale.ENGLISH);
					form.getProduct().setLastUpdateDate(format.parse(form.getLastUpdateString()));
				}
				catch (ParseException e)
				{
					m_logger.error("Exception in SaveProductAction: "+e.getMessage());
					form.addError("You need to enter a valid last update date");
				}
			}
			
			//check the product code if this is an 'add' or the product has changed code...
			if ((product.getId() <= 0) || (!product.getOldCode().equals(form.getPreEditOldProductCode())))
			{
				m_productManager.checkProductOldCode(dbTransaction, product.getOldCode());				
			}
			
			//check the product code if this is an 'add' or the product has changed code...
			if (StringUtil.stringIsPopulated(product.getNewCode()) && ((product.getId() <= 0) || (!product.getNewCode().equals(form.getPreEditNewProductCode()))))
			{
				m_productManager.checkProductNewCode(dbTransaction, product.getNewCode());				
			}
			
			// Disable required segment validation until all the new codes are available
			//if(product.getProductSegment().getId() <= 0)
			//{
			//	form.addError("Please select a product segment.");
			//}
			
			if(StringUtil.stringIsPopulated(product.getHsCode()) && !product.getHsCode().matches("[a-zA-z0-9]{8}"))
			{
				form.addError("Please enter a valid HS Code.  It must contain exactly 8 alphanumeric characters.");
			}
			
			if (!form.getHasErrors())
			{
				long lOldImageId = 0;form.getProduct().getImage().getId();
				
				// See if we need to update the image:
	     		if ((form.getNewProductImage() != null) && (form.getNewProductImage().getFileSize() > 0))
	     		{
	     			// See if the product already has an image:
	     			lOldImageId = form.getProduct().getImage().getId();     			
	     			
	     			// Add the new image:
	     			BImage imageSpec = new BImage();
	     			
	     			// Set the max dimensions:
	     			imageSpec.setWidth(ProductSettings.getMaxImageWidth());
	     			imageSpec.setHeight(ProductSettings.getMaxImageHeight());
	     			
	     			BImage newImage = m_imageManager.addImage(dbTransaction, 
													form.getNewProductImage(), 
													imageSpec);		
	     			
	     			// Set in the product:
	     			product.setImage(newImage);
				
	     		}
	     		else if (form.getDeleteImage())
	     		{
	     			lOldImageId = form.getProduct().getImage().getId();     		
	     			product.setImage(null);
	     		}
	     		
				// Save the product:
				m_productManager.saveProduct(dbTransaction, product);
				
     			// See if the product already has an image:
     			if (lOldImageId >0)
     			{
     				// Delete the existing image.
     				m_imageManager.deleteImage(dbTransaction, lOldImageId);
     			}				
				
				// Get the id of the product and add as an attribute in case we need the new id:
				long lProductId = product.getId();				
				
				a_request.setAttribute(CommonConstants.k_sIDParam, new Long(lProductId));
			
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
			}
			else
			{				
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 				
			}
		}
		catch (DuplicateProductOldCodeException dpoce)
		{			
			 m_logger.error("SaveProductAction.execute : " + dpoce.getMessage());
			 form.addError("The selected product code (old) already exists in the database - please provide an alternate code");
			 afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 
			 try
			 {
				  dbTransaction.rollback();
			 }
			 catch (SQLException se)
			 {
				  // Do nothing.
			 }
		}
		catch (DuplicateProductNewCodeException dpnce)
		{			
			 m_logger.error("SaveProductAction.execute : " + dpnce.getMessage());
			 form.addError("The selected product code (new) already exists in the database - please provide an alternate code");
			 afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 
			 try
			 {
				  dbTransaction.rollback();
			 }
			 catch (SQLException se)
			 {
				  // Do nothing.
			 }
		}
		catch (Bn2Exception bn2e)
		{			
			 m_logger.error("SaveProductAction.execute : " + bn2e.getMessage());
			 afForward = a_mapping.findForward (CommonConstants.SYSTEM_FAILURE_KEY);
			 
			 try
			 {
				  dbTransaction.rollback();
			 }
			 catch (SQLException se)
			 {
				  // Do nothing.
			 }
		}
		finally
		{
			 // Commit the transaction:
			 try
			 {
				  dbTransaction.commit();
			 }
			 catch (SQLException sqle)
			 {
				  m_logger.error("Exception commiting transaction in SaveProductAction:" + sqle.getMessage());				 
				  afForward = a_mapping.findForward (CommonConstants.FAILURE_KEY);
			 }
		}		
		
		form.setProductSegments(m_productSegmentManager.getProductSegments(null));
		return (afForward);
	}
	
	public void setProductManager(ProductManager a_ProductManager)
	{
		m_productManager = a_ProductManager;
	}

	public void setImageManager(ImageManager a_imageManager)
	{
		m_imageManager = a_imageManager;
	}	
	
	public void setTransactionManager(DBTransactionManager a_transactionManager)
	{
		m_transactionManager = a_transactionManager;
	}	
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}
}

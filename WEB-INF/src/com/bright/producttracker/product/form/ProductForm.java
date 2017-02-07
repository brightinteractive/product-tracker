package com.bright.producttracker.product.form;

import java.util.List;
import java.util.Vector;

import org.apache.struts.upload.FormFile;

import com.bn2web.common.form.Bn2Form;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.productsegment.bean.ProductSegment;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductForm.java
 *
 * Contains the ProductForm class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	01-Feb-2005	Martin Wilson		Created
 d2	16-May-2005	Matt Stevenson		Added 'deleteImage' switch
 d3	19-Jul-2005	Matt Stevenson		Added old product code
 d4	25-Oct-2007	Matt Stevenson		Added last treatment date string
 d5	07-Nov-2007	Matt Stevenson		Added show browser switch
 d3	26-Jul-2010	Kevin Bennett		Modified for old/new code and product segment fields
 --------------------------------------------------------------------------------
 */

/**
 * Holds data for a product
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductForm extends Bn2Form
{
	private Product m_product = null;
	private Vector m_vecEvents = null;
	private FormFile m_ffProductImage = null;	
	private boolean m_bDeleteImage = false;
	private String m_sPreEditOldProductCode = null;
	private String m_sPreEditNewProductCode = null;
	private String m_sStatusLastTreatmentDateString = null;
	private String m_sLastUpdateDateString = null;
	private boolean m_bShowBrowser = true;
	private List<ProductSegment> m_productSegments = null;
	
	/** @return Returns the product. */
	public Product getProduct()
	{
		if (m_product == null)
		{
			m_product = new Product();
		}
		
		return (m_product);
	}
	
	/** @param a_sProduct The product to set. */
	public void setProduct(Product a_sProduct)
	{
		m_product = a_sProduct;
	}
	
	/** @return Returns the vecEvents. */
	public Vector getEvents()
	{
		return m_vecEvents;
	}
	/** @param a_sVecEvents The vecEvents to set. */
	public void setEvents(Vector a_vecEvents)
	{
		m_vecEvents = a_vecEvents;
	}	
	/** @return Returns the ffProductImage. */
	public FormFile getNewProductImage()
	{
		return m_ffProductImage;
	}
	/** @param a_sFfProductImage The ffProductImage to set. */
	public void setNewProductImage(FormFile a_ffProductImage)
	{
		m_ffProductImage = a_ffProductImage;
	}
	
	public void setDeleteImage (boolean a_bDeleteImage)
	{
		m_bDeleteImage = a_bDeleteImage;
	}
	
	public boolean getDeleteImage ()
	{
		return (m_bDeleteImage);
	}
	
	/** @return Returns the preEditOldProductCode. */
	public String getPreEditOldProductCode()
	{
		return m_sPreEditOldProductCode;
	}
	
	/** @param a_sPreEditOldProductCode The preEditOldProductCode to set. */
	public void setPreEditOldProductCode(String a_sPreEditOldProductCode)
	{
		m_sPreEditOldProductCode = a_sPreEditOldProductCode;
	}
	
	
	
	public String getPreEditNewProductCode()
	{
		return m_sPreEditNewProductCode;
	}

	public void setPreEditNewProductCode(String a_sPreEditNewProductCode)
	{
		m_sPreEditNewProductCode = a_sPreEditNewProductCode;
	}

	public void setStatusLastTreatmentDateString (String a_sStatusLastTreatmentDateString)
	{
		m_sStatusLastTreatmentDateString = a_sStatusLastTreatmentDateString;
	}
	
	public String getStatusLastTreatmentDateString ()
	{
		return (m_sStatusLastTreatmentDateString);
	}
	
	public void setLastUpdateDateString (String a_sLastUpdateDateString)
	{
		m_sLastUpdateDateString = a_sLastUpdateDateString;
	}
	
	public String getLastUpdateString ()
	{
		return (m_sLastUpdateDateString);
	}
	
	public void setShowBrowser (boolean a_bShowBrowser)
	{
		m_bShowBrowser = a_bShowBrowser;
	}
	
	public boolean getShowBrowser ()
	{
		return (m_bShowBrowser);
	}

	public List<ProductSegment> getProductSegments()
	{
		return m_productSegments;
	}

	public void setProductSegments(List<ProductSegment> a_productSegments)
	{
		m_productSegments = a_productSegments;
	}
	
	
}

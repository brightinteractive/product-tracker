package com.bright.producttracker.product.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import com.bright.framework.database.bean.DataBean;
import com.bright.framework.image.bean.BImage;
import com.bright.framework.simplelist.bean.ListValue;
import com.bright.producttracker.movementtracker.util.MovementTrackerUtil;
import com.bright.producttracker.productsegment.bean.ProductSegment;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * Product.java
 *
 * Contains the Product class.
 */

/*
 Ver	Date				Who			Comments
 --------------------------------------------------------------------------------
 d1	01-Feb-2005		Martin Wilson		Created
 d2	18-Mar-2005		Matt Stevenson		Removed location information and added movements
 d3	25-Oct-2007		Matt Stevenson		Added status and equipment fields
 d4	05-Mar-2008		Matt Stevenson		Added next movement
 d5	27-Mar-2008		Matt Stevenson		Added method to get the next movement date
 d3	26-Jul-2010		Kevin Bennett		Modified for old/new code, gross/net weight and product segment fields
 --------------------------------------------------------------------------------
 */

/**
 * Represents a product
 * 
 * @author Bright Interactive
 * @version d1
 */
public class Product extends DataBean
{
	private ListValue m_vehicle = null;
	private ListValue m_productType = null;
	private ListValue m_manufacturingLocation = null; 
	private BImage m_image = null;
	private String m_sDescription = null;
	private String m_sOldCode = null;
	private String m_sNewCode = null;
	private String m_sOtherVehicle = null;
	private String m_sContactPerson = null;
	private String m_sYearOfManufacture = null;
	private boolean m_bDedicatedPackaging = false;
	private boolean m_bTechnologySheetAvailable = false;
	private boolean m_bLabel = false;
	private String m_sLength = null;
	private String m_sHeight = null;
	private String m_sWidth = null;
	private String m_sGrossWeight = null;	
	private String m_sNetWeight = null;
	private String m_sComments = null;	
	private String m_sModel = null;
	private ProductMovement m_movement = null;
	private ProductMovement m_nextMovement = null;
	private String m_sStatus = null;
	private String m_sStatusLastTreatmentName = null;
	private Date m_dtStatusLastTreatmentDate = null;
	private Date m_dtLastUpdateDate = null;
	private String m_sLastUpdateDoneBy = null;
	
	private Vector m_vecPastMovements = null;
	private Vector m_vecFutureMovements = null;
	
	//equipment fields...
	private String m_sManufacturerName = null;
	private String m_sManufacturerAddress = null;
	private String m_sManufacturerContactName = null;
	private String m_sManufacturerTelephone = null;
	private String m_sManufacturerEmail = null;
	private String m_sEquipmentSize = null;
	private String m_sEquipmentAssemblyInstructions = null;
	
	private String m_sHsCode = null;
	
	private ProductSegment m_productSegment = new ProductSegment();
	
	
	/** @return Returns the model. */
	public String getModel()
	{
		return m_sModel;
	}
	/** @param a_sModel The model to set. */
	public void setModel(String a_sModel)
	{
		m_sModel = a_sModel;
	}
	
	/** @return Returns the imageId. */
	public BImage getImage()
	{
		if (m_image == null)
		{
			m_image = new BImage();
		}
		
		return m_image;
	}
	/** @param a_sImageId The imageId to set. */
	public void setImage(BImage a_image)
	{
		m_image = a_image;
	}
	
	/** @return Returns the manufacturingLocationId. */
	public ListValue getManufacturingLocation()
	{
		if (m_manufacturingLocation == null)
		{
			m_manufacturingLocation = new ListValue();
		}
		
		return m_manufacturingLocation;
	}
	
	/** @param a_sManufacturingLocationId The manufacturingLocationId to set. */
	public void setManufacturingLocation(ListValue a_manufacturingLocation)
	{
		m_manufacturingLocation = a_manufacturingLocation;
	}
	
	
	/** @return Returns the productTypeId. */
	public ListValue getProductType()
	{
		if (m_productType == null)
		{
			m_productType = new ListValue();
		}
		
		return (m_productType);
	}
	/** @param a_sProductTypeId The productTypeId to set. */
	public void setProductType(ListValue a_productType)
	{
		m_productType = a_productType;
	}
	/** @return Returns the vehicleId. */
	public ListValue getVehicle()
	{
		if (m_vehicle == null)
		{
			m_vehicle = new ListValue();
		}
		return (m_vehicle);
	}
	/** @param a_sVehicleId The vehicleId to set. */
	public void setVehicle(ListValue a_vehicle)
	{
		m_vehicle = a_vehicle;
	}
	
	/** @return Returns the dedicatedPackaging. */
	public boolean getDedicatedPackaging()
	{
		return m_bDedicatedPackaging;
	}
	/** @param a_sDedicatedPackaging The dedicatedPackaging to set. */
	public void setDedicatedPackaging(boolean a_bDedicatedPackaging)
	{
		m_bDedicatedPackaging = a_bDedicatedPackaging;
	}
	/** @return Returns the label. */
	public boolean getLabel()
	{
		return m_bLabel;
	}
	/** @param a_sLabel The label to set. */
	public void setLabel(boolean a_bLabel)
	{
		m_bLabel = a_bLabel;
	}

	/** @return Returns the code. */
	public String getOldCode()
	{
		return m_sOldCode;
	}
	/** @param a_sOldCode The code to set. */
	public void setOldCode(String a_sOldCode)
	{
		m_sOldCode = a_sOldCode;
	}
	public String getNewCode()
	{
		return m_sNewCode;
	}
	public void setNewCode(String a_sNewCode)
	{
		m_sNewCode = a_sNewCode;
	}
	/** @return Returns the comments. */
	public String getComments()
	{
		return m_sComments;
	}
	/** @param a_sComments The comments to set. */
	public void setComments(String a_sComments)
	{
		m_sComments = a_sComments;
	}
	/** @return Returns the contactPerson. */
	public String getContactPerson()
	{
		return m_sContactPerson;
	}
	/** @param a_sContactPerson The contactPerson to set. */
	public void setContactPerson(String a_sContactPerson)
	{
		m_sContactPerson = a_sContactPerson;
	}
	
	/** @return Returns the description. */
	public String getDescription()
	{
		return m_sDescription;
	}
	/** @param a_sDescription The description to set. */
	public void setDescription(String a_sDescription)
	{
		m_sDescription = a_sDescription;
	}
	
	/** @return Returns the otherVehicle. */
	public String getOtherVehicle()
	{
		return m_sOtherVehicle;
	}
	/** @param a_sOtherVehicle The otherVehicle to set. */
	public void setOtherVehicle(String a_sOtherVehicle)
	{
		m_sOtherVehicle = a_sOtherVehicle;
	}
	/** @return Returns the yearOfManufacture. */
	public String getYearOfManufacture()
	{
		return m_sYearOfManufacture;
	}
	/** @param a_sYearOfManufacture The yearOfManufacture to set. */
	public void setYearOfManufacture(String a_sYearOfManufacture)
	{
		m_sYearOfManufacture = a_sYearOfManufacture;
	}
	
	
	public String getHeight()
	{
		return m_sHeight;
	}
	public void setHeight(String a_sHeight)
	{
		m_sHeight = a_sHeight;
	}
	public String getLength()
	{
		return m_sLength;
	}
	public void setLength(String a_sLength)
	{
		m_sLength = a_sLength;
	}
	public String getGrossWeight()
	{
		return m_sGrossWeight;
	}
	public void setGrossWeight(String a_sGrossWeight)
	{
		m_sGrossWeight = a_sGrossWeight;
	}
	
	public String getNetWeight()
	{
		return m_sNetWeight;
	}
	public void setNetWeight(String a_sNetWeight)
	{
		m_sNetWeight = a_sNetWeight;
	}
	public String getWidth()
	{
		return m_sWidth;
	}
	public void setWidth(String a_sWidth)
	{
		m_sWidth = a_sWidth;
	}
	public Vector getPastMovements()
	{
		return m_vecPastMovements;
	}
	public void setPastMovements(Vector a_vecMovements)
	{
		m_vecPastMovements = a_vecMovements;
	}
	public Vector getFutureMovements()
	{
		return m_vecFutureMovements;
	}
	public void setFutureMovements(Vector a_vecMovements)
	{
		m_vecFutureMovements = a_vecMovements;
	}
	
	public int getNoOfMovements()
	{
		int iCount = 0;
		
		if (m_vecPastMovements != null)
		{
			iCount = m_vecPastMovements.size();
		}
		
		if (m_vecFutureMovements != null)
		{
			iCount = iCount + m_vecFutureMovements.size();
		}
		return (iCount);
	}
	
	public void setCurrentMovement (ProductMovement a_movement)
	{
		m_movement = a_movement;
	}
	
	public ProductMovement getCurrentMovement ()
	{
		return (m_movement);
	}
	
	public void setNextMovement (ProductMovement a_movement)
	{
		m_nextMovement = a_movement;
	}
	
	public ProductMovement getNextMovement ()
	{
		return (m_nextMovement);
	}
	
	public void setStatus (String a_sStatus)
	{
		m_sStatus = a_sStatus;
	}
	
	public String getStatus ()
	{
		return (m_sStatus);
	}
	
	public void setStatusLastTreatmentName (String a_sStatusLastTreatmentName)
	{
		m_sStatusLastTreatmentName = a_sStatusLastTreatmentName;
	}
	
	public String getStatusLastTreatmentName ()
	{
		return (m_sStatusLastTreatmentName);
	}
	
	public void setStatusLastTreatmentDate (Date a_dtStatusLastTreatmentDate)
	{
		m_dtStatusLastTreatmentDate = a_dtStatusLastTreatmentDate;
	}
	
	public Date getStatusLastTreatmentDate ()
	{
		return (m_dtStatusLastTreatmentDate);
	}
	
	public void setLastUpdateDate (Date a_dtLastUpdateDate)
	{
		m_dtLastUpdateDate = a_dtLastUpdateDate;
	}

	public Date getLastUpdateDate ()
	{
		return (m_dtLastUpdateDate);
	}
	
	public void setLastUpdateDoneBy (String a_sLastUpdateDoneBy)
	{
		m_sLastUpdateDoneBy = a_sLastUpdateDoneBy;
	}
	
	public String getLastUpdateDoneBy ()
	{
		return (m_sLastUpdateDoneBy);
	}
	
	public String getEquipmentAssemblyInstructions() 
	{
		return m_sEquipmentAssemblyInstructions;
	}
	
	public void setEquipmentAssemblyInstructions(String a_sEquipmentAssemblyInstructions) 
	{
		m_sEquipmentAssemblyInstructions = a_sEquipmentAssemblyInstructions;
	}
	
	public String getEquipmentSize() 
	{
		return m_sEquipmentSize;
	}
	
	public void setEquipmentSize(String a_sEquipmentSize) 
	{
		m_sEquipmentSize = a_sEquipmentSize;
	}
	
	public String getManufacturerAddress() 
	{
		return m_sManufacturerAddress;
	}
	
	public void setManufacturerAddress(String a_sManufacturerAddress) 
	{
		m_sManufacturerAddress = a_sManufacturerAddress;
	}
	
	public String getManufacturerContactName() 
	{
		return m_sManufacturerContactName;
	}
	
	public void setManufacturerContactName(String a_sManufacturerContactName) 
	{
		m_sManufacturerContactName = a_sManufacturerContactName;
	}
	
	public String getManufacturerEmail() 
	{
		return m_sManufacturerEmail;
	}
	
	public void setManufacturerEmail(String a_sManufacturerEmail) 
	{
		m_sManufacturerEmail = a_sManufacturerEmail;
	}
	
	public String getManufacturerName() 
	{
		return m_sManufacturerName;
	}
	
	public void setManufacturerName(String a_sManufacturerName) 
	{
		m_sManufacturerName = a_sManufacturerName;
	}
	
	public String getManufacturerTelephone() 
	{
		return m_sManufacturerTelephone;
	}
	
	public void setManufacturerTelephone(String a_sManufacturerTelephone) 
	{
		m_sManufacturerTelephone = a_sManufacturerTelephone;
	}
	
	public Date getNextMovementDate ()
	{
		GregorianCalendar now = new GregorianCalendar();
		
		//first check the departure date of the current movement...
		if (this.getCurrentMovement() != null)
		{
			if (this.getCurrentMovement().getDepartureDate() != null)
			{
				if (this.getCurrentMovement().getDepartureDate().getTime() > now.getTimeInMillis())
				{
					return (this.getCurrentMovement().getDepartureDate());
				}
			}
		}
		
		//otherwise check the next movement...
		if (this.getNextMovement() != null)
		{
			if (this.getNextMovement().getArrivalDate() != null)
			{
				if (this.getNextMovement().getArrivalDate().getTime() > now.getTimeInMillis())
				{
					return (this.getNextMovement().getArrivalDate());
				}
			}
		}
		
		return null;
	}
	
	public ProductSegment getProductSegment()
	{
		return m_productSegment;
	}
	public void setProductSegment(ProductSegment a_productSegment)
	{
		m_productSegment = a_productSegment;
	}
	
	public String getHsCode()
	{
		return m_sHsCode;
	}
	
	public void setHsCode(String a_sHsCode)
	{
		m_sHsCode = a_sHsCode;
	}
	public boolean getTechnologySheetAvailable()
	{
		return m_bTechnologySheetAvailable;
	}
	public void setTechnologySheetAvailable(boolean a_bTechnologySheetAvailable)
	{
		m_bTechnologySheetAvailable = a_bTechnologySheetAvailable;
	}
	
	public List<ProductMovement> getAllMovements()
	{
		List<ProductMovement> movements = new ArrayList<ProductMovement>();
		
		if(m_vecPastMovements != null)
		{
			movements.addAll(m_vecPastMovements);
		}
		
		if(m_vecFutureMovements != null)
		{
			movements.addAll(m_vecFutureMovements);
		}
		
		return movements;
	}
	
	public ProductMovement getFirstMovementOnDate(Date a_date)
	{
		for (ProductMovement productMovement : getAllMovements())
		{
			if(MovementTrackerUtil.hasValidMovementDates(productMovement) && MovementTrackerUtil.movementIsInRange(productMovement, a_date, a_date))
			{
				return productMovement;
			}
		}
		
		return null;
	}
	
	
}

package com.bright.producttracker.product.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.common.bean.Country;
import com.bright.framework.common.service.ScheduleManager;
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.database.bean.DBTransaction;
import com.bright.framework.database.util.DBUtil;
import com.bright.framework.image.bean.BImage;
import com.bright.framework.image.service.ImageManager;
import com.bright.framework.mail.service.EmailManager;
import com.bright.framework.user.constant.UserConstants;
import com.bright.framework.user.service.UserManager;
import com.bright.framework.util.DateUtil;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.application.constant.ApplicationConstants;
import com.bright.producttracker.event.bean.Event;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.constant.ProductSettings;
import com.bright.producttracker.product.exception.DuplicateProductOldCodeException;
import com.bright.producttracker.product.exception.DuplicateProductNewCodeException;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * ProductManager.java
 *
 * Contains the ProductManager class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1		01-Feb-2005		Martin Wilson		Created
 d2		18-Mar-2005		Matt Stevenson		Modified to make use of ProductMovements
 d3		21-Mar-2005		Matt Stevenson		Implemented search changes
 d4		23-Mar-2005		Matt Stevenson		Added country to getProduct
 d5		18-Apr-2005		Matt Stevenson		Modified saveCurrentMovement to set current movement
 d6		14-Jun-2005		Matt Stevenson		Added checkProductCode method
 d7		22-Jun-2005		Matt Stevenson		Added ordering clause to search method, added 
 											getProductMovements method
 d8		19-Jul-2005		Matt Stevenson		Modified getProductMovements method
 d9		12-Dec-2005		Matt Stevenson		Work on searching only current locations
 d10	17-Feb-2006		Matt Stevenson		Work on searching
 d11	16-Jun-2006		Matt Stevenson		Modified search method
 d12	22-Aug-2006		Matt Stevenson		Fixed problem with product movements
 d13	06-Dec-2006		Matt Stevenson		Modified getProductMovements and search method
 d14	25-Oct-2007		Matt Stevenson		Modified saveProduct method
 d15	16-Oct-2007		Matt Stevenson		Updated to include manufacturing fields
 d16	26-Oct-2007		Matt Stevenson		Modified removeListValueReferences
 d17	29-Oct-2007		Matt Stevenson		Added scheduled tasks for emails
 d18	30-Oct-2007		Matt Stevenson		Added reminder email to product movements
 d19	11-Jan-2008		Matt Stevenson		Added product link to reminder email
 d20	07-Feb-2008		Matt Stevenson		Modified emails
 d21	05-Mar-2008		Matt Stevenson		Changed search method
 d22	26-Jul-2010		Kevin Bennett		Modified for old/new code, gross/net weight and product segment fields
 --------------------------------------------------------------------------------
 */

/**
 * Manager for adding, listing and editing products
 * 
 * @author Bright Interactive
 * @version d1
 */
public class ProductManager extends Bn2Manager implements CommonConstants, FrameworkConstants, ApplicationConstants, UserConstants
{
	protected DataSourceComponent m_dataSource = null;
	protected ImageManager m_imageManager = null;
	protected ScheduleManager m_scheduleManager = null;
	protected EmailManager m_emailManager = null;
	protected UserManager m_userManager = null;
	protected ProductSegmentManager m_productSegmentManager = null;
	
	protected final String k_sMovementNotificationFields = 
			"SELECT " +							
				"p.Id pId, " +
				"p.Description , " +
				"p.OldCode," +
				"p.NewCode, " +								
				"pm.ReminderEmail, "+
				"pm.MovedOn, "+
				"e.CoordinatorEmailAddress, "+
				"e.Id EventId, "+
				"IFNULL(pm.ArrivalDate,e.StartDate) ArrivalDate, " +
				"IFNULL(pm.DepartureDate,e.EndDate) DepartureDate, " +				
				"IFNULL(CONCAT(e.Name,IFNULL(CONCAT(' ',ec.Name),'')),CONCAT(lv.Value,' ',IFNULL(c.Name,''))) LocationName " +																				
			"FROM ProductMovement pm "+
			"LEFT JOIN Event e ON pm.EventId=e.Id "+
			"LEFT JOIN Country ec ON e.CountryId=ec.Id "+
			"LEFT JOIN Product p ON pm.ProductId=p.Id "+
			"LEFT JOIN ListValue lv ON pm.LocationId=lv.Id "+
			"LEFT JOIN Country c ON pm.CountryId=c.Id ";
	
	
	/**
	 * Initialise this component. Sets up a scheduled task to send emails
	 *
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void initialize() throws Bn2Exception	
	{		
		m_logger.info("ProductManager.initialize(): Initializing...");		
		
		scheduleAdminEmails();		
		scheduleReminderEmails();		
		scheduleCoordinatorEmails();
	}


	private void scheduleCoordinatorEmails()
	{
		TimerTask task = new TimerTask() 
		{
			public void run()
			{
				m_logger.info("TimerTask.run: About to send coordinator emails");
				try
				{
					sendCoordinatorEmails();					
				}
				catch(Bn2Exception bn2e)
				{
					m_logger.error("ProductManager.startup: Bn2Exception whilst preparing scheduled coordinator emails : " + bn2e);
				}
			}
		};
		
		int iHour = ProductSettings.getHourOfDayToSendMovementCoordinatorNotificationEmails();
		if (iHour >= 0)
		{
			m_logger.info("ProductManager.initialize(): Scheduling coordinator email task : Hour : "+iHour);
			m_scheduleManager.scheduleDailyTask(task, iHour);
		}
	}


	private void scheduleReminderEmails()
	{
		TimerTask task = new TimerTask() 
		{
			public void run()
			{
				m_logger.info("TimerTask.run: About to send reminder emails");
				try
				{
					sendReminderEmails();					
				}
				catch(Bn2Exception bn2e)
				{
					m_logger.error("ProductManager.startup: Bn2Exception whilst preparing scheduled reminder emails : " + bn2e);
				}
			}
		};
		
		int iHour = ProductSettings.getHourOfDayToSendMovementReminderNotificationEmails();
		if (iHour >= 0)
		{
			m_logger.info("ProductManager.initialize(): Scheduling reminder email task : Hour : "+iHour);
			m_scheduleManager.scheduleDailyTask(task, iHour);
		}
	}


	private void scheduleAdminEmails()
	{
		TimerTask task = new TimerTask() 
		{
			public void run()
			{
				m_logger.info("TimerTask.run: About to send admin emails");
				try
				{
					
					sendAdminEmail();					
				}
				catch(Bn2Exception bn2e)
				{
					m_logger.error("ProductManager.startup: Bn2Exception whilst preparing scheduled admin emails : " + bn2e);
				}
			}
		};
		
		int iHour = ProductSettings.getHourOfDayToSendMovementAdminNotificationEmails();
		if (iHour >= 0)
		{
			m_logger.info("ProductManager.initialize(): Scheduling admin email task : Hour : "+iHour);
			m_scheduleManager.scheduleDailyTask(task, iHour);
		}
	}
	
	
	/**
	 * send an email to the admin email address containing a list of upcoming movements 
	 *
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void sendAdminEmail() throws Bn2Exception
	/*
	------------------------------------------------------------------------
	d17	29-Oct-2007		Matt Stevenson		Added scheduled tasks for emails
  	d18	30-Oct-2007		Matt Stevenson		Modified to send reminder emails
  	d19	11-Jan-2008		Matt Stevenson		Added product link to emails
  	d20	07-Feb-2008		Matt Stevenson		Modified date check
 	------------------------------------------------------------------------
	*/
	{
		m_logger.info("ProductManager.sendAdminEmail(): Sending admin emails...");
		Connection con = null;
				
		try
		{
			con = m_dataSource.getConnection();
			
			//get a list of all the product movements happening tomorrow...
			GregorianCalendar start = new GregorianCalendar();
			GregorianCalendar end = new GregorianCalendar();
			end.add(GregorianCalendar.DATE, ProductSettings.getAdminMovementNotificationPeriodInDays());
			
			String sSql = 	k_sMovementNotificationFields + 
							"HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND ArrivalDate >= ? AND ArrivalDate <= ? " +						  
							"ORDER BY p.NewCode";
			
			PreparedStatement psql = con.prepareStatement(sSql);
			int iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(start.getTime().getTime()));
			psql.setDate(iCol++, new java.sql.Date(end.getTime().getTime()));
					
			ResultSet rs = psql.executeQuery();
			String sArrivals = "";									
			while (rs.next())
			{	
				sArrivals += generateMovementNotificationString(rs, "arrives at", "ArrivalDate", true);	
			}
			
			if (!StringUtil.stringIsPopulated(sArrivals))
			{
				sArrivals = "No arrivals scheduled";
			}
			
			//send the email...
			psql.close();
						
			
			sSql = 	k_sMovementNotificationFields + 
			"HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND DepartureDate >= ? AND DepartureDate <= ? " +						  
			"ORDER BY p.NewCode";

			psql = con.prepareStatement(sSql);
			iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(start.getTime().getTime()));
			psql.setDate(iCol++, new java.sql.Date(end.getTime().getTime()));
	
			rs = psql.executeQuery();
			String sDepartures = "";						

			while (rs.next())
			{	
				sDepartures += generateMovementNotificationString(rs, "departs from", "DepartureDate", true);	
			}

			if (!StringUtil.stringIsPopulated(sDepartures))
			{
				sDepartures = "No departures scheduled";
			}

			psql.close();
			
			
			sSql = 	k_sMovementNotificationFields + 
			"HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND DepartureDate <= ? AND DepartureDate IS NOT NULL AND MovedOn = 0 " +						  
			"ORDER BY DepartureDate ASC ";

			psql = con.prepareStatement(sSql);
			iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(start.getTime().getTime()));
	
			rs = psql.executeQuery();
			String sOverdue = "";						

			while (rs.next())
			{	
				sOverdue += generateMovementNotificationString(rs, "should have departed from", "DepartureDate", true);	
			}

			if (!StringUtil.stringIsPopulated(sOverdue))
			{
				sOverdue = "No overdue departures";
			}

			psql.close();

			HashMap hmParams = new HashMap();
			hmParams.put(ProductConstants.k_sParam_Arrivals, sArrivals);
			hmParams.put(ProductConstants.k_sParam_Departures, sDepartures);
			hmParams.put(ProductConstants.k_sParam_Overdue, sOverdue);
			hmParams.put(FrameworkConstants.k_sTemplateParam, ProductConstants.k_sEmailTemplate_UpdateEmail);
			m_emailManager.sendTemplatedEmail(hmParams);
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException sqle)
			{
				// Do nothing.
			}
		
			m_logger.error("SQL Exception in ProductManager.sendAdminEmail: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.sendAdminEmail: " + e.getMessage());
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
				catch(SQLException sqle)
				{
					m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
				}
			}
		}
	}
	
	/**
	 * send reminder email to coordinators
	 *
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void sendReminderEmails() throws Bn2Exception
	/*
	------------------------------------------------------------------------
	d20		07-Feb-2008		Matt Stevenson		Created
 	------------------------------------------------------------------------
	*/
	{
		m_logger.info("ProductManager.sendReminderEmails(): Sending reminder emails...");
		Connection con = null;
				
		try
		{
			con = m_dataSource.getConnection();
			
			//get a list of all the product movements happening tomorrow...
			GregorianCalendar end = new GregorianCalendar();
			end.add(GregorianCalendar.DATE, ProductSettings.getMovementNotificationReminderDaysBefore());
			
			String sSql = k_sMovementNotificationFields +
						  "HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND ReminderEmail IS NOT NULL AND ArrivalDate = ? ";
			
			PreparedStatement psql = con.prepareStatement(sSql);
			int iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(end.getTime().getTime()));			
			
			ResultSet rs = psql.executeQuery();
			
			while (rs.next())
			{				
				String sMovement = generateMovementNotificationString(rs, "arrives at", "ArrivalDate", true);												
				sendReminderEmail(rs, sMovement);
			}
			
			psql.close();
			
			
			sSql = 	k_sMovementNotificationFields +
			  		"HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND ReminderEmail IS NOT NULL AND DepartureDate = ? ";

			psql = con.prepareStatement(sSql);
			iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(end.getTime().getTime()));			

			rs = psql.executeQuery();

			while (rs.next())
			{				
				String sMovement = generateMovementNotificationString(rs, "departs from", "DepartureDate", true);												
				sendReminderEmail(rs, sMovement);
			}

			psql.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException sqle)
			{
				// Do nothing.
			}
		
			m_logger.error("SQL Exception in ProductManager.sendReminderEmails: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.sendReminderEmails: " + e.getMessage());
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
				catch(SQLException sqle)
				{
					m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
				}
			}
		}
	}


	private String sendReminderEmail(ResultSet rs, String sMovement)
	throws SQLException, Bn2Exception
	{
		String sReminderEmail = rs.getString("ReminderEmail");
		if (StringUtil.isValidEmailAddress(sReminderEmail))
		{
			//send a reminder email...
			HashMap hmParams = new HashMap();
			hmParams.put(ProductConstants.k_sParam_EmailAddress, sReminderEmail);
			hmParams.put(FrameworkConstants.k_sTemplateParam, ProductConstants.k_sEmailTemplate_ReminderEmail);
			hmParams.put(ProductConstants.k_sParam_ProductList, sMovement);
			m_emailManager.sendTemplatedEmail(hmParams);
		}
		return sReminderEmail;
	}
	
	
	
	private String getLocationString (ResultSet a_rs) throws SQLException
	{
		String sLocationString = "";
		
		if (a_rs.getLong("eId") > 0)
		{
			sLocationString = " " + a_rs.getString("eName");
		}
		else
		{
			sLocationString = " " + a_rs.getString("Value") + " (" + a_rs.getString("cName") + ")";
		}
		
		return (sLocationString);
	}
	
	
	
	/**
	 * send emails to coordinators and admin user regarding upcoming product movements 
	 *
	 * @param
	 * @return void
	 * @throws Exception
	 */
	public void sendCoordinatorEmails() throws Bn2Exception
	/*
	------------------------------------------------------------------------
	d17	29-Oct-2007		Matt Stevenson		Added scheduled tasks for emails
  	------------------------------------------------------------------------
	*/
	{
		m_logger.info("ProductManager.sendCoordinatorEmails(): Sending coordinator emails...");
		Connection con = null;
		GregorianCalendar now = new GregorianCalendar();
				
		try
		{
			con = m_dataSource.getConnection();
			
			int iDays = ProductSettings.getCoordinatorMovementNotificationPeriodInDays();
			GregorianCalendar end = new GregorianCalendar();
			end.add(GregorianCalendar.DATE, iDays);
			
			String sSql = 	k_sMovementNotificationFields +
							"HAVING Description IS NOT NULL AND LocationName IS NOT NULL AND CoordinatorEmailAddress IS NOT NULL " +
							"AND ArrivalDate >= ? AND ArrivalDate <= ? AND EventId IS NOT NULL " +						  
							"ORDER BY EventId, ArrivalDate";
			
			PreparedStatement psql = con.prepareStatement(sSql);
			int iCol = 1;
			psql.setDate(iCol++, new java.sql.Date(now.getTime().getTime()));
			psql.setDate(iCol++, new java.sql.Date(end.getTime().getTime()));
			
			ResultSet rs = psql.executeQuery();
			String sProductList = "";
			long lLastEventId=0;
			HashMap hmParams = null;
			
			while (rs.next())
			{
				if (lLastEventId != rs.getLong("EventId"))
				{
					//send the email...
					sendCoordinatorEmail(hmParams, sProductList);
					
					//refresh the params...
					hmParams = new HashMap();
					hmParams.put(ProductConstants.k_sParam_EventName, rs.getString("LocationName"));
					hmParams.put(ProductConstants.k_sParam_EmailAddress, rs.getString("CoordinatorEmailAddress"));
					hmParams.put(FrameworkConstants.k_sTemplateParam, ProductConstants.k_sEmailTemplate_CoordinatorEmail);
					hmParams.put(ProductConstants.k_sParam_Days, String.valueOf(iDays));
					lLastEventId = rs.getLong("EventId");
					sProductList = "";
				}	
								
				sProductList += generateMovementNotificationString(rs, "arrives at", "ArrivalDate", false);				
			}
			
			//send the last email...
			sendCoordinatorEmail(hmParams, sProductList);
			psql.close();
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
			}
			catch(SQLException sqle)
			{
				// Do nothing.
			}
		
			m_logger.error("SQL Exception in ProductManager.sendCoordinatorEmails: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.sendCoordinatorEmails: " + e.getMessage());
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
				catch(SQLException sqle)
				{
					m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
				}
			}
		}		
	}
	
	
	private void sendCoordinatorEmail(HashMap a_hmParams, String a_sProductList)
	throws Bn2Exception
	{
		if (a_hmParams != null)
		{
			String sEmail = (String)a_hmParams.get(ProductConstants.k_sParam_EmailAddress);
			if (StringUtil.isValidEmailAddress(sEmail) && StringUtil.stringIsPopulated(a_sProductList))
			{
				a_hmParams.put(ProductConstants.k_sParam_ProductList, a_sProductList);
				m_emailManager.sendTemplatedEmail(a_hmParams);
			}
		}
	}
	
	
	private String generateMovementNotificationString(	ResultSet a_rs,
														String a_sNotificationDescriptor,
														String a_sNotificationDateColumnName, 
														boolean a_bIncludeProductLinks)
	throws SQLException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String notificationDetail = "";

		notificationDetail += a_rs.getString("OldCode") + "/" + StringUtil.getEmptyStringIfNull(a_rs.getString("NewCode")) + " : '" + a_rs.getString("Description") + "' "+a_sNotificationDescriptor+" '"+ a_rs.getString("LocationName") + "' on " + df.format(a_rs.getDate(a_sNotificationDateColumnName)) + "\n";

		if(a_bIncludeProductLinks)
		{
			String sUrl = FrameworkSettings.getApplicationUrl() + k_sViewProductAction + "?" + k_sIDParam + "=" + a_rs.getLong("pId") + "&" + k_sParam_Code + "=";
			String sCode = m_userManager.authoriseSignonWithoutLogin();
			sUrl = sUrl + sCode;												 
			notificationDetail += sUrl;
			notificationDetail += "\n";
		}
		
		notificationDetail += "\n";

		return notificationDetail;
	}
	
	
	/**
	 * Returns a list of Products.
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param SearchCriteria
	 *@param String sOrderBy
	 */
	public List<Product> search(DBTransaction a_dbTransaction, SearchCriteria a_searchCriteria, String a_sOrderBy)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1   	01-Feb-2005  Martin Wilson           Created
	 d2		18-Mar-2005	 Matt Stevenson			 Modified to use product movement
	 d3		21-Mar-2005	 Matt Stevenson			 Implemented search changes
	 d6		14-Jun-2005	 Matt Stevenson			 Added ordering
	 d7		22-Jun-2005	 Matt Stevenson			 Added order by clause parameter
	 d9		12-Dec-2005	 Matt Stevenson			 Modified to only search current location
	 d10 	17-Feb-2006	 Matt Stevenson			 Changed event searching to search all movements
	 d11	16-Jun-2006	 Matt Stevenson			 Modified year of manufacture
	 d12	22-Aug-2006	 Matt Stevenson			 Fixed problem null movements
	 d13	06-Dec-2006	 Matt Stevenson			 Added event country check
	 d15	16-Oct-2007	 Matt Stevenson			 Updated to include manufacturing fields
	 d21	05-Mar-2008	 Matt Stevenson			 Modified to include date check and next movement information
  	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		List<Product> results = new ArrayList<Product>();
		Product product = null;
		String sKeywords = null;
		String sOldCode = null;
		String sNewCode = null;
		String sModel = null;
		String sYearOfManufacture = null;
		HashMap hmProductsAdded = new HashMap();
		try
		{
			boolean bCurrentMovementCriteriaSpecified = (StringUtil.stringIsPopulated(a_searchCriteria.getLocationDescription())) || (a_searchCriteria.getCountryId() > 0);
			
			//convert the date values...
			Date dtFromDate = null;
			Date dtToDate = null;
			
			if (StringUtil.stringIsPopulated(a_searchCriteria.getFromDate()))
			{
				dtFromDate = new Date(FrameworkSettings.getStandardDateFormat().parse(a_searchCriteria.getFromDate()).getTime());
			}
			
			if (StringUtil.stringIsPopulated(a_searchCriteria.getToDate()))
			{
				dtToDate = new Date(FrameworkSettings.getStandardDateFormat().parse(a_searchCriteria.getToDate()).getTime());
			}
			boolean bMovementCriteriaSpecified = dtFromDate != null || dtToDate != null;
		
		
			if(a_dbTransaction == null)
			{
				con = m_dataSource.getConnection();
			}
			else
			{
				con = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT p.Id, v.Id, v.Value, p.Model, pt.Id, pt.Value,"
								+" ml.Id, ml.Value, "
								+ "p.ImageId, p.Description, p.OldCode, p.NewCode,p.OtherVehicle, p.ContactPerson, "
								+ "p.YearOfManufacture, p.DedicatedPackaging,p.TechnologySheetAvailable, p.Label, p.Length, "
								+ "p.Height, p.Width, p.GrossWeight, p.NetWeight, p.Comments, p.Status, p.StatusLastTreatmentName, p.StatusLastTreatmentDate, p.LastUpdateDate, p.LastUpdateDoneBy, " 
								+ "p.ManufacturerName, p.ManufacturerAddress, p.ManufacturerContactName, p.ManufacturerTelephone, p.ManufacturerEmailAddress, p.EquipmentSize, p.EquipmentAssemblyInstructions," 
								+ "p.ProductSegmentId, p.HsCode "
								+ "FROM Product p "
								+ "LEFT JOIN ListValue v ON p.VehicleId = v.Id "
								+ "LEFT JOIN ListValue pt ON p.ProductTypeId = pt.Id "
								+ "LEFT JOIN ListValue ml ON p.ManufacturingLocationId = ml.Id ";
			
			if (a_searchCriteria.getEventId() > 0)
			{
				sSql = sSql + "JOIN ProductMovement pm ON p.Id=pm.ProductId AND pm.EventId=? ";
			}
			
			sSql += "WHERE 1=1 ";
			
			if (a_searchCriteria.getKeywords() != null && a_searchCriteria.getKeywords().length()>0)
			{
				sKeywords = "%"+a_searchCriteria.getKeywords()+"%";
				sSql+=" AND (p.Description LIKE ? OR p.Comments LIKE ?)";
			}
			
			if (a_searchCriteria.getOldCode() != null && a_searchCriteria.getOldCode().length()>0)
			{
				sOldCode = "%" + a_searchCriteria.getOldCode() + "%";
				sSql = sSql + " AND p.OldCode LIKE ?";
			}
			
			if (a_searchCriteria.getNewCode() != null && a_searchCriteria.getNewCode().length()>0)
			{
				sOldCode = "%" + a_searchCriteria.getNewCode() + "%";
				sSql = sSql + " AND p.NewCode LIKE ?";
			}
			
			if (a_searchCriteria.getModel() != null && a_searchCriteria.getModel().length()>0)
			{
				sModel = "%" + a_searchCriteria.getModel() + "%";
				sSql = sSql + " AND p.Model LIKE ?";
			}
			
			if (StringUtil.stringIsPopulated(a_searchCriteria.getHsCode()))
			{
				sModel = "%" + a_searchCriteria.getHsCode() + "%";
				sSql = sSql + " AND p.HsCode LIKE ?";
			}
			
			if (a_searchCriteria.getYearOfManufacture() != null && a_searchCriteria.getYearOfManufacture().length()>0)
			{
				sYearOfManufacture = a_searchCriteria.getYearOfManufacture();
				sSql = sSql + " AND p.YearOfManufacture=?";
			}
			
			if (a_searchCriteria.getVehicle().getId() > 0)
			{
				sSql = sSql + " AND p.VehicleId=?";
			}
			
			if (a_searchCriteria.getProductType().getId() > 0)
			{
				sSql = sSql + " AND p.ProductTypeId=?";
			}
			
			if (a_searchCriteria.getManufacturingLocation().getId() > 0)
			{
				sSql = sSql + " AND p.ManufacturingLocationId=?";
			}
			
			if (a_searchCriteria.getDedicatedPackaging())
			{
				sSql = sSql + " AND p.DedicatedPackaging=1";
			}
			
			if (StringUtil.stringIsPopulated(a_searchCriteria.getProductStatusString()))
			{
				sSql = sSql + " AND p.Status=?";
			}
			
			if (a_searchCriteria.getProductSegment().getId() > 0)
			{
				sSql = sSql + " AND p.ProductSegmentId=?";
			}
			
			if (!a_searchCriteria.getProductSegmentIds().isEmpty() || a_searchCriteria.getUnknownProductSegments() != null)
			{
				sSql += " AND( ";
				
				if(a_searchCriteria.getUnknownProductSegments() != null)
				{
					sSql += " p.ProductSegmentId IS ";
					if(!a_searchCriteria.getUnknownProductSegments().booleanValue())
					{
						sSql += " NOT ";
					}					
					sSql += " NULL ";
					
					if(!a_searchCriteria.getProductSegmentIds().isEmpty())
					{
						sSql += " OR ";
					}
						
				}
				
				for (Iterator<Long> it = a_searchCriteria.getProductSegmentIds().iterator();it.hasNext();)
				{
					it.next();
					sSql += " p.ProductSegmentId=? ";
					if(it.hasNext())
					{
						sSql += " OR ";
					}
				}							
				
				sSql += " ) ";
			}
			
			if (a_sOrderBy != null)
			{
				sSql = sSql + " ORDER BY "+a_sOrderBy;
			}
			
			m_logger.debug("ProductManager.search : SQL : "+sSql);
			PreparedStatement psql = con.prepareStatement(sSql);
			int iField = 1;
			
			if (a_searchCriteria.getEventId() > 0)
			{
				psql.setLong(iField++, a_searchCriteria.getEventId());
			}
			
			if (sKeywords != null)
			{
				psql.setString(iField++, sKeywords);
				psql.setString(iField++, sKeywords);
			}			
			
			if (sOldCode != null)
			{
				psql.setString(iField++, sOldCode);
			}
			
			if (sNewCode != null)
			{
				psql.setString(iField++, sNewCode);
			}
			
			if (sModel != null)
			{
				psql.setString(iField++, sModel);
			}
			
			if (sYearOfManufacture != null)
			{
				psql.setString(iField++, sYearOfManufacture);
			}
			
			if (a_searchCriteria.getVehicle().getId() > 0)
			{
				psql.setLong(iField++, a_searchCriteria.getVehicle().getId());
			}
			
			if (a_searchCriteria.getProductType().getId() > 0)
			{
				psql.setLong(iField++, a_searchCriteria.getProductType().getId());
			}
			
			if (a_searchCriteria.getManufacturingLocation().getId() > 0)
			{
				psql.setLong(iField++, a_searchCriteria.getManufacturingLocation().getId());
			}
			
			if (StringUtil.stringIsPopulated(a_searchCriteria.getProductStatusString()))
			{
				psql.setString(iField++, a_searchCriteria.getProductStatusString());
			}
			
			if (a_searchCriteria.getProductSegment().getId() > 0)
			{
				psql.setLong(iField++, a_searchCriteria.getProductSegment().getId());
			}
			
			if (!a_searchCriteria.getProductSegmentIds().isEmpty())
			{				
				for (Long productSegmentId : a_searchCriteria.getProductSegmentIds())
				{
					psql.setLong(iField++, productSegmentId);
				}				
			}
			
			ResultSet rs = psql.executeQuery();
			long lLastId = 0;
						
			
			while(rs.next())
			{
				long lCurrentId = rs.getLong("p.Id");
				
				// Create the Product object:
				if (lLastId != lCurrentId)
				{
					product = buildProduct(a_dbTransaction,rs);					
					product.setPastMovements(getProductMovements(a_dbTransaction, lCurrentId, ProductConstants.k_iMovementStatus_Past));
					product.setFutureMovements(getProductMovements(a_dbTransaction, lCurrentId, ProductConstants.k_iMovementStatus_CurrentAndFuture));					
					setCurrentAndNextMovement(product);
					
					boolean bCurrentMovementMatch = false;					
					if (bCurrentMovementCriteriaSpecified)
					{
						bCurrentMovementMatch = checkForCurrentMovementMatch(a_searchCriteria, product);
					}
														
					
					boolean bFoundMovementMatch = false;
					if(bMovementCriteriaSpecified)
					{
						bFoundMovementMatch = checkForMovementMatch(product,dtFromDate, dtToDate);
					}
					
					
					//Add the product to the results?
					boolean bAdd = (!bCurrentMovementCriteriaSpecified || bCurrentMovementMatch) && (!bMovementCriteriaSpecified || bFoundMovementMatch);
					if (bAdd && !hmProductsAdded.containsKey(new Long(lCurrentId)))
					{
						hmProductsAdded.put(new Long(lCurrentId), null);
						results.add(product);
						lLastId = product.getId();
					}
				}
			}
			psql.close();
		}
		catch(ParseException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}
		
			m_logger.error("Date Parsing Exception in ProductManager.search: " + e.getMessage());
			throw new Bn2Exception("Date Parsing Exception in ProductManager.search: " + e.getMessage());
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.search: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.search: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (con != null)
				{
					try
					{
						con.commit();
						con.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

		return (results);
	}


	private boolean checkForCurrentMovementMatch(SearchCriteria a_searchCriteria, Product product)
	{
		boolean bCurrentMovementMatch;
		bCurrentMovementMatch = true;
		if (product.getFutureMovements() != null)
		{
			//see if we need to check the location info...
			if (product.getCurrentMovement() != null)
			{
				//check the dates / event id / country id / description...
				if ((
				    (product.getCurrentMovement().getCountry() == null || product.getCurrentMovement().getCountry().getId() != a_searchCriteria.getCountryId()) &&
					(product.getCurrentMovement().getEvent() == null || product.getCurrentMovement().getEvent().getCountry() == null || product.getCurrentMovement().getEvent().getCountry().getId() != a_searchCriteria.getCountryId()) &&
					(a_searchCriteria.getCountryId() > 0)
					) ||
				    (StringUtil.stringIsPopulated(a_searchCriteria.getLocationDescription()) && !StringUtil.containsAtLeastOneKeyword(a_searchCriteria.getLocationDescription(), product.getCurrentMovement().getLocationValue(), " "))
					)
				{
					bCurrentMovementMatch = false;
				}
			}
			else
			{
				bCurrentMovementMatch = false;
			}
		}
		else
		{
			bCurrentMovementMatch = false;
		}
		return bCurrentMovementMatch;
	}


	private boolean checkForMovementMatch(Product product, Date dtFromDate,Date dtToDate)
	{
		for (ProductMovement temporaryMov : product.getAllMovements())
		{							
			//check the dates...
			if (movementInDateRange(temporaryMov, dtFromDate, dtToDate))
			{
				return true;
			}
			
		}
		
		return false;
	}


	private void setCurrentAndNextMovement(Product product)
	{
		if (product.getFutureMovements() != null)
		{
			product.setCurrentMovement((ProductMovement)product.getFutureMovements().firstElement());
			
			if (product.getFutureMovements().size() > 1)
			{
				product.setNextMovement((ProductMovement)product.getFutureMovements().elementAt(1));
			}
		}
	}
	
	
	/*
	 * Returns an Product
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductId
	 */
	public Product getProduct (DBTransaction a_dbTransaction,
										long a_lProductId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d1  	01-Feb-2005  Martin Wilson           Created
	 d2		18-Mar-2005	 Matt Stevenson			 Removed reference to locations
	 d3		21-Mar-2005	 Matt Stevenson		     Added date sorting to movements
	 d4		23-Mar-2005	 Matt Stevenson			 Added country
	 d15	16-Oct-2007	 Matt Stevenson			 Updated to include manufacturing fields
  	 d18	30-Oct-2007	 Matt Stevenson			 Added reminder email to product movements
 	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Product product = null;
		
		try
		{
			if(a_dbTransaction == null)
			{
				con = m_dataSource.getConnection();
			}
			else
			{
				con = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT p.Id, v.Id, v.Value, p.Model, pt.Id, pt.Value,"
								+" ml.Id, ml.Value, "
								+ "p.ImageId, p.Description, p.OldCode,p.NewCode, p.OtherVehicle, p.ContactPerson, "
								+ "p.YearOfManufacture, p.DedicatedPackaging, p.TechnologySheetAvailable, p.Label, p.Length, "
								+ "p.Height, p.Width, p.GrossWeight,p.NetWeight, p.Comments, p.Status, p.StatusLastTreatmentName, p.StatusLastTreatmentDate, p.LastUpdateDate, p.LastUpdateDoneBy, "
								+ "p.ManufacturerName, p.ManufacturerAddress, p.ManufacturerContactName, p.ManufacturerTelephone, p.ManufacturerEmailAddress, p.EquipmentSize, p.EquipmentAssemblyInstructions, " 
								+ "p.ProductSegmentId, p.HsCode  "
								+ "FROM Product p "
								+ "LEFT JOIN ListValue v ON p.VehicleId = v.Id "
								+ "LEFT JOIN ListValue pt ON p.ProductTypeId = pt.Id "
								+ "LEFT JOIN ListValue ml ON p.ManufacturingLocationId = ml.Id "
								+ "WHERE p.id = ?";


			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lProductId);

			ResultSet rs = psql.executeQuery();
			
			while (rs.next())
			{
				// Create the Product object:
				product = buildProduct(a_dbTransaction,rs);

				// Get the image:
				if (rs.getLong("p.ImageId") > 0)
				{
					BImage image = m_imageManager.getImage(rs.getLong("p.ImageId"));
					product.setImage(image);	
				}
				
				product.setPastMovements(this.getProductMovements(a_dbTransaction, product.getId(), ProductConstants.k_iMovementStatus_Past));
				product.setFutureMovements(this.getProductMovements(a_dbTransaction, product.getId(), ProductConstants.k_iMovementStatus_CurrentAndFuture));
			}
			psql.close();
			
			if (product == null)
			{
				m_logger.error("ProductManager.getProduct: there is no Product with id " + a_lProductId + " in the database");
				throw new Bn2Exception("ProductManager.getProduct: there is no Product with id " + a_lProductId + " in the database");
			}
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.getProduct: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.getProduct: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (con != null)
				{
					try
					{
						con.commit();
						con.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

		return (product);
	}	
	
	
	/**
	 * Returns an ProductMovement
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductId
	 *@return ProductMovement
	 */
	public ProductMovement getProductMovement (DBTransaction a_dbTransaction,
										long a_lProductMovementId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d2		18-Mar-2005	 Matt Stevenson		Created
	 d3		21-Mar-2005	 Matt Stevenson		Modified to get country
	 d18	30-Oct-2007	 Matt Stevenson		Added reminder email to product movements
 	 ------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		ProductMovement productMovement = null;
		
		try
		{
			if(a_dbTransaction == null)
			{
				con = m_dataSource.getConnection();
			}
			else
			{
				con = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT pm.Id pmId, pm.EventId, pm.ProductId, pm.LocationId, lv.Value, "+
							  "pm.ArrivalDate, pm.DepartureDate, pm.MovedOn, "+
							  "c.Id cId, c.Name, pm.ReminderEmail "+
							  "FROM ProductMovement pm "+
							  "LEFT JOIN Country c ON c.Id=pm.CountryId "+
							  "LEFT JOIN ListValue lv ON pm.LocationId = lv.Id "+
							  "WHERE pm.Id=?";


			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lProductMovementId);

			ResultSet rs = psql.executeQuery();
			
			if (rs.next())
			{
				//build country if there is one...
				Country country = new Country();
				if (rs.getLong("cId") > 0)
				{
					country.setId(rs.getLong("cId"));
					country.setName(rs.getString("Name"));
				}
				
				// Create the Product object:
				productMovement = new ProductMovement();
				productMovement.setId(rs.getLong("pmId"));	
				productMovement.getEvent().setId(rs.getLong("EventId"));
				productMovement.setProductId(rs.getLong("ProductId"));
				productMovement.setLocationId(rs.getLong("LocationId"));
				productMovement.setLocationValue(rs.getString("Value"));
				productMovement.setArrivalDate(rs.getDate("ArrivalDate"));
				productMovement.setDepartureDate(rs.getDate("DepartureDate"));
				productMovement.setMovedOn(rs.getBoolean("MovedOn"));
				productMovement.setReminderEmail(rs.getString("ReminderEmail"));
				productMovement.setCountry(country);
				
				// Set the date strings:
				if (productMovement.getArrivalDate() != null)
				{
					productMovement.setArrivalDateString(FrameworkSettings.getStandardDateFormat().format(productMovement.getArrivalDate()));
				}
				if (productMovement.getDepartureDate() != null)
				{
					productMovement.setDepartureDateString(FrameworkSettings.getStandardDateFormat().format(productMovement.getDepartureDate()));
				}	
				
				//identify if this is the current movement or not...
				setIsCurrentProductMovement(a_dbTransaction, productMovement);
			}
			else
			{
				m_logger.error("ProductManager.getProductMovement: there is no ProductMovement with id " + a_lProductMovementId + " in the database");
				throw new Bn2Exception("ProductManager.getProductMovement: there is no ProductMovement with id " + a_lProductMovementId + " in the database");
			}
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.getProductMovement: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.getProductMovement: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (con != null)
				{
					try
					{
						con.commit();
						con.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

		return (productMovement);
	}	
	
	
	/**
	 * get the current product movement for this product...
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductId
	 *@return ProductMovement
	 */
	public ProductMovement getCurrentMovement (DBTransaction a_dbTransaction, long a_lProductId)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 	d9		12-Dec-2005		Matt Stevenson			Created
	 	d12		22-Aug-2006		Matt Stevenson			Fixed problem with null product movements
	 ------------------------------------------------------------------------
	 */
	{
		//get all the movements...
		Vector vecMovements = this.getProductMovements(a_dbTransaction, a_lProductId, ProductConstants.k_iMovementStatus_CurrentAndFuture);
		
		//simply return the first one...
		if (vecMovements == null || vecMovements.size() <= 0)
		{
			return (null);
		}
		return ((ProductMovement)vecMovements.firstElement());
	}
	
	/**
	 * gets the product movements for a given product id
	 *
	 *@param DBTransaction a_dbTransaction
	 *@param long a_lProductId
	 *@return Vector - product movements
	 */
	public Vector getProductMovements (DBTransaction a_dbTransaction,
										long a_lProductId, int a_iMovementStatus)
	throws Bn2Exception
	/*
	 ------------------------------------------------------------------------
	 d7		22-Jun-2005	 Matt Stevenson			Created
	 d8		19-Jul-2005	 Matt Stevenson			Modified to get upcoming movements
	 d13	06-Dec-2006	 Matt Stevenson			Added event country
	 d18	30-Oct-2007	 Matt Stevenson			Added reminder email to product movements
 	------------------------------------------------------------------------
	 */
	{
		Connection con = null;
		Vector vecProductMovements = null;
		
		try
		{
			if(a_dbTransaction == null)
			{
				con = m_dataSource.getConnection();
			}
			else
			{
				con = a_dbTransaction.getConnection();
			}

			String sSql = 	"SELECT " +
								"pm.Id pmId, " +
								"pm.EventId, " +
								"pm.ProductId, " +
								"pm.LocationId, " +
								"lv.Value, "+
								"pm.ArrivalDate, " +
								"pm.DepartureDate, " +
								"pm.MovedOn, " +
								"pm.CountryId, "+
								"e.Id eId, " +
								"e.Name eName, " +
								"e.StartDate, " +
								"e.EndDate, " +
								"e.NotVisible, "+
								"c.Id cId, " +
								"c.Name, " +
								"ec.Id ecId, " +
								"ec.Name ecName, " +
								"pm.ReminderEmail "+
							"FROM ProductMovement pm "+
							"LEFT JOIN Country c ON pm.CountryId=c.Id "+
							"LEFT JOIN Event e ON pm.EventId=e.Id "+
							"LEFT JOIN Country ec ON e.CountryId=ec.Id "+
							"LEFT JOIN ListValue lv ON pm.LocationId=lv.Id "+
							"WHERE pm.ProductId=? ";
			
			if (a_iMovementStatus == ProductConstants.k_iMovementStatus_CurrentAndFuture)
			{
				sSql = sSql + "AND pm.MovedOn=0 ";
			}
			else if (a_iMovementStatus == ProductConstants.k_iMovementStatus_Past)
			{
				sSql = sSql + "AND pm.MovedOn=1 ";
			}
			
			sSql = sSql + "ORDER BY pm.ArrivalDate";


			PreparedStatement psql = con.prepareStatement(sSql);
			psql.setLong(1, a_lProductId);

			ResultSet rs = psql.executeQuery();
			
			while (rs.next())
			{
				//build country if there is one...
				Country country = new Country();
				if (rs.getLong("cId") > 0)
				{
					country.setId(rs.getLong("cId"));
					country.setName(rs.getString("Name"));
				}
				
				//create the event if necessary...
				Event event = new Event();
				if (rs.getLong("eId") > 0)
				{
					event.setId(rs.getLong("eId"));
					event.setName(rs.getString("eName"));
					event.setStartDate(rs.getDate("e.StartDate"));
					event.setEndDate(rs.getDate("e.EndDate"));
					if (event.getStartDate() != null)
					{
						event.setStartDateStr(FrameworkSettings.getStandardDateFormat().format(event.getStartDate()));
					}
					if (event.getEndDate() != null)
					{
						event.setEndDateStr(FrameworkSettings.getStandardDateFormat().format(event.getEndDate()));
					}	
					event.setNotVisible(rs.getBoolean("e.NotVisible"));
					
					if (rs.getLong("ecId") > 0)
					{
						Country eventCountry = new Country();
						eventCountry.setId(rs.getLong("ecId"));
						eventCountry.setName(rs.getString("ecName"));
						event.setCountry(eventCountry);
					}
				}
				
				// Create the Product object:
				ProductMovement productMovement = new ProductMovement();
				productMovement.setId(rs.getLong("pmId"));	
				productMovement.setProductId(rs.getLong("ProductId"));
				productMovement.setLocationValue(rs.getString("Value"));
				productMovement.setLocationId(rs.getLong("LocationId"));
				productMovement.setArrivalDate(rs.getDate("ArrivalDate"));
				productMovement.setDepartureDate(rs.getDate("DepartureDate"));
				productMovement.setMovedOn(rs.getBoolean("MovedOn"));
				productMovement.setReminderEmail(rs.getString("ReminderEmail"));
				productMovement.setCountry(country);
				productMovement.setEvent(event);
				
				// Set the date strings:
				if (productMovement.getArrivalDate() != null)
				{
					productMovement.setArrivalDateString(FrameworkSettings.getStandardDateFormat().format(productMovement.getArrivalDate()));
				}
				if (productMovement.getDepartureDate() != null)
				{
					productMovement.setDepartureDateString(FrameworkSettings.getStandardDateFormat().format(productMovement.getDepartureDate()));
				}	
				
				//add to the vector...
				if (vecProductMovements == null)
				{
					vecProductMovements = new Vector();
				}
				
				vecProductMovements.add(productMovement);
			}
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					con.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.getProductMovements: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.getProductMovements: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (con != null)
				{
					try
					{
						con.commit();
						con.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

		return (vecProductMovements);
	}	
	
	
	/**
	 * Saves a Product.
	 * 
	 * @param a_dbTransaction
	 * @param a_Product
	 * @throws Bn2Exception
	 */
	public void saveProduct (DBTransaction a_dbTransaction, Product a_product) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   	01-Feb-2005  Martin Wilson         	Created
	  d2	18-Mar-2005  Matt Stevenson			Removed current location
	  d14	25-Oct-2007	 Matt Stevenson			Added status
	  d15	16-Oct-2007	 Matt Stevenson			Updated to include manufacturing fields
  	------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;
		
		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = null;

			PreparedStatement psql = null;

			if (a_product.getId() <= 0)
			{
				sSql = "INSERT INTO Product (VehicleId, Model, ProductTypeId, ManufacturingLocationId, ImageId, Description, "
					+ "OldCode,NewCode, OtherVehicle, ContactPerson, YearOfManufacture, DedicatedPackaging,TechnologySheetAvailable, Label, Length, "
					+ "Height, Width, GrossWeight, NetWeight,Comments, Status, StatusLastTreatmentName, StatusLastTreatmentDate, LastUpdateDate, LastUpdateDoneBy, "
					+ "ManufacturerName, ManufacturerAddress, ManufacturerTelephone, ManufacturerEmailAddress, ManufacturerContactName, EquipmentSize, EquipmentAssemblyInstructions,ProductSegmentId, HsCode) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
			else
			{
				sSql = "UPDATE Product SET VehicleId=?, Model=?, ProductTypeId=?, ManufacturingLocationId=?, ImageId=?, Description=?, "
					+ "OldCode=?,NewCode=?, OtherVehicle=?, ContactPerson=?, YearOfManufacture=?, DedicatedPackaging=?, TechnologySheetAvailable=?, Label=?, Length=?, "
					+ "Height=?, Width=?, GrossWeight=?,NetWeight=?, Comments=?, Status=?, StatusLastTreatmentName=?, StatusLastTreatmentDate=?, LastUpdateDate=?, LastUpdateDoneBy=?, "
					+ "ManufacturerName=?, ManufacturerAddress=?, ManufacturerTelephone=?, ManufacturerEmailAddress=?, ManufacturerContactName=?, EquipmentSize=?, EquipmentAssemblyInstructions=?,ProductSegmentId=?, HsCode=? "
					+ "WHERE Id=?";
			}

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );

			DBUtil.setFieldIdOrNull (psql, iField++, a_product.getVehicle());
			psql.setString (iField++, a_product.getModel());
			DBUtil.setFieldIdOrNull (psql, iField++, a_product.getProductType());
			DBUtil.setFieldIdOrNull (psql, iField++, a_product.getManufacturingLocation());
			DBUtil.setFieldIdOrNull (psql, iField++, a_product.getImage());	
			psql.setString (iField++, a_product.getDescription());	
			psql.setString (iField++, a_product.getOldCode());	
			psql.setString (iField++, a_product.getNewCode());
			psql.setString (iField++, a_product.getOtherVehicle());	
			psql.setString (iField++, a_product.getContactPerson());			
			psql.setString (iField++, a_product.getYearOfManufacture());			
			psql.setBoolean(iField++, a_product.getDedicatedPackaging());
			psql.setBoolean(iField++, a_product.getTechnologySheetAvailable());
			psql.setBoolean(iField++, a_product.getLabel());
			psql.setString (iField++, a_product.getLength());	
			psql.setString (iField++, a_product.getHeight());				
			psql.setString (iField++, a_product.getWidth());	
			psql.setString (iField++, a_product.getGrossWeight());				
			psql.setString (iField++, a_product.getNetWeight());
			psql.setString (iField++, a_product.getComments());	
			psql.setString (iField++, a_product.getStatus());
			psql.setString (iField++, a_product.getStatusLastTreatmentName());
			DBUtil.setFieldDateOrNull(psql, iField++, a_product.getStatusLastTreatmentDate());
			DBUtil.setFieldDateOrNull(psql, iField++, a_product.getLastUpdateDate());
			psql.setString (iField++, a_product.getLastUpdateDoneBy());
			psql.setString (iField++, a_product.getManufacturerName());
			psql.setString (iField++, a_product.getManufacturerAddress());
			psql.setString (iField++, a_product.getManufacturerTelephone());
			psql.setString (iField++, a_product.getManufacturerEmail());
			psql.setString (iField++, a_product.getManufacturerContactName());
			psql.setString (iField++, a_product.getEquipmentSize());
			psql.setString (iField++, a_product.getEquipmentAssemblyInstructions());
			DBUtil.setFieldIdOrNull (psql, iField++, a_product.getProductSegment());
			psql.setString (iField++, a_product.getHsCode());
			
			if (a_product.getId() <= 0)
			{
				psql.executeUpdate ();
				psql.close();
				
				// Get the Id for this new address:
				sSql = "SELECT LAST_INSERT_ID() AS NewId FROM Product";
				psql = cCon.prepareStatement ( sSql );
				ResultSet rs = psql.executeQuery ();
				if(rs.next ())
				{
					a_product.setId (rs.getLong ("NewId"));
				}
			}
			else
			{
				psql.setLong (iField++, a_product.getId());
				psql.executeUpdate ();
			}	
			psql.close();

		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.saveProduct : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.saveProduct : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}	
	
	
	/**
	 * Check the product old code
	 * 
	 * @param a_dbTransaction
	 * @param a_Product
	 * @throws Bn2Exception
	 */
	public void checkProductOldCode (DBTransaction a_dbTransaction, String a_sProductOldCode) 
	throws Bn2Exception, DuplicateProductOldCodeException
	 /*
	 ------------------------------------------------------------------------
	  d6	 14-Jun-2005  Matt Stevenson			Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;
		
		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT * FROM Product WHERE OldCode LIKE ?";
			PreparedStatement psql = cCon.prepareStatement(sSql);
			psql.setString(1, a_sProductOldCode);
			ResultSet rs = psql.executeQuery();
			boolean bDupe = false;
			
			if (rs.next())
			{
				bDupe = true;
			}
			psql.close();
						
			if (bDupe)
			{
				throw new DuplicateProductOldCodeException("ProductManager.checkProductCode : Product code (old) "+a_sProductOldCode+" already exists");
			}
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.checkProductOldCode : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.checkProductOldCode : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}
	
	
	/**
	 * Check the product new code
	 * 
	 * @param a_dbTransaction
	 * @param a_Product
	 * @throws Bn2Exception
	 */
	public void checkProductNewCode (DBTransaction a_dbTransaction, String a_sProductNewCode) 
	throws Bn2Exception, DuplicateProductNewCodeException	 
	{
		Connection cCon = null;
		
		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = "SELECT * FROM Product WHERE NewCode LIKE ?";
			PreparedStatement psql = cCon.prepareStatement(sSql);
			psql.setString(1, a_sProductNewCode);
			ResultSet rs = psql.executeQuery();
			boolean bDupe = false;
			
			if (rs.next())
			{
				bDupe = true;
			}
			psql.close();
						
			if (bDupe)
			{
				throw new DuplicateProductNewCodeException("ProductManager.checkProductCode : Product code (new) "+a_sProductNewCode+" already exists");
			}
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.checkProductNewCode : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.checkProductNewCode : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}
	

	/**
	 * Saves a Product Movement
	 * 
	 * @param a_dbTransaction
	 * @param a_productMovement
	 * @param a_bIsCurrentLocation - this movement should become the current location
	 * @throws Bn2Exception
	 */
	public void saveProductMovement (DBTransaction a_dbTransaction, 
									 ProductMovement a_productMovement, boolean a_bIsCurrentLocation) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d2	 18-Mar-2005  Matt Stevenson		Created
	  d3	 21-Mar-2005  Matt Stevenson		Modified to deal with country
	  d5	 18-Mar-2005  Matt Stevenson		Added current location check
  	  d18	 30-Oct-2007  Matt Stevenson		Added reminder email to product movements
 	------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = null;

			PreparedStatement psql = null;

			if (a_productMovement.getId() <= 0)
			{
				sSql = "INSERT INTO ProductMovement (EventId, ProductId, ArrivalDate, DepartureDate, MovedOn, CountryId, LocationId, ReminderEmail) "
					+ "VALUES (?,?,?,?,?,?,?,?)";
			}
			else
			{
				sSql = "UPDATE ProductMovement SET EventId=?, ProductId=?, ArrivalDate=?, DepartureDate=?, MovedOn=?, CountryId=?, LocationId=?, ReminderEmail=? "
						+ "WHERE Id=?";
			}

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );

			DBUtil.setFieldIdOrNull (psql, iField++, a_productMovement.getEvent());
			psql.setLong (iField++, a_productMovement.getProductId());
			DBUtil.setFieldDateOrNull(psql, iField++, a_productMovement.getArrivalDate());	
			DBUtil.setFieldDateOrNull(psql, iField++, a_productMovement.getDepartureDate());	
			psql.setBoolean (iField++, a_productMovement.getMovedOn());
			DBUtil.setFieldIdOrNull (psql, iField++, a_productMovement.getCountry());
			DBUtil.setFieldIdOrNull (psql, iField++, a_productMovement.getLocationId());
			psql.setString (iField++, a_productMovement.getReminderEmail());
						
			if (a_productMovement.getId() <= 0)
			{
				psql.executeUpdate ();
				psql.close();
				
				// Get the Id for this new address:
				sSql = "SELECT LAST_INSERT_ID() AS NewId FROM ProductMovement";
				psql = cCon.prepareStatement ( sSql );
				ResultSet rs = psql.executeQuery ();
				
				if(rs.next ())
				{
					a_productMovement.setId (rs.getLong ("NewId"));
				}
			}
			else
			{
				psql.setLong (iField++, a_productMovement.getId());
				psql.executeUpdate ();
			}	
			psql.close();
			
			//check if we need to move the products on so that this is the current location...
			if (a_bIsCurrentLocation)
			{
				moveToProductMovement(a_dbTransaction, a_productMovement);
			}
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.saveProductMovement : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.saveProductMovement : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}	
	
	
	/**
	 * move a product on to the next ProductMovement
	 * 
	 * @param a_dbTransaction
	 * @param a_lCurrentPMId
	 * @throws Bn2Exception
	 */
	public void moveProductOn (DBTransaction a_dbTransaction, 
									 long a_lCurrentPMId) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d2	 18-Mar-2005  Matt Stevenson			Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = "UPDATE ProductMovement SET MovedOn=1 WHERE Id=?";

			PreparedStatement psql = null;

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );
			psql.setLong(iField, a_lCurrentPMId);
			
			psql.executeUpdate ();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.moveProductOn : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.moveProductOn : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}	
	
	
	
	/**
	 * set the given product movement to be the current location of the product
	 * 
	 * @param a_dbTransaction
	 * @param a_lCurrentPMId
	 * @throws Bn2Exception
	 */
	public void moveToProductMovement (DBTransaction a_dbTransaction, 
									 ProductMovement a_productMovement) 
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d5	 18-Apr-2005  Matt Stevenson			Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			//get the current product movement...
			String sSql = "UPDATE ProductMovement SET MovedOn=1 WHERE ProductId=? "+
							  "AND ArrivalDate <= ? AND Id NOT IN (?)";

			PreparedStatement psql = null;

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );
			psql.setLong(iField++, a_productMovement.getProductId());
			DBUtil.setFieldDateOrNull(psql, iField++, a_productMovement.getArrivalDate());
			psql.setLong(iField++, a_productMovement.getId());
			psql.executeUpdate ();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.moveToProductMovement : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.moveToProductMovement : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}	
	
	
	/**
	 * get a boolean that defines whether the given movement is the current one...
	 * 
	 * @param a_dbTransaction
	 * @param ProductMovement a_productMovement
	 * @throws Bn2Exception
	 */
	public void setIsCurrentProductMovement (DBTransaction a_dbTransaction, 
									 ProductMovement a_productMovement) 
	throws Bn2Exception
	/*
	------------------------------------------------------------------------
	  d5	 18-Apr-2005  Matt Stevenson			Created
	------------------------------------------------------------------------
	*/
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if (a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			//get the current product movement...
			String sSql = "SELECT * FROM ProductMovement WHERE MovedOn=0 AND ProductId=? "+
							  "AND ArrivalDate <= ?";

			PreparedStatement psql = null;

			int iField = 1;
			psql = cCon.prepareStatement ( sSql );
			psql.setLong(iField++, a_productMovement.getProductId());
			DBUtil.setFieldDateOrNull(psql, iField++, a_productMovement.getArrivalDate());
			ResultSet rs = psql.executeQuery ();
			
			if (rs.next())
			{
				if (rs.getLong("Id") == a_productMovement.getId())
				{
					a_productMovement.setIsCurrentMovement(true);
				}
				else
				{
					a_productMovement.setIsCurrentMovement(false);
				}
			}
			else
			{
				a_productMovement.setIsCurrentMovement(true);
			}
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.setIsCurrentProductMovement : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.setIsCurrentProductMovement : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}	
	
	
	
	/**
	 * Deletes a Product
	 * 
	 * @param a_dbTransaction
	 * @param a_lProductId
	 */
	/*
	---------------------------------------------------------------
	d1		01-Feb-2005		Martin Wilson		Created 
	-----------------------------------------------------------------
	*/
	public void deleteProduct (DBTransaction a_dbTransaction, 
								  long a_lProductId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   01-Feb-2005  Martin Wilson         Created
	  d2	 18-Mar-2005  Matt Stevenson			Modified to deal with Product Movements
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}
			
			// Remove all the product at Product links:
			String sSql = "DELETE FROM ProductMovement WHERE ProductId=?";

			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lProductId);
			psql.executeUpdate ();
			psql.close();
			
			// Now delete the Product:
			sSql = "DELETE FROM Product WHERE Id=?";

			psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lProductId);			
			psql.executeUpdate ();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.deleteProduct: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.deleteProduct: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}		
	
	
	/**
	 * Deletes a Product Movement
	 * 
	 * @param a_dbTransaction
	 * @param a_lProductId
	 */
	public void deleteProductMovement (DBTransaction a_dbTransaction, 
								  long a_lProductMovementId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d2	 18-Mar-2005  Matt Stevenson			Created
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}
			
			// Remove all the product at Product links:
			String sSql = "DELETE FROM ProductMovement WHERE Id=?";

			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lProductMovementId);
			psql.executeUpdate ();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.deleteProductMovement: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.deleteProductMovement: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}	
	
	
	/**
	 * 
	 * Removes all references to a list value from products
	 * 
	 * @param a_dbTransaction
	 * @param a_lListValueId
	 */
	public void removeListValueReferences (DBTransaction a_dbTransaction, 
														long a_lListValueId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   	01-Feb-2005  	Martin Wilson       Created
	  d16	26-Oct-2007		Matt Stevenson		Modified to location id values
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}
			
			// Remove all the product links:
			String sSql = "UPDATE Product SET VehicleId = NULL WHERE VehicleId = ?";
			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lListValueId);
			psql.executeUpdate ();
			psql.close();
			
			sSql = "UPDATE Product SET ProductTypeId = NULL WHERE ProductTypeId = ?";
			psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lListValueId);
			psql.executeUpdate ();			
			psql.close();
			
			sSql = "UPDATE Product SET ManufacturingLocationId = NULL WHERE ManufacturingLocationId = ?";
			psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lListValueId);
			psql.executeUpdate ();					
			psql.close();
			
			sSql = "UPDATE ProductMovement SET LocationId = NULL WHERE LocationId = ?";
			psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lListValueId);
			psql.executeUpdate ();					
			psql.close();

		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.removeListValueReferences: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.removeListValueReferences: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}			
	
	/**
	 * 
	 * Removes all references to an event from products
	 * 
	 * @param a_dbTransaction
	 * @param long
	 */
	public void removeEventReferences (DBTransaction a_dbTransaction, 
												  long a_lEventId)
	throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   01-Feb-2005  Martin Wilson         Created
	  d2	 18-Mar-2005  Matt Stevenson			Modified to deal with Product Movements
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}
			
			// Remove all the product links:
			String sSql = "DELETE FROM ProductMovement WHERE EventId = ?";
			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setLong (1, a_lEventId);
			psql.executeUpdate ();		
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in ProductManager.removeEventReferences: " + e.getMessage());
			throw new Bn2Exception("SQL Exception in ProductManager.removeEventReferences: " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}

	}					
	

	/**
	 * Removes a product from an event
	 * 
	 * @param a_dbTransaction
	 * @param a_lProductId
	 * @param a_lEventId
	 * @throws Bn2Exception
	 */
	public void removeProductEvent (DBTransaction a_dbTransaction, 
													 long a_lProductId,
													 long a_lEventId) 
	throws Bn2Exception
	/*
	---------------------------------------------------------------
	d1		02-Feb-2005		Martin Wilson		Created 
	d2		18-Mar-2005		Matt Stevenson		Modified to make use of Product Movements
	-----------------------------------------------------------------
	*/
	{
		Connection cCon = null;

		try
		{
			// Get a connection from the transaction or pool:
			if(a_dbTransaction == null)
			{
				cCon = m_dataSource.getConnection();
			}
			else
			{
				cCon = a_dbTransaction.getConnection();
			}

			String sSql = "DELETE FROM ProductMovement WHERE ProductId=? AND EventId=?";
			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql = cCon.prepareStatement ( sSql );	
			psql.setLong(1, a_lProductId);
			psql.setLong(2, a_lEventId);
			
			psql.executeUpdate();
			psql.close();
		}
		catch(SQLException e)
		{
			if(a_dbTransaction == null)
			{
				try
				{
					cCon.rollback();
				}
				catch(SQLException sqle)
				{
					// Do nothing.
				}
			}

			m_logger.error("SQL Exception in EventManager.removeProductFromEvent : " + e.getMessage());
			throw new Bn2Exception("SQL Exception in EventManager.removeProductFromEvent : " + e.getMessage());
		}
		finally
		{
			if(a_dbTransaction == null)
			{
				// Return the connection to the pool:
				if (cCon != null)
				{
					try
					{
						cCon.commit();
						cCon.close();
					}
					catch(SQLException sqle)
					{
						m_logger.error("Sorry, SQL Exception whilst trying to close connection " + sqle.getMessage());
					}
				}
			}
		}
	}		
	
	
	/**
	 * Check if the movement falls within the specified date range...
	 * 
	 * @param ProductMovement
	 * @param FromDate
	 * @param ToDate
	 * @return boolean
	 */
	public boolean movementInDateRange (ProductMovement a_mov, Date a_dtFromDate, Date a_dtToDate) 
	/*
	---------------------------------------------------------------
	d9		12-Dec-2005		Matt Stevenson			Created
	-----------------------------------------------------------------
	*/
	{
		//set the times to compare
		long lArrivalTime = 0;
		long lDepartureTime = 0;
		
		if (a_dtFromDate != null)
		{
			lArrivalTime = a_dtFromDate.getTime();
		}
		
		if (a_dtToDate != null)
		{
			lDepartureTime = a_dtToDate.getTime();
		}
		else
		{
			lDepartureTime = Long.MAX_VALUE;
		}
		
		if ((a_mov.getArrivalDate() != null && a_mov.getArrivalDate().getTime() >= lArrivalTime && a_mov.getArrivalDate().getTime() <= lDepartureTime) ||
		    (a_mov.getDepartureDate() != null && a_mov.getDepartureDate().getTime() >= lArrivalTime && a_mov.getDepartureDate().getTime() <= lDepartureTime))
		{
			return (true);
		}
		
		return (false);
	}
	
	
	
	private Product buildProduct (DBTransaction a_dbTransaction, ResultSet a_rs) throws SQLException,Bn2Exception
	{
		Product product = new Product();
		product.setId(a_rs.getLong("p.Id"));	
		product.getVehicle().setId(a_rs.getLong("v.Id"));				
		product.getVehicle().setValue(a_rs.getString("v.Value"));
		product.setModel(a_rs.getString("p.Model"));				
		product.getProductType().setId(a_rs.getLong("pt.Id"));				
		product.getProductType().setValue(a_rs.getString("pt.Value"));		
		product.getManufacturingLocation().setId(a_rs.getLong("ml.Id"));				
		product.getManufacturingLocation().setValue(a_rs.getString("ml.Value"));	
		product.getImage().setId(a_rs.getLong("p.ImageId"));					
		product.setDescription(a_rs.getString("p.Description"));				
		product.setOldCode(a_rs.getString("p.OldCode"));	
		product.setNewCode(a_rs.getString("p.NewCode"));
		product.setOtherVehicle(a_rs.getString("p.OtherVehicle"));				
		product.setContactPerson(a_rs.getString("p.ContactPerson"));		
		product.setYearOfManufacture(a_rs.getString("p.YearOfManufacture"));				
		product.setDedicatedPackaging(a_rs.getBoolean("p.DedicatedPackaging"));						
		product.setTechnologySheetAvailable(a_rs.getBoolean("p.TechnologySheetAvailable"));
		product.setLabel(a_rs.getBoolean("p.Label"));					
		product.setLength(a_rs.getString("p.Length"));	
		product.setHeight(a_rs.getString("p.Height"));					
		product.setWidth(a_rs.getString("p.Width"));	
		product.setGrossWeight(a_rs.getString("p.GrossWeight"));					
		product.setNetWeight(a_rs.getString("p.NetWeight"));
		product.setComments(a_rs.getString("p.Comments"));
		product.setStatus(a_rs.getString("p.Status"));
		product.setStatusLastTreatmentName(a_rs.getString("p.StatusLastTreatmentName"));
		product.setStatusLastTreatmentDate(a_rs.getDate("p.StatusLastTreatmentDate"));
		product.setLastUpdateDate(a_rs.getDate("p.LastUpdateDate"));
		product.setLastUpdateDoneBy(a_rs.getString("p.LastUpdateDoneBy"));
		product.setManufacturerName(a_rs.getString("p.ManufacturerName"));
		product.setManufacturerAddress(a_rs.getString("p.ManufacturerAddress"));
		product.setManufacturerTelephone(a_rs.getString("p.ManufacturerTelephone"));
		product.setManufacturerEmail(a_rs.getString("p.ManufacturerEmailAddress"));
		product.setManufacturerContactName(a_rs.getString("p.ManufacturerContactName"));
		product.setEquipmentSize(a_rs.getString("p.EquipmentSize"));
		product.setEquipmentAssemblyInstructions(a_rs.getString("p.EquipmentAssemblyInstructions"));
		product.setProductSegment(m_productSegmentManager.getProductSegment(a_dbTransaction,a_rs.getLong("p.ProductSegmentId")));
		product.setHsCode(a_rs.getString("p.HsCode"));
		
		return (product);
	}
	
	
	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
	
	public void setImageManager(ImageManager a_imageManager)
	{
		m_imageManager = a_imageManager;
	}	
		
	public void setScheduleManager (ScheduleManager a_scheduleManager)
	{
		m_scheduleManager = a_scheduleManager;
	}
	
	public void setEmailManager (EmailManager a_emailManager)
	{
		m_emailManager = a_emailManager;
	}
	
	public void setUserManager (UserManager a_userManager)
	{
		m_userManager = a_userManager;
	}
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}
}

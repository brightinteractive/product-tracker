package com.bright.producttracker.product.constant;

/**
 * Bright Interactive, Reputable Trades
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * ApplicationConstants.java
 *
 * Contains the ApplicationConstants class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1	02-Feb-2005		Martin Wilson		Created
 d2	24-Mar-2005		Martin Wilson		Added k_sParam_FieldOrderParam 
 d3	26-Jul-2010		Kevin Bennett		Modified for old/new code and product segments fields
 --------------------------------------------------------------------------------
 */

/**
 * Constants for products
 * 
 * @author Bright Interactive
 * @version d1
 */
public interface ProductConstants
{	
	public static final String k_sReportField_Location = "Location";
	public static final String k_sReportField_NextLocation = "NextLocation";
	public static final String k_sReportField_LocationDates = "LocationDates";
	public static final String k_sReportField_Events = "Events";
	public static final String k_sColumnName_ArrivalDate = "Arrival";
	public static final String k_sColumnName_DepartureDate = "Departure";
	public static final String k_sReportField_DedicatedPackaging = "DedicatedPackaging";
	public static final String k_sReportField_TechnologySheetAvailable = "TechnologySheetAvailable";
	public static final String k_sReportField_Label = "Label";
	
	//html form field names
	public static final String k_sParam_ProductId = "productid";
	public static final String k_sParam_EventId = "eventid";
	public static final String k_sParam_UseLastResults = "last";
	public static final String k_sParam_GoBack = "back";
	public static final String k_sParam_ReportField = "fm_";
	public static final String k_sParam_ReportListField = "li_";
	public static final String k_sParam_ReportFieldNextLocation = k_sParam_ReportListField + k_sReportField_NextLocation;
	public static final String k_sParam_ReportFieldLocation = k_sParam_ReportListField + k_sReportField_Location;
	public static final String k_sParam_ReportFieldLocationDates = k_sParam_ReportListField + k_sReportField_LocationDates;
	public static final String k_sParam_ReportFieldEvents = k_sParam_ReportListField + k_sReportField_Events;
	public static final String k_sParam_Print = "print";
	public static final String k_sParam_Sort = "sort";
	public static final String k_sParam_All = "all";
	
	public static final int k_iSort_Event = 1;
	public static final int k_iSort_Location = 2;
	public static final String k_sSort_Event = "event";
	public static final String k_sSort_Location = "location";
	
	public static final String k_sNoOrderConstant = "none";
	
	public static final String k_sSortConstant_OldCode = "OldCode";
	public static final String k_sSortConstant_NewCodeThenDescription = "NewCode, Description";
	
	public static final String k_sGetMethodStart = "get";
	public static final String k_sClassName_Product = "com.bright.producttracker.product.bean.Product";
	public static final String k_sClassName_ListValue = "com.bright.framework.simplelist.bean.ListValue";
	public static final String k_sClassName_Date = "java.util.Date";
	public static final String k_sClassName_ProductSegment = "com.bright.producttracker.productsegment.bean.ProductSegment";

	public static final String k_sParam_FieldOrderParam = "fieldOrder";	
	
	public static final String k_sLocationType_Current = "current";
	
	public static final String k_sNoneValue = "none";
	
	public static final String k_sStringNoCriteriaString = "0:" + k_sNoneValue;
	public static final String k_sDefaultOrdering = "p.NewCode";
	
	public static final String k_sForward_NoSavedSearch = "NoSavedSearch";
	
	public static final String k_sParam_EventName = "eventName";
	public static final String k_sParam_ProductList = "productList";
	public static final String k_sParam_EmailAddress = "email";
	public static final String k_sParam_Arrivals = "arrivals";
	public static final String k_sParam_Departures = "departures";
	public static final String k_sParam_Overdue = "overdue";
	public static final String k_sParam_Days = "days";
	public static final String k_sEmailTemplate_CoordinatorEmail = "coordinator";
	public static final String k_sEmailTemplate_UpdateEmail = "update";
	public static final String k_sEmailTemplate_ReminderEmail = "reminder";
	
	public static final String k_sForward_AddCancel = "AddCancel";
	
	public static final int k_iMovementStatus_CurrentAndFuture = 0;
	public static final int k_iMovementStatus_Past = 1;
	public static final int k_iMovementStatus_All = 2;
}

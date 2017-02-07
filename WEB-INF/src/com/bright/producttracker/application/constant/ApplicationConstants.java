package com.bright.producttracker.application.constant;

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
 d1	18-Oct-2004		Chris Preager		Created
 --------------------------------------------------------------------------------
 */

/**
 * Application-wide constants
 * 
 * @author Bright Interactive
 * @version d1
 */
public interface ApplicationConstants
{
    public static final String k_sForward_FailedValidation = "FailedValidation";

	// Category Type Ids = trades categories, areas and jokes 
	public static final long k_lCategoryTypeId_Services = 1;
	public static final long k_lCategoryTypeId_Areas = 2;
	public static final long k_lCategoryTypeId_Monthly = 3;
	
	// Action forward names
	public static final String ADMIN_KEY = "Admin";
	public static final String TRADES_KEY = "CompanyAdmin";
	public static final String TRADES_EMPLOYEE_KEY = "Employee";
	public static final String PUBLIC_KEY = "Public";
	public static final String VISITOR_KEY = "Visitor";
	public static final String CANCEL_KEY = "Cancel";
	public static final String BACK_KEY = "Back";
	public static final String NEXT_KEY = "Next";

	// Dates
	public static final String k_sDateParam = "date";
	public static final String k_sDateFormat = "dd/MM/yyyy";

	// Admin User
	public static final long k_lAdminUserId = 1;
	
	//Database naming
	public static final String k_sTableName_CompanyServiceCategory = "CompanyServiceCategory";
	public static final String k_sTableName_CompanyServesAreaCategory = "CompanyServesAreaCategory";
	public static final String k_sFieldName_CompanyId = "CompanyId";
	
	//html form field names
	public static final String k_sParam_CancelButton = "cancel";
	public static final String k_sParam_BackButton = "back";
	public static final String k_sParam_Code = "code";
	
	public static final String k_sParam_Ordering = "ordering";
	
	public static final String k_sDefaultSort_Events = "e.Name";
	
	public static final String k_sAdminUsername = "admin";
	public static final String k_sViewProductAction = "/action/viewProduct";
}

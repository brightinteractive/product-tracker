package com.bright.producttracker.product.action;

import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.framework.user.bean.UserProfile;
import com.bright.producttracker.application.constant.ApplicationConstants;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.form.SearchForm;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.user.bean.PTUserProfile;

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
 d1	31-Feb-2005		Martin Wilson		Created
 d2	14-Jun-2005		Matt Stevenson		Modified execute method
 d3	22-Jun-2005		Matt Stevenson		Added result ordering
 d4	23-Jun-2005		Matt Stevenson		Modified execute method
 d5	24-Oct-2007		Matt Stevenson		Modified execute method
 --------------------------------------------------------------------------------
 */

/**
 * Shows the events, to enable the admin user to edit them or add new ones
 * 
 * @author Bright Interactive
 * @version d1
 */
public class SearchAction extends Bn2Action implements ProductConstants, ApplicationConstants
{
	protected ProductManager m_productManager = null;
	
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
	 d1	31-Feb-2005	Martin Wilson		Created
	 d2	14-Jun-2005	Matt Stevenson		Modified to format paired search criteria
	 d3	22-Jun-2005	Matt Stevenson		Added ordering to search call
	 d4	23-Jun-2005	Matt Stevenson		Passed report searching param on
	 d5	24-Oct-2007	Matt Stevenson		Modified search criteria usage
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		List<Product> vecResults = null;
		SearchForm form = (SearchForm)a_form;		
		PTUserProfile userProfile = (PTUserProfile)UserProfile.getUserProfile(a_request);
				
		try
		{
			// See if we want to use the last search results:
			String sUseLast = a_request.getParameter(k_sParam_UseLastResults);
			String sGoBack = a_request.getParameter(k_sParam_GoBack);
			SearchCriteria criteria = null;
			
			if (sUseLast != null)
			{
				// Get the last criteria
				criteria = userProfile.getLastSearchCriteria();
				
				if (criteria == null)
				{
					//forward to the no saved search page...
					return (a_mapping.findForward(k_sForward_NoSavedSearch));
				}
			}
			else if (sGoBack != null)
			{
				criteria = userProfile.getGoBackSearchCriteria();
			}
			else
			{			
				// Get the critera from the form:
				criteria = form.getSearchCriteria();
			}
				
			//seperate any combined information (ie. event, vehicle etc.)
			String[] aValues = criteria.getEventString().split(":");
			criteria.setEventId(Long.parseLong(aValues[0]));
			if (!(aValues[1].equals(k_sNoneValue)))
			{
				criteria.setEventName(aValues[1]);
			}
			
			aValues = criteria.getVehicleString().split(":");
			criteria.getVehicle().setId(Long.parseLong(aValues[0]));
			if (!(aValues[1].equals(k_sNoneValue)))
			{
				criteria.getVehicle().setValue(aValues[1]);
			}
			
			aValues = criteria.getProductTypeString().split(":");
			criteria.getProductType().setId(Long.parseLong(aValues[0]));
			if (!(aValues[1].equals(k_sNoneValue)))
			{
				criteria.getProductType().setValue(aValues[1]);
			}
			
			aValues = criteria.getManufacturingLocationString().split(":");
			criteria.getManufacturingLocation().setId(Long.parseLong(aValues[0]));
			if (!(aValues[1].equals(k_sNoneValue)))
			{
				criteria.getManufacturingLocation().setValue(aValues[1]);
			}
			
			// see if we have an order by selection...
			String sOrderBy = a_request.getParameter(k_sParam_Ordering);
			
			if (sOrderBy == null)
			{
				//use the default ordering...
				sOrderBy = k_sDefaultOrdering;
			}
			
			if (sOrderBy.equals(k_sNoOrderConstant))
			{
				sOrderBy = null;
			}
			else if (sOrderBy.equals(k_sSort_Event))
			{
				sOrderBy = null;
				form.setReportSort(k_iSort_Event);
			}
			else if (sOrderBy.equals(k_sSort_Location))
			{
				sOrderBy = null;
				form.setReportSort(k_iSort_Location);
			}
			
			m_logger.debug("SearchAction.execute : About to order by "+sOrderBy);
			
			// Get the results:
			vecResults = m_productManager.search(null, criteria, sOrderBy);
			
			// Set in the user profile for returning to:
			if (!criteria.getAll())
			{
				userProfile.setLastSearchCriteria(criteria);
			}
			userProfile.setGoBackSearchCriteria(criteria);
			userProfile.setLastSearchResults(vecResults);
			
			form.setAll(criteria.getAll());
			
			// Set in the form:
			form.setResults(vecResults);
				
			afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY); 
		}
		catch (Bn2Exception bn2)
		{
			m_logger.error("Bn2Exception in SearchAction: " + bn2.getMessage());
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY); 
		}
		
		return (afForward);
	}
	
	/**
	 * Sets the product manager. This method is called in the baseclass when linking in components,
	 * as specified in the framework config file.
	 *
	 * @param EventManager the event manager component to use.
	 */
	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
	
}

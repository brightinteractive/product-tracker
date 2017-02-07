package com.bright.producttracker.movementtracker.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.bn2web.common.action.Bn2Action;
import com.bn2web.common.exception.Bn2Exception;
import com.bright.producttracker.movementtracker.form.MovementTrackerForm;
import com.bright.producttracker.movementtracker.service.MovementTrackerDataFormatter;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.SearchCriteria;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.service.ProductManager;

public class FormatMovementTrackerDataAction extends Bn2Action
{
	private ProductManager m_productManager;
	private MovementTrackerDataFormatter m_movementTrackerDataFormatter;
	
	public ActionForward execute(ActionMapping a_mapping,
			  ActionForm a_form,
			  HttpServletRequest a_request,
			  HttpServletResponse a_response)
	throws Bn2Exception
	{						
		try
		{			
			MovementTrackerForm form = (MovementTrackerForm) a_form;
			List<Product> products = m_productManager.search(null, form.getSearchCriteria(), ProductConstants.k_sSortConstant_NewCodeThenDescription);
			File dataFile = m_movementTrackerDataFormatter.createFormattedDataFile(products);
			
			a_response.setContentType(m_movementTrackerDataFormatter.getDownloadContentType());
			a_response.setHeader("Content-Disposition","attachment; filename=product-movement-tracker."+m_movementTrackerDataFormatter.getDownloadFileExtention());
			FileUtils.copyFile(dataFile, a_response.getOutputStream());
		} 
		catch (IOException e)
		{
			a_mapping.findForward(SYSTEM_FAILURE_KEY);
		}
		
		return null;
	}

	public void setProductManager(ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}

	public void setMovementTrackerDataFormatter(MovementTrackerDataFormatter a_movementTrackerDataFormatter)
	{
		m_movementTrackerDataFormatter = a_movementTrackerDataFormatter;
	}
}

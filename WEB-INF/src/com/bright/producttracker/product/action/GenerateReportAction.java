package com.bright.producttracker.product.action;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
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
import com.bright.framework.constant.FrameworkConstants;
import com.bright.framework.file.service.FileStoreManager;
import com.bright.framework.simplelist.bean.ListValue;
import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.util.FileUtil;
import com.bright.framework.util.StringUtil;
import com.bright.producttracker.application.constant.ApplicationConstants;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.product.bean.ReportLine;
import com.bright.producttracker.product.constant.ProductConstants;
import com.bright.producttracker.product.form.ReportForm;
import com.bright.producttracker.product.service.ProductManager;
import com.bright.producttracker.productsegment.bean.ProductSegment;
import com.bright.producttracker.productsegment.service.ProductSegmentManager;
import com.bright.producttracker.user.bean.PTUserProfile;

/**
 * Bright Interactive, product-tracker
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * GenerateReportAction
 *
 * Contains the GenerateReportAction class.
 */

/*
 Ver	Date				Who					Comments
 --------------------------------------------------------------------------------
 d1		22-Mar-2005		Matt Stevenson		Created
 d2		23-Mar-2005		Matt Stevenson		Added validation
 d3		24-Mar-2005		Martin Wilson		Changed to support specified field orders 
 d4		22-Jun-2005		Matt Stevenson		Initial work on adding locations and events to reports
 d5		23-Jun-2005		Matt Stevenson		Work on sorting
 d6		30-Jun-2005		Matt Stevenson		Modified to retrieve search results with sorting
 d7		19-Jul-2005		Matt Stevenson		Changed retrieval of product movements
 d8		28-Jul-2005		Matt Stevenson		Fixed problem with ordering
 d9		05-Dec-2005		Matt Stevenson		Modified true/false to yes/n
 d10	12-Dec-2005		Matt Stevenson		Changed to only take into account current location
 d11	03-Nov-2006		Matt Stevenson		Modified execute method
 d12	11-Mar-2008		Matt Stevenson		Modified to include next location
 d13	27-Mar-2008		Matt Stevenson		Modified to allow for next movement date
 d14	26-Jul-2010		Kevin Bennett		Modified for old/new code, gross/net weight and product segment fields
 --------------------------------------------------------------------------------
 */

/**
 * GenerateReportAction
 * 
 * @author Bright Interactive
 * @version d1
 */
public class GenerateReportAction extends Bn2Action implements ApplicationConstants, ProductConstants
{
	private ProductManager m_productManager = null;
	protected ProductSegmentManager m_productSegmentManager = null;
	private FileStoreManager m_fileStoreManager = null;
	
	/**
	 * The execute method of the Action.
	 *
	 * @param ActionMapping a_mapping - the action mapping [explain it here].
	 * @param ActionForm a_form - the action form
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
	 d1	22-Mar-2005		Matt Stevenson			Created
	 d3	24-Mar-2005		Martin Wilson			Changed to support specified field orders 	 
	 d4	22-Jun-2005		Matt Stevenson			Added location and events to report
	 d5	23-Jun-2005		Matt Stevenson			Added sorting changed boolean settings
	 d7	19-Jul-2005		Matt Stevenson			Modified retrieval of product movements
	 d8	28-Jul-2005		Matt Stevenson			Fixed problem with null orderby param
	 d9	05-Dec-2005		Matt Stevenson			Modified true/false to yes/n
	 d10	12-Dec-2005		Matt Stevenson			Changed to only take into account current location
	 d11	03-Nov-2006		Matt Stevenson			Modified to set field order
	 ------------------------------------------------------------------------
	 */
	{
		// Go to the default:
		ActionForward afForward = null;
		ReportForm form = (ReportForm)a_form;		
		String sErrorString = "An error occurred generating the report please contact Bright Interactive";
		
		int iPrint = getIntParameter(a_request, k_sParam_Print);
		if (iPrint > 0)
		{
			form.setIsPrint(true);
		}
		
		try
		{
			PTUserProfile userProfile = (PTUserProfile)UserProfile.getUserProfile(a_request);
			
			// identify which fields to to display from the products...
			String sParamName = null;
			String sMethodName = null;
			Vector vecColumnNames = new Vector();
			Vector vecFullColumnNames = new Vector();
			Vector vecMethodNames = new Vector();
			boolean bShowLocations = false;
			boolean bShowNextLocation = false;
			boolean bShowLocationDates = false;
			boolean bShowEvents = false;	
			//int iSorting = getIntParameter(a_request, k_sParam_Sort);
			
			// See if the fields names have been supplied in a comma separated list.
			// They will have if the user's browser has JS turned on:
			String sFieldInOrder = a_request.getParameter(k_sParam_FieldOrderParam);
			
			if (sFieldInOrder == null || sFieldInOrder.length()==0)
			{
				// No, so JS is off. Get all the parameters and put into a list in default order:
				sFieldInOrder = "";
		
				Enumeration e = a_request.getParameterNames();				
				
				while (e.hasMoreElements())
				{
					sParamName = (String)e.nextElement();
					
					if (sParamName.length() >= k_sParam_ReportField.length())
					{
						if ((sParamName.startsWith(k_sParam_ReportField)) || (sParamName.startsWith(k_sParam_ReportListField)))
						{
							// This is one of the parameters, so add to the list:
							sFieldInOrder += sParamName + ",";
						}
					}
				}			
				
				// Remove the last comma:
				if (sFieldInOrder.length()>0)
				{
					sFieldInOrder = sFieldInOrder.substring(0, sFieldInOrder.length()-1);
				}
				
			}
			
			//set the field order in the form...
			form.setFieldOrder(sFieldInOrder);
			
			// Now go through the comma separated list.
			StringTokenizer st = new StringTokenizer(sFieldInOrder, ",");
			
			while (st.hasMoreTokens())
			{
				sParamName = st.nextToken();
				
				if (sParamName.startsWith(k_sParam_ReportField))
				{
					//get the method name - value of the parameter
					sMethodName = a_request.getParameter(sParamName);				
					
					//use the name of the parameter and convert to humar readable column name
					vecFullColumnNames.add(sParamName);
					sParamName = (sParamName.substring(k_sParam_ReportField.length()-1, sParamName.length())).replaceAll("_", " ");
					vecColumnNames.add(sParamName);
					vecMethodNames.add(sMethodName);		
				}
				else if (sParamName.startsWith(k_sParam_ReportListField))
				{
					
					String sStoreValue = (sParamName.substring(k_sParam_ReportListField.length(), sParamName.length())).replaceAll("_", " ");
					
					//can only show the locations or the events not both...
					if (sParamName.equals(k_sParam_ReportFieldLocation) || 
						sParamName.equals(k_sParam_ReportFieldNextLocation))
					{
						vecFullColumnNames.add(sParamName);
						if (sParamName.equals(k_sParam_ReportFieldNextLocation))
						{
							bShowNextLocation = true;
						}
						else
						{
							bShowLocations = true;
						}
						vecColumnNames.add(sStoreValue);
						vecMethodNames.add(sStoreValue);
					}
					else if ((sParamName.equals(k_sParam_ReportFieldLocationDates))
					   	&& (((a_request.getParameter(k_sParam_ReportFieldLocation) != null)
							&& (a_request.getParameter(k_sParam_ReportFieldEvents) == null))  
							|| ((a_request.getParameter(k_sParam_ReportFieldLocation) != null)
							&& (a_request.getParameter(k_sParam_ReportFieldEvents) != null))))
					{
						vecFullColumnNames.add(sParamName);
						bShowLocationDates = true;
						vecColumnNames.add(k_sColumnName_ArrivalDate);
						vecMethodNames.add(sStoreValue);
						vecColumnNames.add(k_sColumnName_DepartureDate);
						vecMethodNames.add(sStoreValue);
					}
					else if ((sParamName.equals(k_sParam_ReportFieldEvents)) 
							&& (a_request.getParameter(k_sParam_ReportFieldLocation) == null))
					{
						vecFullColumnNames.add(sParamName);
						bShowEvents = true;
						vecColumnNames.add(sStoreValue);
						vecMethodNames.add(sStoreValue);
					}
				}
			}
			
			//build the report...
			if (vecColumnNames.size() > 0)
			{
				// use the vector of method names to build a set of 'ReportLine' objects
				Vector vecReportLines = new Vector();
				
				//see if we need ordering...
				String sOrderBy = a_request.getParameter(k_sParam_Ordering);
				int iSorting = 0;
				
				if (sOrderBy != null)
				{
					if (sOrderBy.equals(k_sNoOrderConstant))
					{
						sOrderBy = null;
					}
					else if (sOrderBy.equals(k_sSort_Event))
					{
						sOrderBy = null;
						iSorting = k_iSort_Event;
					}
					else if (sOrderBy.equals(k_sSort_Location))
					{
						sOrderBy = null;
						iSorting = k_iSort_Location;
					}
				}
				
				//Vector vecResults = userProfile.getLastSearchResults();
				if(userProfile.getLastSearchCriteria().getProductSegment().getId() > 0)
				{
					userProfile.getLastSearchCriteria().setProductSegment(m_productSegmentManager.getProductSegment(null, userProfile.getLastSearchCriteria().getProductSegment().getId()));
				}
				List<Product> vecResults = m_productManager.search(null, userProfile.getLastSearchCriteria(), sOrderBy);
				Vector vecFieldValues = null;
				Product product = null;
				Class prodClass = Class.forName(k_sClassName_Product);
				Class listClass = Class.forName(k_sClassName_ListValue);
				Class dateClass = Class.forName(k_sClassName_Date);
				Class productSegmentClass = Class.forName(k_sClassName_ProductSegment);
				Method method = null;
				int iLocationDates = 1;
				boolean bAdded = false;
						
				for (int i=0; i<vecResults.size(); i++)
				{
					vecFieldValues = new Vector();
					product = (Product)vecResults.get(i);
					
					//use introspection to get all the values into the report line object
					for (int x=0; x<vecMethodNames.size(); x++)
					{
							sMethodName = (String)vecMethodNames.elementAt(x);
							
							//deal with the location / event info
							if (sMethodName.equals(k_sReportField_Location) || 
								sMethodName.equals(k_sReportField_NextLocation) || 
								sMethodName.equals(k_sReportField_Events))
							{
								//mark an empty slot for the events...
								vecFieldValues.add(sMethodName);
							}
							else if (sMethodName.equals(k_sReportField_LocationDates))
							{
								//add an identifier to location dates...
								vecFieldValues.add(sMethodName+iLocationDates);
								if (iLocationDates == 2)
								{
									iLocationDates = 0;
								}
								iLocationDates++;
							}
							else
							{
								String sTempMethod = sMethodName;
								sMethodName = k_sGetMethodStart + sMethodName; 
								method = prodClass.getMethod(sMethodName, null);
								Object result = method.invoke(product, null);
								
								if ((sTempMethod.equals(k_sReportField_DedicatedPackaging)) || (sTempMethod.equals(k_sReportField_Label)) || (sTempMethod.equals(k_sReportField_TechnologySheetAvailable)))
								{
									if (((Boolean)result).booleanValue() == true)
									{
										result = "yes";
									}
									else
									{
										result = "no";
									}
								}
								
								//if this is a list value object then use the getValue method to display
								if (listClass.isInstance(result))
								{
									vecFieldValues.add(((ListValue)result).getValue());
								}
								else if (dateClass.isInstance(result))
								{
									Date temp = (Date)result;
									SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
									vecFieldValues.add(format.format(temp));
								}
								else if (productSegmentClass.isInstance(result))
								{
									vecFieldValues.add(((ProductSegment)result).getName());
								}
								else
								{
									vecFieldValues.add(result);
								}
							}
					}
					
					//check to see if we need to get any list information...
					if (bShowLocations || bShowNextLocation)
					{
						//combined the movements into the report line objects...
						Vector vecMovements = m_productManager.getProductMovements(null, product.getId(), k_iMovementStatus_CurrentAndFuture);
						
						ReportLine reportLine = new ReportLine();
						reportLine.setVecFieldValues(vecFieldValues);
						
						if (vecMovements != null)
						{
							for (int x=0; x<vecMovements.size(); x++)
							{
								ProductMovement pm = (ProductMovement)vecMovements.elementAt(x);
								if (pm != null && ((x==0 && bShowLocations) || (x==1 && bShowNextLocation)))
								{
									//store either the event or the description as the location name
									String sLocVal = pm.getLocationValue();
									if (pm.getEvent().getName() != null)
									{
										sLocVal = pm.getEvent().getName();
									}
									
									if (bShowNextLocation && x==1)
									{
										reportLine.setNextLocation(sLocVal);
										reportLine.setNextLocationArrivalDate(pm.getArrivalDate());
										reportLine.setNextLocationDepartureDate(pm.getDepartureDate());
									}
									else
									{
										reportLine.setLocation(sLocVal);
										reportLine.setLocationArrivalDate(pm.getArrivalDate());
										reportLine.setLocationDepartureDate(pm.getDepartureDate());
									}
								}
								else if (x>1)
								{
									break;
								}
							}
						}
						
						//add to the vector of report lines...
						vecReportLines.add(reportLine);
					}
					else if (bShowEvents)
					{
						Vector vecProductMovements = m_productManager.getProductMovements(null, product.getId(), k_iMovementStatus_All);
						bAdded = false;
						
						//combined the movements into the report line objects...
						ProductMovement pm = null;
						for (int x=0; x<vecProductMovements.size(); x++)
						{
							pm = (ProductMovement)vecProductMovements.elementAt(x);
								
							//store the event details if there are any...
							if (pm.getEvent() != null)
							{
								ReportLine reportLine = new ReportLine();
								reportLine.setVecFieldValues(vecFieldValues);
								reportLine.setEvent(pm.getEvent().getName());
						
								//add to the vector of report lines...
								vecReportLines.add(reportLine);
								bAdded = true;
							}
						}
						
						if (!bAdded)
						{
							ReportLine reportLine = new ReportLine();
							reportLine.setVecFieldValues(vecFieldValues);
							vecReportLines.add(reportLine);
						}
					}
					else
					{
						//..if not just build the single report line
						ReportLine reportLine = new ReportLine();
						reportLine.setVecFieldValues(vecFieldValues);
						vecReportLines.add(reportLine);
					}
				}
				
				//check to see if sorting is needed...
				sortVector(vecReportLines, iSorting);
				
				// See whether we should write this reoprt to a file for download
				// or set it in the view
				if(a_request.getParameter("download") != null)
				{
					String sFilename = m_fileStoreManager.getUniqueFilename("report.csv", "downloads");
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Writer writer = null; 
				
					try
					{
						writer = new BufferedWriter(new FileWriter(m_fileStoreManager.getFileStoreRoot() + "/" + sFilename));
						
						// Write the report search criteria 						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getKeywords()))
						{
							writer.append("\"Keywords:\",\""+userProfile.getLastSearchCriteria().getKeywords()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getLocationDescription()))
						{
							writer.append("\"Location:\",\""+userProfile.getLastSearchCriteria().getLocationDescription()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getEventName()))
						{
							writer.append("\"Event:\",\""+userProfile.getLastSearchCriteria().getEventName()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getDatesString()))
						{
							writer.append("\"Dates:\",\""+userProfile.getLastSearchCriteria().getDatesString()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getOldCode()))
						{
							writer.append("\"Code (old):\",\""+userProfile.getLastSearchCriteria().getOldCode()+"\"\n");
						}
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getNewCode()))
						{
							writer.append("\"Code (new):\",\""+userProfile.getLastSearchCriteria().getNewCode()+"\"\n");
						}
						
						if(userProfile.getLastSearchCriteria().getProductSegment().getId() > 0)
						{
							writer.append("\"Product Segment:\",\""+userProfile.getLastSearchCriteria().getProductSegment().getName()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getVehicle().getValue()))
						{
							writer.append("\"Vehicle:\",\""+userProfile.getLastSearchCriteria().getVehicle().getValue()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getModel()))
						{
							writer.append("\"Model:\",\""+userProfile.getLastSearchCriteria().getModel()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getProductType().getValue()))
						{
							writer.append("\"Type:\",\""+userProfile.getLastSearchCriteria().getProductType().getValue()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getManufacturingLocation().getValue()))
						{
							writer.append("\"Manufacturing Location:\",\""+userProfile.getLastSearchCriteria().getManufacturingLocation().getValue()+"\"\n");
						}
						
						if(StringUtil.stringIsPopulated(userProfile.getLastSearchCriteria().getYearOfManufacture()))
						{
							writer.append("\"Year of Manufacture:\",\""+userProfile.getLastSearchCriteria().getYearOfManufacture()+"\"\n");
						}
						
						if(userProfile.getLastSearchCriteria().getDedicatedPackaging())
						{
							writer.append("\"Dedicated Packaging:\",\"Yes\"\n");
						}
						writer.append("\n");
						
						
						// Write the column headers
						for(Iterator colIt = vecColumnNames.iterator();colIt.hasNext();)
						{
							String sColName = (String) colIt.next();
							writer.append("\""+sColName+"\"");
							// If not end of column names add comma (for next value) otherwise end line
							if(colIt.hasNext())
							{
								writer.append(",");
							}
							else
							{
								writer.append("\n");
							}
						}
						
						// Write the report lines
						for(Iterator lineIt = vecReportLines.iterator();lineIt.hasNext();)
						{
							ReportLine reportLine = (ReportLine) lineIt.next();
							for(Iterator fieldValIt = reportLine.getFieldValues().iterator();fieldValIt.hasNext();)
							{
								// Open the field value
								writer.append("\"");
								String sFieldValue = (String) fieldValIt.next();
								
								// write value or n/a
								if(StringUtil.stringIsPopulated(sFieldValue))
								{
									// Check for special reportline values
									if(sFieldValue.equals("Location"))
									{
										if(StringUtil.stringIsPopulated(reportLine.getLocation()))
										{
											writer.append(reportLine.getLocation());
										}
										else
										{
											writer.append("-");
										}
									}
									else if(sFieldValue.equals("LocationDates1"))
									{
										writer.append(simpleDateFormat.format(reportLine.getLocationArrivalDate()));
									}
									else if(sFieldValue.equals("LocationDates2"))
									{
										writer.append(simpleDateFormat.format(reportLine.getLocationDepartureDate()));
									}
									else if(sFieldValue.equals("NextLocation"))
									{
										if(StringUtil.stringIsPopulated(reportLine.getNextLocation()))
										{
											writer.append(reportLine.getNextLocation());
										}
										else
										{
											writer.append("-");
										}
									}
									else if(sFieldValue.equals("NextLocationDates1"))
									{
										writer.append(simpleDateFormat.format(reportLine.getNextLocationArrivalDate()));
									}
									else if(sFieldValue.equals("NextLocationDates2"))
									{
										writer.append(simpleDateFormat.format(reportLine.getNextLocationDepartureDate()));
									}
									else if(sFieldValue.equals("Events"))
									{
										writer.append(reportLine.getEvent());
									}
									else
									{
										writer.append(sFieldValue);
									}
								}
								else
								{
									writer.append("N/A");
								}
								
								// Close the field value
								writer.append("\"");
								
								// If not last field value write comma
								if(fieldValIt.hasNext())
								{
									writer.append(",");
								}
							}
							
							// End line
							writer.append("\n");														
						}
						
						
					}
					catch(IOException ioe)
					{
						m_logger.error("GenerateReportAction.execute() : IOException whilst creating csv file : " + ioe);
					}
					finally
					{
						try
						{
							writer.flush();
							writer.close();
						}
						catch(IOException ioe2)
						{
							// Exception during close - nothing we can do
						}
					}
					
					
					a_request.setAttribute(FrameworkConstants.k_sAttributeName_DeleteFileAfterUse, new Boolean(true));		
					a_request.setAttribute(FrameworkConstants.k_sAttributeName_DownloadFile, FileUtil.encryptFilepath(sFilename));
					a_request.setAttribute(FrameworkConstants.k_sAttributeName_DownloadFilename, "report.csv");
					
					return a_mapping.findForward(FrameworkConstants.DOWNLOAD_KEY);
				}
				
				// Set in the form:
				form.setColumnNames(vecColumnNames);
				form.setFullColumnNames(vecFullColumnNames);
				form.setMethodNames(vecMethodNames);
				form.setReportLines(vecReportLines);
				form.setShowLocationDates(bShowLocationDates);
				form.setShowLocations(bShowLocations);
				form.setShowEvents(bShowEvents);
				
				afForward = a_mapping.findForward(CommonConstants.SUCCESS_KEY);
				
			}
			else
			{
				form.addError("You need to select at least 1 column to display");
				afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
			}
		}
		catch (ClassNotFoundException e)
		{
			m_logger.debug("GenerateReportAction.execute : Unable to find class ; "+e.getMessage());
			form.addError(sErrorString);
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
		}
		catch (NoSuchMethodException e)
		{
			m_logger.debug("GenerateReportAction.execute : Unable to find method ; "+e.getMessage());
			form.addError(sErrorString);
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
		}
		catch (IllegalAccessException e)
		{
			m_logger.debug("GenerateReportAction.execute : Illegal Access ; "+e.getMessage());
			form.addError(sErrorString);
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
		}
		catch (InvocationTargetException e)
		{
			m_logger.debug("GenerateReportAction.execute : Invocation exception ; "+e.getMessage());
			form.addError(sErrorString);
			afForward = a_mapping.findForward(CommonConstants.FAILURE_KEY);
		}
		
		return (afForward);
	}
	
	
	/**
	 * Check if the param vector needs to be sorted by event or location - if so do it!
	 *
	 * @param Vector a_vecToSort
	 * @param int a_iSortType - switch for location, event or no sorting
	 */
	public void sortVector(Vector a_vecToSort, int a_iSortType)
	/*
	 ------------------------------------------------------------------------
	 d5	23-Jun-2005		Matt Stevenson				Created
	 ------------------------------------------------------------------------
	*/
	{
		if ((a_iSortType == k_iSort_Event) || (a_iSortType == k_iSort_Location))
		{
			TreeMap tmSortedObjects = new TreeMap();
			
			//copy the vector into a Tree structure for sorting...
			ReportLine reportLine = null;
			int iIndex = 0;
			
			for (int i=0; i<a_vecToSort.size(); i++)
			{
				reportLine = (ReportLine)a_vecToSort.elementAt(i);
				
				//add each of the values to the tree map - using the key that is required
				//for the correct sort type (ie. event or location)...
				if (a_iSortType == k_iSort_Event)
				{
					tmSortedObjects.put(reportLine.getEvent()+iIndex, reportLine);
					iIndex++;
				}
				else if (a_iSortType == k_iSort_Location)
				{
					tmSortedObjects.put(reportLine.getLocation()+iIndex, reportLine);
					iIndex++;
				}
			}
			
			//now take the sorted tree map and reconstruct the vector in the correct order...
			Set sortedKeys = tmSortedObjects.keySet();
			Iterator keyIterator = sortedKeys.iterator();
			a_vecToSort.clear();
			String sKey = null;
			
			while (keyIterator.hasNext())
			{
				sKey = (String)keyIterator.next();
				a_vecToSort.add(tmSortedObjects.get(sKey));
			}
		}
	}
	
	public void setProductManager (ProductManager a_productManager)
	{
		m_productManager = a_productManager;
	}
	
	public void setProductSegmentManager(ProductSegmentManager a_productSegmentManager)
	{
		m_productSegmentManager = a_productSegmentManager;
	}
	
	public void setFileStoreManager(FileStoreManager a_fileStoreManager)
	{
		m_fileStoreManager = a_fileStoreManager;
	}
}

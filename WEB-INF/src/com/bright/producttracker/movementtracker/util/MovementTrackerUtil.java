package com.bright.producttracker.movementtracker.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.bright.framework.util.StringUtil;
import com.bright.producttracker.movementtracker.constant.MovementTrackerConstants;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.productsegment.bean.ProductSegment;

public class MovementTrackerUtil
{
	public static boolean movementIsInTrackerRange(ProductMovement a_productMovement)
	{										
		return movementIsInRange(a_productMovement,getMovementTrackerLowerDateRange(),getMovementTrackerUpperDateRange());
	}
	
	public static boolean movementIsInRange(ProductMovement a_productMovement,Date lowerRange, Date upperRange)
	{										
		return 	movementStartsOnOrInRange(a_productMovement,lowerRange,upperRange) || 
				movementEndsOnOrInRange(a_productMovement,lowerRange,upperRange) || 
				movementSpansRange(a_productMovement,lowerRange,upperRange);
	}
	
	private static boolean movementStartsOnOrInRange(ProductMovement a_productMovement, Date lowerRange, Date upperRange)
	{
		Date arrivalDate = getArrivalDate(a_productMovement);		
		return !arrivalDate.before(lowerRange) && !arrivalDate.after(upperRange);
	}
	
	private static boolean movementEndsOnOrInRange(ProductMovement a_productMovement,Date lowerRange, Date upperRange)
	{			
		Date departureDate = getDepartureDate(a_productMovement);		
		return !departureDate.before(lowerRange) && !departureDate.after(upperRange);
	}
	
	private static boolean movementSpansRange(ProductMovement a_productMovement,Date lowerRange, Date upperRange)
	{			
		Date departureDate = getDepartureDate(a_productMovement);
		Date arrivalDate = getArrivalDate(a_productMovement);
		return arrivalDate.before(lowerRange) && departureDate.after(upperRange);
	}
	
	public static Date getMovementTrackerLowerDateRange()
	{
		Calendar calendar = new GregorianCalendar();
		zeroTime(calendar);
		calendar.add(Calendar.WEEK_OF_YEAR, -MovementTrackerConstants.LEAD_IN_WEEKS_ON_TRACKER);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);		
		return calendar.getTime();
	}	
	
	public static Date getMovementTrackerUpperDateRange()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(getMovementTrackerLowerDateRange());
		zeroTime(calendar);
		calendar.add(Calendar.WEEK_OF_YEAR, MovementTrackerConstants.WEEKS_TO_SHOW_ON_TRACKER);		
		return calendar.getTime();
	}
	
	private static void zeroTime(Calendar calendar)
	{
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}	
	
	private static Date getToday()
	{
		Calendar calendar = new GregorianCalendar();
		zeroTime(calendar);
		return calendar.getTime();
	}

	public static Date getArrivalDate(ProductMovement a_productMovement)
	{		
		if (getMovementHasArrivalDate(a_productMovement))
		{				
			return a_productMovement.getArrivalDate();					
		}
		else if (getEventHasArrivalDate(a_productMovement))
		{			
			return a_productMovement.getEvent().getStartDate();
					
		}
		else if (getMovementHasDepartureDate(a_productMovement))
		{
			return a_productMovement.getDepartureDate();
		}
		else
		{
			return a_productMovement.getEvent().getEndDate();
		}	
	}
	
	public static Date getDepartureDate(ProductMovement a_productMovement)
	{
		if (getMovementHasDepartureDate(a_productMovement))
		{				
			return a_productMovement.getDepartureDate();					
		}
		else if (getEventHasDepartureDate(a_productMovement))
		{			
			return a_productMovement.getEvent().getEndDate();
					
		}
		else if (getMovementHasArrivalDate(a_productMovement))
		{
			return a_productMovement.getArrivalDate();
		}
		else
		{
			return a_productMovement.getEvent().getStartDate();
		}	
	}
	
	public static String getArrivalDateString(ProductMovement a_productMovement)
	{
		if (getMovementHasArrivalDate(a_productMovement))
		{				
			return a_productMovement.getArrivalDateString();					
		}
		else if (getEventHasArrivalDate(a_productMovement))
		{			
			return a_productMovement.getEvent().getStartDateStr();
					
		}
		else if (getMovementHasDepartureDate(a_productMovement))
		{
			return a_productMovement.getDepartureDateString();
		}
		else
		{
			return a_productMovement.getEvent().getEndDateStr();
		}	
	}
	
	public static String getDepartureDateString(ProductMovement a_productMovement)
	{
		if (getMovementHasDepartureDate(a_productMovement))
		{				
			return a_productMovement.getDepartureDateString();					
		}
		else if (getEventHasDepartureDate(a_productMovement))
		{			
			return a_productMovement.getEvent().getEndDateStr();
					
		}
		else if (getMovementHasArrivalDate(a_productMovement))
		{
			return a_productMovement.getArrivalDateString();
		}
		else
		{
			return a_productMovement.getEvent().getStartDateStr();
		}	
	}		
	
	public static boolean hasValidMovementDates(ProductMovement a_productMovement)
	{
		return hasValidArrivalDate(a_productMovement) || hasValidDepartureDate(a_productMovement);
	}
	
	private static boolean hasValidArrivalDate(ProductMovement a_productMovement)
	{
		return getMovementHasArrivalDate(a_productMovement) || getEventHasArrivalDate(a_productMovement);
	}
	
	private static boolean hasValidDepartureDate(ProductMovement a_productMovement)
	{
		return getMovementHasDepartureDate(a_productMovement) || getEventHasDepartureDate(a_productMovement);
	}
	
	public static boolean getMovementHasDates(ProductMovement a_productMovement)
	{
		return (getMovementHasArrivalDate(a_productMovement) || getMovementHasDepartureDate(a_productMovement));
	}

	private static boolean getMovementHasArrivalDate(ProductMovement a_productMovement)
	{
		return a_productMovement.getArrivalDateString() != null;
	}
	
	private static boolean getMovementHasDepartureDate(ProductMovement a_productMovement)
	{
		return a_productMovement.getDepartureDateString() != null;
	}
	
	public static boolean getEventHasDates(ProductMovement a_productMovement)
	{
		if (getEventHasArrivalDate(a_productMovement) || getEventHasDepartureDate(a_productMovement))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean getEventHasArrivalDate(ProductMovement a_productMovement)
	{
		return a_productMovement.getEvent() != null && a_productMovement.getEvent().getStartDateStr() != null;
	}
	
	private static boolean getEventHasDepartureDate(ProductMovement a_productMovement)
	{
		return  a_productMovement.getEvent() != null && a_productMovement.getEvent().getEndDateStr() != null;
	}
	
	public static String getEventName(ProductMovement a_productMovement)
	{
		String eventName = a_productMovement.getEvent().getName();
		if (eventName == null)
		{
			eventName = a_productMovement.getLocationValue() + " - " + a_productMovement.getCountry().getName();
		}
		else if (a_productMovement.getEvent().getCountry() != null && StringUtil.stringIsPopulated(a_productMovement.getEvent().getCountry().getName()))
		{
			eventName += " - " + a_productMovement.getEvent().getCountry().getName();
		}
			
		return eventName;
	}
	
	public static int getDaySpan(Date a_startDate, Date a_endDate)
	{	
		Date startDate = a_startDate;
		Date endDate = a_endDate;
		if(a_startDate.after(a_endDate))
		{
			startDate = a_endDate;
			endDate = a_startDate;
		}		
		
		Calendar date = new GregorianCalendar();
		date.setTime(((Date)startDate.clone()));
		int daysBetween = 0;  
		while (date.getTime().before(endDate)) 
		{  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}  
		
		// We don't want days between we want day span so add on one. 
		// e.g. if there's no days between the start and the end then it's a one day event.
		return daysBetween+1; 
		
	}
	
	public static Map<ProductSegment, List<Product>> createProductsByProductSegmentMap(List<Product> a_products)
	{
		Map<ProductSegment, List<Product>> map = new TreeMap<ProductSegment, List<Product>>();
		
		for (Product product : a_products)
		{
			ProductSegment productSegment = product.getProductSegment();
			if(productSegment == null || productSegment.getId() <= 0)
			{
				productSegment = ProductSegment.UNKNOWN_SEGMENT;
			}
			
			if(!map.containsKey(productSegment))
			{				
				map.put(productSegment, new ArrayList<Product>());				
			}
			
			map.get(productSegment).add(product);
		}
		
		return map;
	}

	public static boolean getIsOverdue(ProductMovement a_productMovement)
	{
		if(hasValidDepartureDate(a_productMovement))
		{			
			Date departureDate = getDepartureDate(a_productMovement);
			return !departureDate.after(getToday()) && !a_productMovement.getMovedOn();
		}
		
		return false;
		
	}
}

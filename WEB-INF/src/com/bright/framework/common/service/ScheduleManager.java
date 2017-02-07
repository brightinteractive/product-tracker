package com.bright.framework.common.service;

/**
 * Bright Interactive
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * 
 * ScheduleManager.java
 *
 */
/*
Ver   Date           Who               Comments
--------------------------------------------------------------------------------
d1    20-Mar-2004    Martin Wilson     Created from framework code.
d2    ??-???-2005    Martin Wilson     Changed scheduleDailyTask to fix defect. 	
--------------------------------------------------------------------------------
*/
 
import java.util.*;

import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.constant.FrameworkConstants;


/**
 *
 * ScheduleManager - manages scheduled tasks.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class ScheduleManager extends Bn2Manager
{
    private Vector m_vecTasks = new Vector();
    private Timer m_timer = new Timer();
    
    /**
    * Adds a task to the task list.
    * 
    * @param TimerTask a_task - a task to be run.
    * @param long a_lInitialDelay - the initial delay in milliseconds.
    * @param long a_lPeriod - time in milliseconds between runs of the task.
    */    
    public void schedule(TimerTask a_task, long a_lInitialDelay, long a_lPeriod)
    /*
    ------------------------------------------------------------------------
     d1   20-Mar-2004   Martin Wilson 		Created.
    ------------------------------------------------------------------------
    */ 
    {
        // Schedule the task and add to a vector (used for stopping the tasks):
        if (a_task != null)
        {
            m_timer.schedule(a_task, a_lInitialDelay, a_lPeriod);
            m_vecTasks.add(a_task);
        }
    }
    
	/**
	 * Adds a task to be run once a day, on an hour.
	 * 
	 * @param a_task
	 * @param a_iHourOfDay
	 */
	public void scheduleDailyTask(TimerTask a_task, 
											int a_iHourOfDay)
	/*
	 ---------------------------------------------------------------
	 d1		18-Jul-2005		Martin Wilson		Created
	 d2		??-???-2005		Martin Wilson		Fixed defect (whereby negative initial period if Saturday!) by changing 'roll' to 'add'  
	 -----------------------------------------------------------------
	 */    
	{
		long lNow = 0;
		Calendar cal = Calendar.getInstance();
		
		// Get a calendar for now:
		cal.setTime(new Date());	
		lNow = cal.getTime().getTime();		
		
		// See if we have passed the time of day when we want to schedule the task:
		if (cal.get(Calendar.HOUR_OF_DAY) >= a_iHourOfDay)
		{
			// Move on to tomorrow:
			cal.add(Calendar.DATE, 1);
		}
		
		// Set to the exact hour:
		cal.set(Calendar.HOUR_OF_DAY, a_iHourOfDay);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		// Figure out the initial delay to achieve it:
		long lInitialDelay = cal.getTime().getTime() - lNow;
		
		// Schedule the task:
		schedule(a_task, lInitialDelay, FrameworkConstants.k_lOneDayInMillis);	
	}    
	
    /**
    * Adds a task to the task list - to be run at a particular time.
    * 
    * @param TimerTask a_task - a task to be run.
    * @param Date - a_dtDate - the date the task should be run.
    */    
    public void schedule(TimerTask a_task, Date a_dtDate)
    /*
    ------------------------------------------------------------------------
     d1   20-Mar-2004   Martin Wilson 		Created.
    ------------------------------------------------------------------------
    */ 
    {
        // Schedule the task and add to a vector (used for stopping the tasks):
        if (a_task != null)
        {
            m_timer.schedule(a_task, a_dtDate);
            m_vecTasks.add(a_task);
        }
    }    
    
    /**
    * Ends all the tasks quickly.
    * 
    */        
    public void dispose()
    /*
    ------------------------------------------------------------------------
     d1   20-Mar-2004   Martin Wilson 		Created.
    ------------------------------------------------------------------------
    */
    {
        // Stop all the tasks:
        for (int i=0; i < m_vecTasks.size(); i++)
        {
            TimerTask task = (TimerTask)(m_vecTasks.get(i));
            
            if (task != null)
            {
            	task.cancel();
            }
        }
    }           
}
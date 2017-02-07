package com.bright.producttracker.movementtracker.bean;

import java.util.ArrayList;
import java.util.List;

public class MovementTrackerProductJsonRepr
{
	private String id;
	private String code;
	private String name;
	private List<MovementTrackerScheduleJsonRepr> schedule = new ArrayList<MovementTrackerScheduleJsonRepr>();
	private boolean overdue;
	
	
	public MovementTrackerProductJsonRepr(String id, String code, String name)
	{		
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public void addScheduleItem(MovementTrackerScheduleJsonRepr movementTrackerScheduleJsonRepr)
	{
		schedule.add(movementTrackerScheduleJsonRepr);
	}

	public void setOverdue(boolean a_overdue)
	{
		overdue = a_overdue;		
	}
		
}

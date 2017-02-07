package com.bright.producttracker.movementtracker.bean;


public class MovementTrackerScheduleJsonRepr
{
	private String arrival_date;
	private String end_date;
	private int event_span;
	private String event_name;
	private boolean overdue;
	
	
	
	public MovementTrackerScheduleJsonRepr(String a_arrivalDate,String a_endDate, int a_eventSpan, String a_eventName,boolean a_overdue)
	{
		super();
		arrival_date = a_arrivalDate;
		end_date = a_endDate;
		event_span = a_eventSpan;
		event_name = a_eventName;
		overdue = a_overdue;
	}
	
}

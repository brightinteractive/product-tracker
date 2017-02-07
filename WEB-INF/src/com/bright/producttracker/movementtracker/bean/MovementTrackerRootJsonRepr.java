package com.bright.producttracker.movementtracker.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovementTrackerRootJsonRepr
{
	private List<MovementTrackerSegementJsonRepr> segments = new ArrayList<MovementTrackerSegementJsonRepr>();
		
	public void addSegment(MovementTrackerSegementJsonRepr a_movementTrackerSegementJsonRepr)
	{
		segments.add(a_movementTrackerSegementJsonRepr);
	}	
}

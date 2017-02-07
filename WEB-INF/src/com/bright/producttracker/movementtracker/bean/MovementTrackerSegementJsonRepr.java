package com.bright.producttracker.movementtracker.bean;

import java.util.ArrayList;
import java.util.List;

public class MovementTrackerSegementJsonRepr
{
		
	public MovementTrackerSegementJsonRepr(String code, String name)
	{		
		this.code = code;
		this.name = name;
	}
	private String code;
	private String name;
	private List<MovementTrackerProductJsonRepr> products = new ArrayList<MovementTrackerProductJsonRepr>();
	
	public void addProduct(MovementTrackerProductJsonRepr product)
	{
		products.add(product);
	}
	
	
}

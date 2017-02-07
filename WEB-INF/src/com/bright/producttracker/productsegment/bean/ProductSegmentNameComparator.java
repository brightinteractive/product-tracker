package com.bright.producttracker.productsegment.bean;

import java.util.Comparator;

public class ProductSegmentNameComparator implements Comparator<ProductSegment>
{
	public int compare(ProductSegment a_productSegment1, ProductSegment a_productSegment2)
	{
		return a_productSegment1.getName().compareTo(a_productSegment2.getName());
	}

}

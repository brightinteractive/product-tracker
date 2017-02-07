package com.bright.producttracker.movementtracker.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.bright.producttracker.product.bean.Product;

public interface MovementTrackerDataFormatter
{
	public File createFormattedDataFile(List<Product> products) throws IOException;
	
	public String getDownloadFileExtention();
	
	public String getDownloadContentType();

}

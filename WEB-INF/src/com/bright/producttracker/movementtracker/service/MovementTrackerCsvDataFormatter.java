package com.bright.producttracker.movementtracker.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVWriter;

import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.producttracker.movementtracker.util.MovementTrackerUtil;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.productsegment.bean.ProductSegment;

public class MovementTrackerCsvDataFormatter extends Bn2Manager implements MovementTrackerDataFormatter
{
	
	private static final String TEMP_FILE_NAME = "data";
	private static final String FILE_EXT = "csv";
	private static final String[] FIXED_COLUMN_TITLES = {	"Product Segment",
															"Product Code",
															"Product Details",															
														};
	
	public File createFormattedDataFile(List<Product> a_products) throws IOException
	{		
		File dataFile = setupDataFile();
		CSVWriter writer = setupCSVWriter(dataFile);
		writeColumnHeaders(writer);
		writeRows(a_products,writer);							
		flushAllData(writer);									
		return dataFile;			
	}
	
	
	public String getDownloadContentType()
	{
		return "text/"+FILE_EXT;
	}

	
	public String getDownloadFileExtention()
	{
		return FILE_EXT;
	}
	
	private File setupDataFile()
	throws IOException
	{		
		return File.createTempFile(TEMP_FILE_NAME, FILE_EXT);					
	}
	
	private CSVWriter setupCSVWriter(File a_dataFile)
	throws IOException
	{				
		return new CSVWriter(new FileWriter(a_dataFile));				
	}
	
	
	private void writeColumnHeaders(CSVWriter a_writer)
	{
		List<String> headers = new ArrayList<String>();
		
		headers.addAll(Arrays.asList(FIXED_COLUMN_TITLES));		
		addMovementDateHeaders(headers);
				
		a_writer.writeNext(headers.toArray(new String[headers.size()]));
	}


	private void addMovementDateHeaders(List<String> headers)
	{
		Date start = MovementTrackerUtil.getMovementTrackerLowerDateRange();
		Date end = MovementTrackerUtil.getMovementTrackerUpperDateRange();
		Calendar loop = new GregorianCalendar();
		loop.setTime(start);
		while(!loop.getTime().after(end))
		{			
			headers.add(FrameworkSettings.getStandardDateFormat().format(loop.getTime()));
			loop.add(Calendar.DATE, 1);
		}
	}
	
	
	private void writeRows(List<Product> a_products,CSVWriter a_writer)
	{
		Map<ProductSegment, List<Product>> productsBySegment = MovementTrackerUtil.createProductsByProductSegmentMap(a_products);
		for (Entry<ProductSegment, List<Product>> segmentAndProducts : productsBySegment.entrySet())
		{
			ProductSegment productSegment = segmentAndProducts.getKey();
			List<Product> segmentProducts = segmentAndProducts.getValue();
			for (Product product : segmentProducts)
			{
				writeRow(product,productSegment,a_writer);			
			}
		}
		
	}
	
	
	private void writeRow(Product a_product,ProductSegment a_productSegment,CSVWriter a_writer)
	{
		List<String> rowValues = new ArrayList<String>();		
		addDataToRowValues(a_product,a_productSegment, rowValues);
		a_writer.writeNext(rowValues.toArray(new String[rowValues.size()]));
	}
	
	
	private void addDataToRowValues(Product a_product,ProductSegment a_productSegment, List<String> a_rowValues)
	{
		a_rowValues.add(a_productSegment.getName());
		a_rowValues.add(String.valueOf(a_product.getNewCode()));
		a_rowValues.add(a_product.getDescription());		
		addProductMovementDataToRowValues(a_product, a_rowValues);
	}


	private void addProductMovementDataToRowValues(Product a_product,List<String> a_rowValues)
	{
		Date start = MovementTrackerUtil.getMovementTrackerLowerDateRange();
		Date end = MovementTrackerUtil.getMovementTrackerUpperDateRange();
		Calendar loop = new GregorianCalendar();
		loop.setTime(start);
		while(!loop.getTime().after(end))
		{
			loop.add(Calendar.DATE, 1);
			
			ProductMovement productMovement = a_product.getFirstMovementOnDate(loop.getTime());
			if(productMovement != null)
			{
				a_rowValues.add(MovementTrackerUtil.getEventName(productMovement));
			}
			else
			{
				a_rowValues.add("");
			}
		}
	}
	
	
	private void flushAllData(CSVWriter a_writer)
	throws IOException
	{		
		a_writer.flush();		
	}

}

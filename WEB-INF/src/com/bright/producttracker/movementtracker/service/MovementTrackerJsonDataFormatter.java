package com.bright.producttracker.movementtracker.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.bn2web.common.service.Bn2Manager;
import com.bright.producttracker.movementtracker.bean.MovementTrackerProductJsonRepr;
import com.bright.producttracker.movementtracker.bean.MovementTrackerRootJsonRepr;
import com.bright.producttracker.movementtracker.bean.MovementTrackerScheduleJsonRepr;
import com.bright.producttracker.movementtracker.bean.MovementTrackerSegementJsonRepr;
import com.bright.producttracker.movementtracker.util.MovementTrackerUtil;
import com.bright.producttracker.product.bean.Product;
import com.bright.producttracker.product.bean.ProductMovement;
import com.bright.producttracker.productsegment.bean.ProductSegment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MovementTrackerJsonDataFormatter extends Bn2Manager implements MovementTrackerDataFormatter
{
	private static final String TEMP_FILE_NAME = "data";
	private static final String FILE_EXT = "json";

	public File createFormattedDataFile(List<Product> a_products) throws IOException
	{
		MovementTrackerRootJsonRepr data = convertProductsToMovementTrackerRootJsonRepr(a_products);
		
		File dataFile = createDataFile();		
		writeDataToJsonFile(data, dataFile);
				
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
	
	private MovementTrackerRootJsonRepr convertProductsToMovementTrackerRootJsonRepr(List<Product> a_products)
	{		
		
		MovementTrackerRootJsonRepr rootJsonRepr = new MovementTrackerRootJsonRepr();
		
		Map<ProductSegment, List<Product>> productsBySegment = MovementTrackerUtil.createProductsByProductSegmentMap(a_products);
		for (Entry<ProductSegment, List<Product>> segmentAndProducts : productsBySegment.entrySet())
		{
			ProductSegment productSegment = segmentAndProducts.getKey();
			MovementTrackerSegementJsonRepr segementJsonRepr = new MovementTrackerSegementJsonRepr(generateSegmentCode(productSegment), generateSegmentName(productSegment));
			
			for (Product product : segmentAndProducts.getValue())
			{
				segementJsonRepr.addProduct(createNewProductRepr(product));
			}
			
			rootJsonRepr.addSegment(segementJsonRepr);
		}
											
		return rootJsonRepr;
	}

	private MovementTrackerProductJsonRepr createNewProductRepr(Product product)
	{
		MovementTrackerProductJsonRepr productJsonRepr = new MovementTrackerProductJsonRepr(String.valueOf(product.getId()),product.getNewCode(),product.getDescription());
		boolean productIsOverdue = false;
		for (ProductMovement productMovement : product.getAllMovements())
		{
			if(MovementTrackerUtil.getIsOverdue(productMovement))
			{
				productIsOverdue = true;
			}
			
			if(MovementTrackerUtil.hasValidMovementDates(productMovement) && MovementTrackerUtil.movementIsInTrackerRange(productMovement))
			{
				productJsonRepr.addScheduleItem(createNewScheduleItem(productMovement));
			}
		}
		
		productJsonRepr.setOverdue(productIsOverdue);
		
		return productJsonRepr;
	}

	private String generateSegmentName(ProductSegment productSegment)
	{		
		return productSegment.getName();		
	}

	private String generateSegmentCode(ProductSegment productSegment)
	{		
		return generateSegmentName(productSegment).replaceAll(" ", "_").toLowerCase();				
	}
	
	private MovementTrackerScheduleJsonRepr createNewScheduleItem(ProductMovement a_productMovement)
	{											
		MovementTrackerScheduleJsonRepr scheduleItem = new MovementTrackerScheduleJsonRepr(
				MovementTrackerUtil.getArrivalDateString(a_productMovement),
				MovementTrackerUtil.getDepartureDateString(a_productMovement),
				MovementTrackerUtil.getDaySpan(MovementTrackerUtil.getArrivalDate(a_productMovement), MovementTrackerUtil.getDepartureDate(a_productMovement)),
				MovementTrackerUtil.getEventName(a_productMovement),
				MovementTrackerUtil.getIsOverdue(a_productMovement));
				
		return scheduleItem;
	}
	
	
	private void writeDataToJsonFile(MovementTrackerRootJsonRepr data,File dataFile) throws IOException
	{
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		FileUtils.writeStringToFile(dataFile, gson.toJson(data));
	}

	private File createDataFile() throws IOException
	{
		File dataFile = File.createTempFile(TEMP_FILE_NAME, FILE_EXT);
		return dataFile;
	}

}

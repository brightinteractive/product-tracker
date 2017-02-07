 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		18-Jan-2005		Martin Wilson		Implemented
	 d3		18-Mar-2005		Matt Stevenson		Removed locations
	 d4		21-Mar-2005		Matt Stevenson		Added new search fields
	 d5		14-Jun-2005		Matt Stevenson		Changed the way attribute list boxes work
	 d6		30-Jun-2005		Matt Stevenson		Changed sorting
	 d7		12-Dec-2005		Matt Stevenson		Removed dates
	 d8		26-Oct-2007		Matt Stevenson		Added status to search criteria
 --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/bright-tag.tld" prefix="bright" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
	<title>GKN Driveline - Product Tracker</title> 
	<link href="../css/main.css" rel="stylesheet" media="screen, print">
<style type="text/css" media="screen, print"><!--
td.colLeft { padding-top: 4px; padding-right: 4px; padding-bottom: 4px; vertical-align: middle; }
td.colRight { padding-top: 4px; padding-bottom: 4px; padding-left: 4px; vertical-align: middle; }
select { width: 250px; }
--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/calendar.js"></script>
</head>
<body onload="highlightMenu('products','find');"> 
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_product.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/manage_products.gif" alt="Manage Products" width="240" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey" style="padding-bottom: 0;">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
						<div id="searchPanel" style="text-align: left">
						<p>Please enter your search criteria in the following form.</p>
						<p>When entering dates into the location section, if only one of the two required dates is provided the search will assume that you are trying to find products that were at a specific location on the day specified.</p>
						<html:form action="searchProduct" method="get" focus="searchCriteria.keywords">
						<input type="hidden" name="ordering" value="p.newCode">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em; ">
							<tr>
								<td class="content" style="text-align: center">
									<table width="80%" border="0" cellspacing="0" cellpadding="0" style="padding: 1em 0; margin:0 0 0.5em 0.5em; text-align: left;">
										<tr>
											<td class="colLeft" >
												Keywords: 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.keywords" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Product Code (old): 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.oldCode" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Product Code (new): 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.newCode" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Product Segment: 
											</td>
											<td class="colRight">
												<html:select name="searchProductForm" property="searchCriteria.productSegment.id"
												styleId="firstField">
													<html:option value="0">-</html:option>													
													<html:optionsCollection name="searchProductForm" property="productSegments" label="name" value="id"/>
												</html:select> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Vehicle: 
											</td>
											<td class="colRight">
												<bright:refDataList id="vehicleList" componentName="ListManager" methodName="getListValues" argumentValue="Vehicle"/>
												<html:select name="searchProductForm" property="searchCriteria.vehicleString"
												styleId="firstField">
													<html:option value="0:none">-</html:option>
													<logic:iterate name="vehicleList" id="vehicle">
														<option value="<bean:write name='vehicle' property='id'/>:<bean:write name='vehicle' property='value'/>"><bean:write name="vehicle" property="value"/></option>
													</logic:iterate>
												</html:select>			
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Model: 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.model" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Status: 
											</td>
											<td class="colRight">
												<html:select name="searchProductForm" property="searchCriteria.productStatusString"
												styleId="firstField">
													<html:option value="">-</html:option>
													<html:option value="As good as new">As good as new</html:option>
													<html:option value="Still usable">Still usable</html:option>
													<html:option value="Needs refurbishment">Needs refurbishment</html:option>
													<html:option value="Last product treatment">Last product treatment</html:option>
												</html:select>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Type: 
											</td>
											<td class="colRight">
												<bright:refDataList id="typeList" componentName="ListManager" methodName="getListValues" argumentValue="ProductType"/>
												<html:select name="searchProductForm" property="searchCriteria.productTypeString"
												styleId="firstField">
													<html:option value="0:none">-</html:option>
													<logic:iterate name="typeList" id="type">
														<option value="<bean:write name='type' property='id'/>:<bean:write name='type' property='value'/>"><bean:write name="type" property="value"/></option>
													</logic:iterate>
												</html:select>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Manufacturing Location: 
											</td>
											<td class="colRight">
												<bright:refDataList id="mlList" componentName="ListManager" methodName="getListValues" argumentValue="Location"/>
												<html:select name="searchProductForm" property="searchCriteria.manufacturingLocationString"
												styleId="firstField">
													<html:option value="0:none">-</html:option>
													<logic:iterate name="mlList" id="ml">
														<option value="<bean:write name='ml' property='id'/>:<bean:write name='ml' property='value'/>"><bean:write name="ml" property="value"/></option>
													</logic:iterate>
												</html:select>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Year of Manufacture: 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.yearOfManufacture" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Dedicated packaging:
											</td>
											<td class="colRight">
												<html:checkbox name="searchProductForm" property="searchCriteria.dedicatedPackaging"/>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Event: 
											</td>
											<td class="colRight">											
											<bright:refDataList id="eventList" componentName="EventManager" methodName="getActiveEvents"/>

												<html:select name="searchProductForm" property="searchCriteria.eventString"
												styleId="firstField">
													<html:option value="0:none">- Any -</html:option>
													<logic:iterate name="eventList" id="event">
														<option value="<bean:write name='event' property='id'/>:<bean:write name='event' property='name'/>"><bean:write name='event' property='name'/></option>
													</logic:iterate>
												</html:select>

											</td>
										</tr>
										<tr>
											<td colspan="2">
												<b>Current Location:</b>
											</td>
										</tr>
										<tr>
											<td class="colLeft" valign="top">
												Location Description: 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.locationDescription" size="20" styleClass="standard"/>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;or...</td>
										</tr>
										<tr>
											<td class="colLeft" valign="top">
												Country: 
											</td>
											<td class="colRight">
												<bright:refDataList id="countryList" componentName="RefDataManager" methodName="getCountryList"/>
												<html:select name="searchProductForm" property="searchCriteria.countryId">
												styleId="firstField">
													<html:option value="0">- Please Select -</html:option>
													<logic:iterate name="countryList" id="country">
														<option value="<bean:write name='country' property='id'/>"><logic:notEmpty name="country" property="continent"><bean:write name="country" property="continent.name"/>&nbsp;-&nbsp;</logic:notEmpty><bean:write name="country" property="name"/></option>
													</logic:iterate>
												</html:select>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<b>Movement Dates:</b>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												Dates (dd/mm/yyyy): 
											</td>
											<td class="colRight">											
												<table cellspacing="0" cellpadding="5" border="0">
													<tr>
														<td>From:&nbsp;</td>
														<td><html:text name="searchProductForm" property="searchCriteria.fromDate" size="15" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('searchCriteria.fromDate')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a></td>
													</tr>
													<tr>
														<td>To:&nbsp;</td>
														<td><html:text name="searchProductForm" property="searchCriteria.toDate" size="15" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('searchCriteria.toDate')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="colLeft" >
												HS Code: 
											</td>
											<td class="colRight">
												<html:text name="searchProductForm" property="searchCriteria.hsCode" size="35" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="colLeft">&nbsp;</td>
											<td class="colRight" style="text-align: left; padding: 0.5em 1em 1em 4px;" colspan="2">
												<form action="search_results.html" method="get">
													<input type="submit" name="send" value="Search &raquo;"> 
												</form>
											</td>
										</tr>										
									</table>
								</td>
							</tr>
						</table>
						</html:form>
						
						</div>
						
						<p><h2>Welcome to Product Tracker!</h2></p>

						<p>Product Tracker is a database of GKN Driveline products, which allows you to record updated information about products and track their current locations. This application is also primarily used to monitor product movements.</p>
						
						<p>There are three main areas in this application:</p>
						
						<ol>
							<li><strong>Manage Products</strong> - administators can add, edit and delete products, search for products, and record product movements.<br /> <a href="../jsp/help.jsp#manage-products" target="_blank" onclick="popup(this); return false" >Read more &raquo;</a></li>
							<li><strong>Manage Events</strong> - administrators can add, edit and delete events, and attach 'coordinators' to each event.<br /> <a href="../jsp/help.jsp#manage-events" target="_blank" onclick="popup(this); return false" >Read more &raquo;</a></li>
							<li><strong>Manage Lists</strong> - administrators can update the system to reflect changes in locations, product types, and current vehicles. <br /><a href="../jsp/help.jsp#manage-lists" target="_blank" onclick="popup(this); return false" >Read more &raquo;</a></li>
						</ol>
						
						<p>If you have any questions while using these various parts of Product Tracker, you can click on the 'help' link in the upper right hand corner while in the application, which will give you an overview to each section, and useful tips. </p>
						
						<p><a href="../pdfs/product_tracker_coding_systems.pdf" target="_blank">New Product Coding System.</a><br/>
						<a href="../pdfs/product_tracker_hs_numbers.pdf" target="_blank">HS Number System.</a></p>
		
					</td>
				</tr>
				<tr>
					<td>
						<br style="clear:both"/>
						<%@include file="inc/_footer.jsp"%>
					</td>
				</tr>
			</table>
			&nbsp; 
		</td>
	</tr>
</table>
</body>
</html>

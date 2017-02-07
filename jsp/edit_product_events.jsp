 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		25-Jan-2005		Tamora James		Converted to jsp
	 d3		31-Feb-2005		Martin Wilson		Implemented
 --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/bright-tag.tld" prefix="bright" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
	<title>GKN Driveline - Product Tracker</title> 
	<link href="../css/main.css" rel="stylesheet" media="screen, print">
<style type="text/css" media="screen, print"><!--
td.colLeft { padding-top: 4px; padding-right: 4px; padding-bottom: 4px; width: 130px; vertical-align: middle; }
td.colRight { padding-top: 4px; padding-bottom: 4px; padding-left: 4px; vertical-align: middle; }
--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('products','none');">
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
					<td class="hruleGrey">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content">
									<p>
										<a href="../action/viewProduct?id=<bean:write name='productEventsForm' property='product.id'/>">&laquo; Back to product details</a>
									</p>
									<h2>
										Events for <bean:write name="productEventsForm" property="product.description"/>
									</h2>
									<table class="events" cellspacing="0">
									<tr>
									<th>Event</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Country</th><th>&nbsp;</th>
									</tr>
									<logic:empty name="productEventsForm" property="events">
									<tr>
									<td colspan="5">
									No events have been assigned to this product.
									</td>
									</tr>
									</logic:empty>
									<logic:iterate name="productEventsForm" property="events" id="event">
									<tr>
									<td><bean:write name="event" property="name"/></td>
									<td><bean:write name="event" property="startDate" format="dd/MM/yyyy"/></td>
									<td><bean:write name="event" property="endDate" format="dd/MM/yyyy"/></td>
									<td>
									<logic:equal name="event" property="country.id" value="0">
										-
									</logic:equal>
									<logic:notEmpty name="event" property="country">
										<bean:write name="event" property="country.name"/>
									</logic:notEmpty>
									</td>
									<td class="action">
									<a href="../action/deleteProductEvent?id=<bean:write name='productEventsForm' property='product.id'/>&eventid=<bean:write name='event' property='id'/>" onclick="return confirm('Are you sure you want to remove this event from this product?');">Remove</a>
									</td>
									</tr>
									</logic:iterate>
									</table>									

									
								</td>
							</tr>
							<tr>
								<td>
									<html:form action="addProductEvent" method="get">
									<html:hidden name="productEventsForm" property="product.id"/>
									<table width="60%;" cellspacing="0" cellpadding="2" border="0" style="margin: 0em 0em 1em;">
									<tr>
									<td>
									Add new event:
									</td>
									<td>
									<bright:refDataList id="eventList" componentName="EventManager" methodName="getActiveEvents"/>
									<html:select name="productEventsForm" property="newEventId"
									styleId="firstField">
										<html:option value="0">- Please Select -</html:option>
										<html:optionsCollection name="eventList" label="name" value="id"/>
									</html:select>	
									</td>
									<td>
									<input type="submit" value="Add &raquo;">
									</td>
									</tr>
									</table>
									</html:form>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
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

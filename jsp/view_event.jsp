<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	d1		25-Jan-2005		Tamora James		Created
	d2		31-Jan-2005		Martin Wilson		Implemented
	d3		26-Oct-2007		Matt Stevenson		Added coordinator email address
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

	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('events','view');">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_event.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/manage_events.gif" alt="Manage Events" width="210" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey" style="padding-bottom: 0;">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									<table border="0" class="product" cellspacing="0" style="margin-bottom: 1em;">
										<tr>
											<td class="h2" style="padding: 4px 6px; width:20%">Event Details</td>
											<td style="text-align: right; padding-right: 1em;">
												<a href="../action/viewEditEvent?id=<bean:write name='eventForm' property='event.id'/>">Edit Event</a>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Name of Event: 
											</td>
											<td class="fieldValue">
												<bean:write name="eventForm" property="event.name"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Start Date: 
											</td>
											<td class="fieldValue">
												<bean:write name="eventForm" property="event.startDate" format="dd/MM/yyyy"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												End Date: 
											</td>
											<td class="fieldValue">
												<bean:write name="eventForm" property="event.endDate" format="dd/MM/yyyy"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Country: 
											</td>
											<td class="fieldValue">
												<logic:equal name="eventForm" property="event.country.id" value="0">
													-
												</logic:equal>
												<bean:write name="eventForm" property="event.country.name"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Coordinator Email Address: 
											</td>
											<td class="fieldValue">
												<logic:empty name="eventForm" property="event.coordinatorEmailAddress">
													-
												</logic:empty>
												<bean:write name="eventForm" property="event.coordinatorEmailAddress"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Status
											</td>
											<td class="fieldValue">
												<logic:equal name="eventForm" property="event.notVisible" value="false">
													Active
												</logic:equal>
												<logic:equal name="eventForm" property="event.notVisible" value="true">
													Inactive
												</logic:equal>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="content">
									<table class="search-results" cellspacing="0">
									<tr>
									<th>
									Products used at this event
									</th>
									<th>
									&nbsp;
									</th>
									</tr>
									<logic:notEmpty name="eventForm" property="products">
									<logic:iterate name="eventForm" property="products" id="product">
									<tr>
									<td>
									<bean:write name="product" property="description"/>
									</td>
									<td class="action">
									<a href="../action/viewProduct?id=<bean:write name='product' property='id'/>">View</a>
									</td>
									</tr>
									</logic:iterate>
									</logic:notEmpty>
									<logic:empty name="eventForm" property="products">
									<tr>
									<td colspan="2">
									None
									</td>
									</tr>
									</logic:empty>
									</table>	
								</td>
							</tr>
						</table>
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

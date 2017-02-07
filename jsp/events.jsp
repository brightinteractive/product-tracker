 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<!-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		18-Jan-2005		Martin Wilson		Implemented
	 d3		26-Oct-2007		Matt Stevenson		Added link to event name
 -->
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
td.colLeft { padding-top: 4px; padding-right: 4px; padding-bottom: 4px; width: 130px; vertical-align: middle; }
td.colRight { padding-top: 4px; padding-bottom: 4px; padding-left: 4px; vertical-align: middle; }
--></style> 
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
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content">
									<p>The following is a list of all the events stored in the system. You can use the arrows next to the column labels to reorder the list of results, alternatively you can <a href="../action/viewEditEvent">add a new event &raquo;</a></p>
									
									<table border="0" class="events" cellspacing="0">
										<tr>
											<th>
												Event&nbsp;<a href="viewEvents?ordering=e.Name"><img src="../images/bits/up_arrow.gif" border="0" width="5" height="9" alt="up"></a> <a href="viewEvents?ordering=e.Name DESC"><img src="../images/bits/down_arrow.gif" border="0" width="5" height="9" alt="down"></a>
											</th>
											<th>
												Start&nbsp;<a href="viewEvents?ordering=e.StartDate"><img src="../images/bits/up_arrow.gif" border="0" width="5" height="9" alt="up"></a> <a href="viewEvents?ordering=e.StartDate DESC"><img src="../images/bits/down_arrow.gif" border="0" width="5" height="9" alt="down"></a>
											</th>
											<th>
												End&nbsp;<a href="viewEvents?ordering=e.EndDate"><img src="../images/bits/up_arrow.gif" border="0" width="5" height="9" alt="up"></a> <a href="viewEvents?ordering=e.EndDate DESC"><img src="../images/bits/down_arrow.gif" border="0" width="5" height="9" alt="down"></a>
											</th>
											<th>
												Country&nbsp;<a href="viewEvents?ordering=c.Name"><img src="../images/bits/up_arrow.gif" border="0" width="5" height="9" alt="up"></a> <a href="viewEvents?ordering=c.Name DESC"><img src="../images/bits/down_arrow.gif" border="0" width="5" height="9" alt="down"></a>
											</th>
											<th>Status</th>
											<th>&nbsp;</th>
										</tr>
										<logic:empty name="manageEventsForm" property="events">
										<tr>
											<td colspan="5">
												No events have been added yet.
											</td>
										</tr>
										</logic:empty>
										<logic:iterate name="manageEventsForm" property="events" id="event">
										<tr>
											<td>
												<b><a href="../action/viewEvent?id=<bean:write name='event' property='id'/>"><bean:write name="event" property="name"/></a></b>
											</td>
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
											<td>
												<logic:equal name="event" property="notVisible" value="false">
													Active
												</logic:equal>
												<logic:equal name="event" property="notVisible" value="true">
													Inactive
												</logic:equal>
											</td>
											<td class="action">
												<a href="../action/deleteEvent?id=<bean:write name='event' property='id'/>&last=true" onclick="return confirm('Are you sure you want to delete this event?');">delete</a>
											</td>
										</tr>
										</logic:iterate>
									</table>
									<p></p><p></p>
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

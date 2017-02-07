<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<!-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		18-Jan-2005		Martin Wilson		Implemented
	 d3		26-Oct-2007		Matt Stevenson		Added coordinator email address
 -->
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

--></style> 
<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
<script title="text/javascript" lang="Javascript" src="../js/calendar.js"></script>
</head>
<body onload="if (<c:out value='${eventForm.event.id}'/> > 0) { highlightMenu('events','view'); } else { highlightMenu('events','add'); }">
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
						<html:form action="saveEvent" method="post" focus="event.name">
						<html:hidden name="eventForm" property="event.id" />
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									
									<table class="product" cellspacing="0">
										<tr>
											<td class="h2" style="padding: 4px 6px; width:40%">Event Details</td>
											<td>&nbsp;
											</td>
										</tr> 
										<tr>
											<td colspan="2">
												<logic:equal name="eventForm" property="hasErrors" value="true">
													<logic:iterate name="eventForm" property="errors" id="error">
														<p class="error">
															<bean:write name="error" />
														</p>
													</logic:iterate>
												</logic:equal>
											</td>
										</tr>

										<tr>
											<td class="fieldLabel required" style="width: 40%">
												Name of Event:<span class="required">*</span> 
											</td>
											<td class="fieldValue">
												<html:text name="eventForm" property="event.name" size="30" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												Start Date:<span class="required">*</span>
											</td>
											<td class="fieldValue">
												<html:text name="eventForm" property="event.startDateStr" size="30" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('event.startDateStr')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												End Date:<span class="required">*</span> 
											</td>
											<td class="fieldValue">
												<html:text name="eventForm" property="event.endDateStr" size="30" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('event.endDateStr')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Country: 
											</td>
											<bright:refDataList id="countryList" componentName="RefDataManager" methodName="getCountryList"/>
											<td class="fieldValue">
												<html:select name="eventForm" property="event.country.id"
												styleId="firstField">
													<html:option value="0">- Please Select -</html:option>
													<logic:iterate name="countryList" id="country">
														<option value="<bean:write name='country' property='id'/>"><logic:notEmpty name="country" property="continent"><bean:write name="country" property="continent.name"/>&nbsp;-&nbsp;</logic:notEmpty><bean:write name="country" property="name"/>
														</option>
													</logic:iterate>
												</html:select>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Coordinator email address:
											</td>
											<td class="fieldValue">
												<html:text name="eventForm" property="event.coordinatorEmailAddress" size="30" styleClass="standard"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel">
												Hide this event? 
											</td>
											<td class="checkbox">
												<html:checkbox name="eventForm" property="event.notVisible" />
											</td>
										</tr>
										<tr>
											<td style="text-align: left; padding-bottom: 1em; padding-left: 4px;">
												<input name="Cancel" type="submit" value="&laquo; Cancel&nbsp;"> 
											</td>
											<td style="text-align: right; padding-bottom: 1em;">
												<input name="Save" type="submit" value="&nbsp;Save Changes &raquo;"> 
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</html:form>
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

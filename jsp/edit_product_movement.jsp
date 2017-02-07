 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		18-Mar-2005		Matt Stevenson		Created
	 d2		23-Mar-2005		Matt Stevenson		Changed layout
	 d3		15-Apr-2005		Matt Stevenson		Changed cancel button action
	 d4		18-Apr-2005		Matt Stevenson		Changed layout as per discussions with Anouska
	 d5		30-Oct-2007		Matt Stevenson		Added reminder email
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

--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/calendar.js"></script>
</head>
<body onload="highlightMenu('products','find');">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_product.jsp"%>
<bright:applicationSetting id="reminderDays" settingName="reminder-email-days-before"/>
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
					<td class="page" style="position: relative;">
						<html:form action="saveProductMovement" enctype="multipart/form-data" method="post">
						<html:hidden name="productMovementForm" property="productMovement.productId"/>
						<html:hidden name="productMovementForm" property="productMovement.id"/>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									<table class="product" cellspacing="0" border="0">
										<tr>
											<td class="h2" style="padding: 4px 6px;">Product Movement Details</td>
										</tr>
										<tr>
											<td>
												<logic:equal name="productMovementForm" property="hasErrors" value="true">
													<logic:iterate name="productMovementForm" property="errors" id="error">
														<p class="error">
															<bean:write name="error" />
														</p>
													</logic:iterate>
												</logic:equal>
											</td>
										</tr>
										<tr>
											<td>
												<p>Please enter / edit the details of your product movement in the form below. Firstly please select whether this is the current location of the product or a future location (selecting 'current location' will mean all product movements that happen prior to the this one will disappear from the list on the product page):</p>
											</td>
										</tr>
										<tr>
											<td><br>
												<table cellspacing="0" cellpadding="0" border="0" width="100%">
													<tr>
														<td width="17%">&nbsp;</td>
														<td>														
															<html:select name="productMovementForm" property="locationType">
																<option value="future">Future Location</option>
																<option value="current" <c:if test='${productMovementForm.locationType == "current"}'>selected</c:if>>Current Location</option>
															</html:select>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr>
											<td class="hruleGrey">&nbsp;</td>
										</tr>
										<tr>
											<td>
												<p>If the product is being used at an event, please select the event from the list below. For other locations, please select the product's 'Location' and a country from the list.</p>
											</td>
										</tr>
										<tr>
											<td>
												<table cellspacing="0" cellpadding="0" border="0" width="100%">
													<tr>
														<td class="fieldLabel required" rowspan="2">
															Choose Event:<span class="required">*</span>
														</td>
														<td class="fieldValue" rowspan="2">
															<html:select name="productMovementForm" property="productMovement.event.id">
																<option value="0">- No Event Selected -</option>
																<logic:iterate name="productMovementForm" property="events" id="event">
																	<c:if test="${event.id == productMovementForm.productMovement.event.id}">
																		<option value="<bean:write name='event' property='id'/>" selected><bean:write name="event" property="name"/></option>
																	</c:if>
																	<c:if test="${event.id != productMovementForm.productMovement.event.id}">
																		<option value="<bean:write name='event' property='id'/>"><bean:write name="event" property="name"/></option>
																	</c:if>
																</logic:iterate>
															</html:select>&nbsp;
															or...
														</td>
														<td class="fieldLabel required">
															Location:<span class="required">*</span>
														</td>
														<td class="fieldValue">
															<bright:refDataList id="locationList" componentName="ListManager" methodName="getListValues" argumentValue="EventLocation"/>
															<html:select name="productMovementForm" property="productMovement.locationId"
															styleId="firstField">
																<html:option value="0">- Please Select -</html:option>
																<html:optionsCollection name="locationList" label="value" value="id"/>
															</html:select> and...
														</td>
													</tr>
													<tr>
														<td class="fieldLabel required">
															Country:<span class="required">*</span>
														</td>
														<td class="fieldValue"><bright:refDataList id="countryList" componentName="RefDataManager" methodName="getCountryList"/>
														<html:select name="productMovementForm" property="productMovement.country.id"
															styleId="firstField">
																<html:option value="0">- Please Select -</html:option>
																<html:optionsCollection name="countryList" label="name" value="id"/>
															</html:select>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr>
											<td class="hruleGrey">&nbsp;</td>
										</tr>
										<tr>
											<td>
												<p>Enter the Arrival Date and Departure Date for this movement. The departure date and arrival dates can be left blank if there are no movements planned for the product.</p><br>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												<table cellspacing="0" cellpadding="0" border="0" width="100%">
													<tr>
														<td width="17%">
															Arrival Date:
														<td class="fieldValue" align="left">
															<html:text name="productMovementForm" property="productMovement.arrivalDateString" size="30" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('productMovement.arrivalDateString')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Departure Date:
														</td>
														<td class="fieldValue" align="left">
															<html:text name="productMovementForm" property="productMovement.departureDateString" size="30" styleClass="standard"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('productMovement.departureDateString')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a>
														</td>
													</tr>	
												</table>
											</td>
										</tr>
										<tr>
											<td class="hruleGrey">&nbsp;</td>
										</tr>
										<tr>
											<td>
												<p>By entering an email address in the box below, a reminder email will be sent <bean:write name="reminderDays"/> day<c:if test="${reminderDays > 1}">s</c:if> prior to the departure date and arrival date of the movement.</p><br>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												<table cellspacing="0" cellpadding="0" border="0" width="100%">
													<tr>
														<td>
															Reminder email address:
														<td class="fieldValue" align="left">
															<html:text name="productMovementForm" property="productMovement.reminderEmail" size="50"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td style="text-align: right; padding-bottom: 1em;" class="details">
												<input type="submit" value="&nbsp;Save Changes &raquo;">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</html:form>
						<div id="cancelButton">
						<form name="backForm" method="get" action="viewProduct">
							<input type="hidden" name="id" value="<bean:write name='productMovementForm' property='productMovement.productId'/>">
							<input type="submit" name="Cancel" value="&laquo; Cancel&nbsp;">
						</form>
						</div>
						<br>
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

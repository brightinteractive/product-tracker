 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		22-Mar-2005		Matt Stevenson		Created
	 d2		23-Mar-2005		Matt Stevenson		Incorporated buttons - including print functionality
	 d3		14-Jun-2005		Matt Stevenson		Incorporated search criteria
	 d4		22-Jun-2005		Matt Stevenson		Modified to include locations
	 d5		03-Nov-2006		Matt Stevenson		Added field ordering
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
	<logic:equal name="reportForm" property="isPrint" value="true">
		<link href="../css/report_print.css" rel="stylesheet" media="screen, print">
	</logic:equal>	
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('products','find'); <c:if test='${reportForm.isPrint}'>window.print();</c:if>">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_product.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0" id="pagetable">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr id="titlerow">
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/product_report.gif" alt="Product Report" width="210" height="28">
					</td>
				</tr>
				<tr id="titlerule">
					<td class="hruleGrey">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
						<p class="searchCriteria">
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.keywords">
							Keywords:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.keywords"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.locationDescription">
							Location:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.locationDescription"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.eventName">
							Event:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.eventName"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.datesString">
							Dates:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.datesString"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.oldCode">
							Code (old):&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.oldCode"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.newCode">
							Code (new):&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.newCode"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.productSegment.name">
							Product Segment:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.productSegment.name"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.vehicle.value">
							Vehicle:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.vehicle.value"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.model">
							Model:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.model"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.productType.value">
							Type:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.productType.value"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.manufacturingLocation.value">
							Manufacturing Location:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.manufacturingLocation.value"/>
							<br>
							</logic:notEmpty>
							<logic:notEmpty name="userprofile" property="lastSearchCriteria.yearOfManufacture">
							Year of Manufacture:&nbsp;&nbsp;<bean:write name="userprofile" property="lastSearchCriteria.yearOfManufacture"/>
							<br>
							</logic:notEmpty>
							<logic:notEqual name="userprofile" property="lastSearchCriteria.dedicatedPackaging" value="false">
							Dedicated Packaging:&nbsp;&nbsp;Yes
							<br>
							</logic:notEqual>							
						</p>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content">
								<logic:notEmpty name="reportForm" property="reportLines">
									<table class="search-results" cellspacing="0" width="90%">
										<tr>
											<logic:iterate name="reportForm" property="columnNames" id="colName">
												<th valign="top"><bean:write name="colName"/></th>
											</logic:iterate>
										</tr>
										<logic:iterate name="reportForm" property="reportLines" id="reportLine">
										<tr>
											<logic:iterate name="reportLine" property="fieldValues" id="fieldValue">
												<logic:notEmpty name="fieldValue">
													<td valign="top">
													<c:choose>
														<c:when test="${fieldValue == 'Location'}"><logic:empty name="reportLine" property="location">-</logic:empty><logic:notEmpty name="reportLine" property="location"><bean:write name="reportLine" property="location"/></logic:notEmpty></c:when>
														<c:when test="${fieldValue == 'LocationDates1'}"><bean:write name="reportLine" property="locationArrivalDate" format="dd/MM/yyyy"/></c:when>
														<c:when test="${fieldValue == 'LocationDates2'}"><bean:write name="reportLine" property="locationDepartureDate" format="dd/MM/yyyy"/></c:when>
														<c:when test="${fieldValue == 'NextLocation'}"><logic:empty name="reportLine" property="nextLocation">-</logic:empty><logic:notEmpty name="reportLine" property="nextLocation"><bean:write name="reportLine" property="nextLocation"/></logic:notEmpty></c:when>
														<c:when test="${fieldValue == 'NextLocationDates1'}"><bean:write name="reportLine" property="nextLocationArrivalDate" format="dd/MM/yyyy"/></c:when>
														<c:when test="${fieldValue == 'NextLocationDates2'}"><bean:write name="reportLine" property="nextLocationDepartureDate" format="dd/MM/yyyy"/></c:when>
														<c:when test="${fieldValue == 'Events'}"><bean:write name="reportLine" property="event"/></c:when>
														<c:otherwise><bean:write name="fieldValue"/></c:otherwise>
													</c:choose>
													</td>
												</logic:notEmpty>
												<logic:empty name="fieldValue">
													<td valign="top">N/A</td>
												</logic:empty>
											</logic:iterate>
										</tr>
										</logic:iterate>
									</table>
								</logic:notEmpty>
								</td>
							</tr>
						</table>
						<br>
						<table cellspacing="0" cellpadding="0" border="0" width="90%" id="buttonstable">
							<tr>
								<td align="left" valign="top">
									<form name="backForm" action="../jsp/report_generate.jsp" method="get">
										<bean:parameter id="lastFieldList" name="fieldOrder" value=""/>
										<input type="hidden" name="fieldOrder" value="<bean:write name='lastFieldList'/>">
										<bean:parameter id="lastFieldDescList" name="fieldOrderDesc" value=""/>
										<input type="hidden" name="fieldOrderDesc" value="<bean:write name='lastFieldDescList'/>">
										<logic:iterate name="reportForm" property="fullColumnNames" id="colName" indexId="index">
											<input type="hidden" name="<bean:write name='colName'/>" value="<bean:write name='reportForm' property='<%= "methodName[" + index + "]" %>'/>">
										</logic:iterate>
										<input type="submit" name="back" value="&laquo; Back">
									</form>
								</td>
								<td align="right" valign="top">
									<form name="printForm" action="../action/generateReport" method="get" target="_blank">
										<bean:parameter id="ordering" name="ordering" value=""/>
										<input type="hidden" name="ordering" value="<bean:write name='ordering'/>"/>
										<logic:iterate name="reportForm" property="fullColumnNames" id="colName" indexId="index">
											<input type="hidden" name="<bean:write name='colName'/>" value="<bean:write name='reportForm' property='<%= "methodName[" + index + "]" %>'/>">
										</logic:iterate>
										<input type="hidden" name="fieldOrder" value="<bean:write name='reportForm' property='fieldOrder'/>">
										<input type="hidden" name="print" value="1">
										<input type="submit" name="next" value="Print Report">
									</form>
								</td>
								<td align="right" valign="top">
									<form name="printForm" action="../action/generateReport" method="get" target="_blank">
										<bean:parameter id="ordering" name="ordering" value=""/>
										<input type="hidden" name="ordering" value="<bean:write name='ordering'/>"/>
										<logic:iterate name="reportForm" property="fullColumnNames" id="colName" indexId="index">
											<input type="hidden" name="<bean:write name='colName'/>" value="<bean:write name='reportForm' property='<%= "methodName[" + index + "]" %>'/>">
										</logic:iterate>
										<input type="hidden" name="fieldOrder" value="<bean:write name='reportForm' property='fieldOrder'/>">
										<input type="hidden" name="download" value="1">
										<input type="submit" name="next" value="Download Report">
									</form>
								</td>
							</tr>
						</table>
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

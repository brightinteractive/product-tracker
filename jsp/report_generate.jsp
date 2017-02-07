 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		22-Mar-2005		Matt Stevenson		Created
	 d2		23-Mar-2005		Matt Stevenson		Added back button
	 d3		06-Apr-2005		Matt Stevenson		Changed arrow size and positioning
	 d4		22-Jun-2005		Matt Stevenson		Added report ordering list box
	 d5		30-Jun-2005		Matt Stevenson		Reworked report ordering
	 d6		28-Jul-2005		Matt Stevenson		Added label to report
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
select { width: 150px; }
--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/report.js"></script>
</head>
<body onload="highlightMenu('products','find');initFieldList();">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_product.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/generate_product_report.gif" alt="Generate Product Report" width="341" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey" style="padding-bottom: 0;">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page" style="text-align: center">
						<div align="left"><p>Select the fields that you would like to show on the report. To change the order of the fields, select a field in the list and then use the arrows to move the field up or down. Click 'Next' to see the report.</p>
						<logic:present name="reportForm">
							<logic:equal name="reportForm" property="hasErrors" value="true">
								<logic:iterate name="reportForm" property="errors" id="error">
									<p class="error">
									<bean:write name="error"/>
									</p>
								</logic:iterate>
							</logic:equal>
						</logic:present>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content" style="text-align: center">
									<html:form action="generateReport" method="get">

									<bean:parameter id="sort" name="sort" value="0"/>
									<input type="hidden" name="sort" value="<bean:write name='sort'/>">
									<bean:parameter id="lastFieldList" name="fieldOrder" value=""/>
									<input type="hidden" name="fieldOrder" id="fieldOrder" value="<bean:write name='lastFieldList'/>">
									<bean:parameter id="lastFieldDescList" name="fieldOrderDesc" value=""/>
									<input type="hidden" name="fieldOrderDesc" id="fieldOrderDesc" value="<bean:write name='lastFieldDescList'/>">
									<table width="80%" border="0" cellspacing="0" cellpadding="0" style="padding: 1em; margin-bottom: 0.5em; margin-left: 20%; text-align: left;">
										<tr>
											<td class="colLeft">
												<input type="checkbox" name="fm_Description" id="fm_2" value="Description" <logic:present parameter="fm_Description">checked</logic:present> onclick="addField(this, 'fm_Description','Description');">&nbsp;<label for="fm_2">Description</label>
												<br> 
												<input type="checkbox" name="fm_Code_(old)" id="fm_3" value="OldCode" <logic:present parameter="fm_Code_(old)">checked</logic:present> onclick="addField(this, 'fm_Code_(old)','Code (old)');">&nbsp;<label for="fm_3">Code (Old)</label>
												<br> 
												<input type="checkbox" name="fm_Code_(new)" id="fm_25" value="NewCode" <logic:present parameter="fm_Code_(new)">checked</logic:present> onclick="addField(this, 'fm_Code_(new)','Code (new)');">&nbsp;<label for="fm_25">Code (new)</label>
												<br>
												<input type="checkbox" name="fm_Product_Segment" id="fm_27" value="ProductSegment" <logic:present parameter="fm_Product_Segment">checked</logic:present> onclick="addField(this, 'fm_Product_Segment','Product Segment');">&nbsp;<label for="fm_27">Product Segment</label>
												<br>
												<input type="checkbox" name="fm_Vehicle" id="fm_4" value="Vehicle" <logic:present parameter="fm_Vehicle">checked</logic:present> onclick="addField(this, 'fm_Vehicle','Vehicle');">&nbsp;<label for="fm_4">Vehicle</label>
												<br> 
												<input type="checkbox" name="fm_Model" id="fm_5" value="Model" <logic:present parameter="fm_Model">checked</logic:present> onclick="addField(this, 'fm_Model','Model');">&nbsp;<label for="fm_5">Model</label>
												<br> 
												<input type="checkbox" name="fm_Type" id="fm_6" value="ProductType" <logic:present parameter="fm_Type">checked</logic:present> onclick="addField(this, 'fm_Type','Type');">&nbsp;<label for="fm_6">Type</label>
												<br> 
												<input type="checkbox" name="fm_Status" id="fm_20" value="Status" <logic:present parameter="fm_Status">checked</logic:present> onclick="addField(this, 'fm_Status','Status');">&nbsp;<label for="fm_20">Status</label>
												<br> 
												<input type="checkbox" name="fm_LastUpdateDate" id="fm_21" value="LastUpdateDate" <logic:present parameter="fm_LastUpdateDate">checked</logic:present> onclick="addField(this, 'fm_LastUpdateDate','LastUpdateDate');">&nbsp;<label for="fm_21">Last Update Date</label>
												<br> 
												<input type="checkbox" name="fm_LastUpdateDoneBy" id="fm_22" value="LastUpdateDoneBy" <logic:present parameter="fm_LastUpdateDoneBy">checked</logic:present> onclick="addField(this, 'fm_LastUpdateDoneBy','LastUpdateDoneBy');">&nbsp;<label for="fm_22">Last Update Done By</label>
												<br> 
												<input type="checkbox" name="fm_Manufacturing_Location" id="fm_7" value="ManufacturingLocation" <logic:present parameter="fm_Manufacturing_Location">checked</logic:present> onclick="addField(this, 'fm_Manufacturing_Location','Manufacturing Location');">&nbsp;<label for="fm_7">Manufacturing Location</label>
												<br>
												<input type="checkbox" name="fm_Contact_Person" id="fm_8" value="ContactPerson" <logic:present parameter="fm_Contact_Person">checked</logic:present> onclick="addField(this, 'fm_Contact_Person','Contact Person');">&nbsp;<label for="fm_8">Contact Person</label>
												<br> 
												<input type="checkbox" name="fm_Year_Of_Manufacture" id="fm_9" value="YearOfManufacture" <logic:present parameter="fm_Year_Of_Manufacture">checked</logic:present> onclick="addField(this, 'fm_Year_Of_Manufacture','Year Of Manufacture');">&nbsp;<label for="fm_9">Year of Manufacture</label>
												<br> 
												<input type="checkbox" name="fm_Length" id="fm_10" value="Length" <logic:present parameter="fm_Length">checked</logic:present> onclick="addField(this, 'fm_Length','Length');">&nbsp;<label for="fm_10">Length</label>
												<br> 
												<input type="checkbox" name="fm_Height" id="fm_11" value="Height" <logic:present parameter="fm_Height">checked</logic:present> onclick="addField(this, 'fm_Height','Height');">&nbsp;<label for="fm_11">Height or diameter</label>
												<br> 
												<input type="checkbox" name="fm_Width" id="fm_12" value="Width" <logic:present parameter="fm_Width">checked</logic:present> onclick="addField(this, 'fm_Width','Width');">&nbsp;<label for="fm_12">Width or diameter</label>
												<br> 
												<input type="checkbox" name="fm_Gross_Weight" id="fm_13" value="GrossWeight" <logic:present parameter="fm_Gross_Weight">checked</logic:present> onclick="addField(this, 'fm_Gross_Weight','Gross Weight');">&nbsp;<label for="fm_13">Gross Weight</label>
												<br> 
												<input type="checkbox" name="fm_Net_Weight" id="fm_26" value="NetWeight" <logic:present parameter="fm_Net_Weight">checked</logic:present> onclick="addField(this, 'fm_Net_Weight','Net Weight');">&nbsp;<label for="fm_26">Net Weight</label>
												<br> 
												<input type="checkbox" name="fm_Dedicated_Packaging" id="fm_14" value="DedicatedPackaging" <logic:present parameter="fm_Dedicated_Packaging">checked</logic:present> onclick="addField(this, 'fm_Dedicated_Packaging','Dedicated Packaging');">&nbsp;<label for="fm_14">Dedicated Packaging</label>
												<br> 
												<input type="checkbox" name="fm_Label" id="fm_19" value="Label" <logic:present parameter="fm_Label">checked</logic:present> onclick="addField(this, 'fm_Label','Label');">&nbsp;<label for="fm_19">Label</label>
												<br>
												<input type="checkbox" name="fm_Technology_Sheet_Available" id="fm_28" value="TechnologySheetAvailable" <logic:present parameter="fm_Technology_Sheet_Available">checked</logic:present> onclick="addField(this, 'fm_Technology_Sheet_Available','Technology Sheet Available');">&nbsp;<label for="fm_28">Technology Sheet Available</label>
												<br> 
												<input type="checkbox" name="fm_Comments" id="fm_15" value="Comments" <logic:present parameter="fm_Comments">checked</logic:present> onclick="addField(this, 'fm_Comments','Comments');">&nbsp;<label for="fm_15">Comments</label>
												<br> 
												<input type="checkbox" name="li_Location" id="fm_16" value="Location" <logic:present parameter="li_Location">checked</logic:present> onclick="addField(this, 'li_Location','Location');">&nbsp;<label for="fm_16">Location</label>
												<br> 
												<input type="checkbox" name="li_NextLocation" id="fm_23" value="NextLocation" <logic:present parameter="li_NextLocation">checked</logic:present> onclick="addField(this, 'li_NextLocation','NextLocation');">&nbsp;<label for="fm_23">Next Location</label>
												<br> 
												<input type="checkbox" name="fm_NextMovementDate" id="fm_24" value="NextMovementDate" <logic:present parameter="fm_NextMovementDate">checked</logic:present> onclick="addField(this, 'fm_NextMovementDate','NextMovementDate');">&nbsp;<label for="fm_24">Next Movement Date</label>
												<br> 
												<input type="checkbox" name="li_Events" id="fm_18" value="Events" <logic:present parameter="li_Events">checked</logic:present> onclick="addField(this, 'li_Events','Events');">&nbsp;<label for="fm_18">Events</label>
												<br> 
											</td>
											<td>
												<h2>Field Order</h2>
												<table border="0" cellspacing="0" cellpadding="0">
												<tr>
												<td>
													<select id="fieldOrderList" size="12" multiple="multiple">
													</select>
												</td>
												<td style="padding-left: 5px; vertical-align: top;">
													<div>
													<input type="image" src="../images/bits/move_up.gif" onclick="moveSelected(true); return false;">
													</div>
													<input type="image" src="../images/bits/move_down.gif" onclick="moveSelected(false); return false;">
												</td>
												</tr>
												<tr>
													<td colspan="2">
														<br>Sort search report by:
													</td>
												</tr>
												<tr>
													<td colspan="2">
														<select name="ordering" size="1">
															<option value="none">- Select -</option>
															<option value="p.oldCode">Code (old)</option>
															<option value="p.newCode">Code (new)</option>
															<option value="p.productSegmentId">Product Segment</option>
															<option value="p.Description">Description</option>
															<option value="p.Status">Status</option>
															<option value="p.LastUpdateDate">Last Update Date</option>
															<option value="p.LastUpdateDoneBy">Last Update Done By</option>
															<option value="p.Length">Length</option>
															<option value="p.YearOfManufacture">Year of Manufacture</option>
															<option value="v.Value">Vehicle</option>
															<option value="pt.Value">Type</option>
															<option value="ml.Value">Manufacturing Location</option>
															<option value="event">Event</option>
															<option value="location">Location</option>
														</select>
													</td>
												</tr>
												</table>
											</td>
										</tr>
									</table>
									<br>
									<div align="right" style="margin-right: 20px">
										<input type="submit" name="next" value="Next &raquo;">
									</div>
									</html:form>
									<b>
									<form name="backForm" action="../action/searchProduct" method="get">
									<input type="hidden" name="last" value="true">
									<div align="left">
										<input type="submit" name="back" value="&laquo; Back">
									</div>
									</form>
									<br>
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

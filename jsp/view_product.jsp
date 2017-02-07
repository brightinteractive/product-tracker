 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		25-Jan-2005		Tamora James		Converted to jsp
	 d3		31-Feb-2005		Martin Wilson		Implemented
	 d4		18-Mar-2005		Matt Stevenson		Added product movements
	 d5		21-Mar-2005		Matt Stevenson		Removed link to edit events
	 d6		23-Mar-2005		Matt Stevenson		Layout changes
	 d7		18-Apr-2005		Matt Stevenson		Added alt text to move on link
	 d8		16-May-2005		Matt Stevenson		Fixed problem with edit link
	 d9		25-Oct-2007		Matt Stevenson		Added next and previous links
	 d10	26-Oct-2007		Matt Stevenson		Added equipment fields
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
	<style type="text/css">
		tr.hiddenRow { display:none; }
		td.fieldLabel { width:140px;}
	</style>
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/label.js"></script>
	<script type="text/javascript">
		<!--
			function showEquipmentDiv (iId)
			{
				//check if this is the equipment type (id 68) - if so show the equipment div...
				if (iId == 68)
				{
					document.getElementById('equipmentRow').className = "";
				}
			}
		-->
	</script>
</head>

<bean:define id="helpSection" value="view-product" />
<!-- body onload="highlightMenu('products','find');" -->
<body onload="highlightMenu('products', 'find'); showEquipmentDiv(<c:out value='${productForm.product.productType.id}'/>);">
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
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									<p>
										<logic:notEmpty name="userprofile" property="goBackSearchCriteria"> 
											<span style="float: left;"><a href="../action/searchProduct?back=true">&laquo; Back to search results</a></span>
										</logic:notEmpty>
										
										<logic:notEmpty name="productForm" property="product.productSegment.icon">
											<img src="../images/bits/<bean:write name="productForm" property="product.productSegment.icon"/>" alt="<bean:write name="productForm" property="product.productSegment.name"/>" style="float:left; margin-left: 100px;"/>
										</logic:notEmpty>	
									
										<logic:equal name="productForm" property="showBrowser" value="true">
											<span style="float: right;">
												<bean:define id="productId" name="productForm" property="product.id"/>
												<bean:define id="previousId" name="userprofile" property='<%= "previousSearchResultId(" + productId + ")" %>' />
												<bean:define id="nextId" name="userprofile" property='<%= "nextSearchResultId(" + productId + ")" %>' />

												<c:if test="${previousId > 0}"><a href="viewProduct?id=<bean:write name='previousId'/>"></c:if>&laquo; previous<c:if test="${previousId > 0}"></a></c:if>&nbsp;|&nbsp;<c:if test="${nextId > 0}"><a href="viewProduct?id=<bean:write name='nextId'/>"></c:if>next &raquo;<c:if test="${nextId > 0}"></a></c:if>
											</span>
										</logic:equal>
										<br/><br/>
									</p>
									
									<table class="product" cellspacing="0">
										<tr>
											<td class="h2" style="padding: 4px 6px;">Product Details</td>
											<td style="text-align: right; padding-right: 1em;">
												<a href="../action/viewProductLabel?id=<bean:write name='productForm' property='product.id'/>" onclick="return openProductLabel(<bean:write name='productForm' property='product.id'/>);">View Label</a>&nbsp;&nbsp;<span style="color:#bbb">|</span>&nbsp;&nbsp;<a href="../action/viewEditProduct?id=<bean:write name='productForm' property='product.id'/>">Edit Details</a>
											</td>
										</tr>
										<tr>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel">
															Product Description:
														</td>
														<td class="fieldValue">
															<bean:write name="productForm" property="product.description"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Product Code (old):
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.oldCode">-</logic:empty>
															<bean:write name="productForm" property="product.oldCode"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Product Code (new):
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.newCode">-</logic:empty>
															<bean:write name="productForm" property="product.newCode"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Product Segment:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.productSegment">-</logic:empty>
															<logic:notEmpty name="productForm" property="product.productSegment"><bean:write name="productForm" property="product.productSegment.name"/></logic:notEmpty>															
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Vehicle:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.vehicle.value">-</logic:empty>
															<bean:write name="productForm" property="product.vehicle.value"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Model:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.model">-</logic:empty>
															<bean:write name="productForm" property="product.model"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Type:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.productType.value">-</logic:empty>
															<bean:write name="productForm" property="product.productType.value"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturing Location:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturingLocation.value">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturingLocation.value"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Contact Person:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.contactPerson">-</logic:empty>
															<bean:write name="productForm" property="product.contactPerson"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Year of Manufacture:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.yearOfManufacture">-</logic:empty>
															<bean:write name="productForm" property="product.yearOfManufacture"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Last Update Date:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.lastUpdateDate">-</logic:empty>
															<bean:write name="productForm" property="product.lastUpdateDate" format="dd/MM/yyyy"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Last Update Done By:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.lastUpdateDoneBy">-</logic:empty>
															<bean:write name="productForm" property="product.lastUpdateDoneBy"/>
														</td>
													</tr>
												</table>
											</td>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="h3">
															Dimensions
														</td>
														<td>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel" width="37%">
															Length:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.length">0</logic:empty>
															<bean:write name="productForm" property="product.length"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Height or diameter:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.height">0</logic:empty>
															<bean:write name="productForm" property="product.height"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Width or diameter:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.width">0</logic:empty>
															<bean:write name="productForm" property="product.width"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Gross Weight:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.grossWeight">0</logic:empty>
															<bean:write name="productForm" property="product.grossWeight"/> kg
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Net Weight:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.netWeight">0</logic:empty>
															<bean:write name="productForm" property="product.netWeight"/> kg
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Status:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.status">-</logic:empty>
															<bean:write name="productForm" property="product.status"/><logic:equal name="productForm" property="product.status" value="Last product treatment"><logic:notEmpty name="productForm" property="product.statusLastTreatmentDate"> on <bean:write name="productForm" property="product.statusLastTreatmentDate" format="dd/MM/yyyy"/></logic:notEmpty><logic:notEmpty name="productForm" property="product.statusLastTreatmentName"> by <bean:write name="productForm" property="product.statusLastTreatmentName"/></logic:notEmpty></logic:equal>
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
														<td>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Dedicated&nbsp;Packaging:
														</td>
														<td class="fieldValue">
															<logic:equal name="productForm" property="product.dedicatedPackaging" value="true">
															Yes
															</logic:equal>
															<logic:equal name="productForm" property="product.dedicatedPackaging" value="false">
															No
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Label:
														</td>
														<td class="fieldValue">
															<logic:equal name="productForm" property="product.label" value="true">
															Yes
															</logic:equal>
															<logic:equal name="productForm" property="product.label" value="false">
															No
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Technology&nbsp;Sheet&nbsp;Available:
														</td>
														<td class="fieldValue">
															<logic:equal name="productForm" property="product.technologySheetAvailable" value="true">
															Yes
															</logic:equal>
															<logic:equal name="productForm" property="product.technologySheetAvailable" value="false">
															No
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															HS Code:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.hsCode">-</logic:empty>
															<bean:write name="productForm" property="product.hsCode"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr id="equipmentRow" class="hiddenRow">
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel">
															Manufacturer:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturerName">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturerName"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Address:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturerAddress">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturerAddress"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Contact Name:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturerContactName">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturerContactName"/>
														</td>
													</tr>
												</table>
											</td>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel">
															Manufacturer Telephone:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturerTelephone">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturerTelephone"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Email Address:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.manufacturerEmail">-</logic:empty>
															<bean:write name="productForm" property="product.manufacturerEmail"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Size:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.equipmentSize">-</logic:empty>
															<bean:write name="productForm" property="product.equipmentSize"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Assembly Instructions for Events:
														</td>
														<td class="fieldValue">
															<logic:empty name="productForm" property="product.equipmentAssemblyInstructions">-</logic:empty>
															<bean:write name="productForm" property="product.equipmentAssemblyInstructions"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tr>
														<td class="fieldLabel" style="width: 40%; vertical-align: top;">
															Comments:
														</td>
														<td class="text">
															<logic:empty name="productForm" property="product.comments">No comments have been added.</logic:empty>
															<bean:write name="productForm" property="product.comments"/>
														</td>
													</tr>
												</table>
											</td>
											<td class="image">
												<logic:notEqual name="productForm" property="product.image.id" value="0">
													<img src="..<bean:write name='productForm' property='product.image.urlForPage'/>" alt="Product Image" width="<bean:write name='productForm' property='product.image.width'/>" height="<bean:write name='productForm' property='product.image.height'/>">
												</logic:notEqual>
												<logic:equal name="productForm" property="product.image.id" value="0">
													There is currently no image for this product.
												</logic:equal>
											</td>
										</tr>
										<c:if test="${productForm.product.id > 0}">
										<tr>
											<td colspan="2" class="details">
												<span id="movements"><b>Product Movements:</b></span><br>
												<c:if test="${productForm.product.noOfMovements > 0}">
													<p>Clicking the 'move on' link will move the product from the current product movement to the next one in the list.</p>
												</c:if>
												<div align="center">
													<table class="location" width="99%">
														<c:if test="${productForm.product.noOfMovements > 0}">
															<tr>
																<th width="30%" align="left" style="padding-left: 10px;">Event / Location</th>
																<th width="15%" align="left">Country</th>
																<th width="15%">Arrival Date</th>
																<th width="15%">Departure Date</th>
																<th>&nbsp;</th>
															</tr>
															<tr>
																<td colspan="5" style="padding-left: 10px;" align="left"><b>Past location(s):</b></td>
															</tr>
															<logic:notEmpty name="productForm" property="product.pastMovements">
																<logic:iterate name="productForm" property="product.pastMovements" id="movement" indexId="index">
																	<tr>
																		<%@include file="inc/_movement_columns.jsp"%>
																	</tr>
																</logic:iterate>
															</logic:notEmpty>

															<logic:notEmpty name="productForm" property="product.futureMovements">
																<tr>
																	<td colspan="5" style="padding-left: 10px;" align="left"><b>Current location:</b></td>
																</tr>

																<logic:iterate name="productForm" property="product.futureMovements" id="movement" indexId="index">
																	<c:if test="${index == 0}">
																		<tr class="current">
																		<c:set var="setRow" value="true"/>
																	</c:if>
																	<c:if test="${index == 1}">
																		<tr>
																			<td colspan="5" style="padding-left: 10px;" align="left"><b>Future location(s):</b></td>
																		</tr>
																		<c:set var="setRow" value="false"/>
																	</c:if>
																	<c:if test="${index >= 1}">
																		<tr>
																		<c:set var="setRow" value="false"/>
																	</c:if>
																		<%@include file="inc/_movement_columns.jsp"%>
																	</tr>
																</logic:iterate>
															</logic:notEmpty>

														</c:if>
												
														<c:if test="${productForm.product.noOfMovements <= 0}">
															<tr>
																<td colspan="5" align="left">There are currently no product movements recorded for this product</td>
															</tr>
														</c:if>
														<tr>
															<th colspan="5">&nbsp;</th>
														</tr>
														<tr>
															<td align="right" colspan="5"><a href="viewEditProductMovement?productid=<bean:write name='productForm' property='product.id'/>">Add new product movement...</a></td>
														</tr>
													</table>
													</form>
												</div>
											</td>
										</tr>
										</c:if>
										<tr>
											<td class="details" colspan="2">
												<table cellspacing="0" cellpadding="0" class="location" style="margin-bottom: 1em;" width="99%">
													<tr>
														<td class="h2">
															Used at the following events
														</td>
														<td style="text-align: right; padding-right: 0.5em">
														</td>
													</tr>
													<tr>
														<td colspan="2">
														<table class="events" cellspacing="0">
														<tr>
														<th>Event</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Country</th>
														</tr>
														<logic:empty name="productForm" property="events">
														<tr>
														<td colspan="4">
														This product has not been used at any events.
														</td>
														</tr>
														</logic:empty>
														<logic:iterate name="productForm" property="events" id="event">
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
														</tr>
														</logic:iterate>
														</table>									

														</td>
													</tr>
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

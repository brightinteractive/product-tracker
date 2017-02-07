 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		25-Jan-2005		Tamora James		Converted to jsp
	 d3		31-Feb-2005		Martin Wilson		Implemented
	 d4		18-Mar-2005		Matt Stevenson		Removed current and next locations
	 d5		16-May-2005		Matt Stevenson		Modified to allow image deletion
	 d6		19-Jul-2005		Matt Stevenson		Added old product code
	 d7		17-Feb-2006		Matt Stevenson		Minor text change
	 d8		25-Oct-2007		Matt Stevenson		Added status fields
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
	</style>
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/calendar.js"></script>
	<script type="text/javascript">
		<!--
			function hideTreatmentDiv (bSwitch)
			{
				if (bSwitch)
				{ 
					document.getElementById('treatmentDiv').style.display = 'none'; 
				}
			}

			function showTreatmentDiv (bSwitch)
			{
				if (bSwitch)
				{ 
					document.getElementById('treatmentDiv').style.display = 'block'; 
				}
			}

			function switchEquipmentDiv (iId)
			{
				//check if this is the equipment type (id 68) - if so show the equipment div...
				if (iId == 68)
				{
					document.getElementById('equipmentRow').className = "";
				}
				else
				{
					//otherwise, hide the div...
					document.getElementById('equipmentRow').className = "hiddenRow";
				}
			}

			var sCurrentStatus = '<c:out value="${productForm.product.status}"/>';
		-->
	</script>
	
</head>
<body onload="if (<c:out value='${productForm.product.id}'/> > 0) { highlightMenu('products','find'); } else { highlightMenu('products','add'); } if (sCurrentStatus == 'Last product treatment') { showTreatmentDiv(true); } switchEquipmentDiv(<c:out value='${productForm.product.productType.id}'/>);">
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
						<html:form action="saveProduct" enctype="multipart/form-data" method="post">
						<html:hidden name="productForm" property="product.id"/>
						<html:hidden name="productForm" property="preEditOldProductCode"/>
						<html:hidden name="productForm" property="preEditNewProductCode"/>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									<table class="product" cellspacing="0">
										<tr>
											<td class="h2" style="padding: 4px 6px;">Product Details</td>
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<logic:equal name="productForm" property="hasErrors" value="true">
													<logic:iterate name="productForm" property="errors" id="error">
														<p class="error">
															<bean:write name="error" />
														</p>
													</logic:iterate>
												</logic:equal>
											</td>
										</tr>
										<tr>
											<td class="details" width="50%">
												
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel required">
															Product Description:<span class="required">*</span>
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.description" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Product Code (old):
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.oldCode" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel required">
															Product Code (new):<span class="required">*</span>
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.newCode" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Product Segment:
														</td>
														<td class="fieldValue">
															<html:select name="productForm" property="product.productSegment.id"
															styleId="firstField">
																<html:option value="0">-</html:option>
																<html:optionsCollection name="productForm" property="productSegments" label="name" value="id"/>
															</html:select>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Vehicle:
														</td>
														<td class="fieldValue">

															<bright:refDataList id="vehicleList" componentName="ListManager" methodName="getListValues" argumentValue="Vehicle"/>
															<html:select name="productForm" property="product.vehicle.id"
															styleId="firstField">
																<html:option value="0">-</html:option>
																<html:optionsCollection name="vehicleList" label="value" value="id"/>
															</html:select>														
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															<i>Other:</i>
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.otherVehicle" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Model:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.model" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Type:
														</td>
														<td class="fieldValue">
															<bright:refDataList id="typeList" componentName="ListManager" methodName="getListValues" argumentValue="ProductType"/>
															<html:select name="productForm" property="product.productType.id"
															styleId="firstField" onchange="switchEquipmentDiv(this.options[this.selectedIndex].value);">
																<html:option value="0">-</html:option>
																<html:optionsCollection name="typeList" label="value" value="id"/>
															</html:select>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturing Location:
														</td>
														<td class="fieldValue">
															<bright:refDataList id="mlList" componentName="ListManager" methodName="getListValues" argumentValue="Location"/>
															<html:select name="productForm" property="product.manufacturingLocation.id"
															styleId="firstField">
																<html:option value="0">-</html:option>
																<html:optionsCollection name="mlList" label="value" value="id"/>
															</html:select>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Contact Person:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.contactPerson" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Year of Manufacture:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.yearOfManufacture" size="30" styleClass="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Last update date:
														</td>
														<td class="fieldValue">
															<input type="text" name="lastUpdateDateString" class="standard" value="<bean:write name='productForm' property='product.lastUpdateDate' format='dd/MM/yyyy'/>"/>&nbsp;<a href="#" onClick="openDatePicker(document.getElementsByName('lastUpdateDateString')[0],true);"><img src="../images/bits/calendar.gif" alt="pick the date" height="15" width="13" align="middle" border="0"></a>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Last update done by:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.lastUpdateDoneBy" size="30" styleClass="standard"/>
														</td>
													</tr>
													
												</table>
											</td>
											<td class="details" width="50%">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="h3">
															Dimensions
														</td>
														<td>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Length:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.length" size="30" styleClass="standard"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Height or diameter:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.height" size="30" styleClass="standard"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Width or diameter:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.width" size="30" styleClass="standard"/> mm
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Gross Weight:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.grossWeight" size="30" styleClass="standard"/> kg
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Net Weight:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.netWeight" size="30" styleClass="standard"/> kg
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Status:
														</td>
														<td class="fieldValue">
															<html:radio name="productForm" property="product.status" value="As good as new" onclick="hideTreatmentDiv(this.checked);"/>&nbsp;As good as new<br/>
															<html:radio name="productForm" property="product.status" value="Still usable" onclick="hideTreatmentDiv(this.checked);"/>&nbsp;Still usable<br/>
															<html:radio name="productForm" property="product.status" value="Needs refurbishment" onclick="hideTreatmentDiv(this.checked);"/>&nbsp;Needs refurbishment<br/>
															<html:radio name="productForm" property="product.status" value="Last product treatment" onclick="showTreatmentDiv(this.checked);"/>&nbsp;Last product treatment<span id="treatmentDiv" style="display: none;"> on <input type="text" name="statusLastTreatmentDateString" class="medium" value="<bean:write name='productForm' property='product.statusLastTreatmentDate' format='dd/MM/yyyy'/>"/><br/>by <html:text name="productForm" property="product.statusLastTreatmentName" styleId="medium"/></span><br/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Dedicated&nbsp;Packaging:
														</td>
														<td class="checkbox">
															<html:checkbox name="productForm" property="product.dedicatedPackaging" />
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Label:
														</td>
														<td class="checkbox">
															<html:checkbox name="productForm" property="product.label" />
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Technology Sheet Available:
														</td>
														<td class="checkbox">
															<html:checkbox name="productForm" property="product.technologySheetAvailable" />
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															HS Code:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.hsCode" size="30" styleId="standard"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr id="equipmentRow" class="hiddenRow">
											<td class="details" width="50%">
												<table  border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel">
															Manufacturer:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.manufacturerName" size="30" styleId="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Address:
														</td>
														<td class="fieldValue">
															<html:textarea name="productForm" property="product.manufacturerAddress" cols="32" rows="4"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Contact Name:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.manufacturerContactName" size="30" styleId="standard"/>
														</td>
													</tr>
												</table>
											</td>
											<td class="details" width="50%">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td class="fieldLabel">
															Manufacturer Telephone:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.manufacturerTelephone" size="30" styleId="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Manufacturer Email Address:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.manufacturerEmail" size="30" styleId="standard"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Size:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.equipmentSize" size="30" styleId="small"/>
														</td>
													</tr>
													<tr>
														<td class="fieldLabel">
															Assembly Instructions for Events:
														</td>
														<td class="fieldValue">
															<html:text name="productForm" property="product.equipmentAssemblyInstructions" size="30" styleId="small"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tr>
														<td class="fieldLabel" style="text-align: left;">
															Comments:
														</td>

													</tr>
													<tr>
														<td style="text-align: left;">
															<html:textarea name="productForm" property="product.comments" cols="40" rows="4" styleClass="comments"/>
														</td>
													</tr>
												</table>
											</td>
											<td class="details">
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tr>
														<td class="fieldLabel" style="text-align: left;">
															Image:
														</td>
	
													</tr>
													<tr>
														<td  style="padding-left: 65px;">
															<html:hidden name="productForm" property="product.image.id"/>
															<html:file name="productForm" property="newProductImage" size="30"/>
														</td>
													</tr>
													<logic:notEqual name="productForm" property="product.image.id" value="0">
													<tr>
														<td style="padding-left: 65px;">
															Current Image: <bean:write name='productForm' property='product.image.url'/><br>
															<em>Delete this image?:</em> <html:checkbox name="productForm" property="deleteImage"/>
														</td>
													</tr>
													</logic:notEqual>
												</table>
											</td>
										</tr>
										<tr>
											<td style="text-align: left; padding-bottom: 1em; padding-left: 4px;" class="details">
												<input type="submit" name="Cancel" value="&laquo; Cancel&nbsp;"/>
											</td>
											<td style="text-align: right; padding-bottom: 1em;" class="details"/>
												<input type="submit" value="&nbsp;Save Changes &raquo;">
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

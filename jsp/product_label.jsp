 
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

	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="javascript:print();">
<table class="top-level" width="500" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<img src="../images/gkn/masthead_print.gif" alt="GKN Driveline" width="500" height="31">
		</td>
	</tr>
</table>

<table class="top-level" width="500" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level" colspan="2">
		<h1 class="productLabel"><bean:write name="productForm" property="product.description"/></h1>
		
		<table class="productLabel">
			<tr>
				<td>Product Code (old)
				</td>
				<td>
					<logic:empty name="productForm" property="product.oldCode">-</logic:empty>
					<bean:write name="productForm" property="product.oldCode"/>
				</td>
			</tr>
			<tr>
				<td>Product Code (new)
				</td>
				<td>
					<logic:empty name="productForm" property="product.newCode">-</logic:empty>
					<bean:write name="productForm" property="product.newCode"/>
				</td>
			</tr>			
			<tr>
				<td>Product Category
				</td>
				<td><logic:empty name="productForm" property="product.productType.value">-</logic:empty>
					<bean:write name="productForm" property="product.productType.value"/>
				</td>
			</tr>
			<tr>
				<td>Product Segment
				</td>
				<td>
					<logic:empty name="productForm" property="product.productSegment.name">-</logic:empty>
					<bean:write name="productForm" property="product.productSegment.name"/>
				</td>
			</tr>
			<tr>
				<td>Vehicle
				</td>
				<td><logic:empty name="productForm" property="product.vehicle.value">-</logic:empty>
					<bean:write name="productForm" property="product.vehicle.value"/>
				</td>
			</tr>
			<tr>
				<td>Model
				</td>
				<td><logic:empty name="productForm" property="product.model">-</logic:empty>
					<bean:write name="productForm" property="product.model"/>
				</td>
			</tr>
			<tr>
				<td>Year of Manufacture
				</td>
				<td><logic:empty name="productForm" property="product.yearOfManufacture">-</logic:empty>
					<bean:write name="productForm" property="product.yearOfManufacture"/>
				</td>
			</tr>
			<tr>
				<td>Manufacturing Location
				</td>
				<td><logic:empty name="productForm" property="product.manufacturingLocation.value">-</logic:empty>
					<bean:write name="productForm" property="product.manufacturingLocation.value"/>
				</td>
			</tr>
			<tr>
				<td>Length/mm
				</td>
				<td><logic:empty name="productForm" property="product.length">0</logic:empty>
					<bean:write name="productForm" property="product.length"/>
				</td>
			</tr>
			<tr>
				<td>Width or diameter/mm
				</td>
				<td><logic:empty name="productForm" property="product.width">0</logic:empty>
															<bean:write name="productForm" property="product.width"/>
				</td>
			</tr>
			<tr>
				<td>Height or diameter/mm
				</td>
				<td><logic:empty name="productForm" property="product.height">0</logic:empty>
					<bean:write name="productForm" property="product.height"/>
				</td>
			</tr>
			<tr>
				<td>Gross Weight/kg
				</td>
				<td><logic:empty name="productForm" property="product.grossWeight">0</logic:empty>
					<bean:write name="productForm" property="product.grossWeight"/>
				</td>
			</tr>
			<tr>
				<td>Net Weight/kg
				</td>
				<td><logic:empty name="productForm" property="product.netWeight">0</logic:empty>
					<bean:write name="productForm" property="product.netWeight"/>
				</td>
			</tr>
		</table>
		

		<div id="imgWrapper">
			<logic:notEmpty name="productForm" property="product.productSegment.icon">
				<img src="../images/bits/<bean:write name="productForm" property="product.productSegment.icon"/>" alt="<bean:write name="productForm" property="product.productSegment.name"/>" style="float:left; margin-left: 7.4em;"/>
			</logic:notEmpty>	
			<logic:notEqual name="productForm" property="product.image.id" value="0">
				<img src="..<bean:write name='productForm' property='product.image.urlForPage'/>" alt="Product Image" width="<bean:write name='productForm' property='product.image.width'/>" height="<bean:write name='productForm' property='product.image.height'/>" style="float:left; margin-left:1em" />
			</logic:notEqual>
			<logic:equal name="productForm" property="product.image.id" value="0">
				<p style="float:left; padding-top:1em; margin: 0 0 0 1em; width: 100px;">There is currently no image for this product.</p>
			</logic:equal>
		</div>
		</td>
	</tr>
	<tr class="labelFooter">
<td style="text-align: left; padding: 0.5em 1em;">
<img src="../images/gkn/logo_small.gif" alt="GKN" width="100" height="21">
</td>
<td style="text-align: right; padding: 0.5em;">
<img src="../images/gkn/expect_more.gif" alt="Expect More" width="84" height="8">
</td>
</tr>
</table>
</body>
</html>

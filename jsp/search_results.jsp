 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<%-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		25-Jan-2005		Tamora James		Converted to jsp
	 d3		31-Feb-2005		Martin Wilson		Implemented
	 d4		14-Jun-2005		Matt Stevenson		Added product code
	 d5		05-Mar-2008		Matt Stevenson		Added next movement
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

<bean:parameter name="last" id="last" value=""/>

<c:choose>
	<c:when test="${last == true}">
		<body onload="highlightMenu('products','last');">
	</c:when>
	<c:when test="${searchProductForm.all}">
		<body onload="highlightMenu('products','all');">
	</c:when>
	<c:otherwise>
		<body onload="highlightMenu('products','find');">
	</c:otherwise>
</c:choose>
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
						<table  border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content">
								<logic:notEmpty name="searchProductForm" property="results">
									<c:choose>
										<c:when test="${searchProductForm.all}">
											<p class="">
												The following is a list of all the products currently stored in the system. If you would like to refine the list of products below then please visit the <a href="../action/viewSearchProduct">search page</a>.
											</p>
										</c:when>
										<c:otherwise>
											<p class="">
												<bean:size id="resultCount" name="searchProductForm" property="results"/>
												Your search returned <bean:write name="resultCount"/> 
												<logic:equal name="resultCount" value="1">
												result.
												</logic:equal>
												<logic:notEqual name="resultCount" value="1">
												results.
												</logic:notEqual>
												If the product you were looking for is not listed below you can <a href="../action/viewSearchProduct">search again</a>.
											</p>
										
											<logic:notEqual name="resultCount" value="0">
												<p>You can also <a href="../jsp/report_generate.jsp?<%= request.getQueryString() %>&sort=<bean:write name='searchProductForm' property='reportSort'/>">generate a report</a> of these search results.</p>
											</logic:notEqual>
										</c:otherwise>
									</c:choose>
									<table class="search-results" cellspacing="0" border="0">
									<tr>
										<th class="farLeft">
											Product Code (old)
										</th>
										<th>
											Product Code (new)
										</th>
										<th>
											Description
										</th>
										<th>
											Product Segment
										</th>
										<th>
											Current Location
										</th>
										<th>
											Next Location
										</th>
										<th>
											Next Movement Date
										</th>
										<th>
											Model
										</th>
										<th>
											Vehicle
										</th>
										<th>
											&nbsp;
										</th>
									</tr>
									<logic:iterate name="searchProductForm" property="results" id="product">
									<tr 
									   <c:choose>
                                 <c:when test="${product.status == 'Needs refurbishment'}">
                                    class="refurb"
                                 </c:when>
                                 <c:when test="${product.status == 'Still usable'}">
                                    class="usable"
                                 </c:when>
                              </c:choose>	
									>
										<td valign="top" class="farLeft">
											<bean:write name="product" property="oldCode"/> 							
										</td>
										<td valign="top">
											<bean:write name="product" property="newCode"/> 							
										</td>
										<td valign="top" style="width: 170px;">
											<b><a href="../action/viewProduct?id=<bean:write name='product' property='id'/>"><bean:write name="product" property="description"/></a></b>
										</td>
										<td valign="top">
											<bean:write name="product" property="productSegment.name"/>					
										</td>
										<td valign="top">
											<c:set var="movement" value="${product.currentMovement}"/>
											<%@include file="inc/_movement_sr.jsp"%>
										</td>
										<td valign="top">
											<c:set var="movement" value="${product.nextMovement}"/>
											<%@include file="inc/_movement_sr.jsp"%>
										</td>
										<td valign="top">
											<c:choose>
												<c:when test="${product.nextMovementDate == null}">
													-
												</c:when>
												<c:otherwise>
													<bean:write name="product" property="nextMovementDate" format="dd/MM/yyyy"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td valign="top">
											<c:choose>
												<c:when test="${product.model == null || product.model == ''}">
													-
												</c:when>
												<c:otherwise>
													<bean:write name="product" property="model"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td valign="top">
											<c:choose>
												<c:when test="${product.vehicle.value == null || product.vehicle.value == ''}">
													-
												</c:when>
												<c:otherwise>
													<bean:write name="product" property="vehicle.value"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td valign="top" class="action farRight" style="width:45px;">
											<a href="../action/deleteProduct?id=<bean:write name='product' property='id'/>" onclick="return confirm('Are you sure you want to delete this product?');">delete</a>
										</td>
									</tr>
									</logic:iterate>
									</table>
								</logic:notEmpty>
								<logic:empty name="searchProductForm" property="results">
									<p>
										Your search returned did not match any products - <a href="../action/viewSearchProduct">search again</a>.
									</p><br/>
								</logic:empty>
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

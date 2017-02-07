<!doctype html>
<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->
<!--[if lt IE 7 ]> <html class="no-js ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]>    <html class="no-js ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]>    <html class="no-js ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/bright-tag.tld" prefix="bright" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html class="no-js" lang="en">
<!--<![endif]-->
	<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
	<head>
		<title>GKN Product Tracker</title>
		
	<%@include file="./inc/_movement_tracker_head_elements.jsp"%>
	<!-- Google will often use these meta tags as its description of your page/site. Make it good. -->
		<meta name="title" content="" />
		<meta name="description" content="" />
	</head>
	<body>	
	<div id="wrap">
		<div id="header" class="container-fluid">
			<div id="header_inner" class="row-fluid">
				<div class="span6">
					<img src="../images/movement-tracker/gkndrivelinelogo.png" alt="GKN Driveline" id="logo"  /><h1>Product Movement Tracker</h1>
				</div>
				<div class="span6">
					<div id="pt_filter">
						<a href="/product-tracker/action/viewHome" id="" >&larr; Back to Product Tracker</a>
						<a href="#" id="filter_btn" >&darr; Filter</a>
						
						<html:form action="formatMovementTrackerAsCsv" method="GET" >
							<c:if test="${not empty movementTrackerForm.productSegmentIds}">
								<c:forEach var="productSegmentId" items="${movementTrackerForm.productSegmentIds}">								
									<input type="hidden" name="productSegmentIds" value="<c:out value='${productSegmentId}'/>"/>
								</c:forEach>
							</c:if>
														
							<html:hidden property="includeUnknownProductSegments" />
							<input type="submit" id="DownloadBtn"  value="Download .csv" class="btn" />
						</html:form>
					</div>
				</div>
			</div>
		</div>
		<div id="pt_container">
			<div id="pt_container_inner">
				<div id="pt_container_wrap" class="hide">
					<div id="product_details_tbl_container">	
					<div id="product_details_tbl_container_inner">	
					<div id="product_details_header_tbl">
					<table id="" class="pt_table">
						<thead>
							<tr>
								<th id="code_col" >Code</th>
								<th id="details_col" class="last">Product Details</th>
							</tr>
						</thead>
					</table>
					</div>
					<table id="product_details_tbl" class="pt_table">
						<thead>
							<tr id="details_headers_fake" >
								<th id="code_col" >Code</th>
								<th id="details_col" class="">Product Details</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
					</div>
					</div>
					
					<div id="product_movement_tbl_container">
					<div id="product_movement_tbl_container_inner">
					<div id="product_movement_header_tbl_container">
					<div id="product_movement_header_tbl">
					<table id="" class="pt_table">
						<thead>
							<tr id="week_headers" >
							</tr>
							<tr id="day_headers">
							</tr>
						</thead>
					</table>
					</div>
					</div>
	
					<div id="product_movement_tbl">
					<table id="" class="pt_table">
						<thead>
							<tr id="day_headers_fake">
							</tr>
						</thead>
	 					<tbody></tbody>
					</table>
					</div>
					</div>
	
					</div>
					
				</div>
			</div>
				<div id="pt_load_indicator" class="" ><img src="../images/movement-tracker/loader.gif" alt="" /><br />Preparing interface...</div>
			
		</div>
	
		<div id="pt_filter_options" class="hide">
			<html:form action="viewMovementTracker" method="GET">
				<logic:iterate name="movementTrackerForm" property="productSegments" id="productSegment">
					<label class="checkbox">
						<html:multibox property="productSegmentIds"> 
							<bean:write name="productSegment" property="id"/> 
						</html:multibox> 
						<bean:write name="productSegment" property="name"/> 
					</label>					
				</logic:iterate>
				<label class="checkbox"><html:checkbox property="includeUnknownProductSegments" />Unknown Segment</label>				
				<input type="submit" value="Filter" id="filterSubmitBtn" class="btn"  />&nbsp;&nbsp; or <a href="#" id="filterClose" >cancel</a>
			</html:form>
	 	</div>
		
		<div id="pt_event_panel" class="">
			<div id="pt_event_panel_inner">
 				<ul id="pt_event_panel_details"></ul>
			</div>
		</div>		
	</div>
	
	<%@include file="./inc/_movement_tracker_foot_elements.jsp"%>

	</body>
</html>

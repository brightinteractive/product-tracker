 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<!-- History:
	 d1		23-Dec-2004		Tamora James		Created
	 d2		18-Jan-2005		Martin Wilson		Implemented
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

	<style type="text/css">

	</style>
</head>
<body id="help">

	<h1>Product tracker: <span>Help</span></h1>
	
	
	<div class="section">
		<a name="manage-products"></a>
		<br />
		<h2>Manage Products</h2>
		
		<p>You can view, add, edit and delete products in the 'Manage Products' section. When viewing search results, you can click on the description to bring up the details of each product.</p>
		
		<p>This is also the area where you are able to record and monitor product movements. For each product, you will see two boxes under Product Movements - one that details all past, current, and future locations where the product has been/will be, and another that shows only the events pertaining to the product in question.</p>
		
		<p>The first box is where you can control product movements. The current location is highlighted in the table.</p>
		
		<p>You can edit or delete past locations and also add new future movements with corresponding dates. For each movement created, you are able to assign a person to the movement by inserting their email address. This person would then receive reminder emails 1 day prior to the departure date and 1 day prior to the arrival date of the movement.</p>
		  
		<p><em>Note: the current location will not automatically change based on the dates in the system. It is the responsibility of the user to click 'move on' to adjust the product accordingly.</em></p>

		
		<p class="close">[ <a href="" onclick="window.close();">close this window</a> ]</p>
	</div>
	
	<div class="section">
		<a name="manage-events"></a>
<br />
		<h2>Manage Events</h2>
		
		<p>You can view, add, edit and delete events in the 'Manage Events' section. When viewing the list of Events, you can click on the event to bring up details.</p>
		
		<p>On this detail page, you are able to edit the event, and also see a list of products used at the event. Clicking the 'view' link next to each product will bring you to the product detail page for that product.</p>
		
		<p>You can also add new events in the 'Manage Events' section. For each event, you are able to choose a coordinator, who will receive regular emails, informing them of any products arriving at that event within 24 hours.</p>

		<p class="close">[ <a href="" onclick="window.close();">close this window</a> ]</p>
	</div>
	
		<div class="section">
		<a name="manage-lists"></a>
<br />
		<h2>Manage Lists</h2>
		
		<p>In this section, users are able to update the current lists to include any additions to such areas as Locations, Vehicle types, and Product types.</p>
		
		<p>Any additions made in this area will mean that you can choose these items when doing such things as adding products and changing product movement locations.</p>
	
		<p class="close">[ <a href="" onclick="window.close();">close this window</a> ]</p>
	</div>
	
	
<br />
	<div id="footer">
	
		<img src="../images/gkn/expect_more.gif" border="0" width="84" height="8" alt="">
		<span class="copyright">&copy; <a href="http://www.gknplc.co.uk/">GKN plc</a></span>
		
	</div>

</body>
</html>

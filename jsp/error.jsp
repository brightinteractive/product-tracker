 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- Website designed and developed by Bright Interactive, http://www.bright-interactive.com -->
<!-- History:
	 d2		04-Feb-2005		Martin Wilson		Created
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
<style type="text/css" media="screen, print"><!--
td.colLeft { padding-top: 4px; padding-right: 4px; padding-bottom: 4px; vertical-align: middle; }
td.colRight { padding-top: 4px; padding-bottom: 4px; padding-left: 4px; vertical-align: middle; }
--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('products','find');">
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
					<td class="page" style="text-align: center">
						<p>
							Sorry, an unexpected error has occurred. If this continues to happen, please contact <a href="mailto:support@bright-interactive.com">Bright Interactive</a>.
						</p>
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

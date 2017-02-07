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
<style type="text/css" media="screen, print"><!--
td.colLeft { padding-top: 4px; padding-right: 4px; padding-bottom: 4px; width: 130px; vertical-align: middle; }
td.colRight { padding-top: 4px; padding-bottom: 4px; padding-left: 4px; vertical-align: middle; }
--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('lists','view');">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_list.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker" width="213" height="28"><img src="../images/title/manage_lists.gif" alt="Manage Lists" width="182" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em 1em;">
							<tr>
								<td class="content">
									
									<table class="events" cellspacing="0">
									<tr>
									<th><bean:write name="listForm" property="list.description"/></th>
									<th>&nbsp;</th>
									</tr>
									<logic:iterate name="listForm" property="listItems" id="listItem">
									<tr>
									<td><bean:write name="listItem" property="value"/></td>
									<td class="action">
									<a href="deleteListValue?id=<bean:write name='listForm' property='list.id'/>&valueid=<bean:write name='listItem' property='id'/>" onclick="return confirm('Are you sure you want to delete this list value?');">Delete</a>
									</td>
									</tr>
									</logic:iterate>
									<logic:empty name="listForm" property="listItems">
									<tr>
									<td colspan="2">There are no values for this list.</td>
									</tr>
									</logic:empty>
									</tr>
									</table>
									
								</td>
							</tr>
							<tr>
								<td>
									<html:form action="addListValue" method="post">
									<input type="hidden" name="listId" value="<bean:write name='listForm' property='list.id'/>"/>
									<input type="hidden" name="id" value="<bean:write name='listForm' property='list.id'/>"/>
									<table cellspacing="0" cellpadding="2" border="0" style="margin: 0em 0em 1em;">
									<tr>
									<td>
									Add list item:
									</td>
									<td>
									<html:text name="listValueForm" property="listValue.value" size="24" value=
									""/>
									</td>
									<td>
									<input type="submit" value="Add &raquo;">
									</td>
									</tr>
									</table>
									</html:form>
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

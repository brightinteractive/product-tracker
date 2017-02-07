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
<body>
<%@include file="inc/_top_menu.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/login.gif" alt="Login" width="83" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">
		
							<logic:present name="loginForm">
							<logic:equal name="loginForm" property="loginFailed" value="true">
								<p class="error">The username or password was incorrect.</p>
							</logic:equal>
							</logic:present>
							<logic:present name="loginForm">
							<logic:equal name="loginForm" property="accountSuspended" value="true">
								<p class="error">Your account has been suspended.</p>
							</logic:equal>
							</logic:present>
							<p>To start using the system, please enter your login details.</p>				
							<html:form action="/login" method="post" focus="username">
								<html:hidden name="loginForm" property="forwardUrl" />
								
								<table class="login">
									<tr>
										<td colspan="2" class="text">
										</td>
									</tr>
									<tr>
										<td class="fieldLabel">
											Login: 
										</td>
										<td class="fieldValue">
											<html:text property="username" size="24" maxlength="24"/>
										</td>
									</tr>
									<tr>
										<td class="fieldLabel">
											Password: 
										</td>
										<td class="fieldValue">
											<html:password property="password" size="24" maxlength="24" redisplay="false"/> 
										</td>
									</tr>
									<tr>
										<td></td>
										<td style="padding: 6px 3px;">
											<input class="button" type="submit" value="Login" />
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

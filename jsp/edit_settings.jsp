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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
	<title>GKN Driveline - Product Tracker</title> 
	<link href="../css/main.css" rel="stylesheet" media="screen, print">
<style type="text/css" media="screen, print"><!--

--></style> 
	<script title="text/javascript" lang="Javascript" src="../js/popup.js"></script>
	<script title="text/javascript" lang="Javascript" src="../js/nav.js"></script>
</head>
<body onload="highlightMenu('settings','password');">
<%@include file="inc/_top_menu.jsp"%>
<%@include file="inc/_settings.jsp"%>
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-level">
			<table class="page" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="title">
						<img src="../images/title/product_tracker.gif" alt="Product Tracker:" width="213" height="28"><img src="../images/title/edit_settings.gif" alt="Edit Settings" width="176" height="28">
					</td>
				</tr>
				<tr>
					<td class="hruleGrey" style="padding-bottom: 0;">
						<img src="../images/bits/spacer.gif" border="0" width="1" height="1" alt=""> 
					</td>
				</tr>
				<tr>
					<td class="page">				
						<html:form action="changePassword" method="post" focus="oldPassword">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0em;">
							<tr>
								<td class="content">
									
									<table class="product" cellspacing="0">
										<tr>
											<td colspan="2">
												<logic:equal name="changePasswordForm" property="hasErrors" value="true">
													<logic:iterate name="changePasswordForm" property="errors" id="error">
														<p class="error">
															<bean:write name="error" />
														</p>
													</logic:iterate>
												</logic:equal>
											</td>
										</tr>

										<tr>
											<td class="fieldLabel required" style="width: 40%">
												Current Password:<span class="required">*</span> 
											</td>
											<td class="fieldValue">
												<html:password name="changePasswordForm" property="oldPassword" size="30" styleClass="standard"/> 
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												New Password:<span class="required">*</span>
											</td>
											<td class="fieldValue">
												<html:password name="changePasswordForm" property="newPassword" size="30" styleClass="standard"/>
											</td>
										</tr>
										<tr>
											<td class="fieldLabel required">
												Retype New Password:<span class="required">*</span> 
											</td>
											<td class="fieldValue">
												<html:password name="changePasswordForm" property="confirmNewPassword" size="30" styleClass="standard"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: left; padding-bottom: 1em; padding-left: 4px;">
												<input type="button" value="&laquo; Back&nbsp;" onclick="javascript:history.back();"> 
											</td>
											<td style="text-align: right; padding-bottom: 1em;">
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

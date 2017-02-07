
<table class="top-level" width="760" border="0" cellspacing="0" cellpadding="0" id="toptable">
	<tr>
		<td class="top-level">
			<img src="../images/gkn/masthead_top.jpg" alt="" height="20" width="760" border="0">
		</td>
	</tr>
	<tr>
		<td class="top-level">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="masthead-left">
						<img src="../images/gkn/logo_main_lhs.gif" alt="" height="33" width="157" border="0">
					</td>
					<td class="masthead-right">
						<img src="../images/gkn/logo_main_rhs.gif" alt="" height="24" width="199" border="0">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="top-level">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="mainNav">
						&nbsp;
					</th>
					<logic:equal name="userprofile" property="isLoggedIn" value="true">
					<td id="top_menu_products" class="mainNav">
						<a href="../action/viewHome">Manage Products</a>
					</td>
					<td id="top_menu_events" class="mainNav">
						<a href="../action/viewEvents">Manage Events</a> 
					</td>
					<td id="top_menu_lists" class="mainNav">
						<a href="../action/viewManageLists">Manage Lists</a> 
					</td>
					<td id="top_menu_settings" class="mainNav">
						<a href="../action/viewChangePassword">Edit Settings</a>
					</td>
					<td id="top_menu_settings" class="mainNav">
						<a href="../action/viewMovementTracker?default=1">Product Movement Tracker</a>
					</td>
					<td class="mainNav" style=" text-align: right;">
						&nbsp; <a href="../action/logout">Log out &raquo;</a>&nbsp;&nbsp;
					</td>
					</logic:equal>
				</tr>
			</table>
		</td>
	</tr>
</table>

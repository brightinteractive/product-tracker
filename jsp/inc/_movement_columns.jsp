<c:if test="${movement.event != null && movement.event.id > 0}">
	<td style="padding-left: 10px;" align="left"><bean:write name="movement" property="event.name"/></td>
	<td align="left"><bean:write name="movement" property="event.country.name"/></td>
</c:if>
<c:if test="${movement.event == null || movement.event.id <= 0}">
	<td style="padding-left: 10px;" align="left"><bean:write name="movement" property="locationValue"/></td>
	<td align="left"><bean:write name="movement" property="country.name"/></td>
</c:if>
<td><bean:write name="movement" property="arrivalDate" format="dd/MM/yyyy"/></td>
<td><bean:write name="movement" property="departureDate" format="dd/MM/yyyy"/></td>
<td align="left"><a href="viewEditProductMovement?id=<bean:write name='movement' property='id'/>&productid=<bean:write name='productForm' property='product.id'/>">edit</a>&nbsp;|&nbsp;<a href="deleteProductMovement?id=<bean:write name='movement' property='id'/>&productid=<bean:write name='productForm' property='product.id'/>" onclick="return confirm('Are you sure you want to delete this product movement?');">delete</a><logic:equal name="setRow" value="true">&nbsp;|&nbsp;<a href="#" onclick="if (confirm('This will move the product onto the next location.')) { window.location = 'moveOnProduct?id=<bean:write name='movement' property='id'/>&productid=<bean:write name='productForm' property='product.id'/>'; }">move on</a></logic:equal></td>
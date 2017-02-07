<c:choose>
	<c:when test="${movement == null}">
		-
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${movement.event != null && movement.event.id > 0}">
				<bean:write name="movement" property="event.name"/>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${movement.locationValue != null && movement.locationValue != ''}">
						<bean:write name="movement" property="locationValue"/>
					</c:when>
					<c:otherwise>
						-
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
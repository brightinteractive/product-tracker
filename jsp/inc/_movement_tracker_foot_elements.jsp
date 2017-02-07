		<!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if necessary -->
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
		<script>window.jQuery || document.write("<script src='../js/lib/jquery-1.7.1.min.js'>\x3C/script>")</script>
		<script src="../js/lib/datejs-read-only/build/date-en-GB.js" type="text/javascript"></script>
 		
		<!-- All Bright javascript goes in here -->
				<!-- All Bright javascript goes in here -->

		<c:url var="formatMovementTrackerAsJsonUrl" value="/action/formatMovementTrackerAsJson">
			<c:if test="${not empty movementTrackerForm.productSegmentIds}">
				<c:forEach var="productSegmentId" items="${movementTrackerForm.productSegmentIds}">
					<c:param name="productSegmentIds" value="${productSegmentId}"/>
				</c:forEach>
			</c:if>
			<c:if test="${not empty movementTrackerForm.includeUnknownProductSegments}">
				<c:param name="includeUnknownProductSegments" value="${movementTrackerForm.includeUnknownProductSegments}"/>
			</c:if>
			
		</c:url>

		<script src="../js/movement_tracker.js?v=1.00" type="text/javascript"></script>
		<script type="text/javascript">
		<!--		
		var product_tracker_data;
		$.getJSON('<c:out value="${formatMovementTrackerAsJsonUrl}" escapeXml="false"/>', function(data) //  ../data/movement_tracker_test_data.json
		{			
			product_tracker_data = data;
			// startWeek: is the week we want to begin viewing from negative values for past, positive for future 
			// (e.g. -3 start three weeks ago and 4 start four weeks into future)
			var startWeek = -3;

			var weeksToShow = 30;
			
			var daysToShow = 7;

			// startDate default is the first day (Monday) of the current week 	
			var startDate = Date.today().setWeek(Date.today().getWeek() + startWeek).moveToDayOfWeek(1).toString("d");

			// override by manually passing a date as string e.g. ProductTracker("dd/mm/yyy");
			PT = ProductTracker(startDate, weeksToShow, daysToShow);
			PT.refreshCanvas();
			PT.init();			
		})
				
		//-->
		</script>
					
		
		<!-- Asynchronous google analytics; this is the official snippet.
			 Replace UA-XXXXXX-XX with your site's ID and uncomment to enable.

		<script>

		  var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-XXXXXX-XX']);
		  _gaq.push(['_trackPageview']);

		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();

		</script>
		-->
/*
===============
product_tracker.js
===============
Put your in-house javascript functions in here.
----------------------------------------------------------------------
Copyright 2011 Bright Interactive, All Rights Reserved.
*/

/*
	TODO:
	- lint
	- how to not show weekends? is this an issue?
	- show week start vertical lines
	- deal with very long text in table cells - e.g. long product name means there's no line up with movement table	
*/


if (!console) {
	var console = { log : function(){}, 
					time : function(){}, 
					timeEnd : function(){}  };
}
var ONE_DAY = 1000 * 60 * 60 * 24;
var ProductTracker = function(startDate, weeksToShow, daysToShow) {

	var controller = {
	
		name : "Product Tracker",
		interfaceIsBuilt : false,
		endDate: undefined,
		eventPanelHidden: true,
		standardCellWidth: 20, // see span.dayCell in css file
		

		setStartDate : function(newStartDate) {
			console.log("set start date");
   			startDate = newStartDate;
 		},
		
		getStartDate : function() {
			console.log("get start date");
   			return Date.parse(startDate);
 		},
 		
 		
		setWeeksToShow : function(newWeeksToShow) {
			console.log("set weeks to show");
   			weeksToShow = newWeeksToShow;
 		},
		
		getWeeksToShow : function() {
			console.log("get weeks to show");
   			return weeksToShow;
 		},
 		
		setDaysToShow : function(newDaysToShow) {
			console.log("set days to show");
   			daysToShow = newDaysToShow;
 		},
 		
		getDaysToShow : function() {
			console.log("get days to show");
   			return daysToShow;
 		},
 		
		getTotalCols : function() {
			console.log("get total columns");
   			return this.getWeeksToShow() * this.getDaysToShow();
 		},
 		
		getDaysBetween : function(d1, d2) {
			console.log("get days between two dates: "+d1+" "+d2);
			if ((!d1) || (!d2)) {return;};
   			return Math.round((Math.abs(d1.getTime() - d2.getTime()))/ONE_DAY);
 		},

		init : function() {
			console.log("init product tracker");
			this.startTimer();
			this.showLoader();
 			this.prepareInterface();
 			this.populateProductDetailsTable();
			this.populateProductMovementTableHeaders();
			this.populateProductMovementTable();
			this.initScrollEvents();	
			this.initEventPanel();	
			this.initDocumentClickEvents();	
			this.refreshCanvas();
 			
		},
		
		initDocumentClickEvents : function() {
			console.log("Init document click events");
			$(document).click(function(e){
// 				switch ($(e.target).attr('id')) {
// 					
// 				};
				
				// hide the filter if we're not clicking within it
				if ($.contains($("#pt_filter_options").get(0), $(e.target).get(0)) == false ) {
					$("#pt_filter_options").hide();
					$("#filter_btn").removeClass('active');
				}	
			});
		},		
		
		buildElementsList : {
			movementTableHeadersReady: false, 
			detailsTableReady: false,
			movementsTableReady: false
		},

		startTimer : function() {
			console.time("interface ready");
		},
		
		stopTimer : function() {
			console.timeEnd("interface ready");
 		},
		
		prepareInterface : function() {
			console.log("prepare interface");
 			$("#pt_container_wrap").hide();
			
			this.initFilter();
		},
		
		
		initScrollEvents : function() {
			console.log("init scroll events");
 			$("#product_movement_tbl_container").scroll(function(){
				
				$("#product_details_tbl_container").scrollTop($(this).scrollTop());
				$("#product_movement_header_tbl").scrollLeft($(this).scrollLeft());
				if (!controller.eventPanelHidden) {
					controller.hideEventDetailsPanel();
					controller.eventPanelHidden = true;
				};	
 			});
		},
		
		
		initFilter : function() {
			console.log("init filter");
			$("#filter_btn").click(function(e){
			
				$("#pt_filter_options").toggle();
				$("#filter_btn").toggleClass('active');
				
				e.stopPropagation();
			
			});
			
			$("#filterSubmitBtn").click(function(){
			
				$("#pt_filter_options").toggle();
				$("#filter_btn").toggleClass('active');
			
			});
			$("#filterClose").click(function(){
			
				$("#pt_filter_options").hide();
				$("#filter_btn").removeClass('active');				
			
			});
		},
		
		
		
		showLoader : function() {
			console.log("show loader");
			if(!this.interfaceIsBuilt) {
				$("#pt_load_indicator").show();
			};
 		},
		
 		showInterface : function() {
			console.log("show interface");
 			this.interfaceIsBuilt = true;
			$("#pt_load_indicator").hide();
 			$("#pt_container_wrap").show();
 			this.stopTimer();
		},
		
	
 		initEventPanel : function() {
			console.log("Build the event panel - which will show the event info in full");
			
 			
			$("td.event").hover(function(e){
				controller.prepareEventDetailsPanel(e);
				controller.showEventDetailsPanel();
			}, function(){
				controller.hideEventDetailsPanel();
			});			
			$("td.event span").hover(function(e){
				//alert("span");
			});			
 		},
		
	
		refreshCanvas : function() {
			console.log("refresh canvas");
			
			// set the width of the movement window to be current width minus the width of the details table
			$("#product_movement_tbl_container").css("width", $(window).width() - $("#product_details_tbl_container").width() - 44);
			
 			var scroll_bar_width = 16;
 			var scroll_bar_height = 16;
			
			
			// set the height of the overall container to window height minus some padding
			var layoutHeight = $(window).height() - 100;
			var fake_header_height = $("#day_headers_fake").height() + 45;
			
			//$("#pt_container").css("height", layoutHeight);
			$("#pt_container").animate({ height: layoutHeight});
			$("#pt_container_inner").css("height", layoutHeight);
			$("#product_movement_tbl_container").css("height", layoutHeight - fake_header_height);
			$("#product_details_tbl_container").css("height", layoutHeight - fake_header_height - scroll_bar_height);
			var details_table_width = $("#product_details_tbl_container").width();
			var movement_table_width = $("#product_movement_tbl_container").width();
			var details_table_height = $("#product_details_tbl_container table").height();
 			$("#product_details_tbl_container table").css("width", details_table_width);
 			$("#product_movement_tbl_container table").css("width", movement_table_width - scroll_bar_width);
 			
 			$("#product_details_tbl_container_inner").css("height", details_table_height);
 			$("#product_movement_tbl_container_inner").css("height", details_table_height);
 			
 			$("#product_movement_header_tbl").width(movement_table_width-scroll_bar_width);
 			

		},
		
	
		populateProductDetailsTable : function() {
			console.log("populate product details table");
			
			var segment;
			var i = 0;
			var productDetailsArr = [];
			
			
			while (segment = product_tracker_data.segments[i++]) {

				productDetailsArr.push("<tr><th colspan='2'><span class='tdata sgm'>" + segment.name + "</span></th></tr>");
				var k =0;
				while (product = segment.products[k++]) {
					//highlight any overdue products
					var overdue_class = "";
					if (product.overdue) {
						overdue_class = "overdue";
					}
					if(product.name) {
						productDetailsArr.push("<tr class='"+overdue_class+"'><td><a href='/product-tracker/action/viewProduct?id="+ product.id +"'><span class='tdata pcode'>" + product.code + "</span></a></td><td><span class='tdata pname'><a href='/product-tracker/action/viewProduct?id="+ product.id +"'>" + product.name + "</span></a></td></tr>");
					};
				};
				
			};
			
			$("#product_details_tbl tbody").html(productDetailsArr.join(''));
			
			$(document).trigger("detailsTableReady");
		},
		
		populateProductMovementTable : function() {
			console.log("populate product movement table");

			var segment;
			var segment_index = 0;
			var productScheduleArr = [];
 			var totalCols = this.getTotalCols();
			
			while (segment = product_tracker_data.segments[segment_index++]) {

				// this is the blank row that separates product segments
				productScheduleArr.push("<tr><th colspan='" + totalCols + "'><span class='tdata sgm'>&nbsp;</span></th></tr>");
				
				var product_index =0;
				
				// loop through products in this segment 
				while (product = segment.products[product_index++]) {

					if(product.schedule.length) {
 						
 						productScheduleArr.push("<tr>");

 						var schedule_data;
						var schedule_index = 0;
						var startDate = this.getStartDate();
						var endDate = this.endDate;
						var dateCursor = startDate;
						var lastSetDate = startDate;
						var arrival_date;
						var end_date;
						var event_span;	
						var event_name;
						var altClass;
						var altEvent = false;
												
						
						schedule_data = product.schedule[schedule_index];
						
						var schedule_length = product.schedule.length;
						var non_viewable_schedule_count = 0;
						while(schedule_data = product.schedule[schedule_index++]){
							
							if (!schedule_data.arrival_date) {
								continue;
							};
							

							var arrival_date 	= Date.parse(schedule_data.arrival_date);
							var end_date 		= Date.parse(schedule_data.end_date);
							var event_span 		= schedule_data.event_span;						
							var event_name 		= schedule_data.event_name;
							//highlight any overdue products
							var overdue_event_class = "";
							if (schedule_data.overdue) {
								overdue_event_class = "overdue";
							}	
							
							// should this event be viewable?
							// if the arrival date is equal to or past the start date 
							// or the end date is past the start date, then it needs to be shown
							var eventIsViewable = ((Date.compare(startDate, arrival_date) < 1) || (Date.compare(startDate, end_date) == -1));
							//if (!eventIsViewable || (!arrival_date) || (!end_date)) { continue; };
							if (!eventIsViewable || (!arrival_date) || (!end_date)) { 
								non_viewable_schedule_count++;
								
								// we cannot view any of the events for this product - show an empty row
								if (non_viewable_schedule_count == schedule_length) {
									productScheduleArr.push("<td colspan='" + totalCols + "'><span class='tdata'>&nbsp;</span></td>");
									non_viewable_schedule_count = 0;
								}
								continue; 
							};
							
							// if the event is only partially viewable we need to 
							// alter the arrival date and event span
							if (arrival_date.isBefore(startDate)) {
								arrival_date = startDate;
								event_span = controller.getDaysBetween(arrival_date, end_date)+1;								
							}
							
							if (end_date.isAfter(endDate)) {
 								end_date = endDate;
								event_span = controller.getDaysBetween(arrival_date, end_date);								
							}
							
							//var calculated_event_span = controller.getDaysBetween(arrival_date, end_date);															
							//event_span = calculated_event_span;
														
							
							var nextGap = controller.getDaysBetween(lastSetDate, arrival_date) - 1;
							if (lastSetDate == startDate ) {
								nextGap = nextGap + 1;
							};
							
							// fill gap between last event and this event
							if (nextGap) {
								productScheduleArr.push("<td colspan='" + nextGap + "'><span class='tdata'>&nbsp;</span></td>");
							};
							
							var scheduleInfoHtml = controller.getScheduleInfo(schedule_data);
							var eventDetailWidth = (event_span * controller.standardCellWidth);
							
							
							var styleAtr = "";
							// some alterations need to be made if this is a one day event							
							if (Date.compare(end_date, arrival_date) == 0 ) {
								event_span = 1;
								styleAtr = "width: " + (controller.standardCellWidth) + "px";
								eventDetailWidth = 0;
							}
							
							var event_html = "<span class='tdata evn' style='width: "+eventDetailWidth+"px; '>" + event_name + "</span> ";
							if (!eventDetailWidth) {
								event_html = "";
							}
							
							altEvent = !altEvent;
							
							if (altEvent) {
								altClass = "alt";
							} else {
								altClass = "";
							};
							
							
							productScheduleArr.push("<td colspan='" + event_span + "' class='event " + altClass + " " + overdue_event_class + "' " + styleAtr +"> " + event_html + scheduleInfoHtml + "</td>");

							
							// set the last set date to be the end date for this event
							// or the arrival date if there is no end date
							lastSetDate = end_date || arrival_date;
							
							// if this is the last event for this product pad out the 
							// remaining columns with spaces
							if (product.schedule.length == schedule_index) {
								var lastGap = controller.getDaysBetween(lastSetDate, endDate);
								if (lastGap) {
									productScheduleArr.push("<td colspan='" + lastGap + "'><span class='tdata'>&nbsp;</span></td>");
								};	
							};
							
							
						};
						
						productScheduleArr.push("</tr>");
 						
					} else {
						// output a blank row - this product doesn't have any schedule entries
						productScheduleArr.push("<tr><td colspan='" + totalCols + "'><span class='tdata'>&nbsp;</span></td></tr>");
						
					}
				};
				
			};
			
			$("#product_movement_tbl tbody").html(productScheduleArr.join(''));



			$(document).trigger("movementsTableReady");
		},
		
	
		populateProductMovementTableHeaders : function() {
			console.log("populate product movement table headers");

			// show 6 months ahead, roughly 30 weeks
			var weeksToShow = this.getWeeksToShow();
			var daysToShow = this.getDaysToShow();			
			var weekNum;
			var dayNum;
			var dateOfDay;
 			var weekHeaderArr = []; 
			var dayHeaderArr = [];
 			var week_header_str;
			var startDate = this.getStartDate();
 			var weekHeaderValue = startDate.toString("d-MMM")
 			var dayHeaderStr;
			var dayHeaderValue;
			var firstDayOfWeek = startDate;
			
 			// generates the start of the week headers
			for ( weekNum = 0; weekNum < weeksToShow; weekNum++ ) {
				firstDayOfWeek = this.getStartDate().add(weekNum).weeks();
 				weekHeaderValue = firstDayOfWeek.toString("d-MMM");
 				yearValue = firstDayOfWeek.toString("yy");
				weekHeaderStr = "<th colspan='" + daysToShow + "'>" + weekHeaderValue + " " + "<em>'" + yearValue + "</em>" + "</th>";
				weekHeaderArr.push(weekHeaderStr);
				dateOfDay = firstDayOfWeek;
				
				// generates the dates for each day
				for ( dayNum = 0; dayNum < daysToShow; dayNum++ ) {
					isLast = (dayNum == (daysToShow-1)) ? "last" : "";
					isWeekend = (dayNum >= 5) ? "wkend" : "";
					dayHeaderValue = dateOfDay.toString("dd");
					dayHeaderStr = "<th class='" + isLast + " " + isWeekend + "'><span class='dayCell'>" + dayHeaderValue + "</span></th>";
					dayHeaderArr.push(dayHeaderStr);
					dateOfDay = firstDayOfWeek.add(1).day();
				};
				
				
			};
			
			this.endDate = dateOfDay;
			
			$("#week_headers").html(weekHeaderArr.join(''));
			$("#day_headers").html(dayHeaderArr.join(''));
			$("#day_headers_fake").html(dayHeaderArr.join(''));
			

			$(document).trigger("movementTableHeadersReady");

		},
		
		getScheduleInfo : function (schedule_data) {
			console.log("showing schedule info");
			
			var scheduleDataArr = [];
			
			scheduleDataArr.push("<ul>");
			scheduleDataArr.push("<li>Event / Location: "+schedule_data.event_name+"</li>");
			scheduleDataArr.push("<li>Arrival Date: "+schedule_data.arrival_date+"</li>");
			scheduleDataArr.push("<li>Departure Date: "+schedule_data.end_date+"</li>");
 			scheduleDataArr.push("</ul>");
			
			var scheduleDataHTML = scheduleDataArr.join("");
			return scheduleDataHTML;

 		},
		
		prepareEventDetailsPanel : function (e) {
			console.log("showing schedule info");
			var htmlToShow = "";
			if(e.target.nodeName == "TD") {
				htmlToShow = $(e.target).find("ul").html();
			} 
			else if (e.target.nodeName == "SPAN") {
				htmlToShow = $(e.target).parent().find("ul").html();
			} 
			else {
				htmlToShow = "Details not found";
			}
			$("#pt_event_panel_details").html(htmlToShow);
 		},
		
		showEventDetailsPanel : function (e) {
			controller.eventPanelHidden = false;
			$("#pt_event_panel").fadeIn(200);
 		},
		
		hideEventDetailsPanel : function (e) {
			controller.eventPanelHidden = true;		
			$("#pt_event_panel").hide();
 		},
		
		interfaceIsReady : function () {
			console.log("is interface ready?");

			var  isReady = true;
			if (this.interfaceIsBuilt) { return isReady; }
			
			for (var buildElementReady in this.buildElementsList) {
				if (!this.buildElementsList[buildElementReady]) {
					isReady = false;
  				}
			};
			
			return isReady;
 		},
		
		registerEvent : function (e) {
			console.log("register event: " + e.type);
			
  			this.buildElementsList[e.type] = true;
 			console.log(this.buildElementsList);
			if (this.interfaceIsReady()) {
				this.showInterface();	
			};
		}
	};
	
	return controller;

};


$(document).bind("movementTableHeadersReady", function(e){PT.registerEvent(e)} ); 
$(document).bind("detailsTableReady", function(e){PT.registerEvent(e)} ); 
$(document).bind("movementsTableReady", function(e){PT.registerEvent(e)} ); 
$(window).resize(function(){PT.refreshCanvas();});




/* 
======================================================================
DOM READY
----------------------------------------------------------------------
Generic actions to carry out on every page when DOM is loaded
*/




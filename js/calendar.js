/**
 * Bright Interactive, calendar.js
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * 
 * Contains JavaScript functions used by the date selector window.
 *
 */
/*
Ver  Date	        Who					Comments
--------------------------------------------------------------------------------
d1  24-Oct-2003     Martin Wilson       Created.
d2  14-Jul-2004     Chris Preager       Changed to bring calendar window to focus if already open.
--------------------------------------------------------------------------------
*/

var today = new Date();
var day   = today.getDate();
var month = today.getMonth();
var year  = y2k(today.getYear());
var dateFieldBeingChanged;

var bDaysBeforeMonths = true;

function y2k(number)    { return (number < 1000) ? number + 1900 : number; }

function padout(number) { return (number < 10) ? '0' + number : number; }

function restart() 
{
	if(bDaysBeforeMonths)
	{
		dateFieldBeingChanged.value = '' + padout(day) + '/' + padout(month - 0 + 1) + '/' + year;
	}
	else
	{
		dateFieldBeingChanged.value = '' + padout(month - 0 + 1)  + '/' + padout(day) + '/' + year;
	}

	mywindow.close();
}

function openDatePicker(a_dateField,a_bDaysBeforeMonths) 
{		
	bDaysBeforeMonths = a_bDaysBeforeMonths;

	dateFieldBeingChanged = a_dateField;
	
	if (typeof mywindow != 'undefined')
	{
		mywindow.close();
	}

	mywindow=open('../calendar/calendar.html','myname','resizable=no,width=350,height=280,left=100,top=30');
	mywindow.location.href = '../calendar/calendar.html';
	if (mywindow.opener == null) 
	{
		mywindow.opener = self;
	}
}

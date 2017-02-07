/**
 * Bright Interactive, label.js
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * 
 * Contains JavaScript function to open the product label.
 *
 */
/*
Ver  Date	        Who					Comments
--------------------------------------------------------------------------------
d1  07-Feb-2005     Martin Wilson       Created.
--------------------------------------------------------------------------------
*/

function openProductLabel(a_ProductId) 
{		
	if (typeof mywindow != 'undefined')
	{
		mywindow.close();
	}

	sOpenUrl = "../action/viewProductLabel?id=" + a_ProductId;

	mywindow=open(sOpenUrl ,'label','resizable=no,width=500,height=670,left=50,top=10');
	mywindow.location.href = sOpenUrl;
	if (mywindow.opener == null) 
	{
		mywindow.opener = self;
	}

	return (false);
}

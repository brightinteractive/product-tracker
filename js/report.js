/**
 * Bright Interactive, report.js
 *
 * Copyright 2003 Bright Interactive, All Rights Reserved.
 * 
 * Contains JavaScript functions used by the pages displaying reports.
 *
 */
/*
Ver  Date	        Who					Comments
--------------------------------------------------------------------------------
d1  24-Mar-2004     Martin Wilson       Created.
--------------------------------------------------------------------------------
*/


/**
* Called when a checkbox value is changed. 
* 
* @param a_checkbox - the checkbox
* @param - String a_sFieldName.
* @param - String a_sFieldDescription.
*/
function addField(	a_checkbox,
					a_sFieldName, 
					a_sFieldDescription) 
/*
------------------------------------------------------------------------
 d1   24-Mar-2005   Martin Wilson		Created.
------------------------------------------------------------------------
*/
{
	// Get the list:
	lbList = document.getElementById("fieldOrderList");

	// See if checked:
	if (a_checkbox.checked)
	{
		// Add to list:
		lbList.options[lbList.options.length] = 
			new Option(a_sFieldDescription, a_sFieldName);
	}
	else
	{
		var iDeleteIndex = 0;

		// Find the index of the item:
		for (i=0; i< lbList.options.length; i++)
		{
			current = lbList.options[i];

			if (current.value == a_sFieldName)
			{
				iDeleteIndex = i;
				break;
			}
		}

		// Remove the option:
		lbList.options[iDeleteIndex] = null;
	}

	// Update the field order var:
	setFieldOrderVar();
}

/**
* Called when the user clicks to move a row up or down
* 
*/
function moveSelected(a_up) 
/*
------------------------------------------------------------------------
 d1   24-Mar-2005   Martin Wilson		Created.
------------------------------------------------------------------------
*/
{
	// Get the list:
	lbList = document.getElementById("fieldOrderList");

	var selectedOption;
	var swapWithOption;
	var iSwapWithIndex = -1;

	// See if a row is selected:
	if (lbList.selectedIndex != -1)
	{
		// Get the selected option:
		selectedOption = lbList.options[lbList.selectedIndex];

		if (a_up)
		{
			// Get the option above:
			if (lbList.selectedIndex > 0)
			{
				iSwapWithIndex = lbList.selectedIndex -1;
			}
		}
		else
		{
			if (lbList.selectedIndex < (lbList.options.length -1))
			{
				iSwapWithIndex = lbList.selectedIndex +1;
			}
		}

		// See if it ok to swap:
		if (iSwapWithIndex > -1)
		{
			// Get the option to swap with:
			swapWithOption = lbList.options[iSwapWithIndex];

			// Swap:
			lbList.options[lbList.selectedIndex] = new Option(swapWithOption.text, swapWithOption.value);
			lbList.options[iSwapWithIndex] = new Option(selectedOption.text, selectedOption.value);

			// Select the same option again:
			lbList.selectedIndex = iSwapWithIndex;

			// Update the field order var:
			setFieldOrderVar();
		}
		
	}
}

/*
	Sets the field that returns the field order as a string.
*/
function setFieldOrderVar()
/*
------------------------------------------------------------------------
 d1   24-Mar-2005   Martin Wilson		Created.
------------------------------------------------------------------------
*/
{
	var lbList = document.getElementById("fieldOrderList");
	var sFields = "";
	var sFieldDescs = "";

	for (i=0; i< lbList.options.length; i++)
	{
		current = lbList.options[i];

		sFields = sFields + current.value;
		sFieldDescs = sFieldDescs + current.text;

		// Add a comma if not the last one:
		if (i != (lbList.options.length - 1))
		{
			sFields = sFields + ",";
			sFieldDescs = sFieldDescs + ",";
		}

	}

	// Set the hidden field:
	var hidFieldOrder = document.getElementById("fieldOrder");
	var hidFieldOrderDesc = document.getElementById("fieldOrderDesc");
	hidFieldOrder.value = sFields;
	hidFieldOrderDesc.value = sFieldDescs;

}

/*
	Sets the field that returns the field order as a string.
*/
function initFieldList()
/*
------------------------------------------------------------------------
 d1   24-Mar-2005   Martin Wilson		Created.
------------------------------------------------------------------------
*/
{
	var lbList = document.getElementById("fieldOrderList");
	var sFieldsLeft = "";
	var sDescsLeft = "";
	var sFieldName = "";
	var sFieldDesc = "";
	var hidFieldOrder = document.getElementById("fieldOrder");
	var hidFieldOrderDesc = document.getElementById("fieldOrderDesc");

	sFieldsLeft = hidFieldOrder.value;
	sDescsLeft = hidFieldOrderDesc.value;

	if (sFieldsLeft.length > 0)
	{

		while (sFieldsLeft.indexOf(",") > 0)
		{
			// Get the field name:
			sFieldName = sFieldsLeft.substring(0, sFieldsLeft.indexOf(","));

			// Get the description:
			sFieldDesc = sDescsLeft.substring(0, sDescsLeft.indexOf(","));

			// Move on to the next block:
			sFieldsLeft = sFieldsLeft.substring(sFieldsLeft.indexOf(",")+1, sFieldsLeft.length);
			sDescsLeft = sDescsLeft.substring(sDescsLeft.indexOf(",")+1, sDescsLeft.length);

			// Add to the list:
			lbList.options[lbList.options.length] = new Option(sFieldDesc, sFieldName);
		} 

		// Add the last one:
		lbList.options[lbList.options.length] = new Option(sDescsLeft, sFieldsLeft);
	}
}
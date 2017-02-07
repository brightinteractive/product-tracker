function popup(url)
{
	var features = 'resizable=yes,scrollbars=yes,toolbar=no,location=no,width=450,height=400';
	var theWindow = window.open(url, 'help', features);
  	theWindow.focus();
  	return theWindow;
}
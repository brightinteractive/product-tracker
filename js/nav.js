function highlightMenu(section,subsection)
{
	sectionId="top_menu_"+section;
	document.getElementById(sectionId).className="mainNavSelected";
	
	if (subsection)
	{
		subsectionId="sub_menu_"+subsection;
		document.getElementById(subsectionId).className="secNavSel";
	}
}
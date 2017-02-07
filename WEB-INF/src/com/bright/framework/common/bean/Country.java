package com.bright.framework.common.bean;

/**
 * Bright Interactive, Frameword
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * Country.java
 *
 * Contains the Country class.
 */
/*
Ver  Date	    		Who					Comments
--------------------------------------------------------------------------------
d1   04-Oct-2004		Martin Wilson		Created
d2	 24-Oct-2007		Matt Stevenson		Added continent
--------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents a Country
 *
 * @author  Bright Interactive
 * @version d1
 */
public class Country extends DataBean
{
   private String m_sName = null;   
	private boolean m_bIsLocal = false;	
	private Continent m_continent = null;
	
	
   /** Getter for property name.
    * @return Value of property name.
    *
    */
   public String getName()
   {
      return m_sName;
   }
   
   /** Setter for property name.
    * @param name New value of property name.
    *
    */
   public void setName(String a_sName)
   {
      m_sName = a_sName;
   }
	
	public void setIsLocal (boolean a_bIsLocal)
	{
		m_bIsLocal = a_bIsLocal;
	}
	
	public boolean getIsLocal ()
	{
		return (m_bIsLocal);
	}
	
	public void setContinent (Continent a_continent)
	{
		m_continent = a_continent;
	}
	
	public Continent getContinent ()
	{
		return (m_continent);
	}
}
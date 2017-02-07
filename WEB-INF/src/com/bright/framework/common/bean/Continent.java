package com.bright.framework.common.bean;

/**
 * Bright Interactive, Framework
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * Continent.java
 *
 * Contains the Continent class.
 */
/*
Ver  Date	    		Who					Comments
--------------------------------------------------------------------------------
d1   24-Oct-2007		Matt Stevenson		Created
--------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents a Continent
 *
 * @author  Bright Interactive
 * @version d1
 */
public class Continent extends DataBean
{
   private String m_sName = null;   
	
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
}
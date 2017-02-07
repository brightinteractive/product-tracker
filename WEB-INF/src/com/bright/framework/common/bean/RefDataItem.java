package com.bright.framework.common.bean;

/**
 * Bright Interactive, Country
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * RefDataItem.java
 *
 * Contains the RefDataItem class.
 */

/*
 Ver     Date           Who            Comments
 --------------------------------------------------------------------------------
 d1      02-Jun-2004    James Home     Created
 d2      04-Jun-2004    Martin Wilson  Now inherit from DataBean
 --------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents an item of simple refdata, wich contains an id and description
 *
 * @author  Bright Interactive
 * @version d1
 */
public class RefDataItem extends DataBean
{
   private String m_sDescription = null;   
	
   /** Getter for property description.
    * @return Value of property description.
    *
    */
   public String getDescription()
   {
      return m_sDescription;
   }
   
   /** Setter for property description.
    * @param name New value of property description.
    *
    */
   public void setDescription(String a_sDescription)
   {
      m_sDescription = a_sDescription;
   }
	
	
}
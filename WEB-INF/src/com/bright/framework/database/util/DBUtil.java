package com.bright.framework.database.util;

/**
 * Bright Interactive, framework
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * DBUtil.java
 *
 * Contains the DBUtil class.
 */
/*
    Ver     Date					Who						Comments
    --------------------------------------------------------------------------------
	  d1      04-Jun-2004		Martin Wilson			Created
    --------------------------------------------------------------------------------
*/

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bright.framework.database.bean.DataBean;

/**
 * Static methods for common db operations.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class DBUtil
{

	/*
	* Sets the field to either the id value of the object or null if the object is null
	*
	*@param PreparedStatement a_psql
	*@param int a_iFieldNumber
	*@param DataBean a_dataBean
	*@throws SQLException on error
	*/
	public static void setFieldIdOrNull(PreparedStatement a_psql,
													int a_iFieldNumber,
													DataBean a_dataBean)
													throws SQLException
	/*
	-----------------------------------------------------------------------
	d1      04-Jun-2004		Martin Wilson			Created
	-----------------------------------------------------------------------
	*/
	{
		if (a_dataBean != null)
		{
			setFieldIdOrNull(a_psql, a_iFieldNumber, a_dataBean.getId());
		}
		else
		{
			a_psql.setNull (a_iFieldNumber, java.sql.Types.BIGINT);
		}			
	}

	/*
	* Sets the field to the value of a_lId if > 0 otherwise null.
	*
	*@param PreparedStatement a_psql
	*@param int a_iFieldNumber
	*@param long a_lId
	*@throws SQLException on error
	*/
	public static void setFieldIdOrNull(PreparedStatement a_psql,
													int a_iFieldNumber,
													long a_lId)
													throws SQLException
	/*
	-----------------------------------------------------------------------
	d1      04-Jun-2004		Martin Wilson			Created
	-----------------------------------------------------------------------
	*/
	{
		if (a_lId > 0)
		{
			a_psql.setLong (a_iFieldNumber, a_lId);
		}
		else
		{
			a_psql.setNull (a_iFieldNumber, java.sql.Types.BIGINT);
		}		
	}	
	

	/*
	* Sets the field to the value of date if the java.util.Date != null otherwise null.
	*
	*@param PreparedStatement a_psql
	*@param int a_iFieldNumber
	*@param long a_lId
	*@throws SQLException on error
	*/
	public static void setFieldDateOrNull(PreparedStatement a_psql,
													int a_iFieldNumber,
													java.util.Date a_dtDate)
													throws SQLException
	/*
	-----------------------------------------------------------------------
	d1      04-Jun-2004		Martin Wilson			Created
	-----------------------------------------------------------------------
	*/
	{
		if (a_dtDate != null)
		{
			a_psql.setDate (a_iFieldNumber, new java.sql.Date (a_dtDate.getTime()));
		}
		else
		{
			a_psql.setNull (a_iFieldNumber, java.sql.Types.DATE);
		}		
	}		
  
	/*
	* Sets the field to the value of date if the java.util.Date != null otherwise null.
	*
	*@param PreparedStatement a_psql
	*@param int a_iFieldNumber
	*@param long a_lId
	*@throws SQLException on error
	*/
	public static void setFieldTimestampOrNull(PreparedStatement a_psql,
													int a_iFieldNumber,
													java.util.Date a_dtDate)
													throws SQLException
	/*
	-----------------------------------------------------------------------
	d1      04-Jun-2004		Martin Wilson			Created
	-----------------------------------------------------------------------
	*/
	{
		if (a_dtDate != null)
		{
			a_psql.setTimestamp (a_iFieldNumber, new java.sql.Timestamp (a_dtDate.getTime()));
		}
		else
		{
			a_psql.setNull (a_iFieldNumber, java.sql.Types.TIMESTAMP);
		}		
	}		
}

package com.bright.framework.common.bean;

/**
 * Bright Interactive, Address
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * Address.java
 *
 * Contains the Address class.
 */
/*
Ver  Date	    		Who				Comments
--------------------------------------------------------------------------------
d1   04-Oct-2004		Chris Preager	Created
--------------------------------------------------------------------------------
*/

import com.bright.framework.database.bean.DataBean;

/**
 * Represents an Address
 *
 * @author  Bright Interactive
 * @version d1
 */
public class Address extends DataBean
{
    private String m_sAddressLine1 = null;
    private String m_sAddressLine2 = null;
    private String m_sCity = null;
    private String m_sCounty = null;
    private String m_sPostcode = null;

    public void setAddressLine1 (String a_sAddressLine1)
    {
        m_sAddressLine1 = a_sAddressLine1;
    }
    public String getAddressLine1 ()
    {
        return (m_sAddressLine1);
    }

    public void setAddressLine2 (String a_sAddressLine2)
    {
        m_sAddressLine2 = a_sAddressLine2;
    }
    public String getAddressLine2 ()
    {
        return (m_sAddressLine2);
    }

    public void setCity (String a_sCity)
    {
        m_sCity = a_sCity;
    }
    public String getCity ()
    {
        return (m_sCity);
    }


    public void setCounty (String a_sCounty)
    {
        m_sCounty = a_sCounty;
    }
    public String getCounty ()
    {
		 return (m_sCounty);
    }


    public void setPostcode (String a_sPostCode)
    {
        m_sPostcode = a_sPostCode;
    }
    public String getPostcode ()
    {
        return (m_sPostcode);
    }


 	 public void copy(Address a_address)
	 {
		 setAddressLine1(a_address.getAddressLine1());
		 setAddressLine2(a_address.getAddressLine2());
		 setCity(a_address.getCity());
		 setCounty(a_address.getCounty());
		 setPostcode(a_address.getPostcode());
	 }
}
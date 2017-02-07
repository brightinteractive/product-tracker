package com.bright.producttracker.user.bean;

/**
 * Bright Interactive , ETA
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * TradesUserProfileFactory.java
 *
 * Contains the TradesUserProfileFactory class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   26-May-2004    Martin Wilson	Created
--------------------------------------------------------------------------------
*/

import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.user.bean.UserProfileFactory;

/**
 * Creates instances of MerchandiseUserProfile objects.
 *
 * @author  Bright Interactive
 * @version d1
 */
public class PTUserProfileFactory extends UserProfileFactory
{
    /*
     * Returns a new UserProfile object.
     *
     *@return UserProfile
     */
    public UserProfile createUserProfile()
    /*
    ------------------------------------------------------------------------
    d1   22-Jan-2004     Martin Wilson      Created.
    ------------------------------------------------------------------------
    */
    {
        // If the user object has been set then the user has logged in.
        return (new PTUserProfile());
    }

}
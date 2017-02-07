package com.bright.framework.user.bean;

/**
 * Bright Interactive , Reputable Trades
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * UserProfileFactory.java
 *
 * Contains the UserProfileFactory class.
 */
/*
Ver  Date	    Who			Comments
--------------------------------------------------------------------------------
d1   22-Jan-2004    Martin Wilson	Created
--------------------------------------------------------------------------------
*/


/**
 * Creates instances of UserProfile objects. Override this for a factory to create application-specific objects.
 *
 * @author  Bright Interactive
 * @version d1
 */
public abstract class UserProfileFactory
{
    /*
     * Returns a new UserProfile object.
     * Subclass and return your application specific UserProfile class
     *
     *@return UserProfile
     */
    public abstract UserProfile createUserProfile();

}
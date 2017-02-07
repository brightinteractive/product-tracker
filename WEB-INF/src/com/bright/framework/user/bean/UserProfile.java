package com.bright.framework.user.bean;

/**
 * Bright Interactive , ETA
 *
 * Copyright 2002 Bright Interactive, All Rights Reserved.
 * UserProfile.java
 *
 * Contains the UserProfile class.
 */
/*
Ver  Date            Who                Comments
--------------------------------------------------------------------------------
d1   24-May-2004     Martin Wilson      Created
--------------------------------------------------------------------------------
*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bn2web.common.constant.CommonConstants;
import com.bn2web.common.service.GlobalApplication;

/**
 * A wrapper for session scope objects. This object will be stored in the session.
 *
 * This is an abstract class: you should subclass this and UserProfileFactory for you application-specific classes.
 *
 * @author  Bright Interactive
 * @version d1
 */
public abstract class UserProfile implements CommonConstants
{
	protected User m_user = null;
	private static UserProfileFactory c_factory = null;

    /*
     * Default constructor- private to force use of getUserProfile.
     */
    protected UserProfile()
    /*
    ------------------------------------------------------------------------
     d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        /* Empty */
    }

    /*
     * Sets the instance of the UserProfileFactory to use. This method must be called on application startup.
     *@param UserProfile - the object to use.
     */
    public static void setUserProfileFactory(UserProfileFactory a_factory)
    /*
    ------------------------------------------------------------------------
    d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        c_factory = a_factory;
    }

    /*
     * A static method to get the UserProfile object from the session.
     * If it is not there, creates it.
     *
     *@param HttpSession a_session - the session.
     *@return UserProfile - the profile.
     */
    public static UserProfile getUserProfile(HttpSession a_session)
    /*
    ------------------------------------------------------------------------
    d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        // Get the object from the session:
        Object objProfile = a_session.getAttribute(USER_PROFILE_NAME);
        UserProfile userProfile = null;

        if (objProfile != null)
        {
            userProfile = (UserProfile)(objProfile);
        }
        else
        {
            if (c_factory == null)
            {
                GlobalApplication.getInstance().getLogger().error("UserProfileFactory has not been set. It needs to be in [App name]ActionServlet");
            }
            else
            {
                // Create an instance of the right type for this application.
                userProfile = c_factory.createUserProfile();

                a_session.setAttribute(USER_PROFILE_NAME, userProfile);
            }
        }

        return (userProfile);
    }

    /*
     * Resets the UserProfile object, e.g. when user logs out.
     *
     */
    public static void resetUserProfile(HttpSession a_session)
    /*
    ------------------------------------------------------------------------
    d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        // Remove the object from the session:
        a_session.removeAttribute(USER_PROFILE_NAME);

    }

    /*
     * A static method to get the UserProfile object from the request object.
     *
     *@param HttpServletRequest a_request -  the request object.
     *@return UserProfile - the profile.
     */
    public static UserProfile getUserProfile(HttpServletRequest a_request)
    /*
    ------------------------------------------------------------------------
     d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        // Get the session:
        HttpSession session = a_request.getSession(false);
        return (getUserProfile(session));
    }

    /*
     * Returns true if the user has logged in.
     *
     *@return boolean
     */
    public boolean getIsLoggedIn()
    /*
    ------------------------------------------------------------------------
    d1  24-May-2004  Martin Wilson           Created
    ------------------------------------------------------------------------
    */
    {
        // If the user object has been set then the user has logged in.
        return (m_user != null && m_user.getId() > 0);
    }

    /*
     * Returns true if the user is an admin user.
     *
     *@return boolean
     */
    public abstract boolean getIsAdmin();


    public void setUser(User a_user)
    {
        m_user = a_user;
    }

    public User getUser()
    {
        return (m_user);
    }

}
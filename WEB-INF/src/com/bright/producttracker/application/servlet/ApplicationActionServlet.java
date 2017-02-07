package com.bright.producttracker.application.servlet;

/**
 * Bright Interactive, Internal
 *
 * Copyright 2004 Bright Interactive,, All Rights Reserved.
 * ApplicationActionServlet.java
 *
 * Contains the ApplicationActionServlet class.
 */
/*
Ver  Date             Who		Comments
--------------------------------------------------------------------------------
d1   26-May-2004      Martin Wilson	Created.
--------------------------------------------------------------------------------
*/

import javax.servlet.ServletException;

import com.bn2web.common.servlet.Bn2ActionServlet;
import com.bright.producttracker.user.bean.PTUserProfile;
import com.bright.producttracker.user.bean.PTUserProfileFactory;

/**
 * Overrides the Bn2ActionServlet- so that we can do any application initialization specific to this application..
 *
 * @author  Bright Interactive
 * @version d1
 */
public class ApplicationActionServlet extends Bn2ActionServlet
{
    /*
    * Initialises the app.
    */
    public void init() throws ServletException
    /*
    ------------------------------------------------------------------------
     d1   25-Mar-2003  Martin Wilson		Created.
     ------------------------------------------------------------------------
    */
    {
        // Call the super class:
        super.init();
         
        // Do any application specific initialisation here.
        
        // Make sure we use instances of the right UserProfile:
        PTUserProfile.setUserProfileFactory(new PTUserProfileFactory());
    }
}

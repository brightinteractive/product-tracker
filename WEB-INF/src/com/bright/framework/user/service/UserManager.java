package com.bright.framework.user.service;


/**
 * Bright Interactive, GKN
 *
 * Copyright 2004 Bright Interactive, All Rights Reserved.
 * UserManager.java
 *
 *
 */
/*
Ver  Date					Who						Comments
--------------------------------------------------------------------------------
d1  18-Jan-2005         	Martin Wilson			Created code.
d2	11-Jan-2008				Matt Stevenson			Added code for basic signon without login
--------------------------------------------------------------------------------
 */

import com.bn2web.common.exception.Bn2Exception;
import com.bn2web.common.service.Bn2Manager;
import com.bright.framework.constant.FrameworkSettings;
import com.bright.framework.password.PasswordHasher;
import com.bright.framework.user.bean.User;
import com.bright.framework.user.bean.UserProfile;
import com.bright.framework.user.constant.UserConstants;
import com.bright.framework.user.exception.AccountSuspendedException;
import com.bright.framework.user.exception.InvalidLoginException;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * UserManager
 *
 * @author  Bright Interactive
 * @version d1
 */
public class UserManager extends Bn2Manager implements UserConstants
{
	protected DataSourceComponent m_dataSource = null;
	protected Vector m_vecAvailableLoginCodes = new Vector();
	protected Vector m_vecWaitingLoginCodes = new Vector();
	protected PasswordHasher passwordHasher = new PasswordHasher();
	
	/**
	 * Prepare a list of available login codes for signon without login...
	 * 
	 */
	private void prepCodes()
	/*
	------------------------------------------------------------------------
	d2	11-Jan-2008				Matt Stevenson			Created
	------------------------------------------------------------------------
	*/
	{
		//prepare the list of available login codes...
		for (int i=0; i<k_iNoOfLoginCodes; i++)
		{
			m_vecAvailableLoginCodes.add(this.getPasscode(k_iLoginCodeLength));
		}
	}
	
	
	
	/**
	 * get a random code n characters long
	 * 
	 * @param int n - the no of letters long
	 * @return String - the code
	 * 
	 */
	private String getPasscode(int n) 
	/*
	------------------------------------------------------------------------
	d2	11-Jan-2008				Matt Stevenson			Created
	------------------------------------------------------------------------
	*/
	{
		char[] pw = new char[n];
		int iChar  = 'A';
		int iRandom = 0;
		
		for (int i=0; i < n; i++)
		{
			iRandom = (int)(Math.random() * 3);
			
			switch(iRandom) 
			{
				case 0: iChar = '0' +  (int)(Math.random() * 10); break;
				case 1: iChar = 'a' +  (int)(Math.random() * 26); break;
				case 2: iChar = 'A' +  (int)(Math.random() * 26); break;
			}
			pw[i] = (char)iChar;
		}
		
		return (new String(pw));
	}
	
	
	/**
	 * Check if the provided code is one of the ones waiting for a login
	 * 
	 * @param String a_sCode - the code to check
	 * @return boolean - whether the code is valid or not
	 * 
	 */
	public boolean canSignonWithoutLogin (String a_sCode)
	/*
	------------------------------------------------------------------------
	d2	11-Jan-2008				Matt Stevenson			Created
	------------------------------------------------------------------------
	*/
	{
		//check to see if any of the waiting codes match the one provided.
		for (int i=0; i<m_vecWaitingLoginCodes.size(); i++)
		{
			if (a_sCode.equals((String)(m_vecWaitingLoginCodes.elementAt(i))))
			{
				m_vecAvailableLoginCodes.add(a_sCode);
				m_vecWaitingLoginCodes.removeElementAt(i);
				return (true);
			}
		}
		
		return (false);
	}
	
	/**
	 * Put a code in the list of codes waiting for login
	 * 
	 * @return String - the code that was listed
	 * 
	 */
	public String authoriseSignonWithoutLogin ()
	/*
	------------------------------------------------------------------------
	d2	11-Jan-2008				Matt Stevenson			Created
	------------------------------------------------------------------------
	*/
	{
		if (m_vecAvailableLoginCodes.size() <= 0)
		{
			this.prepCodes();
		}
		
		//take a code from the available list, add it to the waiting list and return it
		String sCode = (String)(m_vecAvailableLoginCodes.elementAt(0));
		m_vecAvailableLoginCodes.removeElementAt(0);
		m_vecWaitingLoginCodes.add(sCode);
		
		return (sCode);
	}
	

	 /*
	  * Logs in the user and sets the User object, etc in the profile.
	  * 
	  * TODO: Add transaction support
	  *
	  *@param String a_sUsername - the username.
	  *@param String a_sPassword - the password.
	  */
	public void login (	HttpSession a_userSession,
								String a_sUsername,
								String a_sPassword,
								boolean a_bIgnorePasswordIfAdmin) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	 d1  18-Jan-2005         Martin Wilson			Created code.
	 ------------------------------------------------------------------------
	  */
	{
		Connection cCon = null;

		// Get the UserProfile object:
		UserProfile userProfile = UserProfile.getUserProfile (a_userSession);

		if (a_sPassword == null && !(a_bIgnorePasswordIfAdmin && userProfile.getIsAdmin()))
		{
			throw new InvalidLoginException ("The password cannot be empty.");
		}

		try
		{
			// Basic user check:
			if (a_sUsername == null)
			{
				throw new InvalidLoginException ("The username cannot be empty");
			}

			cCon = m_dataSource.getConnection ();

			String sSql = "SELECT u.Id, u.Username, u.Password, u.IsSuspended " +
								 "FROM User u " +
								 "WHERE u.Username=?";

			PreparedStatement psql = cCon.prepareStatement (sSql);

			psql.setString (1, a_sUsername);

			ResultSet rs = psql.executeQuery ();

			//check to see if a row was retreived from the db
			//if so mark this as a successful login
			if (rs.next ())
			{
				// Check the password:
				String sPasswordInDB = rs.getString ("u.Password");

				// See if we need to check the passwords:
				if (!(a_bIgnorePasswordIfAdmin && userProfile.getIsAdmin()))
				{
					// Check the password was right:
					if (!passwordsAreEqual (a_sPassword, sPasswordInDB))
					{
						throw new InvalidLoginException ("Incorrect password");
					}
				}

				//Reset user profile in session (as otherwise not logging out before logging in again can leave old data in there)
				UserProfile.resetUserProfile(a_userSession);
				// Point to the new (resetted) userProfile object:
				userProfile = UserProfile.getUserProfile (a_userSession);

				// Create the User object:
				User user = new User ();
				user.setId (rs.getLong ("u.Id"));
				user.setUsername (a_sUsername);

				// If this user's account has been suspended then throw an exception:
				if (rs.getBoolean ("u.IsSuspended"))
				{
					throw new AccountSuspendedException ("The user's account has been suspended");
				}

				// Set the user object in the profile:
				userProfile.setUser (user);

			}
			else
			{
				throw new InvalidLoginException ("No such user");
			}
			
			psql.close();
		}
		catch (SQLException e)
		{
			try
			{
				cCon.rollback ();
			}
			catch (SQLException sqle)
			{
				//ignore.
			}

			throw new Bn2Exception ("Exception occurred checking user login : "+e);
		}
		finally
		{
			// Return the connection to the pool:
			if (cCon != null)
			{
				try
				{
					cCon.commit ();
					cCon.close ();
				}

				catch (SQLException sqle)
				{
					m_logger.error ("SQL Exception whilst trying to close connection " + sqle.getMessage ());
				}
			}
		}
	}

	 /*
	  * Logs out the user
	  *
	  *@param HttpSession a_userSession - the session.
	  */
	public void logout (HttpSession a_userSession)
	 /*
	 ------------------------------------------------------------------------
	  d1   24-May-2004  Martin Wilson        Created
	 ------------------------------------------------------------------------
	  */
	{
		// Reset the profile:
		UserProfile.resetUserProfile (a_userSession);
	}

	 /*
	  * Looks up user id of a username to ensure that duplicates are prevented.
	  *
	  *@param String a_sUsername - the username to check.
	  */
	public long getUserIdForUsername (String a_sUsername) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d*  28-Jan-2004  Chris Preager           Created
	  d1  26-Jan-2004         Martin Wilson       Modified for framework.
	 ------------------------------------------------------------------------
	  */
	{
		long lId = 0;
		Connection cCon = null;

		if (a_sUsername == null || a_sUsername.trim ().length () == 0)
		{
			throw new Bn2Exception ("Empty username passed in to UsrMngr.getUserIdForUsername");
		}

		try
		{
			// Get the user:
			cCon = m_dataSource.getConnection ();

			String sSql = "SELECT Id FROM User WHERE Username= ? ";
			PreparedStatement psql = cCon.prepareStatement (sSql);

			psql.setString (1, a_sUsername);
			ResultSet rs = psql.executeQuery ();

			// Check the password:
			if (rs.next ())
			{
				lId = rs.getLong ("Id");
			}
			psql.close();
		}
		catch (SQLException e)
		{
			try
			{
				if (cCon != null)
				{
					cCon.rollback ();
				}
			}
			catch (SQLException sqle)
			{
				//ignore.
			}

			throw new Bn2Exception ("Exception occurred in UserManager.getUserIdForUsername", e);
		}
		finally
		{
			// Return the connection to the pool:
			if (cCon != null)
			{
				try
				{
					cCon.commit ();
					cCon.close ();
				}

				catch (SQLException sqle)
				{
					m_logger.error ("SQL Exception whilst trying to close connection " + sqle.getMessage ());
				}
			}
		}

		return (lId);
	}

	 /*
	  * Attempts to change a user's password. Returns false if the old password does not match
	  * the one in the database.
	  *
	  *@param long a_lUserId - the Id of the user whose password should be changed.
	  *@param String a_sOldPassword - the old password.
	  *@param String a_sNewPassword - the new password.
	  */
	public boolean changePassword (  long a_lUserId,
												String a_sOldPassword,
												String a_sNewPassword) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   19-Jan-2004  Martin Wilson           Created
	 ------------------------------------------------------------------------
	  */
	{
		boolean bSuccess = false;
		Connection cCon = null;

		if (a_lUserId <= 0)
		{
			throw new Bn2Exception ("User id is <=0 in UserManager.changePassword");
		}

		try
		{
			// Get the user:
			cCon = m_dataSource.getConnection ();

			String sSql = "SELECT Password FROM User WHERE Id=?";
			PreparedStatement psql = cCon.prepareStatement (sSql);

			psql.setLong (1, a_lUserId);

			ResultSet rs = psql.executeQuery ();

			String sOldPasswordInDB = null;

			// Check the password:
			if (rs.next ())
			{
				sOldPasswordInDB = rs.getString ("Password");

			}
			psql.close();
			
			// Check they are the same:
			if (passwordsAreEqual (a_sOldPassword, sOldPasswordInDB))
			{
				// All ok, so update the password:
				sSql = "UPDATE User SET Password = ? WHERE Id =?";

				psql = cCon.prepareStatement ( sSql );
				psql.setString (1, encrypt (a_sNewPassword));
				psql.setLong (2, a_lUserId);

				psql.executeUpdate ();
				psql.close();
				bSuccess = true;
			}
			else
			{
				m_logger.error ("UserManager.changePassword: The password entered as the old one, does not match the password in the database.");
				throw new Bn2Exception ("The password entered as the old one, does not match the password in the database.");
			}
		}
		catch (SQLException e)
		{
			try
			{
				if (cCon != null)
				{
					cCon.rollback ();
				}
			}
			catch (SQLException sqle)
			{
				//ignore.
			}

			throw new Bn2Exception ("Exception occurred in UserManager.changePassword", e);
		}
		finally
		{
			// Return the connection to the pool:
			if (cCon != null)
			{
				try
				{
					cCon.commit ();
					cCon.close ();
				}

				catch (SQLException sqle)
				{
					m_logger.error ("SQL Exception whilst trying to close connection " + sqle.getMessage ());
				}
			}
		}

		return (bSuccess);
	}


	/*
	  * change password
	  *
	  *@param long a_lUserId - the Id of the user whose password should be changed.
	  *@param String a_sNewPassword - the new password.
	  */
	public boolean setUserPassword (  long a_lUserId,
												String a_sNewPassword) throws Bn2Exception
	 /*
	 ------------------------------------------------------------------------
	  d1   25-Jun-2004  Matt Stevenson          Created
	 ------------------------------------------------------------------------
	  */
	{
		boolean bSuccess = false;
		Connection cCon = null;

		if (a_lUserId <= 0)
		{
			throw new Bn2Exception ("User id is <=0 in UserManager.setUserPassword");
		}

		try
		{
			// Get the user:
			cCon = m_dataSource.getConnection ();

			String sSql = "UPDATE User SET Password = ? WHERE Id =?";

			PreparedStatement psql = cCon.prepareStatement ( sSql );
			psql.setString (1, encrypt (a_sNewPassword));
			psql.setLong (2, a_lUserId);

			psql.executeUpdate ();
			psql.close();
			bSuccess = true;
		}
		catch (SQLException e)
		{
			try
			{
				if (cCon != null)
				{
					cCon.rollback ();
				}
			}
			catch (SQLException sqle)
			{
				//ignore.
			}

			throw new Bn2Exception ("Exception occurred in UserManager.setUserPassword", e);
		}
		finally
		{
			// Return the connection to the pool:
			if (cCon != null)
			{
				try
				{
					cCon.commit ();
					cCon.close ();
				}

				catch (SQLException sqle)
				{
					m_logger.error ("SQL Exception whilst trying to close connection " + sqle.getMessage ());
				}
			}
		}

		return (bSuccess);
	}


	 /*
	  * Encrypts the password so that it can not be read directly from the database.
	  * NOTE: This is a VERY simple encryption algorythm.
	  *
	  *@param String a_sPassword - the password to encrypt.
	  *@return String the encrypted password.
	  */
	protected String encrypt (String a_sPassword)
	 /*
	 ------------------------------------------------------------------------
	  d*  21-Oct-2003  Martin Wilson      Added.
	  d1  26-Jan-2004  Martin Wilson      Modified for framework.
	 ------------------------------------------------------------------------
	  */
	{
		if (a_sPassword == null)
		{
			return (null);
		}

		// See if this is the password not to encrypt (we do this so that the
		// password can be reset in the database by updating the column to, e.g., 'password'.
		if (a_sPassword.equals (FrameworkSettings.getDefaultPassword()))
		{
			return (a_sPassword);
		}

		return passwordHasher.saltAndHash(a_sPassword);

	}



	protected boolean passwordsAreEqual (String providedPassword, String storedPassword)
	{
		if ((providedPassword == null) && (storedPassword == null))
		{
			return true;
		}

		if (storedPassword == null || providedPassword == null)
		{
			return false;
		}

		if (providedPassword.equals(FrameworkSettings.getDefaultPassword()) && providedPassword.equals(storedPassword))
		{
			return true;
		}

		return passwordHasher.checkPassword(storedPassword, providedPassword);
	}

	public void setDataSourceComponent (DataSourceComponent a_datasource)
	{
		m_dataSource = a_datasource;
	}
	protected DataSourceComponent getDataSourceComponent ()
	{
		return (m_dataSource);
	}
}

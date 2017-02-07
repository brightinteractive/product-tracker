package com.bright.framework.util;

import java.lang.reflect.Array;

import com.bn2web.common.service.GlobalApplication;


/**
 * Bright Interactive, image-bank
 *
 * Copyright 2005 Bright Interactive, All Rights Reserved.
 * RC4CipherUtil.java
 *
 * Contains the RC4CipherUtil class.
 */

/*
 Ver	Date				Who				Comments
 --------------------------------------------------------------------------------
 d1	19-May-2005		James Home		Created from code by R. Prince found at http://java.ittoolbox.com/
 --------------------------------------------------------------------------------
 */

/**
 * Provides RC4 two-way encryption.
 * 
 * @author Bright Interactive
 * @version d1
 */
public class RC4CipherUtil
{
	static int[] cipherBox = new int[256];
	static int[] cipherKeyArray = new int[256]; 
	static String rc4Key = "&h02 &h2d &h48 &h79 &ha3 &hc6 &hf0"; // Do not change this string
	
	// Static initialiser to build the cipher box and key array
	static
	{
		doRC4MatrixSeed(rc4Key);
	}
	
	/**
	 * Returns a string of 2 character hex numbers each of which represent an
	 * ascii character in an rc4 encrypted version of the source.
	 * 
	 * @param a_sSource
	 * @return
	 */	
	public static String encryptToHex(String a_sSource)
	/*
	---------------------------------------------------------------
	d1		19-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sCipheredText = null;
		String sResult = "";
		char asciiChar = 0;
		
		// Get the encrypted ascii string
		sCipheredText = doEncryption(a_sSource);
		
		// Convert the string to a hex representation
		for(int i=0; i<sCipheredText.length(); i++)
		{
			asciiChar = sCipheredText.charAt(i);
			sResult += Integer.toString(((byte) asciiChar & 0xff ) + 0x100, 16).substring( 1 );
		}
		
		return sResult;
	}
	
	/**
	 * Decrypts a string of 2 digit hex numbers representing ascii characters in an rc4 
	 * cipher
	 * 
	 * @param sSource
	 * @return
	 */	
	public static String decryptFromHex(String sSource)
	throws NumberFormatException
	/*
	---------------------------------------------------------------
	d1		19-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String sResult = "";
		
		if(sSource==null)
		{
			return null;
		}
		
		// Build the ascii string from the hex representation
		for(int i=0; i<sSource.length()-1; i+=2)
		{
			String sHexVal = sSource.substring(i, i+2);
			sResult += (char)Integer.parseInt(sHexVal,16);
		}
		
		// Decrypt the ascii string
		sResult = doEncryption(sResult);
		
		return sResult;
	}
	
	/**
	 * Performs encryption and decryption
	 * 
	 * @param cipherString
	 * @return
	 */	
	public static String doEncryption(String cipherString)
	/*
	---------------------------------------------------------------
	d1		19-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		String rc4Data = cipherString;
		String rc4DataCiphered = "";
		String stringReturn = "";

		try
		{
			rc4DataCiphered = doCipher(rc4Data);
			stringReturn = rc4DataCiphered;
		}
		catch(Exception e)
		{
			GlobalApplication.getInstance().getLogger().error(e);
		}

		return stringReturn;
	}

	/**
	 * doCipher will encrypt unencrypted data or decrypt encrypted data
	 * 
	 * @param unencodedText
	 * @return
	 */	
	private static String doCipher(String unencodedText)
	/*
	---------------------------------------------------------------
	d1		19-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		int z = 0;
		int t = 0;
		int i = 0;
		int cipherBy = 0;
		int tempInt = 0;
		String cipher = "";
		char cipherText;
		
		int iCipherBoxLength = Array.getLength(cipherBox);
		int[] myCipherBox = new int[iCipherBoxLength];
		
		System.arraycopy(cipherBox, 0, myCipherBox, 0, iCipherBoxLength);

		for (int a = 0; a < unencodedText.length(); a++)
		{
			i = (i + 1) % 255;
			t = (t + myCipherBox[i]) % 255;
			tempInt = myCipherBox[i];
			myCipherBox[i] = myCipherBox[t];
			myCipherBox[t] = tempInt;

			z = myCipherBox[(myCipherBox[i] + myCipherBox[t]) % 255];

			//get character at position a	
			cipherText = unencodedText.charAt(a);

			//convert to ascii value XOR'd by z
			cipherBy =  cipherText ^ z;
			//System.out.println("CipherBy=" + cipherBy);

			cipher = cipher + (char) cipherBy;
			// System.out.println("CIPHER=" + cipher.toString());
		}
		
		return cipher;
	}

	/**
	 * Setups up the cipherBox and cipherKey Arrays 
	 * 
	 * @param thisKey
	 */	
	private static void doRC4MatrixSeed(String thisKey)
	/*
	---------------------------------------------------------------
	d1		19-May-2005		James Home		Created 
	-----------------------------------------------------------------
	*/
	{
		int keyLength = 0;
		int dataSwap;
		int b;
		int asciiVal = 0;
		char asciiChar;
		keyLength = thisKey.length();

		for (int a = 0; a < 255; a++)
		{
			//take the key character at the selected position	
			asciiChar = thisKey.charAt(a % keyLength);
			asciiVal = asciiChar;
			cipherKeyArray[a] = asciiVal;
			cipherBox[a] = a;
		}

		b = 0;

		for (int a = 0; a < 255; a++)
		{
			b = (b + cipherBox[a] + cipherKeyArray[a]) % 255;
			dataSwap = cipherBox[a];
			cipherBox[a] = cipherBox[b];
			cipherBox[b] = dataSwap;
		}
	}
}


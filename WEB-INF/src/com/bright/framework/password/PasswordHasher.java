package com.bright.framework.password;

/**
 * Copyright 2009 Bright Interactive, All Rights Reserved.
 */

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;


/**
 * <p>Password hasher.</p>
 *
 * <p>Uses the SHA-256 algorithm because potential weaknesses have been
 * discovered in SHA-1 and MD5.</p>
 *
 * <p>Also uses salting and iteration to improve security.</p>
 *
 * @author Bright Interactive
 */
public class PasswordHasher
{
    private static final int SALT_BYTES = 8;
    private static final int ITERATIONS = 1000;

    private SecureRandom m_rand;

    public PasswordHasher()
    {
        try
        {
            m_rand = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            // Should never happen - SHA1PRNG is specified by
            // http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA
            throw new RuntimeException(e);
        }
    }

    public String saltAndHash(String password)
    {
        if (password == null) return null;

        byte[] salt = new byte[SALT_BYTES];
        m_rand.nextBytes(salt);

        byte[] hash = hash(salt, password);

        byte[] saltedHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltedHash, 0, salt.length);
        System.arraycopy(hash, 0, saltedHash, salt.length, hash.length);

        // Base64 is used so that password hashes are more convenient to handle (i.e. they don't contain control characters etc.)
        return base64Encode(saltedHash);
    }

    public boolean checkPassword(String saltedHashString, String a_sCandidatePassword)
    {
        byte[] saltedHash = base64Decode(saltedHashString);

        byte[] salt = new byte[SALT_BYTES];
        int hashLength = saltedHash.length - SALT_BYTES;
        if (hashLength <= 0)
        {
            throw new RuntimeException("Salted hash was too short after base64 decoding");
        }
        byte[] hash = new byte[hashLength];
        System.arraycopy(saltedHash, 0, salt, 0, salt.length);
        System.arraycopy(saltedHash, salt.length, hash, 0, hash.length);

        byte[] candidateHash = hash(salt, a_sCandidatePassword);

        return Arrays.equals(candidateHash, hash);
    }

    private byte[] hash(byte[] salt, String password)
    {
        // The SHA-256 algorithm was chosen because potential weaknesses have
        // been discovered in SHA-1.
        MessageDigest messageDigest = getSHA256MessageDigestInstance();

        messageDigest.update(salt);
        messageDigest.update(utf8BytesFromString(password));

        byte[] hash = messageDigest.digest();

        for(int i = 1; i < ITERATIONS; i++)
        {
            messageDigest.reset();
            hash = messageDigest.digest(hash);
        }
        return hash;
    }

    private static MessageDigest getSHA256MessageDigestInstance()
    {
        MessageDigest messageDigest;
        try
        {
            messageDigest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            // Should never happen because "SHA-256" is in the JCA spec
            // (http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html)
            throw new RuntimeException(e);
        }
        return messageDigest;
    }

    private byte[] utf8BytesFromString(String a_sPassword)
    {
        byte[] passwordBytes;
        try
        {
            passwordBytes = a_sPassword.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // Should never happen - UTF-8 is specified as a supported
            // encoding in the JDK Javadocs
            throw new RuntimeException(e);
        }
        return passwordBytes;
    }

    private String base64Encode(byte[] a_bytes)
    {
        // Do the encoding
        byte[] base64Bytes = Base64.encodeBase64(a_bytes);

        // Convert the result from a byte array to a String
        try
        {
            return new String(base64Bytes, "US-ASCII");
        }
        catch (UnsupportedEncodingException e)
        {
            // Should never happen - US-ASCII is specified as a supported
            // encoding in the JDK Javadocs
            throw new RuntimeException(e);
        }
    }

    private byte[] base64Decode(String base64)
    {
        // Convert the input from a String to a byte array
        byte[] base64Bytes;
        try
        {
            base64Bytes = base64.getBytes("US-ASCII");
        }
        catch (UnsupportedEncodingException e)
        {
            // Should never happen - US-ASCII is specified as a supported
            // encoding in the JDK Javadocs
            throw new RuntimeException(e);
        }

        // Do the decoding
        return Base64.decodeBase64(base64Bytes);
    }
}

package com.orendel.seam.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;

import com.orendel.seam.controllers.UsersController;
import com.orendel.seam.domain.delivery.User;
import com.orendel.seam.exceptions.ApplicationRuntimeException;
import com.orendel.seam.ui.login.LoggedUserService;


public final class AuthenticationUtil {

	private static final Logger logger = Logger.getLogger(AuthenticationUtil.class);

	
	public static boolean verifyUser(String userName, String userPassword) throws Exception {
		boolean result = false;
		
		UsersController controller = new UsersController();
		User user = controller.findUserByUsername(userName);
		if (user == null) {
			throw new Exception("No se encontró el usuario '" + userName + "'.");
		}
		
		String encodedPassword = encodePassword(userPassword);
		if (controller.verifyPassword_MD5(userName, encodedPassword)) {
//		if (encodedPassword.equals(user.getPassword())) {
			logger.info("Password aceptado");
			LoggedUserService.INSTANCE.setUser(user);
			result = true;
		} else {
			logger.info("El password suministrado no coincide...");
		}
		
		return result;
	}
	
	
	public static String encodePassword(String password) {
		logger.info("Pass PBKDF2: " + encodePassword_PBKDF2(password));
		String encodedPassword = encodePassword_MD5v2(password);
		logger.info("Pass MD5: " + encodedPassword);
		return encodedPassword;
	}
	
	
	private static String encodePassword_MD5v2(String password) {
		String passwordToHash = password;
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(passwordToHash.getBytes());
			//Get the hash's bytes
			byte[] bytes = md.digest();
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ApplicationRuntimeException("Error de autenticación: " + e.getMessage() + ".", e);
		}
		return generatedPassword;
	}
	
	
	private static String encodePassword_PBKDF2(String password) {
		int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
        byte[] hash = null;
        
        try {
        	PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        	SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        	hash = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
        	// TODO manejar exception
        	e.printStackTrace();
        } catch (InvalidKeySpecException e) {
        	// TODO manejar exception
        	e.printStackTrace();
        }
        try {
			logger.info(iterations + ":" + new String(salt, "UTF-8") + ":" + new String(hash, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}
	
	
	private static String getSalt() {
		byte[] salt = new byte[16];
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			// TODO manejar exception
			e.printStackTrace();
		}
		return salt.toString();
	}
     
	
	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

}

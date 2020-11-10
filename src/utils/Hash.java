package utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Hash {

	public static String getPasswordHashSHA512(String password) throws UnsupportedEncodingException {
		String passwordHash = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.reset();
			messageDigest.update(password.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            passwordHash = Hex.encodeHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return passwordHash;
	}
	
}

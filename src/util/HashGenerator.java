package util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

	public static String generateHashFromContent(String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] messageDigest = md.digest(content.getBytes());
			return convertBytesToHex(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-1 algorithm not found", e);
		}
	}

	public static String generateHashFromFile(String name) throws IOException {
		
				return generateHashFromContent(name);
			
	}

	private static String convertBytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}

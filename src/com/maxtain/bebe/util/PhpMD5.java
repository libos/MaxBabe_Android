package com.maxtain.bebe.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhpMD5 {
	public static String md5(byte[] bys) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(bys);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 error");
		}
		return convertHashToString(secretBytes);

	}

	@SuppressWarnings("resource")
	public static String md5(InputStream isfile) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DigestInputStream dig = null;
		try {
			dig = new DigestInputStream(isfile, md);
			byte[] buffer = new byte[1024];

			int read = dig.read(buffer);
			while (read > -1) {
				read = dig.read(buffer);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { 
				dig.close();
				isfile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return convertHashToString(dig.getMessageDigest().digest());
	}

	public static String convertHashToString(byte[] md5Bytes) {
		String md5code = new BigInteger(1, md5Bytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
}

package com.bjypt.vipcard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static final String key = "ypt.hyb_encryptSetting_beijing";
	/**
	 * 密码MD5加密方法
	 * @param password
	 * @param randomKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMd5(String password, String randomKey) throws NoSuchAlgorithmException{
		String md5 = getMd5(password);
		return getMd5(md5 + randomKey);
	}
	
	public static String getMd5(String source) throws NoSuchAlgorithmException{
		MessageDigest s = MessageDigest.getInstance("MD5");
		s.update(source.getBytes());		
		byte[] bytes = s.digest();
		return StringBytes.toHexString(bytes);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String password = "999";
		System.out.println(getMd5(password,key));
	}

	public static String string2MD5(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

}

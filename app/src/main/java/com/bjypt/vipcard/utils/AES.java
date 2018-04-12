package com.bjypt.vipcard.utils;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.log4j.Logger;

/**
 * 功能描述：AES加密和解密 
 * 创建日期：2015/10/28 
 * 修改日期：2015/10/30
 * @version V1.0
 * @author Caizhengqing
 */
public class AES {

//	private static Logger log = Logger.getLogger(AES.class);

	/*算法名*/
	private static final String algorithmName = "AES/CBC/PKCS5Padding";
	
	/*初始化向量*/
	private static final String iv16 = "www.yptzg.com@bj";
	
	/*密钥key*/
	public static final String key = "B632B014A338A6CD1626BE148582DB1C";

	/**
	 * AES解密
	 * @param content 要解密的内容
	 * @param pwdkey 密钥
	 * @return
	 */
	public static String decrypt(String content, String pwdkey) {
		try {
			byte[] input = parseHexStr2Byte(content);
			byte[] retVal = decrypt(iv16, pwdkey, input);
			return new String(retVal, "utf-8");
		} catch (Exception ex) {
//			log.error("decrypt error", ex);
		}
		return null;
	}

	/**
	 * AES加密
	 * @param content 要加密的内容
	 * @param pwdkey 密钥
	 * @return
	 */
	public static String encrypt(String content, String pwdkey) {
		try {
			byte[] retVal = encrypt(iv16, pwdkey, content.getBytes());
			return parseByte2HexStr(retVal);
		} catch (Exception ex) {
//			log.error("encrypt error", ex);
		}
		return null;
	}

	public static byte[] decrypt(String iv, String pwdkey, byte[] input) throws Exception {
		byte[] pwd = parseHexStr2Byte(pwdkey);
		SecretKeySpec key = new SecretKeySpec(pwd, "AES");
		Cipher localCipher = Cipher.getInstance(algorithmName);
		IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv.getBytes());
		localCipher.init(Cipher.DECRYPT_MODE, key, localIvParameterSpec);
		return localCipher.doFinal(input);
	}

	public static byte[] encrypt(String iv, String pwdkey, byte[] input) throws Exception {
		byte[] pwd = parseHexStr2Byte(pwdkey);
		SecretKeySpec localSecretKeySpec = new SecretKeySpec(pwd, "AES");
		Cipher localCipher = Cipher.getInstance(algorithmName);
		IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv.getBytes());
		localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
		return localCipher.doFinal(input);
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		if (buf == null || buf.length < 1)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr == null || hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		try {
			String md5sign = MD5.getMd5(sb.toString());
			String sign = AES.encrypt(md5sign, AES.key);
			return sign;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		/*try {
			System.out.println(MD5.getMd5("111", CommonFinal.RANDOMKEY));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String content = "10";
		System.out.println("加密前：" + content);
		
		try {
			/*String encrypt = AES.encrypt(content, AES.key);
			System.out.println("加密后：" + encrypt);
			*/
			String decrypt = AES.decrypt("A6CEF42D6172B77FCC4DA8CC5283ED549942CEF250A409C6B76A9868AD759D267131D816C248990AB7331FE52B753FC5", AES.key);
			System.out.println("解密后：" + decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("11971ED90AD307A1BD484D4B6B3A90A0");
		System.out.println("11971ED90AD307A1BD484D4B6B3A90A0");
	}

}

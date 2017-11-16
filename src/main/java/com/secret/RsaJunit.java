package com.secret;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RsaJunit {

	private String publicKey;
	private String privateKey;

	@Before
	public void setUp() throws Exception {
		Map<String, Object> keyMap = RSACoder.initKey();

		publicKey = RSACoder.getPublicKey(keyMap);
		privateKey = RSACoder.getPrivateKey(keyMap);
		System.out.println("公钥: \n\r" + publicKey);
		System.out.println("私钥： \n\r" + privateKey);
	}
	@Test
	public void test() throws Exception {
		System.out.println("公钥加密——私钥解密");
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
        
		String encodeString=RSACoder.encryptBASE64(encodedData);//将加密的byte 已base64加密成字符串
		
		String decodedData = RSACoder.decryptByPrivateKey(encodedData,privateKey);
        
		String outputStr = new String(decodedData);
		System.out.println("加密前: " + inputStr + "\n\r"+"base64加密后:"+encodeString+"\n\r" + "解密后: " + outputStr);
		//assertEquals(inputStr, outputStr);

	}

	@Test
	public void testSign() throws Exception {
		System.out.println("私钥加密——公钥解密");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);

		String endodeStr=RSACoder.encryptBASE64(encodedData);
		
		String decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.out.println("加密前: " + inputStr + "\n\r"+"base64加密后："+endodeStr+"\n\r" + "解密后: " + outputStr);
		//assertEquals(inputStr, outputStr);

		System.out.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = RSACoder.sign(encodedData, privateKey);
		System.out.println("签名:\r" + sign);

		// 验证签名
		boolean status = RSACoder.verify(encodedData, publicKey, sign);
		System.out.println("状态:\r" + status);
		//assertTrue(status);
	}



}

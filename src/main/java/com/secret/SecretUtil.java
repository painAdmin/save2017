package com.secret;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecretUtil {

	//管理中心授权加密秘钥
		 private static final String privatekey="GmCL+M7jQXf/7dpJRxwQHkccJZjjCSKH+luPkllMqILr4tirK+LMe0OVSqO4GuGhEaweCV24JXRYCxdr+ABCnUPHyYP3j3T/evS4SA5pLo7Nht2XSaiO7Pi2LDH1+9mME4kvLDJ3hdFFwyeng84dPg5aSxGnSKT5oAU4BaXR+xeFiHOS9jZVyslxnDwi2igi0Z3g/gKgIghubYiKZg0KZzR35hNCcby3epUkK15wKN2nJ+l+nwEVBk3Rd+rTY/a1ZVD1sLRmplL64cawHe/xsVTOhGogvb3fBDDCrSeKvWm3BQhrN19vxUOZ4yuVOBxr5dJnrix/3ynMFjocGPun7Q==";
	    /** 
	     * BASE64解密 
	     *  
	     * @param key 
	     * @return 
	     * @throws Exception 
	     */  
	    public static byte[] decryptBASE64(String key) throws Exception {  
	        return (new BASE64Decoder()).decodeBuffer(key);  
	    }  
	  
	    /** 
	     * BASE64加密 
	     *  
	     * @param key 
	     * @return 
	     * @throws Exception 
	     */  
	    public static String encryptBASE64(byte[] key) throws Exception {  
	        return (new BASE64Encoder()).encodeBuffer(key).replace("\r", "").replace("\n", "");  
	    }  
	    /**
	     * des加密
	     * @param datasource byte[]
	     * @param password String
	     * @return byte[]
	     */
	    public static  byte[] encrypt(byte[] datasource, String privatekey) {            
	        try{
	        SecureRandom random = new SecureRandom();
	        DESKeySpec desKey = new DESKeySpec(privatekey.getBytes());
	        //创建一个密匙工厂，然后用它把DESKeySpec转换成
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey securekey = keyFactory.generateSecret(desKey);
	        //Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance("DES");
	        //用密匙初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
	        //现在，获取数据并加密
	        //正式执行加密操作
	        return cipher.doFinal(datasource);
	        }catch(Throwable e){
	                e.printStackTrace();
	        }
	        return null;
	     }
	    /**
	     * des解密
	     * @param src byte[]
	     * @param password String
	     * @return byte[]
	     * @throws Exception
	     */
	    public static byte[] decrypt(byte[] src, String privatekey) throws Exception {
	            // DES算法要求有一个可信任的随机数源
	            SecureRandom random = new SecureRandom();
	            // 创建一个DESKeySpec对象
	            DESKeySpec desKey = new DESKeySpec(privatekey.getBytes());
	            // 创建一个密匙工厂
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	            // 将DESKeySpec对象转换成SecretKey对象
	            SecretKey securekey = keyFactory.generateSecret(desKey);
	            // Cipher对象实际完成解密操作
	            Cipher cipher = Cipher.getInstance("DES");
	            // 用密匙初始化Cipher对象
	            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	            // 真正开始解密操作
	            return cipher.doFinal(src);
	     } 
	public static void main(String[] args) throws Exception{
		String data="test123.com";
//		String encoderStr=encryptBASE64(encrypt(data.getBytes("UTF-8"),privatekey));
//		String decdoerStr=new String(decrypt(decryptBASE64(encoderStr),privatekey),"UTF-8");
//		System.out.println("加密前:"+data);
//		System.out.println("base64加密后:"+encoderStr);
//		System.out.println("解密后:"+decdoerStr);
		String sec=encryptBASE64("huatestTest123123.com".getBytes("UTF-8"));
		String str=new String(decryptBASE64("NzM2Njk0NTIwQ0YxODFBNg=="),"UTF-8");
	   System.out.println(str);
	}
}

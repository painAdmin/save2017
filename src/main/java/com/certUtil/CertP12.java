package com.certUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * 证书下载转为p12 文件和cer格式文件
 * @author pain
 *
 */
public class CertP12 {

	private static String keyStorePath="C:\\Users\\pain\\Desktop\\cert\\huatech.keystore";
	/**
	 * 从jks keystore中保存用户证书到p12文件
	 * @param rootCertAlias 根证书别名
	 * @param rootPassword  keystore密码
	 * @param alias   用户证书别名
	 * @param pwd     用户证书私钥密码
	 * @param path    p12文件路径
	 */
	public static void saveUserCertToP12(String rootCertAlias,String rootPassword,String alias,String pwd,String path){
		try {
			KeyStore keyStore=getKeyStore(rootPassword.toCharArray());
			KeyStore p12KeyStore=KeyStore.getInstance("PKCS12");
			//创建p12 keyStore
			p12KeyStore.load(null, null);
			
			//获取用户证书
			Certificate userCert=keyStore.getCertificate(alias);
			//获取用户证书的私钥
			PrivateKey userPrivateKey=(PrivateKey)keyStore.getKey(alias, pwd.toCharArray());
			
			p12KeyStore.setKeyEntry(alias, userPrivateKey, pwd.toCharArray(), new Certificate[]{userCert,keyStore.getCertificate(rootCertAlias)});
		   
			String p12Path=path+File.separator+"userCert_"+pwd+"_.p12";
			File file =new File(p12Path);
			FileOutputStream fos=new FileOutputStream(file);
			p12KeyStore.store(fos, pwd.toCharArray());
			fos.flush();
			fos.close();
			System.out.println("p12 文件下载成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取keystore
	 * @param keyStorePwd
	 * @return
	 * @throws Exception  密码错误
	 */
	public static synchronized KeyStore getKeyStore(char[] keyStorePwd) throws Exception{
		File file=new File(keyStorePath);
		KeyStore keyStore=null;
		keyStore=KeyStore.getInstance("jks");
		if(file.exists()){
			FileInputStream in=new FileInputStream(file);
			keyStore.load(in, keyStorePwd);
			in.close();
		}else{
			keyStore.load(null, keyStorePwd);
			FileOutputStream fos=new FileOutputStream(keyStorePath);
			keyStore.store(fos, keyStorePwd);
			fos.close();
		}
		return keyStore;
	}
	/**
	 * 生成cer 格式的用户证书
	 * @param rootCertAlias
	 * @param rootPassword
	 * @param alias
	 * @param path
	 */
	public static void saveUserCertToCer(String rootCertAlias,String rootPassword,String alias,String path){
		try {
			KeyStore keyStore=getKeyStore(rootPassword.toCharArray());
			Certificate userCert=keyStore.getCertificate(alias);
			String cerPath=path+File.separator+"user.cer";
			FileOutputStream fos=new FileOutputStream(cerPath);
			fos.write(userCert.getEncoded());
			fos.flush();
			fos.close();
			System.out.println("生成cer证书成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		String path="C:\\Users\\pain\\Desktop\\cert";
		String rootCertAlias="testHuatech";
		String rootpass="123123.com";
		
		String userAlias="userAlias";
		String pwd="admin_123!";
		// 下载p12 证书
		//saveUserCertToP12(rootCertAlias,rootpass,userAlias,pwd,path);
		// 下载cer 证书
		//saveUserCertToCer(rootCertAlias,rootpass,userAlias,path);
	}
}














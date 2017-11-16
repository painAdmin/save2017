package com.certUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
/**
 * 管理中心创建用户证书 ，根证书
 * @author pain
 * !! keyStore.store(io,passWord)  每次存储都会更新keystore 秘钥库的密码
 * !! keyStore.setKeyEntry(..,..,password,..) 密码是用来保护私钥的
 */
public class CertUtils {
    private static final String keyStorePath="C:\\Users\\pain\\Desktop\\cert\\huatech.keystore";
    /**
     * 创建根证书
     * @param alias
     * @param name
     * @param password
     */
	public static void  createRootCert(String alias,String name,String password){
	     KeyPairGenerator kpg=null;
	    try {
	    	//获取秘钥对
			kpg=KeyPairGenerator.getInstance("RSA");
			SecureRandom random=null;
			if(System.getProperty("os.name").toLowerCase().startsWith("win")){
				random=SecureRandom.getInstance("SHA1PRNG");
			}else{
				random=SecureRandom.getInstance("NativePRNGNonBlocking");
			}
			kpg.initialize(2048,random);
			KeyPair keyPair=kpg.generateKeyPair();
			
			//设置证书subject
			X509CertInfo info=new X509CertInfo();
			info.set(X509CertInfo.VALIDITY, getValidityDate(null));//设置有效期
			info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new BigInteger(64,new SecureRandom())));
			
			String issue="CN="+alias+",OU="+name;
			X500Name rootIssue=new X500Name(issue);
			info.set(X509CertInfo.SUBJECT, rootIssue);
			info.set(X509CertInfo.ISSUER, rootIssue);
			info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
			info.set(X509CertInfo.VERSION,new CertificateVersion(CertificateVersion.V3));
			AlgorithmId algo=new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);
			info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));
			
			//获取keyStore
			KeyStore keyStore=getKeyStore(password.toCharArray());
			
			CertificateExtensions exts=new CertificateExtensions();
			BasicConstraintsExtension bce=new BasicConstraintsExtension(true,-1);
			exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false,bce.getExtensionValue()));
			exts.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(new KeyIdentifier(keyPair.getPublic()).getIdentifier()));
			info.set(X509CertInfo.EXTENSIONS, exts);
			
			X509CertImpl  outCert=new X509CertImpl(info);
			
			outCert.sign(keyPair.getPrivate(), "SHA256WithRSA");
			if(keyStore!=null){
				keyStore.setKeyEntry(alias, keyPair.getPrivate(), password.toCharArray(), new Certificate[]{outCert});//Certificate 证书链
				FileOutputStream fos=new FileOutputStream(keyStorePath);
				keyStore.store(fos, password.toCharArray());
				fos.close();
			}
			System.out.println("根证书创建成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create error");
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
	 * 获取证书的有效期
	 * @param date
	 * @return
	 */
	public static CertificateValidity getValidityDate(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.YEAR, 0);
		Date from =calendar.getTime();
		
		Calendar calendarTo=Calendar.getInstance();
		calendarTo.add(Calendar.YEAR, 100);
		Date to=calendarTo.getTime();
		if(date!=null){
			to=date;
		}
		CertificateValidity validDate=new CertificateValidity(from,to);
		return validDate;
	}
	//-----------------------------------------------------------------------------------------------------------------------
	/**
	 * 创建用户证书，并签名，保存到根证书的keystore中
	 * @param rootCertAlias 根证书别名
	 * @param rootpass      根证书密码
	 * @param rootCertName  根证书名
	 * @param alias         用户证书别名
	 * @param name          用户证书名称
	 * @param password      用户证书密码
	 * @param email         用户中证书邮箱
	 * @param date          用户证书有效期
	 */
	public static void CreateUserCertSign(String rootCertAlias,String rootpass,String rootCertName,String alias,String name,String password,String email,Date date){
		if(alias.equals(rootCertAlias)){
			System.out.println("keystore 中是用alias别名来区分各个证书的，名字相同则覆盖证书");
			return ;
		}
		KeyPairGenerator kpg=null;
		SecureRandom random=null;
		try {
			kpg=KeyPairGenerator.getInstance("RSA");
			if(System.getProperty("os.name").toLowerCase().startsWith("win")){//能减少系统运算时间防止防止卡死
				random=SecureRandom.getInstance("SHA1PRNG");
			}else{
				random=SecureRandom.getInstance("NativePRNGNonBlocking");
			}
			kpg.initialize(2048, random);
			KeyPair keyPair=kpg.generateKeyPair();
			
			//设置证书
			X509CertInfo info=new X509CertInfo();
			//设置证书subject
			String issue="CN="+name+",OU="+rootCertName+",Email="+email;
			X500Name owner=new X500Name(issue);
			info.set(X509CertInfo.VALIDITY, getValidityDate(date));
			info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new BigInteger(64,random)));
			info.set(X509CertInfo.SUBJECT, owner);
			
			info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
			info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
			// 设置签名算法
			AlgorithmId algo=new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);
			info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));
			
			// 获取keystore
			KeyStore keyStore=getKeyStore(rootpass.toCharArray());
			
			// 获取根证书
			X509CertImpl rootCert=(X509CertImpl)keyStore.getCertificate(rootCertAlias);
			//获取keyStore中根证书对应实体中的 私钥
			PrivateKey pKey=(PrivateKey)keyStore.getKey(rootCertAlias, rootpass.toCharArray());
			
			//设置证书发行者 既 根证书
			info.set(X509CertInfo.ISSUER, rootCert.getSubjectDN());
			
			//设置证书扩展
			CertificateExtensions exts=new CertificateExtensions();
			BasicConstraintsExtension bce=new BasicConstraintsExtension(true,-1);
			exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false,bce.getExtensionValue()));
			exts.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(new KeyIdentifier(keyPair.getPublic()).getIdentifier()));
			exts.set(AuthorityKeyIdentifierExtension.NAME, new AuthorityKeyIdentifierExtension(new KeyIdentifier(rootCert.getPublicKey()),null,null));
			info.set(X509CertInfo.EXTENSIONS, exts); 
			
			X509CertImpl outCert=new X509CertImpl(info);
			//用根证书 为子证书签名
			outCert.sign(pKey, "SHA256WithRSA");
			if(keyStore!=null){
				keyStore.setKeyEntry(alias, keyPair.getPrivate(), password.toCharArray(), new Certificate[]{outCert,rootCert});
				FileOutputStream fos=new FileOutputStream(keyStorePath);
				keyStore.store(fos, rootpass.toCharArray());
				fos.close();
			}
			System.out.println("用户证书创建并签名成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建用户证书失败");
		}
	}
	public static void main(String[] args){
		String rootCertAlias="testHuatech";
		String rootCertName="huatech";
		String rootpass="123123.com";
		//创建根证书
		//createRootCert(rootCertAlias,rootCertName,rootpass);
		
		String alias="userAlias";
		String name="userName";
		String password="admin_123!";
		String email="1767123925@qq.com";
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.YEAR, 50);
		Date date=calendar.getTime();
		//创建用户证书
		CreateUserCertSign(rootCertAlias,rootpass,rootCertName,alias,name,password,email,date);
		
	}
}

























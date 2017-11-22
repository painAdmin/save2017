package com.certUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.CRL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.secret.SecretUtil;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;



public class CrlTest {

	
	public static void main(String[] args) throws Exception {
		
	
		String certFile="C:\\Users\\pain\\Desktop\\cert\\user.cer";
		// 解析cer证书信息
		//readcerCert(certFile);
		
		String remoteFile="http://down.qingkan9.com/1/1070.txt";
		String localFile="C:\\Users\\pain\\Desktop\\舞动乾坤.txt";
		//下载url远程文件
		//downloadFile(remoteFile,localFile);
		String path="C:\\Users\\pain\\Desktop\\certDis.crl";
		//解析crl文件
		//parseCrl(path);
		
		//如果有密码 Smb://username:password@ip/sharefolder  || smb://chb:123456@192.168.0.1/test
		String smbRemote="smb://pain:123.com@192.168.128.130/file://WIN-3LBGO979C1F/CertEnroll/WIN-3LBGO979C1F-CA.crl";
		String localSmb="C:\\Users\\pain\\Desktop\\WIN-3LBGO979C1F-CA.crl";
		//下载共享文件 测试失败
		//getSMBFile(smbRemote,localSmb);
		String remotePath="\\\\WIN-3LBGO979C1F\\CertEnroll\\WIN-3LBGO979C1F-CA.crl";
		String localPath="C:\\Users\\pain\\Desktop\\WIN-3LBGO979C1F-CA.crl";
		// 下载共享文件
		getShareFile(remotePath,localPath);

	}
	/**
	 * 解析crl文件 获取证书吊销列表
	 * @param path
	 */
	public static void parseCrl(String path){
		try {
			FileInputStream fis=new FileInputStream(path);
			CertificateFactory factory=CertificateFactory.getInstance("X509");
			X509CRL crl=(X509CRL)factory.generateCRL(fis);
			//boolean flag=crl.isRevoked(cert);检查证书是否在吊销列表中
			int i=0;
			Set set=crl.getRevokedCertificates();
			Iterator  iterator=set.iterator();
			while(iterator.hasNext()){
				X509CRLEntry entry=(X509CRLEntry)iterator.next();
				String serial=entry.getSerialNumber().toString(16).toUpperCase();
				String issName=crl.getIssuerDN().toString();
				String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entry.getRevocationDate());
				System.out.println("serial:"+serial);
				System.out.println("issName:"+issName);
				System.out.println("time:"+time);
				System.out.println("*********************************************");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取证书信息列表 cer 
	 * @param certPath
	 */
	public static void readcerCert(String certPath){
		X509Certificate cert=null;
		try {
			CertificateFactory factory=CertificateFactory.getInstance("X509");
			FileInputStream fis=new FileInputStream(certPath);
			cert=(X509Certificate)factory.generateCertificate(fis);
			fis.close();
			
			System.out.println("读取证书信息。。。。。。。。。");
			System.out.println("序列号:"+cert.getSerialNumber().toString().toLowerCase());
			System.out.println("issueDN:"+cert.getIssuerDN());
			System.out.println("subjectDN"+cert.getSubjectDN());
			System.out.println("证书算法OIDsignAllgoID:"+cert.getSigAlgOID());
			System.out.println("有效期："+cert.getNotAfter());
			System.out.println("签名算法"+cert.getSigAlgName());
			System.out.println("版本号："+cert.getVersion());
			System.out.println("publicKey:"+cert.getPublicKey());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 通过url下载远程文件
	 * @param fileUrl
	 * @param fileLocal
	 * @throws Exception
	 */
	 public static void downloadFile(String fileUrl,String fileLocal) throws Exception {
	        URL url = new URL(fileUrl);
	        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
	        //第一种添加用户名授权方式
//	        Authenticator.setDefault(new Authenticator(){   
//
//				@Override
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication("pain","123.com".toCharArray());
//				}
//	        	
//	        });
	        //第二种添加用户名授权方式
//	        String author=SecretUtil.encryptBASE64("pain:123.com".getBytes());
//	        urlCon.setRequestProperty("Authorization", "Basic"+author);
	        urlCon.setConnectTimeout(6000);
	        urlCon.setReadTimeout(6000);
	        int code = urlCon.getResponseCode();
	        if (code != HttpURLConnection.HTTP_OK) {
	            throw new Exception("文件读取失败");
	        }
	       
	        //读文件流
	        DataInputStream in = new DataInputStream(urlCon.getInputStream());
	        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
	        byte[] buffer = new byte[2048];
	        int count = 0;
	        while ((count = in.read(buffer)) > 0) {
	            out.write(buffer, 0, count);
	        }
	        out.close();
	        in.close();
	        System.out.println("下载完成！");
	    }
	/** 测试失败
	 * 从共享目录拷贝文件到本地     path: smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx 　|| "smb://szpcg;jiang.t:xxx@192.168.193.13/Jake/test.txt"
	 * @param remoteFile  如果无密码 则url=smb://ip/sharefolder || smb://192.168.0.77/test  ;  如果有密码 Smb://username:password@ip/sharefolder  || smb://chb:123456@192.168.0.1/test
	 * @param localFile
	 */
   public static void getSMBFile(String remotePath,String localPath){
	    try {
	    	NtlmPasswordAuthentication auth=new NtlmPasswordAuthentication("","pain","123.com");
		    SmbFile remoteFile=new SmbFile(remotePath,auth);
		    if(remotePath==null || remotePath.isEmpty()){
		    	return;
		    }
		    String fileName=remoteFile.getName();//获取文件名
		    File localFile=new File(localPath);
		    
		    InputStream in=new BufferedInputStream(new SmbFileInputStream(remoteFile));
		    OutputStream out=new BufferedOutputStream(new FileOutputStream(localFile));
		    
		    byte[] buf=new byte[1024];
		    int len=0;
		    while((len=in.read(buf))>0){
		    	out.write(buf, 0, len);
		    	buf=new byte[1024];
		    }
		    in.close();
		    out.close();
		    System.out.println("从共享下载文件完成！");
		} catch (Exception e) {
	       e.printStackTrace();
		}
   }
   /** 测试失败！
    * 将本地文件拷贝到共享目录   
    * @param remotePath
    * @param localPath
    */
   public static void printSMBFile(String remotePath,String localPath){
	   try{
		   remotePath="smb:"+remotePath;
		   SmbFile remoteFile=new SmbFile(remotePath);
		   File localFile=new File(localPath);
		   
		   InputStream in=new BufferedInputStream(new FileInputStream(localFile));
		   OutputStream out=new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
		   byte[] buf=new byte[1024];
		   int len=0;
		   while((len=in.read(buf))>0){
			   out.write(buf, 0, len);
			   buf=new byte[1024];
		   }
		   in.close();
		   out.close();
		   System.out.println("从本地上传文件到共享完成");
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   /**
    * 从共享目录下载文件到本地，
    * @param remotePath="\\\\WIN-3LBGO979C1F\\CertEnroll\\WIN-3LBGO979C1F-CA.crl";
    * @param localPath
    */
   public static void getShareFile(String remotePath,String localPath){
	   try {
		   File remoteFile=new File(remotePath);
		   File localFile=new File(localPath);
	  
		   BufferedInputStream bis=new BufferedInputStream(new FileInputStream(remoteFile));
		   BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(localPath));
		   byte[] buf=new byte[1024];
		   int len=0;
		   if((len=bis.read(buf))>0){
			   bos.write(buf,0,len);
			   buf=new byte[1024];
		   }
		   bos.flush();
		   bis.close();
		   bos.close();
		   System.out.println("下载完成");
	   } catch (Exception e) {
		e.printStackTrace();
	   }
   }
 
}






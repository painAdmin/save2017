package com.certUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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



public class CrlTest {

	
	public static void main(String[] args) throws Exception {
		String path="C:\\Users\\pain\\Desktop\\certDis.crl";
		String remoteFile="http://down.qingkan9.com/1/1070.txt";
		String localFile="C:\\Users\\pain\\Desktop\\舞动乾坤.txt";
		String certFile="C:\\Users\\pain\\Desktop\\cert\\user.cer";
		readcerCert(certFile);
		//downloadFile(remoteFile,localFile);
		//parseCrl(path);

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
			System.out.println("证书算法OIDsignAllgoID"+cert.getSigAlgOID());
			System.out.println("有效期："+cert.getNotAfter());
			System.out.println("签名算法"+cert.getSigAlgName());
			System.out.println("版本号："+cert.getVersion());
			System.out.println("publicKey:"+cert.getPublicKey());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 通过url下载远程问价
	 * @param fileUrl
	 * @param fileLocal
	 * @throws Exception
	 */
	 public static void downloadFile(String fileUrl,String fileLocal) throws Exception {
	        URL url = new URL(fileUrl);
	        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
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
	
   

}






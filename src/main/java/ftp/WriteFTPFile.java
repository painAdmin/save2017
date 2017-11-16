package ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

public class WriteFTPFile {

	private Logger logger=Logger.getLogger(WriteFTPFile.class);
	/**
	 * 
	 * @param ftpHost
	 * @param port
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param filePath 文件路径
	 * @param fileContent
	 * @param writeTempFilePath
	 */
	public void upload(String ftpHost,int port,String ftpUserName,String ftpPassword,String filePath,String fileContent,String writeTempFilePath){
	      FTPClient ftpClient=null;
	      logger.info("开始上传文件！");
	      try{
	    	  ftpClient=FTPUtil.getFTPClient(ftpHost, port, ftpUserName, ftpPassword);
	    	  // 设置passiveMode传输
	    	  ftpClient.enterLocalPassiveMode();
	    	  ftpClient.setControlEncoding("UTF-8");
	    	  //设置文件传输方式 以二进制流的方式
	    	  ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	    	  //对远程目录的处理
	    	  String remoteFileName=filePath;
	    	  if(filePath.contains("\\")){
	    		  remoteFileName=filePath.substring(filePath.lastIndexOf("\\")+1);
	    	  }
	    	  // 先把文件写在本地，然后上传ftp成功后再删除
	    	  boolean writeResult=true;
	    	  if(!"1".equals(fileContent)){
	    		  writeResult=write(remoteFileName,fileContent,writeTempFilePath);//仅支持 字符串格式数据
	    	  }else{
	    		  writeResult=writeImage(remoteFileName,fileContent,writeTempFilePath,filePath);// 支持其他类型文件
	    	  }
	    			  
	    	  if(writeResult){
	    		  File f=new File(writeTempFilePath+"\\"+remoteFileName);
	    		  InputStream in=new FileInputStream(f);
	    		  boolean flag=ftpClient.storeFile(new String(remoteFileName.getBytes("GBK"),"iso-8859-1"), in);//文件名在转码解决传输中文名或者中文路径问题
	    		  if(flag){
	    			  logger.info("文件上传成功！");
	    		  }else{
	    			  logger.info("文件上传失败！");
	    		  }
	    		  in.close();
	    		  f.delete();
	    		  
	    	  }else{
	    		  logger.error("文件到temp失败！");
	    	  }
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  
	      }finally{
	    	  try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
	      }  
	}
	public boolean writeImage(String remoteFileName,String fileContent,String writeTempFilePath,String localFilePath){
		if(!"1".equals(fileContent)){
			return false;
		}
		File localFile=new File(localFilePath);
		File remoteFile=new File(writeTempFilePath+"\\"+remoteFileName);
		if(!localFile.exists()){
			return false;
		}
		if(remoteFile.exists()){
			return true;
		}
		try {
			FileInputStream fis=new FileInputStream(localFile);
			byte[] buf=new byte[10*1024];
			FileOutputStream fos=new FileOutputStream(remoteFile,true);
			int len=0;
			while((len=fis.read(buf))!=-1){
				fos.write(buf, 0, len);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public boolean write(String remoteFileName,String fileContent,String writeTempFilePath){
		if("1".equals(fileContent)){//支持其他文件上传
			return true;
		}
		logger.info("开始写配置文件");
		File file=new File(writeTempFilePath+"\\"+remoteFileName);
		if(file.exists()){
			return true;
		}
		PrintWriter pw=null;
		try {
			file.createNewFile();
			FileOutputStream fos=new FileOutputStream(file);
			 pw=new PrintWriter(new OutputStreamWriter(fos,"UTF-8"));
			pw.println(fileContent.replaceAll("\n", "\r\n"));
			pw.flush();
		    pw.close();
		    return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void main(String[] args){
		String remoteFileName="test03.jpg";
		String fileContent="1";
		String writeTempFilePath="F:\\";
		String localFilePath="C:\\Users\\pain\\Desktop\\test03.jpg";
		boolean flag=new WriteFTPFile().writeImage(remoteFileName, fileContent, writeTempFilePath, localFilePath);
		System.out.println(flag);
	}
}






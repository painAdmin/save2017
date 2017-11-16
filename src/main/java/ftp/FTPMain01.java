package ftp;

import org.apache.log4j.Logger;

public class FTPMain01 {

	public static void main(String[] args) {
		ReadFTPFile readFTPFile=new ReadFTPFile();
		String ftpHost="192.168.32.119";
		int port=21;
		String ftpUserName="test";
		String ftpPassword="test";
		String filePath="C:\\Users\\pain\\Desktop\\Python编程：从入门到实践.pdf";
		String fileName="1";
	//	String result=readFTPFile.readConfigFileForFTP(ftpHost,port,ftpUserName,ftpPassword,filePath,fileName);
	//	System.out.println("FTP服务器文件读取内容："+result);
		
		WriteFTPFile writeFTPFile=new WriteFTPFile();
		String fileContent="1";
		String writeTempFilePath="F:\\";
		writeFTPFile.upload(ftpHost, port, ftpUserName, ftpPassword, filePath, fileContent, writeTempFilePath);
	}
}

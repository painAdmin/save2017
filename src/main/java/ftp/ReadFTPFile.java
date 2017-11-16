package ftp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

public class ReadFTPFile {

	private Logger logger=Logger.getLogger(ReadFTPFile.class);
	/**
	 * 
	 * @param ftpHost
	 * @param port
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param filePath 文件路径
	 * @param fileName  文件名称
	 * @return
	 */
	public String readConfigFileForFTP(String ftpHost,int port,String ftpUserName,String ftpPassword,String filePath,String fileName){
		StringBuffer resultBuffer=new StringBuffer();
		FileInputStream inFile=null;
		InputStream in=null;
		FTPClient ftpClient=null;
		logger.info("开始读取绝对路径"+filePath+"文件");
		try {
			ftpClient=FTPUtil.getFTPClient(ftpHost, port, ftpUserName, ftpPassword);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			// 更改 工作目录
		    ftpClient.changeWorkingDirectory(filePath);
		    in=ftpClient.retrieveFileStream(fileName);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		}catch (FileNotFoundException e){
			e.printStackTrace();
			logger.info("没找到"+filePath+"文件");
			return "下载配置文件失败！";
		}catch (SocketException e){
			e.printStackTrace();
			logger.info("连接失败！");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("文件读取错误！");
			return "配置文件读取失败！";
		}
		if(in!=null){
			try {
				BufferedReader br=new BufferedReader(new InputStreamReader(in,"UTF-8"));
				String data=null;
		
				while((data=br.readLine())!=null){
					resultBuffer.append(data+";");
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("转换字符失败！");
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("文件读取失败！");
				e.printStackTrace();
				return "配置文件读取失败！";
			}finally{
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}else{
			logger.info("in 为空，不能读取");
			return "配置文件读取失败";
		}
		return resultBuffer.toString();
	}
}

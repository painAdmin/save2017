package ftp;

import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FTPUtil {

	private static Logger logger=Logger.getLogger(FTPUtil.class);
	/**
	 * 
	 * @param ftpHost 地址
	 * @param port 端口
	 * @param ftpPassword 密码
	 * @param ftpUserName 用户名
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, int port,String ftpUserName,String ftpPassword){
		FTPClient ftpClient=null;
		try{
			ftpClient=new FTPClient();
			ftpClient.connect(ftpHost, port);//连接ftp服务器
			ftpClient.login(ftpUserName, ftpPassword);
			int code=ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(code)){
				logger.info("未连接到ftp用户，用户名或密码错误");
			}else{
				logger.info("ftp连接成功！");
			}
		}catch(SocketException e){
			e.printStackTrace();
			logger.info("FTP ip 地址错误");
		}catch(Exception e1){
			e1.printStackTrace();
			logger.info("FTP 端口错误请重新输入");
		}
		return ftpClient;
	}
}

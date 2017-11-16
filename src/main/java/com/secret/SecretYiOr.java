package com.secret;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SecretYiOr {

	private static final byte[] Key="cegshimni1232#$$%^^^^^##@@#@LKJKJKJFfdsf4564564".getBytes();
	/**
	 * 利用异或加密字符串
	 * @param str
	 * @return
	 */
	public static String encodeString(String str){
		byte[] strAry=str.getBytes();
		int keyLen=Key.length;
		int pos=0;
		for(int i=0;i<strAry.length;i++){
			strAry[i]^=Key[pos];
			if(pos<keyLen){
				pos++;
			}else{
				pos=0;
			}
			
		}
		try {
			return SecretUtil.encryptBASE64(strAry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 利用异或解密字符串
	 * @param str
	 * @return
	 */
	public static String decodeString(String str){
		try {
			byte[] encodeAry = SecretUtil.decryptBASE64(str);
			int keyLen=Key.length;
			int pos=0;
			for(int i=0;i<encodeAry.length;i++){
				encodeAry[i]^=Key[pos];
				if(pos<keyLen){
					pos++;
				}else{
					pos=0;
				}
			}
			return new String(encodeAry,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 利用异或加密文件
	 * @param oldFile
	 * @param newFile
	 * @return
	 */
	 public static boolean decryptFile(String oldFile, String newFile) {
			boolean decryptMark = true;
			File sourceFile = new File(oldFile);
			if (sourceFile.exists() && sourceFile.length() > 0L) {
				FileInputStream fis = null;
				FileOutputStream fos = null;

				try {
					fis = new FileInputStream(oldFile);
					File e = new File(newFile);
					if (!e.exists()) {
						e.createNewFile();
					}

					fos = new FileOutputStream(e);
					int pos = 0;
					int keylen = Key.length;
					byte[] buffer = new byte[1024];

					int c;
					while ((c = fis.read(buffer)) != -1) {
						for (int i = 0; i < c; ++i) {
							buffer[i] ^= Key[pos];
							fos.write(buffer[i]);
							++pos;
							if (pos == keylen) {
								pos = 0;
							}
						}
					}
				} catch (Exception arg14) {
					//log.error("文件(" + oldFile + ")解密过程异常", arg14, new Object[0]);
					decryptMark = false;
				} finally {
					close((InputStream) fis);
					close((OutputStream) fos);
				}
			} else {
				//log.info("文件解密过程出错,文件{}不存在", new Object[]{oldFile});
				decryptMark = false;
			}

			return decryptMark;
		}
	 /**
	  * 利用异或解密文件
	  * @param oldFile
	  * @param newFile
	  * @return
	  */
		public static boolean encryptFile(String oldFile, String newFile) {
			boolean encryptMark = true;
			File sourceFile = new File(oldFile);
			if (sourceFile.exists() && sourceFile.length() > 0L) {
				FileInputStream fis = null;
				FileOutputStream fos = null;

				try {
					File e = new File(newFile);
					if (!e.exists()) {
						e.createNewFile();
					}

					fis = new FileInputStream(sourceFile);
					fos = new FileOutputStream(e);
					int pos = 0;
					int keylen = Key.length;
					byte[] buffer = new byte[1024];

					int c;
					while ((c = fis.read(buffer)) != -1) {
						for (int i = 0; i < c; ++i) {
							buffer[i] ^= Key[pos];
							fos.write(buffer[i]);
							++pos;
							if (pos == keylen) {
								pos = 0;
							}
						}
					}
				} catch (Exception arg14) {
					//log.error("文件(" + oldFile + ")加密过程异常", arg14, new Object[0]);
					encryptMark = false;
				} finally {
					close((InputStream) fis);
					close((OutputStream) fos);
				}
			} else {
				//log.info("文件加密过程出错,文件{}不存在", new Object[]{oldFile});
				encryptMark = false;
			}

			return encryptMark;
		}
		 public static final void close(InputStream is) {
				if (is != null) {
					try {
						is.close();
					} catch (Exception arg1) {
						;
					}
				}

			}
			public static final void close(OutputStream os) {
				if (os != null) {
					try {
						os.close();
					} catch (Exception arg1) {
						;
					}
				}

			}
    public static void main(String[] args) {
//       String src="ceshiadmin_123!中国";
//       String encodeStr=encodeString(src);
//       String decodeStr=decodeString(encodeStr);
//       System.out.println("加密后："+encodeStr);
//       System.out.println("解密后："+decodeStr);
    	String oldFile="C:\\Users\\pain\\Desktop\\udisk.sql";
		String newFile="C:\\Users\\pain\\Desktop\\udiskbat.sql";
		//encryptFile(oldFile,newFile);
		//decryptFile(newFile,oldFile);
	}
}

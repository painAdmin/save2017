package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IO {

	public static void main(String[] args) throws Exception{
		readIndexFile(14L);
		//readIndexChannelFile(14L);  
	}
	/**
	 * channel 读取文件
	 * @param index
	 * @throws Exception
	 */
	public static void readIndexChannelFile(Long index) throws Exception{
		String fileStr=IO.class.getClassLoader().getSystemResource("mongo.properties").getPath();
		File file=new File(fileStr);
		FileInputStream fis=new FileInputStream(file);
		FileChannel fileChannel=fis.getChannel();
	    fileChannel.position(index);
	    ByteBuffer buffer=ByteBuffer.allocate(1024*20);
	    buffer.clear();
	    fileChannel.read(buffer);
	    buffer.flip();
	    byte[] str=new byte[1024*20];
	    int count=0;
	    while(buffer.hasRemaining()){
	    	int len=buffer.limit()-buffer.position();
	    	 byte[] strAry=null;
	    	if(len>=18){
	    		strAry=new byte[18];
	    	}else{
	    		strAry=new byte[len];
	    	}
	        buffer.get(strAry);
	 	    System.arraycopy(strAry, 0, str, count*18, strAry.length);
	 	    count++;
	    }
	    String info=new String(str,"UTF-8");
	    System.out.println(info);
	   
	    
	}
	/**
	 * RandomAccessFile  按index的读写文件
	 * @param index
	 * @throws Exception
	 */
	public static void readIndexFile(Long index) throws Exception{
		String fileStr=IO.class.getClassLoader().getResource("mongo.properties").getPath();
		RandomAccessFile raf=new RandomAccessFile(fileStr,"rw");
		raf.seek(index);
//		byte[] buffer=new byte[18];
//		byte[] str=new byte[1024*20];
//		int len=0;
//		int count=0;
//		while((len=raf.read(buffer))>0){
//			System.out.println(raf.getFilePointer());
//			System.arraycopy(buffer, 0, str, count*18, len);
//			count++;
//		}
//		String info=new String(str,"UTF-8");
//		System.out.println(info);
		String buf="";
		while((buf=raf.readLine())!=null){
			System.out.println(buf);
		}
		System.out.println(raf.getFilePointer());
		raf.close();
	}
	/**
	 * 普通流 读取配置文件
	 * @throws Exception
	 */
	public static void readFile() throws Exception{
		 String fileStr=IO.class.getClassLoader().getResource("mongo.properties").getPath();
		  File file=new File(fileStr);
		  FileInputStream fis=new FileInputStream(file);
		  BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
		  String buf="";
		  while((buf=br.readLine())!=null){
			  System.out.println(buf);
		  }
		  br.close();
	}
}

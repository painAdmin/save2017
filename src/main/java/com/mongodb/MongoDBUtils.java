package com.mongodb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
/**
 * 创建用户时必须在当前数据库下创建 
 * show users；查看当前数据库用户列表
 * @author pain
 *
 */
public class MongoDBUtils {

	public static MongoClient mongoClient=getMongoClient();
	public static final String dataBase="runoob";
	public static void closeMogoClient(){
		mongoClient.close();
	}
	public static MongoClient getMongoClient(){
		if(mongoClient!=null){
			return mongoClient;
		}
		Properties p=new Properties();
		try {
			p.load(MongoDBUtils.class.getResourceAsStream("/mongo.properties"));
			String ip=p.getProperty("ip");
			String port=p.getProperty("port");
			String token=p.getProperty("token");
			String user=p.getProperty("user");
			String password=p.getProperty("password");
		
			if(token.equals("false")){
				mongoClient =new MongoClient(ip,Integer.parseInt(port));
			}else{
				List<ServerAddress> addr=new ArrayList<ServerAddress>();
				ServerAddress serverAddress=new ServerAddress(ip,Integer.parseInt(port));
				addr.add(serverAddress);
				MongoCredential credential=MongoCredential.createScramSha1Credential(user, dataBase, password.toCharArray());
				List<MongoCredential> mongoCredential=new ArrayList<MongoCredential>();
				mongoCredential.add(credential);
				mongoClient=new MongoClient(addr,mongoCredential);
				
				
			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mongoClient;
	}
	public static MongoDatabase getMongoDatabase(String databaseName){
		 return getMongoClient().getDatabase(databaseName);
	}
	public static MongoCollection<Document> getMongoCollection(String collectionName){
		return getMongoDatabase(dataBase).getCollection(collectionName);
	}
	public static void main(String[] args){
		MongoCollection<Document> collection=getMongoCollection("col");
		FindIterable<Document> findIterable=collection.find();
		MongoCursor mongoCursor=findIterable.iterator();
		while(mongoCursor.hasNext()){
			System.out.println(mongoCursor.next());
		}
	}
}










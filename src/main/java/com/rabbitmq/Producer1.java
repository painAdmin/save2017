package com.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * Direct Exchange的消息生产/消费
 * @author pain
 *
 */
public class Producer1 {

	private final static String queue_name="test_queue";
	
	public static void main(String[] args){
		//创建连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		//设置rabbitmq 相关信息
		factory.setHost("localhost");
		factory.setUsername("rabbitmq_producer");
		factory.setPassword("123.com");
		factory.setPort(5672);
		factory.setVirtualHost("test_vhosts");
		//创建与rabitmq 服务器的tcp连接
		try {
			Connection conn=factory.newConnection();
			//创建一个通道
			Channel channel=conn.createChannel();
			//添加directChange
			channel.exchangeDeclare("directExchange", "direct");
			//声明一个队列
			channel.queueDeclare(queue_name, true, false, false, null);
			channel.queueBind(queue_name, "directExchange", "directMessage");
			String message="Hello rabbitmq13";
			//发送到队列中
			channel.basicPublish("directExchange", "directMessage", null, message.getBytes("UTF-8"));
			System.out.println("producer send message:"+message);
			
			//关闭通道和链接
			channel.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

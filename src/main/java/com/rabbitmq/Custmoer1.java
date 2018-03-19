package com.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
/**
 * Direct Exchange的消息生产/消费
 * @author pain
 *
 */
public class Custmoer1 {

	private static final String queue_name="test_queue";
	public static void main(String[] args){
		
		//创建工厂
		ConnectionFactory factory=new ConnectionFactory();
		
		//设置rabbitmq地址
		factory.setHost("localhost");
		factory.setUsername("rabbitmq_consumer");
		factory.setPassword("123.com");
		factory.setVirtualHost("test_vhosts");
		factory.setPort(5672);
		try {
			//创建一个新的连接
			Connection conn=factory.newConnection();
			//创建一个通道
			Channel  channel=conn.createChannel();
			//声明要关注的队列
			channel.queueDeclare(queue_name, true, false, false, null);
			System.out.println("start custmoer receive message::::::::::");
			Consumer consumer=new DefaultConsumer(channel){

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String message=new String(body,"UTF-8");
					System.out.println("custmoer receive message:"+message);
					
				}
				
			};
			// 自动回复队列应答，  rabbitmq 中的消息确认机制
			channel.basicConsume(queue_name, true,consumer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}

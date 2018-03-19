package com.rabbitmq;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
/**
 * RPC方式的消息生产/消费
 * @author pain
 *
 */
public class Producer2 {

	private final static String queue_name="rpcTest_queue";
	
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
			//创建rpc发送消息exchange和队列绑定
			channel.exchangeDeclare("rpcSendExchange", "direct");
			channel.queueDeclare("rpcSendQueue", true, false, false, null);
			channel.queueBind("rpcSendQueue", "rpcSendExchange", "rpcSendMessage");
			// 建立RPc返回消息的 directExchange  消息队列绑定
			channel.exchangeDeclare("rpcReplyExchange", "direct");
			channel.queueDeclare("rpcReplyQueue", true, false, false, null);
			channel.queueBind("rpcReplyQueue", "rpcReplyExchange", "rpcReplyMessage");
			
			//创建返回消息的消费者
			QueueingConsumer replyCustomer=new QueueingConsumer(channel);
			channel.basicConsume("rpcReplyQueue", true,replyCustomer);
			
			String message="10";
			//生成rpc消息的 correlationId
			String correlationId=UUID.randomUUID().toString();
			//在rabbitMq消息的Properties中设置RPC请求消息的correleationId以及
			// replyTo(这里使用的是Exchange的名称不是 消息队列的名称)
			BasicProperties props=new BasicProperties
					                  .Builder()
					                  .correlationId(correlationId)
					                  .replyTo("rpcReplyExchange")
					                  .build();
			
			//发送到队列中
			channel.basicPublish("rpcSendExchange", "rpcSendMessage", props, message.getBytes("UTF-8"));
			System.out.println("producer send message:"+message);
			
			String response="";
			while(true){
				// 从发返回消息中取出一条信息
				Delivery delivery=replyCustomer.nextDelivery();
				//如果消息的CorrelationId与发送的CorrelationId一致的话，表示是
				// 发送消息的对应回复信息
				String messageCorrelationId=delivery.getProperties().getCorrelationId();
				if(messageCorrelationId!=null && correlationId.equals(messageCorrelationId)){
					response=new String(delivery.getBody());
					break;
				}
				System.out.println("this message reply is："+messageCorrelationId);
			}
			//关闭通道和链接
			channel.close();
			conn.close();
			
			System.out.println(" send message reply is "+response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

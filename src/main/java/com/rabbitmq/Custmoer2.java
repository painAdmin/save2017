package com.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
/**
 * RPC方式的消息生产/消费
 * @author pain
 *
 */
public class Custmoer2 {

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
			channel.queueDeclare("rpcSendQueue", true, false, false, null);
			System.out.println("start custmoer receive message::::::::::");
			Consumer consumer=new DefaultConsumer(channel){

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					String replyExchange=properties.getReplyTo();
					//设置返回消息的properties 附带发送消息的CorrelationId
					String correlationId=properties.getCorrelationId(); 
					String message=new String(body,"UTF-8");
					
					AMQP.BasicProperties replyProps=new  AMQP.BasicProperties()
							                             .builder()
							                             .correlationId(correlationId)
							                             .replyTo(replyExchange)
							                             .build();
					
					System.out.println("custmoer receive message:"+message);
					
					int num=Integer.parseInt(message);
					String replyMessage=num*num*num+"";
					
					this.getChannel().basicPublish(replyExchange, "rpcReplyMessage", replyProps, replyMessage.getBytes("UTF-8"));
				}
				
			};
			// 自动回复队列应答，  rabbitmq 中的消息确认机制
			channel.basicConsume("rpcSendQueue", true,consumer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}

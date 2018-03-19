package com.rabbitmq;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.recovery.AutorecoveringChannel;
import com.rabbitmq.client.impl.recovery.AutorecoveringConnection;
/**
 * RPC方式的消息生产/消费
 * @author pain
 *
 */
public class RecoveryProducerApp {

	private final static String queue_name="rpcTest_queue";
	
	public static void main(String[] args){
		//创建连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		//设置rabbitmq 相关信息
		factory.setNetworkRecoveryInterval(60000);
		factory.setTopologyRecoveryEnabled(true);
		factory.setHost("localhost");
		factory.setPort(5672);
//		factory.setUsername("rabbitmq_producer");
//		factory.setPassword("123.com");

//		factory.setVirtualHost("test_vhosts");
		//创建与rabitmq 服务器的tcp连接
		try {
			AutorecoveringConnection conn=(AutorecoveringConnection)factory.newConnection();
			//创建一个通道
			AutorecoveringChannel channel=(AutorecoveringChannel)conn.createChannel();
			//设置channel 为 publish confirm 模式
		    channel.confirmSelect();
			//创建rpc发送消息exchange和队列绑定
			channel.exchangeDeclare("recoveryExchange", BuiltinExchangeType.DIRECT,false,true,null);
			channel.queueDeclare("recoveryQueue", false, false, true, null);
			channel.queueBind("recoveryQueue", "recoveryExchange", "recoveryMessage");
			String message=" this  is  recovery test";
			
			
			//发送到队列中
			channel.basicPublish("recoveryExchange", "recoveryMessage", null, message.getBytes("UTF-8"));
			System.out.println("producer send message:"+message);
			
			//关闭通道和链接
			channel.close();
			conn.close();
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
		} 
		
	}
}

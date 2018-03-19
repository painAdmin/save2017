package com.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.rabbitmq.client.impl.recovery.AutorecoveringChannel;
import com.rabbitmq.client.impl.recovery.AutorecoveringConnection;

public class RecoveryConsumerApp {

	public static void main(String[] args){
		ConnectionFactory factory=new ConnectionFactory();
		
		AutorecoveringConnection connection;
		try {
			connection = (AutorecoveringConnection)factory.newConnection();
			String originaLocalAddres=connection.getLocalAddress()+":"+connection.getLocalPort();
			System.out.println("the origin connection is ::::"+originaLocalAddres);
		
		    AutorecoveringChannel channel=(AutorecoveringChannel)connection.createChannel();
		    System.out.println("the origin channel id :::"+channel.getChannelNumber());
		    
		    channel.exchangeDeclare("recoveryExchange", BuiltinExchangeType.DIRECT,false,true,null);
		    channel.queueDeclare("recoveryQueue", false, false, true, null);
		    channel.queueBind("recoveryQueue", "recoveryExchange", "recoveryMessage");
		    
		    connection.addRecoveryListener(new RecoveryListener(){

				public void handleRecovery(Recoverable recoverable) {
						System.out.println(" 111_11 connection handleRecovery method is called");
						AutorecoveringConnection recoveringConnection=(AutorecoveringConnection)recoverable;
						String recoveredLocalAddress=recoveringConnection.getLocalAddress()+":"+recoveringConnection.getLocalPort();
						System.out.println(" 111_22 the recovered connection is :::"+recoveredLocalAddress);
						
				}

				public void handleRecoveryStarted(Recoverable arg0) {	
					System.out.println(" 111_33 connection handleRecoveryStarted is called");
				}
		    	
		    });
		    channel.addRecoveryListener(new RecoveryListener(){

				public void handleRecovery(Recoverable recoverable) {
					  System.out.println(" 222_11 connection handleRecovery is called");
					  AutorecoveringConnection recoveringConnection=(AutorecoveringConnection)recoverable;
					  String recoveryLocalAddress=recoveringConnection.getLocalAddress()+":"+recoveringConnection.getLocalPort();
					  System.out.println(" 222_22  the recovery connection Address is ::"+recoveryLocalAddress);
					
				}

				public void handleRecoveryStarted(Recoverable recoverable) {
					System.out.println(" 222_33 the connection handleRecoveryStarted is called");
					
				}
		    	
		    });
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			
			e.printStackTrace();
		}
		
	}
}

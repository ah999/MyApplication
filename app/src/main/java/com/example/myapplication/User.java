package com.example.myapplication;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class User extends Thread{
    private String i;

    public User(){}
    private final static String QUEUE_NAME = "messenger";


    // run your networking code here
    public void publish(String i) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("ahmed");
        factory.setPassword("123");
        factory.setHost("192.168.2.104");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            if(i==null){System.out.println("null");}else {
            channel.basicPublish("", QUEUE_NAME, null, i.getBytes());
            System.out.println(" [x] Sent '" + i + "'");
            channel.close();
            connection.close();}
        }catch (TimeoutException |java.io.IOException e){

        }
    }

    public void subscribe()  {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("ahmed");
        factory.setPassword("123");
        factory.setHost("192.168.2.104");
        factory.setPort(15672);
        try{Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }catch (java.io.IOException| TimeoutException e){

        }
    }

}

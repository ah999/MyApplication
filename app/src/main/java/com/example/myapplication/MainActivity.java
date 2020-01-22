package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    ConnectionFactory factory = new ConnectionFactory();
    private final static String QUEUE_NAME = "messenger";
    TextView txt;
    Button publish, Publish2, subscribe, subscribe2;
    EditText edttext;
    TextView textView;
     String etext;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publish = (Button) findViewById(R.id.publish);
        Publish2 = (Button) findViewById(R.id.publish2);
        subscribe = (Button) findViewById(R.id.subscribe);
        subscribe2 = (Button) findViewById(R.id.subscribe2);
        edttext = (EditText) findViewById(R.id.editText);
        txt = (TextView) findViewById(R.id.textViewResult);

        publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Log.i(TAG, "sendNumber_Button was clicked");
                try {
                    edttext = (EditText) findViewById(R.id.editText);
                    etext = (String) edttext.getText().toString();

                    new Publisher().execute(etext);


                    Context context = getApplicationContext();
                    CharSequence text = "published to Blue";

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, etext, duration);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        subscribe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // Log.i(TAG, "sendNumber_Button was clicked");
                try {
                    Context context = getApplicationContext();
                    final CharSequence text = "subscribed Blue";
                    txt = (TextView) findViewById(R.id.textViewResult);

                    new Subscriber().execute();
                    //new Subscriber().onPostExecute(etext);
                    String m;
                    m = message;
                    txt.setText(m);

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Publish2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Log.i(TAG, "sendNumber_Button was clicked");
                try {
                    edttext = (EditText) findViewById(R.id.editText);
                    etext = (String) edttext.getText().toString();

                    new Publisher().execute(etext);


                    Context context = getApplicationContext();
                    CharSequence text = "published to Red";

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, etext, duration);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        subscribe2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Log.i(TAG, "sendNumber_Button was clicked");
                try {
                    Context context = getApplicationContext();
                    final CharSequence text = "subscribed Red";
                    txt = (TextView) findViewById(R.id.textViewResult);

                    new Subscriber().execute();
                    //new Subscriber().onPostExecute(etext);
                    String m;
                    m = message;
                    txt.setText(etext);

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }




    public class Publisher extends AsyncTask<String, Void, String> {


        private Exception exception;
        private final static String QUEUE_NAME = "messenger";
        private static final String EXCHANGE_NAME = "logs";


        @Override
        protected String doInBackground(String... strings) {
            try {
                factory.setHost("192.168.2.104");
                factory.setPort(5672);
                factory.setVirtualHost("/");
                factory.setUsername("ahmed");
                factory.setPassword("123");

                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, EXCHANGE_NAME, "");


                //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish(EXCHANGE_NAME, "", null, strings[0].getBytes("UTF-8"));
                channel.close();
                connection.close();
            } catch (TimeoutException | java.io.IOException e) {

            }

            return strings[0];
        }

        // run your networking code here


    }
    public class Subscriber extends AsyncTask< String, Void, String> {
        private Exception exception;
        private static final String EXCHANGE_NAME = "logs";


        @Override
        protected String doInBackground(String... strings) {
            try{
                factory.setUsername("ahmed");
                factory.setPassword("123");
                factory.setHost("192.168.2.104");
                factory.setPort(5672);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, EXCHANGE_NAME, "");

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        message = new String(body, "UTF-8");
                        System.out.println(" [x] Received '" + message + "'");
                    }
                };
                channel.basicConsume(QUEUE_NAME, true, consumer);

            }catch (java.io.IOException| TimeoutException e){
                return null;
            }
            return message;
        }
        @Override
        protected void onPostExecute(String result) {
            txt.setText(result);
            super.onPostExecute(result);
        }


    }


}
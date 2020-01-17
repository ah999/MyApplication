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



import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    ConnectionFactory factory = new ConnectionFactory();
    private final static String QUEUE_NAME = "messenger";
    TextView txt;
    Button btn;
    EditText edttext;
    TextView textView;
     String etext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Publish(View view) throws java.io.IOException, TimeoutException {
        edttext = (EditText) findViewById(R.id.editText3);
        etext = (String) edttext.getText().toString();

        new Publisher().execute(etext);


        Context context = getApplicationContext();
        CharSequence text = "published ";

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, etext, duration);
        toast.show();


    }

    public void subscribe(View view) throws java.io.IOException, java.lang.InterruptedException, TimeoutException {
        Context context = getApplicationContext();
        final CharSequence text = "subscribed ";
        txt = (TextView) findViewById(R.id.textView);

        new Subscriber().execute();
        new Subscriber().onPostExecute(etext);

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public class Publisher extends AsyncTask<String, Void, String> {


        private Exception exception;
        private final static String QUEUE_NAME = "messenger";


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
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, strings[0].getBytes());
                channel.close();
                connection.close();
            } catch (TimeoutException | java.io.IOException e) {
                return null;
            }

            return strings[0];
        }

        // run your networking code here


    }
    public class Subscriber extends AsyncTask< String, Void, String> {
        private Exception exception;
        String message;


        @Override
        protected String doInBackground(String... strings) {
            try{
                factory.setUsername("ahmed");
                factory.setPassword("123");
                factory.setHost("192.168.2.104");
                factory.setPort(5672);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
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
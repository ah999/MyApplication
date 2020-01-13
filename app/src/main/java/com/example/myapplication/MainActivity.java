package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    Button btn;
    EditText edttext;
    User user = new User();

    //    User subscriber = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Publish(View view) throws java.io.IOException, TimeoutException {
        edttext = (EditText) findViewById(R.id.editText3);
        final String etext = (String) edttext.getText().toString();
        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                try  {
                    user.publish(etext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        Context context = getApplicationContext();
        CharSequence text = "published ";

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, etext, duration);
        toast.show();


    }
    public void subscribe (View view)throws java.io.IOException, java.lang.InterruptedException, TimeoutException{
        Context context = getApplicationContext();
        CharSequence text = "subscribed ";
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    user.subscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
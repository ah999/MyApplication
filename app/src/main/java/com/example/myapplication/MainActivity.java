package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    TextView txt ;
    Button btn ;
    EditText edttext ;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Publish (View view)throws java.io.IOException, TimeoutException{
        edttext= (EditText)findViewById(R.id.editText3);
        Context context = getApplicationContext();
        user.publish();
        CharSequence text = "published ";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, edttext.getText() , duration);
        toast.show();
    }
    public void subcribe (View view)throws java.io.IOException, java.lang.InterruptedException, TimeoutException{
        Context context = getApplicationContext();
        CharSequence text = "subscribed ";
        user.subscribe();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
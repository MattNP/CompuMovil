package com.example.destebanalvarez.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private int cont=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v){


        if(cont==3) {
            Intent intent = new Intent();
            intent.setAction("co.udea.compumovil.custombradcast");
            sendBroadcast(intent);
            cont=1;
        }
        else cont++;
    }
}

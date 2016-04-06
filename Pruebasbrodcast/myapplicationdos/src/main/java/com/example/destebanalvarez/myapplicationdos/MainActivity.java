package com.example.destebanalvarez.myapplicationdos;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String CUSTOM_INTENT="co.edu.udea.compumovil.broadcast";
    private LocalBroadcastManager localBroadcastManager;
    private MyReceiver myreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
        myreceiver= new MyReceiver();

        localBroadcastManager = localBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(myreceiver,intentFilter);
    }

    public void onClick(View v){
        localBroadcastManager.sendBroadcast(new Intent(CUSTOM_INTENT));

    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(myreceiver);
        super.onDestroy();
    }
}

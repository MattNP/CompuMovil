package com.example.destebanalvarez.myapplicationalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class MainActivityAlarm extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_alarm);
        spinner=(Spinner)findViewById(R.id.spinner);

    }

    public void onClick(View v ){
        int num=Integer.parseInt(spinner.getSelectedItem().toString());
        Intent intent=new Intent(this, MyReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),123456789,intent,0);


        AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+(num*1000),pendingIntent);
    }
}

package com.example.destebanalvarez.pruebasbrodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("SMSReceivor","in onReceive");
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        String address = "";

        if (bundle != null) {

            //--retrieve the SMS message received--
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                address = msgs[i].getOriginatingAddress();
                str += "SMS from " + address;
                str += ": ";
                str += msgs[i].getMessageBody().toString();
                str += "\n";

                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            }

        }
        Toast.makeText(context,"estamos en onReceive",Toast.LENGTH_LONG).show();

    }
}

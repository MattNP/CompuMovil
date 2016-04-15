package com.example.destebanalvarez.pruebacronometro;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView txt_cron;
    int contador=0;
    Button iniciar;
    @Override
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("newapi")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_cron=(TextView)findViewById(R.id.textView2);
        iniciar=(Button)findViewById(R.id.button);
        txt_cron.setText("00:25:00");

        final counterclass timer= new counterclass(1500000,1000);
        iniciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timer.start();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("newapi")
    public class counterclass extends CountDownTimer{
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public counterclass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("newapi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {

            long millis=millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            txt_cron.setText(hms);

        }

        @Override
        public void onFinish() {

        }
    }
}

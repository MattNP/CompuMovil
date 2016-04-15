package co.edu.udea.compumovil.gr4.lab3pomodoro;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class ServiceTimer extends Service {

    private final String TAG = "MyService";

    public static CountDownTimer timer;

    private final static String TAG2 = "BroadcastService";
    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";

    public final static String NOTIFICATION = "Notificación";
    public final static String FILEPATH = "Algún FilePath";
    public final static String RESULT = "Algún Result";
    public static String hms;
    public static Intent msj;

    public final String outputPath = "Algún Path de salida";
    public final String result = "Algún result";



    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    public ServiceTimer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.


        int miliseconds = intent.getIntExtra("MILISECONDS",0 );
        int interval = intent.getIntExtra("INTERVAL",0);

        timer = new CountDownTimer(miliseconds, interval) {
            @Override
            public void onTick(long millisUntilFinished) {


                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);

                Log.d("CronometroService", "time: " + hms);
            }

            @Override
            public void onFinish() {

                Log.d("CronometroService", "time is up");
                Log.i(TAG2, "Timer finished");
            }
        };

        timer.start();

//        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
//        Log.v(TAG, "onStartCommand");
//
//        final int currentId = startId;
//        Log.d(TAG, "Service started");
//
//        timer.start();
//
//        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG2, "Timer cancelled");
        timer.cancel();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.v(TAG, "onDestroy");
    }



}

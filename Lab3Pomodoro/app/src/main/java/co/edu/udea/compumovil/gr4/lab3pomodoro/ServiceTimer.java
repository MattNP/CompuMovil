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

    private CountDownTimer timer;



    public final static String NOTIFICATION = "Notificación";
    public final static String FILEPATH = "Algún FilePath";
    public final static String RESULT = "Algún Result";

    public final String outputPath = "Algún Path de salida";
    public final String result = "Algún result";


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

        int miliseconds = intent.getIntExtra("MILISECONDS", 10000);
        int interval = intent.getIntExtra("INTERVAL",1000);

        timer = new CountDownTimer(miliseconds, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                Log.d("CronometroService", "time: " + hms);
            }

            @Override
            public void onFinish() {
                Log.d("CronometroService", "time is up");
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
        timer.cancel();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.v(TAG, "onDestroy");
    }


}

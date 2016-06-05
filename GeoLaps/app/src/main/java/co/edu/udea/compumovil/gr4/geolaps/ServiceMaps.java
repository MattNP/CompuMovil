package co.edu.udea.compumovil.gr4.geolaps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ServiceMaps extends Service {

    public final static String BROADCAST = "co.edu.udea.compumovil.gr4.geolaps";
    public final static String FINISH = "finish";

    private Intent intentBroadcast = new Intent(BROADCAST);
    private final String TAG = "ServiceMaps";

    public ServiceMaps() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }


    /*
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        int milliseconds = intent.getIntExtra(MILLISECONDS, 0);
        int interval = intent.getIntExtra(INTERVAL, 0);

        timer = new CountDownTimer(milliseconds, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                intentBroadcast.putExtra(COUNTDOWN, millisUntilFinished);
                intentBroadcast.putExtra(FINISH, false);
                sendBroadcast(intentBroadcast);

                Log.d(TAG, "time: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                intentBroadcast.putExtra(FINISH, true);
                Log.d(TAG, "time is up");
                if(intent.hasExtra(OpcionesActivity.PREF_VIBRATION)) {
                    if(intent.getBooleanExtra(OpcionesActivity.PREF_VIBRATION, false)) {
                        Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                }
                sendBroadcast(intentBroadcast);
                presentNotification(Notification.VISIBILITY_PUBLIC, android.R.drawable.ic_dialog_alert, getString(R.string.notification_title), getString(R.string.notification_information));

            }
        };

        timer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void presentNotification(int visibility, int icon, String title, String text) {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this)
                .setCategory(Notification.CATEGORY_ALARM)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setVisibility(visibility)
                .addAction(android.R.drawable.ic_menu_view, getString(R.string.notification_information), contentIntent)
                .setContentIntent(contentIntent)
                .setSound(uri)
                .setPriority(Notification.PRIORITY_HIGH).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
    */
}

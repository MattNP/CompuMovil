package co.edu.udea.compumovil.gr4.geolaps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBUtil;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class ServiceMaps extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public final static String BR = "co.edu.udea.compumovil.gr4.geolaps";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Intent intentBroadcast;

    private double currentLatitude;
    private double currentLongitude;

    public ServiceMaps() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(90 * 1000)        // 90 seconds, in milliseconds
                .setFastestInterval(60 * 1000); // 60 seconds, in milliseconds

        mGoogleApiClient.connect();

        intentBroadcast=new Intent(BR);

        return START_STICKY;
    }

    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Log.d("ServiceMaps", "onConnected: " + currentLatitude + ", " + currentLongitude);

            intentBroadcast.putExtra(Dashboard.CURRENT_LATITUDE, currentLatitude);
            intentBroadcast.putExtra(Dashboard.CURRENT_LONGITUDE, currentLongitude);
            sendBroadcast(intentBroadcast);

            verificarRadio();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onConnectionSuspended(int i) {
        Log.d("ServiceMaps", "onConnectionSuspended: " + currentLatitude + ", " + currentLongitude);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */

        if (connectionResult.hasResolution()) {

            /*
            try {


            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
            */
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */

            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Log.d("ServiceMaps", "onLocation: " + currentLatitude + ", " + currentLongitude);

        intentBroadcast.putExtra(Dashboard.CURRENT_LATITUDE,currentLatitude);
        intentBroadcast.putExtra(Dashboard.CURRENT_LONGITUDE,currentLongitude);
        sendBroadcast(intentBroadcast);
        verificarRadio();
    }

    //Esto lo debe hacer cada poco tiempo
    private void verificarRadio() {

        int radio = 100; //Debe ser configurable, y por evento

        List<Recordatorio> recordatoriosActivos = DBUtil.getRecordatoriosActivos(this);

        for(Recordatorio recordatorio : recordatoriosActivos) {
            Lugar lugar = recordatorio.getLugares().get(0);
            float[] distance = new float[2];
            Location.distanceBetween(lugar.getLatitud(), lugar.getLongitud(),
                    currentLatitude, currentLongitude, distance);

            if(distance[0] < radio){
                if(recordatorio.getAdentro() == 0) {
                    presentNotification(Notification.VISIBILITY_PUBLIC, android.R.drawable.ic_dialog_alert, "Recuerda que debes ir a " + recordatorio.getNombre(), recordatorio.getDescripcion() );
                    DBUtil.actualizarEstado(recordatorio.getId(), 1, this);
                }
                Log.d("verificarRadio", "Inside " + recordatorio.getNombre() + ". Distancia: " + distance[0]);
            } else {
                if(recordatorio.getAdentro() == 1) {
                    DBUtil.actualizarEstado(recordatorio.getId(), 0, this);
                }
            }
        }
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
}

package co.edu.udea.compumovil.gr4.geolaps;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.database.DBUtil;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServiceMaps extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String BR = "co.edu.udea.compumovil.gr4.geolaps";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Intent intentBroadcast;

    private double currentLatitude;
    private double currentLongitude;

    public IntentServiceMaps() {
        super("IntentServiceMaps");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("onHandleIntent", "Antes");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        Log.d("onHandleIntent", "Start");

        mGoogleApiClient.connect();

        intentBroadcast=new Intent(BR);

    }

    public void onConnected(Bundle bundle) {

        Log.d("onHandleIntent", "I got maps at first");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Log.d("onHandleIntent", "I got maps");

            intentBroadcast.putExtra(Dashboard.CURRENT_LATITUDE, currentLatitude);
            intentBroadcast.putExtra(Dashboard.CURRENT_LONGITUDE, currentLongitude);
            Log.d("onHandleIntent", "broadcast");
            sendBroadcast(intentBroadcast);

            verificarRadio();
        }
    }

    public void onConnectionSuspended(int i) {}

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
        Log.d("verificarRadio", "cambio ubicación");
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        //Enviar broadcast
        intentBroadcast.putExtra(Dashboard.CURRENT_LATITUDE,currentLatitude);
        intentBroadcast.putExtra(Dashboard.CURRENT_LONGITUDE,currentLongitude);
        sendBroadcast(intentBroadcast);
        verificarRadio();
    }

    //Esto lo debe hacer cada poco tiempo
    private void verificarRadio() {

        Log.d("verificarRadio", "Inside the method");

        int radio = 100; //Debe ser configurable, y por evento

        List<Recordatorio> recordatoriosActivos = getRecordatoriosActivos();

        for(Recordatorio recordatorio : recordatoriosActivos) {
            Lugar lugar = recordatorio.getLugar();
            float[] distance = new float[2];
            Location.distanceBetween(lugar.getLatitud(), lugar.getLongitud(),
                    currentLatitude, currentLongitude, distance);

            if(distance[0] < radio){
                Log.d("verificarRadio", "Inside " + recordatorio.getNombre() + ". Distancia: " + distance[0]);
                Toast.makeText(this, "Inside " + recordatorio.getNombre(), Toast.LENGTH_SHORT).show();
                //presentNotification(Notification.VISIBILITY_PUBLIC, android.R.drawable.ic_dialog_alert, getString(R.string.notification_title), getString(R.string.notification_information));
            }
        }
    }

    private List<Recordatorio> getRecordatoriosActivos() {

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Recordatorio> recordatoriosActivos = new ArrayList<>();

        db = dbHelper.getWritableDatabase();

        Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d("getRecordatorio", "recordatorios: " + Integer.toString(cursorRecordatorio.getCount()));

        if(cursorRecordatorio.moveToFirst()) {
            do {
                Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, this);
                recordatoriosActivos.add(recordatorio);

            } while(cursorRecordatorio.moveToNext());
        }

        db.close();

        return recordatoriosActivos;
    }

    /*
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

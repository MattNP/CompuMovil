package co.edu.udea.compumovil.gr4.geolaps;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        RecordatorioFragment.OnListFragmentInteractionListener{

    private GoogleMap mMap;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private List<Recordatorio> recordatoriosActivos;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "oprimio la tecla", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getBaseContext(), NuevoRecordatorio.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mapa);
        mapFragment.getMapAsync(this);

        recordatoriosActivos = getRecordatoriosActivos();

        Fragment fragment = new RecordatorioFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.recordatoriosContent,fragment).commit();

    }

    private void ubicarMarcadores() {
        mMap.clear();
        for(Recordatorio recordatorio : recordatoriosActivos) {
            Lugar lugar = recordatorio.getLugar();
            LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());
            Log.d("ubicarMarcadores", "lat: " + lugar.getLatitud() + " lng: + " + lugar.getLongitud());
            mMap.addMarker(new MarkerOptions().position(latLng));
            Log.d("ubicarMarcadores", lugar.getNombre());
        }
    }

    public List<Recordatorio> getRecordatoriosActivos() {

        recordatoriosActivos = new ArrayList<>();

        Cursor cursorRecordatorios = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d("getRecordatorio", Integer.toString(cursorRecordatorios.getCount()));

        if(cursorRecordatorios.moveToFirst()) {
            do {
                Recordatorio recordatorio = new Recordatorio();
                recordatorio.setId(cursorRecordatorios.getInt(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.ID)));
                recordatorio.setUid(cursorRecordatorios.getInt(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.UID)));
                recordatorio.setTipo(cursorRecordatorios.getInt(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIPO)));

                int lugarId = cursorRecordatorios.getInt(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.LUGAR));
                recordatorio.setLugar(getLugar(lugarId));

                recordatorio.setNombre(cursorRecordatorios.getString(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.NOMBRE)));
                recordatorio.setFecha_limite(cursorRecordatorios.getString(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE)));
                recordatorio.setHora_limite(cursorRecordatorios.getString(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.HORA_LIMITE)));
                recordatorio.setTimestamp(cursorRecordatorios.getLong(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIMESTAMP)));
                recordatorio.setDescripcion(cursorRecordatorios.getString(cursorRecordatorios.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.DESCRIPCION)));

            } while(cursorRecordatorios.moveToNext());
        }

        return recordatoriosActivos;
    }

    public Lugar getLugar(int id) {

        Lugar lugar = new Lugar();

        Cursor cursorLugar = db.query(GeoLapsContract.TABLE_LUGAR,
                null,
                GeoLapsContract.ColumnaLugar.ID + "=?",
                new String[] {Integer.toString(id)},
                null,
                null,
                null);

        Log.d("getRecordatorio", Integer.toString(cursorLugar.getCount()));

        if (cursorLugar.moveToFirst()) {
            lugar.setId(cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.ID)));
            lugar.setTipo(Integer.toString(cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.TIPO))));
            lugar.setNombre(cursorLugar.getString(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.NOMBRE)));
            lugar.setLatitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LATITUD)));
            double lat = (cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LATITUD)));
            Log.d("getLugares", "lat: " + lat);
            lugar.setLongitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LONGITUD)));
            double lng = cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LONGITUD));
            Log.d("getLugares", "log: " + lng);
        }

        return lugar;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                ||(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)){

            mMap.setMyLocationEnabled(true);

        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                //lstLatLngs.add(point);
                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(point));
            }
        });

        ubicarMarcadores();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {


            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //mMap.clear();
            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mp.title("onConnected");
            //mMap.addMarker(mp);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));
        }
    }


    @Override
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
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

        //mMap.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("onLocationChanged");

        //mMap.addMarker(mp);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.perfil_usuario) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.my_place) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(Recordatorio item) {
        Toast.makeText(this, "Presionó un recordatorio", Toast.LENGTH_SHORT).show();
    }
}

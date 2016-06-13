package co.edu.udea.compumovil.gr4.geolaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.database.DBUtil;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        RecordatoriosFragment.OnListFragmentInteractionListener{

    private GoogleMap mMap;

    private double currentLatitude;
    private double currentLongitude;
    private List<Recordatorio> recordatoriosActivos;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public static final String CURRENT_LATITUDE = "currentLatitude";
    public static final String CURRENT_LONGITUDE = "currentLongitude";
    public static final int REQUEST_NUEVO = 2606;
    public static final String RECORDATORIO_SELECCIONADO = "recordatorioSeleccionado";

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //updateGUI(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getBaseContext(), NuevoRecordatorio.class);
                intent.putExtra(CURRENT_LATITUDE, currentLatitude);
                intent.putExtra(CURRENT_LONGITUDE, currentLongitude);
                startActivityForResult(intent, REQUEST_NUEVO);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mapa);
        mapFragment.getMapAsync(this);

        recordatoriosActivos = getRecordatoriosActivos();

        Intent intent = new Intent(this, IntentServiceMaps.class);
        startService(intent);

        getSupportFragmentManager().beginTransaction().replace(R.id.recordatoriosContent,new RecordatoriosFragment()).commit();

                       /*
                Cuando reciba el broadcast
                            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mp.title("onConnected");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));
                */

    }

    private void ubicarMarcadores() {
        mMap.clear();
        for(Recordatorio recordatorio : recordatoriosActivos) {
            Lugar lugar = recordatorio.getLugar();
            LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());
            mMap.addMarker(new MarkerOptions().position(latLng).title(recordatorio.getNombre()).snippet(lugar.getNombre()));
        }
    }

    public List<Recordatorio> getRecordatorios() {
        return recordatoriosActivos;
    }

    private List<Recordatorio> getRecordatoriosActivos() {

        recordatoriosActivos = new ArrayList<>();

        db = dbHelper.getWritableDatabase();

        Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                null,
                null,
                null,
                null,
                null,
                null);

        db.close();

        Log.d("getRecordatorio", "recordatorios: " + Integer.toString(cursorRecordatorio.getCount()));

        if(cursorRecordatorio.moveToFirst()) {
            do {
                Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, this);
                recordatoriosActivos.add(recordatorio);

            } while(cursorRecordatorio.moveToNext());
        }

        return recordatoriosActivos;
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
                //Quiza crear un recordatorio al presionar un punto del mapa
                //lstLatLngs.add(point);
                //mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(point));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        ubicarMarcadores();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(IntentServiceMaps.BR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.v(this.getClass().getSimpleName(), "onPause()");

        /*
        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        */

    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, IntentServiceMaps.class));
        super.onDestroy();
    }

    /*
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

            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mp.title("onConnected");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            verificarRadio();
        }
    }
    */


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
        Intent intent = new Intent(this, RecordatorioActivity.class);
        intent.putExtra(RECORDATORIO_SELECCIONADO, item);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_NUEVO && data != null){
            if(data.hasExtra(NuevoRecordatorio.ID_NUEVO)) {
                Long id = data.getLongExtra(NuevoRecordatorio.ID_NUEVO, 0);
                Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                        null,
                        GeoLapsContract.ColumnaRecordatorio.ID + "=?",
                        new String[] {Long.toString(id)},
                        null,
                        null,
                        null);

                if (cursorRecordatorio.moveToFirst()) {
                    Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, this);
                    recordatoriosActivos.add(recordatorio);

                    Lugar lugar = recordatorio.getLugar();
                    LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());
                    mMap.addMarker(new MarkerOptions().position(latLng));

                    getSupportFragmentManager().beginTransaction().replace(R.id.recordatoriosContent,new RecordatoriosFragment()).commit();
                }
            }
        }
    }
}

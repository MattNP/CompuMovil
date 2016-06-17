package co.edu.udea.compumovil.gr4.geolaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBUtil;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        RecordatoriosFragment.OnListFragmentInteractionListener{

    private GoogleMap mMap;

    private double currentLatitude;
    private double currentLongitude;
    private List<Recordatorio> recordatorios;

    public static final String CURRENT_LATITUDE = "currentLatitude";
    public static final String CURRENT_LONGITUDE = "currentLongitude";
    public static final int REQUEST_NUEVO = 2606;
    public static final int REQUEST_ELIMINAR = 0626;
    public static final String RECORDATORIO_SELECCIONADO = "recordatorioSeleccionado";
    public static final String RECORDATORIO_ELIMINADO = "recordatorioEliminado";

    private float[] colores = {BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_YELLOW};

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(CURRENT_LATITUDE) && intent.hasExtra(CURRENT_LONGITUDE)) {
                currentLatitude = intent.getDoubleExtra(CURRENT_LATITUDE, 0);
                currentLongitude = intent.getDoubleExtra(CURRENT_LONGITUDE, 0);
                Log.d("onHandleIntent", currentLatitude + ", " + currentLongitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(currentLatitude, currentLongitude), 13));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent = new Intent(this, ServiceMaps.class);
        Log.d("onCreate", "iniciando servicio");
        startService(intent);
        recordatorios = DBUtil.getRecordatoriosActivos(this);

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

    }

    private void ubicarMarcadores() {
        mMap.clear();
        for(Recordatorio recordatorio : recordatorios) {
            Lugar lugar = recordatorio.getLugares().get(0);
            LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(recordatorio.getNombre()).snippet(lugar.getNombre()).icon(BitmapDescriptorFactory
                    .defaultMarker(colores[recordatorio.getColor()]));
            mMap.addMarker(markerOptions);
            Log.d("UbicarMarcadores", "lat" + lugar.getLatitud() + "Lon" + lugar.getLongitud());
        }
    }

    public List<Recordatorio> getRecordatorios() {
        return recordatorios;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){

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
        recordatorios = DBUtil.getRecordatoriosActivos(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.recordatoriosContent,new RecordatoriosFragment()).commit();
        registerReceiver(br, new IntentFilter(ServiceMaps.BR));
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
        stopService(new Intent(this, ServiceMaps.class));
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

        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.perfil_usuario:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            case R.id.recordatorios_activos:
                return true;
            case R.id.recordatorios_vencidos:
                return true;
            case R.id.opciones:
                return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(Recordatorio item) {
        Intent intent = new Intent(this, RecordatorioActivity.class);
        intent.putExtra(RECORDATORIO_SELECCIONADO, item);
        startActivityForResult(intent, REQUEST_ELIMINAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_NUEVO && data != null){
            onResume();
            /*if(data.hasExtra(NuevoRecordatorio.ID_NUEVO)) {
                Long id = data.getLongExtra(NuevoRecordatorio.ID_NUEVO, 0);

                db = dbHelper.getWritableDatabase();

                Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                        null,
                        GeoLapsContract.ColumnaRecordatorio.ID + "=?",
                        new String[] {Long.toString(id)},
                        null,
                        null,
                        null);

                if (cursorRecordatorio.moveToFirst()) {
                    Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, this);
                    recordatorios.add(recordatorio);

                    Lugar lugar = recordatorio.getLugares();
                    LatLng latLng = new LatLng(lugar.getLatitud(), lugar.getLongitud());
                    mMap.addMarker(new MarkerOptions().position(latLng));

                    getSupportFragmentManager().beginTransaction().replace(R.id.recordatoriosContent,new RecordatoriosFragment()).commit();
                }

                db.close();
            }
            */
            ubicarMarcadores();
        }

        if(requestCode == REQUEST_ELIMINAR && data != null) {
           onResume();
            ubicarMarcadores();

        }


    }
}

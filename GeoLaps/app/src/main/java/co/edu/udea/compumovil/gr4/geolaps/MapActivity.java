package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String LATITUD = "lat";
    public static final String LONGITUD = "lon";

    private GoogleMap mMap;
    private double lat;
    private double lng;
    private EditText txt_buscar;

    private String busqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if(intent.hasExtra(Dashboard.CURRENT_LONGITUDE) && intent.hasExtra(Dashboard.CURRENT_LATITUDE)) {
                lat = intent.getDoubleExtra(Dashboard.CURRENT_LATITUDE, 0);
                lng = intent.getDoubleExtra(Dashboard.CURRENT_LONGITUDE, 0);
            }
        }

        txt_buscar = (EditText)findViewById(R.id.txt_buscar);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
        ||(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)){

            mMap.setMyLocationEnabled(true);

        }

        LatLng latLng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                crearMarcador(point);
            }
        });
    }

    private void crearMarcador(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        lat = latLng.latitude;
        lng = latLng.longitude;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nuevo_recordatorio_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardarRecordatorio:
                guardarMarcador();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSearch(View view) {
        busqueda = txt_buscar.getText().toString();

        if(busqueda != null && !busqueda.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> listaLugares = geocoder.getFromLocationName(busqueda, 1);
                if(listaLugares.size() == 0) {
                    Toast.makeText(this, "No se pudo encontrar el lugar", Toast.LENGTH_SHORT);

                } else {
                    Address lugar = listaLugares.get(0);
                    LatLng lugarLatLng = new LatLng(lugar.getLatitude(), lugar.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(lugarLatLng).title(lugar.getFeatureName()).snippet(lugar.getAddressLine(0)));
                    lat = lugarLatLng.latitude;
                    lng = lugarLatLng.longitude;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugarLatLng, 16));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarMarcador() {
        Intent intent = new Intent();
        intent.putExtra(LATITUD, lat);
        intent.putExtra(LONGITUD, lng);
        setResult(NuevoRecordatorio.REQUEST_MAP, intent);
        finish();
    }
}

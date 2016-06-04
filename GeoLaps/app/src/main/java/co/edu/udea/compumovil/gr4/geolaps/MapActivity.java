package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.jar.Manifest;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String LATITUD = "lat";
    public static final String LONGITUD = "lon";

    private GoogleMap mMap;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if(intent.hasExtra(Dashboard.CURRENT_LONGITUDE) && intent.hasExtra(Dashboard.CURRENT_LATITUDE)) {
                lat = intent.getDoubleExtra(Dashboard.CURRENT_LATITUDE, 0);
                lng = intent.getDoubleExtra(Dashboard.CURRENT_LONGITUDE, 0);
                Log.d("onMapReady: ", "intentRecibido: (" + Double.toString(lat) + "," + Double.toString(lng) + ")");
            }
        }
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
        Log.d("onMapReady: ", "Punto ubicado: " + latLng.toString());
        mMap.addMarker(new MarkerOptions().position(latLng));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                //lstLatLngs.add(point);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                lat = point.latitude;
                lng = point.longitude;
            }
        });
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

    private void guardarMarcador() {
        Intent intent = new Intent();
        intent.putExtra(LATITUD, lat);
        intent.putExtra(LONGITUD, lng);
        setResult(NuevoRecordatorio.REQUEST_MAP, intent);
        finish();
    }
}

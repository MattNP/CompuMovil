package co.edu.udea.compumovil.gr4.geolaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class NuevoRecordatorio extends AppCompatActivity {

    GoogleMap mapas;
    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recordatorio);
        //ingresar=(Button)findViewById(R.id.btn_abrir_mapa);
        SupportMapFragment mymap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Mapas);
        mapas = mymap.getMap();

        mapas.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mapas.setMyLocationEnabled(true);


        }
        mapas.getUiSettings().setZoomControlsEnabled(true);
        

    }
    public void onClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}

package co.edu.udea.compumovil.gr4.geolaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;

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

    public List<String> getTiposRecordatorio() {

        List<String> tiposRecordatorio = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(GeoLapsContract.TABLE_TIPO_LUGAR,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d("getTiposCarrera", Integer.toString(cursor.getCount()));

        if (cursor.moveToFirst()) {
            do {
                String tipoLugar = cursor.getString(cursor.getColumnIndex(GeoLapsContract.ColumnaTipoRecordatorio.TIPO));
                tiposRecordatorio.add(tipoLugar);
            } while (cursor.moveToNext());
        }

        return tiposRecordatorio;
    }

}

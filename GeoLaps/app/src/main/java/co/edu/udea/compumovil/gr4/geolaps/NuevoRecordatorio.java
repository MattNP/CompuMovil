package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;

public class NuevoRecordatorio extends AppCompatActivity {


    Button ingresar;
    Spinner spinner_tipo_recordatorio, spinner_tipo_lugar;
    EditText txt_titulo, txt_lugar;
    TextView txt_fecha, txt_hora;

    private final int TIPO_LUGAR = 1;
    private final int TIPO_RECORDATORIO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recordatorio);
        //ingresar=(Button)findViewById(R.id.btn_abrir_mapa);

        //Llena spinner tipo recordatorio
        spinner_tipo_recordatorio = (Spinner) findViewById(R.id.spinner_tipo_recordatorio);
        List<String> tiposRecordatorio = getTipos(TIPO_RECORDATORIO);
        ArrayAdapter<String> adapterRecordatorio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapterRecordatorio.addAll(tiposRecordatorio);
        spinner_tipo_recordatorio.setAdapter(adapterRecordatorio);

        //Llena spinner tipo lugar
        spinner_tipo_lugar = (Spinner) findViewById(R.id.spinner_tipo_lugar);
        List<String> tiposLugar = getTipos(TIPO_LUGAR);
        ArrayAdapter<String> adapterLugar = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapterLugar.addAll(tiposLugar);
        spinner_tipo_lugar.setAdapter(adapterLugar);
    }

    public void onClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public List<String> getTipos(int tipo) {

        String tabla = GeoLapsContract.TABLE_TIPO_RECORDATORIO;
        if(tipo == TIPO_LUGAR) {
            tabla = GeoLapsContract.TABLE_TIPO_LUGAR;
        }

        List<String> tipos = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tabla,
                null,
                null,
                null,
                null,
                null,
                null);

        Log.d("getTipos", Integer.toString(cursor.getCount()));

        if (cursor.moveToFirst()) {
            do {
                String tipoD = cursor.getString(cursor.getColumnIndex(GeoLapsContract.ColumnaTipoRecordatorio.TIPO));
                tipos.add(tipoD);
            } while (cursor.moveToNext());
        }

        return tipos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nuevo_recordatorio_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.guardarRecordatorio:
                guardarRecordatorio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void guardarRecordatorio() {

    }

}

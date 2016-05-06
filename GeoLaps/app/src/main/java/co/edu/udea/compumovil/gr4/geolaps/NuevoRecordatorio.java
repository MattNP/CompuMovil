package co.edu.udea.compumovil.gr4.geolaps;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;

public class NuevoRecordatorio extends AppCompatActivity {

    public static final String FORMATO_FECHA = "dd-MMM-yyyy";
    public static final String FORMATO_HORA = "HH:mm";
    public static final int REQUEST_MAP = 4236;

    private Spinner spinner_tipo_recordatorio, spinner_tipo_lugar;
    private EditText txt_titulo, txt_descripcion;
    private TextView txt_fecha, txt_hora, txt_lugar;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
    private SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
    private final Calendar calendar = Calendar.getInstance();
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;
    private double latitud, longitud;
    private String tipo_recordario, tipo_lugar, titulo, lugar, descripcion, fecha, hora, timestamp;

    private final int TIPO_LUGAR = 1;
    private final int TIPO_RECORDATORIO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recordatorio);

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

        txt_titulo = (EditText)findViewById(R.id.txt_titulo_recordatorio);
        txt_lugar = (TextView)findViewById(R.id.txt_lugar);
        txt_descripcion = (EditText)findViewById(R.id.txt_descripcion);

        txt_fecha = (TextView)findViewById(R.id.txt_fecha);
        txt_fecha.setText(formatoFecha.format(calendar.getTime()));
        datePickerFragment = new DatePickerFragment();

        txt_hora = (TextView)findViewById(R.id.txt_hora);
        txt_hora.setText(formatoHora.format(calendar.getTime()));
        timePickerFragment = new TimePickerFragment();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.txt_fecha:
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.txt_hora:
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.img_mapa:
                Intent intentMap = new Intent(this, MapActivity.class);
                startActivityForResult(intentMap,REQUEST_MAP);
        }
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
        switch (item.getItemId()) {
            case R.id.guardarRecordatorio:
                guardarRecordatorio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void guardarRecordatorio() {
        tipo_recordario = spinner_tipo_lugar.getSelectedItem().toString();
        tipo_lugar = spinner_tipo_recordatorio.getSelectedItem().toString();
        titulo = txt_titulo.getText().toString();
        lugar = txt_lugar.getText().toString();
        descripcion = txt_descripcion.getText().toString();
        fecha = txt_fecha.getText().toString();
        hora = txt_hora.getText().toString();
        timestamp = Long.toString(calendar.getTimeInMillis());

        if(titulo.equals("") || lugar.equals("")) {
            Toast.makeText(NuevoRecordatorio.this, getString(R.string.campos_incompletos), Toast.LENGTH_SHORT).show();
        } else {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues valuesLugar = new ContentValues();
            valuesLugar.put(GeoLapsContract.ColumnaLugar.TIPO, 0);
            valuesLugar.put(GeoLapsContract.ColumnaLugar.NOMBRE, lugar);
            valuesLugar.put(GeoLapsContract.ColumnaLugar.LATITUD, latitud);
            valuesLugar.put(GeoLapsContract.ColumnaLugar.LONGITUD, longitud);

            Log.d("guardarRecordatorio", "lat: " + latitud);
            Log.d("guardarRecordatorio", "lng: " + longitud);

            long lugarId = db.insertWithOnConflict(GeoLapsContract.TABLE_LUGAR, null, valuesLugar,
                    SQLiteDatabase.CONFLICT_IGNORE);


            ContentValues valuesRecordatorio = new ContentValues();
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.UID, 0);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.TIPO, 0);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.LUGAR, lugarId);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.NOMBRE, titulo);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE, fecha);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.HORA_LIMITE, hora);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.TIMESTAMP, timestamp);
            valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.DESCRIPCION, descripcion);

            db.insertWithOnConflict(GeoLapsContract.TABLE_RECORDATORIO, null, valuesRecordatorio,
                    SQLiteDatabase.CONFLICT_IGNORE);
            dbHelper.close();

            Toast.makeText(this, getString(R.string.recordatorio_guardado), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_MAP && data != null){
            latitud = data.getDoubleExtra(MapActivity.LATITUD, 0);
            longitud = data.getDoubleExtra(MapActivity.LONGITUD, 0);
            lugar = String.format("%.6f, %.6f", latitud, longitud);
            txt_lugar.setText(lugar);
        }
    }

}

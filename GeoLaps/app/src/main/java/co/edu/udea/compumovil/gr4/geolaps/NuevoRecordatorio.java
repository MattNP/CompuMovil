package co.edu.udea.compumovil.gr4.geolaps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class NuevoRecordatorio extends AppCompatActivity {

    public static final String FORMATO_FECHA = "dd-MMM-yyyy";
    public static final String FORMATO_HORA = "HH:mm";
    private static final int TIPO_LUGAR = 1;
    private static final int TIPO_RECORDATORIO = 2;
    public static final int REQUEST_MAP = 4236;
    public static final String ID_NUEVO = "idNuevo";
    public static final String ID_EDITAR="idEditar";

    private Spinner spinner_tipo_recordatorio, spinner_tipo_lugar;
    private EditText txt_titulo, txt_descripcion;
    private TextView txt_fecha, txt_hora, txt_lugar;
    private SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
    private SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
    private final Calendar calendar = Calendar.getInstance();
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;
    private double latitud, longitud, latitudActual, longitudActual;
    private String tipo_recordario, tipo_lugar, titulo, lugar, descripcion, fecha, hora, timestamp;
    private Recordatorio recordatorio;



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
        recordatorio=null;
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if(intent.hasExtra(Dashboard.CURRENT_LONGITUDE) && intent.hasExtra(Dashboard.CURRENT_LATITUDE)) {
                longitudActual = intent.getDoubleExtra(Dashboard.CURRENT_LONGITUDE, 0);
                latitudActual = intent.getDoubleExtra(Dashboard.CURRENT_LATITUDE, 0);
            }
            if(intent.hasExtra(Dashboard.RECORDATORIO_SELECCIONADO)){
                recordatorio=intent.getParcelableExtra(Dashboard.RECORDATORIO_SELECCIONADO);
                if(recordatorio != null){
                    txt_titulo.setText(recordatorio.getNombre());
                    txt_descripcion.setText(recordatorio.getDescripcion());
                    txt_hora.setText(recordatorio.getHora_limite());
                    txt_fecha.setText(recordatorio.getFecha_limite());
                    txt_lugar.setText(recordatorio.getLugar().getNombre());
                }
            }
        }

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
                intentMap.putExtra(Dashboard.CURRENT_LATITUDE, latitudActual);
                intentMap.putExtra(Dashboard.CURRENT_LONGITUDE, longitudActual);
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
            long id;
            if(recordatorio==null){
                 id = db.insertWithOnConflict(GeoLapsContract.TABLE_RECORDATORIO, null, valuesRecordatorio,
                        SQLiteDatabase.CONFLICT_IGNORE);
                Intent intent = new Intent();
                intent.putExtra(ID_NUEVO, id);
                setResult(Dashboard.REQUEST_NUEVO, intent);
            }else{
                id=recordatorio.getId();
                db.update(GeoLapsContract.TABLE_RECORDATORIO,valuesRecordatorio,GeoLapsContract.ColumnaRecordatorio.ID + "=" +id,null);
                Intent intent = new Intent();
                intent.putExtra(ID_EDITAR, id);
                setResult(RecordatorioActivity.REQUEST_EDITAR, intent);
                Log.d("startActivityForResult",recordatorio.getNombre()+ " nuevo");
            }

            dbHelper.close();

            Toast.makeText(this, getString(R.string.recordatorio_guardado), Toast.LENGTH_SHORT).show();



            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_MAP && data != null && data.hasExtra(MapActivity.LATITUD) && data.hasExtra(MapActivity.LONGITUD)){
            latitud = data.getDoubleExtra(MapActivity.LATITUD, 0);
            longitud = data.getDoubleExtra(MapActivity.LONGITUD, 0);
            String nombreLugar = "";
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> listaDirecciones = geocoder.getFromLocation(latitud, longitud, 1);
                if(listaDirecciones.size() != 0) {
                    nombreLugar = listaDirecciones.get(0).getAddressLine(0);
                } else {
                    Toast.makeText(this, "El lugar no tiene dirección", Toast.LENGTH_SHORT).show();
                    DecimalFormat numberFormat = new DecimalFormat("#.####");
                    nombreLugar = numberFormat.format(latitud) + ", " + numberFormat.format(longitud);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            lugar = nombreLugar;
            txt_lugar.setText(lugar);
        }
    }

}

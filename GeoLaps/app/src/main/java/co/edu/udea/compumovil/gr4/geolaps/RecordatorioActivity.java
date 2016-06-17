package co.edu.udea.compumovil.gr4.geolaps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.edu.udea.compumovil.gr4.geolaps.database.DBHelper;
import co.edu.udea.compumovil.gr4.geolaps.database.DBUtil;
import co.edu.udea.compumovil.gr4.geolaps.database.GeoLapsContract;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class RecordatorioActivity extends AppCompatActivity {

    private TextView txt_nombre_recordatorio, txt_nombre_lugar_recordatorio, txt_hora_limite, txt_fecha_limite, txt_descripcion, txt_tipo_recordatorio;
    private ImageView img_mapa;
    private Recordatorio recordatorio;
    public static final int REQUEST_EDITAR=0354;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private static final String IMG_URL = "http://maps.google.com/maps/api/staticmap?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        Intent intent = getIntent();
        recordatorio = null;
        txt_nombre_recordatorio = (TextView)findViewById(R.id.txt_nombre_recordatorio);
        txt_nombre_lugar_recordatorio = (TextView)findViewById(R.id.txt_lugar_recordatorio);
        txt_fecha_limite = (TextView)findViewById(R.id.txt_fecha_limite);
        txt_hora_limite = (TextView)findViewById(R.id.txt_hora_limite);
        txt_descripcion = (TextView)findViewById(R.id.txt_descripcion_info);
        txt_tipo_recordatorio = (TextView)findViewById(R.id.txt_tipo_recordatorio_info);
        img_mapa = (ImageView)findViewById(R.id.img_mapa);

        if (intent.hasExtra(Dashboard.RECORDATORIO_SELECCIONADO)) {
            recordatorio = intent.getParcelableExtra(Dashboard.RECORDATORIO_SELECCIONADO);
            txt_nombre_recordatorio.setText(recordatorio.getNombre());
            txt_nombre_lugar_recordatorio.setText(recordatorio.getLugares().get(0).getNombre());

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(recordatorio.getFecha_limite());

            SimpleDateFormat formatoFecha = new SimpleDateFormat(NuevoRecordatorio.FORMATO_FECHA);
            SimpleDateFormat formatoHora = new SimpleDateFormat(NuevoRecordatorio.FORMATO_HORA);

            txt_hora_limite.setText(formatoHora.format(c.getTime()));
            txt_fecha_limite.setText(formatoFecha.format(c.getTime()));
            txt_descripcion.setText(recordatorio.getDescripcion());
            txt_tipo_recordatorio.setText(recordatorio.getTipo().getTipo());
        }
        dbHelper = new DBHelper(this);
        getImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recordatorio_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.detalles_recordatorio) + " " + recordatorio.getNombre());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editarRecordatorio:
                editarRecordatorio();
                return true;
            case R.id.eliminarRecordatorio:
                eliminarRecordatorio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getImage() {

        RequestQueue queue = Volley.newRequestQueue(this);

        double lat = recordatorio.getLugares().get(0).getLatitud();
        double lng = recordatorio.getLugares().get(0).getLongitud();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        String img_url = IMG_URL + "center=" + lat + "," + lng + "&zoom=16&size=640x640&markers=color:red|" + lat + "," + lng + "&mobile=true&sensor=false";
        Log.d("getImage", img_url);
        ImageRequest request = new ImageRequest(img_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        img_mapa.setImageBitmap(bitmap);
                    }
                }, 640, 640, null, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("getImage", "Error cargando la imagen");
                    }
                });
        // Access the RequestQueue through your singleton class.
        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.hasExtra(NuevoRecordatorio.ID_EDITAR)&&(requestCode==REQUEST_EDITAR)){
            long id=data.getLongExtra(NuevoRecordatorio.ID_EDITAR,0);
            db = dbHelper.getWritableDatabase();

            Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                    null,
                    GeoLapsContract.ColumnaRecordatorio.ID + "=?",
                    new String[] {Long.toString(id)},
                    null,
                    null,
                    null);

            if (cursorRecordatorio.moveToFirst()) {
                recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, this);
            }
            db.close();
            txt_nombre_recordatorio.setText(recordatorio.getNombre());
            txt_nombre_lugar_recordatorio.setText(recordatorio.getLugares().get(0).getNombre());

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(recordatorio.getFecha_limite());

            SimpleDateFormat formatoFecha = new SimpleDateFormat(NuevoRecordatorio.FORMATO_FECHA);
            SimpleDateFormat formatoHora = new SimpleDateFormat(NuevoRecordatorio.FORMATO_HORA);

            txt_hora_limite.setText(formatoHora.format(c.getTime()));
            txt_fecha_limite.setText(formatoFecha.format(c.getTime()));

            txt_descripcion.setText(recordatorio.getDescripcion());
            Log.d("startActivityForResult",recordatorio.getNombre() + " Activity");
            onResume();

        }

    }

    private void editarRecordatorio() {
        Intent intent=new Intent(this,NuevoRecordatorio.class);
        intent.putExtra(Dashboard.RECORDATORIO_SELECCIONADO,recordatorio);
        startActivityForResult(intent,REQUEST_EDITAR);
    }

    private void eliminarRecordatorio() {
        Log.d("eliminarRecordatorio", "Eliminar");
        DBHelper dbHelper = new DBHelper(this);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(GeoLapsContract.TABLE_RECORDATORIO,
                GeoLapsContract.ColumnaRecordatorio.ID + "=" + recordatorio.getId(),
                null);
        db.close();

        Intent intent = new Intent();
        intent.putExtra(Dashboard.RECORDATORIO_ELIMINADO, recordatorio);
        setResult(Dashboard.REQUEST_ELIMINAR, intent);
        finish();
    }


}

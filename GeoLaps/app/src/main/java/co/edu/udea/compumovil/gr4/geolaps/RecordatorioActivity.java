package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

public class RecordatorioActivity extends AppCompatActivity {

    private TextView txt_nombre_recordatorio;
    private TextView txt_nombre_lugar_recordatorio;
    private TextView txt_fecha_limite;
    private TextView txt_hora_limite;
    private TextView txt_descripcion;
    private Recordatorio recordatorio;


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

        if (intent.hasExtra(Dashboard.RECORDATORIO_SELECCIONADO)) {
            recordatorio = intent.getParcelableExtra(Dashboard.RECORDATORIO_SELECCIONADO);
            txt_nombre_recordatorio.setText(recordatorio.getNombre());
            txt_nombre_lugar_recordatorio.setText(recordatorio.getLugar().getNombre());
            txt_fecha_limite.setText(recordatorio.getFecha_limite());
            txt_hora_limite.setText(recordatorio.getHora_limite());
            txt_descripcion.setText(recordatorio.getDescripcion());
        }

        //http://maps.google.com/maps/api/staticmap?center=6.2856213,-75.5580297&zoom=16&size=310x310&markers=color:red|6.2856213,-75.5580297&mobile=true&sensor=false
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recordatorio_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editarRecordatorio:
                return true;
            case R.id.guardarRecordatorio:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

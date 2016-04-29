package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NuevoRecordatorio extends AppCompatActivity {

    Button ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recordatorio);
        ingresar=(Button)findViewById(R.id.btn_abrir_mapa);

    }
    public void onClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}

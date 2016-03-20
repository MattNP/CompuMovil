package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NuevaCarreraActivity extends AppCompatActivity {

    private EditText txt_nombre, txt_distancia, txt_lugar, txt_fecha, txt_telefono, txt_correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_carrera);

        txt_nombre = (EditText)findViewById(R.id.nombreCarrera);
        txt_distancia = (EditText)findViewById(R.id.distanciaCarrera);
        txt_lugar = (EditText)findViewById(R.id.lugarCarrera);
        txt_fecha = (EditText)findViewById(R.id.fechaCarrera);
        txt_telefono = (EditText)findViewById(R.id.telefonoCarrera);
        txt_correo = (EditText)findViewById(R.id.correoCarrera);
    }

    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.guardarCarrera:

                String nombreCarrera = txt_nombre.getText().toString();
                String distanciaCarrera = txt_distancia.getText().toString();
                String lugarCarrera = txt_lugar.getText().toString();
                String fechaCarrera = txt_fecha.getText().toString();
                String telefonoCarrera = txt_telefono.getText().toString();
                String correoCarrera = txt_correo.getText().toString();

                DbHelper dbHelper = new DbHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREF_USUARIO, MODE_PRIVATE);
                int usuarioId = sharedPreferences.getInt(MainActivity.ID_USUARIO, -1);

                ContentValues values = new ContentValues();
                values.put(CarrerasContract.ColumnaCarrera.NOMBRE, nombreCarrera);
                values.put(CarrerasContract.ColumnaCarrera.UID, usuarioId);
                values.put(CarrerasContract.ColumnaCarrera.DISTANCIA, distanciaCarrera);
                values.put(CarrerasContract.ColumnaCarrera.LUGAR, lugarCarrera);
                values.put(CarrerasContract.ColumnaCarrera.FECHA, fechaCarrera);
                values.put(CarrerasContract.ColumnaCarrera.TELEFONO, telefonoCarrera);
                values.put(CarrerasContract.ColumnaCarrera.CORREO, correoCarrera);

                db.insertWithOnConflict(CarrerasContract.TABLE_CARRERA, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
                dbHelper.close();


                Toast.makeText(this, "Guardar carrera", Toast.LENGTH_SHORT).show();
                finish();

                break;
        }
    }
}

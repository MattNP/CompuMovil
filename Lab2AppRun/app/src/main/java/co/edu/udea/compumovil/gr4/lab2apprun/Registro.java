package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity {
    private EditText usuario, correo, clave, clave2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usuario = (EditText)findViewById(R.id.usuarioR);
        clave = (EditText)findViewById(R.id.claveR);
        clave2 = (EditText)findViewById(R.id.confirmarC);
        correo = (EditText)findViewById(R.id.correo);
    }
    public void registrar(View v){

        String usuarioR = usuario.getText().toString();
        String claveR = clave.getText().toString();
        String claveR2 = clave2.getText().toString();
        String correoR = correo.getText().toString();



        //Añade los datos a la base de datos
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CarrerasContract.ColumnaUsuario.USUARIO, usuarioR);
        values.put(CarrerasContract.ColumnaUsuario.CORREO, correoR);
        values.put(CarrerasContract.ColumnaUsuario.CLAVE, claveR);

        db.insertWithOnConflict(CarrerasContract.TABLE_USUARIO, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);

        dbHelper.close();


        Intent intent=new Intent();
        intent.putExtra(MainActivity.USER, usuarioR);
        intent.putExtra(MainActivity.CLAVE, claveR);


        setResult(MainActivity.REQUEST, intent);

        finish();

    }
}

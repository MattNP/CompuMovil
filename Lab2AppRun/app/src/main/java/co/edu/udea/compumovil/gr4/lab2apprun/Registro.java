package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {
    private EditText txt_usuario, txt_correo, txt_clave, txt_clave2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txt_usuario = (EditText)findViewById(R.id.usuarioR);
        txt_clave = (EditText)findViewById(R.id.claveR);
        txt_clave2 = (EditText)findViewById(R.id.confirmarC);
        txt_correo = (EditText)findViewById(R.id.correo);
    }

    public void registrar(View v) {

        String usuario = txt_usuario.getText().toString();
        String clave = txt_clave.getText().toString();
        String clave2 = txt_clave2.getText().toString();
        String correo = txt_correo.getText().toString();

        if((usuario.equals(""))||(clave.equals(""))||(clave2.equals(""))||(correo.equals(""))){
            Toast.makeText(this,"Todos los datos son obligatorios",Toast.LENGTH_SHORT).show();
        }else

        if (clave.equals(clave2)) {

            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursorUsuario = db.query(CarrerasContract.TABLE_USUARIO,
                    null,
                    CarrerasContract.ColumnaUsuario.USUARIO + "=?",
                    new String[]{usuario},
                    null,
                    null,
                    null);

            Cursor cursorCorreo = db.query(CarrerasContract.TABLE_USUARIO,
                    null,
                    CarrerasContract.ColumnaUsuario.CORREO + "=?",
                    new String[] {correo},
                    null,
                    null,
                    null);

            if (cursorUsuario.moveToFirst()) {
                Toast.makeText(this,"El usuario ya está en uso",Toast.LENGTH_SHORT).show();
                cursorUsuario.close();
                cursorCorreo.close();
            } else {

                if (cursorCorreo.moveToFirst()) {
                    Toast.makeText(this,"El correo ya está en uso",Toast.LENGTH_SHORT).show();
                    cursorCorreo.close();
                } else {
                    //Añade los datos a la base de datos
                    ContentValues values = new ContentValues();
                    values.put(CarrerasContract.ColumnaUsuario.USUARIO, usuario);
                    values.put(CarrerasContract.ColumnaUsuario.CORREO, correo);
                    values.put(CarrerasContract.ColumnaUsuario.CLAVE, clave);

                    db.insertWithOnConflict(CarrerasContract.TABLE_USUARIO, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);
                    dbHelper.close();

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.USER, usuario);
                    intent.putExtra(MainActivity.CLAVE, clave);

                    setResult(MainActivity.REQUEST, intent);

                    finish();
                }
            }
        } else {
            Toast.makeText(this,"Las contraseñas deben coincidir",Toast.LENGTH_SHORT).show();
        }
    }
}

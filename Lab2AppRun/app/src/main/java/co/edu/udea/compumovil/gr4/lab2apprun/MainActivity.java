package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST=1;
    public static final String USER="USUARIO";
    public static final String CLAVE="CLAVE";
    public static final String CORREO="CORREO";
    private EditText txt_user, txt_clave;
    public static final String PREF_USUARIO="pref_usuario";
    public static final String ID_USUARIO = "id_usuario";
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_user = (EditText) findViewById(R.id.usuarioIngreso);
        txt_clave =(EditText) findViewById(R.id.claveIngreso);

        SharedPreferences sharedPref = getSharedPreferences(PREF_USUARIO, Context.MODE_PRIVATE);
        idUsuario = sharedPref.getInt(ID_USUARIO, -1);
        Log.d("MainActivity_onCreate", "ID user = " + idUsuario);

        if(idUsuario != -1) {
            Intent intent = new Intent(this, Eventos.class);
            intent.putExtra(ID_USUARIO, idUsuario);

            startActivity(intent);
            finish();
        }

    }

    public void registrar(View v){
        Intent intent= new Intent(this,Registro.class);
        startActivityForResult(intent, REQUEST);
    }

    public void ingresar(View v){

        String usuario = txt_user.getText().toString();
        String clave = txt_clave.getText().toString();

        if(usuario.equals("") || clave.equals("")) {
            Toast.makeText(this, "Todos los datos son obligatorios", Toast.LENGTH_SHORT).show();
        } else {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(CarrerasContract.TABLE_USUARIO,
                    null,
                    CarrerasContract.ColumnaUsuario.USUARIO + "=?" +
                            " and " + CarrerasContract.ColumnaUsuario.CLAVE + "=?",
                    new String[] {usuario, clave},
                    null,
                    null,
                    null);

            if (cursor.moveToFirst()) {

                SharedPreferences sharedPref = getSharedPreferences(PREF_USUARIO, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.putInt(ID_USUARIO, cursor.getInt(cursor.getColumnIndex(CarrerasContract.ColumnaUsuario.ID)));
                editor.putInt(USER, cursor.getInt(cursor.getColumnIndex(CarrerasContract.ColumnaUsuario.USUARIO)));
                editor.putInt(CORREO, cursor.getInt(cursor.getColumnIndex(CarrerasContract.ColumnaUsuario.CORREO)));
                editor.commit();

                Intent intent = new Intent(this,Eventos.class);
                intent.putExtra(ID_USUARIO, cursor.getInt(0));

                startActivityForResult(intent, REQUEST);
                cursor.close();
                dbHelper.close();
                finish();
            } else {
                txt_clave.setText("");
                Toast.makeText(this, "El usuario no existe o la contraseña no es correcta", Toast.LENGTH_SHORT).show();
                dbHelper.close();
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST && data != null){
            String userL = data.getStringExtra(USER);
            String claveL = data.getStringExtra(CLAVE);
            txt_user.setText(userL);
            txt_clave.setText(claveL);
        }
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
    }
}

package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    static final int REQUEST=1;
    public static final String USER="USUARIO";
    public static final String CLAVE="CLAVE";
    private EditText txt_user, txt_clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_user = (EditText) findViewById(R.id.usuario_Log);
        txt_clave =(EditText) findViewById(R.id.contraseña);

    }

    public void onClick(View v){

        Intent intent;

        switch (v.getId()) {
            case R.id.btn_registroLogin:
                intent = new Intent(this, Registro.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                intent= new Intent(this,Dashboard.class);
                startActivity(intent);
                break;
        }



        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

    }
}

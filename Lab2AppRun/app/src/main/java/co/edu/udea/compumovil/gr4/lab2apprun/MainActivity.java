package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST=1;
    static final String USER="USER";
    static final String CLAVE="CLAVE";
    private EditText user, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user= (EditText) findViewById(R.id.usuario);
        clave=(EditText) findViewById(R.id.clave);

    }
    public void Lanzar(View v){
        Intent intent= new Intent(this,Registro.class);
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST){

            String userL=data.getStringExtra(USER);
            String claveL=data.getStringExtra(CLAVE);
            user.setText(userL);
            clave.setText(claveL);


        }
    }
    public void ingresar(View v){
        Intent intent =new Intent(this,Eventos.class);
        startActivityForResult(intent,REQUEST);

    }
}

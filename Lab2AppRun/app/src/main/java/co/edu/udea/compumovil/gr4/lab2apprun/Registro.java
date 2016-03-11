package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity {
    private EditText usuario,clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usuario = (EditText)findViewById(R.id.usuarioR);
        clave = (EditText)findViewById(R.id.claveR);
    }
    public void volver(View v){

        String usuarioR = usuario.getText().toString();
        String claveR=clave.getText().toString();
        Intent intent=new Intent();
        intent.putExtra(MainActivity.USER, usuarioR);
        intent.putExtra(MainActivity.CLAVE, claveR);

        setResult(MainActivity.REQUEST, intent);

        finish();//finishing activity

    }
}

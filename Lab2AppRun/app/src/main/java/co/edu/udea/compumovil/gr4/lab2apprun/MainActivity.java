package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Lanzar(View v){
        Intent intent= new Intent(this,Registro.class);
        startActivity(intent);
    }
}

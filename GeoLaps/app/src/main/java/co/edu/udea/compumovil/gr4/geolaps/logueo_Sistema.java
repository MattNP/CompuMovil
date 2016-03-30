package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class logueo_Sistema extends AppCompatActivity {

    static final int REQUEST=1;
    public static final String USER="USUARIO";
    public static final String CLAVE="CLAVE";
    private EditText txt_user, txt_clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo__sistema);

        txt_user = (EditText) findViewById(R.id.usuario_Log);
        txt_clave =(EditText) findViewById(R.id.contraseña);

    }

    public void Ingresar(View v){

        Intent intent= new Intent(this,CajaPrincipal.class);
        startActivity(intent);

    }
}

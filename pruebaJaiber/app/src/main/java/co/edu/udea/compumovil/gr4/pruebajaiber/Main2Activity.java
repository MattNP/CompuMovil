package co.edu.udea.compumovil.gr4.pruebajaiber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.security.Key;

public class Main2Activity extends AppCompatActivity {
    private TextView recv;
    public static final  String RECIBE="sdhafjk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recv=(TextView)findViewById(R.id.textView2);
        Intent intent = getIntent();
        String mensaje=intent.getStringExtra(MainActivity.KEY);
        recv.setText(mensaje);
    }

    public void finish(){

        Intent data=new Intent();
        data.putExtra(RECIBE,"Mensaje de retorno");
        setResult(RESULT_OK,data);
        super.finish();
    }

}

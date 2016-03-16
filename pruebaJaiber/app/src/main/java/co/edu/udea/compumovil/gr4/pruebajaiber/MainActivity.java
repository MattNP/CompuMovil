package co.edu.udea.compumovil.gr4.pruebajaiber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText envio;
    public static String KEY="mensaje";
    public static int REQUEST=45;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        envio=(EditText)findViewById(R.id.Envio);
    }
    public void onClick(View v){
        Intent intent= new Intent(this, Main2Activity.class);
        String mensaje=envio.getText().toString();
        intent.putExtra(KEY,mensaje);
        startActivityForResult(intent, REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode == RESULT_OK)&&(requestCode == REQUEST)){
            if(data.hasExtra(Main2Activity.RECIBE)){
                Toast.makeText(this,data.getStringExtra(Main2Activity.RECIBE),Toast.LENGTH_SHORT).show();
            }
        }
    }
}

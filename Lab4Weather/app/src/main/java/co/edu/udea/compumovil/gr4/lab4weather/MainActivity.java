package co.edu.udea.compumovil.gr4.lab4weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView txt_ciudades;
    private Button btn_clima;
    private String ciudades[], idCiudades[];
    public static String ID_CIUDAD = "idCiudad";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBarMain = (Toolbar) findViewById(R.id.appBarMain);
        setSupportActionBar(appBarMain);


        ciudades = getResources().getStringArray(R.array.ciudades);
        idCiudades = getResources().getStringArray(R.array.idCiudades);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ciudades);

        txt_ciudades = (AutoCompleteTextView)findViewById(R.id.txt_ciudades);
        txt_ciudades.setAdapter(adapter);
        btn_clima = (Button)findViewById(R.id.btn_clima);

        txt_ciudades.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_clima.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClick(View view){

        switch (view.getId()) {
            case R.id.btn_clima:
                int index = Arrays.asList(ciudades).indexOf(txt_ciudades.getText().toString());
                if(index != -1) {
                    String idCiudad = idCiudades[index];
                    Intent intent = new Intent(this, WeatherActivity.class);
                    intent.putExtra(ID_CIUDAD, idCiudad);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "La ciudad no se encuentra", Toast.LENGTH_SHORT).show();
                    txt_ciudades.setText("");
                }
                break;
        }

    }





}

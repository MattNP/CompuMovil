package co.edu.udea.compumovil.gr4.lab1ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static RadioGroup rg;
    private static RadioButton rb;

    Spinner sp;
    Button btn;
    TextView txt_Calendar,txt_apellido,txt_nombre,txt_telefono,txt_correo,txt_direccion,
    txt_fecha,txt_sexo,txt_aficion,txt_escritorio,txt_pais;
    int year_x, month_x, day_x;
    static final int Dialog_id = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        final Calendar cal= Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);

        AutoCompleteTextView textView=(AutoCompleteTextView)findViewById(R.id.pais);
        Locale[] locales= Locale.getAvailableLocales();
        ArrayList<String> countries=new ArrayList<String>();
        for(Locale locale:locales){
            String country = locale.getDisplayCountry();
            if(country.trim().length()>0 && !countries.contains(country)){
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);


        Spinner spinner=(Spinner)findViewById(R.id.aficiones);
        ArrayAdapter<CharSequence> adapter1= ArrayAdapter.createFromResource(this,
                R.array.aficiones_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        onClick();

        showDialogMostrarFecha();

    }

    public void onClick(){
        rg=(RadioGroup)findViewById(R.id.seleccionar_genero);
        btn=(Button)findViewById(R.id.mostrar);
        txt_escritorio=(TextView)findViewById(R.id.escritorio);
        sp=(Spinner)findViewById(R.id.aficiones);



        btn.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        int seleccion_id=rg.getCheckedRadioButtonId();
                        rb=(RadioButton)findViewById(seleccion_id);
                        txt_nombre=(TextView)findViewById(R.id.nombre);
                        txt_apellido=(TextView)findViewById(R.id.apellido);
                        txt_pais=(TextView)findViewById(R.id.pais);
                        txt_telefono=(TextView)findViewById(R.id.telefono);
                        txt_direccion=(TextView)findViewById(R.id.direccion);
                        txt_correo=(TextView)findViewById(R.id.correo);
                        txt_aficion=(TextView)findViewById(R.id.aficiones);


                        txt_escritorio.setText("Nombre= "+txt_nombre.getText()+"\n"+
                                "Sexo= "+rb.getText().toString()+"\n"+
                                "Fecha de nacimiendo= "+txt_Calendar.getText().toString()+"\n"+
                                "Pais= "+txt_pais.getText().toString()+"\n"+
                                "Telefono= "+txt_telefono.getText().toString()+"\n"+
                                "Direccion= "+txt_direccion.getText().toString()+"\n"+
                                "Correo= "+txt_correo.getText().toString()+"\n"+
                                "Hobbie= "+sp.getOnItemSelectedListener().toString());

                    }
                }
        );
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int posicion, long id){

    }

    public void showDialogMostrarFecha() {
        txt_Calendar = (TextView) findViewById(R.id.fecha_nacimiento);
        txt_Calendar.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDialog(Dialog_id);

                    }
                }
        );

    }

    protected Dialog onCreateDialog(int id){
        if(id == Dialog_id)
            return new DatePickerDialog(this,dpickerListener,year_x,month_x,day_x);
        return null;
    }

    private  DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener(){

                public void onDateSet(DatePicker view, int year, int monthyear, int dayofmonth){
                    year_x=year;
                    month_x=monthyear + 1;
                    day_x=dayofmonth;
                    txt_Calendar.setText(year_x + "/" + month_x + "/" + day_x);
                    Toast.makeText(MainActivity.this,year_x + "/" + month_x + "/" + day_x,Toast.LENGTH_LONG).show();
                }
            };
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://co.edu.udea.compumovil.gr4.lab1ui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://co.edu.udea.compumovil.gr4.lab1ui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

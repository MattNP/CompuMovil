package co.edu.udea.compumovil.gr4.lab4weather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import co.edu.udea.compumovil.gr4.lab4weather.model.WeatherData;

public class WeatherActivity extends AppCompatActivity {

    private final String TAG = "Volley";
    private static final String APPID = "&APPID=52e359532a13971b030578a1c0e35e07";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    private String URL,  REQUEST, idCiudad;
    private WeatherData weatherData = null;
    private RequestQueue queue;
    private Gson gson;

    ImageView img_weatherIcon;
    ProgressDialog progressDialog;
    TextView txt_ciudad, txt_temperatura, txt_humedad, txt_descripcion, txt_viento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBarWeather);
        setSupportActionBar(myToolbar);

        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREF_USUARIO, MODE_PRIVATE);
        idCiudad = sharedPref.getString(MainActivity.ID_CIUDAD, "0");
        Log.d("Weather_onCreate", "ID ciudad = " + idCiudad);

        REQUEST = "/weather?q=" +  idCiudad + "&units=metric";
        URL = BASE_URL+REQUEST+APPID;
        Log.d("URL: ", URL);
        sendRequest();

        img_weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        txt_ciudad=(TextView) findViewById(R.id.txt_ciudad);
        txt_temperatura=(TextView) findViewById(R.id.txt_temperatura);
        txt_humedad=(TextView )findViewById(R.id.txt_porcentajeHumedad);
        txt_descripcion=(TextView )findViewById(R.id.txt_descripcion);
        txt_viento=(TextView )findViewById(R.id.txt_velocidadViento);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cambioCiudad) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cambioCiudad(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cambioCiudad) {
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
            SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREF_USUARIO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
            finish();
        }
    }

    private void sendRequest() {

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = response;
                        gson = new Gson();

                        JsonParser jsonParser = new JsonParser();
                        JsonObject jo = (JsonObject)jsonParser.parse(response.toString());
                        weatherData = gson.fromJson(jo, WeatherData.class);
                        if(weatherData != null) {
                            Log.d(TAG, "*weatherData: " + weatherData.getMain().getTemp());
                            showWeatherInfo();
                            Log.d(TAG, "weatherData != null");
                        } else {
                            Log.d(TAG, "**weatherData is null " );
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String myError = getString(R.string.error);
                        Toast.makeText(getBaseContext(), myError, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        queue.add(jsObjRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.cargando));
        progressDialog.show();
    }

    public void getImage(String icon) {
        String img_url = IMG_URL + icon + ".png";
        Log.d(TAG, img_url);
        ImageRequest request = new ImageRequest(img_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        img_weatherIcon.setImageBitmap(bitmap);
                    }
                }, 50, 50, null, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error cargando la imagen");
                    }
                });
        // Access the RequestQueue through your singleton class.
        queue.add(request);
    }

    private void showWeatherInfo() {
        getImage(weatherData.getWeather()[0].getIcon());
        txt_ciudad.setText(weatherData.getName() + ", " + weatherData.getSys().getCountry());
        txt_descripcion.setText(weatherData.getWeather()[0].getDescription());
        txt_temperatura.setText(Integer.toString((int)weatherData.getMain().getTemp()) + "\u00b0");
        txt_humedad.setText(Integer.toString((int)weatherData.getMain().getHumidity()) + "%");
        txt_viento.setText(Float.toString(weatherData.getWind().getSpeed()) + "m/s");
    }
}

package co.edu.udea.compumovil.gr4.lab4weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    private final String TAG = "Volley";

    private static final String APPID = "&APPID=52e359532a13971b030578a1c0e35e07";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5";
    private String URL;
    private String REQUEST;
    private JSONObject jsonObject = null;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        String idCiudad = intent.getStringExtra(MainActivity.ID_CIUDAD);

        REQUEST = "/weather?id="+  idCiudad;
        URL = BASE_URL+REQUEST+APPID;
        sendRequest();
    }

    private void sendRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject = response;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String myError = getString(R.string.error);
                        Toast.makeText(getBaseContext(), myError, Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(jsObjRequest);
    }
}

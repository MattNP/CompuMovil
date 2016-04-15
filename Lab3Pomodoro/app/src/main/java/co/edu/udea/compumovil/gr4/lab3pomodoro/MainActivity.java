package co.edu.udea.compumovil.gr4.lab3pomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_startPomodoro;
    private static final int RESULT_SETTINGS = 1;
    private static final String NSECONDS = "nSeconds";
    private final String TAG = "MyService";
    private Intent intentService, intentBroadcast;

    private int nPomodoros = 0;
    private boolean vibration, debugMode;
    private int longbreakTime, shortbreakTime;

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                String string = bundle.getString(ServiceTimer.FILEPATH);
//                int resultCode = bundle.getInt(ServiceTimer.RESULT);
//                if (resultCode == RESULT_OK) {
//                    Toast.makeText(MainActivity.this,
//                            "Download complete. Download URI: " + string,
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Download failed",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        intentService = new Intent(getBaseContext(), ServiceTimer.class);

        btn_startPomodoro = (Button)findViewById(R.id.btn_startPomodoro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserPreferences();
    }

    private void getUserPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sb = sharedPreferences.getString(OpcionesActivity.PREF_SHORTBREAK, "0");
        String lb = sharedPreferences.getString(OpcionesActivity.PREF_LONGBREAK, "0");

        vibration = sharedPreferences.getBoolean(OpcionesActivity.PREF_VIBRATION, true);
        shortbreakTime = Integer.valueOf(sb);
        longbreakTime = Integer.valueOf(lb);
        debugMode = sharedPreferences.getBoolean(OpcionesActivity.PREF_DEBUG, false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startPomodoro:
                if(btn_startPomodoro.getText().toString().equals(getResources().getString(R.string.startPomodoro))) {
                    //Botón dice "Iniciar Pomodoro"
                    iniciarPomodoro();
                } else {
                    //Botón dice "Detener Pomodoro"
                    detenerPomodoro();
                }

                break;
        }
    }

    private void iniciarPomodoro(){
        Log.v(TAG, "Start button");
        intentService.putExtra("MILISECONDS", 15000);
        intentService.putExtra("INTERVAL", 1000);
        startService(intentService);
        btn_startPomodoro.setText(R.string.stopPomodoro);
        nPomodoros++;
    }

    private void detenerPomodoro(){
        Log.v(TAG, "Stop button");
        stopService(intentService);
        btn_startPomodoro.setText(R.string.startPomodoro);
        nPomodoros--;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, OpcionesActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                break;
        }

    }
}

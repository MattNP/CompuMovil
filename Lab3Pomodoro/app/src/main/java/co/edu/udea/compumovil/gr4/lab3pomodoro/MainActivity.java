package co.edu.udea.compumovil.gr4.lab3pomodoro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private static final int RESULT_SETTINGS = 1;
    private final String TAG = "MainActivity";
    private Intent intentService;

    private TextView txt_reloj;
    private Button btn_startPomodoro;

    private int nPomodoros;
    private boolean isBreak = false;
    private boolean vibration, debugMode;
    private int longbreakTime, shortbreakTime, multiplier, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getUserPreferences();
        intentService = new Intent(getBaseContext(), ServiceTimer.class);

        txt_reloj = (TextView) findViewById(R.id.txt_timeLeft);
        btn_startPomodoro = (Button) findViewById(R.id.btn_startPomodoro);

        nPomodoros = 0;
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            if (intent.hasExtra(ServiceTimer.COUNTDOWN)) {
                long millisUntilFinished = intent.getLongExtra(ServiceTimer.COUNTDOWN, 0);
                Log.d(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                long millis = millisUntilFinished;
                txt_reloj.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
            }
            if(intent.hasExtra(ServiceTimer.FINISH)) {
                boolean timeUp = intent.getBooleanExtra(ServiceTimer.FINISH, false);
                if(timeUp) {
                    txt_reloj.setText("00:00");
                    timeUp();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(ServiceTimer.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.d(TAG, "Unregistered broacast receiver");
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, ServiceTimer.class));
        Log.d(TAG, "Stopped service");
        super.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startPomodoro:
                if (buttonStart()) {
                    //Botón dice "Iniciar..."
                    iniciarTimer();
                } else {
                    //Botón dice "Detener..."
                    detenerTimer();
                }
                break;
        }
    }

    private boolean buttonStart() {
        boolean startP = btn_startPomodoro.getText().toString().equals(getString(R.string.startPomodoro));
        boolean startL = btn_startPomodoro.getText().toString().equals(getString(R.string.startLongbreak));
        boolean startS = btn_startPomodoro.getText().toString().equals(getString(R.string.startShortbreak));
        return startP || startL || startS;
    }

    private void iniciarTimer() {
        Log.d(TAG, "Start button");

        if (isBreak) {
            if (nPomodoros % 4 == 0) {
                minutes = longbreakTime;
            } else {
                minutes = shortbreakTime;
            }
        } else {
            minutes = 25;
        }

        intentService.putExtra(ServiceTimer.MILLISECONDS, minutes * multiplier + 1000);
        intentService.putExtra(ServiceTimer.INTERVAL, 1000);
        intentService.putExtra(OpcionesActivity.PREF_VIBRATION, vibration);
        startService(intentService);

        updateButton(false);
    }

    private void timeUp() {

        if(!isBreak) {
            isBreak = true;
            nPomodoros++;
        } else {
            isBreak = false;
        }
        updateButton(true);
    }

    private void updateButton(boolean iniciar) {

        String textButton;
        if (iniciar) {
            if(isBreak) {
                if (nPomodoros % 4 == 0) {
                    textButton = getString(R.string.startLongbreak);
                } else {
                    textButton = getString(R.string.startShortbreak);
                }
            } else {
                textButton = getString(R.string.startPomodoro);
            }
        } else {
            if(isBreak) {
                if (nPomodoros % 4 == 0) {
                    textButton = getString(R.string.stopLongbreak);
                } else {
                    textButton = getString(R.string.stopShortbreak);
                }
            } else {
                textButton = getString(R.string.stopPomodoro);
            }
        }
        btn_startPomodoro.setText(textButton);
    }

    private void detenerTimer() {
        Log.d(TAG, "Stop button");
        stopService(intentService);
        updateButton(true);
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
                getUserPreferences();
                break;
        }
    }

    private void getUserPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sb = sharedPreferences.getString(OpcionesActivity.PREF_SHORTBREAK, "0");
        String lb = sharedPreferences.getString(OpcionesActivity.PREF_LONGBREAK, "0");

        vibration = sharedPreferences.getBoolean(OpcionesActivity.PREF_VIBRATION, true);
        shortbreakTime = Integer.valueOf(sb);
        longbreakTime = Integer.valueOf(lb);
        debugMode = sharedPreferences.getBoolean(OpcionesActivity.PREF_DEBUG, false);

        multiplier = 60000;

        if(debugMode) {
            multiplier = 1000;
        }
    }
}

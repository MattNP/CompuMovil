package co.edu.udea.compumovil.gr4.lab3pomodoro;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OpcionesActivity extends PreferenceActivity {

    final static String PREF_SHORTBREAK="prefShortbreak";
    final static String PREF_LONGBREAK="prefLongbreak";
    final static String PREF_VIBRATION="prefVibration";
    final static String PREF_DEBUG="prefDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new OpcionesFragment()).commit();
        addPreferencesFromResource(R.xml.settings);
    }


    public static class OpcionesFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}

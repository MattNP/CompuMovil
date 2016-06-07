package co.edu.udea.compumovil.gr4.geolaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RecordatorioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);

        //http://maps.google.com/maps/api/staticmap?center=6.2856213,-75.5580297&zoom=16&size=310x310&markers=color:red|6.2856213,-75.5580297&mobile=true&sensor=false
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recordatorio_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editarRecordatorio:
                return true;
            case R.id.guardarRecordatorio:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

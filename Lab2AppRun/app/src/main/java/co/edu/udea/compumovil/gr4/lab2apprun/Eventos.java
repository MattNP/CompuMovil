package co.edu.udea.compumovil.gr4.lab2apprun;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Eventos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AcercaFragment.OnFragmentInteractionListener,
        PerfilFragment.OnFragmentInteractionListener,
        CarrerasFragment.OnListFragmentInteractionListener{

    public static final String TAG_ACERCA = "acerca";
    private int idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NuevaCarreraActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREF_USUARIO, MODE_PRIVATE);
        idUser = sharedPref.getInt(MainActivity.ID_USUARIO, -1);
        Log.d("Eventos_onCreate", "ID user = " + idUser);


        Fragment fragment = new CarrerasFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesion) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        boolean FragmentTransaction=false;
        Fragment fragment=null;
        if (id == R.id.nav_carreras) {
            fragment = new CarrerasFragment();
            FragmentTransaction=true;

        } else if (id == R.id.nav_perfil) {
            fragment = new PerfilFragment();
            FragmentTransaction=true;

        } else if (id == R.id.nav_acerca) {
            fragment= new AcercaFragment();
            FragmentTransaction=true;

        }
        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void cerrarSesion(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cerrar_sesion) {
            Log.d("onCreate_clicCerrar", "Presionado");
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
            SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREF_USUARIO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Carrera item) {
        Toast.makeText(this, "Presionó una carrera", Toast.LENGTH_SHORT).show();
    }
}

package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MattNP on 17/03/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, CarrerasContract.DB_NAME, null, CarrerasContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlUsuario = String.format("create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s blob)",
                CarrerasContract.TABLE_USUARIO,
                CarrerasContract.ColumnaUsuario.ID,
                CarrerasContract.ColumnaUsuario.USUARIO,
                CarrerasContract.ColumnaUsuario.CORREO,
                CarrerasContract.ColumnaUsuario.CLAVE,
                CarrerasContract.ColumnaUsuario.FOTO);

        Log.d(TAG, "onCreate with SQL: " + sqlUsuario);
        db.execSQL(sqlUsuario);

        String sqlCarrera = String.format("" +
                        "create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s text, " +
                        "%s real, " +
                        "%s text, " +
                        "%s text, " +
                        "%s blob, " +
                        "%s text, " +
                        "%s text)",
                CarrerasContract.TABLE_CARRERA,
                CarrerasContract.ColumnaCarrera.ID,
                CarrerasContract.ColumnaCarrera.NOMBRE,
                CarrerasContract.ColumnaCarrera.DISTANCIA,
                CarrerasContract.ColumnaCarrera.LUGAR,
                CarrerasContract.ColumnaCarrera.FECHA,
                CarrerasContract.ColumnaCarrera.FOTO,
                CarrerasContract.ColumnaCarrera.TELEFONO,
                CarrerasContract.ColumnaCarrera.CORREO);

        Log.d(TAG, "onCreate with SQL: " + sqlCarrera);
        db.execSQL(sqlCarrera);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CarrerasContract.TABLE_USUARIO);
        db.execSQL("drop table if exists " + CarrerasContract.TABLE_CARRERA);
        onCreate(db);
    }
}

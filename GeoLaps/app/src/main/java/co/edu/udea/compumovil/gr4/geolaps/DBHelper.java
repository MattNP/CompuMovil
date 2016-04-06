package co.edu.udea.compumovil.gr4.geolaps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mateo.norena on 6/04/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, GeoLapsContract.DB_NAME, null, GeoLapsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlUsuario = String.format("create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s blob)",
                GeoLapsContract.TABLE_USUARIO,
                GeoLapsContract.ColumnaUsuario.ID,
                GeoLapsContract.ColumnaUsuario.USUARIO,
                GeoLapsContract.ColumnaUsuario.NOMBRE,
                GeoLapsContract.ColumnaUsuario.CORREO,
                GeoLapsContract.ColumnaUsuario.CLAVE,
                GeoLapsContract.ColumnaUsuario.FOTO);

        Log.d(TAG, "onCreate with SQL: " + sqlUsuario);
        db.execSQL(sqlUsuario);

        String sqlRecordatorio = String.format("" +
                        "create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s integer, " +
                        "%s integer, " +
                        "%s integer, " +
                        "%s text, " +
                        "%s date, " +
                        "%s time, " +
                        "%s datetime, " +
                        "%s text, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))" +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))" +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))",
                GeoLapsContract.TABLE_RECORDATORIO,
                GeoLapsContract.ColumnaRecordatorio.ID,
                GeoLapsContract.ColumnaRecordatorio.UID,
                GeoLapsContract.ColumnaRecordatorio.TIPO,
                GeoLapsContract.ColumnaRecordatorio.LUGAR,
                GeoLapsContract.ColumnaRecordatorio.NOMBRE,
                GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE,
                GeoLapsContract.ColumnaRecordatorio.HORA_LIMITE,
                GeoLapsContract.ColumnaRecordatorio.FECHA_CREACION,
                GeoLapsContract.ColumnaRecordatorio.NOTA,
                GeoLapsContract.ColumnaRecordatorio.UID,
                GeoLapsContract.TABLE_USUARIO,
                GeoLapsContract.ColumnaUsuario.ID,
                GeoLapsContract.ColumnaRecordatorio.TIPO,
                GeoLapsContract.TABLE_TIPO_RECORDATORIO,
                GeoLapsContract.ColumnaTipoRecordatorio.ID,
                GeoLapsContract.ColumnaRecordatorio.LUGAR,
                GeoLapsContract.TABLE_LUGAR,
                GeoLapsContract.ColumnaLugar.ID);

        Log.d(TAG, "onCreate with SQL: " + sqlRecordatorio);
        db.execSQL(sqlRecordatorio);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists " + CarrerasContract.TABLE_USUARIO);
        //db.execSQL("drop table if exists " + CarrerasContract.TABLE_CARRERA);
        onCreate(db);
    }
}

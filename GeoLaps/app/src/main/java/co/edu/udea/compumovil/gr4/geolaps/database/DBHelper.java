package co.edu.udea.compumovil.gr4.geolaps.database;

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

        String sqlTipoLugar = String.format("create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s text)",
                GeoLapsContract.TABLE_TIPO_LUGAR,
                GeoLapsContract.ColumnaTipoLugar.ID,
                GeoLapsContract.ColumnaTipoLugar.TIPO);

        Log.d(TAG, "onCreate with SQL: " + sqlTipoLugar);
        db.execSQL(sqlTipoLugar);

        String sqlTipoRecordatorio = String.format("create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s text)",
                GeoLapsContract.TABLE_TIPO_RECORDATORIO,
                GeoLapsContract.ColumnaTipoRecordatorio.ID,
                GeoLapsContract.ColumnaTipoRecordatorio.TIPO);

        Log.d(TAG, "onCreate with SQL: " + sqlTipoRecordatorio);
        db.execSQL(sqlTipoRecordatorio);

        String sqlLugar = String.format("create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s integer, " +
                        "%s integer, " +
                        "%s text, " +
                        "%s double, " +
                        "%s double, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))",
                GeoLapsContract.TABLE_LUGAR,
                GeoLapsContract.ColumnaLugar.ID,
                GeoLapsContract.ColumnaLugar.TIPO,
                GeoLapsContract.ColumnaLugar.RECORDATORIO,
                GeoLapsContract.ColumnaLugar.NOMBRE,
                GeoLapsContract.ColumnaLugar.LATITUD,
                GeoLapsContract.ColumnaLugar.LONGITUD,
                GeoLapsContract.ColumnaLugar.TIPO,
                GeoLapsContract.TABLE_TIPO_LUGAR,
                GeoLapsContract.ColumnaTipoLugar.ID,
                GeoLapsContract.ColumnaLugar.RECORDATORIO,
                GeoLapsContract.TABLE_RECORDATORIO,
                GeoLapsContract.ColumnaRecordatorio.ID);

        Log.d(TAG, "onCreate with SQL: " + sqlLugar);
        db.execSQL(sqlLugar);

        String sqlRecordatorio = String.format("" +
                        "create table %s " +
                        "(%s integer primary key autoincrement, " +
                        "%s integer, " +
                        "%s integer, " +
                        "%s text, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s text, " +
                        "%s integer, " +
                        "%s integer, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s))",
                GeoLapsContract.TABLE_RECORDATORIO,
                GeoLapsContract.ColumnaRecordatorio.ID,
                GeoLapsContract.ColumnaRecordatorio.UID,
                GeoLapsContract.ColumnaRecordatorio.TIPO,
                GeoLapsContract.ColumnaRecordatorio.NOMBRE,
                GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE,
                GeoLapsContract.ColumnaRecordatorio.TIMESTAMP,
                GeoLapsContract.ColumnaRecordatorio.DESCRIPCION,
                GeoLapsContract.ColumnaRecordatorio.COLOR,
                GeoLapsContract.ColumnaRecordatorio.ADENTRO,
                GeoLapsContract.ColumnaRecordatorio.UID,
                GeoLapsContract.TABLE_USUARIO,
                GeoLapsContract.ColumnaUsuario.ID,
                GeoLapsContract.ColumnaRecordatorio.TIPO,
                GeoLapsContract.TABLE_TIPO_RECORDATORIO,
                GeoLapsContract.ColumnaTipoRecordatorio.ID);

        Log.d(TAG, "onCreate with SQL: " + sqlRecordatorio);
        db.execSQL(sqlRecordatorio);

        String sqlAddTiposRecordatorio = String.format("INSERT INTO '%s' (%s) " +
                "SELECT '%s' AS '%s' " +
                "UNION ALL SELECT '%s' " +
                "UNION ALL SELECT '%s' " +
                "UNION ALL SELECT '%s' ",
                GeoLapsContract.TABLE_TIPO_RECORDATORIO,
                GeoLapsContract.ColumnaTipoRecordatorio.TIPO,
                "Diligencia",
                GeoLapsContract.ColumnaTipoRecordatorio.TIPO,
                "Compra",
                "Entretenimiento",
                "Otro");

        Log.d(TAG, "Insert with SQL: " + sqlAddTiposRecordatorio);
        db.execSQL(sqlAddTiposRecordatorio);

        String sqlAddTiposLugares = String.format("INSERT INTO '%s' " +
                        "(%s) " +
                        "SELECT '%s' " +
                        "AS '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' " +
                        "UNION ALL SELECT '%s' ",
                GeoLapsContract.TABLE_TIPO_LUGAR,
                GeoLapsContract.ColumnaTipoLugar.TIPO,
                "Notaria",
                GeoLapsContract.ColumnaTipoRecordatorio.TIPO,
                "Banco",
                "Centro de salud",
                "Supermercado",
                "Tienda",
                "Sala de cine",
                "Centro comercial",
                "Parque",
                "Restaurante",
                "Otro");

        Log.d(TAG, "Insert with SQL: " + sqlAddTiposLugares);
        db.execSQL(sqlAddTiposLugares);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + GeoLapsContract.TABLE_USUARIO);
        db.execSQL("drop table if exists " + GeoLapsContract.TABLE_TIPO_LUGAR);
        db.execSQL("drop table if exists " + GeoLapsContract.TABLE_TIPO_RECORDATORIO);
        db.execSQL("drop table if exists " + GeoLapsContract.TABLE_LUGAR);
        db.execSQL("drop table if exists " + GeoLapsContract.TABLE_RECORDATORIO);
        onCreate(db);
    }
}

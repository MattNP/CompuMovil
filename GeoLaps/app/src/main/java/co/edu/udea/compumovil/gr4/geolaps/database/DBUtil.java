package co.edu.udea.compumovil.gr4.geolaps.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.Dashboard;
import co.edu.udea.compumovil.gr4.geolaps.RecordatorioActivity;
import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;
import co.edu.udea.compumovil.gr4.geolaps.model.TipoRecordatorio;

/**
 * Created by MattNP on 5/06/2016.
 */
public class DBUtil {

    public static Recordatorio getRecordatorioFromCursor(Cursor cursorRecordatorio, Context context) {

        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setId(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.ID)));
        recordatorio.setUid(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.UID)));

        int idTipoR = cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIPO);



        TipoRecordatorio tipoR = getTipoFromId(cursorRecordatorio.getLong(idTipoR), context);

        Log.d("getRecordatorio", "TipoR: " + tipoR.getTipo());
        recordatorio.setTipo(tipoR);
        recordatorio.setLugares(getListaLugaresRecordatorio(recordatorio.getId(), context));
        recordatorio.setNombre(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.NOMBRE)));
        recordatorio.setFecha_limite(cursorRecordatorio.getLong(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE)));
        recordatorio.setTimestamp(cursorRecordatorio.getLong(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIMESTAMP)));
        recordatorio.setDescripcion(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.DESCRIPCION)));
        recordatorio.setColor(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.COLOR)));
        recordatorio.setAdentro(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.ADENTRO)));
        return recordatorio;
    }

    public static List<Lugar> getListaLugaresRecordatorio(long idRecordatorio, Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Lugar> lugares = new ArrayList<>();

        Cursor cursorLugar = db.query(GeoLapsContract.TABLE_LUGAR,
                null,
                GeoLapsContract.ColumnaLugar.RECORDATORIO + "=?",
                new String[] {Long.toString(idRecordatorio)},
                null,
                null,
                null);



        if (cursorLugar.moveToFirst()) {

            do {
                int lugarId = cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.ID));
                Lugar lugar = getLugarFromId(lugarId, context);
                lugares.add(lugar);
            } while(cursorLugar.moveToNext());
        }

        return lugares;
    }

    public static Lugar getLugarFromId(long id, Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorLugar = db.query(GeoLapsContract.TABLE_LUGAR,
                null,
                GeoLapsContract.ColumnaLugar.ID + "=?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);

        Lugar lugar = new Lugar();

        if (cursorLugar.moveToFirst()) {
            lugar.setId(cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.ID)));
            lugar.setNombre(cursorLugar.getString(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.NOMBRE)));
            lugar.setLatitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LATITUD)));
            lugar.setLongitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LONGITUD)));
        }

        return lugar;
    }

    public static List<Recordatorio> getRecordatoriosActivos(Context context) {

        List<Recordatorio>recordatoriosActivos = new ArrayList<>();

        long actual_time = Calendar.getInstance().getTimeInMillis();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                null,
                GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE + ">= ?",
                new String[] {Long.toString(actual_time)},
                null,
                null,
                null);

        Log.d("getRecordatorio", "recordatorios: " + Integer.toString(cursorRecordatorio.getCount()));

        if(cursorRecordatorio.moveToFirst()) {
            do {
                    Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, context);
                    recordatoriosActivos.add(recordatorio);

                } while (cursorRecordatorio.moveToNext());
        }

        db.close();
        return recordatoriosActivos;
    }


    public static List<Recordatorio> getRecordatoriosInactivos(Context context) {

        List<Recordatorio>recordatoriosActivos = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long actual_time = Calendar.getInstance().getTimeInMillis();

        Cursor cursorRecordatorio = db.query(GeoLapsContract.TABLE_RECORDATORIO,
                null,
                GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE + "< ?",
                new String[] {Long.toString(actual_time)},
                null,
                null,
                null);

        Log.d("getRecordatorio", "recordatorios: " + Integer.toString(cursorRecordatorio.getCount()));

        if(cursorRecordatorio.moveToFirst()) {
            do {
                Recordatorio recordatorio = DBUtil.getRecordatorioFromCursor(cursorRecordatorio, context);
                recordatoriosActivos.add(recordatorio);

            } while (cursorRecordatorio.moveToNext());
        }

        db.close();
        return recordatoriosActivos;
    }

    /*
    public static int insertarRecordatorio(Recordatorio recordatorio, Context context) {
    }
    */

    public static TipoRecordatorio getTipoFromId(long id, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorTipoRecordatorio = db.query(GeoLapsContract.TABLE_TIPO_RECORDATORIO,
                null,
                GeoLapsContract.ColumnaTipoRecordatorio.ID + "=?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);

        TipoRecordatorio tipoR = new TipoRecordatorio();

        if (cursorTipoRecordatorio.moveToFirst()) {
            tipoR.setId(cursorTipoRecordatorio.getLong(cursorTipoRecordatorio.getColumnIndex(GeoLapsContract.ColumnaTipoRecordatorio.ID)));
            tipoR.setTipo(cursorTipoRecordatorio.getString(cursorTipoRecordatorio.getColumnIndex(GeoLapsContract.ColumnaTipoRecordatorio.TIPO)));
        }

        return tipoR;
    }

    public static int actualizarEstado(long idRecordatorio, int valor, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valuesRecordatorio = new ContentValues();
        valuesRecordatorio.put(GeoLapsContract.ColumnaRecordatorio.ADENTRO, valor);

        return db.update(GeoLapsContract.TABLE_RECORDATORIO,valuesRecordatorio,GeoLapsContract.ColumnaRecordatorio.ID + "=" +idRecordatorio,null);


    }
}

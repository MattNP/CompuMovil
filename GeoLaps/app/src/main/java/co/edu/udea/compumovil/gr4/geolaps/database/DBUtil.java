package co.edu.udea.compumovil.gr4.geolaps.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.edu.udea.compumovil.gr4.geolaps.model.Lugar;
import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

/**
 * Created by MattNP on 5/06/2016.
 */
public class DBUtil {

    public static Recordatorio getRecordatorioFromCursor(Cursor cursorRecordatorio, Context context) {

        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setId(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.ID)));
        recordatorio.setUid(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.UID)));
        recordatorio.setTipo(cursorRecordatorio.getInt(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIPO)));

        long lugarId = cursorRecordatorio.getLong(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.LUGAR));
        recordatorio.setLugar(getLugarFromId(lugarId, context));

        recordatorio.setNombre(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.NOMBRE)));
        recordatorio.setFecha_limite(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.FECHA_LIMITE)));
        recordatorio.setHora_limite(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.HORA_LIMITE)));
        recordatorio.setTimestamp(cursorRecordatorio.getLong(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.TIMESTAMP)));
        recordatorio.setDescripcion(cursorRecordatorio.getString(cursorRecordatorio.getColumnIndex(GeoLapsContract.ColumnaRecordatorio.DESCRIPCION)));

        return recordatorio;
    }

    public static Lugar getLugarFromId(long id, Context context) {

        Lugar lugar = new Lugar();

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorLugar = db.query(GeoLapsContract.TABLE_LUGAR,
                null,
                GeoLapsContract.ColumnaLugar.ID + "=?",
                new String[] {Long.toString(id)},
                null,
                null,
                null);

        if (cursorLugar.moveToFirst()) {
            lugar.setId(cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.ID)));
            lugar.setTipo(Integer.toString(cursorLugar.getInt(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.TIPO))));
            lugar.setNombre(cursorLugar.getString(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.NOMBRE)));
            lugar.setLatitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LATITUD)));
            double lat = (cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LATITUD)));
            lugar.setLongitud(cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LONGITUD)));
            double lng = cursorLugar.getDouble(cursorLugar.getColumnIndex(GeoLapsContract.ColumnaLugar.LONGITUD));
        }

        return lugar;
    }
}

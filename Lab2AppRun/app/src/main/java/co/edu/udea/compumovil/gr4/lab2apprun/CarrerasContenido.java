package co.edu.udea.compumovil.gr4.lab2apprun;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MattNP on 19/03/2016.
 */
public class CarrerasContenido {

    private List<Carrera> carreras = new ArrayList<Carrera>();
    private Map<String, Carrera> carrerasMap = new HashMap<String, Carrera>();
    private Context context;

    public CarrerasContenido(Context context) {
        this.context = context;
    }

    public List<Carrera> getCarreras(int idUsuario) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CarrerasContract.TABLE_CARRERA,
                null,
                CarrerasContract.ColumnaCarrera.UID + "=?",
                new String[] {Integer.toOctalString(idUsuario)},
                null,
                null,
                null);

        Log.d("getCarreras", Integer.toString(cursor.getCount()));
        Log.d("getCarreras", Integer.toString(idUsuario));

        if (cursor.moveToFirst()) {
            do {
                Carrera carrera = new Carrera();
                carrera.setId(Integer.toString(cursor.getInt(cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.ID))));
                carrera.setNombre(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.NOMBRE))));
                carrera.setDistancia(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.DISTANCIA))));
                carrera.setLugar(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.LUGAR))));
                carrera.setFecha(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.FECHA))));
                carrera.setTelefono(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.TELEFONO))));
                carrera.setCorreo(cursor.getString((cursor.getColumnIndex(CarrerasContract.ColumnaCarrera.CORREO))));

                carreras.add(carrera);
                carrerasMap.put(carrera.getId(), carrera);

            } while (cursor.moveToNext());
        }

        return carreras;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}

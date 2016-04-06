package co.edu.udea.compumovil.gr4.geolaps;

import android.provider.BaseColumns;

/**
 * Created by mateo.norena on 6/04/16.
 */
public class GeoLapsContract {

    public static final String DB_NAME = "geolaps.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_RECORDATORIO = "recordatorio";

    public class ColumnaUsuario {
        public static final String ID = BaseColumns._ID;
        public static final String USUARIO = "usuario";
        public static final String CORREO = "correo";
        public static final String CLAVE = "clave";
        public static final String FOTO = "foto";
    }

    public class ColumnaRecordatorio {
        public static final String ID = BaseColumns._ID;
        public static final String UID = "usuarioID";
        public static final String NOMBRE = "nombre";
        public static final String DISTANCIA = "distancia";
        public static final String LUGAR = "lugar";
        public static final String FECHA = "fecha";
        public static final String FOTO = "foto";
        public static final String TELEFONO = "telefono";
        public static final String CORREO = "correo";
    }
}

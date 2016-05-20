package co.edu.udea.compumovil.gr4.geolaps.database;

import android.provider.BaseColumns;

/**
 * Created by mateo.norena on 6/04/16.
 */
public class GeoLapsContract {

    public static final String DB_NAME = "geolaps.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_RECORDATORIO = "recordatorio";
    public static final String TABLE_TIPO_RECORDATORIO = "tipo_recordatorio";
    public static final String TABLE_LUGAR = "lugar";
    public static final String TABLE_TIPO_LUGAR = "tipo_lugar";


    public class ColumnaUsuario {
        public static final String ID = BaseColumns._ID;
        public static final String USUARIO = "usuario";
        public static final String NOMBRE = "nombre";
        public static final String CORREO = "correo";
        public static final String CLAVE = "clave";
        public static final String FOTO = "foto";
    }

    public class ColumnaRecordatorio {
        public static final String ID = BaseColumns._ID;
        public static final String UID = "usuarioID";
        public static final String TIPO = "tipo";
        public static final String LUGAR = "lugar";
        public static final String NOMBRE = "nombre";
        public static final String FECHA_LIMITE = "fecha_limite";
        public static final String HORA_LIMITE = "hora_limite";
        public static final String TIMESTAMP = "timestamp";
        public static final String DESCRIPCION = "descripcion";
    }

    public class ColumnaTipoRecordatorio {
        public static final String ID = BaseColumns._ID;
        public static final String TIPO = "tipo";
    }

    public class ColumnaLugar {
        public static final String ID = BaseColumns._ID;
        public static final String TIPO = "tipo";
        public static final String NOMBRE = "nombre";
        public static final String TELEFONO = "telefono";
        public static final String CORREO = "correo";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String FOTO = "foto";
        public static final String DIRECCION = "direccion";
    }

    public class ColumnaTipoLugar {
        public static final String ID = BaseColumns._ID;
        public static final String TIPO = "tipo";
    }
}

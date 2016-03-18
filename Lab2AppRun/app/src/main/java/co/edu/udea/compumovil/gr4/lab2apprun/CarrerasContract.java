package co.edu.udea.compumovil.gr4.lab2apprun;

import android.provider.BaseColumns;

/**
 * Created by MattNP on 17/03/2016.
 */
public class CarrerasContract {

    public static final String DB_NAME = "carreras.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_CARRERA = "carrera";

    public class ColumnaUsuario {
        public static final String ID = BaseColumns._ID;
        public static final String USUARIO = "usuario";
        public static final String CORREO = "correo";
        public static final String CLAVE = "clave";
    }

    public class ColumnaCarrera {
        public static final String ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String DISTANCIA = "distancia";
        public static final String LUGAR = "lugar";
        public static final String FECHA = "fecha";
        public static final String FOTO = "foto";
        public static final String TELEFONO = "telefono";
        public static final String CORREO = "correo";
    }


}

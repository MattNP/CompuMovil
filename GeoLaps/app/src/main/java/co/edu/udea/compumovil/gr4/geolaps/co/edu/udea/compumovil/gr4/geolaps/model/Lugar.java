package co.edu.udea.compumovil.gr4.geolaps.co.edu.udea.compumovil.gr4.geolaps.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by MattNP on 5/05/2016.
 */
public class Lugar {

    private int id;
    private double latitud;
    private double longitud;
    private String nombre;
    private String tipo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

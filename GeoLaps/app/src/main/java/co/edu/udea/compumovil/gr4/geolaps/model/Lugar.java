package co.edu.udea.compumovil.gr4.geolaps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MattNP on 5/05/2016.
 */
public class Lugar implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.latitud);
        dest.writeDouble(this.longitud);
        dest.writeString(this.nombre);
        dest.writeString(this.tipo);
    }

    public Lugar() {
    }

    protected Lugar(Parcel in) {
        this.id = in.readInt();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.nombre = in.readString();
        this.tipo = in.readString();
    }

    public static final Parcelable.Creator<Lugar> CREATOR = new Parcelable.Creator<Lugar>() {
        @Override
        public Lugar createFromParcel(Parcel source) {
            return new Lugar(source);
        }

        @Override
        public Lugar[] newArray(int size) {
            return new Lugar[size];
        }
    };
}

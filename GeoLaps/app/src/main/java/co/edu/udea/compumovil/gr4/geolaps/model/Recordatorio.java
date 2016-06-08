package co.edu.udea.compumovil.gr4.geolaps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MattNP on 20/05/2016.
 */
public class Recordatorio implements Parcelable {

    private int id;
    private int uid;
    private int tipo;
    private Lugar lugar;
    private String nombre;
    private String fecha_limite;
    private String hora_limite;
    private long timestamp;
    private String descripcion;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getHora_limite() {
        return hora_limite;
    }

    public void setHora_limite(String hora_limite) {
        this.hora_limite = hora_limite;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.uid);
        dest.writeInt(this.tipo);
        dest.writeParcelable(this.lugar, flags);
        dest.writeString(this.nombre);
        dest.writeString(this.fecha_limite);
        dest.writeString(this.hora_limite);
        dest.writeLong(this.timestamp);
        dest.writeString(this.descripcion);
    }

    public Recordatorio() {
    }

    protected Recordatorio(Parcel in) {
        this.id = in.readInt();
        this.uid = in.readInt();
        this.tipo = in.readInt();
        this.lugar = in.readParcelable(Lugar.class.getClassLoader());
        this.nombre = in.readString();
        this.fecha_limite = in.readString();
        this.hora_limite = in.readString();
        this.timestamp = in.readLong();
        this.descripcion = in.readString();
    }

    public static final Parcelable.Creator<Recordatorio> CREATOR = new Parcelable.Creator<Recordatorio>() {
        @Override
        public Recordatorio createFromParcel(Parcel source) {
            return new Recordatorio(source);
        }

        @Override
        public Recordatorio[] newArray(int size) {
            return new Recordatorio[size];
        }
    };
}

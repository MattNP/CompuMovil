package co.edu.udea.compumovil.gr4.geolaps.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MattNP on 20/05/2016.
 */
public class Recordatorio implements Parcelable {

    private int id;
    private int uid;
    private TipoRecordatorio tipo;
    private List<Lugar> lugares;
    private String nombre;
    private long fecha_limite;
    private long timestamp;
    private String descripcion;
    private int color;
    private int adentro;

    public TipoRecordatorio getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecordatorio tipo) {
        this.tipo = tipo;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAdentro() {
        return adentro;
    }

    public void setAdentro(int adentro) {
        this.adentro = adentro;
    }

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

    public List<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<Lugar> lugares) {
        this.lugares = lugares;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(long fecha_limite) {
        this.fecha_limite = fecha_limite;
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


    public Recordatorio() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.uid);
        dest.writeParcelable(this.tipo, flags);
        dest.writeTypedList(this.lugares);
        dest.writeString(this.nombre);
        dest.writeLong(this.fecha_limite);
        dest.writeLong(this.timestamp);
        dest.writeString(this.descripcion);
        dest.writeInt(this.color);
        dest.writeInt(this.adentro);
    }

    protected Recordatorio(Parcel in) {
        this.id = in.readInt();
        this.uid = in.readInt();
        this.tipo = in.readParcelable(TipoRecordatorio.class.getClassLoader());
        this.lugares = in.createTypedArrayList(Lugar.CREATOR);
        this.nombre = in.readString();
        this.fecha_limite = in.readLong();
        this.timestamp = in.readLong();
        this.descripcion = in.readString();
        this.color = in.readInt();
        this.adentro = in.readInt();
    }

    public static final Creator<Recordatorio> CREATOR = new Creator<Recordatorio>() {
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

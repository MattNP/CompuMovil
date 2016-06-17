package co.edu.udea.compumovil.gr4.geolaps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MattNP on 17/06/2016.
 */
public class TipoRecordatorio implements Parcelable {

    private long id;
    private String tipo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public TipoRecordatorio() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.tipo);
    }

    protected TipoRecordatorio(Parcel in) {
        this.id = in.readLong();
        this.tipo = in.readString();
    }

    public static final Creator<TipoRecordatorio> CREATOR = new Creator<TipoRecordatorio>() {
        @Override
        public TipoRecordatorio createFromParcel(Parcel source) {
            return new TipoRecordatorio(source);
        }

        @Override
        public TipoRecordatorio[] newArray(int size) {
            return new TipoRecordatorio[size];
        }
    };

    @Override
    public String toString() {
        return tipo;
    }
}

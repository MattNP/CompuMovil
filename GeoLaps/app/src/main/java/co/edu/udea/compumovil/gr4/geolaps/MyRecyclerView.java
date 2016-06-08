package co.edu.udea.compumovil.gr4.geolaps;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr4.geolaps.model.Recordatorio;

/**
 * Created by sergio.marriaga on 8/06/16.
 */
public class MyRecyclerView implements Parcelable {

    public String nombre;
    public String places;
    public String detalles;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.places);
        dest.writeString(this.detalles);
    }

    public MyRecyclerView() {
    }

    protected MyRecyclerView(Parcel in) {
        this.nombre = in.readString();
        this.places = in.readString();
        this.detalles = in.readString();
    }

    public static final Parcelable.Creator<MyRecyclerView> CREATOR = new Parcelable.Creator<MyRecyclerView>() {
        @Override
        public MyRecyclerView createFromParcel(Parcel source) {
            return new MyRecyclerView(source);
        }

        @Override
        public MyRecyclerView[] newArray(int size) {
            return new MyRecyclerView[size];
        }
    };
}


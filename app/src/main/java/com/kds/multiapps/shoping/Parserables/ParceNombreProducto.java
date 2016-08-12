package com.kds.multiapps.shoping.Parserables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Isaac Martinez on 02/08/2016.
 * shoping
 */
public class ParceNombreProducto implements Parcelable {

    private final int IdProducto;
    private final String NombreProducto;

    public ParceNombreProducto(int idProducto, String nombreProducto) {
        IdProducto = idProducto;
        NombreProducto = nombreProducto;
    }

    protected ParceNombreProducto(Parcel in) {
        IdProducto = in.readInt();
        NombreProducto = in.readString();
    }

    public static final Creator<ParceNombreProducto> CREATOR = new Creator<ParceNombreProducto>() {
        @Override
        public ParceNombreProducto createFromParcel(Parcel in) {
            return new ParceNombreProducto(in);
        }

        @Override
        public ParceNombreProducto[] newArray(int size) {
            return new ParceNombreProducto[size];
        }
    };

    public int getIdProducto() {
        return IdProducto;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdProducto);
        dest.writeString(NombreProducto);
    }
}

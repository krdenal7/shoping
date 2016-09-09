package com.kds.multiapps.shoping.Parserables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Isaac Martinez on 02/08/2016.
 * shoping
 */
public class ParceNombreSeccion implements Parcelable {
    private final int IdSeccion;
    private final String NombreSeccion;

    public ParceNombreSeccion(int idSeccion, String nombreSeccion) {
        IdSeccion = idSeccion;
        NombreSeccion = nombreSeccion;
    }

    private ParceNombreSeccion(Parcel in) {
        IdSeccion = in.readInt();
        NombreSeccion = in.readString();
    }

    public static final Creator<ParceNombreSeccion> CREATOR = new Creator<ParceNombreSeccion>() {
        @Override
        public ParceNombreSeccion createFromParcel(Parcel in) {
            return new ParceNombreSeccion(in);
        }

        @Override
        public ParceNombreSeccion[] newArray(int size) {
            return new ParceNombreSeccion[size];
        }
    };

    public int getIdSeccion() {
        return IdSeccion;
    }

    public String getNombreSeccion() {
        return NombreSeccion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdSeccion);
        dest.writeString(NombreSeccion);
    }
}

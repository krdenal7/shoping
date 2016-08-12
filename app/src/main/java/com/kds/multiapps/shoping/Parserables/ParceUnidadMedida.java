package com.kds.multiapps.shoping.Parserables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Isaac Martinez on 04/08/2016.
 * shoping
 */
public class ParceUnidadMedida implements Parcelable {
    private int IdUnidad;

    private String NombreUnidad;

    private String abreviatura;

    public ParceUnidadMedida(int idUnidad, String nombreUnidad,String abreviatura) {
        this.IdUnidad = idUnidad;
        this.NombreUnidad = nombreUnidad;
        this.abreviatura=abreviatura;
    }

    protected ParceUnidadMedida(Parcel in) {
        IdUnidad = in.readInt();
        NombreUnidad = in.readString();
    }

    public static final Creator<ParceUnidadMedida> CREATOR = new Creator<ParceUnidadMedida>() {
        @Override
        public ParceUnidadMedida createFromParcel(Parcel in) {
            return new ParceUnidadMedida(in);
        }

        @Override
        public ParceUnidadMedida[] newArray(int size) {
            return new ParceUnidadMedida[size];
        }
    };

    public int getIdUnidad() {
        return IdUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        IdUnidad = idUnidad;
    }

    public String getNombreUnidad() {
        return NombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        NombreUnidad = nombreUnidad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IdUnidad);
        dest.writeString(NombreUnidad);
        dest.writeString(abreviatura);
    }
}

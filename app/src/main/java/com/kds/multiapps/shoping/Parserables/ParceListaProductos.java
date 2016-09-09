
package com.kds.multiapps.shoping.Parserables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Isaac Martinez on 12/08/2016.
 * shoping
 */
public class ParceListaProductos implements Parcelable {
    private int id_producto;
    private final int id_lista;
    private final String nombre_producto;
    private final String seccion;
    private final String unidad;
    private final float cantidad;
    private final float precio;
    private final String fecha_registro;

    public ParceListaProductos(int id_producto,int id_lista, String nombre_producto, String seccion, String unidad, float cantidad, float precio, String fecha_registro) {
        this.id_producto=id_producto;
        this.id_lista = id_lista;
        this.nombre_producto = nombre_producto;
        this.seccion = seccion;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fecha_registro = fecha_registro;
    }

    private ParceListaProductos(Parcel in) {
        id_producto = in.readInt();
        id_lista = in.readInt();
        nombre_producto = in.readString();
        seccion = in.readString();
        unidad = in.readString();
        cantidad = in.readFloat();
        precio = in.readFloat();
        fecha_registro = in.readString();
    }

    public static final Creator<ParceListaProductos> CREATOR = new Creator<ParceListaProductos>() {
        @Override
        public ParceListaProductos createFromParcel(Parcel in) {
            return new ParceListaProductos(in);
        }

        @Override
        public ParceListaProductos[] newArray(int size) {
            return new ParceListaProductos[size];
        }
    };

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_lista() {
        return id_lista;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public String getSeccion() {
        return seccion;
    }

    public String getUnidad() {
        return unidad;
    }

    public float getCantidad() {
        return cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_producto);
        dest.writeInt(id_lista);
        dest.writeString(nombre_producto);
        dest.writeString(seccion);
        dest.writeString(unidad);
        dest.writeFloat(cantidad);
        dest.writeFloat(precio);
        dest.writeString(fecha_registro);
    }
}

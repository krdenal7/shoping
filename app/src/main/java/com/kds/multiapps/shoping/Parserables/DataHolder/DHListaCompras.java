package com.kds.multiapps.shoping.Parserables.DataHolder;

/**
 * Created by Isaac Martinez on 29/07/2016.
 * shoping
 */
public class DHListaCompras {
    private int id;
    private String nombre_lista;
    private int total_piezas;
    private Double monto;

    public DHListaCompras(int id, String nombre_lista, int total_piezas, Double monto) {
        this.id = id;
        this.nombre_lista = nombre_lista;
        this.total_piezas = total_piezas;
        this.monto = monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_lista() {
        return nombre_lista;
    }

    public void setNombre_lista(String nombre_lista) {
        this.nombre_lista = nombre_lista;
    }

    public int getTotal_piezas() {
        return total_piezas;
    }

    public void setTotal_piezas(int total_piezas) {
        this.total_piezas = total_piezas;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}

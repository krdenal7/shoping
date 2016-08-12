package com.kds.multiapps.shoping.Utils;

/**
 * Created by Isaac Martinez on 12/07/2016.
 * shoping
 */
public class SearchCode {


    /**
     * Busca dentro del código html el valor de href
     * @param code Codigo html que contiene la etiqueta
     * @return valor de href
     */
    public String searchLink(String code){
        StringBuilder val=new StringBuilder();

        int valInicio=code.indexOf("href=\"")+6; //se le suma 5 que es el tamaño de la palabra de busqueda

        for(int i=1;i<code.length();i++){ //comienza el recorrido del código hasta terminar de concatenar el link
            String letra=String.valueOf(code.charAt(valInicio+i));
            if(letra.equalsIgnoreCase("\"")){
                break;
            }else {
                val.append(letra);
            }
        }

        return val.toString();
    }

    public String getProductoWalmart(String html){

        for(int i=0;i<html.length();i++){
           new Logs().LogE("s",""+i);
        }

        return "";
    }

}

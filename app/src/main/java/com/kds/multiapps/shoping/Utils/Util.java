package com.kds.multiapps.shoping.Utils;

import java.awt.font.NumericShaper;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Isaac Martinez on 16/08/2016.
 * shoping
 */
public class Util {

    public String decimalFormat(String numbre){
        Double valor=Double.parseDouble(numbre);
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return  format.format(valor);
    }

    public String ValidaEntero(String val){
         Double d=Double.parseDouble(val);
         if(d-Math.floor(d)==0){
             NumberFormat format=NumberFormat.getInstance(Locale.US);
             format.setMinimumFractionDigits(0);
             format.setMaximumFractionDigits(0);
              return format.format(d);
         }else {
              return  val;
         }
    }
}

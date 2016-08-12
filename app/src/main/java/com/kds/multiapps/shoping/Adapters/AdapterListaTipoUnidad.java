package com.kds.multiapps.shoping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kds.multiapps.shoping.Parserables.ParceUnidadMedida;
import com.kds.multiapps.shoping.R;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 05/08/2016.
 * shoping
 */
public class AdapterListaTipoUnidad extends ArrayAdapter<String> {

    private final ArrayList<ParceUnidadMedida> mAlUnidadMedida;
    private final Context mContext;
    private AdapterListaUnidadListener listener;

    public AdapterListaTipoUnidad(Context mContext,ArrayList<ParceUnidadMedida>mAlUnidadMedida) {
        super(mContext,-1);
        this.mAlUnidadMedida=mAlUnidadMedida;
        this.mContext=mContext;
    }

   class ViewHolder{
       TextView txtNombre;
   }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.custom_textview,null);
            holder.txtNombre=(TextView)view.findViewById(R.id.customerNameLabel);
            view.setTag(holder);
        }else {
            view=convertView;
            holder=(ViewHolder)convertView.getTag();
        }

        holder.txtNombre.setText(mAlUnidadMedida.get(position).getNombreUnidad());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemUnidad(mAlUnidadMedida.get(
                        position).getIdUnidad(),
                        mAlUnidadMedida.get(position).getAbreviatura());
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return mAlUnidadMedida.size();
    }

    public void setOnListenerTipoUnidad(AdapterListaUnidadListener listener){
        this.listener=listener;
    }

    public interface AdapterListaUnidadListener{
        void onClickItemUnidad(int id,String unidad);
    }
}

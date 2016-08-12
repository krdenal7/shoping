package com.kds.multiapps.shoping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kds.multiapps.shoping.Parserables.ParceNombreSeccion;
import com.kds.multiapps.shoping.R;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 09/08/2016.
 * shoping
 */
public class AdapterListaSeccion extends ArrayAdapter<String> {

    private final ArrayList<ParceNombreSeccion> mAlseccion;
    private OnListenerSeccion listener;
    private final Context context;

    public AdapterListaSeccion(Context context,ArrayList<ParceNombreSeccion> mAlseccion) {
        super(context, -1);
        this.context=context;
        this.mAlseccion=mAlseccion;
    }

    class ViewHolder{
        TextView txtNombre;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.custom_textview,null);
            viewHolder.txtNombre=(TextView)view.findViewById(R.id.customerNameLabel);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

            viewHolder.txtNombre.setText(mAlseccion.get(position).getNombreSeccion());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectedItem(
                            mAlseccion.get(position).getIdSeccion(),
                            mAlseccion.get(position).getNombreSeccion());
                }
            });

        return view;
    }

    @Override
    public int getCount() {
        return mAlseccion.size();
    }

    @Override
    public String getItem(int position) {
        return mAlseccion.get(position).getNombreSeccion();
    }

    public void onSetInterfaceAdapter(OnListenerSeccion listener){
        this.listener=listener;
    }

    public interface OnListenerSeccion{
          void onSelectedItem(int id,String seccion);
    }
}

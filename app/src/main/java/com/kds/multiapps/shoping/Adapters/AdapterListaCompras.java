package com.kds.multiapps.shoping.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kds.multiapps.shoping.Parserables.DataHolder.DHListaCompras;
import com.kds.multiapps.shoping.R;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 29/07/2016.
 * shoping
 */
public class AdapterListaCompras extends RecyclerView.Adapter<AdapterListaCompras.HolderCompras>{

    private final ArrayList<DHListaCompras> listaCompras;
    private AdapterListaComprasListener listener;

    public AdapterListaCompras(ArrayList<DHListaCompras> listaCompras) {
        this.listaCompras=listaCompras;
    }

    @Override
    public HolderCompras onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_list,parent,false);
        return new HolderCompras(view);
    }

    @Override
    public void onBindViewHolder(HolderCompras holder, final int position) {
        holder.txtNombre.setText(listaCompras.get(position).getNombre_lista());
        holder.txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClikItemList(listaCompras.get(position).getId(),
                        listaCompras.get(position).getNombre_lista());
            }
        });
        holder.imgbSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClikItemList(listaCompras.get(position).getId(),
                        listaCompras.get(position).getNombre_lista());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onDeleteItem(listaCompras.get(position).getId());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCompras.size();
    }

    public class HolderCompras extends RecyclerView.ViewHolder {
        public final TextView txtNombre;
        public final ImageButton imgbSiguiente;
        public HolderCompras(View itemView) {
            super(itemView);
            txtNombre=(TextView)itemView.findViewById(R.id.txtNombre);
            imgbSiguiente=(ImageButton)itemView.findViewById(R.id.imgbSiguiente);
        }
    }

    public void onSetAdapterListaComprasListener(AdapterListaComprasListener listener){
        this.listener=listener;
    }

    public interface AdapterListaComprasListener{
         void onDeleteItem(int Id);
         void onClikItemList(int id,String nombre);
    }

}

package com.kds.multiapps.shoping.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kds.multiapps.shoping.Parserables.ParceListaProductos;
import com.kds.multiapps.shoping.R;
import com.kds.multiapps.shoping.Utils.Util;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 12/08/2016.
 * shoping
 */
public class AdapterListaProductos extends RecyclerView.Adapter<AdapterListaProductos.ViewHolderProductos>  {

    private final ArrayList<ParceListaProductos> mAlListaProductos;
    private AdapterListaProductosListener listener;

    public AdapterListaProductos(ArrayList<ParceListaProductos> mAlListaProductos){
        this.mAlListaProductos=mAlListaProductos;
    }

    @Override
    public ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_lista_productos,parent,false);
        return new ViewHolderProductos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderProductos holder, int position) {
        float cantidad=mAlListaProductos.get(position).getCantidad();
        float precio=mAlListaProductos.get(position).getPrecio();
        String subtotal= new Util().decimalFormat(String.valueOf(cantidad*precio));

        holder.txtNombreProducto.setText(mAlListaProductos.get(position).getNombre_producto());
        String seccion=mAlListaProductos.get(position).getSeccion();
        holder.txtSeccion.setText(seccion);
        String cantidad_u=String.valueOf(cantidad)+" "+mAlListaProductos.get(position).getUnidad();
        holder.txtPiezas.setText(cantidad_u);
        String sub_="$"+subtotal;
        holder.txtSubtotal.setText(sub_);
        String prec_="$"+precio;
        holder.txtPrecio.setText(prec_);

        final int idLista=mAlListaProductos.get(position).getId_lista();
        final int idProducto=mAlListaProductos.get(position).getId_producto();
        final int pos=holder.getAdapterPosition();

        holder.imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickEliminar(idLista,idProducto);
            }
        });
        holder.imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickEditar(pos,idLista,idProducto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlListaProductos.size();
    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder{
        public final TextView txtNombreProducto;
        public final TextView txtSubtotal;
        public final TextView txtPrecio;
        public final TextView txtSeccion;
        public final TextView txtPiezas;
        public final ImageButton imgEliminar;
        public final ImageButton imgEditar;
        public ViewHolderProductos(View itemView) {
            super(itemView);
            txtNombreProducto=(TextView)itemView.findViewById(R.id.txtNombre);
            txtSeccion=(TextView)itemView.findViewById(R.id.txtSeccion);
            txtPrecio=(TextView)itemView.findViewById(R.id.txtPrecio);
            txtSubtotal=(TextView) itemView.findViewById(R.id.txtSubtotal);
            txtPiezas=(TextView)itemView.findViewById(R.id.txtPiezas);
            imgEliminar=(ImageButton)itemView.findViewById(R.id.imgbEliminar);
            imgEditar=(ImageButton)itemView.findViewById(R.id.imgbEditar);
            }
        }

    public void onSetListenerAdapter(AdapterListaProductosListener listener){
        this.listener=listener;
    }

    public interface AdapterListaProductosListener{
         void onClickEditar(int position,int idLista,int idProducto);
         void onClickEliminar(int idLista,int idProducto);
    }

}

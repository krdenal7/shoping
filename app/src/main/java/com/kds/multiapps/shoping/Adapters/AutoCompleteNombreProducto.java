package com.kds.multiapps.shoping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kds.multiapps.shoping.Parserables.ParceNombreProducto;
import com.kds.multiapps.shoping.R;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 02/08/2016.
 * shoping
 */
public class AutoCompleteNombreProducto extends ArrayAdapter<String> implements Filterable{

    private ArrayList<ParceNombreProducto> mAlProductos;
    private ArrayList<ParceNombreProducto> mAlItemsFilter;
    private final Context mContext;
    private AutoCompleteNombreListener listener;
    private final int LayoutResource;

    public AutoCompleteNombreProducto(Context context,int LayoutResource,ArrayList<ParceNombreProducto> mAlProductos) {
        super(context,LayoutResource);
        this.mAlProductos=mAlProductos;
        this.mContext=context;
        this.LayoutResource=LayoutResource;
        mAlItemsFilter=new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(LayoutResource,null);
            holder.actNombre=(TextView)view.findViewById(R.id.customerNameLabel);
            view.setTag(holder);
        }else {
            view=convertView;
            holder=(ViewHolder) view.getTag();
        }

        holder.actNombre.setText(mAlItemsFilter.get(position).getNombreProducto());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(mAlItemsFilter.get(position).getNombreProducto());
            }
        });

        return view;
    }

    class ViewHolder{
        TextView actNombre;
    }

    @Override
    public int getCount() {
        return mAlItemsFilter.size();
    }

    public void setmAlProductos(ArrayList<ParceNombreProducto> mAlProductos) {
        this.mAlProductos = mAlProductos;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mAlItemsFilter=new ArrayList<>();
                if(constraint!=null){
                    for(ParceNombreProducto pcNombre:mAlProductos){
                          if(pcNombre.getNombreProducto().toLowerCase().contains(constraint.toString().toLowerCase()))
                              mAlItemsFilter.add(new ParceNombreProducto(pcNombre.getIdProducto(),pcNombre.getNombreProducto()));
                    }
                }
                if(mAlItemsFilter.size()>0){
                    mAlProductos=mAlItemsFilter;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=mAlItemsFilter;
                filterResults.count=mAlItemsFilter.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                for(ParceNombreProducto pcNombreproducto:(ArrayList<ParceNombreProducto>)results.values){
                    addAll(pcNombreproducto.getNombreProducto());
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public String convertResultToString(Object resultValue) {
                return resultValue.toString();
            }
        };
    }

    public void onSetListener(AutoCompleteNombreListener listener){
        this.listener=listener;
    }

    public interface AutoCompleteNombreListener{
          void onSelected(String texto);
    }
}

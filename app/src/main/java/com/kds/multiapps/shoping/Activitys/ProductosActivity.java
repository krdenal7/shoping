package com.kds.multiapps.shoping.Activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kds.multiapps.shoping.Adapters.AdapterListaSeccion;
import com.kds.multiapps.shoping.Adapters.AdapterListaTipoUnidad;
import com.kds.multiapps.shoping.Adapters.AutoCompleteNombreProducto;
import com.kds.multiapps.shoping.DataBase.SQLiteHelper;
import com.kds.multiapps.shoping.Parserables.ParceNombreProducto;
import com.kds.multiapps.shoping.Parserables.ParceNombreSeccion;
import com.kds.multiapps.shoping.Parserables.ParceUnidadMedida;
import com.kds.multiapps.shoping.R;
import com.kds.multiapps.shoping.Utils.Logs;

import java.text.NumberFormat;
import java.util.ArrayList;


public class ProductosActivity extends AppCompatActivity  {

    private final String TAG=getClass().getSimpleName();
    private int mIdLista;
    private String mNombreLista="";

    private ArrayList<ParceNombreProducto> mAlNombreProducto;
    private final String mSiAlNombreProducto="mSiAlNombreProducto";
    private ArrayList<ParceNombreSeccion> mAlNombreSeccion;
    private final String mSiAlNombreSeccion="mSiAlNombreSeccion";
    private ArrayList<ParceUnidadMedida> mAlUnidadMedida;
    private final String mSiAlUnidadMedida="mSiAlUnidadMedida";

    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    private RecyclerView mRecyclerListaProductos;
    private TextView mTxtMensaje;
    private TextView txtUnidadMedida;
    private AutoCompleteTextView autSeccion;
    private AutoCompleteTextView autNombre;
    private ImageButton imgbAgregar;

    private AlertDialog mDialogAgregarProducto;
    private final String mSiAgregarProducto="mSiAgregarProducto";
    private Boolean mbAgregarProducto=false;
    private AlertDialog mDialogSeleccionarScaner;
    private final String mSiSeleccionarScaner="mSiSeleccionarScaner";
    private Boolean mbSeleccionarScaner=false;
    private AlertDialog mDialogSeleccionarUnidad;
    private final String mSiSeleccionarUnidad="mSiSeleccionarUnidad";
    private boolean mbSeleccionarUnidad=false;
    private AlertDialog mDialogSeleccionarSeccion;
    private final String mSiSeleccionarSeccion="";
    private boolean mbSeleccionarSeccion=false;

    private String mValNombreProducto="";
    private final String mSiNombreProducto="mSiNombreProducto";
    private String mValCantidad="";
    private final String mSiCantidad="mSiCantidad";
    private String mValSeccion="";
    private final String mSiSeccion="mSiSeccion";
    private String mValPrecio="";
    private final String mSiPrecio="mSiPrecio";
    private String mValUnidadMedida="";
    private final String mSiUnidadMedida="mSiUnidadMedida";

    private Boolean mbErrorInputName=false;
    private final String mSiErrorInputName="mSiErrorInputName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Bundle bundle= getIntent().getExtras();
        mContext=this;
        sqLiteHelper=new SQLiteHelper(mContext);
        sqLiteHelper.OpenDataBase();

        if(bundle!=null){
            mIdLista=bundle.getInt("idLista");
            mNombreLista=bundle.getString("Nombre");
        }
        if(savedInstanceState!=null){
            mbAgregarProducto=savedInstanceState.getBoolean(mSiAgregarProducto);
            mbSeleccionarScaner=savedInstanceState.getBoolean(mSiSeleccionarScaner);
            mbSeleccionarUnidad=savedInstanceState.getBoolean(mSiSeleccionarUnidad);
            mbSeleccionarSeccion=savedInstanceState.getBoolean(mSiSeleccionarSeccion);
            mbErrorInputName=savedInstanceState.getBoolean(mSiErrorInputName);
            mAlNombreProducto=savedInstanceState.getParcelableArrayList(mSiAlNombreProducto);
            mAlNombreSeccion=savedInstanceState.getParcelableArrayList(mSiAlNombreSeccion);
            mAlUnidadMedida=savedInstanceState.getParcelableArrayList(mSiAlUnidadMedida);
            mValNombreProducto=savedInstanceState.getString(mSiNombreProducto);
            mValCantidad=savedInstanceState.getString(mSiCantidad);
            mValPrecio=savedInstanceState.getString(mSiPrecio);
            mValSeccion=savedInstanceState.getString(mSiSeccion);
            mValUnidadMedida=savedInstanceState.getString(mSiUnidadMedida);
        }else {
            ConsultaProductos();
            ConsultaSeccion();
            ConsultaUnidadMedida();
        }
        setTitle(mNombreLista);
        Init();
        VerificarItems();
        imgbAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarMensajeAgregar();
            }
        });
    }

    private void Init(){
        mRecyclerListaProductos=(RecyclerView)findViewById(R.id.rvListaItems);
        mTxtMensaje=(TextView)findViewById(R.id.txtMsjSinItems);
        imgbAgregar=(ImageButton)findViewById(R.id.imgbAgregar);
    }

    private void VerificarItems(){
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select id_lista from tb_lista_productos where\n" +
                    "id_lista=?",new String[]{String.valueOf(mIdLista)});

            if(rs.getCount()>0){
                mRecyclerListaProductos.setVisibility(View.VISIBLE);
                mTxtMensaje.setVisibility(View.GONE);
            }else {
                mRecyclerListaProductos.setVisibility(View.GONE);
                mTxtMensaje.setVisibility(View.VISIBLE);
            }

        }finally {
            if(db!=null)
                db.close();
            if(rs!=null)
                rs.close();
        }
    }

    private void MostrarMensajeAgregar(){
        View view= ConfigViewAgregarProducto();

        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Agregar productos");
        builder.setPositiveButton("Aceptar", null);
        builder.setNeutralButton("Buscar por código", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbAgregarProducto=false;
                MostrarMensajeSeleccionarScanner();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbAgregarProducto=false;
                mValNombreProducto="";
                mValCantidad="";
                mValSeccion="";
                mValPrecio="";
            }
        });
        mDialogAgregarProducto=builder.create();
        mDialogAgregarProducto.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn=mDialogAgregarProducto.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(autNombre.getText().toString().trim().isEmpty()){
                            mbErrorInputName=true;
                            autNombre.setError("Debe agregar el nombre del producto");
                        }else {
                            mbErrorInputName=false;
                        }
                    }
                });
            }
        });
        mDialogAgregarProducto.show();
        mbAgregarProducto=true;
    }

    private void MostrarMensajeSeleccionarScanner(){
        View view=ConfigViewSelectOptionScanner();
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Seleccione una opción");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbSeleccionarScaner=false;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbSeleccionarScaner=false;
                MostrarMensajeAgregar();
            }
        });
        mDialogSeleccionarScaner=builder.create();
        mDialogSeleccionarScaner.show();
        mbSeleccionarScaner=true;
    }

    private void MostrarListaUnidades(){
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_layout_list_tipounidad,null);
        AdapterListaTipoUnidad adapter=new AdapterListaTipoUnidad(mContext,mAlUnidadMedida);
        adapter.setOnListenerTipoUnidad(new AdapterListaTipoUnidad.AdapterListaUnidadListener() {
            @Override
            public void onClickItemUnidad(int id, String unidad) {
                mbSeleccionarUnidad=false;
                mValUnidadMedida=unidad;
                txtUnidadMedida.setText(unidad);
                if(mDialogSeleccionarUnidad!=null)
                   mDialogSeleccionarUnidad.dismiss();
            }
        });
        ListView ls=(ListView)view.findViewById(R.id.lvSimple);
        ls.setAdapter(adapter);
        AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
        alert.setView(view);
        mDialogSeleccionarUnidad=alert.create();
        mDialogSeleccionarUnidad.show();
        mbSeleccionarUnidad=true;
    }

    private void MostrarListaSecciones(){
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_layout_list_tipounidad,null);
        AdapterListaSeccion adapter=new AdapterListaSeccion(mContext,mAlNombreSeccion);
        adapter.onSetInterfaceAdapter(new AdapterListaSeccion.OnListenerSeccion() {
            @Override
            public void onSelectedItem(int id, String seccion) {
                mbSeleccionarSeccion=false;
                if(mDialogSeleccionarSeccion!=null)
                mDialogSeleccionarSeccion.dismiss();
                mValSeccion=seccion;
                autSeccion.setText(seccion);
            }
        });
        ListView ls=(ListView)view.findViewById(R.id.lvSimple);
        ls.setAdapter(adapter);
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setView(view);
        mDialogSeleccionarSeccion=builder.create();
        mDialogSeleccionarSeccion.show();
        mbSeleccionarSeccion=true;

    }

    private View ConfigViewAgregarProducto(){
        LayoutInflater inflater= getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_agregar_producto,null,false);

        autNombre=(AutoCompleteTextView)view.findViewById(R.id.etdNombre);
        final EditText etdCantidad=(EditText)view.findViewById(R.id.etdCantidad);
        final EditText etdPrecio=(EditText)view.findViewById(R.id.etdPrecio);
        txtUnidadMedida=(TextView)view.findViewById(R.id.txtUnidadMedida);
        autSeccion=(AutoCompleteTextView)view.findViewById(R.id.etdSeccion);
        final ImageButton imgDeleteNombre=(ImageButton)view.findViewById(R.id.imgDeleteNombre);
        final ImageButton imgDeletePrecio=(ImageButton)view.findViewById(R.id.imgDeletePrecio);

        /*Config autNombre*/
        final AutoCompleteNombreProducto adpterNombreProducto=
                new AutoCompleteNombreProducto(mContext,R.layout.custom_textview,mAlNombreProducto);
        adpterNombreProducto.onSetListener(new AutoCompleteNombreProducto.AutoCompleteNombreListener() {
            @Override
            public void onSelected(String texto) {
                autNombre.setText(texto);
                etdCantidad.requestFocus();
            }
        });
        autNombre.setAdapter(adpterNombreProducto);
        autNombre.setText(mValNombreProducto);
        if(!mValNombreProducto.trim().isEmpty()){
            autNombre.setSelection(mValNombreProducto.length());
            imgDeleteNombre.setVisibility(View.VISIBLE);
        }
        imgDeleteNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autNombre.setText("");
                adpterNombreProducto.setmAlProductos(mAlNombreProducto);
            }
        });
        autNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>0){
                    if(imgDeleteNombre.getVisibility()==View.GONE)
                    imgDeleteNombre.setVisibility(View.VISIBLE);
                }else if((count==1 && start==0)|| (start==0 && count>0 && after==0)){
                    if(imgDeleteNombre.getVisibility()==View.VISIBLE)
                    imgDeleteNombre.setVisibility(View.GONE);
                    adpterNombreProducto.setmAlProductos(mAlNombreProducto);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mValNombreProducto=s.toString();
            }
        });
        if(mbErrorInputName)
            autNombre.setError("Debe agregar el nombre del producto");

        /*Config autNombre*/

        /*Config cantidad*/
         etdCantidad.setText(mValCantidad);
         if(!mValCantidad.trim().isEmpty()){
             etdCantidad.setSelection(mValCantidad.length());
         }
        etdCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               mValCantidad=s.toString();
            }
        });
        txtUnidadMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarListaUnidades();
            }
        });
        /*Config cantidad*/

        /*Config txtUnidadMedida*/
        if(!mValUnidadMedida.trim().isEmpty())
            txtUnidadMedida.setText(mValUnidadMedida);
        /*Config txtUnidadMedida*/

        /*Config precio*/
        if(mValPrecio.trim().isEmpty()) {
            etdPrecio.setText(getString(R.string.ceros));
            imgDeletePrecio.setVisibility(View.GONE);
        }
        else{
            etdPrecio.setText(mValPrecio);
            imgDeletePrecio.setVisibility(View.VISIBLE);
        }
        imgDeletePrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdPrecio.setText(getString(R.string.ceros));
                mValPrecio=getString(R.string.ceros);
            }
        });
        etdPrecio.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                     MostrarListaSecciones();
                    return true;
                }
                return false;
            }
        });
        etdPrecio.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    etdPrecio.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    if(parsed>0)
                        imgDeletePrecio.setVisibility(View.VISIBLE);
                    else
                        imgDeletePrecio.setVisibility(View.GONE);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    mValPrecio=formatted;
                    etdPrecio.setText(formatted);
                    new Logs().LogE(TAG,formatted);
                    etdPrecio.setSelection(formatted.length());
                    etdPrecio.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etdPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etdPrecio.setSelection(etdPrecio.getText().length());
            }
        });
        /*Config precio*/

        /*Config seccion*/
        autSeccion.setText(mValSeccion);
        autSeccion.setFocusable(false);
        autSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarListaSecciones();
            }
        });
        /*Config seccion*/


        return view;
    }

    private View ConfigViewSelectOptionScanner(){
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_select_scanner_option,null,false);

        final ImageButton imgInput=(ImageButton)view.findViewById(R.id.imgbscanner_input);
        final ImageButton imgRead=(ImageButton)view.findViewById(R.id.imgbscanner_read);

        imgInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgInput.setBackgroundResource(R.drawable.roundcorner);
                imgRead.setBackgroundResource(android.R.color.transparent);
            }
        });

        imgRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRead.setBackgroundResource(R.drawable.roundcorner);
                imgInput.setBackgroundResource(android.R.color.transparent);
            }
        });

        return view;
    }

    private void ConsultaProductos(){
        mAlNombreProducto=new ArrayList<>();
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select * from tb_productos",null);
            while (rs.moveToNext()){
                int id=rs.getInt(0);
                String nombre=rs.getString(1);
                mAlNombreProducto.add(new ParceNombreProducto(id,nombre));
            }

        }finally {
            if(rs!=null)
                rs.close();
            db.close();
        }
    }

    private void ConsultaSeccion(){
        mAlNombreSeccion=new ArrayList<>();
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select * from tb_seccion",null);
            while (rs.moveToNext()){
                int id=rs.getInt(0);
                String nombre=rs.getString(1);
                mAlNombreSeccion.add(new ParceNombreSeccion(id,nombre));
            }

        }finally {
            if(rs!=null)
                rs.close();
            if(db!=null)
                db.close();
        }
    }

    private void ConsultaUnidadMedida(){
        mAlUnidadMedida=new ArrayList<>();
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select * from tb_unidad",null);
            while (rs.moveToNext()){
                int id=rs.getInt(0);
                String unidad=rs.getString(1);
                String abreviatura=rs.getString(2);
                mAlUnidadMedida.add(new ParceUnidadMedida(id,unidad,abreviatura));
            }
        }finally {
            if(rs!=null)
                rs.close();
            if(db!=null)
                db.close();
        }
    }

    private void DimissAlertOnResume(boolean onResume){
        if(onResume){
            /*Alert agregar producto*/
            if(mbAgregarProducto){
                if(mDialogAgregarProducto==null)
                    MostrarMensajeAgregar();
                else if(!mDialogAgregarProducto.isShowing())
                    MostrarMensajeAgregar();
            }
            /*Alert seleccionar escaner*/
            if(mbSeleccionarScaner){
                if(mDialogSeleccionarScaner==null)
                    MostrarMensajeSeleccionarScanner();
                else if(!mDialogSeleccionarScaner.isShowing())
                    MostrarMensajeSeleccionarScanner();
            }
            /*Alert seleccionar unidad*/
            if(mbSeleccionarUnidad){
                if(mDialogSeleccionarUnidad==null)
                    MostrarListaUnidades();
                else if (!mDialogSeleccionarUnidad.isShowing())
                    MostrarListaUnidades();
            }

            /*Alert seleccionar seccion*/
            if(mbSeleccionarSeccion){
                if(mDialogSeleccionarSeccion==null)
                    MostrarListaSecciones();
                else if(!mDialogSeleccionarSeccion.isShowing())
                    MostrarListaSecciones();
            }

        }else {
            if(mbAgregarProducto)
                mDialogAgregarProducto.dismiss();
            if(mbSeleccionarScaner)
                mDialogSeleccionarScaner.dismiss();
            if(mbSeleccionarUnidad)
                mDialogSeleccionarUnidad.dismiss();
            if(mbSeleccionarSeccion)
                mDialogSeleccionarSeccion.dismiss();

        }
    }

    private void GuardarProducto(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        DimissAlertOnResume(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DimissAlertOnResume(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(mSiAgregarProducto,mbAgregarProducto);
        outState.putBoolean(mSiSeleccionarScaner,mbSeleccionarScaner);
        outState.putBoolean(mSiSeleccionarUnidad,mbSeleccionarUnidad);
        outState.putBoolean(mSiSeleccionarSeccion,mbSeleccionarSeccion);
        outState.putBoolean(mSiErrorInputName,mbErrorInputName);
        outState.putParcelableArrayList(mSiAlNombreProducto,mAlNombreProducto);
        outState.putParcelableArrayList(mSiAlNombreSeccion,mAlNombreSeccion);
        outState.putParcelableArrayList(mSiAlUnidadMedida,mAlUnidadMedida);
        outState.putString(mSiNombreProducto,mValNombreProducto);
        outState.putString(mSiCantidad,mValCantidad);
        outState.putString(mSiSeccion,mValSeccion);
        outState.putString(mSiPrecio,mValPrecio);
        outState.putString(mSiUnidadMedida,mValUnidadMedida);
    }

}

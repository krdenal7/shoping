package com.kds.multiapps.shoping.Activitys;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.Toast;

import com.kds.multiapps.shoping.Adapters.AdapterListaProductos;
import com.kds.multiapps.shoping.Adapters.AdapterListaSeccion;
import com.kds.multiapps.shoping.Adapters.AdapterListaTipoUnidad;
import com.kds.multiapps.shoping.Adapters.AutoCompleteNombreProducto;
import com.kds.multiapps.shoping.DataBase.SQLiteHelper;
import com.kds.multiapps.shoping.Parserables.ParceListaProductos;
import com.kds.multiapps.shoping.Parserables.ParceNombreProducto;
import com.kds.multiapps.shoping.Parserables.ParceNombreSeccion;
import com.kds.multiapps.shoping.Parserables.ParceUnidadMedida;
import com.kds.multiapps.shoping.R;
import com.kds.multiapps.shoping.Utils.Util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ProductosActivity extends AppCompatActivity {

    //private final String TAG=getClass().getSimpleName();
    private int mIdLista;
    private String mNombreLista = "";

    private ArrayList<ParceNombreProducto> mAlNombreProducto;
    private final String mSiAlNombreProducto = "mSiAlNombreProducto";
    private ArrayList<ParceNombreSeccion> mAlNombreSeccion;
    private final String mSiAlNombreSeccion = "mSiAlNombreSeccion";
    private ArrayList<ParceUnidadMedida> mAlUnidadMedida;
    private final String mSiAlUnidadMedida = "mSiAlUnidadMedida";
    private ArrayList<ParceListaProductos> mAlListaProductos;
    private final String mSiAlListaProductos = "mSiAlListaProductos";

    private SQLiteHelper sqLiteHelper;
    private Context mContext;

    private RecyclerView mRecyclerListaProductos;
    private TextView mTxtMensaje;
    private TextView txtUnidadMedida;
    private TextView txtTotal;
    private AutoCompleteTextView autSeccion;
    private AutoCompleteTextView autNombre;
    private ImageButton imgbAgregar;

    private AlertDialog mDialogAgregarProducto;
    private final String mSiAgregarProducto = "mSiAgregarProducto";
    private Boolean mbAgregarProducto = false;
    private AlertDialog mDialogSeleccionarScaner;
    private final String mSiSeleccionarScaner = "mSiSeleccionarScaner";
    private Boolean mbSeleccionarScaner = false;
    private AlertDialog mDialogSeleccionarUnidad;
    private final String mSiSeleccionarUnidad = "mSiSeleccionarUnidad";
    private boolean mbSeleccionarUnidad = false;
    private AlertDialog mDialogSeleccionarSeccion;
    private final String mSiSeleccionarSeccion = "mSiSeleccionarSeccion";
    private boolean mbSeleccionarSeccion = false;
    private AlertDialog mDialogElimianarProducto;
    private final String mSiEliminarProducto = "mSiEliminarProducto";
    private boolean mbEliminarProducto = false;
    private AlertDialog mDialogEditarProducto;
    private final String mSiEditarProducto = "mSiEditarProducto";
    private boolean mbEditarProducto = false;

    private String mValNombreProducto = "";
    private final String mSiNombreProducto = "mSiNombreProducto";
    private String mValCantidad = "";
    private final String mSiCantidad = "mSiCantidad";
    private String mValSeccion = "";
    private final String mSiSeccion = "mSiSeccion";
    private String mValPrecio = "";
    private final String mSiPrecio = "mSiPrecio";
    private String mValUnidadMedida = "";
    private final String mSiUnidadMedida = "mSiUnidadMedida";
    private String mValtotal = "$00.00";
    private final String mSiValtotal = "mSiValtotal";

    private Boolean mbErrorInputName = false;
    private final String mSiErrorInputName = "mSiErrorInputName";

    private int miFocusEditText = -1;
    private final String mSiFocusEditText = "mSiFocusEditText";
    private int miIdSeccion = 30;
    private final String mSiIdSeccion = "mSiIdSeccion";
    private int miIdUnidad = 1;
    private final String mSiIdUnidad = "mSiIdUnidad";
    private int miIdEliminarLista = -1;
    private final String mSiIdEliminarLista = "mSiIdEliminarLista";
    private int miIdEliminarProducto = -1;
    private final String mSiIdEliminarProducto = "mSiIdEliminarProducto";
    private int miIdEditarLista = -1;
    private final String mSiIdEditarLista = "mSiIdEditarLista";
    private int miIdEditarProducto = -1;
    private final String mSiIdEditarProducto = "mSiIdEditarProducto";
    private int miEditarPosicion = -1;
    private final String mSiEditarPosicion = "mSiEditarPosicion";

    private final String mSiNombreEditar="mSiNombreEditar";
    private String mNombreEditar="";
    private final String mSiCantidadEditar="mCantidadEditar";
    private String mCantidadEditar="";
    private final String mSiPrecioEditar="mSiPrecioEditar";
    private String mPrecioEditar="";
    private final String mSiIdUnidadEditar="mSiIdUnidadEditar";
    private int mIdUnidadEditar=-1;
    private final String mSiIdSeccionEditar="mSiIdSeccionEditar";
    private int mIdSeccionEditar=-1;

    private enum TipoElemento{PAGREGAR,PEDITAR}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        Bundle bundle = getIntent().getExtras();
        mContext = this;
        sqLiteHelper = new SQLiteHelper(mContext);
        sqLiteHelper.OpenDataBase();
        Init();

        if (bundle != null) {
            mIdLista = bundle.getInt("idLista");
            mNombreLista = bundle.getString("Nombre");
        }
        if (savedInstanceState != null) {
            mbAgregarProducto = savedInstanceState.getBoolean(mSiAgregarProducto);
            mbSeleccionarScaner = savedInstanceState.getBoolean(mSiSeleccionarScaner);
            mbSeleccionarUnidad = savedInstanceState.getBoolean(mSiSeleccionarUnidad);
            mbSeleccionarSeccion = savedInstanceState.getBoolean(mSiSeleccionarSeccion);
            mbErrorInputName = savedInstanceState.getBoolean(mSiErrorInputName);
            mbEditarProducto = savedInstanceState.getBoolean(mSiEditarProducto);
            mbEliminarProducto = savedInstanceState.getBoolean(mSiEliminarProducto);
            mAlNombreProducto = savedInstanceState.getParcelableArrayList(mSiAlNombreProducto);
            mAlNombreSeccion = savedInstanceState.getParcelableArrayList(mSiAlNombreSeccion);
            mAlUnidadMedida = savedInstanceState.getParcelableArrayList(mSiAlUnidadMedida);
            mAlListaProductos = savedInstanceState.getParcelableArrayList(mSiAlListaProductos);
            mValNombreProducto = savedInstanceState.getString(mSiNombreProducto);
            mValCantidad = savedInstanceState.getString(mSiCantidad);
            mValPrecio = savedInstanceState.getString(mSiPrecio);
            mValSeccion = savedInstanceState.getString(mSiSeccion);
            mValUnidadMedida = savedInstanceState.getString(mSiUnidadMedida);
            mValtotal = savedInstanceState.getString(mSiValtotal);
            mNombreEditar = savedInstanceState.getString(mSiNombreEditar);
            mCantidadEditar = savedInstanceState.getString(mSiCantidadEditar);
            mPrecioEditar = savedInstanceState.getString(mSiPrecioEditar);
            mIdUnidadEditar = savedInstanceState.getInt(mSiIdUnidadEditar);
            mIdSeccionEditar = savedInstanceState.getInt(mSiIdSeccionEditar);
            miFocusEditText = savedInstanceState.getInt(mSiFocusEditText);
            miIdSeccion = savedInstanceState.getInt(mSiIdSeccion);
            miIdUnidad = savedInstanceState.getInt(mSiIdUnidad);
            miIdEliminarLista = savedInstanceState.getInt(mSiIdEliminarLista);
            miIdEliminarProducto = savedInstanceState.getInt(mSiIdEliminarProducto);
            miIdEditarLista = savedInstanceState.getInt(mSiIdEditarLista);
            miIdEditarProducto = savedInstanceState.getInt(mSiIdEditarProducto);
            miEditarPosicion = savedInstanceState.getInt(mSiEditarPosicion);
            txtTotal.setText(mValtotal);
        } else {
            ConsultaProductos();
            ConsultaSeccion();
            ConsultaUnidadMedida();
            VerificarItems();
        }
        setTitle(mNombreLista);
        LlenarAdapterCardView();
        imgbAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarMensajeAgregar();
            }
        });
    }

    private void Init() {
        mRecyclerListaProductos = (RecyclerView) findViewById(R.id.rvListaItems);
        mTxtMensaje = (TextView) findViewById(R.id.txtMsjSinItems);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        imgbAgregar = (ImageButton) findViewById(R.id.imgbAgregar);
    }

    private void VerificarItems() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        mAlListaProductos = new ArrayList<>();
        Cursor rs = null;
        try {
            rs = db.rawQuery("select p.id_producto,p.id_lista,p.nombre_producto,s.seccion,\n" +
                    "       u.Abreviatura,p.cantidad,p.precio,p.fecha_registro\n" +
                    "       from tb_lista_productos as p\n" +
                    "       inner join tb_seccion as s on p.id_seccion=s.id_seccion\n" +
                    "       inner join tb_unidad as u on p.id_unidad=u.id_unidad\n" +
                    "       where p.id_lista=?", new String[]{String.valueOf(mIdLista)});

            if (rs.getCount() > 0) {
                mRecyclerListaProductos.setVisibility(View.VISIBLE);
                mTxtMensaje.setVisibility(View.GONE);

                while (rs.moveToNext()) {
                    int id_producto = rs.getInt(0);
                    int id_lista = rs.getInt(1);
                    String nombre_producto = rs.getString(2);
                    String seccion = rs.getString(3);
                    String unidad = rs.getString(4);
                    float cantidad = rs.getFloat(5);
                    float precio = rs.getFloat(6);
                    String fecha_registro = rs.getString(7);
                    mAlListaProductos.add(new ParceListaProductos(id_producto,
                            id_lista, nombre_producto,
                            seccion, unidad,
                            cantidad, precio, fecha_registro));
                }
                ActualizarTotal();
            } else {
                mRecyclerListaProductos.setVisibility(View.GONE);
                mTxtMensaje.setVisibility(View.VISIBLE);
            }
        } finally {
            if (db != null)
                db.close();
            if (rs != null)
                rs.close();
        }
    }

    private boolean VerificarProductoNombre(String nombre_producto){

        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select * from tb_lista_productos where nombre_producto=? and id_lista=?",
                    new String[]{nombre_producto, String.valueOf(mIdLista)});

            return rs.getCount() <= 0;

        }finally {
            if(rs!=null)
                rs.close();
            if(db!=null)
                db.close();
        }

    }

    private void LlenarAdapterCardView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        AdapterListaProductos adapter = new AdapterListaProductos(mAlListaProductos);
        adapter.onSetListenerAdapter(new AdapterListaProductos.AdapterListaProductosListener() {
            @Override
            public void onClickEditar(int position, int idLista, int idProducto) {
                miIdEditarLista = idLista;
                miIdEditarProducto = idProducto;
                miEditarPosicion = position;
                MostrarMensajeEditar();
            }

            @Override
            public void onClickEliminar(int idLista, int idProducto) {
                miIdEliminarLista = idLista;
                miIdEliminarProducto = idProducto;
                MostrarMensajeEliminar();
            }
        });
        mRecyclerListaProductos.setLayoutManager(manager);
        if (mAlListaProductos.size() > 0)
            mRecyclerListaProductos.setAdapter(adapter);
    }

    private void MostrarMensajeAgregar() {
        View view = ConfigViewAgregarProducto();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Agregar productos");
        builder.setPositiveButton("Aceptar", null);
        builder.setNeutralButton("Buscar por código", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbAgregarProducto = false;
                MostrarMensajeSeleccionarScanner();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbAgregarProducto = false;
                LimpiarItems();
            }
        });
        mDialogAgregarProducto = builder.create();
        mDialogAgregarProducto.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn = mDialogAgregarProducto.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (autNombre.getText().toString().trim().isEmpty()) {
                            mbErrorInputName = true;
                            autNombre.setError("Debe agregar el nombre del producto");
                        } else {
                            mbErrorInputName = false;
                            if(VerificarProductoNombre(autNombre.getText().toString())){
                                GuardarProducto();
                            }else {
                                Toast.makeText(mContext,"Este producto ya se encuentra agregado",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        mDialogAgregarProducto.show();
        mbAgregarProducto = true;
    }

    private void MostrarMensajeSeleccionarScanner() {
        View view = ConfigViewSelectOptionScanner();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Seleccione una opción");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbSeleccionarScaner = false;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbSeleccionarScaner = false;
                MostrarMensajeAgregar();
            }
        });
        mDialogSeleccionarScaner = builder.create();
        mDialogSeleccionarScaner.show();
        mbSeleccionarScaner = true;
    }

    private void MostrarListaUnidades(final TipoElemento tipoElemento) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_layout_list_tipounidad, null);
        AdapterListaTipoUnidad adapter = new AdapterListaTipoUnidad(mContext, mAlUnidadMedida);
        adapter.setOnListenerTipoUnidad(new AdapterListaTipoUnidad.AdapterListaUnidadListener() {
            @Override
            public void onClickItemUnidad(int id, String unidad) {
                if(tipoElemento==TipoElemento.PAGREGAR){
                    mValUnidadMedida = unidad;
                    miIdUnidad = id;
                }else {
                    mIdUnidadEditar = id;
                }

                mbSeleccionarUnidad = false;
                txtUnidadMedida.setText(unidad);
                if (mDialogSeleccionarUnidad != null)
                    mDialogSeleccionarUnidad.dismiss();
            }
        });
        ListView ls = (ListView) view.findViewById(R.id.lvSimple);
        ls.setAdapter(adapter);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(view);
        mDialogSeleccionarUnidad = alert.create();
        mDialogSeleccionarUnidad.show();
        mbSeleccionarUnidad = true;
    }

    private void MostrarListaSecciones(final TipoElemento tipoElemento) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_layout_list_tipounidad, null);
        AdapterListaSeccion adapter = new AdapterListaSeccion(mContext, mAlNombreSeccion);
        adapter.onSetInterfaceAdapter(new AdapterListaSeccion.OnListenerSeccion() {
            @Override
            public void onSelectedItem(int id, String seccion) {
                if(tipoElemento==TipoElemento.PAGREGAR){
                    mValSeccion = seccion;
                    miIdSeccion = id;
                }else {
                    mIdSeccionEditar = id;
                }
                    mbSeleccionarSeccion = false;
                if (mDialogSeleccionarSeccion != null)
                    mDialogSeleccionarSeccion.dismiss();
                    autSeccion.setText(seccion);

            }
        });
        ListView ls = (ListView) view.findViewById(R.id.lvSimple);
        ls.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        mDialogSeleccionarSeccion = builder.create();
        mDialogSeleccionarSeccion.show();
        mbSeleccionarSeccion = true;

    }

    private void MostrarMensajeEditar() {
        View view = ConfigViewEditarProducto();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Aviso");
        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton("Aceptar", null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbEditarProducto = false;
            }
        });
        mDialogEditarProducto = builder.create();
        mDialogEditarProducto.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn = mDialogEditarProducto.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(autNombre.getText().toString().trim().isEmpty()){
                            autNombre.setError("Debe agregar el nombre del producto");
                        }else {
                            mbEditarProducto = false;
                            EditarProducto();
                        }
                    }
                });
            }
        });
        mDialogEditarProducto.show();
        mbEditarProducto = true;
    }

    private View ConfigViewAgregarProducto() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_agregar_producto, null, false);

        autNombre = (AutoCompleteTextView) view.findViewById(R.id.etdNombre);
        final EditText etdCantidad = (EditText) view.findViewById(R.id.etdCantidad);
        final EditText etdPrecio = (EditText) view.findViewById(R.id.etdPrecio);
        txtUnidadMedida = (TextView) view.findViewById(R.id.txtUnidadMedida);
        autSeccion = (AutoCompleteTextView) view.findViewById(R.id.etdSeccion);
        final ImageButton imgDeleteNombre = (ImageButton) view.findViewById(R.id.imgDeleteNombre);
        final ImageButton imgDeletePrecio = (ImageButton) view.findViewById(R.id.imgDeletePrecio);

        /*Config autNombre*/
        final AutoCompleteNombreProducto adpterNombreProducto =
                new AutoCompleteNombreProducto(mContext, R.layout.custom_textview, mAlNombreProducto);
        adpterNombreProducto.onSetListener(new AutoCompleteNombreProducto.AutoCompleteNombreListener() {
            @Override
            public void onSelected(String texto) {
                autNombre.setText(texto);
                etdCantidad.requestFocus();
            }
        });
        autNombre.setAdapter(adpterNombreProducto);
        autNombre.setText(mValNombreProducto);
        if (!mValNombreProducto.trim().isEmpty()) {
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
                if (after > 0) {
                    if (imgDeleteNombre.getVisibility() == View.GONE)
                        imgDeleteNombre.setVisibility(View.VISIBLE);
                } else if ((count == 1 && start == 0) || (start == 0 && count > 0 && after == 0)) {
                    if (imgDeleteNombre.getVisibility() == View.VISIBLE)
                        imgDeleteNombre.setVisibility(View.GONE);
                    adpterNombreProducto.setmAlProductos(mAlNombreProducto);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 0) {
                    if (mbErrorInputName)
                        mbErrorInputName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mValNombreProducto = s.toString();
            }
        });
        autNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                miFocusEditText = 1;
            }
        });
        if (mbErrorInputName)
            autNombre.setError("Debe agregar el nombre del producto");

        /*Config autNombre*/

        /*Config cantidad*/
        etdCantidad.setText(mValCantidad);
        if (!mValCantidad.trim().isEmpty()) {
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
                mValCantidad = s.toString();
            }
        });
        etdCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                miFocusEditText = 2;
            }
        });
        txtUnidadMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarListaUnidades(TipoElemento.PAGREGAR);
            }
        });
        /*Config cantidad*/

        /*Config txtUnidadMedida*/
        if (!mValUnidadMedida.trim().isEmpty())
            txtUnidadMedida.setText(mValUnidadMedida);
        /*Config txtUnidadMedida*/

        /*Config precio*/
        if (mValPrecio.trim().isEmpty()) {
            etdPrecio.setText(getString(R.string.ceros));
            imgDeletePrecio.setVisibility(View.GONE);
        } else {
            etdPrecio.setText(mValPrecio);
            imgDeletePrecio.setVisibility(View.VISIBLE);
        }
        imgDeletePrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdPrecio.setText(getString(R.string.ceros));
                mValPrecio = getString(R.string.ceros);
            }
        });
        etdPrecio.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    MostrarListaSecciones(TipoElemento.PAGREGAR);
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
                if (!s.toString().equals(current)) {
                    etdPrecio.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    if (parsed > 0)
                        imgDeletePrecio.setVisibility(View.VISIBLE);
                    else
                        imgDeletePrecio.setVisibility(View.GONE);
                    String formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed / 100);

                    current = formatted;
                    mValPrecio = formatted;
                    etdPrecio.setText(formatted);
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
                miFocusEditText = 3;
            }
        });
        /*Config precio*/

        /*Config seccion*/
        autSeccion.setText(mValSeccion);
        autSeccion.setFocusable(false);
        autSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarListaSecciones(TipoElemento.PAGREGAR);
            }
        });
        /*Config seccion*/

        switch (miFocusEditText) {
            case 1:
                autNombre.requestFocus();
                break;
            case 2:
                etdCantidad.requestFocus();
                break;
            case 3:
                etdPrecio.requestFocus();
                break;
        }

        return view;
    }

    private View ConfigViewSelectOptionScanner() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_select_scanner_option, null, false);

        final ImageButton imgInput = (ImageButton) view.findViewById(R.id.imgbscanner_input);
        final ImageButton imgRead = (ImageButton) view.findViewById(R.id.imgbscanner_read);

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

    private View ConfigViewEditarProducto() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_agregar_producto, null, false);
        autNombre = (AutoCompleteTextView) view.findViewById(R.id.etdNombre);
        final EditText etdCantidad = (EditText) view.findViewById(R.id.etdCantidad);
        final EditText etdPrecio = (EditText) view.findViewById(R.id.etdPrecio);
        txtUnidadMedida = (TextView) view.findViewById(R.id.txtUnidadMedida);
        autSeccion = (AutoCompleteTextView) view.findViewById(R.id.etdSeccion);
        final ImageButton imgDeleteNombre = (ImageButton) view.findViewById(R.id.imgDeleteNombre);
        final ImageButton imgDeletePrecio = (ImageButton) view.findViewById(R.id.imgDeletePrecio);

        /*Nombre*/
        String nombre_producto =mNombreEditar.isEmpty()?mAlListaProductos.get(miEditarPosicion).getNombre_producto():mNombreEditar;
        final AutoCompleteNombreProducto adpterNombreProducto =
                new AutoCompleteNombreProducto(mContext, R.layout.custom_textview, mAlNombreProducto);
        adpterNombreProducto.onSetListener(new AutoCompleteNombreProducto.AutoCompleteNombreListener() {
            @Override
            public void onSelected(String texto) {
                autNombre.setText(texto);
                mNombreEditar=texto;
                etdCantidad.requestFocus();
            }
        });
        autNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 0) {
                    if (imgDeleteNombre.getVisibility() == View.GONE)
                        imgDeleteNombre.setVisibility(View.VISIBLE);
                        mNombreEditar="";
                } else if ((count == 1 && start == 0) || (start == 0 && count > 0 && after == 0)) {
                    if (imgDeleteNombre.getVisibility() == View.VISIBLE)
                        imgDeleteNombre.setVisibility(View.GONE);
                    adpterNombreProducto.setmAlProductos(mAlNombreProducto);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 0) {
                    if (mbErrorInputName)
                        mbErrorInputName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               mNombreEditar=s.toString();
            }
        });
        imgDeleteNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autNombre.setText("");
            }
        });
        autNombre.setText(nombre_producto);
        autNombre.setSelection(nombre_producto.length());
        autNombre.setAdapter(adpterNombreProducto);
        /*Nombre*/

        /*Cantidad*/
        String cantidad = !mCantidadEditar.isEmpty() ? mCantidadEditar : String.valueOf(mAlListaProductos.get(miEditarPosicion).getCantidad());
        String cantidad_ = new Util().ValidaEntero(cantidad);
        etdCantidad.setText(cantidad_);
        etdCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              mCantidadEditar=s.toString();
            }
        });
        etdCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etdCantidad.setSelection(etdCantidad.getText().length());
            }
        });
        /*Cantidad*/

        /*UnidadMedida*/
        String unidad =mIdUnidadEditar==-1?mAlListaProductos.get(miEditarPosicion).getUnidad():
                mAlUnidadMedida.get(mIdUnidadEditar-1).getAbreviatura();
        txtUnidadMedida.setText(unidad);
        txtUnidadMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 MostrarListaUnidades(TipoElemento.PEDITAR);
            }
        });
        /*UnidadMedida*/

        /*Precio*/
        String precio = mPrecioEditar.isEmpty()?String.valueOf(mAlListaProductos.get(miEditarPosicion).getPrecio() * 10):mPrecioEditar;
        etdPrecio.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    etdPrecio.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    if (parsed > 0)
                        imgDeletePrecio.setVisibility(View.VISIBLE);
                    else
                        imgDeletePrecio.setVisibility(View.GONE);
                    String formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed / 100);

                    current = formatted;
                    etdPrecio.setText(formatted);
                    mPrecioEditar=formatted;
                    etdPrecio.setSelection(formatted.length());
                    etdPrecio.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etdPrecio.setText(precio);
        imgDeletePrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etdPrecio.setText(getString(R.string.ceros));
                imgDeletePrecio.setVisibility(View.GONE);
            }
        });
        /*Precio*/

        /*Seccion*/

        /*Config seccion*/
        String seccion =mIdSeccionEditar==-1?mAlListaProductos.get(miEditarPosicion).getSeccion():
               mAlNombreSeccion.get(mIdSeccionEditar-1).getNombreSeccion();
        autSeccion.setText(seccion);
        autSeccion.setFocusable(false);
        autSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarListaSecciones(TipoElemento.PEDITAR);
            }
        });
        /*Config seccion*/
        /*Seccion*/


        return view;
    }

    private void ConsultaProductos() {
        mAlNombreProducto = new ArrayList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor rs = null;
        try {
            rs = db.rawQuery("select * from tb_productos", null);
            while (rs.moveToNext()) {
                int id = rs.getInt(0);
                String nombre = rs.getString(1);
                mAlNombreProducto.add(new ParceNombreProducto(id, nombre));
            }

        } finally {
            if (rs != null)
                rs.close();
            db.close();
        }
    }

    private void ConsultaSeccion() {
        mAlNombreSeccion = new ArrayList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor rs = null;
        try {
            rs = db.rawQuery("select * from tb_seccion", null);
            while (rs.moveToNext()) {
                int id = rs.getInt(0);
                String nombre = rs.getString(1);
                mAlNombreSeccion.add(new ParceNombreSeccion(id, nombre));
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
    }

    private void ConsultaUnidadMedida() {
        mAlUnidadMedida = new ArrayList<>();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor rs = null;
        try {
            rs = db.rawQuery("select * from tb_unidad", null);
            while (rs.moveToNext()) {
                int id = rs.getInt(0);
                String unidad = rs.getString(1);
                String abreviatura = rs.getString(2);
                mAlUnidadMedida.add(new ParceUnidadMedida(id, unidad, abreviatura));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
    }

    private void DimissAlertOnResume(boolean onResume) {
        if (onResume) {
            /*Alert agregar producto*/
            if (mbAgregarProducto) {
                if (mDialogAgregarProducto == null)
                    MostrarMensajeAgregar();
                else if (!mDialogAgregarProducto.isShowing())
                    MostrarMensajeAgregar();
            }
            /*Alert seleccionar escaner*/
            if (mbSeleccionarScaner) {
                if (mDialogSeleccionarScaner == null)
                    MostrarMensajeSeleccionarScanner();
                else if (!mDialogSeleccionarScaner.isShowing())
                    MostrarMensajeSeleccionarScanner();
            }

            /*Alert pregunta eliminar*/
            if (mbEliminarProducto) {
                if (mDialogElimianarProducto == null)
                    MostrarMensajeEliminar();
                else if (!mDialogElimianarProducto.isShowing())
                    MostrarMensajeEliminar();
            }

            /*Alert editar producto*/
            if (mbEditarProducto) {
                if (mDialogEditarProducto == null)
                    MostrarMensajeEditar();
                else if (!mDialogEditarProducto.isShowing())
                    MostrarMensajeEditar();
            }
             /*Alert seleccionar unidad*/
            if (mbSeleccionarUnidad) {
                if (mDialogSeleccionarUnidad == null)
                    MostrarListaUnidades(TipoElemento.PAGREGAR);
                else if (!mDialogSeleccionarUnidad.isShowing())
                    MostrarListaUnidades(TipoElemento.PAGREGAR);
            }

            /*Alert seleccionar seccion*/
            if (mbSeleccionarSeccion) {
                if (mDialogSeleccionarSeccion == null)
                    MostrarListaSecciones(TipoElemento.PAGREGAR);
                else if (!mDialogSeleccionarSeccion.isShowing())
                    MostrarListaSecciones(TipoElemento.PAGREGAR);
            }
        } else {
            if (mbAgregarProducto)
                mDialogAgregarProducto.dismiss();
            if (mbSeleccionarScaner)
                mDialogSeleccionarScaner.dismiss();
            if (mbSeleccionarUnidad)
                mDialogSeleccionarUnidad.dismiss();
            if (mbSeleccionarSeccion)
                mDialogSeleccionarSeccion.dismiss();
            if (mbEliminarProducto)
                mDialogElimianarProducto.dismiss();
            if (mbEditarProducto)
                mDialogEditarProducto.dismiss();
        }
    }

    private void LimpiarItems() {
        mValNombreProducto = "";
        mValCantidad = "";
        mValPrecio = "";
        mValSeccion = "";
        miFocusEditText = -1;
        miIdUnidad = 1;
    }

    private void GuardarProducto() {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("id_lista", mIdLista);
            values.put("nombre_producto", mValNombreProducto);
            values.put("id_seccion", miIdSeccion);
            values.put("id_unidad", miIdUnidad);
            values.put("cantidad", mValCantidad);
            values.put("precio", mValPrecio.replace("$", "").replace(",", ""));

            miIdSeccion = 30;

            long val = db.insertOrThrow("tb_lista_productos", null, values);

            if (val > 0) {
                VerificarItems();
                LlenarAdapterCardView();
                LimpiarItems();
                mbAgregarProducto = false;
                mDialogAgregarProducto.dismiss();
            } else {
                Toast.makeText(mContext, "No se pudo guardar el producto. Intente mas tarde", Toast.LENGTH_SHORT).show();
            }

        } finally {
            if (db != null)
                db.close();
        }
    }

    private void ActualizarTotal() {
        if (mAlListaProductos.size() > 0) {
            double total = 0.00;
            for (ParceListaProductos productos : mAlListaProductos) {
                float cantidad = productos.getCantidad();
                float precio = productos.getPrecio();
                total += cantidad * precio;
            }
            mValtotal = "$" + new Util().decimalFormat(String.valueOf(total));
            txtTotal.setText(mValtotal);
        }
    }

    private void MostrarMensajeEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Aviso");
        builder.setMessage("¿Desea eliminar el producto?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbEliminarProducto = false;
                EliminarProducto();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mbEliminarProducto = false;
            }
        });
        mDialogElimianarProducto = builder.create();
        mDialogElimianarProducto.show();
        mbEliminarProducto = true;
    }

    private void EliminarProducto() {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        try {

            int val = db.delete("tb_lista_productos", "id_producto=? and id_lista=?",
                    new String[]{String.valueOf(miIdEliminarProducto),
                            String.valueOf(miIdEliminarLista)});
            if (val > 0) {
                Toast.makeText(mContext, "Producto eliminado correctemente", Toast.LENGTH_SHORT).show();
                VerificarItems();
                LlenarAdapterCardView();
            }
        } finally {
            if (db != null)
                db.close();
        }
    }

    private void EditarProducto(){
        SQLiteDatabase db=sqLiteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("nombre_producto",mNombreEditar);
        values.put("id_seccion",mIdSeccionEditar);
        values.put("id_unidad",mIdUnidadEditar);
        values.put("cantidad",mCantidadEditar);
        values.put("precio",mPrecioEditar);

        int val=db.update("tb_lista_productos",values,"",new String[]{});

        VerificarItems();

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
        outState.putBoolean(mSiAgregarProducto, mbAgregarProducto);
        outState.putBoolean(mSiSeleccionarScaner, mbSeleccionarScaner);
        outState.putBoolean(mSiSeleccionarUnidad, mbSeleccionarUnidad);
        outState.putBoolean(mSiSeleccionarSeccion, mbSeleccionarSeccion);
        outState.putBoolean(mSiErrorInputName, mbErrorInputName);
        outState.putBoolean(mSiEliminarProducto, mbEliminarProducto);
        outState.putBoolean(mSiEditarProducto, mbEditarProducto);
        outState.putParcelableArrayList(mSiAlNombreProducto, mAlNombreProducto);
        outState.putParcelableArrayList(mSiAlNombreSeccion, mAlNombreSeccion);
        outState.putParcelableArrayList(mSiAlUnidadMedida, mAlUnidadMedida);
        outState.putParcelableArrayList(mSiAlListaProductos, mAlListaProductos);
        outState.putString(mSiNombreProducto, mValNombreProducto);
        outState.putString(mSiCantidad, mValCantidad);
        outState.putString(mSiSeccion, mValSeccion);
        outState.putString(mSiPrecio, mValPrecio);
        outState.putString(mSiUnidadMedida, mValUnidadMedida);
        outState.putString(mSiValtotal, mValtotal);
        outState.putString(mSiNombreEditar,mNombreEditar);
        outState.putString(mSiCantidadEditar,mCantidadEditar);
        outState.putString(mSiPrecioEditar,mPrecioEditar);
        outState.putInt(mSiIdUnidadEditar,mIdUnidadEditar);
        outState.putInt(mSiIdSeccionEditar,mIdSeccionEditar);
        outState.putInt(mSiFocusEditText, miFocusEditText);
        outState.putInt(mSiIdSeccion, miIdSeccion);
        outState.putInt(mSiIdUnidad, miIdUnidad);
        outState.putInt(mSiIdEditarLista, miIdEditarLista);
        outState.putInt(mSiIdEliminarLista, miIdEliminarLista);
        outState.putInt(mSiIdEditarProducto, miIdEditarProducto);
        outState.putInt(mSiIdEliminarProducto, miIdEliminarProducto);
        outState.putInt(mSiEditarPosicion, miEditarPosicion);
    }

}

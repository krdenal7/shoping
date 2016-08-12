package com.kds.multiapps.shoping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kds.multiapps.shoping.Activitys.ProductosActivity;
import com.kds.multiapps.shoping.Adapters.AdapterListaCompras;
import com.kds.multiapps.shoping.CallRequest.RequestWebView;
import com.kds.multiapps.shoping.DataBase.SQLiteHelper;
import com.kds.multiapps.shoping.Enums.EnumTipoTienda;
import com.kds.multiapps.shoping.Parserables.DataHolder.DHListaCompras;
import com.kds.multiapps.shoping.Parserables.ParceWebView;
import com.kds.multiapps.shoping.Utils.SearchCode;
import com.kds.multiapps.shoping.Utils.Logs;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RequestWebView.RequestWebViewListener{

    OkHttpClient client = new OkHttpClient();
    private ProgressDialog progressBar;
    private final String TAG=getClass().getSimpleName();
    private Context mContext;
    private RequestWebView mRequestWebView;

    private final boolean isLoadSearch=false;
    private boolean isShowInputNameM=false;
    private boolean isShowMsgDeleteItemB=false;
    private final String mItemDelete="mItemDelete";
    private final String mItemDeleteB="mItemDeleteB";
    private final String isLoadDialogSearch="isLoadSearch";
    private final String ParcerableWebView="parcerableWebView";
    private final String isShowInputName="isShowInputName";
    private final String mMensajeInputDialog="mMensajeInputDialog";
    private static String mMensajeInputName="";
    private static int mIdItem;

    private ArrayList<DHListaCompras> mAListacompras;

    private SQLiteHelper sqLiteHelper;


    private AlertDialog dialogInputName;
    private AlertDialog dialogDelete;

    private ImageButton mImgbAgregar;
    private RecyclerView mRecyclerCompras;
    private TextView mTxtMensaje;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        sqLiteHelper=new SQLiteHelper(mContext);
        sqLiteHelper.OpenDataBase();

        if(savedInstanceState!=null){
             mMensajeInputName=savedInstanceState.getString(mMensajeInputDialog);
             if(savedInstanceState.getBoolean(isLoadDialogSearch)) {
                 ShowLoad();
                 ParceWebView parcelable=savedInstanceState.getParcelable(ParcerableWebView);
                 mRequestWebView=parcelable.getRequestWebView();
             }
            if(savedInstanceState.getBoolean(isShowInputName)){
                MostrarAlertaAgregar();
            }
            if(savedInstanceState.getBoolean(mItemDeleteB)){
                MostrarAlertEliminar();
            }
        }

        Init();
        VerificarItems();
        mImgbAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarAlertaAgregar();
            }
        });

    }

    private void MostrarAlertaAgregar(){

        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_input_name_list,null,false);

        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        final EditText editText=(EditText)view.findViewById(R.id.input);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMensajeInputName=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setText(mMensajeInputName);
        editText.setHint("Nombre");

        builder.setCancelable(false);
        builder.setView(view);
        builder.setTitle("Agergar lista");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowInputNameM=false;
                mMensajeInputName="";
                if(AgregarLista(editText.getText().toString()))
                 StartActivityProductos(getLastId(),editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowInputNameM=false;
                mMensajeInputName="";
            }
        });

        dialogInputName=builder.create();
        dialogInputName.show();
        isShowInputNameM=true;

    }

    private void StartActivityProductos(int id,String nombre){
        Intent intent=new Intent(mContext, ProductosActivity.class);
        intent.putExtra("idLista",id);
        intent.putExtra("Nombre",nombre);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        startActivity(intent);
    }

    private void MostrarAlertEliminar(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("Aviso");
        builder.setMessage("¿Desea eliminar la lista?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowMsgDeleteItemB=false;
                EliminarItems();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowMsgDeleteItemB=false;
            }
        });
        dialogDelete=builder.create();
        dialogDelete.show();
        isShowMsgDeleteItemB=true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(isLoadDialogSearch,isLoadSearch);
        outState.putBoolean(isShowInputName,isShowInputNameM);
        outState.putString(mMensajeInputDialog,mMensajeInputName);
        outState.putBoolean(mItemDeleteB,isShowMsgDeleteItemB);
        outState.putInt(mItemDelete,mIdItem);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ValidaResumPauseAlert(true);
        ValidaResumePauseEliminar(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ValidaResumPauseAlert(false);
        ValidaResumePauseEliminar(false);
    }

    private void Init(){
       /* etBuscar=(EditText)findViewById(R.id.editText);
        btnBuscar=(Button)findViewById(R.id.button);*/
        mRecyclerCompras=(RecyclerView)findViewById(R.id.rvListaItems);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        mRecyclerCompras.setLayoutManager(llm);
        mTxtMensaje=(TextView)findViewById(R.id.txtMsjSinItems);
        mImgbAgregar=(ImageButton)findViewById(R.id.imgbAgregar);

        if(mRequestWebView==null)
            mRequestWebView=new RequestWebView(mContext);
        mRequestWebView.setOnWebViewListener(this);
        mRequestWebView.setJavascriptInterface(new LoadListener(),"HTMLOUT");
    }

    private void VerificarItems(){
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs = null;
        try {
            if (db != null) {
                rs = db.rawQuery("select * from tb_lista_compras", null);
                if (rs != null) {
                    if (rs.getCount() > 0) {
                        mRecyclerCompras.setVisibility(View.VISIBLE);
                        mTxtMensaje.setVisibility(View.GONE);
                        CargaListas();
                    } else {
                        mRecyclerCompras.setVisibility(View.GONE);
                        mTxtMensaje.setVisibility(View.VISIBLE);
                    }
                }
            }
        }finally {
            if(rs!=null)
                rs.close();
            if(db!=null)
                db.close();
        }
    }

    private void EliminarItems(){
         SQLiteDatabase db=sqLiteHelper.getWritableDatabase();
         int val=db.delete("tb_lista_compras","id_lista=?",new String[]{String.valueOf(mIdItem)});
         if(val>0){
             Toast.makeText(mContext,"Lista eliminada correctamente",Toast.LENGTH_SHORT).show();
         }
         VerificarItems();
    }

    private int getLastId(){
        int id=-1;

        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select max(id_lista) from tb_lista_compras",null);

            if(rs.moveToFirst()){
                id=rs.getInt(0);
            }
        }finally {
            if(db!=null)
                db.close();
            if(rs!=null)
                rs.close();
        }

        return id;
    }

    private boolean AgregarLista(String nombre){
       SQLiteDatabase db=sqLiteHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("nombre_lista",nombre);
        values.put("total_piezas",0);
        values.put("monto_total",0.00);
        long val=db.insert("tb_lista_compras",null,values);
        if(val>0){
            VerificarItems();
            db.close();
            return true;
        }else {
            Toast.makeText(mContext,"No se pudo agregar la lista.",Toast.LENGTH_SHORT).show();
            db.close();
            return false;
        }

    }

    private void CargaListas(){
        SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        mAListacompras=new ArrayList<>();
        Cursor rs=null;
        try{
            rs=db.rawQuery("select * from tb_lista_compras",null);

            while (rs.moveToNext()){
                int id=rs.getInt(0);
                String nombre=rs.getString(1);
                int piezas=rs.getInt(2);
                double total=rs.getDouble(3);
                mAListacompras.add(new DHListaCompras(id,nombre,piezas,total));
            }
            CargaAdapterRecycler();
        }finally {
           if(rs!=null)
               rs.close();
            if(db!=null)
                db.close();
        }

    }

    private void CargaAdapterRecycler(){
        AdapterListaCompras adapterListaCompras=new AdapterListaCompras(mAListacompras);
        adapterListaCompras.onSetAdapterListaComprasListener(new AdapterListaCompras.AdapterListaComprasListener() {
            @Override
            public void onDeleteItem(int Id) {
                mIdItem=Id;
                MostrarAlertEliminar();
            }

            @Override
            public void onClikItemList(int id, String nombre) {
                StartActivityProductos(id,nombre);
            }
        });
        mRecyclerCompras.setAdapter(adapterListaCompras);
    }

    private void BusquedaPrincipal(EnumTipoTienda tipoTienda,String texto){
        switch (tipoTienda){
             case TWALMART:
                 RequestWalmart(texto);
                 break;
             case TSUPERAMA:
                 break;
             case TSORIANA:
                 break;
             case TSAMS:
                 break;
             case TCOMERCIALM:
                 break;
         }
    }

    private void RequestWalmart(String texto){
        mRequestWebView.LoadPageRequest(
                getString(R.string.Walmart),
                R.array.RequestWalmart,
                EnumTipoTienda.TWALMART,
                texto,
                "0");
    }

    private void SearchLinkProducto(final String link, final EnumTipoTienda tipoTienda){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (tipoTienda){
                    case TSUB_WALMART:

                        mRequestWebView.LoadPageSimpleLink(getString(R.string.Walmart)+link,EnumTipoTienda.TSUB_WALMART);
                        break;
                }
            }
        });

    }

    @Override
    public void onPageFinishedLoad(WebView view, String url, EnumTipoTienda tienda) {
        switch (tienda){
            case TWALMART:
                view.loadUrl("javascript:window.HTMLOUT.productWalmart(document.getElementsByClassName('pnlMid')[0].innerHTML);");
                break;
            case TSUB_WALMART:
                view.loadUrl("javascript:window.HTMLOUT.processSubProductWalmart(" +
                              "document.getElementById('lblTitle')==null?'SinInfo':document.getElementById('lblTitle').innerHTML," +
                              "document.getElementById('lblPromocion')==null?'SinInfo':document.getElementById('lblPromocion').innerHTML," +
                              "document.getElementById('lblPrice')==null?'SinInfo':document.getElementById('lblPrice').innerHTML)");
                break;
        }

    }

    @Override
    public void onPageErrorLoad(WebView view, WebResourceRequest request, WebResourceError error,EnumTipoTienda tienda) {
        DimissLoad();
        Toast.makeText(mContext,"Error",Toast.LENGTH_SHORT).show();
        new Logs().LogI(TAG,error.toString());
    }

    private void ShowLoad(){
        progressBar=ProgressDialog.show(mContext,"Aviso","Buscando producto",true,false);
    }

    private void DimissLoad(){
        if(progressBar!=null)
            if(progressBar.isShowing())
                progressBar.dismiss();
    }

    private void ValidaResumPauseAlert(boolean resume){
          if(resume){
               if(isShowInputNameM){
                   if(dialogInputName==null){
                       MostrarAlertaAgregar();
                   }else if(!dialogInputName.isShowing()){
                       MostrarAlertaAgregar();
                   }
               }

          }else {
              if(isShowInputNameM){
                    dialogInputName.dismiss();
              }
          }
    }

    private void ValidaResumePauseEliminar(boolean resume){
        if(resume){
            if(isShowMsgDeleteItemB){
                if(dialogDelete==null){
                    MostrarAlertEliminar();
                }else if (!dialogDelete.isShowing()){
                    MostrarAlertEliminar();
                }
            }
        }else {
            if(isShowMsgDeleteItemB){
                dialogDelete.dismiss();
            }
        }

    }

    private class LoadListener{

        @JavascriptInterface
        public void productWalmart(String html) throws IOException {
            new Logs().LogI(TAG,html);
            String link=  new SearchCode().searchLink(html);
            SearchLinkProducto(link,EnumTipoTienda.TSUB_WALMART);
        }

        @JavascriptInterface
        public void processSubProductWalmart(String titulo,String promocion,String precio){
            DimissLoad();
            new Logs().LogI(TAG,titulo+"----"+promocion+"-----"+precio);
        }

    }

    public void ObtenerArchivos2(){


        File directorio = new File(getString(R.string.path_db));
        File[] files=directorio.listFiles();
        CopiarArchivos2(files);
    }

    private void CopiarArchivos2(File[] files){
        byte[] buffer=new byte[1024];
        int length;
        FileOutputStream myOuput=null;
        try {

            FileInputStream myInput;

            File folder = android.os.Environment.getExternalStorageDirectory();
            File directorio2 = new File(folder.getAbsolutePath());


            for (File file : files) {
                try {
                    myInput = new FileInputStream(file);
                    String archivo = file.getName();
                    myOuput = new FileOutputStream(directorio2 + "/" + archivo);
                    while ((length = myInput.read(buffer)) > 0) {
                        myOuput.write(buffer, 0, length);
                    }


                    myInput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if(myOuput!=null)
            myOuput.close();
            if(myOuput!=null)
            myOuput.flush();
        }
        catch (Exception e){
            Log.e("ErrorCopiar:",e.toString());
        }
    }

}


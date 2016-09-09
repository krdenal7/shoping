package com.kds.multiapps.shoping.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.kds.multiapps.shoping.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Isaac Martinez on 29/07/2016.
 * shoping
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String dbName="db_compras.s3db";
    private final String db_path;
    private static final int db_version=1;
    private final Context context;

    public SQLiteHelper(Context context) {
        super(context, dbName, null, db_version);
        this.context=context;
        db_path=context.getString(R.string.path_db);
    }

    private boolean VerificaBD(){
        File path=new File(db_path);
        if(!path.exists())
              path.mkdirs();

        File file_db=new File(db_path+dbName);
        if(!file_db.exists()){
            try {
                if(file_db.createNewFile()){
                    return CopiaBD();
                }else {
                    Toast.makeText(context,"Error al crear base de datos",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (IOException e) {
                Toast.makeText(context,"Error al crear base de datos",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean CopiaBD(){
        try {
            InputStream inputStream=context.getAssets().open("DataBase/db_compras.s3db");
            OutputStream outputStream=new FileOutputStream(db_path+dbName);


            byte[] buffer=new byte[1024];
            int lenght;
            while ((lenght=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,lenght);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            ReiniciarIndices();
            CargarProductos();
            CargarSeccion();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void CargarProductos(){
        try {
            InputStream stream=context.getAssets().open("DataBase/productos.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String valor;
            SQLiteDatabase db=getWritableDatabase();
            db.beginTransaction();

            while ((valor = reader.readLine()) != null) {
                ContentValues values=new ContentValues();
                values.put("Descripcion",valor);
                db.insert("tb_productos",null,values);
            }
            reader.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CargarSeccion(){
        try {
            InputStream stream=context.getAssets().open("DataBase/secciones.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String valor;
            SQLiteDatabase db=getWritableDatabase();
            db.beginTransaction();

            while ((valor = reader.readLine()) != null) {
                ContentValues values=new ContentValues();
                values.put("seccion",valor);
                db.insert("tb_seccion",null,values);
            }
            reader.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReiniciarIndices(){
        SQLiteDatabase db=getWritableDatabase();
        if(db!=null){
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='tb_productos'");
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='tb_lista_compras'");
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='tb_lista_productos'");
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='tb_seccion'");
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='tb_unidad'");
        }
    }

    public SQLiteDatabase OpenDataBase(){
       if(VerificaBD()){
            return SQLiteDatabase.openDatabase(db_path+dbName,null,SQLiteDatabase.OPEN_READWRITE);
       }else {
           return null;
       }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

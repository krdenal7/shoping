package com.kds.multiapps.shoping.CallRequest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kds.multiapps.shoping.Enums.EnumTipoTienda;
import com.kds.multiapps.shoping.Utils.Logs;

/**
 * Created by Isaac Martinez on 12/07/2016.
 * shoping
 */
public class RequestWebView {

    private final Context mContext;
    private final WebView webView;
    private final String TAG=getClass().getSimpleName();
    RequestWebViewListener listener;

    public  RequestWebView(Context mContext){
           this.mContext=mContext;
                webView=new WebView(mContext);
                webView.getSettings().setJavaScriptEnabled(true);
       }

    public void LoadPageRequest(final String link, int resourcemethod, final EnumTipoTienda tipoTienda, String...params){

                StringBuilder builder=new StringBuilder();
                builder.append(link);
                String[] keys=mContext.getResources().getStringArray(resourcemethod);
                //Se agregan los parametros
                for(int i=0;i<keys.length;i++){
                      if(i==0){
                          builder.append(keys[i]).append("?");
                      }else {
                          builder.append(String.format("%s=%s&",keys[i],params[i-1]));
                      }
                }


                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        listener.onPageFinishedLoad(view,url,tipoTienda);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        listener.onPageErrorLoad(view,request,error,tipoTienda);
                    }
                });
                new Logs().LogI(TAG,builder.toString());
                webView.loadUrl(builder.toString());
       }

    public void LoadPageSimpleLink(String link, final EnumTipoTienda tipoTienda){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                listener.onPageFinishedLoad(view,url,tipoTienda);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                listener.onPageErrorLoad(view,request,error,tipoTienda);
            }


        });
        new Logs().LogI(TAG,link);
        webView.loadUrl(link);
    }

    @SuppressLint("JavascriptInterface")
    public void setJavascriptInterface(Object object, String name){
           webView.addJavascriptInterface(object,name);
    }

    public void setOnWebViewListener(RequestWebViewListener listener){
        this.listener=listener;
    }

    public interface RequestWebViewListener{
             void onPageFinishedLoad(WebView view, String url,EnumTipoTienda tipoTienda);
             void onPageErrorLoad(WebView view, WebResourceRequest request, WebResourceError error,EnumTipoTienda tipoTienda);
    }

}

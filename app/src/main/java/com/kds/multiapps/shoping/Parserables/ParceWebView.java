package com.kds.multiapps.shoping.Parserables;

import android.os.Parcel;
import android.os.Parcelable;

import com.kds.multiapps.shoping.CallRequest.RequestWebView;

/**
 * Created by Isaac Martinez on 13/07/2016.
 * shoping
 */
public class ParceWebView implements Parcelable {

    private RequestWebView requestWebView;

    private ParceWebView(RequestWebView requestWebView) {
        this.requestWebView = requestWebView;
    }

    public RequestWebView getRequestWebView() {
        return requestWebView;
    }

    public void setRequestWebView(RequestWebView requestWebView) {
        this.requestWebView = requestWebView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(dest.readParcelable(ParceWebView.class.getClassLoader()), flags);
    }

    private ParceWebView(Parcel in) {
        this.requestWebView = in.readParcelable(RequestWebView.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParceWebView> CREATOR = new Parcelable.Creator<ParceWebView>() {
        @Override
        public ParceWebView createFromParcel(Parcel source) {
            return new ParceWebView(source);
        }

        @Override
        public ParceWebView[] newArray(int size) {
            return new ParceWebView[size];
        }
    };
}

package com.kk_cards.BroadcastReceiver;

/**
 * Created by user on 3/8/2018.
 */

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
    String cartquantity;

    public String getCartquantity() {
        return cartquantity;
    }

    public void setCartquantity(String cartquantity) {
        this.cartquantity = cartquantity;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkStatusChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkStatusChangeReceiver.connectivityReceiverListener = listener;
    }
}
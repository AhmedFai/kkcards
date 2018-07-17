package com.kk_cards.Modal;

import android.app.Application;

/**
 * Created by user on 3/19/2018.
 */

public class MyApplication extends Application {


    public String getCart_counter() {
        return cart_counter;
    }

    public void setCart_counter(String cart_counter) {
        this.cart_counter = cart_counter;
    }

    private String cart_counter;


}

package com.kk_cards.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk_cards.R;
import com.kk_cards.SessionManagement;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 1/29/2018.
 */

public class account_settings extends Fragment {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;



    @BindView(R.id.mobile)
    TextView mobile;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.change_pwd)
    TextView change_pwd;

    @BindView(R.id.update_account)
    TextView update_account;

    SessionManagement session;


    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.account_settings, container, false);
        ButterKnife.bind(this, v);


        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
        mobile.setText(user.get(SessionManagement.KEY_MOBILE));





        return v;

    }

    @OnClick(R.id.change_pwd)
    protected void set_name(){

        change_password grid = new change_password();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();

    }


    @OnClick(R.id.update_account)
    protected void update(){

        update_account grid = new update_account();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).commit();

    }
}

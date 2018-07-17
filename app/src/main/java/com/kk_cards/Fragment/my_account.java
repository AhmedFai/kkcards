package com.kk_cards.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.R;
import com.kk_cards.SessionManagement;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 1/29/2018.
 */

public class my_account extends Fragment {

    @BindView(R.id.acount_setting)
    TextView acount_setting;

    @BindView(R.id.orders)
    TextView orders;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.log_out)
    TextView log_out;



    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SessionManagement session;
    @BindView(R.id.name_edit)
    TextView name;


    @BindView(R.id.email)
    TextView email_data;

    @BindView(R.id.my_orders)
    CardView my_orders;
    @BindView(R.id.my_address)
    CardView my_address;
    @BindView(R.id.account_seeting)
    CardView account_seeting;
    @BindView(R.id.log_outt)
    CardView log_outt;


    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.my_account, container, false);
        ButterKnife.bind(this, v);

        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
        name.setText(user.get(SessionManagement.KEY_NAME));

        // email
       email_data.setText(user.get(SessionManagement.KEY_EMAIL));

        return v;

    }

    @OnClick(R.id.name_edit)
    protected void set_name(){

        account_settings grid = new account_settings();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




    }

    @OnClick(R.id.account_seeting)
    protected void setMy_account(){

        account_settings grid = new account_settings();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




    }
    @OnClick(R.id.my_address)
    protected void address(){

        my_addresses grid = new my_addresses();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




    }

    @OnClick(R.id.my_orders)
    protected void orderss(){

        com.kk_cards.Fragment.my_orders grid = new my_orders();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




    }

    @OnClick(R.id.log_outt)
    protected void log_out(){
        if (session.isLoggedIn()==true) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage("Are you sure you want to logout of this app?");
            alertDialogBuilder.setPositiveButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {


                        }
                    });

            alertDialogBuilder.setNegativeButton("YES",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Logout.. Please Wait");
                            progressDialog.show();


                            // TODO: Implement your own authentication logic here.

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            session.logoutUser();
                                            // onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }

        else
        {

            Toast.makeText(getActivity(), "Please Login", Toast.LENGTH_SHORT).show();
        }



    }

}

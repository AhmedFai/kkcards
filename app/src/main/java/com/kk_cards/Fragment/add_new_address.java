package com.kk_cards.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2/7/2018.
 */

public class add_new_address extends Fragment {

    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.state)
    EditText state;
    @BindView(R.id.pincode)
    EditText pincode;
    @BindView(R.id.landmark)
    EditText landmark;
    @BindView(R.id.flat_no)
    EditText flat_no;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.alter_phone)
    EditText alter_phone;
    @BindView(R.id.radiogrp)
    RadioGroup radiogrp;

    @BindView(R.id.radio1)
    RadioButton home_radio;
    @BindView(R.id.radio2)
    RadioButton work_radio;

    @BindView(R.id.save)
    Button save;

    String address_type;

    SessionManagement session;

    String key_mobile;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    String check,address_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_address, container, false);
        ButterKnife.bind(this, v);
        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
        key_mobile=user.get(SessionManagement.KEY_MOBILE);



        radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.radio1:
                        address_type = "Home";
                        break;

                    case R.id.radio2:
                        address_type = "Work";
                        break;
                }
            }
        });



         check=getArguments().getString("check_value");
        address_id=getArguments().getString("address_id");

        if("edit".equals(check))
        {

            city.setText(getArguments().getString("city"));
            state.setText(getArguments().getString("state"));
            pincode.setText(getArguments().getString("pincode"));
            area.setText(getArguments().getString("area"));
            flat_no.setText(getArguments().getString("flat_no"));

            name.setText(getArguments().getString("name"));
            phone.setText(getArguments().getString("phone"));
            alter_phone.setText(getArguments().getString("alternat_phone"));
            landmark.setText(getArguments().getString("landmark"));


            if("Home".equals(getArguments().getString("address_type")))
            {
                home_radio.setChecked(true);

            }
            else
            {

                work_radio.setChecked(true);
            }



        }



        return v;
    }

    @OnClick(R.id.save)

    public void save()
    {

        if(city.length()==0)
            city.setError("Please fill necessary details");
       else if(area.length()==0)
            area.setError("Please fill necessary details");
       else if(flat_no.length()==0)
            flat_no.setError("Please fill necessary details");
       else if(pincode.length()==0)
            pincode.setError("Please fill necessary details");
       else if(state.length()==0)
            state.setError("Please fill necessary details");
      else  if(name.length()==0)
            name.setError("Please fill necessary details");
       else if(phone.length()==0)
            phone.setError("Please fill necessary details");
      else   if(address_type==null)
            Toast.makeText(getActivity(), "Please Choose address type", Toast.LENGTH_SHORT).show();


      else

        {


            if ("add".equals(check)) {

                add_address(new CallBack() {
                    @Override
                    public void onSuccess(String data) {

                        Log.d("addresssssssssss", data);
                        try {
                            JSONObject obj = new JSONObject(data);
                            String success_val = obj.getString("success");

                            if ("true".equals(success_val)) {


                                Toast.makeText(getContext(), "Address Added", Toast.LENGTH_SHORT).show();
                                my_addresses grid = new my_addresses();
                                mFragmentManager = getActivity().getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.containerView, grid).commit();
                                mFragmentManager.popBackStack();
                            } else {

                                Toast.makeText(getContext(), "Address not Added", Toast.LENGTH_SHORT).show();
                                my_addresses grid = new my_addresses();
                                mFragmentManager = getActivity().getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.containerView, grid).commit();
                                mFragmentManager.popBackStack();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data


                        Log.d("jhvfff", data);


                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(String msg) {

                        Log.d("jhvfff", "failed");
                        // Do Stuff
                    }
                });


            }

            else
            {

                edit_address(new CallBack() {
                    @Override
                    public void onSuccess(String data) {

                        Log.d("addresssssssssss", data);
                        try {
                            JSONObject obj = new JSONObject(data);
                            String success_val = obj.getString("success");

                            if ("true".equals(success_val)) {


                                Toast.makeText(getContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                                my_addresses grid = new my_addresses();
                                mFragmentManager = getActivity().getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.containerView, grid).commit();
                                mFragmentManager.popBackStack();

                            } else {

                                Toast.makeText(getContext(), "Address not Updated", Toast.LENGTH_SHORT).show();
                                my_addresses grid = new my_addresses();
                                mFragmentManager = getActivity().getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.containerView, grid).commit();
                                mFragmentManager.popBackStack();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data


                        Log.d("jhvfff", data);


                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(String msg) {

                        Log.d("jhvfff", "failed");
                        // Do Stuff
                    }
                });
            }

        }


    }

    private void add_address(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/insert_add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("addressssss",response);

                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile",key_mobile);
                params.put("city",city.getText().toString());
                params.put("locality", area.getText().toString());
                params.put("flatno",flat_no.getText().toString());
                params.put("pincode", pincode.getText().toString());

                params.put("state",state.getText().toString());
                params.put("landmark", landmark.getText().toString());

                params.put("name",name.getText().toString());
                params.put("amobile", phone.getText().toString());

                params.put("omobile",alter_phone.getText().toString());
                params.put("addtype", address_type);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void edit_address(final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/insert_add.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("addressssss",response);

                        //  callback.onSuccessResponse(response);
                      /*  SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("hello",response);
                        editor.commit();
*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Please check your network connection and try again", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile",key_mobile);
                params.put("city",city.getText().toString());
                params.put("locality", area.getText().toString());
                params.put("flatno",flat_no.getText().toString());
                params.put("pincode", pincode.getText().toString());

                params.put("state",state.getText().toString());
                params.put("landmark", landmark.getText().toString());

                params.put("name",name.getText().toString());
                params.put("amobile", phone.getText().toString());

                params.put("omobile",alter_phone.getText().toString());
                params.put("addtype", address_type);
                params.put("add_id", address_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}
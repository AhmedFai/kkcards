package com.kk_cards;

/**
 * Created by user on 3/14/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class add_address_activity extends AppCompatActivity {

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
    String cart_counter_real=null;
    SessionManagement session;

    String key_mobile;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    String check,address_id;
    private String MY_PREFS_NAME;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        // name
        key_mobile=user.get(SessionManagement.KEY_MOBILE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }


        this.setTitle("Add New Address");


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



      check=getIntent().getStringExtra("check_value");
        address_id=getIntent().getStringExtra("address_id");

        if("edit".equals(check))
        {

            city.setText(getIntent().getStringExtra("city"));
            state.setText(getIntent().getStringExtra("state"));
            pincode.setText(getIntent().getStringExtra("pincode"));
            area.setText(getIntent().getStringExtra("area"));
            flat_no.setText(getIntent().getStringExtra("flat_no"));

            name.setText(getIntent().getStringExtra("name"));
            phone.setText(getIntent().getStringExtra("phone"));
            alter_phone.setText(getIntent().getStringExtra("alternat_phone"));
            landmark.setText(getIntent().getStringExtra("landmark"));

           // Log.d("quannnnn",getIntent().getStringExtra("qunatity"));


            if("Home".equals(getIntent().getStringExtra("address_type")))
            {
                home_radio.setChecked(true);

            }
            else
            {

                work_radio.setChecked(true);
            }





        }



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
            Toast.makeText(getApplicationContext(), "Please Choose address type", Toast.LENGTH_SHORT).show();


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


                                Toast.makeText(getApplicationContext(), "Address Added", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), select_address.class);

                                i.putExtra("address","shipping_address");
                                i.putExtra("qunatity","");
                                i.putExtra("check","cart_to");     startActivity(i);
                            } else {

                                Toast.makeText(getApplicationContext(), "Address not Added", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), select_address.class);
                                i.putExtra("address","shipping_address");
                                i.putExtra("qunatity","");
                                i.putExtra("check",getIntent().getStringExtra("check"));      startActivity(i);

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


                                Toast.makeText(getApplicationContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), select_address.class);
                                i.putExtra("address","shipping_address");
                                i.putExtra("qunatity","");
                                i.putExtra("check","cart_to");
                                startActivity(i);
                            } else {

                                Toast.makeText(getApplicationContext(), "Address not Updated", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), select_address.class);
                                i.putExtra("address","shipping_address");
                                i.putExtra("qunatity","");
                                i.putExtra("check","cart_to"); startActivity(i);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
      final TextView  cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);



        MenuItem item = menu.findItem(R.id.action_search);

        cart_icon.setVisibility(View.GONE);
        item.setVisible(false);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

}
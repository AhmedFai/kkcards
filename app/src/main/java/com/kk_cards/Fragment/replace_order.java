package com.kk_cards.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 4/2/2018.
 */

public class replace_order extends Fragment implements AdapterView.OnItemSelectedListener{

    @BindView(R.id.select_reason)
    Spinner select_reason_spin;
    @BindView(R.id.comments)
    EditText tracking_id;
    @BindView(R.id.service_type)
    EditText service_type;
    String reason,id_value;
    @BindView(R.id.cancel_order)
    Button replace_order;

    ArrayList<String> courier_list,id_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cancel_order, container, false);
        ButterKnife.bind(this, v);

        tracking_id.setHint("Tracking Id");
        replace_order.setText("Replace Order");
        service_type.setVisibility(View.VISIBLE);

        courier_list=new ArrayList<>();
        id_list=new ArrayList<>();

        courier_list.add("Courier Name");
        id_list.add("");

        getservicecorrier_name();


        select_reason_spin.setOnItemSelectedListener(this);

        ArrayAdapter aa1 = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,courier_list);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        select_reason_spin.setAdapter(aa1);










        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       reason=select_reason_spin.getSelectedItem().toString();
        id_value=id_list.get(select_reason_spin.getSelectedItemPosition());


    }


    @OnClick(R.id.cancel_order)
    protected void cancel_order() {


        if("Courier Name".equals(reason))
        {

            Toast.makeText(getActivity(), "Please select a courier name", Toast.LENGTH_LONG).show();

        }

        else  if(tracking_id.length()==0)
        {

            Toast.makeText(getActivity(), "Please enter tracking id", Toast.LENGTH_LONG).show();

        }   else {

            final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait....");
            progressDialog.show();


            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            cancel_order(getArguments().getString("treansaction_id"), getArguments().getString("product_id"), new CallBack() {
                                @Override
                                public void onSuccess(String data) {

                                    //  Log.d("loginnnnninnnter", data);
                                    try {
                                        JSONObject obj = new JSONObject(data);
                                        String success_val = obj.getString("success");

                                        if ("true".equals(success_val)) {

                                            Toast.makeText(getContext(), "Applied for replacement Successfully", Toast.LENGTH_SHORT).show();

                                            my_orders grid=new my_orders();


                                            android.support.v4.app.FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                                            mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




                                        } else {
                                            Toast.makeText(getContext(), "Replacement not applied, please try again", Toast.LENGTH_SHORT).show();


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                @Override
                                public void onFail(String msg) {

                                    Toast.makeText(getContext(), "Order not cancelled, please try again", Toast.LENGTH_SHORT).show();
                                    Log.d("jhvfff", "failed");
                                    // Do Stuff
                                }
                            });


                            progressDialog.dismiss();
                        }
                    }, 3000);
        }}
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void cancel_order(final String txn_id,final String product_id, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/returnProductApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);

                        Log.d("loginnnnnnnnn",response);

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
                params.put("txnid",txn_id);
                params.put("productID",product_id);
                params.put("trackingID",tracking_id.getText().toString());
                params.put("courierID",id_value);
                params.put("serviceType",service_type.getText().toString());



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getservicecorrier_name(){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.Base_Url+"/API/courierListApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj=new JSONObject(response);
                            JSONArray product_list=obj.getJSONArray("courier");

                            for(int i=0;i<product_list.length();i++) {
                                JSONObject objj = product_list.getJSONObject(i);
                                courier_list.add(objj.getString("courierName"));
                                id_list.add(objj.getString("id"));



                            }




                        }
                        catch (JSONException e)
                        {


                        }


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
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

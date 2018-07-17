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

public class cancel_order extends Fragment implements AdapterView.OnItemSelectedListener{

    @BindView(R.id.select_reason)
    Spinner select_reason_spin;
    @BindView(R.id.comments)
    EditText comments;
    String reason;
    @BindView(R.id.cancel_order)
    Button cancel_order;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cancel_order, container, false);
        ButterKnife.bind(this, v);

        ArrayList<String> reason_list=new ArrayList<>();
        reason_list.add("Select Reason");
        reason_list.add("Need to change shipping details");
        reason_list.add("Order placed by mistake");
        reason_list.add("The delivery is delayed");
        reason_list.add("Expected Delivery is too long");
        reason_list.add("Item price/shipping cost is high");
        reason_list.add("Bought it from somewhere else");
        reason_list.add("My reason is not listed");


        select_reason_spin.setOnItemSelectedListener(this);

        ArrayAdapter aa1 = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,reason_list);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        select_reason_spin.setAdapter(aa1);










        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        reason=select_reason_spin.getSelectedItem().toString();
        Log.d("resooooo",reason);

    }


    @OnClick(R.id.cancel_order)
    protected void cancel_order() {


        if("Select Reason".equals(reason))
        {

            Toast.makeText(getActivity(), "Please select a reason", Toast.LENGTH_LONG).show();

        }
          //  cancel_order(getArguments().getString("treansaction_id"),getArguments().getString("product_id"));
        else {

            final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cancelling Order...");
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

                                            Toast.makeText(getContext(), "Order cancelled Successfully", Toast.LENGTH_SHORT).show();

                                            my_orders grid=new my_orders();


                                           android.support.v4.app.FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                                           android.support.v4.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                                            mFragmentTransaction.replace(R.id.containerView,grid).addToBackStack(null).commit();




                                        } else {
                                            Toast.makeText(getContext(), "Order not cancelled, please try again", Toast.LENGTH_SHORT).show();


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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/orderCancellationApi.php",
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
                params.put("ctxnid",txn_id);
                params.put("productID",product_id);
                params.put("pStatus",reason);
                params.put("comment",comments.getText().toString());



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

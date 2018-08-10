package com.kk_cards.Adapter;

/**
 * Created by user on 2/8/2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.Fragment.add_new_address;
import com.kk_cards.Fragment.my_addresses;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.R;
import com.kk_cards.add_address_activity;
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



public class display_add_adapter extends RecyclerView.Adapter<display_add_adapter.ViewHolder> implements View.OnClickListener {
    // private ItemData[] itemsData;
    public ArrayList<ItemData> os_versions;
    View itemLayoutView;
    ItemData fp;
    Context mContext;
    int viewtype;
    String check_value,address_check;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;



    private int lastCheckedPosition = -1;
    public display_add_adapter(Context context, ArrayList<ItemData> itemsData, String value, String address_check) {

        this.os_versions = itemsData;
        this.mContext = context;
        this.check_value=value;
        this.address_check=address_check;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_recycle_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        fp = os_versions.get(position);


        viewHolder.name.setText(fp.getCat_name());
        viewHolder.address.setText(fp.getFlat_no()+" , "+fp.getArea()+" , "+fp.getCity()+" , "+fp.getState()+" - "+fp.getPincode()+" , "+fp.getLandmark());
        viewHolder.phone.setText(fp.getMobile());





        if("select_address".equals(check_value))
        {

            viewHolder.linear_layout.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.GONE);
            viewHolder.edit_address.setVisibility(View.VISIBLE);

            viewHolder.linear.setVisibility(View.VISIBLE);
            viewHolder.name.setVisibility(View.GONE);

            viewHolder.name1.setText(fp.getCat_name());



        }


//        Log.d("dddddddddddd",address_check);

        viewHolder.radioButton.setChecked(position == lastCheckedPosition);
        if(lastCheckedPosition!=-1) {
               //   Log.d("name_checked", os_versions.get(lastCheckedPosition).getCat_name());




                  if("shipping_address".equals(address_check)) {

                      SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                      editor.putString("name", os_versions.get(lastCheckedPosition).getCat_name());
                      editor.putString("phone", os_versions.get(lastCheckedPosition).getMobile());
                      editor.putString("address", fp.getFlat_no() + " , " + os_versions.get(lastCheckedPosition).getArea() + " , " + os_versions.get(lastCheckedPosition).getCity() + " , " + os_versions.get(lastCheckedPosition).getState() + " - " + os_versions.get(lastCheckedPosition).getPincode() + " , " + os_versions.get(lastCheckedPosition).getLandmark());
                      editor.putString("billing_name", os_versions.get(lastCheckedPosition).getCat_name());
                      editor.putString("billing_phone", os_versions.get(lastCheckedPosition).getMobile());
                      editor.putString("billing_address", fp.getFlat_no() + " , " + os_versions.get(lastCheckedPosition).getArea() + " , " + os_versions.get(lastCheckedPosition).getCity() + " , " + os_versions.get(lastCheckedPosition).getState() + " - " + os_versions.get(lastCheckedPosition).getPincode() + " , " + os_versions.get(lastCheckedPosition).getLandmark());
                      editor.putString("billing_address_id", os_versions.get(lastCheckedPosition).getId());
                      editor.putString("radio_select", "yes");
                      editor.putString("address_id", os_versions.get(lastCheckedPosition).getId());
                      editor.apply();
                      editor.commit();

                  }

                  else if("billing_address".equals(address_check))
                  {

                      SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                      editor.putString("billing_name", os_versions.get(lastCheckedPosition).getCat_name());
                      editor.putString("billing_phone", os_versions.get(lastCheckedPosition).getMobile());
                      editor.putString("billing_address", fp.getFlat_no() + " , " + os_versions.get(lastCheckedPosition).getArea() + " , " + os_versions.get(lastCheckedPosition).getCity() + " , " + os_versions.get(lastCheckedPosition).getState() + " - " + os_versions.get(lastCheckedPosition).getPincode() + " , " + os_versions.get(lastCheckedPosition).getLandmark());
                      editor.putString("radio_select", "yes");
                      editor.putString("billing_address_id", os_versions.get(lastCheckedPosition).getId());
                      editor.commit();

                  }

              }

              else
        {

            SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("radio_select", "no");
            editor.commit();

        }


     //   Log.d("fffffff", String.valueOf(os_versions.size()));

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if("select_address".equals(check_value)) {
                Intent i=new Intent(mContext,add_address_activity.class);
                    i.putExtra("city", os_versions.get(position).getCity());
                    i.putExtra("area", os_versions.get(position).getArea());
                    i.putExtra("flat_no", os_versions.get(position).getFlat_no());
                    i.putExtra("pincode", os_versions.get(position).getPincode());
                    i.putExtra("state", os_versions.get(position).getState());
                    i.putExtra("landmark", os_versions.get(position).getLandmark());
                    i.putExtra("name", os_versions.get(position).getCat_name());
                    i.putExtra("phone", os_versions.get(position).getMobile());
                    i.putExtra("alternat_phone", os_versions.get(position).getAlter_mobile());
                    i.putExtra("check_value", "edit");
                    i.putExtra("address_type", os_versions.get(position).getAdd_type());
                    i.putExtra("address_id", os_versions.get(position).getId());



                    mContext.startActivity(i);


                }

                else {


                    add_new_address grid = new add_new_address();

                    Bundle arg = new Bundle();
                    arg.putString("city", os_versions.get(position).getCity());
                    arg.putString("area", os_versions.get(position).getArea());
                    arg.putString("flat_no", os_versions.get(position).getFlat_no());
                    arg.putString("pincode", os_versions.get(position).getPincode());
                    arg.putString("state", os_versions.get(position).getState());
                    arg.putString("landmark", os_versions.get(position).getLandmark());
                    arg.putString("name", os_versions.get(position).getCat_name());
                    arg.putString("phone", os_versions.get(position).getMobile());
                    arg.putString("alternat_phone", os_versions.get(position).getAlter_mobile());
                    arg.putString("check_value", "edit");
                    arg.putString("address_type", os_versions.get(position).getAdd_type());

                    arg.putString("address_id", os_versions.get(position).getId());

                    grid.setArguments(arg);

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();
                }


            }
        });

        viewHolder.edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,add_address_activity.class);
                i.putExtra("city",os_versions.get(position).getCity());
                i.putExtra("area",os_versions.get(position).getArea());
                i.putExtra("flat_no",os_versions.get(position).getFlat_no());
                i.putExtra("pincode",os_versions.get(position).getPincode());
                i.putExtra("state",os_versions.get(position).getState());
                i.putExtra("landmark",os_versions.get(position).getLandmark());
                i.putExtra("name",os_versions.get(position).getCat_name());
                i.putExtra("phone",os_versions.get(position).getMobile());
                i.putExtra("alternat_phone",os_versions.get(position).getAlter_mobile());
                i.putExtra("check_value","edit");
                i.putExtra("address_id",os_versions.get(position).getId());
                i.putExtra("address_type", os_versions.get(position).getAdd_type());

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(i);




            }
        });


        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Do you want to remove ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final ProgressDialog progressDialog = new ProgressDialog(mContext,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Removing.. Please Wait");
                                progressDialog.show();


                                // TODO: Implement your own authentication logic here.

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {


                                                delete_address(os_versions.get(position).getId(),new CallBack() {
                                                        @Override
                                                        public void onSuccess(String data) {

                                                            Log.d("loginnnnninnnter",data);
                                                            try {
                                                                JSONObject obj=new JSONObject(data);
                                                                String success_val=obj.getString("success");

                                                                if ("true".equals(success_val))
                                                                {
                                                                    Toast.makeText(mContext, "Address removed", Toast.LENGTH_SHORT).show();

                                                                    my_addresses grid = new my_addresses();
                                                                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, grid).addToBackStack(null).commit();




                                                                }
                                                                else
                                                                {

                                                                    Toast.makeText(mContext, "Address not removed", Toast.LENGTH_SHORT).show();


                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                            // Creating user login session
                                                            // For testing i am stroing name, email as follow
                                                            // Use user real data



                                                            Log.d("jhvfff",data);


                                                            //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onFail(String msg) {

                                                            //  Toast.makeText(LoginActivity.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                                                            Log.d("jhvfff","failed");
                                                            // Do Stuff
                                                        }
                                                    });

                                                    // onLoginFailed();








                                                progressDialog.dismiss();
                                            }
                                        }, 3000);


                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });



    }






    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.phone) TextView phone;
        @BindView(R.id.edit) TextView edit;
        @BindView(R.id.remove)
        TextView remove;
        @BindView(R.id.edit_address)
        Button edit_address;

        @BindView(R.id.name1)
        TextView name1;

        @BindView(R.id.linear)
        LinearLayout linear;

        @BindView(R.id.linear_layout)
        LinearLayout linear_layout;
        @BindView(R.id.view1)
        View view1;

        @BindView(R.id.radio)
        RadioButton radioButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public void onClick(View v) {


    }


    @Override

    public int getItemCount() {
        return os_versions.size();
    }

    private void delete_address(final String id, final CallBack onCallBack){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url+"/API/removeAddressApi.php",
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
                params.put("addID",id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

}

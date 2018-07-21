package com.kk_cards.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kk_cards.Config;
import com.kk_cards.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by user on 3/26/2018.
 */

public class product_specification extends Fragment {
    View v;

    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.general)
    TextView general;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.warentyy)
    TextView warentyy;
    @BindView(R.id.table1)
    TableLayout table;

    @BindView(R.id.table2)
    TableLayout table2;
    @BindView(R.id.scroll)
    ScrollView scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.product_specification, container, false);
        ButterKnife.bind(this, v);


      //  Log.d("descccccccc",getArguments().getString("id_value"));

        if("specification".equals(getArguments().getString("check_value")))
        {

            description.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
            table.setVisibility(View.VISIBLE);
            table2.setVisibility(View.VISIBLE);
        }

        else
        {
            general.setVisibility(View.GONE);
            table.setVisibility(View.GONE);
            table2.setVisibility(View.GONE);
            warentyy.setVisibility(View.GONE);



        }
        get_product_detail(Config.Base_Url+"/API/productDetailApi.php?product_id="+getArguments().getString("id_value"));


        return v;
    }

    public void get_product_detail(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {



                        ArrayList<String> dim=new ArrayList<>();
                        ArrayList<String> dim_val=new ArrayList<>();

                        ArrayList<String> war=new ArrayList<>();
                        ArrayList<String> war_val=new ArrayList<>();

                        war.add("Domestic Warranty");
                        //war.add("International Warranty");
                        war.add("Covered in Warranty");
                        war.add("Not Covered in Warranty");
                        war.add("Warranty Service Type");





                        try {
                            JSONObject obj=new JSONObject(response);

                            JSONArray banner_array=obj.getJSONArray("products");


                            if (obj.has("description")) {
                                JSONArray description_array = obj.getJSONArray("description");
                                String value = "";

                                for (int i = 0; i < description_array.length(); i++) {
                                    JSONObject objj = description_array.getJSONObject(i);

                                    value = value + Html.fromHtml("<p>" + Html.fromHtml(objj.getString("productDesc")) + "</p>");


                                }
                                description.setText(value);
                            }

                            else
                            {

                                desc.setVisibility(View.GONE);
                                description.setTextSize(20);
                                description.setGravity(Gravity.CENTER);
                                description.setText("No Description available");


                            }


                            for(int i=0;i<banner_array.length();i++)
                            {

                                dim.add("In the Box");
                                dim.add("Item Weight");
                                JSONObject objj=banner_array.getJSONObject(i);



                              dim_val.add(objj.getString("inTheBox"));
                                dim_val.add(objj.getString("pWeight"));
                                war_val.add(objj.getString("intranationalWarranty"));
                                //war_val.add(objj.getString("internationalWarranty"));
                                war_val.add("Manufacturing Defects Only");
                                war_val.add("Physical Damage");
                                war_val.add("Replacement");


                            }


                            if (obj.has("general")) {
                                JSONArray general = obj.getJSONArray("general");

                                for (int i = 0; i < general.length(); i++) {
                                    JSONObject objj = general.getJSONObject(i);
                                    dim.add(objj.getString("generalKey"));


                                    dim_val.add(objj.getString("generalValue"));


                                }
                            }

                            else
                            {

                                general.setVisibility(View.GONE);
                            }


                                table.removeAllViews();
                                table2.removeAllViews();


                                for (int i = 0; i < dim.size(); i++) {
                                    // Inflate your row "template" and fill out the fields.
                                    TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.table_row_layout, null);
                                    ((TextView) row.findViewById(R.id.attrib_name)).setText(dim.get(i));
                                    ((TextView) row.findViewById(R.id.attrib_value)).setText(dim_val.get(i));

                                    row.setBackgroundResource(R.drawable.table_border);


                                    table.addView(row, i);
                                }
                                table.requestLayout();




                            for (int i = 0; i < war.size(); i++) {
                                // Inflate your row "template" and fill out the fields.
                                TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.table_row_layout, null);
                                ((TextView) row.findViewById(R.id.attrib_name)).setText(war.get(i));
                                ((TextView) row.findViewById(R.id.attrib_value)).setText(war_val.get(i));

                                row.setBackgroundResource(R.drawable.table_border);


                                table2.addView(row, i);
                            }
                            table2.requestLayout();


                     scroll.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Result handling
                    Log.d("sub_category",response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();


            }
        });

// Add the request to the queue
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

}
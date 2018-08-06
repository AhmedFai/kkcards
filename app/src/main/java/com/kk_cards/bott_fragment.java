package com.kk_cards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Fragment.product_details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kk_cards.Fragment.product_details.cartcounterTV;

public class bott_fragment extends Fragment implements View.OnClickListener {


    LinearLayout lin1, lin2, lin3, mainLin, mainLin1;

    TextView device, lense, both, simple, medium, high, piece1, piece2, piece3, money, minus, plus, countText;

    String catId, product_id;

    String step;

    String cardType,price_txt,name,price_cut_txt,dicount_txt,image;
    int cardId,quantity;

    product_details pd;

    String amount[];

    int quant = 1;

    String cart_counter_real = null;

    SessionManagement session;

   // BottomSheetBehavior sheetBehavior;
    Boolean visibility_cart = false;
    public static Button add_to_cart;


    public void updateArray(String amount[])
    {
        this.amount = amount;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet , container , false);
        ButterKnife.bind(this, view);

        session = new SessionManagement(getContext());

        pd = ((product_details)getActivity());

        catId = getArguments().getString("catid");
        price_txt = getArguments().getString("priceTxt");
        product_id = getArguments().getString("product");
        name = getArguments().getString("name");
        price_cut_txt = getArguments().getString("price_cut_txt");
        dicount_txt = getArguments().getString("dicount_txt");
        image = getArguments().getString("image");
        quantity = Integer.parseInt(getArguments().getString("quantity"));
        cardType = getArguments().getString("cardType");




      //  sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        add_to_cart = (Button) view.findViewById(R.id.add_cart);

        lin1 = (LinearLayout) view.findViewById(R.id.lin1);
        lin2 = (LinearLayout) view.findViewById(R.id.lin2);
        lin3 = (LinearLayout) view.findViewById(R.id.lin3);
        money = (TextView) view.findViewById(R.id.money);
        minus = (TextView) view.findViewById(R.id.minus);
        countText = (TextView) view.findViewById(R.id.counting);
        plus = (TextView) view.findViewById(R.id.plus);
        device = (TextView) view.findViewById(R.id.forDevice);
        device.setOnClickListener(this);
        lense = (TextView) view.findViewById(R.id.forLence);
        lense.setOnClickListener(this);
        both = (TextView) view.findViewById(R.id.forBoth);
        both.setOnClickListener(this);
        mainLin = (LinearLayout) view.findViewById(R.id.mainLin);
        mainLin1 = (LinearLayout) view.findViewById(R.id.mainLin1);
        simple = (TextView) view.findViewById(R.id.simple);

        simple.setOnClickListener(this);
        medium = (TextView) view.findViewById(R.id.medium);
        medium.setOnClickListener(this);
        high = (TextView) view.findViewById(R.id.high);
        high.setOnClickListener(this);
        piece1 = (TextView) view.findViewById(R.id.piece1);
        piece1.setOnClickListener(this);
        piece2 = (TextView) view.findViewById(R.id.piece6);
        piece2.setOnClickListener(this);
        piece3 = (TextView) view.findViewById(R.id.piece12);
        piece3.setOnClickListener(this);



        if ("1".equals(catId)) {

            Log.d("main", "main wala");
            mainLin.setVisibility(View.VISIBLE);

        } else {

            Log.d("duantity", "quantity wala");
            mainLin1.setVisibility(View.VISIBLE);
            //cardId = 50;

        }

        money.setText( price_txt);



        if (session.isLoggedIn() == true) {
            cart_counter(new CallBack() {
                @Override
                public void onSuccess(String data) {


                    try {
                        JSONObject obj = new JSONObject(data);
                        cart_counter_real = obj.getString("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (session.isLoggedIn() == true) {
                        if (!"0".equals(cart_counter_real)) {
                            cartcounterTV.setVisibility(View.VISIBLE);
                            cartcounterTV.setText(cart_counter_real);
                        } else
                            cartcounterTV.setVisibility(View.GONE);

                    }


                }

                @Override
                public void onFail(String msg) {
                    //  Log.d("jhvfff", "failed");

                }
            });

        } else {
            DatabaseHandler db = new DatabaseHandler(getContext());

            String cart_value = String.valueOf(db.count_rows());


            if (!"0".equals(cart_value)) {
                cartcounterTV.setVisibility(View.VISIBLE);
                cartcounterTV.setText(cart_value);
            } else
                cartcounterTV.setVisibility(View.GONE);


        }



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(countText.getText().toString()) <= 4) {

                    int counter = Integer.parseInt(countText.getText().toString());

                    counter = counter + 1;

                    countText.setText(Integer.toString(counter));

                    int grand_total_adpter = 0, total_without_del = 0;

                    grand_total_adpter = Integer.parseInt(countText.getText().toString()) * Integer.parseInt(price_txt);

                    money.setText( Integer.toString(grand_total_adpter));


                } else {
                    Toast.makeText(getContext(), "You can only select maximum 5 items", Toast.LENGTH_SHORT).show();
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = Integer.parseInt(countText.getText().toString());
                if (counter == 1) {
                    counter = 1;
                } else {
                    counter = counter - 1;
                }
                countText.setText(Integer.toString(counter));

                int grand_total_adpter = 0, total_without_del = 0;

                grand_total_adpter = Integer.parseInt(countText.getText().toString()) * Integer.parseInt(price_txt);

                money.setText(Integer.toString(grand_total_adpter));
            }
        });

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forDevice: {

                step = "device";

                device.setBackgroundResource(R.drawable.background_blue);
                device.setTextColor(Color.parseColor("#ffffff"));
                lense.setBackgroundResource(R.drawable.background);
                lense.setTextColor(Color.parseColor("#125688"));
                both.setBackgroundResource(R.drawable.background);
                both.setTextColor(Color.parseColor("#125688"));
                lin2.setVisibility(View.VISIBLE);
                lin3.setVisibility(View.GONE);


                if (cardType.equals("plastic")) {
                    simple.setEnabled(false);
                    simple.setBackgroundResource(R.drawable.background_grey);
                    simple.setTextColor(Color.parseColor("#e2e2e2"));
                    medium.setEnabled(false);
                    medium.setBackgroundResource(R.drawable.background_grey);
                    medium.setTextColor(Color.parseColor("#e2e2e2"));
                }


                break;
            }

            case R.id.forLence: {

                step = "lense";

                lense.setBackgroundResource(R.drawable.background_blue);
                lense.setTextColor(Color.parseColor("#ffffff"));
                device.setBackgroundResource(R.drawable.background);
                device.setTextColor(Color.parseColor("#125688"));
                both.setBackgroundResource(R.drawable.background);
                both.setTextColor(Color.parseColor("#125688"));
                lin2.setVisibility(View.GONE);
                lin3.setVisibility(View.VISIBLE);

                break;

            }

            case R.id.forBoth: {

                step = "both";

                int b = Integer.parseInt(amount[12]);

                cardId = 12;

                Log.d("amount ki position", String.valueOf(cardId));

                money.setText(String.valueOf(b));


                device.setBackgroundResource(R.drawable.background);
                device.setTextColor(Color.parseColor("#125688"));
                lense.setBackgroundResource(R.drawable.background);
                lense.setTextColor(Color.parseColor("#125688"));
                both.setBackgroundResource(R.drawable.background_blue);
                both.setTextColor(Color.parseColor("#ffffff"));
                lin2.setVisibility(View.GONE);
                lin3.setVisibility(View.GONE);
                break;

            }

            case R.id.simple: {

                step = "simple";

                high.setBackgroundResource(R.drawable.background);
                high.setTextColor(Color.parseColor("#125688"));
                medium.setBackgroundResource(R.drawable.background);
                medium.setTextColor(Color.parseColor("#125688"));
                simple.setBackgroundResource(R.drawable.background_blue);
                simple.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                lin3.setVisibility(View.VISIBLE);
                break;

            }

            case R.id.medium: {

                step = "medium";

                high.setBackgroundResource(R.drawable.background);
                high.setTextColor(Color.parseColor("#125688"));
                simple.setBackgroundResource(R.drawable.background);
                simple.setTextColor(Color.parseColor("#125688"));
                medium.setBackgroundResource(R.drawable.background_blue);
                medium.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                lin3.setVisibility(View.VISIBLE);
                break;

            }

            case R.id.high: {

                step = "high";

                simple.setBackgroundResource(R.drawable.background);
                simple.setTextColor(Color.parseColor("#125688"));
                medium.setBackgroundResource(R.drawable.background);
                medium.setTextColor(Color.parseColor("#125688"));
                high.setBackgroundResource(R.drawable.background_blue);
                high.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                lin3.setVisibility(View.VISIBLE);


                if (cardType.equals("plastic")) {
                    simple.setEnabled(false);
                    simple.setBackgroundResource(R.drawable.background_grey);
                    simple.setTextColor(Color.parseColor("#e2e2e2"));
                    medium.setEnabled(false);
                    medium.setBackgroundResource(R.drawable.background_grey);
                    medium.setTextColor(Color.parseColor("#e2e2e2"));
                }


                break;

            }

            case R.id.piece1: {

                switch (step) {
                    case "lense": {

                        // int a = Integer.parseInt(price_txt);
                        int b = Integer.parseInt(amount[0]);

                        cardId = 0;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[3]);

                        cardId = 3;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[6]);

                        cardId = 6;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[9]);

                        cardId = 9;

                        money.setText(String.valueOf(b));

                        break;
                    }
                }


                piece2.setBackgroundResource(R.drawable.background);
                piece2.setTextColor(Color.parseColor("#125688"));
                piece3.setBackgroundResource(R.drawable.background);
                piece3.setTextColor(Color.parseColor("#125688"));
                piece1.setBackgroundResource(R.drawable.background_blue);
                piece1.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                //lin3.setVisibility(View.VISIBLE);
                break;

            }

            case R.id.piece6: {

                switch (step) {
                    case "lense": {

                        // int a = Integer.parseInt(price_txt);
                        int b = Integer.parseInt(amount[1]);

                        cardId = 1;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[4]);

                        cardId = 4;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[7]);

                        cardId = 7;

                        money.setText( String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[10]);

                        cardId = 10;

                        money.setText( String.valueOf(b));

                        break;
                    }
                }


                piece1.setBackgroundResource(R.drawable.background);
                piece1.setTextColor(Color.parseColor("#125688"));
                piece3.setBackgroundResource(R.drawable.background);
                piece3.setTextColor(Color.parseColor("#125688"));
                piece2.setBackgroundResource(R.drawable.background_blue);
                piece2.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                //lin3.setVisibility(View.VISIBLE);
                break;

            }

            case R.id.piece12: {

                switch (step) {
                    case "lense": {

                        // int a = Integer.parseInt(price_txt);
                        int b = Integer.parseInt(amount[2]);

                        cardId = 2;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[5]);

                        cardId = 5;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[8]);

                        cardId = 8;

                        money.setText(String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[11]);

                        cardId = 11;

                        money.setText(String.valueOf(b));

                        break;
                    }
                }


                piece1.setBackgroundResource(R.drawable.background);
                piece1.setTextColor(Color.parseColor("#125688"));
                piece2.setBackgroundResource(R.drawable.background);
                piece2.setTextColor(Color.parseColor("#125688"));
                piece3.setBackgroundResource(R.drawable.background_blue);
                piece3.setTextColor(Color.parseColor("#ffffff"));
                //lin2.setVisibility(View.GONE);
                //lin3.setVisibility(View.VISIBLE);
                break;

            }

        }

    }


    @OnClick(R.id.add_cart)
    public void add_cart() {




        if (session.isLoggedIn() == true) {

            if (visibility_cart == false) {


                JSONObject jo = new JSONObject();


                try {

                    Collection<JSONObject> items = new ArrayList<JSONObject>();


                    for (int i = 0; i < 1; i++) {

                        JSONObject item1 = new JSONObject();
                        item1.put("product_id", product_id);

                        if ("1".equals(catId)) {
                            Log.d("Marked", "marked");
                            item1.put("quantity", String.valueOf(quant));
                            item1.put("cardID", String.valueOf(cardId));
                        } else {
                            item1.put("quantity", countText.getText().toString());
                            item1.put("cardID", String.valueOf(50));
                            Log.d("poker", "poker");
                        }
                        item1.put("price", money.getText().toString());
                        items.add(item1);
                    }

                    jo.put("cart_data", new JSONArray(items));

                    Log.d("jsonmaidata", new JSONArray(items).toString());

                    System.out.println(jo.toString());
                } catch (Exception e) {

                    e.printStackTrace();


                }

                Log.d("checkJO", String.valueOf(jo));

                Log.d("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                add_cart(String.valueOf(jo), new CallBack() {

                    @Override
                    public void onSuccess(String data) {

                        Log.d("loginnnnninnnter", data);
                        try {
                            JSONObject obj = new JSONObject(data);
                            String success_val = obj.getString("success");

                            if ("true".equals(success_val)) {
                                DatabaseHandler db = new DatabaseHandler(getActivity());
                                Boolean result = db.insert_for_go_to_cart(product_id);

                                //   Log.d("hjffhui", String.valueOf(result));

                                Toast.makeText(getContext(), " added in cart", Toast.LENGTH_SHORT).show();
                                add_to_cart.setText("GO TO CART");


                                onResume();

                                AppRater.showRateDialog(getContext());


                                // AppRater.app_launched(product_details.this);

                      /*   Intent i=new Intent(getApplicationContext(), com.active_india.Fragment.add_to_cart.class);
                         startActivity(i);*/
                                //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);






                            } else {

                                Log.d("checkLog", "firstwala");
                                Intent i = new Intent(getContext(), com.kk_cards.Fragment.add_to_cart.class);
                                i.putExtra("test", "");
                                startActivity(i);
                                Toast.makeText(getContext(), "Item already exist in cart", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("jhvfff", data);


                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(String msg) {

                        Toast.makeText(getContext(), "Invalid Login Details", Toast.LENGTH_SHORT).show();
                        Log.d("jhvfff", "failed");
                        // Do Stuff
                    }
                });

            } else {

                Intent i = new Intent(getContext(), com.kk_cards.Fragment.add_to_cart.class);

                Log.d("checkLog", "Secondwala");
                i.putExtra("test", "");
                startActivity(i);


            }

        } else {


            if (!visibility_cart) {

                DatabaseHandler db = new DatabaseHandler(getContext());

                Boolean result = db.insert(name, price_txt, price_cut_txt, "1", dicount_txt, product_id, image);

                Log.d("count", product_id);
                //((MyApplication)this.getApplication()).setCartquantity(String.valueOf(quantity));

                SharedPreferences preferences = getContext().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Name", quantity);
                editor.apply();

                if (result) {


                    Toast.makeText(getContext(), "Item Added to cart", Toast.LENGTH_SHORT).show();
                    db.insert_for_go_to_cart_logout(product_id);

                    add_to_cart.setText("GO TO CART");


                    onResume();
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            /*   Intent i = new Intent(getApplicationContext(), add_to_cart.class);
               startActivity(i);*/
                } else {
                    Log.d("checkLog", "thirdwala");
                    Intent i = new Intent(getContext(), com.kk_cards.Fragment.add_to_cart.class);
                    i.putExtra("test", "");
                    startActivity(i);
                    Toast.makeText(getContext(), "Item Already available in cart", Toast.LENGTH_SHORT).show();

               /*Intent i = new Intent(getApplicationContext(), add_to_cart.class);
               startActivity(i);*/
                }
            } else {

                Log.d("checkLog", "forthwala");

                Intent i = new Intent(getContext(), com.kk_cards.Fragment.add_to_cart.class);
                i.putExtra("test", "");
                startActivity(i);


            }
        }


    }


    private void add_cart(final String cart_data, final CallBack onCallBack) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/cartApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onCallBack.onSuccess(response);
                        Log.d("derailss", response);

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cart", cart_data);
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));


                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);


                }

                return super.parseNetworkResponse(response);
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    protected void cart_counter(final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/cartCounterApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssssssssss", response);


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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}

package com.kk_cards.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.kk_cards.Adapter.no_product_adapter;
import com.kk_cards.Adapter.review_Adapter;
import com.kk_cards.All_reviews;
import com.kk_cards.AppRater;
import com.kk_cards.CallBack;
import com.kk_cards.Config;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Listener.RecyclerItemClickListener;
import com.kk_cards.LoginActivity;
import com.kk_cards.Modal.ItemData;
import com.kk_cards.NonScrollListView;
import com.kk_cards.R;
import com.kk_cards.SessionManagement;
import com.kk_cards.check_out_buy_now;
import com.kk_cards.search_activity;
import com.kk_cards.youtube_video;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 1/23/2018.
 */

public class product_details extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions, review_versions, rate_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @BindView(R.id.bottom)
    TextView btnBottomSheet;

    LinearLayout lin1, lin2, lin3, mainLin, mainLin1;

    TextView device, lense, both, simple, medium, high, piece1, piece2, piece3, money, minus, plus, countText;

    ImageView cancel;

    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    public static Button add_to_cart;
    /*  @BindView(R.id.buy_now)
      TextView buy_now;*/
    @BindView(R.id.footer)
    RelativeLayout footer;
    ArrayList<String> feature_list;
    @BindView(R.id.progressBar)
    ProgressBar p_bar;

    @BindView(R.id.scrollableContents)
    ScrollView scrollableContents;

    @BindView(R.id.product_image)
    ImageView product_image;
    @BindView(R.id.price1)
    TextView price_val;
    @BindView(R.id.price2)
    TextView price2;
    String cart_counter_real = null;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.code)
    TextView code;

    @BindView(R.id.more_features)
    TextView more_features;

    @BindView(R.id.feature)
    TextView feature;
    @BindView(R.id.all_details)
    TextView all_details;
    @BindView(R.id.warrenty)
    TextView warrenty;
    @BindView(R.id.warrenty1)
    TextView warrenty1;
    @BindView(R.id.subtract)
    TextView subtract;
    @BindView(R.id.count)
    TextView count_txt;
    @BindView(R.id.all_reviews)
    TextView all_reviews;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.review)
    TextView review_txt;

    public static TextView cartcounterTV;
    @BindView(R.id.star_val)
    TextView star_val;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.discount)
    TextView discount_txt;
    @BindView(R.id.lv_nonscroll_list)
    NonScrollListView listv;
    ItemData feed;
    int count = 0;
    private HashMap<String, ArrayList<String>> words;
    String product_id, image;
    int cardId;
    int quant = 1;

    //int catId ;

    SessionManagement session;

    private String MY_PREFS_NAME;
    int MODE_PRIVATE;
    ArrayList<ArrayList<String>> cart_list;

    Boolean visibility = false;
    String animals_list[];
    ArrayList<String> split_list;
    String id, quantity_val;
    String price_txt, price_cut_txt, dicount_txt;
    ArrayList<String> path_list, fname_list;
    int quantity = 0;

    String amount[];

    Boolean visibility_cart = false;
    int cartquantity;

    @BindView(R.id.recyclerview_rating)
    RecyclerView recyclerview_rating;
    @BindView(R.id.card_view_review)
    CardView card_view_review;
    @BindView(R.id.main_rating_card)
    CardView main_rating_card;
    @BindView(R.id.pb_4)
    ProgressBar pb_4;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    List<Integer> percent_list = new ArrayList<Integer>();

    String step;

    String cardType;

    int postion_val = 0;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        ButterKnife.bind(this);

        add_to_cart = (Button) findViewById(R.id.add_cart);
        // cancel = (ImageView)findViewById(R.id.cancel);

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        lin3 = (LinearLayout) findViewById(R.id.lin3);
        money = (TextView) findViewById(R.id.money);
        minus = (TextView) findViewById(R.id.minus);
        countText = (TextView) findViewById(R.id.counting);
        plus = (TextView) findViewById(R.id.plus);
        device = (TextView) findViewById(R.id.forDevice);
        device.setOnClickListener(this);
        lense = (TextView) findViewById(R.id.forLence);
        lense.setOnClickListener(this);
        both = (TextView) findViewById(R.id.forBoth);
        both.setOnClickListener(this);
        mainLin = (LinearLayout) findViewById(R.id.mainLin);
        mainLin1 = (LinearLayout) findViewById(R.id.mainLin1);
        simple = (TextView) findViewById(R.id.simple);
        simple.setOnClickListener(this);
        medium = (TextView) findViewById(R.id.medium);
        medium.setOnClickListener(this);
        high = (TextView) findViewById(R.id.high);
        high.setOnClickListener(this);
        piece1 = (TextView) findViewById(R.id.piece1);
        piece1.setOnClickListener(this);
        piece2 = (TextView) findViewById(R.id.piece6);
        piece2.setOnClickListener(this);
        piece3 = (TextView) findViewById(R.id.piece12);
        piece3.setOnClickListener(this);
        session = new SessionManagement(getApplicationContext());


        // hide(
        // );

        cart_list = new ArrayList<ArrayList<String>>();
        split_list = new ArrayList<>();

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Log.d("categoryIdCatWali", getIntent().getStringExtra("catId"));


        if ("1".equals(getIntent().getStringExtra("catId"))) {

            Log.d("main", "main wala");
            mainLin.setVisibility(View.VISIBLE);

        } else {

            Log.d("duantity", "quantity wala");
            mainLin1.setVisibility(View.VISIBLE);
            //cardId = 50;

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

                    money.setText("\u20B9" + grand_total_adpter);


                } else {
                    Toast.makeText(getApplicationContext(), "You can only select maximum 5 items", Toast.LENGTH_SHORT).show();
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

                money.setText("\u20B9" + grand_total_adpter);
            }
        });


/*
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("notworking","yeah its not");
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    // btnBottomSheet.setText("Close sheet");
                }
            }
        });
*/


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
               /*     case BottomSheetBehavior.STATE_EXPANDED: {
                       // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;*/
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }


        this.setTitle("");


        p_bar.setProgress(0);
        footer.setVisibility(View.GONE);
        scrollableContents.setVisibility(View.GONE);

        os_versions = new ArrayList<ItemData>();
        review_versions = new ArrayList<ItemData>();
        rate_versions = new ArrayList<ItemData>();


        id = getIntent().getStringExtra("id_value");


        //    Log.d("id_vallllll",id);

        get_product_detail(Config.Base_Url + "/API/productDetailApi.php?product_id=" + id);


        get_review(new CallBack() {
            @Override
            public void onSuccess(String data) {

                Log.d("loginnnnninnnter", data);
                try {
                    JSONObject obj = new JSONObject(data);
                    JSONArray review = null;

                    get_vote(new CallBack() {
                        @Override
                        public void onSuccess(String data) {

                            try {
                                JSONObject obj = new JSONObject(data);

                                JSONArray array = obj.getJSONArray("review");

                                for (int i = 0; i < array.length(); i++) {
                                    ItemData feed = new ItemData();
                                    JSONObject objj = array.getJSONObject(i);
                                    feed.setId(objj.getString("id"));
                                    feed.setUp_vote(objj.getString("uvote"));
                                    feed.setDown_vote(objj.getString("dvote"));

                                    rate_versions.add(feed);


                                }


                            } catch (JSONException e) {


                            }
                        }

                        @Override
                        public void onFail(String msg) {

                            // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                            Log.d("jhvfff", "failed");
                            // Do Stuff
                        }
                    });
                    //  Log.d("tjhjjj", String.valueOf(rate_versions.size()));


                    if (obj.has("review")) {
                        review = obj.getJSONArray("review");


                        int average_rate = 0;

                        for (int i = 0; i < review.length(); i++) {
                            ItemData feed = new ItemData();
                            JSONObject objj = review.getJSONObject(i);

                            feed.setId(objj.getString("id"));
                            feed.setMobile(objj.getString("mobile"));
                            feed.setCat_name(objj.getString("name"));
                            feed.setRate(objj.getString("rate"));
                            feed.setReview(objj.getString("review"));

                            feed.setDate(objj.getString("date"));

                            feed.setUp_vote(objj.getString("upvote"));
                            feed.setDown_vote(objj.getString("downvote"));

                            if ("1".equals(feed.getRate()))
                                feed.setShort_des_review("Bad Product");
                            if ("2".equals(feed.getRate()))
                                feed.setShort_des_review("Average");
                            if ("3".equals(feed.getRate()))
                                feed.setShort_des_review("Good Product");
                            if ("4".equals(feed.getRate()))
                                feed.setShort_des_review("Vey Good");
                            if ("5".equals(feed.getRate()))
                                feed.setShort_des_review("Brilliant");

                            average_rate = average_rate + Integer.parseInt(feed.getRate());


                            Log.d("nhiudyi", String.valueOf(average_rate));

                            review_versions.add(feed);

                            //   Log.d("khh", feed.getRate());


                        }

                        average_rate = average_rate / review_versions.size();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), GridLayoutManager.VERTICAL);
                        //   Log.d("gjjkkk", String.valueOf(rate_versions.size()));
                        review_Adapter mAdapter1 = new review_Adapter(getApplicationContext(), review_versions, recyclerview_rating, rate_versions);
                        recyclerview_rating.setHasFixedSize(true);
                        // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                        recyclerview_rating.setLayoutManager(gridLayoutManager);


                        recyclerview_rating.setAdapter(mAdapter1);


                        if (obj.has("summary")) {

                            JSONObject summary = obj.getJSONObject("summary");

                            rating.setText(summary.getString("rate") + " Rating, ");
                            review_txt.setText(summary.getString("rNum") + " Reviews");
                            ProgressBar progressbar5 = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                            progressbar5 = (ProgressBar) findViewById(R.id.pb_5);
                            ProgressBar progressbar4 = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                            progressbar4 = (ProgressBar) findViewById(R.id.pb_4);
                            ProgressBar progressbar3 = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                            progressbar3 = (ProgressBar) findViewById(R.id.pb_3);
                            ProgressBar progressbar2 = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                            progressbar2 = (ProgressBar) findViewById(R.id.pb_2);
                            ProgressBar progressbar1 = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                            progressbar1 = (ProgressBar) findViewById(R.id.pb_1);

                            try {
                                double per = 0.0, per1 = 0.0;
                                per = Double.parseDouble(summary.getString("rate5")) / Double.parseDouble(summary.getString("rate")) * 100;
                                progressbar5.setProgress((int) per);
                                progressbar5.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

                                per1 = (Double.parseDouble(summary.getString("rate4")) / Integer.parseInt(summary.getString("rate"))) * 100;
                                Log.d("mnoti", String.valueOf(per));


                                progressbar4.setProgress((int) per1);
                                progressbar4.setProgressTintList(ColorStateList.valueOf(Color.GREEN));


                                per = (Double.parseDouble(summary.getString("rate3")) / Integer.parseInt(summary.getString("rate"))) * 100;

                                progressbar3.setProgress((int) per);
                                progressbar3.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

                                per = (Double.parseDouble(summary.getString("rate2")) / Integer.parseInt(summary.getString("rate"))) * 100;

                                progressbar2.setProgress((int) per);
                                progressbar2.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));

                                per = (Double.parseDouble(summary.getString("rate1")) / Integer.parseInt(summary.getString("rate"))) * 100;

                                progressbar1.setProgressTintList(ColorStateList.valueOf(Color.RED));

                                progressbar1.setProgress((int) per);


                            } catch (Exception e) {


                            }
                        }


                        try {


                            average_rate = average_rate / os_versions.size();


                        } catch (Exception e) {

                        }

                        star_val.setText("" + average_rate);

                        if (review_versions.size() > 3) {

                            card_view_review.setVisibility(View.VISIBLE);
                        }


                        //  String success_val = obj.getString("success");

                       /* if ("true".equals(success_val)) {

                        }*/


                    } else {

                        main_rating_card.setVisibility(View.GONE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {

                // Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                Log.d("jhvfff", "failed");
                // Do Stuff
            }
        });

        Log.d("ihuf", String.valueOf(percent_list.size()));


        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        if ("Image".equals(fname_list.get(position))) {

                            progress = new ProgressDialog(product_details.this);
                            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progress.setIndeterminate(true);
                            progress.setMessage("Image Loading....");
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            ViewGroup.LayoutParams params = progress.getWindow().getAttributes();
//                            params.y = 100;
                            WindowManager.LayoutParams wmlp = progress.getWindow().getAttributes();
                            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                            wmlp.x = 100;   //x position
                            wmlp.y = 200;   //y position

                            //progress.getWindow().setLayout(30,30);
                            // progress.getWindow().setGravity(Gravity.TOP);
                            //progress.setProgress(0);
                            progress.show();

                            postion_val = position;
                            Picasso.with(getApplicationContext())
                                    .load(path_list.get(position))
                                    .into(product_image, new Callback() {

                                        @Override
                                        public void onSuccess() {
                                            product_image.setVisibility(View.VISIBLE);
                                            progress.dismiss();

                                        }

                                        @Override
                                        public void onError() {
                                            product_image.setVisibility(View.INVISIBLE);
                                            progress.show();
                                        }
                                    });


                        } else {


                            Intent i = new Intent(getApplicationContext(), youtube_video.class);
                            i.putExtra("video_path", path_list.get(position));
                            startActivity(i);

                            Log.d("video_path", path_list.get(position));
                        }


                        // TODO Handle item click
                    }
                })
        );


    }


    @OnClick(R.id.bottom)
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            // btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //btnBottomSheet.setText("Expand sheet");
        }
    }

    @OnClick(R.id.card_view_review)
    public void all_viewss() {


        Intent i = new Intent(getApplicationContext(), All_reviews.class);
        i.putExtra("id_value", getIntent().getStringExtra("id_value"));
        startActivity(i);


    }


    @OnClick(R.id.more_features)
    public void more_featuress() {
        if (visibility) {
            ArrayAdapter adapter = new ArrayAdapter<String>(product_details.this, R.layout.list_row, split_list);
            listv.setAdapter(adapter);

            visibility = false;
            more_features.setText("More..");

        } else {
            ArrayAdapter adapter = new ArrayAdapter<String>(product_details.this, R.layout.list_row, feature_list);
            listv.setAdapter(adapter);

            visibility = true;
            more_features.setText("Less..");
        }


    }


    @OnClick(R.id.all_details)
    public void all_detailss() {


        Intent i = new Intent(this, product_all_details.class);
        i.putExtra("product_image", image);
        i.putExtra("product_price", price_val.getText().toString());
        i.putExtra("product_name", name.getText().toString());

        i.putExtra("id_value", getIntent().getStringExtra("id_value"));
        startActivity(i);


    }


    @OnClick(R.id.product_image)
    public void product_imagee() {
        ArrayList<String> image_list = new ArrayList<>();


        for (int i = 0; i < path_list.size(); i++)

        {

            if ("Image".equals(fname_list.get(i))) {
                image_list.add(path_list.get(i));
            }


        }

        Intent j = new Intent(getApplicationContext(), no_product_details.class);
        j.putExtra("image_path", image_list);
        j.putExtra("position", postion_val);
        startActivity(j);


    }


/*
    @OnClick(R.id.buy_now)
    public void buy_now() {

        if(quantity<1) {


            AlertDialog.Builder builder = new AlertDialog.Builder(product_details.this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("Enter Your Email");

            final EditText input = new EditText(product_details.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input,20,20,20,20);

            // builder.setMessage("This is the example code snippet to disable button if edittext attached to dialog is empty.");
            builder.setPositiveButton("SUBMIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            final ProgressDialog progressDialog = new ProgressDialog(product_details.this,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();


                            // TODO: Implement your own authentication logic here.

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {


                                            //    Toast.makeText(mContext, ""+os_versions.get(position).getId(), Toast.LENGTH_SHORT).show();


                                            // On complete call either onSignupSuccess or onSignupFailed
                                            // depending on success


                                            notify_user(input.getText().toString(), new CallBack() {
                                                @Override
                                                public void onSuccess(String data) {

                                                    //  Log.d("channnn",data);

                                                    JSONObject obj = null;
                                                    try {
                                                        obj = new JSONObject(data);
                                                        String success_val = obj.getString("success");

                                                        if ("true".equals(success_val)) {

                                                            Toast.makeText(getApplicationContext(), "You will be notified when the product comes back in stock..", Toast.LENGTH_SHORT).show();
                                                            AppRater.showRateDialog(product_details.this);



                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Please try again, there is some error", Toast.LENGTH_SHORT).show();


                                                        }

                                                    } catch (JSONException e) {
                                                        Toast.makeText(getApplicationContext(), "Please try again, there is some error", Toast.LENGTH_SHORT).show();

                                                        e.printStackTrace();
                                                    }


                                                }

                                                @Override
                                                public void onFail(String msg) {
                                                    Log.d("jhvfff", "failed");
                                                    // Do Stuff
                                                }
                                            });

                                            // onSignupFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);


                        }
                    });
            builder.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
// Set `EditText` to `dialog`. You can add `EditText` from `xml` too.
               final AlertDialog dialog = builder.create();
            dialog.show();
// Initially disable the button
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    .setEnabled(false);
// OR you can use here setOnShowListener to disable button at first
// time.

// Now set the textchange listener for edittext

            final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Check if edittext is empty
                    if (!input.getText().toString().matches(emailPattern)){
                        // Disable ok button
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        // Something into edit text. Enable the button.
                        ((AlertDialog) dialog).getButton(
                                AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }

                }
            });


        }


        else {


            if (session.isLoggedIn() == true) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.delete_buy_now();

                Boolean result = db.insert_buy_now_product(os_versions.get(0).getPrice_cut(), "1", os_versions.get(0).getDel_charge());

                Log.d("inseghhdk", String.valueOf(result));


                Intent i = new Intent(getApplicationContext(), check_out_buy_now.class);
                startActivity(i);


            } else {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                editor.putString("from_where","buy_now");
                editor.commit();

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.delete_buy_now();

                Boolean result = db.insert_buy_now_product(os_versions.get(0).getPrice_cut(), "1", os_versions.get(0).getDel_charge());


                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();

            }
                                      */
    /*  *//*



        }

    }
*/


    public void get_product_detail(String url) {

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // hidePDialog();


                        path_list = new ArrayList<>();
                        fname_list = new ArrayList<>();

                        //amount = new ArrayList<>();

                        feature_list = new ArrayList<String>();

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.has("products")) {
                                JSONArray banner_array = obj.getJSONArray("products");

                                for (int i = 0; i < banner_array.length(); i++) {
                                    JSONObject objj = banner_array.getJSONObject(i);
                                    ItemData feed = new ItemData();
                                    feed.setProductName(objj.getString("productName"));
                                    feed.setProductID(objj.getString("productID"));

                                    feed.setCategoryID(objj.getString("categoryID"));

                                    Log.d("categoryId", feed.getCategoryID());

                                    Log.d("CatID", objj.getString("categoryID"));

                                    // Config.catId = feed.getCid();

                                    //catId=Integer.parseInt(objj.getString("cid"));

                                    feed.setQuantity(objj.getString("quantity"));

                                    quantity = Integer.parseInt(feed.getQuantity());

                                    feed.setFeature(objj.getString("feature"));

                                    product_id = objj.getString("productID");
                                    image = objj.getString("productImage");
                                    path_list.add(objj.getString("productImage"));
                                    fname_list.add("productImage");

                                    if (session.isLoggedIn() == true) {

                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());


                                        Cursor c = db.get_go_to_cart();


                                        if (c != null)
                                            if (c.moveToFirst()) {
                                                do {


                                                    if (c.getString(c.getColumnIndex("product_id")).equals(feed.getProductID())) {

                                                        add_to_cart.setText("GO TO CART");

                                                        visibility_cart = true;

                                                    }
                                                }
                                                while (c.moveToNext());

                                            }

                                    } else

                                    {

                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());


                                        Cursor c = db.get_go_to_cart_log_out();


                                        if (c != null)
                                            if (c.moveToFirst()) {
                                                do {


                                                    if (c.getString(c.getColumnIndex("product_id")).equals(feed.getProductID())) {

                                                        add_to_cart.setText("GO TO CART");

                                                        visibility_cart = true;

                                                    }
                                                }
                                                while (c.moveToNext());

                                            }


                                    }


                                    feed.setDel_charge(objj.getString("deliveryCharge"));

                                    if (quantity < 1) {

                                        add_to_cart.setText("OUT OF STOCK");
                                        add_to_cart.setClickable(false);
                                        // buy_now.setText("NOTIFY ME");

                                    }


                                    // feed.setFeature(objj.getString("feature"));
                                    //feed.setPackage_in(objj.getString("package_include"));
                                    //feed.setSpecification(objj.getString("specification"));
                                    feed.setWarenty(objj.getString("warranty"));

                                    //  amount.add(objj.getString("internationalWarranty"));

                                    feed.setInternationalWarranty(objj.getString("otherPrices"));

                                    String am = feed.getInternationalWarranty();

                                    amount = am.split(",");

                                    if (amount[amount.length - 1].equals("0")) {
                                        cardType = "paper";
                                    } else {
                                        cardType = "plastic";
                                       /*  simple.setEnabled(false);
                                         simple.setBackgroundResource(R.drawable.background_grey);
                                         simple.setTextColor(Color.parseColor("#e2e2e2"));
                                         medium.setEnabled(false);
                                         medium.setBackgroundResource(R.drawable.background_grey);
                                         medium.setTextColor(Color.parseColor("#e2e2e2"));*/

                                        /* if (step.equals("high")){
                                             simple.setBackgroundResource(R.drawable.background_grey);
                                             simple.setTextColor(Color.parseColor("#e2e2e2"));
                                             medium.setBackgroundResource(R.drawable.background_grey);
                                             medium.setTextColor(Color.parseColor("#e2e2e2"));
                                         }*/
                                    }

                                    Log.d("amount", String.valueOf(amount.length));


                                    /*int discount = Integer.parseInt(objj.getString("discount"));
                                    int price = Integer.parseInt(objj.getString("price"));*/

                                    //discount_txt.setText(discount + "% off");

                                    // int price_cut = (100 - discount) * price / 100;
                                    feed.setPrice(objj.getString("price"));
                                    feed.setMrp(objj.getString("mrp"));


                                    feed.setProductImage(objj.getString("productImage"));


                                    code.setText("Code: " + feed.getProductID());
                                    name.setText(feed.getProductName());
                                    price_val.setText("\u20B9" + feed.getPrice());
                                    price2.setText("\u20B9" + feed.getMrp());

                                    price_txt = feed.getPrice();

                                    money.setText("\u20B9" + price_txt);

                                    price_cut_txt = feed.getMrp();
                                    // dicount_txt=objj.getString("discount");


                                    if (!"no_images".equals(getIntent().getStringExtra("image_path"))) {
//                                     Log.d("dhdhjdj",getIntent().getStringExtra("image_path"));
                                        Picasso.with(getApplicationContext())
                                                .load(getIntent().getStringExtra("image_path")).into(product_image);


                                    } else {

                                        // Log.d("nbvngklj",feed.getCat_image()) ;
                                        Picasso.with(getApplicationContext()).load(feed.getProductImage()).into(product_image);


                                    }


                                    price2.setPaintFlags(price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("name_product", name.getText().toString());
                                    editor.putString("price_value_product", objj.getString("price"));
                                    editor.putString("code", code.getText().toString());
                                    editor.putString("price_value_cut_product", price_val.getText().toString());
                                    editor.putString("product_id", objj.getString("productID"));
                                    editor.putString("cat_id", objj.getString("categoryID"));
                                    editor.putString("mrp", objj.getString("mrp"));
                                    // editor.putString("discount_value", objj.getString("discount"));
                                    editor.putString("image", objj.getString("productImage"));
                                    editor.putString("quantity_value", "1");
                                    editor.putString("total_quantity", objj.getString("quantity"));

                                    editor.putString("delivery_price", objj.getString("deliveryCharge"));
                                    editor.putString("delivery_time", obj.getString("deliveryTime"));


                                    editor.commit();






                              /*  String htmlString = "√ First item<br/>"+
                                        "√ Second item<br/>"+
                                        "√ Third item";*/
                                    // feature.setText(Html.fromHtml(htmlString));


                                    //feature.setText("Feature"+objj.getString("feature"));
                                    //     specification.setText("Specification"+objj.getString("specification"));
                                    //      package_in.setText("Package Include"+objj.getString("package_include"));
                                    warrenty.setText("Domestic Warranty:  " + feed.getWarenty());

                                    //feed.setWarenty(objj.getString("internationalWarranty"));

                                    //warrenty1.setText("International Warranty:  " + feed.getWarenty());

                                    os_versions.add(feed);


                                }


                            }

                            if (obj.has("media")) {
                                JSONArray media_array = obj.getJSONArray("media");


                                for (int i = 0; i < media_array.length(); i++) {
                                    JSONObject objj = media_array.getJSONObject(i);


                                    path_list.add(objj.getString("mediaName"));
                                    fname_list.add(objj.getString("mediaType"));


                                }
                                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                                no_product_adapter mAdapter1 = new no_product_adapter(getApplicationContext(), path_list, fname_list, id);
                                mAdapter.setHasFixedSize(true);
                                // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

                                mAdapter.setLayoutManager(gridLayoutManager);


                                mAdapter.setAdapter(mAdapter1);


                            }


                            if (obj.has("general")) {
                                JSONArray general = obj.getJSONArray("general");

                                for (int i = 0; i < general.length(); i++) {
                                    JSONObject objj = general.getJSONObject(i);
                                    feature_list.add(objj.getString("generalKey") + ":  " + objj.getString("generalValue"));


                                }


                                if (feature_list.size() > 4) {
                                    for (int j = 0; j < 4; j++) {


                                        split_list.add(feature_list.get(j));

                                    }

                                }


                                if (feature_list.size() > 4)

                                {
                                    ArrayAdapter adapter = new ArrayAdapter<String>(product_details.this, R.layout.list_row, split_list);
                                    listv.setAdapter(adapter);
                                } else {

                                    ArrayAdapter adapter = new ArrayAdapter<String>(product_details.this, R.layout.list_row, feature_list);
                                    listv.setAdapter(adapter);
                                    more_features.setVisibility(View.GONE);

                                }

                            } else {

                                feature_list.add(os_versions.get(0).getFeature());

                                ArrayAdapter adapter = new ArrayAdapter<String>(product_details.this, R.layout.list_row, feature_list);
                                listv.setAdapter(adapter);
                                more_features.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // Result handling
                        Log.d("sub_category", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
                hidePDialog();

            }
        });

// Add the request to the queue

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mRequestQueue.add(stringRequest);

        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (p_bar != null && p_bar.isShown())
                    hidePDialog();
            }
        });


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


                        if ("1".equals(getIntent().getStringExtra("catId"))) {

                            Log.d("Marked", "marked");
                            item1.put("quantity", String.valueOf(quant));
                            item1.put("cardId", String.valueOf(cardId));

                        } else {

                            item1.put("quantity", countText.getText().toString());
                            item1.put("cardId", String.valueOf(50));
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

                add_cart(String.valueOf(jo), new CallBack() {

                    @Override
                    public void onSuccess(String data) {

                        Log.d("loginnnnninnnter", data);
                        try {
                            JSONObject obj = new JSONObject(data);
                            String success_val = obj.getString("success");

                            if ("true".equals(true)) {
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                Boolean result = db.insert_for_go_to_cart(product_id);

                                //   Log.d("hjffhui", String.valueOf(result));

                                Toast.makeText(product_details.this, " added in cart", Toast.LENGTH_SHORT).show();
                                add_to_cart.setText("GO TO CART");


                                onResume();

                                AppRater.showRateDialog(product_details.this);


                                // AppRater.app_launched(product_details.this);

                      /*   Intent i=new Intent(getApplicationContext(), com.active_india.Fragment.add_to_cart.class);
                         startActivity(i);*/

                            } else {

                                Log.d("firstLog", "firstwala");
                                Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);
                                i.putExtra("test", "");
                                startActivity(i);
                                Toast.makeText(product_details.this, "Item already exist in cart", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("jhvfff", data);


                        //    Toast.makeText(getActivity(), ""+data, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFail(String msg) {

                        Toast.makeText(product_details.this, "Invalid Login Details", Toast.LENGTH_SHORT).show();
                        Log.d("jhvfff", "failed");
                        // Do Stuff
                    }
                });

            } else {

                Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);

                Log.d("secondLog", "Secondwala");
                i.putExtra("test", "");
                startActivity(i);


            }

        } else {


            if (visibility_cart == false) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                Boolean result = db.insert(name.getText().toString(), price_txt, price_cut_txt, "1", dicount_txt, product_id, image);

                Log.d("count", product_id);
                //((MyApplication)this.getApplication()).setCartquantity(String.valueOf(quantity));

                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Name", quantity);
                editor.apply();

                if (result == true) {


                    Toast.makeText(this, "Item Added to cart", Toast.LENGTH_SHORT).show();
                    db.insert_for_go_to_cart_logout(product_id);

                    add_to_cart.setText("GO TO CART");


                    onResume();
            /*   Intent i = new Intent(getApplicationContext(), add_to_cart.class);
               startActivity(i);*/
                } else {
                    Log.d("thirdLog", "thirdwala");
                    Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);
                    i.putExtra("test", "");
                    startActivity(i);
                    Toast.makeText(this, "Item Already available in cart", Toast.LENGTH_SHORT).show();

               /*Intent i = new Intent(getApplicationContext(), add_to_cart.class);
               startActivity(i);*/
                }
            } else {

                Log.d("forthLog", "forthwala");

                Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);
                i.putExtra("test", "");
                startActivity(i);


            }
        }


    }

    private void hidePDialog() {
        if (p_bar != null) {
            p_bar.setVisibility(View.GONE);
            footer.setVisibility(View.VISIBLE);
            scrollableContents.setVisibility(View.VISIBLE);

        }
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

                money.setText("\u20B9" + String.valueOf(b));


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

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[3]);

                        cardId = 3;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[6]);

                        cardId = 6;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[9]);

                        cardId = 9;

                        money.setText("\u20B9" + String.valueOf(b));

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

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[4]);

                        cardId = 4;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[7]);

                        cardId = 7;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[10]);

                        cardId = 10;

                        money.setText("\u20B9" + String.valueOf(b));

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

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "simple": {

                        int b = Integer.parseInt(amount[5]);

                        cardId = 5;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "medium": {

                        int b = Integer.parseInt(amount[8]);

                        cardId = 8;

                        money.setText("\u20B9" + String.valueOf(b));

                        break;
                    }
                    case "high": {

                        int b = Integer.parseInt(amount[11]);

                        cardId = 11;

                        money.setText("\u20B9" + String.valueOf(b));

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

    static class VersionHelper {
        static void refreshActionBarMenu(Activity activity) {
            activity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        VersionHelper.refreshActionBarMenu(this);
        // put your code here...

    }


    public void hide() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            // btnBottomSheet.setText("Close sheet");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
        cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
        ImageView cart_icon = (ImageView) menu_item_cart.findViewById(R.id.carticon);


        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);
                i.putExtra("test", "");
                startActivity(i);

            }
        });
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
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            String cart_value = String.valueOf(db.count_rows());


            if (!"0".equals(cart_value)) {
                cartcounterTV.setVisibility(View.VISIBLE);
                cartcounterTV.setText(cart_value);
            } else
                cartcounterTV.setVisibility(View.GONE);


        }

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

        } else if (item.getItemId() == R.id.action_search) {
            Intent i = new Intent(this, search_activity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.cart) {
            Intent i = new Intent(getApplicationContext(), com.kk_cards.Fragment.add_to_cart.class);
            i.putExtra("test", "");
            startActivity(i);


        } else if (item.getItemId() == R.id.action_search) {
            Intent i = new Intent(this, search_activity.class);
            startActivity(i);
            return true;
        }
      /*  else if(id == R.id.action_search)
        {

            Intent i=new Intent(MainActivity.this,search_activity.class);
            startActivity(i);
          //  loadToolBarSearch();
        }*/

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
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


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    protected void get_review(final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/fetchReviewApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssss5555ssssss", response);


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
                params.put("productID", getIntent().getStringExtra("id_value"));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    protected void notify_user(final String email, final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/notifyMeApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);

                        Log.d("sssss5555ssssss", response);


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
                params.put("productID", getIntent().getStringExtra("id_value"));
                params.put("email", email);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    protected void get_vote(final CallBack mResultCallback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Base_Url + "/API/fraLoginApi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResultCallback.onSuccess(response);


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
                params.put("productID", getIntent().getStringExtra("id_value"));
                params.put("mobile", session.getUserDetails().get(SessionManagement.KEY_MOBILE));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
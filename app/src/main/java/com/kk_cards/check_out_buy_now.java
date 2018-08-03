package com.kk_cards;

/**
 * Created by user on 3/19/2018.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk_cards.Adapter.buy_now_adapter;
import com.kk_cards.Database.DatabaseHandler;
import com.kk_cards.Modal.ItemData;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;





/**
 * Created by pooja on 7/19/2017.
 */

public class check_out_buy_now extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;
    ArrayList<ItemData> os_versions;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    buy_now_adapter mAdapter1;

    private GridLayoutManager gridLayoutManager;

    ArrayList<String> id;
    DatabaseHandler db;

    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    @BindView(R.id.checkout)
    TextView checkout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;



    int grand_total = 0;

    @BindView(R.id.no_item)
    TextView no_item;

    @BindView(R.id.del_price)
    TextView del_price;
    SessionManagement session;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.addressss)
    TextView addressss;
    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.chage_add)
    Button chage_add;

    @BindView(R.id.pric_r_layout)
    RelativeLayout pric_r_layout;
    private String MY_PREFS_NAME;
    int MODE_PRIVATE;

    @BindView(R.id.linearrr)
    LinearLayout linear;

    String cart_counter_real=null;

    public static TextView total_amt;
    public static TextView   tot_price_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }



        this.setTitle("Checkout");


        os_versions=new ArrayList<>();
        session = new SessionManagement(getApplicationContext());
        total_amt=(TextView) findViewById(R.id.total);
        tot_price_items=(TextView) findViewById(R.id.tot_price_items);

        //   MainActivity.footer.setVisibility(View.GONE);


            // Toast.makeText(this, "fkfjigohjih", Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            ItemData feed = new ItemData();

            feed.setProductID(prefs.getString("product_id", null));

            feed.setCartID(prefs.getString("product_id", null));

            feed.setQuantity(prefs.getString("quantity_value", null));
        feed.setTot_quantity(prefs.getString("total_quantity", null));

            feed.setProductName(prefs.getString("name_product", null));
            feed.setProductImage(prefs.getString("image", null));

          //  int discount = Integer.parseInt(prefs.getString("discount_value", null));
            int price = Integer.parseInt(prefs.getString("price_value_product", null));


           // int price_cut = (100 - discount) * price / 100;
            feed.setPrice(prefs.getString("price_value_product", null));
           // feed.setPrice_cut(String.valueOf(price_cut));
           // feed.setDiscount(String.valueOf(discount));

            feed.setDeliveryCharge(prefs.getString("delivery_price", null));

            os_versions.add(feed);

            int grand_total=0,grand_tottttt=0;
            grand_total = grand_total + Integer.parseInt(feed.getQuantity()) * Integer.parseInt(feed.getPrice());
        tot_price_items.setText("\u20B9" + grand_total);

        grand_tottttt=grand_total;




                grand_total = grand_total + Integer.parseInt(prefs.getString("delivery_price", null));


            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("total_price", grand_total);
        editor.putInt("total_price_with_del", grand_tottttt);
            editor.putString("check_value", "");
        editor.putString("buy_now_or_cart","buy");
             editor.putString("product_no_items", no_item.getText().toString());



        editor.commit();




        pric_r_layout.setVisibility(View.VISIBLE);



            total_amt.setText("\u20B9" + grand_total);
             no_item.setText("Price 1 item)");
            if (!"0".equals(prefs.getString("delivery_price", null)))
                del_price.setText("\u20B9" +prefs.getString("delivery_price", null));
            else
                del_price.setText(prefs.getString("delivery_price", null));


            gridLayoutManager = new GridLayoutManager(check_out_buy_now.this, GridLayoutManager.VERTICAL);
            mAdapter1 = new buy_now_adapter(check_out_buy_now.this, os_versions);
            mAdapter.setHasFixedSize(true);
            // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

            mAdapter.setLayoutManager(gridLayoutManager);


            mAdapter.setAdapter(mAdapter1);

            mAdapter1.notifyDataSetChanged();

            //   total_amt.setText("Total: " + "Rs." + grand_total);
                              /*  JSONArray sub_category=objj.getJSONArray("subcategory");
                                sub_category_size=sub_category.length();
*/
            //




        linear.setVisibility(View.VISIBLE);


        name.setText(prefs.getString("name", null));
        phone.setText(prefs.getString("phone", null));
        addressss.setText(prefs.getString("address", null));


        if(prefs.getString("name", null)==null)
        {
            name.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
            addressss.setVisibility(View.GONE);

        }


        if(name.getText().toString()==null||name.getText().toString()=="")
        {

            chage_add.setText("Add Address");
        }
        else
        {
            chage_add.setText("Change or Add Address");

        }



    }

    @OnClick(R.id.checkout)
    public void checksum()

    {

        if(name.getText().toString()==null||name.getText().toString()=="")
        {
            Toast.makeText(this, "Please choose a address", Toast.LENGTH_SHORT).show();
        }

        else {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(check_out_buy_now.this);
            alertDialogBuilder.setMessage("Is your Shipping Address and Billing Address Same?");
            alertDialogBuilder.setPositiveButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent i = new Intent(getApplicationContext(), select_address.class);
                            i.putExtra("address","billing_address");
                          //  i.putExtra("qunatity",getIntent().getStringExtra("qunatity"));
                            i.putExtra("check",getIntent().getStringExtra("check"));

                            startActivity(i);
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("YES",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {


                            final ProgressDialog progressDialog = new ProgressDialog(check_out_buy_now.this,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Please Wait.....");
                            progressDialog.show();


                            // TODO: Implement your own authentication logic here.

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Intent i = new Intent(getApplicationContext(), payment_page.class);

                                          //  i.putExtra("no_of_items", "Price(1 item)");
                                            i.putExtra("amt",total_amt.getText().toString());
                                            i.putExtra("check_vall", "buy");
                                         //   i.putExtra("delivery", del_price.getText().toString());
                                            startActivity(i);
                                            finish();
                                            // onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);





                        }
                    });


            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();





        }

    }


    @OnClick(R.id.chage_add)
    public void chage_adddd()

    {

        Intent i = new Intent(getApplicationContext(), select_address.class);
        i.putExtra("address","shipping_address");
        i.putExtra("check_valuee","buy");
        startActivity(i);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        View menu_item_cart = menu.findItem(R.id.cart).getActionView();
        final TextView cartcounterTV = (TextView) menu_item_cart.findViewById(R.id.cartcounter);
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

    private void hidePDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            mAdapter.setVisibility(View.VISIBLE);
            pric_r_layout.setVisibility(View.VISIBLE);

        }
    }

}
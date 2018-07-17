package com.kk_cards.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kk_cards.Adapter.no_details_adapter_class;
import com.kk_cards.Adapter.no_product_sliding_adapter;
import com.kk_cards.Listener.RecyclerItemClickListener;
import com.kk_cards.R;
import com.kk_cards.search_activity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by user on 1/23/2018.
 */

public class no_product_details extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView mAdapter;

    private static ViewPager mPager;
    private static final Integer[] XMEN = {R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy, R.drawable.dummy};
    private ArrayList<String> XMENArray = new ArrayList<String>();
    private int currentPage;
    @BindView(R.id.linear)
    LinearLayout linear;
    Bitmap thumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_product_detail);
        ButterKnife.bind(this);
        mPager = (ViewPager) findViewById(R.id.pager);


        if(getSupportActionBar()!=null)
        {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }

        this.setTitle("");



        init();


        mAdapter.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                          mPager.setCurrentItem(position);



                        // TODO Handle item click
                    }
                })
        );


    }

    private void init() {
        for(int i=0;i<getIntent().getStringArrayListExtra("image_path").size();i++) {
             XMENArray.add(getIntent().getStringArrayListExtra("image_path").get(i));


        }

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);



        no_details_adapter_class mAdapter1 = new no_details_adapter_class(getApplicationContext(), XMENArray);
        mAdapter.setHasFixedSize(true);
        // mAdapter.setLayoutManager(new StaggeredGridLayoutManager(2,1)); for tils

        mAdapter.setLayoutManager(gridLayoutManager);


        mAdapter.setAdapter(mAdapter1);
       // Log.d("jhdfieuroig",XMENArray.get(0));




        mPager.setAdapter(new no_product_sliding_adapter(getApplicationContext(),XMENArray));
        final float density = getResources().getDisplayMetrics().density;
        mPager.setCurrentItem(getIntent().getIntExtra("position",0));


//Set circle indicator radius
      

        

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        /*Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
*/
        // Pager listener over indicator
      

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

        else if (id == R.id.cart) {
            Intent i=new Intent(getApplicationContext(),add_to_cart.class);
            startActivity(i);


        }

        else if(item.getItemId()==R.id.action_search){
            Intent i = new Intent(this,search_activity.class);
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
}
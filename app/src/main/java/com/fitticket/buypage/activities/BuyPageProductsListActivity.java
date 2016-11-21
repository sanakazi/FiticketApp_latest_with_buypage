package com.fitticket.buypage.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fitticket.R;
import com.fitticket.buypage.adapters.BuyPageProductsListAdapter;
import com.fitticket.buypage.fragments.BuyPageProductsListFragment;


public class BuyPageProductsListActivity extends CartMenuActivity implements BuyPageProductsListAdapter.ProductSelectedListener{

    public static final String CAT_ID = "CAT_ID";
    public static final String CAT_NAME = "CAT_NAME";
    private static final String ACT_FRAGMENT = "ACT_FRAGMENT";

    public static String catId,catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page_products);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catId = getIntent().getStringExtra(CAT_ID);
        catName = getIntent().getStringExtra(CAT_NAME);

        BuyPageProductsListFragment activitiesFragment = BuyPageProductsListFragment.newInstance("", catId,catName);
        replaceFragment(activitiesFragment, ACT_FRAGMENT, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void replaceFragment(Fragment fragment, String tag, boolean addtoBackStack) {
        if (addtoBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:onBackPressed();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onProductSelected(int activityId) {
        Intent i = new Intent(this, BuyPageProductDetailsActivity.class);
        i.putExtra(BuyPageProductDetailsActivity.ACTIVITY_ID, activityId);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
      //  overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
    }
}

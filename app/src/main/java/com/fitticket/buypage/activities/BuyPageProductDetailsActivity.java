package com.fitticket.buypage.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.fitticket.R;
import com.fitticket.buypage.fragments.BuyPageProductDetailsFragment;
import com.fitticket.buypage.fragments.BuyPageProductsListFragment;

public class BuyPageProductDetailsActivity extends CartMenuActivity {
    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String TAG = BuyPageProductDetailsActivity.class.getSimpleName();
    BuyPageProductDetailsFragment detailFragment;
    int catId;
    public static final String CAT_ID = "CAT_ID";
    private static final String ACT_FRAGMENT = "ACT_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_buy_page_product_details);
        catId = getIntent().getIntExtra(ACTIVITY_ID,0);
        Log.w(TAG,"CATEGORY ID IS" + catId);
        detailFragment = BuyPageProductDetailsFragment.newInstance(getIntent().getIntExtra(ACTIVITY_ID,0));
        getSupportFragmentManager().beginTransaction().add(R.id.container, detailFragment).commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:onBackPressed();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }

}

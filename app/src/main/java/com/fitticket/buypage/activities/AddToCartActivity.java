package com.fitticket.buypage.activities;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fitticket.R;
import com.fitticket.buypage.fragments.AddToCartFragment;
import com.fitticket.buypage.fragments.BuyNowFragment;
import com.fitticket.viewmodel.activities.PayTmWebViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddToCartActivity extends AppCompatActivity {

    public static final String FROM_CART_OR_BUYNOW = "FROM_CART_OR_BUYNOW";
    private static final String ACT_FRAGMENT = "ACT_FRAGMENT";
    String fragmentToOpen;

    public static Location location;

    @Bind(R.id.pay)
    TextView pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        fragmentToOpen = intent.getStringExtra(FROM_CART_OR_BUYNOW);

        if(fragmentToOpen.equals("1")) {
            AddToCartFragment activitiesFragment = new AddToCartFragment();
            replaceFragment(activitiesFragment, ACT_FRAGMENT, false);
        }
        else   if(fragmentToOpen.equals("2")) {
            BuyNowFragment buynowragment = new BuyNowFragment();
            replaceFragment(buynowragment, ACT_FRAGMENT, false);
        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payTmIntent = new Intent(AddToCartActivity.this, PayTmWebViewActivity.class);
                payTmIntent.putExtra("URL TO LOAD", "paytm url");
                startActivity(payTmIntent);
            }
        });
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
}

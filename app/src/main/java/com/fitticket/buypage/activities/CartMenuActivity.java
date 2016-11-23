package com.fitticket.buypage.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitticket.R;

import butterknife.Bind;

public class CartMenuActivity extends AppCompatActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.icon_cart)
    ImageView icon_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //setting custom actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_cart_list);

      /*  icon_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartMenuActivity.this, AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
            }
        });*/

    }




}





 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addtocart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_to_cart:
                Intent intent = new Intent(CartMenuActivity.this, AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

package com.fitticket.buypage.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.buypage.adapters.AddToCartAdapter;
import com.fitticket.buypage.fragments.AddToCartFragment;
import com.fitticket.buypage.fragments.BuyNowFragment;
import com.fitticket.buypage.others.BuyPageSingleton;
import com.fitticket.buypage.pojos.AddtoCartJsonResponse;
import com.fitticket.buypage.pojos.RemoveFromCartJsonResponse;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.activities.PayTmWebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddToCartActivity extends AppCompatActivity implements AddToCartAdapter.RemoveFromCartListener,AddToCartAdapter.CartCountListener {

    public static final String FROM_CART_OR_BUYNOW = "FROM_CART_OR_BUYNOW";
    public static final String FROM_WHICH_ACTIVITY = "FROM_WHICH_ACTIVITY";
    public static final String URL_REMOVE_FROM_URL = "http://192.168.168.234:86/WebServices/v1/AppService.svc/BuyPageAddtoCart";
    private static final String ACT_FRAGMENT = "ACT_FRAGMENT";
    private static final String TAG= AddToCartActivity.class.getSimpleName();
    String fragmentToOpen,from_which_activity;
    String CartId,Quantity,Price,ProductImage,ProductName,ShortDescription,UnitPrice;

    private TextView mycart_title;
    String title_text = "My Cart ("+String.valueOf(BuyPageSingleton.CART_COUNT)+ ")" ;


    public static Location location;

    @Bind(R.id.pay)
    TextView pay;
    @Bind(R.id.txt_total)
    TextView txt_total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting custom actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_addtocart);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        fragmentToOpen = intent.getStringExtra(FROM_CART_OR_BUYNOW);
        from_which_activity=intent.getStringExtra(FROM_WHICH_ACTIVITY);
        CartId = intent.getStringExtra("CartId");
        Quantity = intent.getStringExtra("Quantity");
        Price = intent.getStringExtra("Price");
        ProductImage = intent.getStringExtra("ProductImage");
        ProductName = intent.getStringExtra("ProductName");
        ShortDescription = intent.getStringExtra("ShortDescription");
        UnitPrice = intent.getStringExtra("UnitPrice");

        if(fragmentToOpen.equals("1")) {
            AddToCartFragment activitiesFragment = new AddToCartFragment();
            replaceFragment(activitiesFragment, ACT_FRAGMENT, false);
        }
        else   if(fragmentToOpen.equals("2")) {
            BuyNowFragment buynowfragment = new BuyNowFragment();
            Bundle bundle = new Bundle();
            bundle.putString("CartId",CartId);
            bundle.putString("Quantity",Quantity);
            bundle.putString("Price",Price);
            bundle.putString("ProductImage",ProductImage);
            bundle.putString("ProductName",ProductName);
            bundle.putString("ShortDescription",ShortDescription);
            bundle.putString("UnitPrice",UnitPrice);
            buynowfragment.setArguments(bundle);
            replaceFragment(buynowfragment, ACT_FRAGMENT, false);
        }

        events();


    }


    private void events(){


        mycart_title = (TextView)findViewById(R.id.mycart_title);
        setBarTitle(String.valueOf(BuyPageSingleton.CART_COUNT),title_text,String.valueOf(BuyPageSingleton.CART_COUNT));

        ImageView btn_back  =(ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent payTmIntent = new Intent(AddToCartActivity.this, PayTmWebViewActivity.class);
                payTmIntent.putExtra("URL TO LOAD", "paytm url");
                startActivity(payTmIntent);*/
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


    //region removeFromCart service
    @Override
    public void onRemoveFromCartSelected(int cartId, int userID) {
        RemoveFromCartJsonResponse json = new RemoveFromCartJsonResponse();
        json.setCustomerId(String.valueOf(userID));
        json.setOrderID(String.valueOf(cartId));
        json.setStatus("0");

        WebServices.triggerVolleyPostRequest(AddToCartActivity.this,json,URL_REMOVE_FROM_URL,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "Cart response " + response.toString());

                try{

                    int StatusCode = response.getInt("StatusCode");
                    if(StatusCode!=0)
                    {
                        BuyPageSingleton.CART_COUNT= response.getInt("CartCount");
                        Log.w(TAG, "Cart response " +  BuyPageSingleton.CART_COUNT+" ");
                        AddToCartFragment activitiesFragment = new AddToCartFragment();
                        replaceFragment(activitiesFragment, ACT_FRAGMENT, false);
                        Toast.makeText(AddToCartActivity.this,"Item removed",Toast.LENGTH_SHORT).show();

                    }
                }
                catch (JSONException e){}


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddToCartActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }, RemoveFromCartJsonResponse.class);
    }

    //endregion


    @Override
    public void onCartAmountChanged(double value) {
        txt_total.setText(String.valueOf(value));
    }


    public void setBarTitle(String cart_count,String title, String total_amount)
    {
        BuyPageSingleton.CART_COUNT=Integer.parseInt(cart_count);
        mycart_title.setText(String.valueOf(title) );
        txt_total.setText(total_amount);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.w(TAG,"cartcount is " +  BuyPageSingleton.CART_COUNT);
        if(from_which_activity.equals("1")) {
            BuyPageProductsListActivity counter = new BuyPageProductsListActivity();
            counter.setCartCount(String.valueOf(BuyPageSingleton.CART_COUNT));
        }

        else if(from_which_activity.equals("2")) {
            BuyPageProductDetailsActivity counter1 = new BuyPageProductDetailsActivity();
            counter1.setCartCount(String.valueOf(BuyPageSingleton.CART_COUNT));
        }
    }
}

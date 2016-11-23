package com.fitticket.buypage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.buypage.fragments.BuyPageProductDetailsFragment;
import com.fitticket.buypage.fragments.BuyPageProductsListFragment;
import com.fitticket.buypage.others.BuyPageSingleton;
import com.fitticket.buypage.pojos.AddtoCartJsonResponse;
import com.fitticket.buypage.pojos.GetCartCountJsonResponse;
import com.fitticket.model.constants.Constants;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyPageProductDetailsActivity extends AppCompatActivity {
    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String TAG = BuyPageProductDetailsActivity.class.getSimpleName();
    BuyPageProductDetailsFragment detailFragment;
    int catId;
    //int cartcount = 0;

    static TextView txt_cartcount;
    ImageView btn_back,icon_cart;
    private static  final String URL_TO_GET_COUNT = "http://192.168.168.234:86/WebServices/v1/AppService.svc/GetCartCount?CustomerId=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //setting custom actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_cart_details);
        events();

        setContentView(R.layout.activity_buy_page_product_details);
        catId = getIntent().getIntExtra(ACTIVITY_ID,0);
        Log.w(TAG,"CATEGORY ID IS" + catId);
        detailFragment = BuyPageProductDetailsFragment.newInstance(getIntent().getIntExtra(ACTIVITY_ID,0));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();

    }


    private void events(){
        btn_back = (ImageView)findViewById(R.id.btn_back);
        icon_cart = (ImageView)findViewById(R.id.icon_cart);
        txt_cartcount = (TextView)findViewById(R.id.txt_cartcount);
        icon_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyPageProductDetailsActivity.this, AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                intent.putExtra(AddToCartActivity.FROM_WHICH_ACTIVITY,"2");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                intent.putExtra("CartId"," ");
                intent.putExtra("Price"," ");
                intent.putExtra("ProductImage"," ");
                intent.putExtra("ProductName"," ");
                intent.putExtra("Quantity"," ");
                intent.putExtra("ShortDescription"," ");
                intent.putExtra("UnitPrice"," ");

                startActivity(intent);
                overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getCartCount();

        if(BuyPageSingleton.CART_COUNT==0)
        {txt_cartcount.setVisibility(View.GONE);}
        else {
            setCartCount(String.valueOf(BuyPageSingleton.CART_COUNT));
        }
    }


    public void setCartCount(String count)
    {
        BuyPageSingleton.CART_COUNT=Integer.parseInt(count);
        if(count.equals("0"))
        {txt_cartcount.setVisibility(View.GONE);}
        else {
            txt_cartcount.setVisibility(View.VISIBLE);
            txt_cartcount.setText(String.valueOf( BuyPageSingleton.CART_COUNT));
        }
    }


    public void getCartCount()
    {
        PreferencesManager sPref;
        sPref = PreferencesManager.getInstance(this);
        int userId=sPref.getUserId();

        WebServices.triggerVolleyGetRequest(this,URL_TO_GET_COUNT+ userId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get categories response: " + response);
                        Gson gson = new Gson();
                        GetCartCountJsonResponse jsonResponse = gson.fromJson(response, GetCartCountJsonResponse.class);
                        if (jsonResponse.getGetCartCountResult()!=null) {
                            BuyPageSingleton.CART_COUNT= jsonResponse.getGetCartCountResult().getCount();
                            setCartCount(String.valueOf(jsonResponse.getGetCartCountResult().getCount()));
                        } else {
                            Log.e(TAG, "Some error with Json Response");
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utilities.handleVolleyError(BuyPageProductDetailsActivity.this, error);

                    }

   });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.w(TAG,"cartcount is " +  BuyPageSingleton.CART_COUNT);
        BuyPageProductsListActivity counter = new BuyPageProductsListActivity();
        counter.setCartCount(String.valueOf( BuyPageSingleton.CART_COUNT));
    }
}

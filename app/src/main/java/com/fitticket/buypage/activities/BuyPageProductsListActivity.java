package com.fitticket.buypage.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.buypage.adapters.BuyPageProductsListAdapter;
import com.fitticket.buypage.fragments.BuyPageProductsListFragment;
import com.fitticket.buypage.fragments.CartLogicFragment;
import com.fitticket.buypage.others.BuyPageSingleton;
import com.fitticket.buypage.pojos.BuyPageCategoryJsonResponse;
import com.fitticket.buypage.pojos.GetCartCountJsonResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.constants.Constants;
import com.fitticket.model.singleton.MySingleton;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;


public class BuyPageProductsListActivity extends AppCompatActivity implements BuyPageProductsListAdapter.ProductSelectedListener,BuyPageProductsListAdapter.AddtoCartListener {



    ImageView btn_back,icon_cart;
    static TextView txt_cartcount,title;
    //int cartcount = 0;

    public static final String CAT_ID = "CAT_ID";
    public static final String CAT_NAME = "CAT_NAME";
    private static final String ACT_FRAGMENT = "ACT_FRAGMENT";
    private static final String TAG = BuyPageProductsListActivity.class.getSimpleName();

    public static String catId,catName;

    private static  final String URL_TO_GET_COUNT = "http://192.168.168.234:86/WebServices/v1/AppService.svc/GetCartCount?CustomerId=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page_products);

      //  ButterKnife.bind(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting custom actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_cart_list);



        catId = getIntent().getStringExtra(CAT_ID);
        catName = getIntent().getStringExtra(CAT_NAME);

        BuyPageProductsListFragment activitiesFragment = BuyPageProductsListFragment.newInstance("", catId,catName);
        replaceFragment(activitiesFragment, ACT_FRAGMENT, false);

        events();
    }



    private void events(){
        btn_back = (ImageView)findViewById(R.id.btn_back);
        icon_cart = (ImageView)findViewById(R.id.icon_cart);
        txt_cartcount = (TextView)findViewById(R.id.txt_cartcount);
        title = (TextView)findViewById(R.id.title) ;
       /* Typeface type = Typeface.createFromAsset(getAssets(), "Gotham-Black.ttf");
        title.setTypeface(type);*/
        getCartCount();
        if(BuyPageSingleton.CART_COUNT==0)
        {txt_cartcount.setVisibility(View.GONE);}
        else {
            setCartCount(String.valueOf(BuyPageSingleton.CART_COUNT));
        }
        icon_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyPageProductsListActivity.this, AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                intent.putExtra(AddToCartActivity.FROM_WHICH_ACTIVITY,"1");
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
    }

    private void replaceFragment(Fragment fragment, String tag, boolean addtoBackStack) {
        if (addtoBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
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


    @Override
    public void onAddToCartSelected(String productid, String priceid , String customer_id , String price,String qty , String status , String orderid, String from_which_context ) {
        Log.w(TAG, "show popup");

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog1");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.

        CartLogicFragment newFragment = new CartLogicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productid", productid);
        bundle.putString("priceid", priceid);
        bundle.putString("customer_id", customer_id);
        bundle.putString("price", price);
        bundle.putString("qty", qty);
        bundle.putString("status", status);
        bundle.putString("from_which_context",from_which_context);
        bundle.putString("orderid", orderid);
        newFragment.setArguments(bundle);
        newFragment.show(ft, "dialog1");
    }


    public static void setCartCount(String count)
    {
        BuyPageSingleton.CART_COUNT = (Integer.parseInt(count));
        if(count.equals("0"))
        {txt_cartcount.setVisibility(View.GONE);}
        else {
            txt_cartcount.setVisibility(View.VISIBLE);
            txt_cartcount.setText(String.valueOf(BuyPageSingleton.CART_COUNT));
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
                        Utilities.handleVolleyError(BuyPageProductsListActivity.this, error);

                    }
                });

    }



}

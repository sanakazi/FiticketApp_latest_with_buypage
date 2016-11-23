package com.fitticket.buypage.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.buypage.activities.AddToCartActivity;
import com.fitticket.buypage.adapters.AddToCartAdapter;
import com.fitticket.buypage.others.BuyPageSingleton;
import com.fitticket.buypage.pojos.AddToCartListJsonResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.pojos.CategoryJsonResponse;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SanaKazi on 11/16/2016.
 */
public class AddToCartFragment extends Fragment {

    private static final String GET_URL = "http://192.168.168.234:86/WebServices/v1/AppService.svc/ListOfCartProduct?CustomerId=";

    private  static final String TAG = AddToCartFragment.class.getSimpleName();
    PreferencesManager sPref;
    int userid;
    double total=0.0;
    double price=0;
    int qty=0;


    // region temporary values
    private AppCompatActivity parentActivity;
    AddToCartAdapter mBuyPageProductsAdater;
    private ArrayList<AddToCartListJsonResponse.CartProducts> mCategoryList;

    //endregion

    // region ButterKnife bindviews

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.activities_listview)
    RecyclerView mActivityListView;
    @Bind(R.id.noActLayout)
    RelativeLayout noActLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    //endregion



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AppCompatActivity) getActivity();
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(parentActivity);
        mActivityListView.setLayoutManager(mLayoutManager);
        sPref = PreferencesManager.getInstance(getActivity());
        userid = sPref.getUserId();
        mCategoryList = new ArrayList<>();
        triggerCategoryVolleyRequest();
    }

    private void triggerCategoryVolleyRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Log.w(TAG, "Get categories Requst: " + GET_URL + userid);
        WebServices.triggerVolleyGetRequest(parentActivity, GET_URL + userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "Get categories response: " + response);
                        Gson gson = new Gson();
                        AddToCartListJsonResponse jsonResponse = gson.fromJson(response, AddToCartListJsonResponse.class);
                        mCategoryList=jsonResponse.getListOfCartProductResult().getCartProducts();
                        Log.w(TAG,jsonResponse.getListOfCartProductResult().getStatusCode()+"CODE and count is " +jsonResponse.getListOfCartProductResult().getCount());
                        if (jsonResponse.getListOfCartProductResult().getStatusCode()==1) {

                            for(int i=0;i<jsonResponse.getListOfCartProductResult().getCount();i++)
                            {
                                total+=Double.parseDouble(mCategoryList.get(i).getUnitPrice())*mCategoryList.get(i).getQuantity();
                            }

                                setBuyPageAdater(jsonResponse.getListOfCartProductResult().getCount());

                            }
                        else {
                            Log.w(TAG, "Cart is empty");
                          //  setBuyPageAdater();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utilities.handleVolleyError(parentActivity, error);
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

    private void setBuyPageAdater(int cart_count_title) {

       ((AddToCartActivity) getActivity()).setBarTitle(String.valueOf(cart_count_title) ,"My Cart ("+ BuyPageSingleton.CART_COUNT+")",String.valueOf(total));
        mBuyPageProductsAdater = new AddToCartAdapter(parentActivity, mCategoryList);
        mActivityListView.setAdapter(mBuyPageProductsAdater);
    }
}

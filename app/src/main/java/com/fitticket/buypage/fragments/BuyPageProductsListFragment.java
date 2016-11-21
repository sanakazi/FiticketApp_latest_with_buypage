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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.buypage.activities.BuyPageProductsListActivity;
import com.fitticket.buypage.adapters.BuyPageProductsListAdapter;
import com.fitticket.buypage.pojos.GetProductByCategoryResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.utils.WebServices;

import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SanaKazi on 11/14/2016.
 */
public class BuyPageProductsListFragment extends Fragment {

    private static final String GET_URL = "GET_URL";
    private static final String FROM_FLAG = "FROM_FLAG";
    private  static final String TAG = BuyPageProductsListFragment.class.getSimpleName();
    String catId,catName,mUrl;


// region temporary values
   private AppCompatActivity parentActivity;
    BuyPageProductsListAdapter mBuyPageProductsAdater;
    private ArrayList<GetProductByCategoryResponse.Products> mCategoryList;

 //endregion

  // region ButterKnife bindviews

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.activities_listview)
    RecyclerView mActivityListView;
    @Bind(R.id.noActLayout)
    RelativeLayout noActLayout;
    @Bind(R.id.txt_catName)
    TextView txt_catName;
    private RecyclerView.LayoutManager mLayoutManager;
    //endregion




    public static BuyPageProductsListFragment newInstance(String url, String cateId, String catName) {
        BuyPageProductsListFragment fragment = new BuyPageProductsListFragment();
        Bundle args = new Bundle();
        args.putString(GET_URL, url);
        args.putString(BuyPageProductsListActivity.CAT_ID, cateId);
        args.putString(BuyPageProductsListActivity.CAT_NAME, catName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(GET_URL);
        catId = getArguments().getString(BuyPageProductsListActivity.CAT_ID);
        catName = getArguments().getString(BuyPageProductsListActivity.CAT_NAME);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buypageproducts, container, false);
    //    View view = inflater.inflate(R.layout.buypageproducts_listitem, container, false); - for adapter
        ButterKnife.bind(this, view);
        txt_catName.setText(catName);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AppCompatActivity) getActivity();
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(parentActivity);
        mActivityListView.setLayoutManager(mLayoutManager);
        mCategoryList = new ArrayList<>();
        triggerProductsByCategoryVolleyRequest(catId);
    }


    private void triggerProductsByCategoryVolleyRequest(String catId) {
        progressBar.setVisibility(View.VISIBLE);
        Log.w(TAG, "Get categories Requst: " + Apis.GET_BUY_PAGE_PRODUCT_BY_CATEGORY + catId);
        WebServices.triggerVolleyGetRequest(parentActivity, Apis.GET_BUY_PAGE_PRODUCT_BY_CATEGORY + catId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "Get categories response: " + response);


                        Gson gson = new Gson();
                        GetProductByCategoryResponse jsonResponse = gson.fromJson(response, GetProductByCategoryResponse.class);
                        mCategoryList=jsonResponse.getGetBuyPageProductByCategoryResult().getProducts();
                        if (mCategoryList.size()!=0) {
                                setBuyPageAdater();
                        } else {
                            Log.w(TAG, "error with Json");
                            setBuyPageAdater();
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



    private void setBuyPageAdater() {
        mBuyPageProductsAdater = new BuyPageProductsListAdapter(parentActivity, mCategoryList);
        mActivityListView.setAdapter(mBuyPageProductsAdater);
    }
}

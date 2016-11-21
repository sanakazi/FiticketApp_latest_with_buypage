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
import com.fitticket.buypage.adapters.AddToCartAdapter;
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

    private static final String GET_URL = "GET_URL";
    private static final String FROM_FLAG = "FROM_FLAG";
    private String mUrl,catId,latitude,longitude;
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String CAT_ID = "CAT_ID";
    private  static final String TAG = AddToCartFragment.class.getSimpleName();

    // region temporary values
    private AppCompatActivity parentActivity;
    AddToCartAdapter mBuyPageProductsAdater;
    private ArrayList<CategoryJson> mCategoryList;

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
        mCategoryList = new ArrayList<>();
        triggerCategoryVolleyRequest();
    }

    private void triggerCategoryVolleyRequest() {
        progressBar.setVisibility(View.VISIBLE);
        String cityId = PreferencesManager.getInstance(parentActivity).getSelectedCityId();
        Log.w(TAG, "Get categories Requst: " + Apis.GET_CATEGORIES_GROUP_URL + cityId);
        WebServices.triggerVolleyGetRequest(parentActivity, Apis.GET_CATEGORIES_GROUP_URL + cityId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "Get categories response: " + response);
                        Gson gson = new Gson();
                        CategoryJsonResponse jsonResponse = gson.fromJson(response, CategoryJsonResponse.class);
                        mCategoryList=jsonResponse.getData().getCategories();
                        if (jsonResponse.getStatusCode().equalsIgnoreCase("0")) {
                            if (jsonResponse.getData() != null && jsonResponse.getData().getCategories() != null
                                    && !jsonResponse.getData().getCategories().isEmpty()) {
                                setBuyPageAdater();
                            }

                        } else {
                            Log.w(TAG, jsonResponse.getStatusMsg());
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
        mBuyPageProductsAdater = new AddToCartAdapter(parentActivity, mCategoryList);
        mActivityListView.setAdapter(mBuyPageProductsAdater);
    }
}

package com.fitticket.buypage.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitticket.R;

import butterknife.ButterKnife;

/**
 * Created by SanaKazi on 11/16/2016.
 */
public class BuyNowFragment  extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addtocart_listitem, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}

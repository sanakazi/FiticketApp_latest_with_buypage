package com.fitticket.buypage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.activities.AddToCartActivity;
import com.fitticket.buypage.activities.BuyPageProductDetailsActivity;
import com.fitticket.buypage.pojos.GetProductByCategoryResponse;
import com.fitticket.buypage.pojos.GetProductDescriptionResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.singleton.MySingleton;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SanaKazi on 11/15/2016.
 */
public class BuyPageProductDetailsFragment extends Fragment {

    private static final String TAG = BuyPageProductDetailsFragment.class.getSimpleName();
    private int mActivityId;
    private static String URL = "http://192.168.168.234:86/WebServices/v1/AppService.svc/GetBuyPageProductDescriptionBy?ProductId=1";
// region findviewbyid
    @Bind(R.id.btn_addtocart) Button btn_addtocart;
    @Bind(R.id.btn_buy_now) Button btn_buynow;
    @Bind(R.id.item_increment) TextView item_increment;
    @Bind(R.id.item_decrement) TextView item_decrement;
    @Bind(R.id.item_count) TextView item_count;
    @Bind(R.id.qty_layout) LinearLayout qty_layout;
    @Bind(R.id.images_layout) LinearLayout images_layout;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.scroll_view) ScrollView scroll_view;
    @Bind(R.id.txt_productName) TextView txt_productName;
    @Bind(R.id.txt_short_description) TextView txt_short_description;
    @Bind(R.id.txt_product_overView) TextView txt_product_overView;
    @Bind(R.id.txt_default_qty) TextView txt_default_qty;
    @Bind(R.id.imgBanner) NetworkImageView imgBanner;

    static int price_pos=0;
    ImageLoader imageLoader;

    //endregion
    private ArrayList<String> list;
    private ArrayList<GetProductDescriptionResponse.GetBuyPageProductDescriptionByIdResultClass> mCategoryList;
    ArrayList<GetProductDescriptionResponse.ProductPricing> mPricingList;
    ArrayList<String> mImageList;
    GetProductDescriptionResponse jsonResponse;


    public static BuyPageProductDetailsFragment newInstance(int activityId) {
        BuyPageProductDetailsFragment fragment = new BuyPageProductDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(BuyPageProductDetailsActivity.ACTIVITY_ID, activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mActivityId = getArguments().getInt(BuyPageProductDetailsActivity.ACTIVITY_ID);
        }

        mCategoryList = new ArrayList<>();
        mPricingList = new ArrayList<>();
        list = new ArrayList<>();
        list.add("200gm");
        list.add("400gm");
        list.add("600gm");
        list.add("800gm");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buypageproductdetails, container, false);
        ButterKnife.bind(this, v);
        Log.w(TAG,"cat id is" +mActivityId );
       imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        triggerProductDetailsVolleyRequest(mActivityId);

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                startActivity(intent);
            }
        });
        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"2");
                startActivity(intent);
            }
        });

        item_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a  =Integer.parseInt(item_count.getText().toString());
                if(a>=0 && a<10 )
                    a++;
                item_count.setText(String.valueOf(a));
            }
        });

        item_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b  =Integer.parseInt(item_count.getText().toString());
                if(b>0)
                    b--;
                item_count.setText(String.valueOf(b));
            }
        });

    }


    //region setvalues
    private void setValues()
    {
        txt_productName.setText(jsonResponse.getGetBuyPageProductDescriptionByIdResult().getProductName());
        txt_short_description.setText(jsonResponse.getGetBuyPageProductDescriptionByIdResult().getShortDescription());
        txt_product_overView.setText(jsonResponse.getGetBuyPageProductDescriptionByIdResult().getProductOverView());
        imgBanner.setImageUrl(mImageList.get(0), imageLoader);
        imgBanner.setDefaultImageResId(R.drawable.ic_launcher);
        addqty();
        addimages(mImageList.size());


    }

 //   endregion

    //region addqty
   private void addqty()
   {


       for (int i = 0; i < mPricingList.size(); i++) {

            //TextView textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.timeslot_textview, null);
         LinearLayout l1 = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_buypageproductdetails_qtylist, null);
          final TextView  txt_qty = (TextView)l1.findViewById(R.id.txt1);
           txt_qty.setText(mPricingList.get(i).getUnitName());
           txt_qty.setTag(i);
           if(price_pos==i)
           {txt_qty.setBackgroundResource(R.color.GrayCloud);}
           else
               txt_qty.setBackgroundResource(R.color.Platinum);

           txt_qty.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   txt_default_qty.setText("(" + txt_qty.getText().toString() + ")");
                   price_pos= (int)v.getTag();
                   qty_layout.removeAllViews();
                   addqty();
                 /*  int pos = (Integer)v.getTag();
                   selectedpos = pos;
                   Toast.makeText(getActivity(),pos + " position ", Toast.LENGTH_SHORT).show();
                   txt_qty.setBackgroundResource(R.color.GrayCloud);
                 */

               }
           });



           qty_layout.addView(l1);
       }




        }
// endregion

    //region add images
    private void addimages(int imgsize)
    {
        for (int i = 0; i <imgsize; i++) {

            //TextView textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.timeslot_textview, null);
            LinearLayout l1 = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_buypageproductdetails_imageslist, null);
            NetworkImageView productImg = (NetworkImageView)l1.findViewById(R.id.img1);

            productImg.setImageUrl(mImageList.get(i), imageLoader);
            productImg.setDefaultImageResId(R.drawable.ic_launcher);
            productImg.setTag(i);
            productImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag();
                    imgBanner.setImageUrl(mImageList.get(pos), imageLoader);
                 //   Log.w(TAG,mImageList.get(pos));
                }
            });
            images_layout.addView(l1);
        }
    }
//endregion
    private void triggerProductDetailsVolleyRequest(int catId) {
        progressBar.setVisibility(View.VISIBLE);
        Log.w(TAG, "Get categories Requst: " + URL );
        WebServices.triggerVolleyGetRequest(getActivity(), URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "Get categories response: " + response);


                        Gson gson = new Gson();
                        jsonResponse = gson.fromJson(response, GetProductDescriptionResponse.class);

                        scroll_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        mPricingList = jsonResponse.getGetBuyPageProductDescriptionByIdResult().getProductPricing();
                        mImageList = jsonResponse.getGetBuyPageProductDescriptionByIdResult().getProductImages();
                        setValues();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utilities.handleVolleyError(getActivity(), error);
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }

}

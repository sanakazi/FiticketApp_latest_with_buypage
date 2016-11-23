package com.fitticket.buypage.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.pojos.AddtoCartJsonResponse;
import com.fitticket.buypage.pojos.GetProductByCategoryResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.pojos.RateActivityJson;
import com.fitticket.model.singleton.MySingleton;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.fragments.UserRatingFragment;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/15/2016.
 */


public class BuyPageProductsListAdapter extends RecyclerView.Adapter<BuyPageProductsListAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<GetProductByCategoryResponse.Products> mCategoryList;
    PreferencesManager sPref;
    ProductSelectedListener listener;
    AddtoCartListener cart_listener;
    private static final String TAG = BuyPageProductsListAdapter.class.getSimpleName();


    public interface ProductSelectedListener {
        void onProductSelected(int activityId);
    }

    public interface AddtoCartListener {
       // void onAddToCartSelected(AddtoCartJsonResponse json);
        void onAddToCartSelected(String productid, String priceid , String customer_id , String price ,String qty , String status , String orderid, String from_which_context);
    }


    public BuyPageProductsListAdapter(Context context, ArrayList<GetProductByCategoryResponse.Products> categoryList) {
        listener = (ProductSelectedListener) context;
        cart_listener = (AddtoCartListener) context;
        mCategoryList = categoryList;
        mContext = context;
        sPref=PreferencesManager.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buypageproducts_listitem, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.gymNameTxtView.setText(mCategoryList.get(position).getShortDescription());
        holder.activityNameTxtView.setText(mCategoryList.get(position).getProductName());
        holder.txt_rupee.setText(String.valueOf(mCategoryList.get(position).getUnitPrice()));
        ImageLoader imageLoader = MySingleton.getInstance(mContext).getImageLoader();
        holder.gymLogoImgView.setImageUrl(mCategoryList.get(position).getProductImage(), imageLoader);
        holder.gymLogoImgView.setDefaultImageResId(R.drawable.ic_launcher);
        holder.gymClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductSelected(mCategoryList.get(position).getId());
            }
        });
        holder.item_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int a  =Integer.parseInt( holder.item_count.getText().toString());
                if(a>=0 && a<10 )
                a++;
           holder.item_count.setText(String.valueOf(a));
         holder.txt_rupee.setText(String.valueOf(Double.parseDouble( mCategoryList.get(position).getUnitPrice())*a));


            }
        });

        holder.item_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b  =Integer.parseInt( holder.item_count.getText().toString());
                if(b>0)
                b--;
                holder.item_count.setText(String.valueOf(b));
                holder.txt_rupee.setText(String.valueOf(Double.parseDouble( mCategoryList.get(position).getUnitPrice())*b));
            }
        });
        holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a,b,c,d,e,f,g,from_which_context;



                a=String.valueOf(mCategoryList.get(position).getId());
                b=String.valueOf(mCategoryList.get(position).getUnitId());
                c=String.valueOf(sPref.getUserId());
                d=holder.txt_rupee.getText().toString();
                e=holder.item_count.getText().toString();
                f="1";
                g="0";
                from_which_context="1";

                Log.w(TAG, "productid "+String.valueOf(mCategoryList.get(position).getId()));
                Log.w(TAG, "pricing is "+ String.valueOf(mCategoryList.get(position).getUnitId()));
                Log.w(TAG,"customerid is "+String.valueOf(sPref.getUserId()));
                Log.w(TAG,"pricing  is "+holder.txt_rupee.getText().toString());
                Log.w(TAG,"quantity is "+holder.item_count.getText().toString());

              cart_listener.onAddToCartSelected(a,b,c,d,e,f,g,from_which_context);


    }
});
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView gymLogoImgView;
        TextView activityNameTxtView,gymNameTxtView,txt_rupee,item_increment,item_decrement,item_count,btn_addtocart;
        public final LinearLayout gymClickLayout;



        public ViewHolder(View view) {
            super(view);
            gymLogoImgView = (NetworkImageView) view.findViewById(R.id.gymLogoImgView);
            activityNameTxtView = (TextView) view.findViewById(R.id.activityNameTxtView);
            gymNameTxtView = (TextView) view.findViewById(R.id.gymNameTxtView);
            txt_rupee = (TextView) view.findViewById(R.id.txt_rupee);
            gymClickLayout = (LinearLayout) view.findViewById(R.id.gymClickLayout);
            item_increment = (TextView) view.findViewById(R.id.item_increment);
            item_decrement = (TextView) view.findViewById(R.id.item_decrement);
            item_count = (TextView) view.findViewById(R.id.item_count);
            btn_addtocart = (TextView) view.findViewById(R.id.btn_addtocart);

        }
    }







}

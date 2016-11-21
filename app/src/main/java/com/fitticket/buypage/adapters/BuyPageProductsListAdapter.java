package com.fitticket.buypage.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.pojos.GetProductByCategoryResponse;
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.singleton.MySingleton;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/15/2016.
 */


public class BuyPageProductsListAdapter extends RecyclerView.Adapter<BuyPageProductsListAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<GetProductByCategoryResponse.Products> mCategoryList;
    SharedPreferences mPrefs;
    ProductSelectedListener listener;

    public interface ProductSelectedListener {
        void onProductSelected(int activityId);
    }


    public BuyPageProductsListAdapter(Context context, ArrayList<GetProductByCategoryResponse.Products> categoryList) {
        listener = (ProductSelectedListener) context;
        mCategoryList = categoryList;
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
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
        TextView activityNameTxtView,gymNameTxtView,txt_rupee,item_increment,item_decrement,item_count;
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
        }
    }


}

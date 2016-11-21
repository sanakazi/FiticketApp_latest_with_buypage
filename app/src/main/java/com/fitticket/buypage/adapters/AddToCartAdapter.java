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
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.singleton.MySingleton;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/16/2016.
 */


public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<CategoryJson> mCategoryList;
    SharedPreferences mPrefs;





    public AddToCartAdapter(Context context, ArrayList<CategoryJson> categoryList) {

        mCategoryList = categoryList;
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addtocart_listitem, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.gymNameTxtView.setText(mCategoryList.get(position).getCount() + " Activities today");
        holder.activityNameTxtView.setText(mCategoryList.get(position).getName());
        holder.txt_rupee.setText(mCategoryList.get(position).getId());
        ImageLoader imageLoader = MySingleton.getInstance(mContext).getImageLoader();
        holder.gymLogoImgView.setImageUrl(mCategoryList.get(position).getCategoryImage(), imageLoader);
        holder.gymLogoImgView.setDefaultImageResId(R.drawable.ic_launcher);
        holder.gymClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.item_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a  =Integer.parseInt( holder.item_count.getText().toString());
                if(a>=0 && a<10 )
                    a++;
                holder.item_count.setText(String.valueOf(a));
            }
        });

        holder.item_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b  =Integer.parseInt( holder.item_count.getText().toString());
                if(b>0)
                    b--;
                holder.item_count.setText(String.valueOf(b));
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
            item_increment = (TextView) view.findViewById(R.id.item_increment);
            item_decrement = (TextView) view.findViewById(R.id.item_decrement);
            item_count = (TextView) view.findViewById(R.id.item_count);
            gymClickLayout = (LinearLayout) view.findViewById(R.id.gymClickLayout);

        }
    }
}

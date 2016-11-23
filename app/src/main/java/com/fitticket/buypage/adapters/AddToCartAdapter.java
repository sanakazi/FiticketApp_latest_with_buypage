package com.fitticket.buypage.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.pojos.AddToCartListJsonResponse;
import com.fitticket.model.pojos.CategoryJson;
import com.fitticket.model.singleton.MySingleton;
import com.fitticket.model.singleton.PreferencesManager;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/16/2016.
 */


public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<AddToCartListJsonResponse.CartProducts> mCategoryList;
    PreferencesManager sPref;
    RemoveFromCartListener listener;
    CartCountListener cartCountListener;
    private static final String TAG=AddToCartAdapter.class.getSimpleName();
    double total=0.0;
    double []total_value;


    public interface RemoveFromCartListener{
         void onRemoveFromCartSelected(int cartId , int userID);
    }

    public interface CartCountListener{
         void onCartAmountChanged(double value);
    }

    public AddToCartAdapter(Context context, ArrayList<AddToCartListJsonResponse.CartProducts> categoryList) {

        mCategoryList = categoryList;
        mContext = context;
        sPref = PreferencesManager.getInstance(context);
        listener = (RemoveFromCartListener) context;
        cartCountListener = (CartCountListener) context;
        total_value = new double[categoryList.size()];
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


        holder.gymNameTxtView.setText(mCategoryList.get(position).getShortDescription() + " Activities today");
        holder.activityNameTxtView.setText(mCategoryList.get(position).getProductName());
        holder.item_count.setText(String.valueOf(mCategoryList.get(position).getQuantity()));
        double rupee =Integer.parseInt(holder.item_count.getText().toString())*Double.parseDouble(mCategoryList.get(position).getUnitPrice());
        holder.txt_rupee.setText(String.valueOf(rupee));
        total_value[position] = rupee;
        ImageLoader imageLoader = MySingleton.getInstance(mContext).getImageLoader();
        holder.gymLogoImgView.setImageUrl(mCategoryList.get(position).getProductImage(), imageLoader);
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
                if(a>=1 && a<10 )
                    a++;
                holder.item_count.setText(String.valueOf(a));
                double rupee =Integer.parseInt(holder.item_count.getText().toString())*Double.parseDouble(mCategoryList.get(position).getUnitPrice());
                holder.txt_rupee.setText(String.valueOf(rupee));

                total_value[position] = rupee;
                calculate();

            }
        });

        holder.item_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b  =Integer.parseInt( holder.item_count.getText().toString());
                if(b>1)
                    b--;
                holder.item_count.setText(String.valueOf(b));
                double rupee =Integer.parseInt(holder.item_count.getText().toString())*Double.parseDouble(mCategoryList.get(position).getUnitPrice());
                holder.txt_rupee.setText(String.valueOf(rupee));
                total_value[position] = rupee;
                calculate();
            }
        });

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogBox(v,position);

            }
        });

        calculate();
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView gymLogoImgView;
        TextView activityNameTxtView,gymNameTxtView,txt_rupee,item_increment,item_decrement,item_count,btn_remove;
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
            btn_remove = (TextView) view.findViewById(R.id.btn_remove);

        }
    }


    //region alert DialogBox

    public void openDialogBox(View v,final int position){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage("Are you sure you want to remove the item from cart?");

        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        total_value[position]=0.0;
                        calculate();
                        listener.onRemoveFromCartSelected(mCategoryList.get(position).getCartId(), sPref.getUserId());
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.dismiss();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //endregion

//region calculate total
    private void calculate()
    {
        total=0.0;
        for(int i=0;i<mCategoryList.size();i++)
        {
            total+=total_value[i];
            Log.w(TAG,"pos" + i + " value " + total_value[i]);
        }
        Log.w(TAG,"total value is " + total);
        cartCountListener.onCartAmountChanged(total);
    }

    //endregion
}

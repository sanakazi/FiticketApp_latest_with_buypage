package com.fitticket.buypage.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.activities.AddToCartActivity;
import com.fitticket.buypage.others.BuyPageSingleton;
import com.fitticket.model.singleton.MySingleton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SanaKazi on 11/16/2016.
 */
public class BuyNowFragment  extends Fragment {

    @Bind(R.id.activityNameTxtView) TextView activityNameTxtView;
    @Bind(R.id.gymNameTxtView) TextView gymNameTxtView;
    @Bind(R.id.item_count) TextView item_count;
    @Bind(R.id.txt_rupee) TextView txt_rupee;
    @Bind(R.id.gymLogoImgView) NetworkImageView gymLogoImgView;
    @Bind(R.id.item_increment) TextView item_increment;
    @Bind(R.id.item_decrement) TextView item_decrement;
    @Bind(R.id.btn_remove) TextView btn_remove;




    String CartId,Quantity,Price,ProductImage,ProductName,ShortDescription,UnitPrice;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addtocart_listitem, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        CartId = bundle.getString("CartId"," ");
        Quantity = bundle.getString("Quantity"," ");
        Price = bundle.getString("Price"," ");
        ProductImage = bundle.getString("ProductImage"," ");
        ProductName = bundle.getString("ProductName"," ");
        ShortDescription = bundle.getString("ShortDescription"," ");
        UnitPrice = bundle.getString("UnitPrice"," ");


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_remove.setVisibility(View.GONE);
        activityNameTxtView.setText(String.valueOf(ProductName));
        gymNameTxtView.setText(String.valueOf(ShortDescription));
        item_count.setText(String.valueOf(Quantity));
        double rupee =Integer.parseInt(item_count.getText().toString())*Double.parseDouble(UnitPrice);
        txt_rupee.setText(String.valueOf(rupee));
        calculate(rupee);
        ImageLoader imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        gymLogoImgView.setImageUrl(ProductImage, imageLoader);
       gymLogoImgView.setDefaultImageResId(R.drawable.ic_launcher);

        events();
    }

    public void events()
    {
        item_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a  =Integer.parseInt( item_count.getText().toString());
                if(a>=1 && a<10 )
                    a++;
                item_count.setText(String.valueOf(a));
                double rupee =Integer.parseInt(item_count.getText().toString())*Double.parseDouble(UnitPrice);

                txt_rupee.setText(String.valueOf(rupee));

                calculate(rupee);
            }
        });

        item_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b  =Integer.parseInt( item_count.getText().toString());
                if(b>1)
                    b--;
                item_count.setText(String.valueOf(b));
                double rupee =Integer.parseInt(item_count.getText().toString())*Double.parseDouble(UnitPrice);
               txt_rupee.setText(String.valueOf(rupee));
                calculate(rupee);
            }
        });
    }

    private void calculate(double rupee)
    {
        ((AddToCartActivity) getActivity()).setBarTitle(String.valueOf(BuyPageSingleton.CART_COUNT)," ",String.valueOf(rupee));
    }
}

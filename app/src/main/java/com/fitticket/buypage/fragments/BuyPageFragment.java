package com.fitticket.buypage.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fitticket.R;
import com.fitticket.buypage.activities.AddToCartActivity;
import com.fitticket.buypage.pojos.BuyPageCategoryJsonResponse;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.CategoryJsonResponse;
import com.fitticket.model.services.FetchAllActivitiesService;
import com.fitticket.model.services.FetchAllGymsService;
import com.fitticket.model.singleton.MySingleton;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.activities.MainActivity;
import com.fitticket.viewmodel.custom.ProgressBarCircular;
import com.fitticket.viewmodel.utils.Utilities;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyPageFragment extends Fragment {
    private static final String TAG = BuyPageFragment.class.getSimpleName();
    private static final long ONE_DAY = 24 * 60 * 60 * 1000; //No of milliseconds in a day
    ProgressBarCircular progressBar;
    SharedPreferences mPrefs;


    Typeface awesomeFont;
    public FragmentInteractionListener mListener;
    private RecyclerView mCatRecyclerView;
    private RecyclerView.Adapter mBuyPageAdater;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppCompatActivity parentActivity;
    ImageView icon_cart;

    private ArrayList<BuyPageCategoryJsonResponse.BuyPageCategoryJson> mCategoryList;

    public BuyPageFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static BuyPageFragment newInstance(String param1, String param2) {
        BuyPageFragment fragment = new BuyPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mListener = (FragmentInteractionListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //TODO
        }

        ((MainActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.custom_actionbar_fragment_buypage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_page, container, false);
        mCatRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        progressBar = (ProgressBarCircular) view.findViewById(R.id.progressBar);


        mCatRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       setHasOptionsMenu(true);
        parentActivity = (AppCompatActivity) getActivity();

        awesomeFont = Typeface.createFromAsset(parentActivity.getAssets(), "fontawesome-webfont.ttf");



        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(parentActivity);
        mCatRecyclerView.setLayoutManager(mLayoutManager);

        //Get Category list from sungleton, if it is empty get new list from server
        mCategoryList = MySingleton.getInstance(parentActivity).getBuyPagecategoryList();
        if (mCategoryList.isEmpty()) {
            triggerCategoryVolleyRequest();
        } else {
            setBuyPageAdater();
        }

    }

 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_addtocart, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_to_cart:
                Intent intent = new Intent(getActivity(), AddToCartActivity.class);
                intent.putExtra(AddToCartActivity.FROM_CART_OR_BUYNOW,"1");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void triggerCategoryVolleyRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Get categories Requst: " + Apis.GET_BUY_PAGE_MAIN_CATEGORY );
        WebServices.triggerVolleyGetRequest(parentActivity, Apis.GET_BUY_PAGE_MAIN_CATEGORY ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Get categories response: " + response);
                        Gson gson = new Gson();
                        BuyPageCategoryJsonResponse jsonResponse = gson.fromJson(response, BuyPageCategoryJsonResponse.class);
                        if (jsonResponse.getGetBuyPageCategoryResult()!=null) {
                            mCategoryList=jsonResponse.getGetBuyPageCategoryResult().getCategories();
                            MySingleton.getInstance(parentActivity).setBuyPagecategoryList(mCategoryList);
                            setBuyPageAdater();
                        } else {
                            Log.e(TAG, "Some error with Json Response");
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
        mBuyPageAdater = new BuyPageAdater(parentActivity, mCategoryList);
        mCatRecyclerView.setAdapter(mBuyPageAdater);
    }

    public interface FragmentInteractionListener {
        void onBuyPageCategoryClicked(String cateGoryId,int position,String name);
    }

    public class BuyPageAdater extends RecyclerView.Adapter<BuyPageAdater.ViewHolder> {

        private final ArrayList<BuyPageCategoryJsonResponse.BuyPageCategoryJson> mCategoryList;
        Context mContext;
        SharedPreferences mPrefs;
        private ImageLoader mImageLoader;
        private int lastPosition = -1;


        // Provide a suitable constructor (depends on the kind of dataset)
        public BuyPageAdater(Context context, ArrayList<BuyPageCategoryJsonResponse.BuyPageCategoryJson> categoryList) {
            mImageLoader = MySingleton.getInstance(context).getImageLoader();
            mCategoryList = categoryList;
            mContext = context;
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.buypage_listitem, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            viewHolder.mCategoryTitle.setText(mCategoryList.get(position).getCategoryName());
            viewHolder.mCategorySubTitle.setText(mCategoryList.get(position).getShortDescription());

            viewHolder.mCategoryImageview.setImageUrl(mCategoryList.get(position).getBackgroundImage(), mImageLoader);

            viewHolder.mCategoryImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onBuyPageCategoryClicked(mCategoryList.get(position).getId(),position,mCategoryList.get(position).getCategoryName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }


        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mCategoryTitle, mCategorySubTitle;
            public NetworkImageView mCategoryImageview;
            FrameLayout categoryLayout;

            public ViewHolder(View v) {
                super(v);
                mCategoryTitle = (TextView) v.findViewById(R.id.titleTextView);
                mCategorySubTitle = (TextView) v.findViewById(R.id.subTitleTextView);
                mCategoryImageview = (NetworkImageView) v.findViewById(R.id.categoryImageView);
                mCategoryImageview.setDefaultImageResId(R.drawable.ic_default);
                categoryLayout = (FrameLayout) v.findViewById(R.id.category_list_item);

            }

        }
    }


}

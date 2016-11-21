package com.fitticket.viewmodel.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.R;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.RateActivityJson;
import com.fitticket.model.pojos.TrackingDetailsJson;
import com.fitticket.model.utils.WebServices;

import org.json.JSONObject;

/**
 * Created by SanaKazi on 11/4/2016.
 */
public class UserRatingFragment extends DialogFragment {
    RelativeLayout rl2,rl3;
    Button btn_submit;
    RatingBar ratingbar;
    CheckBox check1,check2,check3,check4;
    private String enrollmentId,gymName, activityName;
    private static final String ENROLLMENTID ="enrollmentId" ;
    private static final String GYM_NAME ="gymName" ;
    private static final String ACTIVITYNAME ="activityName" ;
    private static final String TAG = UserRatingFragment.class.getSimpleName();
    int attended = 1;
    private static String comments="abc";

    public static UserRatingFragment newInstance(String enrollmentId,String gymName, String activityName) {
        UserRatingFragment f = new UserRatingFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ENROLLMENTID,enrollmentId);
        args.putString(GYM_NAME,gymName);
        args.putString(ACTIVITYNAME,activityName);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth);
        enrollmentId = getArguments().getString(ENROLLMENTID);
        gymName= getArguments().getString(GYM_NAME);
        activityName=getArguments().getString(ACTIVITYNAME);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rating_popup, container, false);
        rl2 = (RelativeLayout)v.findViewById(R.id.rl2);
        rl3 = (RelativeLayout)v.findViewById(R.id.rl3);

        check1 = (CheckBox) v.findViewById(R.id.check1);
        check2 = (CheckBox)v.findViewById(R.id.check2);
        check3 = (CheckBox)v.findViewById(R.id.check3);
        check4 = (CheckBox)v.findViewById(R.id.check4);

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check1.setChecked(true);
                check2.setChecked(false);
                check3.setChecked(false);
                check4.setChecked(false);
                comments =check1.getText().toString();
            }
        });

        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check2.setChecked(true);
                check1.setChecked(false);
                check3.setChecked(false);
                check4.setChecked(false);
                comments =check2.getText().toString();
            }
        });


        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check3.setChecked(true);
                check1.setChecked(false);
                check2.setChecked(false);
                check4.setChecked(false);
                comments =check3.getText().toString();
            }
        });

        check4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check4.setChecked(true);
                check1.setChecked(false);
                check2.setChecked(false);
                check3.setChecked(false);
                comments =check4.getText().toString();
            }
        });


        TextView txt3 = (TextView)v.findViewById(R.id.txt3);
        TextView txt4 = (TextView)v.findViewById(R.id.txt4);
        btn_submit = (Button)v.findViewById(R.id.btn_submit);
        final EditText et1 = (EditText)v.findViewById(R.id.et1);
        SwitchCompat switchCompat=(SwitchCompat)v.findViewById(R.id.switchButton);
         ratingbar=(RatingBar)v.findViewById(R.id.ratings);
        txt3.setText(gymName);
        txt4.setText(activityName);



        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    rl2.setVisibility(View.GONE);
                    rl3.setVisibility(View.VISIBLE);
                    attended = 1;
                }
                else
                {
                    attended = 0;
                    rl3.setVisibility(View.GONE);
                    rl2.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final RateActivityJson json = new RateActivityJson();
                json.setEnrollmentId(enrollmentId);
                json.setAttendStatus(String.valueOf(attended));

                if(attended==1) {
                    json.setRating( String.valueOf(ratingbar.getRating())  );
                    json.setComments(String.valueOf(et1.getText().toString()));
                    if(ratingbar.getRating()==0.0)
                        Toast.makeText(getActivity(),"Please provide rating" , Toast.LENGTH_SHORT).show();
                    else{
                        callRatingService(json);}


                }
                else if (attended==0)
                {
                    json.setRating("0");
                    if(comments.equals("abc"))
                    {
                        Toast.makeText(getActivity(),"Please select a reason" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        json.setComments(comments);
                        callRatingService(json);
                    }

                }




            }
        });
        return v;

    }

    private void callRatingService(RateActivityJson json)
    {
        WebServices.triggerVolleyPostRequest(getActivity(),json, Apis.RATE_ACTIVITIES,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.w(TAG, "Rating response " + response.toString());

                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Booking response error: " + error.toString());
                dismiss();
            }
        }, RateActivityJson.class);
    }
}

package com.fitticket.viewmodel.proximityalert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fitticket.model.constants.Apis;
import com.fitticket.model.pojos.BookRequestJson;
import com.fitticket.model.pojos.PostResponseJson;
import com.fitticket.model.pojos.TrackingDetailsJson;
import com.fitticket.model.singleton.PreferencesManager;
import com.fitticket.model.utils.WebServices;
import com.fitticket.viewmodel.activities.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;


public class ProximityIntentReceiver extends BroadcastReceiver {
    private static final String TAG= ProximityIntentReceiver.class.getSimpleName();

       private static final int NOTIFICATION_ID = 1000;
    SharedPreferences shared;

       @SuppressWarnings("deprecation")
       @Override
       public void onReceive(Context context, Intent intent) {
           shared = context.getSharedPreferences(PreferencesManager.PREF_NAME, context.MODE_PRIVATE);
           String enrollmentId= shared.getString("tracking_enrollmentId", "");
           String lat = shared.getString("tracking_lati", "");
           String longi = shared.getString("tracking_longi", "");

    	   String key = LocationManager.KEY_PROXIMITY_ENTERING;
    	   Boolean entering = intent.getBooleanExtra(key, false);
    	   if (entering) {

                     Log.d(getClass().getSimpleName(), "entering");
              }else {
                     Log.d(getClass().getSimpleName(), "exiting");
              }

           Log.w("lat and longi are" , lat + " , " + longi);

           final TrackingDetailsJson json = new TrackingDetailsJson();
           json.setEnrollmentId(Integer.parseInt(enrollmentId));
           json.setLat(lat);
           json.setLongi(longi);

           WebServices.triggerVolleyPostRequest(context,json, Apis.TRACKING_ATTENDANCE_URL,new Response.Listener<JSONObject>() {

               @Override
               public void onResponse(JSONObject response) {
                   Log.w(TAG, "Tracking response " + response.toString());

                 /*  shared.edit().remove("tracking_enrollmentId");
                   shared.edit().remove("tracking_lati");
                   shared.edit().remove("tracking_longi");
                   shared.edit().apply();*/
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.e(TAG, "Booking response error: " + error.toString());

               }
           }, TrackingDetailsJson.class);




      }

}

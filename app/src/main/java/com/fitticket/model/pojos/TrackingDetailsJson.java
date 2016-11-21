package com.fitticket.model.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SanaKazi on 11/4/2016.
 */
public class TrackingDetailsJson {
    private int enrollmentId;
    private String lat;
 //   @SerializedName("long")
   private String longi;

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}

package com.lefalexiou.geoar_app.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Place {
    private LatLng latLng;
    private String title;
    private long AOE;

    public Place() {
    }

    public Place(LatLng latLng, String title, long AOE) {
        this.latLng = latLng;
        this.title = title;
        this.AOE = AOE;
    }

    public LatLng makeGeoPointToLatLng(GeoPoint gp){
        return new LatLng(gp.getLatitude(),gp.getLongitude());
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAOE() {
        return AOE;
    }

    public void setAOE(long AOE) {
        this.AOE = AOE;
    }

    @Override
    public String toString() {
        return "place{" +
                "latLng=" + latLng +
                ", title='" + title + '\'' +
                ", AOE=" + AOE +
                '}';
    }
}

package com.lefalexiou.geoar_app.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

public class Place {
    private LatLng latLng;
    private String title;
    private long AOE;
    private DocumentReference hologramReference;

    public Place() {
    }

    public Place(LatLng latLng, String title, long AOE, DocumentReference hologramReference) {

        this.latLng = latLng;
        this.title = title;
        this.AOE = AOE;
        this.hologramReference = hologramReference;
    }

    public LatLng makeGeoPointToLatLng(GeoPoint gp) {
        return new LatLng(gp.getLatitude(), gp.getLongitude());
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

    public DocumentReference getHologramReference() {
        return hologramReference;
    }

    @NonNull
    @Override
    public String toString() {
        return "place{" +
                "latLng=" + latLng +
                ", title='" + title + '\'' +
                ", AOE=" + AOE +
                ", Hologram=" + hologramReference.getPath() +
                '}';
    }
}

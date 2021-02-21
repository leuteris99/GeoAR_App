package com.lefalexiou.geoar_app.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Place {
    private LatLng latLng;
    private String title;
    private long AOE;
    private Hologram hologram;

    public Place() {
    }

    public Place(LatLng latLng, String title, long AOE, Hologram hologram) {

        this.latLng = latLng;
        this.title = title;
        this.AOE = AOE;
        this.hologram = hologram;
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

    public Hologram getHologram() {
        return hologram;
    }

    @NonNull
    @Override
    public String toString() {
        return "place{" +
                "latLng=" + latLng +
                ", title='" + title + '\'' +
                ", AOE=" + AOE +
                ", Hologram=" + hologram.toString() +
                '}';
    }
}

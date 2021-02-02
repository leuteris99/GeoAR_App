package com.lefalexiou.geoar_app.models;

import java.util.ArrayList;

public class Route {
    private String title;
    private ArrayList<String> places;

    public Route() {
    }

    public Route(String title, ArrayList<String> places) {
        this.title = title;
        this.places = places;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tittle) {
        this.title = tittle;
    }

    public ArrayList<String> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Route{" +
                "title='" + title + '\'' +
                ", places=" + places +
                '}';
    }
}

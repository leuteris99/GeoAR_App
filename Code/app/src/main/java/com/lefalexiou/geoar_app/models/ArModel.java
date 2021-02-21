package com.lefalexiou.geoar_app.models;

import androidx.annotation.NonNull;

public class ArModel {
    private String title = "";
    private String modelURL = "";
    private float scale = 0;
    private float distFromAnchor = 0;
    private int animationSpeed = 0;

    public ArModel(String title, String modelURL, float scale, float distFromAnchor, int animationSpeed) {
        this.title = title;
        this.modelURL = modelURL;
        this.scale = scale;
        this.distFromAnchor = distFromAnchor;
        this.animationSpeed = animationSpeed;
    }

    public String getTitle() {
        return title;
    }

    public String getModelURL() {
        return modelURL;
    }

    public float getScale() {
        return scale;
    }

    public float getDistFromAnchor() {
        return distFromAnchor;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    @NonNull
    @Override
    public String toString() {
        return "ArModel{" +
                "title='" + title + '\'' +
                ", modelURL='" + modelURL + '\'' +
                ", scale=" + scale +
                ", distFromAnchor=" + distFromAnchor +
                ", animationSpeed=" + animationSpeed +
                '}';
    }
}

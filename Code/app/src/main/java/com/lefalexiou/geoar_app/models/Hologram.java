package com.lefalexiou.geoar_app.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Hologram {
    private String title = "";
    private String imageURL = "";
    private String description = "";
    private String question = "";
    private ArrayList<String> answerArray;
    private String webURL = "";
    private ArModel arModel;
    private static final String TAG = "Hologram";

    public Hologram(String title, String imageURL, String description, ArrayList<String> questionArray, String webURL, ArModel arModel) {
        this.title = title;
        if (!imageURL.equals("")) {
            this.imageURL = imageURL;
        }
        this.description = description;
        if (!questionArray.isEmpty()) {
            answerArray = new ArrayList<>();
            this.question = questionArray.get(0);
            questionArray.remove(0);
            answerArray.addAll(questionArray);
        }
        if (!webURL.equals("")) {
            this.webURL = webURL;
        }
        if (!arModel.getTitle().equals("")){
            this.arModel = arModel;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswerArray() {
        return answerArray;
    }

    public String getWebURL() {
        return webURL;
    }

    public ArModel getArModel() {
        return arModel;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hologram{" +
                "title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", answerArray=" + answerArray +
                ", webURL='" + webURL + '\'' +
                ", arModel=" + arModel.toString() + '\'' +
                '}';
    }
}

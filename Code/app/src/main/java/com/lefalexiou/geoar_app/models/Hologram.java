package com.lefalexiou.geoar_app.models;

import java.util.ArrayList;

public class Hologram {
    private String title = "";
    private String imageURL = "";
    private String description = "";
    private String question = "";
    private ArrayList<String> answerArray;
    private String webURL = "";

    public Hologram() {
    }

    public Hologram(String title, String imageURL, String description, ArrayList<String> questionArray, String webURL) {
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

    @Override
    public String toString() {
        return "Hologram{" +
                "title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", description='" + description + '\'' +
                ", question='" + question + '\'' +
                ", answerArray=" + answerArray +
                ", webURL='" + webURL + '\'' +
                '}';
    }
}

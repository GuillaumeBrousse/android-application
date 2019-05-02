package com.example.jaee.allocine.classes;

import java.io.Serializable;

public class Movie implements Serializable{
    private Integer code;
    private String title;
    private String actors;
    private String directors;
    private Integer productionYear;
    private String posterLink;
    private String link;
    private String synopsis;

    public Movie(Integer code, String title, String actors, String directors, Integer productionYear, String posterLink, String link, String synopsis) {
        this.code = code;
        this.title = title;
        this.actors = actors;
        this.directors = directors;
        this.productionYear = productionYear;
        this.posterLink = posterLink;
        this.link = link;
        this.synopsis = synopsis;
    }

    public Integer getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getActors() {
        return actors;
    }

    public String getDirectors() {
        return directors;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public String getLink() {
        return link;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
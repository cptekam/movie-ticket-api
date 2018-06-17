package com.movie.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "shows", indexes = {@Index(name = "showIndex", columnList = "showId", unique = true)})
public class Show {
    @Id
    private int showId;
    private String language;
    private String city;
    private int availability;
    private Date date;
    private int movieId;

    public Show() {
    }

    public Show(int showId, String language, String city, int availability, Date date, int movieId) {
        this.showId = showId;
        this.language = language;
        this.city = city;
        this.availability = availability;
        this.date = date;
        this.movieId = movieId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showId=" + showId +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", availability=" + availability +
                ", date=" + date +
                ", movieId=" + movieId +
                '}';
    }
}

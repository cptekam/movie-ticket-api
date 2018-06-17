package com.movie.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Movie {
    @Id
    private int id;
    private String name;
    private String description;
    private int ratiing;
    private Date releaseDate;

    public Movie() {
    }

    public Movie(int id, String name, String description, int ratiing, Date releaseDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ratiing = ratiing;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRatiing() {
        return ratiing;
    }

    public void setRatiing(int ratiing) {
        this.ratiing = ratiing;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ratiing=" + ratiing +
                ", releaseDate=" + releaseDate +
                '}';
    }
}

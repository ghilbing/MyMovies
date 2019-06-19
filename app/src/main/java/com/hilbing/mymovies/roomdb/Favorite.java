package com.hilbing.mymovies.roomdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorite_table")
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movie_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "vote_average")
    private Double vote_average;

    @ColumnInfo(name = "poster_path")
    private String poster_path;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "release_date")
    private String release_date;

    @Ignore
    public Favorite(int movie_id, String title, Double vote_average, String poster_path, String overview, String release_date) {
        this.movie_id = movie_id;
        this.title = title;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public Favorite(int id, int movie_id, String title, Double vote_average, String poster_path, String overview, String release_date) {
        this.id = id;
        this.movie_id = movie_id;
        this.title = title;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}

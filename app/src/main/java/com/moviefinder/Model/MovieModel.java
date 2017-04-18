package com.moviefinder.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by training on 18/04/17.
 */

public class MovieModel implements Parcelable {

    private String title, genre, releaseDate, plot, thumbnailUrl;
    private ArrayList<RatingModel> ratings;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public ArrayList<RatingModel> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<RatingModel> ratings) {
        this.ratings = ratings;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public MovieModel(){

    }

    private MovieModel(Parcel in) {
        title = in.readString();
        genre = in.readString();
        releaseDate = in.readString();
        plot = in.readString();
        ratings = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(genre);
        parcel.writeString(releaseDate);
        parcel.writeString(plot);
        parcel.writeList(ratings);
    }
}

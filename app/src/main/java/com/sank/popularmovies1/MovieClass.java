package com.sank.popularmovies1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sanket on 25/10/16.
 */

public class MovieClass implements Parcelable{
    private String posterurl, movieName, rating, releaseDate, overview;

    public MovieClass(){}
    public String getPosterurl() {
        return posterurl;
    }

    public void setPosterurl(String posterurl) {
        this.posterurl = posterurl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterurl);
        dest.writeString(movieName);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieClass> CREATOR = new Parcelable.Creator<MovieClass>() {
        public MovieClass createFromParcel(Parcel in) {
            return new MovieClass(in);
        }

        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };

    private MovieClass(Parcel in) {
        posterurl = in.readString();
        movieName = in.readString();
        overview= in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }
}

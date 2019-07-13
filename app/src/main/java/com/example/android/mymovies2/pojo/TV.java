package com.example.android.mymovies2.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.mymovies2.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TV implements Parcelable {
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = null;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("origin_country")
    private List<String> originCountry = null;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;

    protected TV(Parcel in) {
        originalName = in.readString();
        name = in.readString();
        popularity = in.readDouble();
        originCountry = in.createStringArrayList();
        voteCount = in.readInt();
        firstAirDate = in.readString();
        backdropPath = in.readString();
        originalLanguage = in.readString();
        id = in.readInt();
        voteAverage = in.readDouble();
        overview = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<TV> CREATOR = new Creator<TV>() {
        @Override
        public TV createFromParcel(Parcel in) {
            return new TV(in);
        }

        @Override
        public TV[] newArray(int size) {
            return new TV[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalName);
        dest.writeString(name);
        dest.writeDouble(popularity);
        dest.writeStringList(originCountry);
        dest.writeInt(voteCount);
        dest.writeString(firstAirDate);
        dest.writeString(backdropPath);
        dest.writeString(originalLanguage);
        dest.writeInt(id);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(posterPath);
    }

    public TV(String originalName, String name, String firstAirDate, int id, double voteAverage, String overview, String posterPath) {
        this.originalName = originalName;
        this.name = name;
        this.firstAirDate = firstAirDate;
        this.id = id;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public String getFullSmallPosterPath () {
        return Constants.BASE_URL_POSTER + Constants.SMALL_POSTER_SIZE + posterPath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}

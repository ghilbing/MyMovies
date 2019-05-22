package com.hilbing.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResults implements Parcelable
{

        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("total_results")
        @Expose
        private Integer totalResults;
        @SerializedName("total_pages")
        @Expose
        private Integer totalPages;
        @SerializedName("results")
        @Expose
        private List<Movie> results = null;
        public final static Parcelable.Creator<MovieResults> CREATOR = new Creator<MovieResults>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public MovieResults createFromParcel(Parcel in) {
                return new MovieResults(in);
            }

            public MovieResults[] newArray(int size) {
                return (new MovieResults[size]);
            }

        };

        protected MovieResults(Parcel in) {
            this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(this.results, (Movie.class.getClassLoader()));
        }

        public MovieResults() {
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public List<Movie> getResults() {
            return results;
        }

        public void setResults(List<Movie> results) {
            this.results = results;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(page);
            dest.writeValue(totalResults);
            dest.writeValue(totalPages);
            dest.writeList(results);
        }

        public int describeContents() {
            return 0;
        }

}


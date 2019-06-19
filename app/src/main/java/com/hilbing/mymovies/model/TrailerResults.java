package com.hilbing.mymovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrailerResults implements Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("results")
        @Expose
        private List<Trailer> results = null;
        public final static Parcelable.Creator<TrailerResults> CREATOR = new Creator<TrailerResults>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public TrailerResults createFromParcel(Parcel in) {
                return new TrailerResults(in);
            }

            public TrailerResults[] newArray(int size) {
                return (new TrailerResults[size]);
            }

        };

        protected TrailerResults(Parcel in) {
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(this.results, (Trailer.class.getClassLoader()));
        }

        /**
         * No args constructor for use in serialization
         *
         */
        public TrailerResults() {
        }

        /**
         *
         * @param id
         * @param results
         */
        public TrailerResults(Integer id, List<Trailer> results) {
            super();
            this.id = id;
            this.results = results;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<Trailer> getResults() {
            return results;
        }

        public void setResults(List<Trailer> results) {
            this.results = results;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeList(results);
        }

        public int describeContents() {
            return 0;
        }

}

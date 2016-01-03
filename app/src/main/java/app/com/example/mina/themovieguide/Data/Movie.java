package app.com.example.mina.themovieguide.Data;

import java.io.Serializable;

/**
 * original title
 movie poster image thumbnail
 A plot synopsis (called overview in the api)
 user rating (called vote_average in the api)
 release date
 */
public class Movie implements Serializable {

    private String title;
    private String imagePath;
    private String overView;
    private double rating;
    private String date;
    private String id ;

    public Movie(String id , String title, String imagePath, String overView, double rating, String date) {
        this.title = title;
        this.imagePath = imagePath;
        this.overView = overView;
        this.rating = rating;
        this.date = date;
        this.id = id ;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }


}

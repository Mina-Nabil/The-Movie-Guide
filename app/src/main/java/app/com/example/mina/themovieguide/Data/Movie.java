package app.com.example.mina.themovieguide.Data;

/**
 * original title
 movie poster image thumbnail
 A plot synopsis (called overview in the api)
 user rating (called vote_average in the api)
 release date
 */
public class Movie {

    private String Title;
    private String imagePath;
    private String overView;
    private float rating;
    private String date;

    public Movie(String title, String imagePath, String overView, float rating, String date) {
        Title = title;
        this.imagePath = imagePath;
        this.overView = overView;
        this.rating = rating;
        this.date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package app.com.example.mina.themovieguide.Data;

/**
 * Created by Mina on 26-Dec-15.
 */
public class Review {

    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
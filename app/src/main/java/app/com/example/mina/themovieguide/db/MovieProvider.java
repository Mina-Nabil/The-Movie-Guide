package app.com.example.mina.themovieguide.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Mina on 28-Dec-15.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {

    public static final String AUTHORITY = "app.com.example.mina.themovieguide.db.MovieProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String REVIEWS = "reviews";
        String TRAILERS = "trailers";
    }

    private static Uri buildUri (String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for(String path: paths){
            builder.appendPath(path);
        }
        return builder.build();
    }


    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri (
                path = Path.MOVIES,
                type = "vnd.android.cursor.item/movie",
                defaultSort = MovieColumns.MovieTitle+ " ASC")
        public static final Uri CONTENT_Uri = buildUri(Path.MOVIES);


        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.MovieID,
                pathSegment = 1)
        public static Uri withId(String id){
            return buildUri(Path.MOVIES, id);
        }


    }


    @TableEndpoint(table = MovieDatabase.REVIEWS)
      public static class Reviews {
        @ContentUri (
                path = Path.REVIEWS,
                type = "vnd.android.cursor.item/review",
                defaultSort = ReviewColumns.ReviewID+ " ASC")
        public static final Uri CONTENT_Uri = buildUri(Path.REVIEWS);


        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumns.ReviewID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.REVIEWS, String.valueOf(id));
        }


    }


    @TableEndpoint(table = MovieDatabase.TRAILERS)
    public static class Trailers {
        @ContentUri (
                path = Path.TRAILERS,
                type = "vnd.android.cursor.item/trailer",
                defaultSort = TrailerColumns.TrailerID+ " ASC")
        public static final Uri CONTENT_Uri = buildUri(Path.TRAILERS);


        @InexactContentUri(
                name = "TRAILER_ID",
                path = Path.TRAILERS + "/#",
                type = "vnd.android.cursor.item/trailer",
                whereColumn = TrailerColumns.TrailerID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.TRAILERS, String.valueOf(id));
        }


    }
}

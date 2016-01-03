package app.com.example.mina.themovieguide.db;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Mina on 28-Dec-15.
 */

@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {

    private MovieDatabase() {}

    public static final int VERSION = 2;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";

    @Table(ReviewColumns.class) public static final String REVIEWS = "reviews";

    @Table(TrailerColumns.class) public static final String TRAILERS = "trailers";
}

package app.com.example.mina.themovieguide.db;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Mina on 28-Dec-15.
 */
public interface MovieColumns {

    /*
    *  private String title;
    private String imagePath;
    private String overView;
    private double rating;
    private String date;
    private String id ;
    * */

    @DataType(DataType.Type.TEXT) @PrimaryKey
    public static final String MovieID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String MovieTitle = "MovieTitle";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String MovieImagePath = "MovieImagePath";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String MovieOverview = "MovieOverview";

    @DataType(DataType.Type.REAL)
    public static final String MovieRating = "MovieRating";

    @DataType(DataType.Type.TEXT)
    public static final String MovieDate = "MovieDate";

}

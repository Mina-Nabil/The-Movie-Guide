package app.com.example.mina.themovieguide.db;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Mina on 28-Dec-15.
 */
public interface TrailerColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String TrailerID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TrailerMovieID = "TrailerMovieID";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TrailerKey = "TrailerKey";

    @DataType(DataType.Type.TEXT)
    public static final String TrailerTitle = "TrailerTitle";



}

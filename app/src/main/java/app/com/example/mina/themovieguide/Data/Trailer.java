package app.com.example.mina.themovieguide.Data;

/**
 * Created by Mina on 26-Dec-15.
 */
public class Trailer {
        String Name ;
        String key ;

    public Trailer(String name, String key) {
        Name = name;
        this.key = key;
    }

    public String getName() {
        return Name;
    }

    public String getKey() {
        return key;
    }
}

import java.text.SimpleDateFormat;

public class Main {
    // private static final SimpleDateFormat sdf = new
    // SimpleDateFormat("yyyy/MM/dd-hh:mm:ssa");clear
    // name of item to database PK. purpose is for searching things in the db by
    // keywords
    // private static HashMap<String, UUID> keyRelation = new HashMap<String,
    // UUID>();
    // main database that holds all the objects with the shoppong item info
    // private static HashMap<UUID, Item> db = new HashMap<>();

    // private static boolean updateCache;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ssa");
    public static void main(String[] args) {

        App app = new App(args[0], args[1], args[2]);

        try {
            app.init();
           // app.dbToString();
            app.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    

    }
}
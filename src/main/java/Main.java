import spark.Spark;

/**
 * The main class.
 * Created by anoop on 2/4/16.
 */
public class Main {

    public static void main(String[] args) {

        // respond to root request
        Spark.get("/", ((request, response) -> "Hello, EE 461L!"));
    }
}

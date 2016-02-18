import spark.Spark;

/**
 * The main class.
 * Created by anoop on 2/4/16.
 */
public class Main {

    public static void main(String[] args) {

        Spark.port(Main.getHerokuAssignedPort());

        // respond to root request
        Spark.get("/", ((request, response) -> "Hello, EE 461L!"));
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}

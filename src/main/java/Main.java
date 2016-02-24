import spark.Spark;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
/**
 * The main class.
 * Created by anoop on 2/4/16.
 */
public class Main {

    public static void main(String[] args) {

        Spark.port(Main.getHerokuAssignedPort());

        // respond to root request
        try{
            Connection connection = getConnection();
            Spark.get("/", (((request, response) -> connection.toString())));
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
            stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
            String str = "Any new timestamps?\n";
            while (rs.next()) {
                str = str + rs.getTimestamp("tick") + "\n";
            }
            System.out.println(str);
            closeConnection(connection);
            Spark.get("/table", (request, response) -> "How do I put str variable in here?");
        }catch (java.net.URISyntaxException e){
            System.out.println(e);
            Spark.get("/", (((request, response) -> e.toString())));
            e.printStackTrace();
        }catch (java.sql.SQLException e){
            System.out.println(e);
            Spark.get("/", (((request, response) -> e.toString())));
            e.printStackTrace();
        }

    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        /*

        */
        try {
            Class.forName("org.postgresql.Driver");

            System.out.println("Initialized Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        System.out.println(dbUrl);
        System.out.println(username);
        System.out.println(password);
        Spark.get("/creds", (request, response) -> dbUrl + "\n"
            + username + "\n" + password + "\n");
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void closeConnection(Connection connArg) {
        System.out.println("Releasing all open resources ...");
        try {
            if (connArg != null) {
                connArg.close();
                connArg = null;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to Close");
        }
    }
}

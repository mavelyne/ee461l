import com.heroku.sdk.jdbc.DatabaseUrl;
import j2html.TagCreator;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class.
 * Created by anoop on 2/4/16.
 */
public class Main {

    public static void main(String[] args) {

        Spark.port(Main.getHerokuAssignedPort());
        Spark.staticFileLocation("public");

        // respond to root request
//        try{
//            Connection connection = getConnection();
//            Spark.get("/", (((request, response) -> connection.toString())));
//            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
//            stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
//            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//            String str = "Any new timestamps?\n";
//            while (rs.next()) {
//                str = str + rs.getTimestamp("tick") + "\n";
//            }
//            System.out.println(str);
//            closeConnection(connection);
//            Spark.get("/table", (request, response) -> "How do I put str variable in here?");
//        }catch (java.net.URISyntaxException e){
//            System.out.println(e);
//            Spark.get("/", (((request, response) -> e.toString())));
//            e.printStackTrace();
//        }catch (java.sql.SQLException e){
//            System.out.println(e);
//            Spark.get("/", (((request, response) -> e.toString())));
//            e.printStackTrace();
//        }

        Spark.get("/", ((request, response) -> {
            Connection connection = DatabaseUrl.extract().getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            ResultSet rs = statement.executeQuery("SELECT tick FROM ticks");

            ArrayList<String> ticks = new ArrayList<>();
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("tick");
                ticks.add(timestamp.toString());
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World");
            attributes.put("ticks", ticks);

            return new ModelAndView(attributes, "index.ftl");
        }), new FreeMarkerEngine());

        Spark.get("/deleteall", ((request, response) -> {
            Connection connection = DatabaseUrl.extract().getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE ticks");

            response.redirect("/");
            return "";
        }));
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
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

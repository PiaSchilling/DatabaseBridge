package de.hdm_stuttgart.mi;


import de.hdm_stuttgart.mi.connect.ConnectionDetails;
import de.hdm_stuttgart.mi.connect.DatabaseSystem;
import de.hdm_stuttgart.mi.connect.SourceConnectionHandler;

import java.sql.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException {
        // Usage example, schema movies must exist!
        SourceConnectionHandler.getInstance().connectDatabase(new ConnectionDetails(DatabaseSystem.MYSQL, "localhost", 3306, "movies", "root", "example"));
        Connection conn = SourceConnectionHandler.getInstance().connection;
        Statement stmt = conn.createStatement();
        //Table movie needs to exist
        ResultSet result = stmt.executeQuery("SELECT * FROM movie");

        while(result.next()) {
            System.out.println(result.getString("movie_name"));
        }

        stmt.close();
        conn.close();

    }
}
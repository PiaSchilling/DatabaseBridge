package de.hdm_stuttgart.mi.read.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.connect.model.DatabaseSystem;
import de.hdm_stuttgart.mi.read.api.UsersReader;
import de.hdm_stuttgart.mi.read.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersReaderImpl implements UsersReader {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionHandler sourceConnection;
    private final DatabaseSystem sourceDatabaseSystem;

    @Inject
    public UsersReaderImpl(@Named("sourceConnection") ConnectionHandler sourceConnection) {
        this.sourceConnection = sourceConnection;
        this.sourceDatabaseSystem = sourceConnection.getConnectionDetails().getDatabaseSystem();
    }

    @Override
    public ArrayList<User> readUsers() {
        final ArrayList<User> users = new ArrayList<>();
        try(Statement statement = sourceConnection.getConnection().createStatement()){
            final ResultSet usersResult = statement.executeQuery(buildSelectUserTableQuery());
            while (usersResult.next()){
                final String userName = usersResult.getString(sourceDatabaseSystem.userNameColumnName);
                users.add(new User(userName));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading users: "+ e.getMessage());
            return users;
        }
        return users;
    }

    /**
     * Builds a database system specific query to select all users from the users-table
     * @return a SELECT query based on the database system
     */
    private String buildSelectUserTableQuery() {
        String base = "SELECT * FROM ";
        return switch (sourceDatabaseSystem) {
            case POSTGRES -> base + DatabaseSystem.POSTGRES.userTableName;
            case MYSQL -> base + DatabaseSystem.MYSQL.userTableName;
            case MARIADB -> base + DatabaseSystem.MARIADB.userTableName;
        };
    }

}

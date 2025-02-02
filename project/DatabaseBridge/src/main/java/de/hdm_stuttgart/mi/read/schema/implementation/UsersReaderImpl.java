package de.hdm_stuttgart.mi.read.schema.implementation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.hdm_stuttgart.mi.connect.api.ConnectionHandler;
import de.hdm_stuttgart.mi.read.schema.api.UsersReader;
import de.hdm_stuttgart.mi.read.schema.model.User;
import de.hdm_stuttgart.mi.util.consts.DestinationConsts;
import de.hdm_stuttgart.mi.util.consts.SourceConsts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersReaderImpl implements UsersReader {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final ConnectionHandler sourceConnection;

    @Inject
    public UsersReaderImpl(@Named("sourceConnection") ConnectionHandler sourceConnection) {
        this.sourceConnection = sourceConnection;
    }

    @Override
    public ArrayList<User> readUsers() {
        final ArrayList<User> users = new ArrayList<>();
        final User currentLoggedInUser = new User(sourceConnection.getConnectionDetails().getUsername(),
                sourceConnection.getConnectionDetails().getPassword(), true);
        try (Statement statement = sourceConnection.getConnection().createStatement()) {
            final ResultSet usersResult = statement.executeQuery(buildSelectUserTableQuery());
            while (usersResult.next()) {
                final String userName = usersResult.getString(SourceConsts.userNameColName);
                if (userName.equals(currentLoggedInUser.username())) {
                    // add current logged-in user with password since password is available for this user
                    users.add(currentLoggedInUser);
                } else if (!SourceConsts.systemUserNames.contains(userName)) {
                    // do not add system users like mysql.sys, postgres, ...
                    // add all other users with default password since passwords are not available
                    users.add(new User(userName, DestinationConsts.tempPassword, false));
                }
            }

        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException while reading users: " + e.getMessage());
            return users;
        }
        return users;
    }

    /**
     * Builds a database system specific query to select all users from the users-table
     *
     * @return a SELECT query based on the database system
     */
    private String buildSelectUserTableQuery() {
        return "SELECT DISTINCT * FROM " + SourceConsts.userTableName;
    }

}

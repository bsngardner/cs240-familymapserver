package edu.byu.broderick.fmserver.main.database;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Class containing database helper code to be accessed by DAOs when issuing commands to the database
 *
 * @author Broderick Gardner
 */
public class Database {

    private final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "        main.users (" +
            "          username TEXT PRIMARY KEY NOT NULL UNIQUE, " +
            "          password TEXT NOT NULL, " +
            "          email TEXT NOT NULL, " +
            "          firstname TEXT NOT NULL," +
            "          lastname TEXT NOT NULL, " +
            "          gender TEXT NOT NULL," +
            "          personid TEXT NOT NULL, " +
            "          info TEXT NOT NULL" +
            "                   );";
    private final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS main.users";

    private final String CREATE_AUTHKEYS_TABLE = "CREATE TABLE IF NOT EXISTS \n" +
            "        main.authKeys (\n" +
            "          key TEXT PRIMARY KEY NOT NULL UNIQUE,\n" +
            "          userid TEXT NOT NULL,\n" +
            "          time TEXT NOT NULL\n" +
            "                   );";
    private final String DROP_AUTHKEYS_TABLE = "DROP TABLE IF EXISTS main.authkeys";

    private final String CREATE_PERSONS_TABLE = "CREATE TABLE IF NOT EXISTS \n" +
            "        main.persons (\n" +
            "          personid varchar(20),\n" +
            "          userid TEXT NOT NULL,\n" +
            "          firstname TEXT NOT NULL,\n" +
            "          lastname TEXT NOT NULL,\n" +
            "          gender TEXT NOT NULL,\n" +
            "          father TEXT,\n" +
            "          mother TEXT,\n" +
            "          spouse TEXT\n" +
            "                      );";
    private final String DROP_PERSONS_TABLE = "DROP TABLE IF EXISTS main.persons";

    private final String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS \n" +
            "        main.events (\n" +
            "          eventid varchar(20),\n" +
            "          userid TEXT NOT NULL,\n" +
            "          personid TEXT NOT NULL,\n" +
            "          latitude REAL NOT NULL,\n" +
            "          longitude REAL NOT NULL,\n" +
            "          country TEXT NOT NULL,\n" +
            "          city TEXT NOT NULL,\n" +
            "          eventType TEXT NOT NULL,\n" +
            "          year TEXT NOT NULL\n" +
            "                   );";
    private final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS main.events";

    /**
     * Singleton object of Database
     */
    private static Database db = new Database();

    public Connection conn;

    public UserDAO userData = new UserDAO(this);
    public EventDAO eventData = new EventDAO(this);
    public PersonDAO personData = new PersonDAO(this);

    private Random rand = new Random();

    private String sql_cmd;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    /**
     * Constructor
     */
    public Database() {
        loadDriver();
    }

    /**
     * Loads SQL driver
     * Call before starting any SQL transactions
     */
    private void loadDriver() {
        final String driver = "org.sqlite.JDBC";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("SQL driver class not found");
        }
    }

    /**
     * Called by all database transaction methods, begins database transaction
     */
    private void startTransaction() {
        openDatabase();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.print("Error disabling auto commit");
        }

    }

    /**
     * Called by all database transaction methods, ends database transaction. Call with parameter false to revert
     * update, usually in case of error.
     *
     * @param commit
     */
    private void endTransaction(boolean commit) {
        try {
            if (conn == null)
                throw new Exception("SQL: endTransaction called on null connection");

            if (commit)
                conn.commit();
            else
                conn.rollback();

        } catch (SQLException e) {
            System.out.println("SQL: commit failed on endTransaction");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeSQLStatement();
        }

        conn = null;

    }

    /**
     * Call select on database
     *
     * @param table
     * @param columnList
     * @param column
     * @param value
     * @return
     */
    public List<List<Object>> select(String table, List<String> columnList, String column, String value) {

        startTransaction();

        String columns = "*";
        if (columnList != null)
            columns = String.join(",", columnList);
        sql_cmd = "SELECT " + columns + " FROM " + table;
        if (column != null) {
            sql_cmd += " WHERE " + column + " = ?";
        }
        List<List<Object>> rows = new ArrayList<>();

        try {
            stmt = this.conn.prepareStatement(sql_cmd);
            if (column != null)
                stmt.setString(1, value);
            rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();

            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }

            endTransaction(true);
        } catch (SQLException e) {
            System.out.println("SQL error: select");
            e.printStackTrace();
            endTransaction(false);
        } finally {
            closeSQLStatement();
        }

        return rows;
    }

    /**
     * Update database entry
     *
     * @param table
     * @param columns
     * @param entries
     * @param column
     * @param value
     * @return
     */
    public boolean update(String table, List<String> columns, List<Object> entries, String column, String value) {

        startTransaction();

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + table + " SET ");
        StringJoiner sj = new StringJoiner(", ");

        for (String col : columns) {
            sj.add(col + " = " + "?");
        }
        sb.append(sj.toString());

        if (column != null)
            sb.append(" WHERE " + column + " = ? ");
        sql_cmd = sb.toString();

        boolean success = false;

        try {
            stmt = conn.prepareStatement(sql_cmd);
            for (int i = 1; i <= columns.size(); i++) {
                stmt.setObject(i, entries.get(i - 1));
            }
            if (column != null)
                stmt.setObject(columns.size() + 1, value);

            stmt.executeUpdate();
            success = true;

            endTransaction(true);
        } catch (SQLException e) {
            System.out.println("SQL error: update");
            e.printStackTrace();
            endTransaction(false);
        } finally {
            closeSQLStatement();
        }

        return success;
    }

    /**
     * Delete database entry
     *
     * @param table
     * @param attribute
     * @param value
     * @return
     */
    public boolean delete(String table, String attribute, String value) {

        startTransaction();

        sql_cmd = "DELETE FROM " + table;
        if (attribute != null)
            sql_cmd += " WHERE " + attribute + " = " + "?";
        boolean success = false;

        try {
            stmt = conn.prepareStatement(sql_cmd);
            if (attribute != null)
                stmt.setString(1, value);
            stmt.executeUpdate();
            success = true;

            endTransaction(true);
        } catch (SQLException e) {
            System.out.println("SQL error: delete");
            e.printStackTrace();
            endTransaction(false);
        } finally {
            closeSQLStatement();
        }

        return success;
    }

    /**
     * Insert entry into database
     *
     * @param table
     * @param columns
     * @param entries
     * @return
     */
    public boolean insert(String table, List<String> columns, List<Object> entries) {

        startTransaction();

        StringBuilder sb = new StringBuilder("INSERT INTO " + table);
        sb.append(" (" + String.join(", ", columns) + ")");
        sb.append(" VALUES (");
        StringJoiner j = new StringJoiner(", ");
        for (int i = entries.size(); i-- > 0; )
            j.add("?");
        sb.append(j.toString());
        sb.append(")");
        sql_cmd = sb.toString();
        boolean success = false;

        try {
            stmt = conn.prepareStatement(sql_cmd);
            for (int i = 1; i <= entries.size(); i++) {
                stmt.setObject(i, entries.get(i - 1));
            }
            stmt.executeUpdate();
            success = true;

            endTransaction(true);
        } catch (SQLException e) {
            System.out.println("SQL error: insert");
            e.printStackTrace();
            endTransaction(false);
        } finally {
            closeSQLStatement();
        }

        return success;
    }

    /**
     * Private helper method for closing sql statements
     */
    private void closeSQLStatement() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException e) {
            System.out.println("SQL error: failed to close SQL objects");
            e.printStackTrace();
        }
    }


    /**
     * Open connection to database
     * Called by startTransaction
     */
    public void openDatabase() {
        final String dbName = "database.sqlite";
        final String dbPath = "db" + File.separator + dbName;
        final String connectionPath = "jdbc:sqlite:" + dbPath;

        File dir = new File("db");
        conn = null;

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            conn = DriverManager.getConnection(connectionPath);
            createDatabaseINE();
        } catch (SQLException e) {
            System.out.println("SQL database access error: " + e.getMessage());
        }
    }

    /**
     * Create database tables if they does not exist.
     * Called by openDatabase.
     *
     * @throws SQLException
     */
    public void createDatabaseINE() throws SQLException {

        PreparedStatement stmt_users = conn.prepareStatement(CREATE_USERS_TABLE);
        stmt_users.executeUpdate();
        PreparedStatement stmt_authkeys = conn.prepareStatement(CREATE_AUTHKEYS_TABLE);
        stmt_authkeys.executeUpdate();
        PreparedStatement stmt_persons = conn.prepareStatement(CREATE_PERSONS_TABLE);
        stmt_persons.executeUpdate();
        PreparedStatement stmt_events = conn.prepareStatement(CREATE_EVENTS_TABLE);
        stmt_events.executeUpdate();
    }

    /**
     * Reset database by dropping all tables and recreating them
     *
     * @throws SQLException
     */
    public void resetDatabase() throws SQLException {

        startTransaction();

        PreparedStatement drop_users = conn.prepareStatement(DROP_USERS_TABLE);
        drop_users.executeUpdate();

        PreparedStatement drop_authkeys = conn.prepareStatement(DROP_AUTHKEYS_TABLE);
        drop_authkeys.executeUpdate();

        PreparedStatement drop_persons = conn.prepareStatement(DROP_PERSONS_TABLE);
        drop_persons.executeUpdate();

        PreparedStatement drop_events = conn.prepareStatement(DROP_EVENTS_TABLE);
        drop_events.executeUpdate();

        createDatabaseINE();
        endTransaction(true);
    }


    /**
     * Getter for singleton object
     *
     * @return
     */
    public static Database getDB() {
        return db;
    }


    /**
     * Generate random alphanumeric id, uses for event and person id's
     * @return
     */
    public String generateID() {
        final char letters[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        final int UID_LEN = 16;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < UID_LEN; i++) {
            b.append(letters[rand.nextInt(letters.length)]);
        }
        return b.toString();
    }

}

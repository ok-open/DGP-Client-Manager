import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:dgp.db";

    static {
        try {

            Class.forName("org.sqlite.JDBC");

            // Initialize database
            initDatabase();

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(
                    "SQLite JDBC Driver not found in classpath",
                    e
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to initialize database",
                    e
            );
        }
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }

    private static void initDatabase()
            throws SQLException {

        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement()
        ) {

            // -------------------------------------------------
            // FOREIGN KEYS
            // -------------------------------------------------

            stmt.execute("PRAGMA foreign_keys = ON");


            // -------------------------------------------------
            // CLIENTS TABLE
            // -------------------------------------------------

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clients (

                    client_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    name           TEXT NOT NULL,
                    contact_person TEXT,
                    phone          TEXT,
                    email          TEXT,
                    address        TEXT,
                    notes          TEXT,
                    status         TEXT DEFAULT 'ACTIVE'

                );
            """);


            // -------------------------------------------------
            // CLIENT DATABASE MIGRATION
            // -------------------------------------------------

            // Older databases may not have the status column.

            if (!columnExists(
                    conn,
                    "clients",
                    "status"
            )) {

                stmt.execute("""
                    ALTER TABLE clients
                    ADD COLUMN status TEXT DEFAULT 'ACTIVE'
                """);

                System.out.println(
                        "Database upgraded: added clients.status column."
                );
            }


            // -------------------------------------------------
            // PROJECTS TABLE
            // -------------------------------------------------

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projects (

                    project_id     INTEGER PRIMARY KEY AUTOINCREMENT,
                    client_id      INTEGER NOT NULL,
                    project_name   TEXT,
                    location       TEXT,
                    date_started   TEXT,
                    date_completed TEXT,
                    status         TEXT,
                    total_cost     REAL,
                    notes          TEXT,
                    specs          TEXT,
                    po_number      INTEGER,
                    sales_invoice  INTEGER,
                    dr_number     INTEGER,

                    FOREIGN KEY (client_id)
                        REFERENCES clients(client_id)
                        ON DELETE CASCADE

                );
            """);


            // -------------------------------------------------
            // PROJECT DATABASE MIGRATIONS
            // -------------------------------------------------

            // Older databases may not have the specs column.

            if (!columnExists(
                    conn,
                    "projects",
                    "specs"
            )) {

                stmt.execute("""
                    ALTER TABLE projects
                    ADD COLUMN specs TEXT
                """);

                System.out.println(
                        "Database upgraded: added projects.specs column."
                );
            }
        }
    }


    // -------------------------------------------------
    // CHECK IF COLUMN EXISTS
    // -------------------------------------------------

    private static boolean columnExists(
            Connection conn,
            String tableName,
            String columnName
    ) throws SQLException {

        String sql =
                "PRAGMA table_info(" + tableName + ")";

        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                String existingColumn =
                        rs.getString("name");

                if (columnName.equalsIgnoreCase(
                        existingColumn
                )) {

                    return true;
                }
            }
        }

        return false;
    }
}
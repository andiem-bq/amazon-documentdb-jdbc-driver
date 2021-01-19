package software.amazon.documentdb;

import com.mongodb.ReadPreference;
import org.bson.UuidRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

public class DocumentDBConnectionTest extends DocumentDBTest {

    private static final String HOSTNAME = "localhost";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DATABASE = "database";
    private static final DocumentDBConnectionProperties VALID_CONNECTION_PROPERTIES = new DocumentDBConnectionProperties();

    /** Initializes the test class. */
    @BeforeAll
    public static void initialize() {
        VALID_CONNECTION_PROPERTIES.setUser(USERNAME);
        VALID_CONNECTION_PROPERTIES.setPassword(PASSWORD);
        VALID_CONNECTION_PROPERTIES.setHostname(HOSTNAME);
        VALID_CONNECTION_PROPERTIES.setDatabase(DATABASE);

        // Add 1 valid user so we can successfully authenticate.
        addUser(DATABASE, USERNAME, PASSWORD);
    }

    /**
     * Tests isValid() when connected to a local MongoDB instance.
     *
     * @throws SQLException if an error occurs instantiating a Connection.
     */
    @Test
    void testIsValidWhenConnectionIsValid() throws SQLException {
        final DocumentDbConnection connection = new DocumentDbConnection(VALID_CONNECTION_PROPERTIES);
        // NOTE: Observed approximate 10 .. 11 seconds delay before first heartbeat is returned.
        final int timeoutSeconds = 15;
        Assertions.assertTrue(connection.isValid(timeoutSeconds));
    }

    /**
     * Tests isValid() when connected to a local MongoDB instance but timeout is negative.
     *
     * @throws SQLException if an error occurs instantiating a Connection.
     */
    @Test
    void testIsValidWhenTimeoutIsNegative() throws SQLException {
        final DocumentDbConnection connection = new DocumentDbConnection((VALID_CONNECTION_PROPERTIES));
        Assertions.assertThrows(SQLException.class, () -> connection.isValid(-1));
    }

    /**
     * Tests close() when connected to a local mongoDB instance and Connection is not yet closed.
     *
     * @throws SQLException if an error occurs instantiating a Connection.
     */
    @Test
    void testClose() throws SQLException {
        final DocumentDbConnection connection = new DocumentDbConnection(VALID_CONNECTION_PROPERTIES);
        Assertions.assertFalse(connection.isClosed());
        connection.close();
        Assertions.assertTrue(connection.isClosed());
    }

    /**
     * Tests constructor when passed valid options.
     *
     * @throws SQLException if an error occurs instantiating a Connection.
     */
    @Test
    void testConnectionWithValidOptions() throws SQLException {
        final DocumentDBConnectionProperties properties = new DocumentDBConnectionProperties(VALID_CONNECTION_PROPERTIES);
        properties.setApplicationName("test");
        properties.setServerSelectionTimeout("10");
        properties.setLocalThreshold("10");
        properties.setHeartbeatFrequency("10");
        properties.setConnectTimeout("10");
        properties.setSocketTimeout("10");
        properties.setMaxPoolSize("10");
        properties.setMinPoolSize("10");
        properties.setWaitQueueTimeout("10");
        properties.setMaxIdleTime("10");
        properties.setMaxLifeTime("10");
        properties.setRetryReadsEnabled("false");
        properties.setReadPreference(ReadPreference.primary().getName());
        properties.setUUIDRepresentation(UuidRepresentation.JAVA_LEGACY.toString());

        final DocumentDbConnection connection = new DocumentDbConnection(properties);
        Assertions.assertNotNull(connection);
        Assertions.assertEquals(DATABASE, connection.getMongoDatabase().getName());
    }

    /**
     * Tests constructor when passed invalid options. Invalid options are ignored.
     *
     * @throws SQLException if an error occurs instantiating a Connection.
     */
    @Test
    void testConnectionWithInvalidOptions() throws SQLException {
        final DocumentDBConnectionProperties properties = new DocumentDBConnectionProperties(VALID_CONNECTION_PROPERTIES);
        properties.setReadPreference("invalidReadPreference");
        properties.setTlsEnabled("invalidBoolean");
        properties.setServerSelectionTimeout("invalidNumber");
        properties.setUUIDRepresentation("invalidUUIDRepresentation");

        final DocumentDbConnection connection = new DocumentDbConnection(properties);
        Assertions.assertNotNull(connection);
        Assertions.assertEquals(DATABASE, connection.getMongoDatabase().getName());
    }

    /** Tests constructor when passed an invalid database name. */
    @Test
    void testConnectionWithInvalidDatabase() {
        final DocumentDBConnectionProperties properties = new DocumentDBConnectionProperties(VALID_CONNECTION_PROPERTIES);
        properties.setDatabase(" ");

        Assertions.assertThrows(SQLException.class, () -> new DocumentDbConnection(properties));
    }

    /** Tests constructor when passed invalid credentials. */
    @Test
    void testConnectionWithInvalidCredentials() {
        final DocumentDBConnectionProperties properties = new DocumentDBConnectionProperties(VALID_CONNECTION_PROPERTIES);
        properties.setUser("invalidUser");

        Assertions.assertThrows(SQLException.class, () -> new DocumentDbConnection(properties));
    }
}

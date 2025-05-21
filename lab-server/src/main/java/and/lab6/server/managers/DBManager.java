package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;

public class DBManager {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    public DBManager(String url, String user, String password) {
        this.password = password;
        this.url = url;
        this.user = user;
    }

    public void start() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при подключении драйвера");
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
            createTable();
        } catch (SQLException e) {
            logger.error("Ошибка при создании подключения");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() throws SQLException {
        if(connection == null) {System.exit(0);}
        return connection.createStatement();
    }

    public PreparedStatement getPreparedStatement(String s) throws SQLException {
        if(connection == null) {System.exit(0);}
        return connection.prepareStatement(s);
    }

    public PreparedStatement getPreparedStatementWithKeys(String s) throws SQLException {
        if(connection == null) {System.exit(0);}
        return connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
    }

    public void createTable() throws SQLException {
        var scriptFileName = "drop_enums.sql";
        try {
            Statement stmt = getStatement();
            connection.setAutoCommit(false);
            if (!Files.exists(Paths.get(scriptFileName))) {
                logger.error(new IllegalArgumentException("Файл не существует: " + scriptFileName));
                return;
            }
            String sql = Files.readString(Paths.get(scriptFileName));
            String[] commands = sql.split(";\\s*");
            for (String command : commands) {
                try {
                    String tcom = command.trim();
                    if (!tcom.isEmpty()) {
                        //System.out.println(tcom);
                        stmt.execute(tcom);
                    }
                } catch (SQLException e) {
                    logger.error(e);
                    System.out.println("asdr");
                }
            }
            connection.commit();

            scriptFileName = "element_create.sql";

            stmt = getStatement();
            connection.setAutoCommit(false);
            if (!Files.exists(Paths.get(scriptFileName))) {
                logger.error(new IllegalArgumentException("Файл не существует: " + scriptFileName));
                return;
            }
            sql = Files.readString(Paths.get(scriptFileName));
            commands = sql.split(";\\s*");
            for (String command : commands) {

                String tcom = command.trim();
                if (!tcom.isEmpty()) {
                    System.out.println(tcom);
                    stmt.execute(tcom);
                }

            }
            connection.commit();
        } catch (SQLException | IOException e) {
            logger.error("Ошибка при выполнении транзакции");
            throw new RuntimeException(e);
        }
    }
    public  byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[48];
        random.nextBytes(salt);
       // System.out.println(Arrays.toString(salt));
        return salt;
    }
    public  String getSecurePassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt);
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();
            String encodedHash = Base64.getEncoder().encodeToString(hash);
           // System.out.println("Хэш-значение пароля: " + encodedHash);
            return encodedHash;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error(e);
        }
        return null;
    }

}

package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Action;
import util.Response;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

public class DBUserManager {
    private DBManager dbManager;
    private static final Logger logger = LogManager.getLogger(DBUserManager.class);

    public DBUserManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Response autUser(String login, String password) {
        try {
            String query = "Select * from users where login = ?;";
            PreparedStatement preparedStatement = null;
            preparedStatement = dbManager.getPreparedStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                String  salt = resultSet.getString("salt");
                byte[] saltBase64 = Base64.getDecoder().decode(salt);
                var hashPassword = dbManager.getSecurePassword(password, saltBase64);

                if (resultSet.getString("password_digest").equals(hashPassword)) {
                    return new Response("Авторизация прошла успешно", null, 200, Action.AUTHORIZATION);
                } else {
                    return new Response("неправильный пароль", null, 404, Action.FAIL);
                }
            } else {
                return new Response("Пользователя с таким логином не существует", null, 404, Action.FAIL);
            }
        } catch (SQLException e) {
            logger.error(1 + " " + e);
            return new Response("не получилось авторизоваться", null, 404, Action.FAIL);
        }
    }


    public Response regUser(String login, String password) {
        try {
            String query = "Select * from users where login = ?";
            PreparedStatement preparedStatement = null;
            preparedStatement = dbManager.getPreparedStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Response("пользователь с таким логином уже существует", null, 404, Action.FAIL);
            }
            query = "INSERT INTO users(" +
                    "login, password_digest,salt)" +
                    "VALUES (?, ?, ?)";
            preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setString(1, login);

            var salt= dbManager.getSalt();
            var hashPassword = dbManager.getSecurePassword(password, salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);

            preparedStatement.setString(2, hashPassword);
            preparedStatement.setString(3, saltBase64);
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.error("one fault and you fail");
                return new Response("не получилось зарегистрировать", null, 404, Action.FAIL);
            } else {
                return new Response("Успешно зарегистриован новый пользоователь", null, 200, Action.REGISTRATION);
            }
        } catch (SQLException e) {
            logger.error(2 + " " + e);
            return new Response("не получилось зарегистрировать", null, 404, Action.FAIL);
        }
    }

    public long verifyUser(String login, String password) {
        System.out.println("==="+login+" "+password);
        try {
            String query = "Select * from users where login = ? ;";
            PreparedStatement preparedStatement = null;
            preparedStatement = dbManager.getPreparedStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
               // System.out.println(resultSet.getString("login")+"="+resultSet.getString("password_digest"));

                String  salt = resultSet.getString("salt");
                byte[] saltBase64 = Base64.getDecoder().decode(salt);
                var hashPassword = dbManager.getSecurePassword(password, saltBase64);
                if(resultSet.getString("login").equals(login) && resultSet.getString("password_digest").equals(hashPassword)){
                    return resultSet.getLong("id");
                }
            }
            return -1;
            //else return -1;
        } catch (SQLException e) {
            logger.error(3 + " " + e);
            return -1;
        }
    }
}

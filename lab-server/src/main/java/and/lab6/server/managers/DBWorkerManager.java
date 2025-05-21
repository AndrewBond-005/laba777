package and.lab6.server.managers;

import models.Coordinates;
import models.Person;
import models.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBWorkerManager {
    private final DBManager dbManager;
    private static final Logger logger = LogManager.getLogger(DBWorkerManager.class);

    public DBWorkerManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean insert(Worker worker, Long creatorId) {
        System.out.println(creatorId);
        if (creatorId == -1) {
            return false;
        }
        try {
            String query = "INSERT INTO workers(" +
                    "name, coordinates,creation_date, salary,end_date,position,status,person,creator_id)" +
                    "VALUES (?, ?, ?, ?,?,?::worker_position,?::worker_status,?,?)";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setString(1, worker.getName());
            preparedStatement.setLong(2, insert(worker.getCoordinates()));
            preparedStatement.setObject(3, worker.getCreationDate());
            preparedStatement.setInt(4, worker.getSalary());
            preparedStatement.setObject(5, worker.getEndDate());
            preparedStatement.setString(6, worker.getPosition().toString());
            preparedStatement.setString(7, worker.getStatus().toString());
            preparedStatement.setLong(8, insert(worker.getPerson()));
            preparedStatement.setLong(9, creatorId);
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.warn("не удалось добавить Worker, ноль строчек изменено");
            }
            try (ResultSet gk = preparedStatement.getGeneratedKeys()) {
                if (gk.next()) {
                    worker.setId(gk.getInt(1));
                } else {
                    logger.error("не удалось добавить Worker, нет свободных id");
                }
            }
        } catch (SQLException e) {
            logger.error(4 + " " + e);
            try {
                dbManager.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Rollback Worker failed: {}", rollbackEx.getMessage());
            }
            return false;
        }
        return true;
    }

    public long insert(Person person) {
        try {
            String query = "Select * from persons where " +
                    "weight = ? and eye_color = ?::eye_color and hair_color=?::hair_color and nationality=?::country";
            PreparedStatement preparedStatement = null;
            preparedStatement = dbManager.getPreparedStatement(query);
            preparedStatement.setDouble(1, person.getWeight());
            preparedStatement.setString(2, person.getEyeColor().name());
            preparedStatement.setString(3, person.getHairColor().name());
            preparedStatement.setString(4, person.getNationality().name());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
                return resultSet.getLong(1);
            }
            query = "INSERT INTO persons(weight, eye_color,hair_color, nationality)" +
                    "VALUES (?, ?::eye_color, ?::hair_color, ?::country)";
            preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setDouble(1, person.getWeight());
            preparedStatement.setString(2, person.getEyeColor().name());
            preparedStatement.setString(3, person.getHairColor().name());
            preparedStatement.setString(4, person.getNationality().name());
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.warn("не удалось добавить Person, ноль строчек изменено");
            }
            try (ResultSet gk = preparedStatement.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    logger.error("не удалось добавить Person, нет свободных id");
                }
            }
        } catch (SQLException e) {
            logger.error(5 + " " + e);
            try {
                dbManager.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Rollback Person failed: {}", rollbackEx.getMessage());
            }
            return 0;
        }

        return -1;
    }

    public long insert(Coordinates coordinates) {
        try {

            String query = "Select * from coordinates where x = ? and y = ?";
            PreparedStatement preparedStatement = null;
            preparedStatement = dbManager.getPreparedStatement(query);
            preparedStatement.setFloat(1, coordinates.getX());
            preparedStatement.setLong(2, coordinates.getY());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //System.out.println( resultSet.getString(1) + " " + resultSet.getString(2));
                return resultSet.getLong(1);
            }
            query = "INSERT INTO coordinates(x, y)VALUES (?, ?)";
            preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setFloat(1, coordinates.getX());
            preparedStatement.setLong(2, coordinates.getY());
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.warn("не удалось добавить Coordinates, ноль строчек изменено");
            }
            try (ResultSet gk = preparedStatement.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    logger.error("не удалось добавить Coordinates, нет свободных id");
                }
            }
        } catch (SQLException e) {
            logger.error(6 + " " + e);
            try {
                dbManager.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Rollback Coordianates failed: {}", rollbackEx.getMessage());
            }
            return 0;
        }
        return -1;
    }

    public boolean update(Worker worker, long creatorID) {
        if (creatorID == -1) return false;
        try {

            String query = "Select * from workers where id = ?";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setLong(1, worker.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                logger.warn("нет worker с таким id");
                return false;
            }

            query = "UPDATE workers SET " +
                    "name=?, coordinates=?, salary=?,end_date=?,position=?::worker_position,status=?::worker_status,person=? " +
                    "WHERE id = ? and creator_id=?";
            preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setString(1, worker.getName());
            preparedStatement.setLong(2, insert(worker.getCoordinates()));
            preparedStatement.setInt(3, worker.getSalary());
            preparedStatement.setObject(4, worker.getEndDate());
            preparedStatement.setString(5, worker.getPosition().toString());
            preparedStatement.setString(6, worker.getStatus().toString());
            preparedStatement.setLong(7, insert(worker.getPerson()));
            preparedStatement.setLong(8, worker.getId());
            preparedStatement.setLong(9, creatorID);
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.error(new SQLException("не удалось обновить Worker, ноль строчек изменено"));
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.error(7 + " " + e);
            try {
                dbManager.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Rollback W failed: {}", rollbackEx.getMessage());
            }
            return false;
        }
    }

    public List<Worker> load() {
        try {
            String query = "Select *\n" +
                    "from workers w\n" +
                    "join coordinates c on c.id =w.coordinates\n" +
                    "join persons p on w.person = p.id";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Worker> workers = new ArrayList<Worker>();
            if (!resultSet.next()) {
                return null;
            }
            do {
                String id = resultSet.getString(1);
                String name = resultSet.getString("name");
                String x = resultSet.getString("x");
                String y = resultSet.getString("y");
                String creationDate = resultSet.getString("creation_date");
                String salary = resultSet.getString("salary");
                String endDate = resultSet.getString("end_date");
                String position = resultSet.getString("position");
                String status = resultSet.getString("status");
                String weight = resultSet.getString("weight");
                String eyeColor = resultSet.getString("eye_color");
                String hairColor = resultSet.getString("hair_color");
                String nationality = resultSet.getString("nationality");
                Person person = new Person(weight + ';' + eyeColor + ';' + hairColor + ';' + nationality);
                String[] str = new String[]{id, name, x + ";" + y, creationDate,
                        salary, endDate, position, status, person.toString()};
                workers.add(Worker.fromArray(str));
                //System.out.println(position+" "+creationDate+" "+status+" "+Worker.fromArray(str));

            } while (resultSet.next());
            return workers;
        } catch (SQLException e) {
            logger.error(6 + " " + e);
            return null;
        }
    }


    public int maxID() {
        try {
            String query = "Select max(id) from workers";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("max");
            } else return 0;
        } catch (SQLException e) {
            logger.error(7 + " " + e);
        }
        return 0;
    }

    public List<Integer> removeAll(long creatorID) {
        try {
            String query = "Select id from workers where creator_id = ?";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setLong(1, creatorID);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            return list;
        } catch (SQLException e) {
            logger.error(8 + " " + e);
        }
        return null;
    }
    public boolean remove(long id, long creatorID) {
        try{
            String query = "Select id from workers where creator_id = ? and id= ?";
            PreparedStatement preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setLong(1, creatorID);
            preparedStatement.setLong(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                logger.warn("удаляемого Worker нет в коллекции");
                return false;
            }
            query = "Delete from workers where id =?;";
            preparedStatement = dbManager.getPreparedStatementWithKeys(query);
            preparedStatement.setDouble(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.getConnection().commit();
            if (rowsAffected == 0) {
                logger.warn("не удалось удалить Worker, ноль строчек изменено");
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.error(9 + " " + e);
        }
        return false;
    }
}
//insert Worker person coordinates
//update
//
//remove id > worker
//remove all
//show = load - взять всю коллекцию
//
//

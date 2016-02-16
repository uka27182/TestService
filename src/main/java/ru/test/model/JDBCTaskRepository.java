package ru.test.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Deprecated
public class JDBCTaskRepository implements ITaskRepository{
    final static Logger logger = LoggerFactory.getLogger(JDBCTaskRepository.class);

    @Autowired
    DataSource dataSource;

    @Override
    public Task getByUuid(String uuid) {
        logger.debug("getByUuid");
        Connection connection = null;
        try {
            Task task = null;
            connection = dataSource.getConnection();
            ResultSet rs = connection.prepareStatement("select uuid, action from task").executeQuery();
            while (rs.next()) {
                task = new Task(rs.getString("uuid"), rs.getString("action"), null);
            }
            return task;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeSilently(connection);
        }
    }

    @Override
    public Task persist(Task task) {
        logger.debug("persist");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into task(uuid, action, args, result) values (?, ?, ?, ?)");
            stmt.setString(1, task.getUuid());
            stmt.setString(2, task.getAction());
            stmt.setString(3, task.getArgs());
            stmt.setString(4, task.getResult());
            stmt.execute();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeSilently(connection);
        }
        return task;
    }

    @Override
    public Long getCount() {
        return null;
    }

    @Override
    public List<Task> getAllTask() {
        return null;
    }

    @Override
    public int deleteAllTask() {
        return 0;
    }

    @Override
    public void saveOrUpdate(Task task) {

    }

    private void closeSilently(Connection connection) {
        try {
            if (connection != null) connection.close();
        }
        catch (SQLException ignore) {
        }
    }
}

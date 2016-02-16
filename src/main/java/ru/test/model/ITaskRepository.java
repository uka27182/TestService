package ru.test.model;

import java.util.List;

public interface ITaskRepository {
    Task getByUuid(String uuid);
    Task persist(Task task);
    void saveOrUpdate(Task task);

    Long getCount();
    List<Task> getAllTask();
    int deleteAllTask();
}

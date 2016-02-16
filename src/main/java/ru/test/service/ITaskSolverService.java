package ru.test.service;

import ru.test.model.Task;

import java.util.List;

public interface ITaskSolverService {
    Answer getAnswerFor(String uuid, String userName, String action, List<String> args);
    List<Task> getAllTask();
    int deleteAllTask();
}

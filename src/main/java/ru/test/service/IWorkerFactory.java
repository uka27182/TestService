package ru.test.service;

import ru.test.model.Task;

public interface IWorkerFactory {
    Runnable getWorkerFor(Task task);
}

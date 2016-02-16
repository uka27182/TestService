package ru.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.test.model.ITaskRepository;
import ru.test.model.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Component
public abstract class Worker implements Runnable {
    final static Logger logger = LoggerFactory.getLogger(Worker.class);
    protected List<String> args;
    private Task task;
    private ITaskRepository repository;

    public Worker(Task task, ITaskRepository repository) {
        this.task = task;
        this.repository = repository;
        String argsAsString = task.getArgs();
        args = Arrays.asList(argsAsString.split(";"));
    }

    @Override
    public void run() {
        long timeout = 1000*Long.valueOf(args.get(0));
        try {
            task.setSolvingStartDt(LocalDateTime.now());
            logger.debug("run task {} [{}]", task.getUuid(), task);
            Thread.currentThread().sleep(timeout);
            doWork(task);
            logger.debug("task {} done [{}]", task.getUuid(), task);
            task.setSolvedDt(LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Error while solving task {}. ERR_MSG: {}", task.getUuid(), e.getMessage());
            task.setState(2);
            task.setResult(e.getMessage());
            task.setSolvedDt(LocalDateTime.now());
        }
        repository.saveOrUpdate(task);
    }

    public abstract void doWork(Task task);
}

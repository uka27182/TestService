package ru.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.test.model.ITaskRepository;
import ru.test.model.Task;

@Component
public class SimpleWorkerFactory implements IWorkerFactory{
    final static Logger logger = LoggerFactory.getLogger(SimpleWorkerFactory.class);

    @Autowired(required = true)
    @Qualifier(value = "hibernateTaskRepository")
    private ITaskRepository repository;

    @Override
    public Runnable getWorkerFor(Task task) {
        logger.debug("Search worker for task {}, sessionFactory ", task);

        if (task.getAction() == null) {
            throw new IllegalArgumentException("null action");
        }

        if (task.getArgs() == null || task.getArgs().isEmpty()) {
            throw new IllegalArgumentException("null or empty args");
        }

        String action = task.getAction();

        if (action.equalsIgnoreCase("SUM")) {
            return new Worker(task, repository) {
                @Override
                public void doWork(Task task) {
                    task.setState(1);
                    task.setResult(Long.toString(Long.valueOf(args.get(0)) + Long.valueOf(args.get(1))));
                }
            };
        }

        if (action.equalsIgnoreCase("SUB")) {
            return new Worker(task, repository) {
                @Override
                public void doWork(Task task) {
                    task.setState(1);
                    task.setResult(Long.toString(Long.valueOf(args.get(0)) - Long.valueOf(args.get(1))));
                }
            };
        }

        if (action.equalsIgnoreCase("MUL")) {
            return new Worker(task, repository) {
                @Override
                public void doWork(Task task) {
                    task.setState(1);
                    task.setResult(Long.toString(Long.valueOf(args.get(0)) * Long.valueOf(args.get(1))));
                }
            };
        }

        if (action.equalsIgnoreCase("DIV")) {
            return new Worker(task, repository) {
                @Override
                public void doWork(Task task) {
                    task.setState(1);
                    task.setResult(Long.toString(Long.valueOf(args.get(0)) / Long.valueOf(args.get(1))));
                }
            };
        }

        return new Worker(task, repository) {
            @Override
            public void doWork(Task task) {
                task.setState(2);
                task.setResult(task.getAction() +  " - unimplemented");
            }
        };
    }
}



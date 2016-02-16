package ru.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.model.ITaskRepository;
import ru.test.model.Task;
import ru.test.settings.ISettings;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("SolverService")
@Transactional
public class TaskSolverService implements ITaskSolverService {
    final static Logger logger = LoggerFactory.getLogger(TaskSolverService.class);

    @Autowired(required = true)
    @Qualifier("hibernateTaskRepository")
    private ITaskRepository repository;

    @Autowired
    private ISettings settings;

    @Autowired
    private IWorkerFactory workerFactory;

    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor executorService;

    @PostConstruct
    public void Init() {
        queue = new ArrayBlockingQueue<>(settings.getTaskQueueCounts());
        executorService = new ThreadPoolExecutor(
                settings.getCorePoolSize(),
                settings.getMaximumPoolSize(),
                0L,
                TimeUnit.MILLISECONDS,
                queue);
    }

    @PreDestroy
    private void resourcesRelease(){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            logger.error("Error while send shutdown to thread pool: ERR MSG {}", e.getMessage());
        }
    }

    @Override
    public Answer getAnswerFor(String uuid, String userName, String action, List<String> args) {
        if ((args == null) || args.size() < 2) {
            logger.error("Have got task with empty list arguments");
            return new Answer("error", null, "There are should be two not null arguments");
        }

        String argsToString = String.join(";", args);
        logger.debug("Have got task to solve: action {} uuid {} args {}", action, uuid, argsToString);

        // if task already exists check state and return result
        Task task = repository.getByUuid(uuid);
        if (task != null) {
            if (task.getState() == 0) {
                logger.debug("Task {} already exists, status=wait", uuid);
                return Answer.ANSWER_FOR_NEW_TASK;
            }
            if (task.getState() == 1) {
                logger.debug("Task {} already exists, status=done", uuid);
                return Answer.withResult(task);
            }
            logger.debug("Task {} already exists, status=error", uuid);
            return Answer.withError(task);
        }

        logger.debug("Task {} is new, status=new", uuid);
        // else return "wait" and run task
        task = new Task(uuid, action, argsToString);
        repository.persist(task);
        executorService.execute(workerFactory.getWorkerFor(task));

        return Answer.ANSWER_FOR_NEW_TASK;
    }

    @Override
    public List<Task> getAllTask() {
        return repository.getAllTask();
    }

    @Override
    public int deleteAllTask() {
        return repository.deleteAllTask();
    }
}

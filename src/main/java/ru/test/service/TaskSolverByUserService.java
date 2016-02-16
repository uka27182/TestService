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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("SolverByUserService")
@Transactional
public class TaskSolverByUserService implements ITaskSolverService{
    final static Logger logger = LoggerFactory.getLogger(TaskSolverByUserService.class);

    @Autowired(required = true) @Qualifier("hibernateTaskRepository")
    private ITaskRepository repository;

    @Autowired
    private ISettings settings;

    @Autowired
    private IWorkerFactory workerFactory;

    private Map<String, UserEnvironment> usersTasks = new HashMap<>();
    private ScheduledExecutorService cleaner;

    @Override
    public Answer getAnswerFor(String uuid, String userName, String action, List<String> args) {
        if ((args == null) || args.size() < 2) {
            logger.error("Have got task with empty list arguments");
            return new Answer("error", null, "There are should be two not null arguments");
        }

        String argsToString = String.join(";", args);
        logger.info("Have got task to solve: user={}, action={} uuid={} args={}", userName, action, uuid, argsToString);

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
        task = new Task(uuid, userName, action, argsToString);
        repository.persist(task);
        synchronized (usersTasks) {
            if (usersTasks.containsKey(userName)) {
                logger.debug("User with name {} already exist, so task {} add to his environment", userName, task.getUuid());
                usersTasks.get(userName).addWorker(workerFactory.getWorkerFor(task));
            } else {
                logger.debug("User with name {} not exist, create new user environment, add task {} to it", userName, task.getUuid());
                UserEnvironment ue = new UserEnvironment(userName, settings.getTaskQueueCounts(), settings.getDownTime());
                ue.addWorker(workerFactory.getWorkerFor(task));
                usersTasks.put(userName, ue);
            }
        }

        return Answer.ANSWER_FOR_NEW_TASK;
    }

    @PostConstruct
    private void startUsersCleaner() {
        cleaner = Executors.newSingleThreadScheduledExecutor();
        Runnable cleaningTask = () -> {
            try {
                logger.debug("Cleaner: there are {} users", usersTasks.size());
                Iterator it = usersTasks.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, UserEnvironment> pair = (Map.Entry<String, UserEnvironment>) it.next();
                    if (pair.getValue().isDeadUser()) {
                        logger.debug("User with name {} don't work a lot of time. Remove it", pair.getKey());
                        it.remove();
                    }
                }
            } catch (Exception e) {
                logger.error("Error while cleaning users. ERR MSG: {}", e.getMessage());
            }
        };
        cleaner.scheduleAtFixedRate(cleaningTask, 5, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void resourcesRelease() {
        logger.debug("start resources releasing");
        cleaner.shutdownNow();
        Iterator it = usersTasks.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Map.Entry<String, UserEnvironment> pair = (Map.Entry<String, UserEnvironment>) it.next();
                pair.getValue().shutDownNow();
            } catch (Exception e) {
                logger.error("Error while send shutdown to user's thread: ERR MSG {}", e.getMessage());
            }
        }
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

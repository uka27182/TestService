package ru.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class for infrastructure storing for each user:
 * tasks queue and dedicated thread for maintenance order of execution
 */
public class UserEnvironment {
    final static Logger logger = LoggerFactory.getLogger(UserEnvironment.class);

    private String userName;
    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor executorService;
    private int downTime;
    private LocalDateTime touchDt;

    /**
     * Create User Environment
     * @param userName user name
     * @param queueSize queue size for task
     * @param downTime very simple way to determine time of killing this user,
     *                 if queue is empty and last activity was earlier than 'downTime' seconds ago
     *                 this environment will be removed
     */
    public UserEnvironment(String userName, int queueSize, int downTime) {
        this.userName = userName;
        this.downTime = downTime;
        queue = new ArrayBlockingQueue<>(queueSize);
        executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue);
        touchDt = LocalDateTime.now();
    }

    public boolean isTaskQueueEmpty() {
        return queue.isEmpty();
    }

    public void addWorker(Runnable worker) {
        logger.debug("Username {} , queue size {}", userName, queue.size());
        touchDt = LocalDateTime.now();
        executorService.submit(worker);
    }

    public boolean isDowntimeExceeded() {
        return ChronoUnit.SECONDS.between(touchDt, LocalDateTime.now()) > downTime;
    }

    public boolean isDeadUser() {
        return isDowntimeExceeded() && queue.isEmpty() && executorService.getActiveCount() == 0;
    }

    public void shutDownNow() {
        executorService.shutdownNow();
    }
}


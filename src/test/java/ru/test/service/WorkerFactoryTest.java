package ru.test.service;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.test.model.Task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


@Ignore("work if there is no artificial delay before getting result")
public class WorkerFactoryTest {
    final static Logger logger = LoggerFactory.getLogger(TaskSolverService.class);

    SimpleWorkerFactory simpleWorkerFactory = new SimpleWorkerFactory();

    @Test
    public void testWorkerSum() throws Exception {
        Task task = new Task("1000", "sum", "1;2");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("3"));
    }

    @Test
    public void testWorkerSub() throws Exception {
        Task task = new Task("1000", "sub", "20;1");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("19"));
    }

    @Test
    public void testWorkerMul() throws Exception {
        Task task = new Task("1000", "mul", "3;2");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("6"));
    }

    @Test
    public void testWorkerDiv() throws Exception {
        Task task = new Task("1000", "div", "4;2");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("2"));
    }

    @Test
    public void testWorkerFactorial() throws Exception {
        Task task = new Task("1000", "factorial", "4;2");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        String res = task.getResult();
        logger.debug("was got {} ", res);
        assertThat(res, is("factorial - unimplemented"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWorker_NullAction() throws Exception {
        Task task = new Task("1000", null, "4;2");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWorker_EmptyArgs() throws Exception {
        Task task = new Task("1000", "sum", "");
        Runnable worker = simpleWorkerFactory.getWorkerFor(task);
        new Thread(worker).start();
        Thread.currentThread().sleep(100);
        assertThat(task.getResult(), is("2"));
    }
}
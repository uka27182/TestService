package ru.test.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@ContextConfiguration(locations = {"classpath:spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(false)
//@Transactional
public class TaskSolverServiceTest {
    final static Logger logger = LoggerFactory.getLogger(TaskSolverService.class);

    @Autowired
    @Qualifier("SolverService")
    private ITaskSolverService service;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() throws Exception {
        if (setUpIsDone) {
            return;
        }
        service.deleteAllTask();
        service.getAnswerFor("1000", "unknown", "sum", Arrays.asList("1", "1"));
        service.getAnswerFor("2000", "unknown", "sub", Arrays.asList("2", "1"));
        service.getAnswerFor("3000", "unknown", "mul", Arrays.asList("2", "3"));
        service.getAnswerFor("4000", "unknown", "div", Arrays.asList("2", "2"));
        service.getAnswerFor("5000", "unknown", "fft", Arrays.asList("2", "12"));

        service.getAnswerFor("6000", "unknown", "div", Arrays.asList("1", "0"));
        service.getAnswerFor("7000", "unknown", "sum", Arrays.asList("40", "1"));
        Thread.currentThread().sleep(3000);

        setUpIsDone = true;
    }

    @Test
    public void testGetAnswerFor_NewTask() throws Exception {
        Answer answer = service.getAnswerFor("100", "unknown", "sum", Arrays.asList("1", "2"));
        assertThat(answer.getStatus(), is("wait"));
    }

    @Test
    public void testGetAnswerFor_EmptyArgs() throws Exception {
        Answer answer = service.getAnswerFor("200", "unknown", "test", null);
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_sum() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1000", "unknown", "sum", Arrays.asList("1", "1"));
        assertThat(answer.getResult(), is("2"));
    }

    @Test
    public void test_sub() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2000", "unknown", "sub", Arrays.asList("2", "1"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_mul() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("3000", "unknown", "mul", Arrays.asList("2", "3"));
        assertThat(answer.getResult(), is("6"));
    }

    @Test
    public void test_div() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("4000", "unknown", "div", Arrays.asList("2", "2"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_fft() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("5000", "unknown", "fft", Arrays.asList("2", "12"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_div_zero_division() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("6000", "unknown", "div", Arrays.asList("1", "0"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_aum_not_ready() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("7000", "unknown", "sum", Arrays.asList("3", "1"));
        assertThat(answer.getStatus(), is("wait"));
    }
}
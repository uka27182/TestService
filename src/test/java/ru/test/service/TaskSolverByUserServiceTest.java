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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(locations = {"classpath:spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(false)
//@Transactional
public class TaskSolverByUserServiceTest {
    final static Logger logger = LoggerFactory.getLogger(TaskSolverByUserServiceTest.class);

    @Autowired
    @Qualifier("SolverByUserService")
    private ITaskSolverService service;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() throws Exception {
        if (setUpIsDone) {
            return;
        }
        service.deleteAllTask();
        service.getAnswerFor("1001", "unknown", "sum", Arrays.asList("1", "1"));
        service.getAnswerFor("1002", "unknown", "sub", Arrays.asList("2", "1"));
        service.getAnswerFor("1003", "unknown", "mul", Arrays.asList("2", "3"));
        service.getAnswerFor("1004", "unknown", "div", Arrays.asList("2", "2"));
        service.getAnswerFor("1005", "unknown", "fft", Arrays.asList("2", "12"));
        service.getAnswerFor("1006", "unknown", "div", Arrays.asList("1", "0"));  // error
        service.getAnswerFor("1007", "unknown", "sum", Arrays.asList("40", "1")); // wait

        service.getAnswerFor("2001", "test1", "sum", Arrays.asList("1", "1"));
        service.getAnswerFor("2002", "test2", "sub", Arrays.asList("2", "1"));
        service.getAnswerFor("2003", "test3", "mul", Arrays.asList("2", "3"));
        service.getAnswerFor("2004", "test4", "div", Arrays.asList("2", "2"));
        service.getAnswerFor("2005", "test5", "fft", Arrays.asList("2", "12"));
        service.getAnswerFor("2006", "test6", "div", Arrays.asList("1", "0"));  // error
        service.getAnswerFor("2007", "test7", "sum", Arrays.asList("40", "1")); // wait

        Thread.currentThread().sleep(11000);

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
        answer = service.getAnswerFor("1001", "unknown", "sum", Arrays.asList("1", "1"));
        assertThat(answer.getResult(), is("2"));
    }

    @Test
    public void test_sub() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1002", "unknown", "sub", Arrays.asList("2", "1"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_mul() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1003", "unknown", "mul", Arrays.asList("2", "3"));
        assertThat(answer.getResult(), is("6"));
    }

    @Test
    public void test_div() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1004", "unknown", "div", Arrays.asList("2", "2"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_fft() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1005", "unknown", "fft", Arrays.asList("2", "12"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_div_zero_division() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1006", "unknown", "div", Arrays.asList("1", "0"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_sum_not_ready() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("1007", "unknown", "sum", Arrays.asList("40", "1"));
        assertThat(answer.getStatus(), is("wait"));
    }

    @Test
    public void test_diff_user__sum() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2001", "test1", "sum", Arrays.asList("1", "1"));
        assertThat(answer.getResult(), is("2"));
    }

    @Test
    public void test_diff_user__sub() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2002", "test2", "sub", Arrays.asList("2", "1"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_diff_user__mul() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2003", "test3", "mul", Arrays.asList("2", "3"));
        assertThat(answer.getResult(), is("6"));
    }

    @Test
    public void test_diff_user__div() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2004", "test4", "div", Arrays.asList("2", "2"));
        assertThat(answer.getResult(), is("1"));
    }

    @Test
    public void test_diff_user__fft() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2005", "test5", "fft", Arrays.asList("2", "12"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_diff_user__div_zero_division() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2006", "test6", "div", Arrays.asList("1", "0"));
        assertThat(answer.getStatus(), is("error"));
    }

    @Test
    public void test_diff_user__sum_not_ready() throws Exception {
        Answer answer;
        answer = service.getAnswerFor("2007", "test7", "sum", Arrays.asList("40", "1"));
        assertThat(answer.getStatus(), is("wait"));
    }
}
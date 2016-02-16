package ru.test.model;

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
import ru.test.service.TaskSolverService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@ContextConfiguration(locations = {"classpath:spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Rollback(false)
@Transactional
public class TaskRepositoryTest {
    @Autowired(required = true)
    @Qualifier(value = "hibernateTaskRepository")
    private ITaskRepository repository;


    @Before
    public void setUp() throws Exception {
        repository.deleteAllTask();
    }

    @Test
    public void testCount() throws Exception {
        Long count = repository.getCount();
        assertThat(count, is(0L));
    }

    @Test
    public void testAdd() throws Exception {
        repository.persist(new Task("123", "action", "1;2"));
        Long count = repository.getCount();
        assertThat(count, is(1L));
    }
}
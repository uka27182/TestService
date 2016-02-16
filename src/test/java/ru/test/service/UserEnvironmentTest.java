package ru.test.service;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class UserEnvironmentTest {

    UserEnvironment ue;

    @Before
    public void setUp() throws Exception {
        ue = new UserEnvironment("test", 2, 2);
    }

    @Test
    public void testUE_emptyQueue() throws Exception {
        assertThat(ue.isTaskQueueEmpty(),is(true));
    }

    @Test
    public void testUE_downtimeNotExceeded() throws Exception {
        assertThat(ue.isDowntimeExceeded(),is(false));
    }

    @Test
    public void testUE_downtimetExceeded() throws Exception {
        Thread.currentThread().sleep(3000);
        assertThat(ue.isDowntimeExceeded(),is(true));
    }
}
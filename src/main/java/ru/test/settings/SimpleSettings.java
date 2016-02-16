package ru.test.settings;

import org.springframework.stereotype.Component;

@Component
public class SimpleSettings implements ISettings{

    @Override
    public int getCorePoolSize() {
        return 10;
    }

    @Override
    public int getMaximumPoolSize() {
        return 20;
    }

    @Override
    public int getTaskQueueCounts() {
        return 100;
    }

    @Override
    public int getDownTime() {
        return 10;
    }
}

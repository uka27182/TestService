package ru.test.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.test.model.Task;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer {
    private String status;
    private String result;
    private String message;

    public Answer(String status, String result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static final Answer ANSWER_FOR_NEW_TASK = new Answer("wait", null, null);

    public static final Answer TEST_ANSWER = new Answer("test", "test", "test");

    public static Answer withError(Task task) {
        return new Answer("error", null, task.getResult());
    }

    public static Answer withResult(Task task) {
        return new Answer("ok", task.getResult(), null);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "status='" + status + '\'' +
                ", result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

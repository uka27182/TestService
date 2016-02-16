package ru.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="task")
public class Task {
    @Id
    private String uuid;
    private String action;
    private @Column(name="username") String userName;
    private String args;
    // 0 - new, 1 - done, 2 - error
    private int state;
    private String result;
    private @Column(name="solved_dt") LocalDateTime solvedDt;
    private @Column(name="post_dt") LocalDateTime postDt;
    private @Column(name="solvingstart_dt") LocalDateTime solvingStartDt;

    public Task() {
    }

    public Task(String uuid, String action, String args) {
        this(uuid, "unknown", action, args, 0, LocalDateTime.now());
    }

    public Task(String uuid, String userName, String action, String args) {
        this(uuid, userName, action, args, 0, LocalDateTime.now());
    }

    public Task(String uuid, String userName, String action, String args, int state, LocalDateTime postDt) {
        this.uuid = uuid;
        this.action = action;
        this.userName = userName;
        this.args = args;
        this.state = state;
        this.postDt = postDt;
    }


    public String getUuid() {
        return uuid;
    }

    public String getAction() {
        return action;
    }

    public String getArgs() {
        return args;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        Done();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public LocalDateTime getSolvedDt() {
        return solvedDt;
    }

    public void Done() {
        setSolvedDt(LocalDateTime.now());
    }

    public void setSolvedDt(LocalDateTime solvedDt) {
        this.solvedDt = solvedDt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getSolvingStartDt() {
        return solvingStartDt;
    }

    public void setSolvingStartDt(LocalDateTime solvingStartDt) {
        this.solvingStartDt = solvingStartDt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uuid='" + uuid + '\'' +
                ", action='" + action + '\'' +
                ", userName='" + userName + '\'' +
                ", args='" + args + '\'' +
                ", state=" + state +  + '\'' +
                ", result='" + result + '\'' +
                ", solvingStartDt=" + solvingStartDt + '\'' +
                ", postDt=" + postDt + '\'' +
                ", solvedDt=" + solvedDt +
                '}';
    }


}

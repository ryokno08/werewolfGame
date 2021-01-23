package jp.hack.minecraft.werewolfgame.core;

public class Task {
    int taskNo;
    boolean finished = false;

    public Task(int no) {
        this.taskNo = no;
    }

    public void finished() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getTaskNo() {
        return taskNo;
    }
}
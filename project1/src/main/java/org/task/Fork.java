package org.task;

import java.util.concurrent.locks.ReentrantLock;

public class Fork{

    public int forkId;
    public ReentrantLock lock;

    Fork(int id){
        this.forkId = id;
        this.lock = new ReentrantLock();
    }

    @Override
    public String toString() {
        return "Fork " + forkId;
    }
}
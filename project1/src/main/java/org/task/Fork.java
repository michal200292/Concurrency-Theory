package org.task;

import java.util.concurrent.locks.ReentrantLock;

public class Fork{

    public int forkId;
    public boolean taken;
    public ReentrantLock lock;

    Fork(int id){
        this.forkId = id;
        this.taken = false;
        this.lock = new ReentrantLock();
    }

    @Override
    public String toString() {
        return "Fork " + forkId;
    }

}
package org.task;

import java.util.concurrent.CountDownLatch;

public class Sol6 extends Philosopher{
    Sol6(int id, Fork leftFork, Fork rightFork, int waitTime, boolean testing, Arbiter arbiter, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, testing, arbiter, latch);
    }

    @Override
    public void takeForks(){
        if(arbiter.sem.tryAcquire()){
            this.leftFork.lock.lock();
            this.rightFork.lock.lock();
        }
        else{
            this.rightFork.lock.lock();
            this.leftFork.lock.lock();
        }
        arbiter.sem.release();
    }
}

package org.task;

import java.util.concurrent.CountDownLatch;

public class Sol5 extends Philosopher{
    Sol5(int id, Fork leftFork, Fork rightFork, int waitTime, boolean testing, Arbiter arbiter, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, testing, arbiter, latch);
    }
    @Override
    public void takeForks() throws InterruptedException {
        arbiter.sem.acquire();
        this.leftFork.lock.lock();
        this.rightFork.lock.lock();
        arbiter.sem.release();
    }
}

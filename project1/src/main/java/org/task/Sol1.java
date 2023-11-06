package org.task;

import java.util.concurrent.CountDownLatch;

public class Sol1 extends Philosopher{

    Sol1(int id, Fork leftFork, Fork rightFork, int waitTime, boolean testing, Arbiter arbiter, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, testing, arbiter, latch);
    }

    @Override
    public void takeForks() {
        this.leftFork.lock.lock();
        this.rightFork.lock.lock();
    }
}

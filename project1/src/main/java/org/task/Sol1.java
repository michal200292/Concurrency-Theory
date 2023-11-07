package org.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol1 extends Philosopher{

    Sol1(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks() {
        this.leftFork.lock.lock();
        this.rightFork.lock.lock();
    }
}

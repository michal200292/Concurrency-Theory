package org.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol5 extends Philosopher{

    Sol5(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks() throws InterruptedException {
        sem.acquire();
        this.leftFork.lock.lock();
        this.rightFork.lock.lock();
        sem.release();
    }
}

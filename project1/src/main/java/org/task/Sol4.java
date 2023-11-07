package org.task;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol4 extends Philosopher{
    private final Random rand = new Random();

    Sol4(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks() {
        if(rand.nextBoolean()){
            this.leftFork.lock.lock();
            this.rightFork.lock.lock();
        }
        else{
            this.rightFork.lock.lock();
            this.leftFork.lock.lock();
        }
    }
}

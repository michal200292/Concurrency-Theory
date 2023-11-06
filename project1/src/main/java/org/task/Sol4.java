package org.task;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Sol4 extends Philosopher{

    private final Random rand = new Random();
    Sol4(int id, Fork leftFork, Fork rightFork, int waitTime, boolean testing, Arbiter arbiter, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, testing, arbiter, latch);
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

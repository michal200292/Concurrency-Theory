package org.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol3 extends Philosopher{


    Sol3(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks() {
        if(id % 2 == 0){
            this.leftFork.lock.lock();
            this.rightFork.lock.lock();
        }
        else{
            this.rightFork.lock.lock();
            this.leftFork.lock.lock();
        }
    }
}

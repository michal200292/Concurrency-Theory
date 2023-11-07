package org.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol6 extends Philosopher{

    Sol6(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks(){
        if(sem.tryAcquire()){
            this.leftFork.lock.lock();
            this.rightFork.lock.lock();
        }
        else{
            this.rightFork.lock.lock();
            this.leftFork.lock.lock();
        }
        sem.release();
    }
}

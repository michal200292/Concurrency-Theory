package org.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Sol2 extends Philosopher{

    Sol2(int id, Fork leftFork, Fork rightFork, int waitTime, Semaphore sem, CountDownLatch latch) {
        super(id, leftFork, rightFork, waitTime, sem, latch);
    }

    @Override
    public void takeForks() {
        boolean taken = false;
        while(!taken){
            try{
                this.sem.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!(leftFork.taken && rightFork.taken)){
                leftFork.taken = true;
                rightFork.taken = true;
                taken = true;
            }
            this.sem.release();
        }
    }

    @Override
    public void putForks() {
        try{
            this.sem.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leftFork.taken = false;
        rightFork.taken = false;
        this.sem.release();
    }
}

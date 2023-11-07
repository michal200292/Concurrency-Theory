package org.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

abstract public class Philosopher implements Runnable{

    public int id;
    public final Fork leftFork;
    public final Fork rightFork;
    public int waitTime;
    public final Semaphore sem;
    public CountDownLatch latch;

    public List<Long> time;

    Philosopher(int id, Fork leftFork, Fork rightFork, int waitTime,
                Semaphore sem, CountDownLatch latch){
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.waitTime = waitTime;
        this.sem = sem;
        this.latch = latch;
        this.time =  new ArrayList<>();
    }

    abstract public void takeForks() throws InterruptedException;
    public void putForks(){
        this.leftFork.lock.unlock();
        this.rightFork.lock.unlock();
    }

    public void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        long t1, t2;
        for(int i = 0; i < 100; i++){
            sleep(waitTime);
            if(id == 1) System.out.println(i);
            t1 = System.nanoTime();
            try {
                takeForks();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            t2 = System.nanoTime();
            time.add(t2 - t1);
            sleep(waitTime);
            putForks();
        }
        latch.countDown();
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}

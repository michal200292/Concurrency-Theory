package org.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

abstract public class Philosopher implements Runnable{

    public int id;
    public Fork leftFork;
    public Fork rightFork;
    public int waitTime;
    public boolean testing;
    public Arbiter arbiter;
    public CountDownLatch latch;

    public List<Long> time;

    Philosopher(int id, Fork leftFork, Fork rightFork, int waitTime,
                boolean testing, Arbiter arbiter, CountDownLatch latch){
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.waitTime = waitTime;
        this.testing = testing;
        this.arbiter = arbiter;
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
//            if(arbiter.isFinished()){
//                latch.countDown();
//                return;
//            }
//            sleep(waitTime);
            t1 = System.nanoTime();
            try {
                takeForks();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            t2 = System.nanoTime();
            putForks();
//            sleep(waitTime);
            time.add(t2 - t1);
        }
//        arbiter.finishSimulation();
        latch.countDown();
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}

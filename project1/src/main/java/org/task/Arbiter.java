package org.task;

import java.util.concurrent.Semaphore;

public class Arbiter {
    public int noOfPhilosophers;
    private boolean finished;

    public Semaphore sem;
    Arbiter(int noOfPhilosophers){
        this.noOfPhilosophers = noOfPhilosophers;
        this.finished = false;
        this.sem = new Semaphore(noOfPhilosophers - 1);
    }

    synchronized public void finishSimulation(){
        this.finished = true;
    }

    public boolean isFinished(){
        return this.finished;
    }

}

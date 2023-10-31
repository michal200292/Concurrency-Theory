package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad1 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();

        int noOfPhilosophers = 5;

        Arbiter arbiter = new Arbiter(noOfPhilosophers - 1);
        List<Fork> forks = new ArrayList<>();
        for(int i = 1; i <= noOfPhilosophers ; i ++){
            forks.add(new Fork(i));
        }

        for(int i = 1; i < noOfPhilosophers; i++){
            exec.execute(new Thread(new Philosopher(i, forks.get(i-1), forks.get(i), arbiter)));
        }
        exec.execute(new Thread(new Philosopher(noOfPhilosophers, forks.get(noOfPhilosophers - 1), forks.get(0), arbiter)));
        Thread.sleep(40000);
        exec.shutdownNow();
    }
}


class Fork{
    boolean taken;

    private int forkId;

    Fork(int id){
        this.forkId = id;
        this.taken = false;
    }

    public boolean get() throws InterruptedException {
        synchronized (this){
            while(taken){
                wait();
            }
            this.taken = true;
        }
        return true;
    }

    public void put(){
        synchronized (this){
            this.taken = false;
            notify();
        }
    }
}

class Arbiter{

    private int count;
    Arbiter(int count){
        this.count = count;
    }

    public boolean get() throws InterruptedException {
        synchronized (this){
            while(count <= 0){
                wait();
            }
            count--;
        }
        return true;
    }

    public void put(){
        synchronized (this){
            count ++;
            notify();
        }
    }
}

class Philosopher implements Runnable{
    Fork leftFork;
    Fork rightFOrk;

    Arbiter arbiter;

    private int id = 0;

    Philosopher(int id, Fork left, Fork right, Arbiter arbiter){
        this.id = id;
        this.leftFork = left;
        this.rightFOrk = right;
        this.arbiter = arbiter;
    }

    private void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() throws RuntimeException{
        while(true){
            System.out.println("Philosopher " + this.id + " thinks.");
            this.sleep();
            try {
                arbiter.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                leftFork.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sleep();
            try {
                rightFOrk.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Philosopher " + this.id + " eats.");
            this.sleep();
            leftFork.put();
            rightFOrk.put();
            arbiter.put();
        }
    }
}

package org.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class Test{
    Semaphore semaphore;

    private final int noOfPhilosophers;
    private final ExecutorService executor;
    private final CountDownLatch latch;
    private final List<Fork> forks = new ArrayList<>();
    private final ArrayList<Philosopher> philosophers;

    private final int waitTime;
    Test(int noOfPhilosophers, int waitTime, boolean binarySemaphore){
        this.noOfPhilosophers = noOfPhilosophers;
        if(binarySemaphore) semaphore = new Semaphore(1);
        else semaphore = new Semaphore(noOfPhilosophers - 1);
        latch = new CountDownLatch(noOfPhilosophers);
        executor = Executors.newFixedThreadPool(noOfPhilosophers);
        IntStream.range(0, noOfPhilosophers).forEach(i -> forks.add(new Fork(i)));
        this.waitTime = waitTime;
        philosophers = new ArrayList<>();
    }

    public void test() throws InterruptedException, FileNotFoundException {
        IntStream.range(0, noOfPhilosophers - 1)
                .forEach(i -> philosophers.add(new Sol6(i, forks.get(i), forks.get(i + 1), waitTime, semaphore, latch)));
        philosophers.add(new Sol6(noOfPhilosophers - 1,
                forks.get(noOfPhilosophers - 1), forks.get(0), waitTime, semaphore, latch));

        for(Philosopher phil : philosophers){
            executor.execute(new Thread(phil));
        }
        executor.shutdown();
        latch.await();
        writeToCSV();

    }

    private void writeToCSV() throws FileNotFoundException {
        File file = new File("measures/measures6_2.csv");
        PrintWriter out = new PrintWriter(file);
        for(int i = 0; i < noOfPhilosophers; i++){
            out.print(i + 1);
            for(long time : philosophers.get(i).time){
                out.print(", " + time);
            }
            out.println();
        }
        out.close();
    }

}

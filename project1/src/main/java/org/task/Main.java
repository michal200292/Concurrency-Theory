package org.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        boolean testing = false;

        int noOfPhilosophers = 5;
        CountDownLatch latch = new CountDownLatch(noOfPhilosophers);
        ExecutorService executor = Executors.newFixedThreadPool(noOfPhilosophers);

        Arbiter arbiter = new Arbiter(noOfPhilosophers);
        List<Fork> forks = new ArrayList<>();
        IntStream.range(0, noOfPhilosophers).forEach(i -> forks.add(new Fork(i)));

        List<Philosopher> philosophers = new ArrayList<>();
        IntStream.range(0, noOfPhilosophers - 1)
                .forEach(i -> philosophers.add(new Sol3(i, forks.get(i), forks.get(i + 1),
                        10, testing, arbiter, latch)));

        philosophers.add(new Sol3(noOfPhilosophers - 1, forks.get(noOfPhilosophers - 1),
                forks.get(0), 10, testing, arbiter, latch));

        for(Philosopher phil : philosophers){
            executor.execute(new Thread(phil));
        }

        executor.shutdown();
        latch.await();
        for(Philosopher phil : philosophers){
            Optional<Long> opt =  phil.time.stream().reduce(Long::sum);
            if(opt.isPresent()){
                float sum = (float) opt.get();
                System.out.println(sum / ((float) phil.time.size()));
            }

        }
    }
}
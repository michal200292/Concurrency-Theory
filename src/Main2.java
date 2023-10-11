package src;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Threading thread1 = new Threading(1000000, counter);
        Threading thread2 = new Threading(1000000, counter);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

class Counter{
    static int counter;

    public int getCounter(){
        return counter;
    }
    public void increment(){
        synchronized (this){
            counter++;
        }
    }

    public static void increment2(Counter sync){
        synchronized (sync) {
            counter++;
        }
    }
}

class Threading extends Thread{
    Counter count;
    int n;
    Threading(int n, Counter count){
        this.n = n;
        this.count = count;
    }
    public void run(){
        for(int i = 0; i < n; i++){
            this.count.increment();
            Counter.increment2(this.count);
        }
        System.out.println(this.count.getCounter());
    }
}


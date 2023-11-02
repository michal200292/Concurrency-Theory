// Incrementing global variable

package lab1.zad2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad2{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Adder globalAdder = new Adder();
        for(int i = 0; i < 4; i++){
            MyThread myThread = new MyThread(1000_000, globalAdder) ;
            Thread thread = new Thread(myThread);
            exec.execute(thread);
        }
        exec.shutdown();
    }
}


class MyThread implements Runnable {

    private final int n;
    private final Adder adder;
    MyThread(int n, Adder adder){
        this.n = n;
        this.adder = adder;
    }
    public void run() {
        for(int i = 0; i < this.n; i++){
            adder.increment(); // You cannot use both this functions because they access the same static variable
            Adder.increment2(); // and have different monitors
        }
        System.out.println("Thread " + Thread.currentThread().threadId() +
                ", counter value = " + adder.getCounter());
    }
}

class Adder{
    static int counter = 0;
    synchronized void increment(){
        counter++;
    }
    static synchronized void increment2(){
        counter++;
    }
    synchronized int getCounter(){
        return counter;
    }
}
// implementing a counter semaphore

package lab1.zad6b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Zad6b{
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(10, 0);
        IntStream.range(0, 5).forEach(i -> exec.execute(new Thread(new Prod(semaphore))));
        IntStream.range(0, 15).forEach(i -> exec.execute(new Thread(new Cons(semaphore))));
        exec.shutdown();
    }
}

class Semaphore{
    private int maxCapacity;

    private int curCapacity;
    private int buffer;

    public Semaphore(int maxCapacity, int initialBuffer){
        this.maxCapacity = maxCapacity;
        this.curCapacity = 0;
        this.buffer = initialBuffer;
    }

    public synchronized int P(){
        while(curCapacity <= 0){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        curCapacity -= 1;
        int number = buffer;
        notify();
        return number;
    }

    public synchronized void V(int number){
        while(curCapacity > maxCapacity){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer = number;
        curCapacity += 1;
        notify();
    }
}


class Prod implements Runnable{
    Semaphore semaphore;
    Prod(Semaphore semaphore){
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        int i = 0;
        while(true){
            semaphore.V(i);
            i++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Cons implements Runnable{
    Semaphore semaphore;
    Cons(Semaphore semaphore){
        this.semaphore = semaphore;
    }
    @Override
    public void run() {
        while(true){
            int number = semaphore.P();
            System.out.println("Got a number: " + Thread.currentThread().threadId() + " " + number);
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

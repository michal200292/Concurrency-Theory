// implementing a binary semaphore

package lab1.zad6a;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad6a {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(false, 0);
        exec.execute(new Thread(new Prod(semaphore)));
        exec.execute(new Thread(new Cons(semaphore)));
        exec.shutdown();
    }
}

class Semaphore{
    private boolean state;
    private int buffer;

    public Semaphore(boolean initialState, int initialBuffer){
        this.state = initialState;
        this.buffer = initialBuffer;
    }

    public synchronized int P(){
        while(!state){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        state = false;
        int number = buffer;
        notify();
        return number;
    }

    public synchronized void V(int number){
        while(state){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        buffer = number;
        state = true;
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


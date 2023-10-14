
// Binary semaphore implementation
//
package lab1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad6{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();

        BinarySemaphore semaphore = new BinarySemaphore();

        Producer prod = new Producer(semaphore, Arrays.asList("One", "Two", "Three", "Four", "Five", "End"));
        Consumer cons = new Consumer(semaphore);

        Thread thread1 = new Thread(prod);
        Thread thread2 = new Thread(cons);

        exec.execute(thread1);
        exec.execute(thread2);
        exec.shutdown();
    }
}

class BinarySemaphore{
    private boolean state = false;

    static String buffer = "";
    public BinarySemaphore () {
    }
    public synchronized void P() throws RuntimeException {
        while (!state){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        state = false;

    }
    public synchronized void V(){
        if(!state){
            state = true;
            notify();
        }
    }
}


class Producer implements Runnable{

    BinarySemaphore semaphore;

    List<String> toSend;

    public Producer(BinarySemaphore semaphore, List<String> toSend){
        this.semaphore = semaphore;
        this.toSend = toSend;
    }
    public void run(){
        for(String msg: toSend){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            BinarySemaphore.buffer = msg;
            semaphore.V();
        }
    }
}


class Consumer implements Runnable{

    BinarySemaphore semaphore;

    List<String> receivedMsg = new LinkedList<>();
    public Consumer(BinarySemaphore semaphore){
        this.semaphore = semaphore;
    }
    public void run() throws RuntimeException{
        String received="";
        while(!received.equals("End")){
            semaphore.P();
            received = BinarySemaphore.buffer;
            receivedMsg.add(received);
            System.out.println(received);
        }
        System.out.println();
        for(String msg : receivedMsg){
            System.out.print(msg + " ");
        }
    }
}

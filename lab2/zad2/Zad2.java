package lab2.zad2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad2 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Buff next = new Buff();
        exec.execute(new Thread(new Proc(0, 0, new Buff(), next)));
        for(int i = 1; i <= 99; i++){
            Buff next_next = new Buff();
            exec.execute(new Thread(new Proc(1, i, next, next_next)));
            next = next_next;
        }
        exec.execute(new Thread(new Proc(2, 100, next, new Buff())));
        Thread.sleep(500000);
        exec.shutdownNow();
    }
}


class Buff{
    private String buff;

    private int count;
    Buff(){
        this.count = 0;
    }

    public String get(int threadId) throws InterruptedException {
        String x;
        synchronized (this){
            while(count <= 0){
                wait();
            }
            count -= 1;
            x = buff + " " + threadId;
            notify();
        }
        return x;
    }

    public void put(String msg) throws InterruptedException {
        synchronized (this){
            while(count >= 1){
                wait();
            }
//            System.out.println(msg);
            count += 1;
            buff = msg;
            notify();
        }
    }
}


class Proc implements Runnable{

    private final int job;

    private int threadId;
    private Buff before;
    private Buff after;
    Proc(int job, int threadId, Buff before, Buff after){
        this.threadId = threadId;
        this.job = job;
        this.before = before;
        this.after = after;
    }
    public void run(){
        while(true){
            String x = String.valueOf(threadId);
            if (job > 0){
                try {
                    x = before.get(threadId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(job < 2){
                try {
                    after.put(x);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                System.out.println(x);
            }
            threadId++;
        }

    }
}

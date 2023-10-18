package lab2;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad1 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Buffer buff = new Buffer(10);

        for(int i = 0; i < 5; i ++){
            Prod prod = new Prod(buff, i);
            Thread thread = new Thread(prod);
            exec.execute(thread);
        }

        for(int i = 0; i < 7; i ++){
            Cons cons = new Cons(buff, i + 10);
            Thread thread = new Thread(cons);
            exec.execute(thread);
        }

        Thread.sleep(20000);
        exec.shutdownNow();
    }
}


class RNG{
    static Random rand = new Random();
    static int randInt(int a, int b){
        return rand.nextInt(a,b);
    }
}



class Buffer{
    private int count;

    private static int pizzaId = 0;
    private int maxCapacity;

    List<Pair<Integer, Integer>> queue = new LinkedList<>();
    Buffer(int maxCapacity){
        this.count = 0;
        this.maxCapacity = maxCapacity;
    }

    public Pair get(int threadId) throws InterruptedException {
        Pair x;
        synchronized (this){
            while(count <= 0){
                wait();
            }
            count -= 1;
            x = this.queue.removeFirst();
            System.out.println("Thread number: " + threadId + ", got pizza from thread: " + x.getL() + ", pizza id: " + x.getR());
            notify();
        }
        return x;
    }

    public void put(int i, int threadID) throws InterruptedException {
        synchronized (this){
            while(count >= maxCapacity){
                wait();
            }
            count += 1;
            pizzaId++;
            this.queue.add(new Pair(threadID, pizzaId));
            System.out.println("Thread number: " + threadID + ", put pizza, pizza id: " + pizzaId);
            notify();
        }
    }
}

class Pair<L,R> {
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){
        return l;
    }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}

class Prod implements Runnable{

    public Buffer buff;
    private int id;
    Prod(Buffer buff, int id) {
        this.buff = buff;
        this.id = id;
    }
    public void run(){
        int number = 0;
        while(true){
            try {
                this.buff.put(number, id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(RNG.randInt(500, 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            number += 1;
        }
    }
}

class Cons implements Runnable{
    public Buffer buff;
    private int id;
    Cons(Buffer buff, int id) {
        this.buff = buff;
        this.id = id;
    }
    public void run(){
        Pair x;
        while(true){
            try {
                Thread.sleep(RNG.randInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                x = buff.get(this.id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

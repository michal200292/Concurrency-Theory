package lab4.zad1;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        SyncLinkedList list = new SyncLinkedList(10);
        IntStream.range(1, 5)
                .map(i -> 5 - i)
                .forEach(i -> list.pushNode(new Node<>(i)));


        out.println(list);

        IntStream.range(0, 20)
                .forEach(i -> executor.execute(new Thread(new Reader(list, i))));

        IntStream.range(1, 5)
                .forEach(i -> executor.execute(new Thread(new Writer(list))));

        executor.shutdown();

        Thread.sleep(50000);

        out.println(list);

    }
}


class RNG{

    static Random rand = new Random();
    public static int randInt(int a, int b){
        return RNG.rand.nextInt(b) + a;
    }
}


class SyncLinkedList{

    private int maxLength;
    private final Node<Integer> guard = new Node<>(-1);
    public SyncLinkedList(int maxLength){
        this.maxLength = maxLength;
    }

    public void pushNode(Node<Integer> newHead){
        newHead.next = this.guard.next;
        this.guard.next = newHead;
    }

    public boolean syncedContains(Integer o){

        this.guard.lock.readLock().lock();
        Node<Integer> cur = this.guard;

        while(cur.next != null){
            cur.next.lock.readLock().lock();
            cur.lock.readLock().unlock();
            cur = cur.next;
            if(cur.value.equals(o)){
                cur.lock.readLock().unlock();
                return true;
            }
        }
        cur.lock.readLock().unlock();
        return false;
    }

    public void syncedAdd(Integer o){

        Node<Integer> newNode = new Node<>(o);
        this.guard.lock.readLock().lock();
        Node<Integer> cur = this.guard;

        while(cur.next != null){
            cur.next.lock.readLock().lock();
            cur.lock.readLock().unlock();
            cur = cur.next;
        }
        cur.lock.readLock().unlock();
        cur.lock.writeLock().lock();
        cur.next = newNode;
        cur.lock.writeLock().unlock();
    }

    public boolean syncedRemove(Integer o){

        this.guard.lock.readLock().lock();
        Node<Integer> cur = this.guard;

        while(cur.next != null){
            cur.next.lock.readLock().lock();
            if(cur.next.value.equals(o)){
                cur.lock.readLock().unlock();
                cur.lock.writeLock().lock();
                cur.next.lock.readLock().unlock();
                cur.next.lock.writeLock().lock();

                cur.next = cur.next.next;
                cur.lock.writeLock().unlock();
                return true;
            }
            cur.lock.readLock().unlock();
            cur = cur.next;
        }
        cur.lock.readLock().unlock();
        return false;
    }
    @Override
    public String toString() {
        StringBuilder listView = new StringBuilder();
        Node<Integer> cur = this.guard.next;
        while(cur != null){
            listView.append(cur.value.toString());
            listView.append(" -> ");
            cur = cur.next;
        }
        listView.append("end\n");
        return listView.toString();
    }
}


class Node<T>{
    public T value;

    public ReadWriteLock lock;
    public Node<Integer> next = null;
    public Node(T value){
        this.value = value;
        this.lock = new ReentrantReadWriteLock();
    }
}

class Reader implements Runnable{

    SyncLinkedList list;

    private final Integer numberToSearch;
    public Reader(SyncLinkedList list, Integer numberToSearch){
        this.list = list;
        this.numberToSearch = numberToSearch;
    }
    @Override
    public void run() {
        for(int i = 0; i < 30; i++){
            System.out.println("Thread: " + Thread.currentThread().threadId()
                    + " " + "contains " + numberToSearch + " = " + list.syncedContains(numberToSearch));
            try {
                Thread.sleep(RNG.randInt(500, 1500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

class Writer implements Runnable{
    SyncLinkedList list;
    public Writer(SyncLinkedList list){
        this.list = list;
    }
    @Override
    public void run(){
        for(int i = 0; i < 15; i++){
            int number = RNG.randInt(0, 20);
            if(RNG.randInt(0, 2) < 1){
                list.syncedAdd(number);
                out.println("Added " + number);
            }
            else{
                if (list.syncedRemove(number)) {
                    out.println("Removed first " + number);
                }else{
                    out.println("Failed to find " + number);
                }
            }
            try {
                Thread.sleep(RNG.randInt(1000, 4000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
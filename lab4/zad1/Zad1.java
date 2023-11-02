package lab4.zad1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Zad1 {
    public static void main(String[] args) throws InterruptedException {
        LinkedList list = new LinkedList();
        for(int i=1; i < 8;i++){
            list.pushNode(new Node(i));
        }
        list.printList();

        ExecutorService exec = Executors.newCachedThreadPool();

        for(int i = 0; i < 5; i++){
            exec.execute(new Reader(i, list));
        }

        for(int i = 0; i < 2; i++){
            exec.execute(new Writer(i, list));
        }
        Thread.sleep(40000);
        exec.shutdownNow();

    }
}

class Node{

    public final ReentrantLock readLock;
    private int number;
    Node next = null;

    Node(int number){
        this.number = number;
        this.readLock =  new ReentrantLock();
    }

    public void setValue(int num){
        this.number = num;
    }

    public int getValue(){
        return this.number;
    }
}


class LinkedList{

    public final ReentrantLock getHeadLock;
    Node head = null;

    LinkedList(){
        this.getHeadLock = new ReentrantLock();
    }

    public void printList() {
        Node cur = head;
        while(cur != null){
            System.out.print(cur.getValue() + " -> ");
            cur = cur.next;
        }
        System.out.println("end");
    }
    public void pushNode(Node node){
        node.next = head;
        head = node;
    }
}


class Reader implements Runnable{

    private int id;

    LinkedList list;
    Reader(int id, LinkedList list){
        this.id = id;
        this.list = list;
    }

    @Override
    public void run() {
        while(true){
            System.out.println("I am reader " + this.id + " " + contains(0));
            sleep();
        }
    }

    private int contains(int number){
        int index = 0;
        Node cur = list.head;
        cur.readLock.lock();
        Node prev = null;
        while(cur != null){
            if(cur.getValue() == number){
                cur.readLock.unlock();
                return index;
            }
            if(cur.next != null){
                cur.next.readLock.lock();
            }
            System.out.println("threadReader: " + id + " on index: " + index);
            sleep();
            prev = cur;
            cur = cur.next;
            prev.readLock.unlock();
            index++;
        }
        return -1;
    }
    private void sleep(){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Writer implements Runnable{

    private int id;

    LinkedList list;
    Writer(int id, LinkedList list){
        this.id = id;
        this.list = list;
    }

    @Override
    public void run() {
        while(true){
            add(0);
            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int add(int number){
        int index = 0;
        Node newNode = new Node(number);
        Node cur = list.head;
        cur.readLock.lock();
        Node prev = null;
        while(cur.next != null){
            prev = cur;
            cur.next.readLock.lock();
            cur = cur.next;
            prev.readLock.unlock();
            System.out.println("threadWriter: " + id + " on index: " + index);
            sleep();
        }
        cur.next = newNode;
        cur.readLock.unlock();
        return 1;
    }
}
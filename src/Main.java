package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Synchronizer sync = new Synchronizer();
        for(int i = 0; i < 10; i++){
            Gunner gun = new Gunner(3, sync);
            exec.execute(gun);
        }
        exec.shutdown();
    }
}

class Gunner extends Thread{
    int n;
    Synchronizer sync;
    Gunner(int n, Synchronizer sync){
        this.n = n;
        this.sync = sync;
    }
    public void run(){
        for(int i = 0; i < n; i++){
            if(!sync.checkIfRunning()) return;
            System.out.println(n - i);
        }
        sync.stopRunning();
        System.out.println("Pif");
        System.out.println("Paf");
    }
}

class Synchronizer {
    private boolean running = true;

    public boolean checkIfRunning(){
        synchronized (this){
            return running;
        }
    }

    public void stopRunning(){
        synchronized (this){
            running = false;
        }
    }
}
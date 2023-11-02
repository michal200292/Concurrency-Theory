// Simple synchronization program
// The gunners are simultaneously counting down from a given number
// and all stop if only one finishes counting


package lab1.zad1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad1{
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

    synchronized public boolean checkIfRunning(){
        return running;
    }

    synchronized public void stopRunning(){
            running = false;
    }
}
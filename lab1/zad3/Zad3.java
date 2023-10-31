//Checking atomicity of string assignment operation

package lab1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad3{
    static String yes = "";

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++){
            MyThread2 myThread = new MyThread2(1000_000, String.valueOf(i+999999)) ;
            Thread thread = new Thread(myThread);
            exec.execute(thread);
        }
        exec.shutdown();
    }
}


class MyThread2 implements Runnable {

    private final int n;
    private String val = "";
    MyThread2(int n, String name){
        this.n = n;
        this.val = name;
    }
    public void run() {
        for(int i = 0; i < 10000; i++){
            Zad3.yes = val;
        }
        System.out.println(Zad3.yes);
    }
}
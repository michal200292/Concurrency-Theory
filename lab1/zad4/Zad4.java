// Program that is supposed to encounter deadlock

package lab1.zad4;

public class Zad4{

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new MyThread3(true));
        Thread thread2 = new Thread(new MyThread3(false));
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(MyThread3.counter);
    }
}

class MyThread3 implements Runnable{

    static boolean crappySemaphore=true;
    private final boolean turn;
    MyThread3(boolean turn){
        this.turn = turn;
    }
    static int counter = 0;
    public void run() {
        for(int i = 0; i < 1000; i++){
            System.out.println(Thread.currentThread().getName() + " " + MyThread3.counter + " " + i);
            while(!(this.turn == crappySemaphore));
            MyThread3.counter++;
            crappySemaphore = !turn;
        }
        System.out.println(Thread.currentThread().getName() + " Finished");
    }
}

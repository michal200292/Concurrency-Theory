// Simulating multithreaded Bingo game. Without using wait and notify.

package lab1.zad5b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad5b {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        Bingo bingo = new Bingo();
        Thread bingo_thread = new Thread(bingo);
        exec.execute(bingo_thread);

        for(int i = 0; i < 10; i++){
            Player player = new Player(bingo);
            Thread thread = new Thread(player);
            exec.execute(thread);
        }

        exec.shutdown();
    }
}

class RNG{
    static Random rand = new Random();
    synchronized static int randInt(int a, int b){
        return rand.nextInt(a,b);
    }
}


class Bingo implements Runnable {

    public boolean gameOver = false;
    List<Integer> numbers;
    Bingo(){
        this.numbers = new ArrayList<>();
    }
    public void run(){
        while(!this.gameOver){
            int nextNum = RNG.randInt(0, 25);
            this.addNumber(nextNum);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void addNumber(int nextNum){
        synchronized (this){
            numbers.add(nextNum);
        }
    }

    public synchronized void readNumbers(Player player){
        while (player.numbersRead < player.bingo.numbers.size()) {
            player.newNumbers.add(player.bingo.numbers.get(player.numbersRead++));
        }
    }

    public synchronized void endGame(Player player){
        player.bingo.gameOver = true;
    }
}


class Player implements Runnable {

    public final Bingo bingo;
    private int[][] bingoMap = new int[5][5];
    public int numbersRead;

    public int numbersWritten;
    public List<Integer> newNumbers;

    Player(Bingo bingo){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                bingoMap[i][j] = RNG.randInt(0, 25);
            }
        }
        this.numbersRead = 0;
        this.numbersWritten = 0;
        this.newNumbers = new ArrayList<>();
        this.bingo = bingo;
    }
    public void run(){
        while (!this.bingo.gameOver){
            this.bingo.readNumbers(this);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while(this.numbersWritten < this.numbersRead){
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        if(this.bingoMap[i][j] == this.newNumbers.get(this.numbersWritten)){
                            this.bingoMap[i][j] = -1;
                        }
                    }
                }
                this.numbersWritten++;
            }
            for(int i = 0; i < 5; i++){
                boolean check=true;
                for(int j = 0; j < 5; j++){
                    if(this.bingoMap[i][j] != -1){
                        check = false;
                    }
                }
                if(check){
                    this.bingo.endGame(this);
                    System.out.println(Arrays.deepToString(this.bingoMap));
                    break;
                }
            }
        }
    }
}
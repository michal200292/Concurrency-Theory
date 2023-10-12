// Simulating multithreaded Bingo game. Without using wait and notify.

package lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad5b {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++){
            Bingo player = new Bingo((i==0));
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
    private static List < Bingo > players= new ArrayList < Bingo >();

    private static boolean running = true;

    private static int winner = 0;

    private static int number = 0;

    private int id;
    private int[][] bingoMap = new int[5][5];
    private int[] rowCount = {0, 0, 0, 0, 0};
    private int[] colCount = {0, 0, 0, 0, 0};
    private int diag1Count = 0;
    private int diag2Count = 0;

    boolean gameLeader;

    private static void putNumber(){

    }
    static synchronized int addPlayer(Bingo player){
        Bingo.players.add(player);
        return Bingo.players.size() - 1;
    }

    static synchronized boolean isOver(){
        return running;
    }

    static synchronized void endTheGame(int winner){
        Bingo.winner = winner;
        running = false;
    }
    Bingo(boolean gameLeader){
        this.gameLeader = gameLeader;
        this.id = Bingo.addPlayer(this);
        for(int i = 0;  i < 5; i++){
            for(int j = 0; j < 5; j++){
                bingoMap[i][j] = RNG.randInt(0, 25);
            }
        }
    }
    @Override
    public void run() {
        if(this.gameLeader){
            while(Bingo.isOver()){
                int newNumber = RNG.randInt(0, 25);

            }

        }
        else{
            while(Bingo.isOver()){

            }
        }
    }
}





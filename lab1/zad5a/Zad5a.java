// Committees count votes and update global vote count

package lab1.zad5a;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zad5a {
    public static void main(String[] args){
        MainCommittee mainCommittee = new MainCommittee(10);
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++){
            Committee committee = new Committee(mainCommittee, (i+1)*100, 100);
            Thread thread = new Thread(committee);
            exec.execute(thread);
        }
        exec.shutdown();
    }
}


class Committee implements Runnable{

    static Random rand = new Random();
    private int votes;

    private int[] partyVotes = new int[10];
    private final int step;

    MainCommittee mainCommittee;
    Committee(MainCommittee mainCommittee, int votes, int step){
        for(int i = 0; i < 10; i++){
            partyVotes[i] = 0;
        }
        this.mainCommittee = mainCommittee;
        this.votes = votes;
        this.step = step;
    }

    @Override
    public void run() {
        int votes_so_far=0;
        for(int i = 0; i < this.votes; i++){
            int int_random = rand.nextInt(10);
            partyVotes[int_random]++;
            votes_so_far++;
            if(votes_so_far >= this.step){
                mainCommittee.increaseVotes(partyVotes);
                votes_so_far=0;
                for(int j = 0; j < 10; j++){
                    partyVotes[j] = 0;
                }
            }
        }
        mainCommittee.increaseVotes(partyVotes);
        mainCommittee.printCurrentVotes();
    }
}

class MainCommittee{

    private int committeesSoFar=0;
    private static List<Party> parties= new ArrayList<>() ;

    MainCommittee(int howMany){
        for(int i = 0; i < howMany; i++){
            parties.add(new Party(i));
        }
    }

    synchronized public void increaseVotes(int[] partyVotes){
        for(int i = 0; i < 10; i++){
            parties.get(i).increaseCount(partyVotes[i]);
        }

    }

    synchronized public void printCurrentVotes(){
        System.out.println("\nCommittees so far: " + ++this.committeesSoFar);
        for(Party party: parties){
            System.out.println(party);
        }
    }

}

class Party{
    private int voteCount;
    public int partyId;
    Party(int id){
        this.voteCount = 0;
        this.partyId = id;
    }

    public void increaseCount(int increase){
        this.voteCount += increase;
    }

    @Override
    public String toString(){
        return this.partyId + " Votes: " + this.voteCount;
    }
}

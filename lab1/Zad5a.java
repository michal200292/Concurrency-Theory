// Committees count votes and update global vote count

package lab1;

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
            Committee committee = new Committee(mainCommittee, (i+1)*100);
            Thread thread = new Thread(committee);
            exec.execute(thread);
        }
        exec.shutdown();
    }
}


class Committee implements Runnable{

    static Random rand = new Random();
    private int votes;
    MainCommittee mainCommittee;
    Committee(MainCommittee mainCommittee, int votes){
        this.mainCommittee = mainCommittee;
        this.votes = votes;
    }

    @Override
    public void run() {
        for(int i = 0; i < this.votes; i++){
            int int_random = rand.nextInt(10);
            mainCommittee.increaseVotes(int_random);
        }
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

    synchronized public void increaseVotes(int id){
        parties.get(id).increaseCount();
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

    public void increaseCount(){
        this.voteCount++;
    }

    @Override
    public String toString(){
        return this.partyId + " Votes: " + this.voteCount;
    }
}

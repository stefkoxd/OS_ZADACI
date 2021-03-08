package VotingSystem;

public class Candidate {
    private String name;
    private int numVotes;

    public String getName() {
        return name;
    }

    public int getNumVotes() {
        return numVotes;
    }

    Candidate(String name){
        this.name = name;
        this.numVotes = 0;
    }

    public void incrementNumVotes(){
        this.numVotes++;
    }

}

package VotingSystem;

public class Candidate {
    private String name;
    private int numVotes;

    Candidate(String name, int numVotes){
        this.name = name;
        this.numVotes = numVotes;
    }

    public String getName() {
        return name;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void incrementVotes(){
        this.numVotes++;
    }
}

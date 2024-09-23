package VotingSystem;

/**
* Represents a Party, and holds asssociated information such as seat they win, number of votes received, name and candidates. 
* Used by the CPL class.
* @author Lane Enget
* @author Pyrenees Gavois
*/
public class Party {
    private String name;
    private String [] candidates;
    private int numVotes;
    private int seatsReceived;

    /**
    * This is the constructor for Party.
    * @param name A String that represents the name of the party
    * remaining fields are set to default values; candidates set to null, numVotes and seatsReceived set to 0
    */
    public Party(String name){
        this.name = name;
        candidates = null;
        numVotes = 0;
        seatsReceived = 0;
    }
    /**
    * Gets the name of the party.
    *
    * @return the name of the part.
    */
    public String getName(){return name;}

    /**
    * Gets the number of votes of the party.
    *
    * @return the numVotes of the party.
    */
    public int getNumVotes(){return numVotes;}

    /**
    * Gets the number of Seats that the party received.
    *
    * @return the seatsReceived of the party.
    */
    public int getSeatsReceived(){return seatsReceived;}

    /**
    * Gets the names of all the candidates that are in the party.
    *
    * @return the candidates (Array of Strings) of the party.
    */   
    public String [] getCandidates(){return candidates;}

    /**
    * Setter method for the party name.
    */   
    public void setName(String name){
        this.name = name;
    }

    /**
    * Setter method for the candidates array.
    */   
    public void setCandidates(String [] candidates){
        this.candidates = candidates;
    }

    /**
    * Setter method for the number of votes.
    */   
    public void setNumVotes(int votes){
        numVotes = votes;
    }

    /**
    * Setter method for the number of seats received.
    */   
    public void setSeatsReceived(int seats){
        seatsReceived = seats;
    }

    /**
    * Increases the number of votes (numVotes) by one.
    */   
    public void incrementVoteCount(){numVotes++;}

    /**
    * Increases the number of seats (seatsReceived) by one.
    */   
    public void incrementSeatCount(){seatsReceived++;}
}
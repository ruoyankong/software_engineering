/*******************************************************************
 *  Candidate
 * 
 * The class of candidate.
 * Can be created by Candidate (String partyID, String name, int ranks).
 * 
 * Author: ruoyan
*******************************************************************/
package voting_system;

public class Candidate {
	String partyID;
	public String name;
	int ranks;

	/**
	 * Construct a new Candidate object.
	 * 
	 * @param partyID, String, the ID of the candidate's party
	 * @param name, String, the name of the candidate
	 * @param ranks, int, the rank of the candidate in the party, 1 for open voting
	 */
	public Candidate (String partyID, String name, int ranks) {
		this.partyID = partyID;
		this.name = name;
		this.ranks = ranks;
	}

	/**
	 * Return a hash code value for the Candidate object in support of
	 * {@link java.util.HashMap}.
	 * 
	 * @return  a hash code value for the Candidate object.
	 * @author  Jingfan Guo
	 */
	@Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + (this.name == null ? 0 : this.name.hashCode());  
        return hash;
    }

	/**
	 * Indicates whether some other Candidate object is "equal to" this one
	 * in support of {@link java.util.HashMap}.
	 * 
	 * @param   obj   the reference object with which to compare.
	 * @return  {@code true} if this Candidate object is the same as the argument;
	 *          {@code false} otherwise.
	 * @author  Jingfan Guo
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
		Candidate other = (Candidate) obj;
        if (this.name != other.name)
            return false;
        return true;
    }
}

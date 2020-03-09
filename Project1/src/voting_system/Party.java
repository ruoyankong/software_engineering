/*******************************************************************
 *  Party
 * 
 * Party is the class that stores the party information.
 * Can be created by Party(CopyOnWriteArrayList<Candidate> candidateList, String ID)
 * 
 * Author: ruoyan
*******************************************************************/
package voting_system;
import java.util.concurrent.CopyOnWriteArrayList;

public class Party {
	public CopyOnWriteArrayList<Candidate> candidateList;
	public String ID;

	/**
	 * Construct a new Party object.
	 * 
	 * @param candidateList the list of candidates of the party.
	 * @param ID String, the ID of the party
	 * 
	 * @author ruoyan
	 */
	public Party(CopyOnWriteArrayList<Candidate> candidateList, String ID) {
		this.candidateList = candidateList;
		this.ID = ID;
	}

	/**
	 * Return a hash code value for the Party object in support of
	 * {@link java.util.HashMap}.
	 * 
	 * @return  a hash code value for the Party object.
	 * @author  Jingfan Guo
	 */
	@Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + (this.ID == null ? 0 : this.ID.hashCode());
        return hash;
    }

	/**
	 * Indicates whether some other Party object is "equal to" this one
	 * in support of {@link java.util.HashMap}.
	 * 
	 * @param   obj   the reference object with which to compare.
	 * @return  {@code true} if this Party object is the same as the argument;
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
		Party other = (Party) obj;
        if (this.ID.equals(other.ID))
            return false;
        return true;
    }
}

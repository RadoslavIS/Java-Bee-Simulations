import java.util.Iterator;

/** Observation for bees that can lead a social lifestyle **/
public interface SocialBee extends Bee {

	/** Returns an iterator of all valid observations of the same individual with a social lifestyle<br>
	 * @return <code>SocialBee</code> Iterator with behaviour == social of the same individual **/
	Iterator<SocialBee> social();
}
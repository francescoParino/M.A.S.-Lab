package restaurants;
import jade.core.Agent;

public class Restaurant extends Agent {

	float rank;
	
	public Restaurant() {
		
	}

	protected void Setup() {
		//setup rank
		System.out.println("Hello from " + getLocalName());
	}

	/**
	 * @return the rank
	 */
	public float getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(float rank) {
		this.rank = rank;
	}
}
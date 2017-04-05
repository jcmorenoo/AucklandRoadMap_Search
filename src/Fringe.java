import java.util.PriorityQueue;
import java.util.Queue;

public class Fringe {

	private Queue<SearchNode> searchNodes;
	
	public Fringe(){
		this.searchNodes = new PriorityQueue<SearchNode>();
	}
	
}

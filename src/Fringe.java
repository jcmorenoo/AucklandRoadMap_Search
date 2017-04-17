import java.util.PriorityQueue;
import java.util.Queue;

public class Fringe {

	private Queue<SearchNode> searchNodes;
	
	public Fringe(){
		this.searchNodes = new PriorityQueue<SearchNode>();
	}
	
	public void enqueue(Node n, SearchNode sn, double costFromStart, double totalCost){
		SearchNode searchN = new SearchNode(n, sn, costFromStart, totalCost);
		this.searchNodes.add(searchN);
	}
	
	public SearchNode dequeue(){
		return this.searchNodes.poll();
	}
	
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.searchNodes.isEmpty();
	}
	
}

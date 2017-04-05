
public class SearchNode implements Comparable<SearchNode> {
	
	private Node node;
	private Segment path;
	private SearchNode from;
	private double costFromStart;
	private double totalCost;
	
	public SearchNode(Node node, SearchNode from, double costFromStart, double totalCost){
		this.node = node;
		this.from = from;
		this.costFromStart = costFromStart;
		this.totalCost = totalCost;
	}
	
	@Override
	public int compareTo(SearchNode o) {
		// TODO Auto-generated method stub
		
		
		return 0;
	}
	
	public SearchNode getFrom(){
		return this.from;
	}
	
	public void setFrom(SearchNode s){
		this.from = s;
	}
	
	public Node getNode(){
		return this.node;
	}
	
}

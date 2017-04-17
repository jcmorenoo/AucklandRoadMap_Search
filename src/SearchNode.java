
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
		
		if(from != null){
			for(Segment segIn : node.getSegIn()){
				if(segIn.getNode1() == node){
					if(segIn.getNode2() == from.getNode()){
						this.path = segIn;
						break;
					}
				}
				else{
					if(segIn.getNode1() == from.getNode()){
						this.path = segIn;
						break;
					}
				}
			}
		}
		else{
			this.path = null;
		}
	}
	public double getCostFromStart(){
		return this.costFromStart;
	}
	
	public void setPath(Segment s){
		this.path = s;
	}
	public Segment getPath(){
		return this.path;
	}
	public double getTotalCost(){
		return this.totalCost;
	}
	@Override
	public int compareTo(SearchNode o) {
		// TODO Auto-generated method stub
		if(this.totalCost < o.totalCost){
			return -1;
		}
		else if(this.totalCost > o.totalCost){
			return 1;
		}
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

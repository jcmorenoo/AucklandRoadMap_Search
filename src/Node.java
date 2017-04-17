import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * @author julian
 * Class which represents intersections in the map
 */
public class Node {	
	
	private Location location;
	private int id;
	private Set<Segment> segmentsIn = new HashSet<Segment>();
	private Set<Segment> segmentsOut = new HashSet<Segment>();
	private boolean isHighlighted = false;
	private ArrayList<Node> children = new ArrayList<Node>();
	private int count;
	private int reachBack;
	private boolean isAP = false;
	
	
	
	//FOR SEARCH
	private boolean visited;
	
	
	public Node(int id, Location loc){
		this.id = id;
		this.location = loc;
		this.visited = false;
		this.children = new ArrayList<Node>();
		this.count = Integer.MAX_VALUE;
		
	}
	
	
	public ArrayList<Node> getChildren(){
		return this.children;
	}
	
	public void addSegmentIn(Segment segIn){
		this.segmentsIn.add(segIn);
	}
	
	public void setChildren(ArrayList<Node> children){
		this.children = children;
	}
	public void addSegmentOut(Segment segOut){
		this.segmentsOut.add(segOut);
	}
	
	public Set<Segment> getSegIn(){
		return this.segmentsIn;
	}
	
	public Set<Segment> getSegOut(){
		return this.segmentsOut;
	}
	
	public Location getLocation(){	
		return this.location;
	}
	
	
	public int getId(){
		return this.id;
	}
	
	public void draw(Graphics g, Location origin, double scale){
		
		
		//System.out.println(this.location.toString());
		if(this.isHighlighted){
			g.setColor(Color.RED);
			Point p = this.location.asPoint(origin, scale);
			g.fillOval((int)p.getX()-5, (int)p.getY()-5, 10, 10);
		}
		else if(this.isAP){
			g.setColor(Color.orange);
			Point p = this.location.asPoint(origin, scale);
			g.fillOval((int)p.getX()-5, (int)p.getY()-5, 5, 5);
		}
		else{
			g.setColor(Color.GRAY);
			Point p = this.location.asPoint(origin, scale);
			g.fillOval((int)p.getX(), (int)p.getY(), 1, 1);
		}
		
		//System.out.println(p.toString());
		
	}
	
	public void highlight(){
		this.isHighlighted = true;
	}
	
	public void unHighlight(){
		this.isHighlighted = false;
	}
	
	public boolean isVisited(){
		return this.visited;
	}
	
	public void visit(){
		this.visited = true;
	}
	
	public void unVisit(){
		this.visited = false;
	}
	
	public int getCount(){
		return this.count;
	}
	
	
	
	public void setCount(int count){
		this.count = count;
	}
	
	public void setReachBack(int count){
		this.reachBack = count;
	}
	
	public int getReachBack(){
		return this.reachBack;
	}
	
	public void setAp(){
		this.isAP = true;
	}
	
	public void removeAp(){
		this.isAP = false;
	}


	
	public Set<Node> getNeighbours(){
		Set<Node> neighbours = new HashSet<Node>();
		if(!this.segmentsIn.isEmpty()){
			for(Segment seg : this.segmentsIn){
				neighbours.add(seg.getNode1());
			}
		}
		if(!this.segmentsOut.isEmpty()){
			for(Segment seg : this.segmentsOut){
				neighbours.add(seg.getNode2());
				
			}
			
		}
		
		
		return neighbours;

		
	
		
	
	}
	
	
	
}

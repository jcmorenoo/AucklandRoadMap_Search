import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	
	public Node(int id, Location loc){
		this.id = id;
		this.location = loc;
	}
	
	public void addSegmentIn(Segment segIn){
		this.segmentsIn.add(segIn);
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
	
	
}

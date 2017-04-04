/**
 * Segment is a part of the road. It connects two nodes. 
 * It does not contain intersection in the middle.
 * May not always be a straight line.
 * 
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Segment {
	
	private double length;
	private ArrayList<Location> location;
	private Node node1;
	private Node node2;
	private Road road;
	private boolean highlighted = false;
	
	
	public Segment(Road road, double length, Node node1, Node node2, ArrayList<Location> loc){
		this.road = road;
		this.length = length;
		this.node1 = node1;
		this.node2 = node2;
		this.location = new ArrayList<Location>();
		this.location = loc;
		
		
	}
	
	/*
	 * Draws the segment
	 */
	public void draw(Graphics g, Location origin, double scale){
		g.setColor(Color.LIGHT_GRAY);
		if(this.highlighted){
			g.setColor(Color.MAGENTA);
		}
		ArrayList<Point> points = new ArrayList<Point>();
		for(Location loc : this.location){
			points.add(loc.asPoint(origin, scale));
		}
		
		int count = 0;
		while(count<points.size() - 1){
			g.drawLine(points.get(count).x, points.get(count).y, points.get(count+1).x, points.get(count+1).y);
			count = count + 1;
		}
		
	}
	
	/*
	 * Highligh selected segment
	 */
	public void highlight(){
		this.highlighted = true;
	}
	
	public void unHighlight(){
		this.highlighted = false;
	}
	
	public ArrayList<Location> getLocation(){
		return this.location;
	}
	
	public int getRoadId(){
		return this.road.getId();
	}
	
	public Node getNode1(){
		return this.node1;
	}
	
	public Node getNode2(){
		return this.node2;
	}
	
	
	
	
	
		
}

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AucklandRoadMap extends GUI {
	
	//collections
	private Map<Integer, Road> roads = new HashMap<Integer, Road>();
	private Set<Segment> segments = new HashSet<Segment>();
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	private ArrayList<Misc> polygons = new ArrayList<Misc>();
	private Trie trie = null;
	
	
	private Node selectedNode = null;
	private Road selectedRoad = null;
	private Set<Road> selectedRoads = null;
	private ArrayList<Segment> selectedSegments = new ArrayList<Segment>();
	private Set<Road> roadsConnected = new HashSet<Road>();
	
	public static double max = Double.NEGATIVE_INFINITY;
	public static double min = Double.POSITIVE_INFINITY;
	public static double minLon = Double.POSITIVE_INFINITY;
	public static double maxLon = Double.NEGATIVE_INFINITY;
	
	public static Location origin;
	public static double WINDOW_SIZE = 400;
	public static double scale;
	
	
	
	/*
	 * Constructor. Open file loader.
	 * Set all data structures to new.
	 */
	public AucklandRoadMap(){
		this.selectedRoads = new HashSet<Road>();
		this.selectedSegments = new ArrayList<Segment>();
		this.roadsConnected = new HashSet<Road>();
		this.max = Double.NEGATIVE_INFINITY;
		this.min = Double.POSITIVE_INFINITY;
		this.minLon = Double.POSITIVE_INFINITY;
		this.maxLon = Double.NEGATIVE_INFINITY;
		this.trie = null;
	}
	

	/**
	 * add road names to the trie
	 */
	private void addTrie(){
		this.trie = new Trie();
		
		for(Map.Entry<Integer, Road> entry : this.roads.entrySet()){
			
			Road r = entry.getValue();
			
			this.trie.addWord(r.getName());

		}
	}
	
	
	
	@Override
	protected void redraw(Graphics g) {
		// TODO Auto-generated method stub
		
		for(Misc poly : this.polygons){
			poly.draw(g, this.origin, this.scale);
		}
		
		for(Map.Entry<Integer, Node> node : this.nodes.entrySet()){
			Node n = node.getValue();
			n.unHighlight();
			if(n == this.selectedNode){
				n.highlight();
			}
			n.draw(g,this.origin, this.scale);
			//System.out.println(n.getLocation().x + " " + n.getLocation().y);
		}
		
		for(Segment seg : this.segments){
			seg.unHighlight();
			if(this.selectedSegments.contains(seg)){
				seg.highlight();
			}
			seg.draw(g, this.origin, this.scale);
		}
		
		
		
		
		
		
	}

	@Override
	protected void onClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
		Point p = new Point();
		p.setLocation(e.getX(), e.getY());
		Location mouseLoc = Location.newFromPoint(p, this.origin, this.scale);
		for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()){
			Node n = entry.getValue();
			if(n.getLocation().isClose(mouseLoc, 10/this.scale)){
				System.out.println("Node ID: " + entry.getKey());
				this.selectedNode = n;
				this.roadsConnected = new HashSet<Road>();
				Set<Segment> segsIn = n.getSegIn();
				Set<Segment> segsOut = n.getSegOut();
				
				for(Segment seg : segsIn){
					
					if(!this.roadsConnected.contains(this.roads.get(seg.getRoadId()))){
						this.roadsConnected.add(this.roads.get(seg.getRoadId()));
					}
				}
				for(Segment segOut : segsOut){
					if(!this.roadsConnected.contains(this.roads.get(segOut.getRoadId()))){
						this.roadsConnected.add(this.roads.get(segOut.getRoadId()));
					}
				}
				for(Road r : roadsConnected){
					//System.out.println(r.getName());
				}
				break;
				
			}
		}
		if(this.selectedNode != null){
			getTextOutputArea().setText("Selected Intersection ID: " + this.selectedNode.getId());
			if(!this.roadsConnected.isEmpty()){
				getTextOutputArea().append("\n" + "Roads Connected to this Intersection:");
				for(Road r : this.roadsConnected){
					getTextOutputArea().append("\n" + r.getName());
				}
			}
			
		}
		//this.redraw();
		
		
		
	}

	@Override
	protected void onSearch() {
		// TODO Auto-generated method stub
		
		String text = getSearchBox().getText();
		this.selectedRoads = new HashSet<Road>();
		this.selectedSegments = new ArrayList<Segment>();
		Set<String> matchedRoads = new HashSet<String>();
		matchedRoads = this.trie.getAll(text);
		
		
	
		if(matchedRoads!=null){
			getTextOutputArea().setText("Suggested Roads:");	
			for(String matched : matchedRoads){
				for(Map.Entry<Integer, Road> entry : this.roads.entrySet()){
					if(entry.getValue().getName().equals(matched)){
						this.selectedRoads.add(entry.getValue());
						for(Segment seg : entry.getValue().getSegments()){
							this.selectedSegments.add(seg);
						}
					}
				}
				getTextOutputArea().append("\n" + matched);
				System.out.println(matched);
			}
		}

		
	}
	
	

	@Override
	protected void onMove(Move m) {
		// TODO Auto-generated method stub
		Point p = new Point();
		
		Point midPoint = new Point();
		midPoint.setLocation(this.WINDOW_SIZE/2, this.WINDOW_SIZE/2);
		
		
		if(m == Move.ZOOM_IN){
			
			
			Location oldMid = Location.newFromPoint(midPoint, this.origin, this.scale);
			
			Location middle = Location.newFromPoint(midPoint, this.origin, this.scale*1.5);
			double diffX = -(middle.x - oldMid.x);
			double diffY = -(middle.y - oldMid.y);
			this.origin = this.origin.moveBy(diffX, diffY);
			this.scale = this.scale * 1.5;

			
			
		}
		
		if(m == Move.ZOOM_OUT){
			
			Location oldMid = Location.newFromPoint(midPoint, this.origin, this.scale);
			
			Location middle = Location.newFromPoint(midPoint, this.origin, this.scale/1.5);
			double diffX = -(middle.x - oldMid.x);
			double diffY = -(middle.y - oldMid.y);
			this.origin = this.origin.moveBy(diffX, diffY);
			this.scale = this.scale / 1.5;
		}
		
		if(m == Move.EAST){
			this.origin = this.origin.moveBy(20/scale, 0);
		}
		
		if(m == Move.WEST){
			this.origin = this.origin.moveBy(-20/scale, 0);
		}
		
		if(m == Move.NORTH){
			this.origin = this.origin.moveBy(0, 20/scale);
		}
		
		if(m == Move.SOUTH){
			this.origin = this.origin.moveBy(0, -20/scale);
		}
		
		//this.redraw();
		
		
	}
	
	

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		// TODO Auto-generated method stub
		this.roads = new HashMap<Integer,Road>();
		this.segments = new HashSet<Segment>();
		this.nodes = new HashMap<Integer, Node>();
		this.max = Double.NEGATIVE_INFINITY;
		this.min = Double.POSITIVE_INFINITY;
		this.minLon = Double.POSITIVE_INFINITY;
		this.maxLon = Double.NEGATIVE_INFINITY;
		this.polygons = new ArrayList<Misc>();
		
		if(nodes != null){
			try {
				BufferedReader nodeFile = new BufferedReader(new FileReader(nodes));
				String line;
				
				try {
					while((line = nodeFile.readLine()) != null){
						String[] values = line.split("\t");
						int id = Integer.parseInt(values[0]);
						double lat = Double.parseDouble(values[1]);
						double lon = Double.parseDouble(values[2]);
						//then create node
						
						
						Location loc = Location.newFromLatLon(lat,lon);
						//System.out.println(loc.toString());
						
						if(lat > this.max){
							this.max = lat;
						}
						
						if(lat < this.min){
							this.min = lat;
						}
						
						if(lon > this.maxLon){
							this.maxLon = lon;
						}
						if(lon < this.minLon){
							this.minLon = lon;
						}
						
						
						Node n = new Node(id, loc);
						this.nodes.put(id, n);
						
							
					}
					
					this.origin = Location.newFromLatLon(this.max, this.minLon);
					//this.scale = Math.min((this.WINDOW_SIZE/(this.max-this.min))/111.0, (this.WINDOW_SIZE/(this.maxLon-this.minLon))/88.649);
					this.scale = (this.WINDOW_SIZE/(this.max-this.min))/111.0;
					//this.scale = Math.min(((this.WINDOW_SIZE/(this.max-this.min))/111),((this.WINDOW_SIZE/(this.maxLon-this.minLon))/88.649));
				
					nodeFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		if(roads != null){
			try {
				BufferedReader roadFile = new BufferedReader(new FileReader(roads));
				
				String line;
				try {
					while((line = roadFile.readLine()) != null){
						String[] values = line.split("\t");
						if(!values[0].equals("roadid")){
							int id = Integer.parseInt(values[0]);
							int type = Integer.parseInt(values[1]);
							String name = values[2];
							String city = values[3];
							int oneWay = Integer.parseInt(values[4]);
							int speed = Integer.parseInt(values[5]);
							int roadClass = Integer.parseInt(values[6]);
							int noCar = Integer.parseInt(values[7]);
							int noPede = Integer.parseInt(values[8]);
							int noBicy = Integer.parseInt(values[9]);
							boolean notForCar = false;
							boolean notForPede = false;
							boolean notForBicy = false;
							boolean oneWayStreet = false;
							if(noCar == 1){
								notForCar = true;
							}
							if(noPede == 1){
								notForPede = true;
							}
							if(noBicy == 1){
								notForBicy = true;
							}
							if(oneWay == 1){
								oneWayStreet = true;
							}
							
							// then create new road
							Road r = new Road(id, type, name, city, oneWayStreet, speed, roadClass, notForCar, notForPede, notForBicy);
							this.roads.put(id, r);
							
							
							
						}
						
					}
					
					this.addTrie();
					
					roadFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(segments != null){
			try {
				BufferedReader segmentFile = new BufferedReader(new FileReader(segments));
				
				String line;
				
				while((line = segmentFile.readLine()) != null){
					String[] values = line.split("\t");
					if(!values[0].equals("roadID")){
						int id = Integer.parseInt(values[0]);
						double length = Double.parseDouble(values[1]);
						int node1Id = Integer.parseInt(values[2]);
						int node2Id = Integer.parseInt(values[3]);
						int coordinates = (values.length - 4)/2;
						ArrayList<Location> location = new ArrayList<Location>();
						int count = 0;
						while(count < coordinates){
							Double lat = Double.parseDouble(values[4 + (count * 2)]);
							Double lon = Double.parseDouble(values[4 + (count * 2) + 1]);
							count = count + 1;
							location.add(Location.newFromLatLon(lat,lon));
						}
						Node node1 = this.nodes.get(node1Id);
						Node node2 = this.nodes.get(node2Id);
						
						Road r = this.roads.get(id);
						Segment seg = new Segment(r, length, node1, node2, location);
						this.segments.add(seg);
						node1.addSegmentOut(seg);
						node2.addSegmentIn(seg);
						r.addSegment(seg);
						if(!r.isOneWay()){
							//create new segment
							Segment seg2 = new Segment(r, length, node2, node1, location);
							node1.addSegmentIn(seg2);
							node2.addSegmentOut(seg2);
							this.segments.add(seg);
							r.addSegment(seg2);
						}
						
					}
					
				}
				
				segmentFile.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(polygons != null){
			try {
				BufferedReader polygonsFile = new BufferedReader(new FileReader(polygons));
				String line;
				Misc polygon = null;
				
				
				ArrayList<Location> coordinates = new ArrayList<Location>();
				try {
					while((line = polygonsFile.readLine()) != null){
						String[] values = line.split("=");
						if(values[0].equals("[POLYGON]")){
							//new polygon
							polygon = new Misc();
							
						}
						else if(values[0].equals("Type")){
							//set  type
							
							String type = values[1];
							
							String val[] = type.split("x");
							
							int decFromHex = Integer.parseInt(val[1], 16);
							
							polygon.setType(decFromHex);
							
							//polygon.setType(Integer.parseInt(type, 16));
							
						}
						else if(values[0].equals("Label")){
							//set label
							//polygon.setLabel(values[1]);
							
							polygon.setLabel(values[1]);
						}
						else if(values[0].equals("EndLevel")){
							//set endlevel
							polygon.setEndLevel(Integer.parseInt(values[1]));
							
						}
						else if(values[0].equals("CityIdx")){
							//set city
							polygon.setCityId(Integer.parseInt(values[1]));
							
						}
						else if(values[0].equals("Data0")){
							//set coordinates
							coordinates = new ArrayList<Location>();
							String[] coordinatesArray = values[1].split("(\\),\\()|\\(|,|\\)");
							
							int size = coordinatesArray.length;
							int count = 0;
							while(count < size - 1 ){
								double lat = Double.parseDouble(coordinatesArray[(count) + 1]);
								double lon = Double.parseDouble(coordinatesArray[(count) + 2]);
								count = count + 2;
								Location loc = Location.newFromLatLon(lat, lon);
								coordinates.add(loc);
								
							}
							
							PolygonCoordinates pc = new PolygonCoordinates(coordinates);
							polygon.getCoordinates().add(pc);
						}
						else if(values[0].equals("Data1")){
							//set coordinates
							coordinates = new ArrayList<Location>();
							String[] coordinatesArray = values[1].split("(\\),\\()|\\(|,|\\)");
							
							int size = coordinatesArray.length;
							int count = 0;
							while(count < size - 1 ){
								double lat = Double.parseDouble(coordinatesArray[(count) + 1]);
								double lon = Double.parseDouble(coordinatesArray[(count) + 2]);
								count = count + 2;
								Location loc = Location.newFromLatLon(lat, lon);
								coordinates.add(loc);
								
							}
							
							PolygonCoordinates pc = new PolygonCoordinates(coordinates);
							polygon.getCoordinates().add(pc);
						}
						else if(values[0].equals("[END]")){
							this.polygons.add(polygon);
						}
						
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.redraw();
		
		
		
	}
	
	public static void main(String[] args){
		new AucklandRoadMap();
		
	}

	/**
	 * when user presses enter and tries to search for an exact name
	 */
	@Override
	protected void exactSearch() {
		// TODO Auto-generated method stub

		
		String text = getSearchBox().getText();
		
		if(this.trie.contains(text)){
			this.selectedRoads = new HashSet<Road>();
			this.selectedSegments = new ArrayList<Segment>();
			for(Map.Entry<Integer, Road> entry : this.roads.entrySet()){
				Road r = entry.getValue();
				if(r.getName().equals(text)){
					//this.selectedRoad = r;
					this.selectedRoads.add(r);
					for(Segment seg : r.getSegments()){
						this.selectedSegments.add(seg);
					}
					
					getTextOutputArea().setText("Road Selected: " + r.getName());
					getTextOutputArea().append("\nRoad ID: " + r.getId());
				}
			}
		}

		this.redraw();
		
	}

	@Override

	/**
	 * zoom in and zoom out on mouse scroll
	 */
	protected void onMouseWheelAction(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
		Point p = new Point();
		
		Point midPoint = new Point();
		midPoint.setLocation(this.WINDOW_SIZE/2, this.WINDOW_SIZE/2);
		if(e.getWheelRotation() < 0){
			Location oldMid = Location.newFromPoint(midPoint, this.origin, this.scale);
			
			Location middle = Location.newFromPoint(midPoint, this.origin, this.scale*1.1);
			double diffX = -(middle.x - oldMid.x);
			double diffY = -(middle.y - oldMid.y);
			this.origin = this.origin.moveBy(diffX, diffY);
			this.scale = this.scale * 1.1;
		}
		else if(e.getWheelRotation() > 0){
			Location oldMid = Location.newFromPoint(midPoint, this.origin, this.scale);
			
			Location middle = Location.newFromPoint(midPoint, this.origin, this.scale/1.1);
			double diffX = -(middle.x - oldMid.x);
			double diffY = -(middle.y - oldMid.y);
			this.origin = this.origin.moveBy(diffX, diffY);
			this.scale = this.scale / 1.1;
		}
		
		
	}

	
	/**
	 * Moves the origin when map is dragged
	 */
	@Override
	protected void onDrag(int x, int y) {
		this.origin = this.origin.moveBy(x/this.scale, y/this.scale);
		
		// TODO Auto-generated method stub
		
	}

}

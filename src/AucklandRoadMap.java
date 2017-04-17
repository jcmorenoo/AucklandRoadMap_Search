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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AucklandRoadMap extends GUI {
	
	//collections
	private Map<Integer, Road> roads = new HashMap<Integer, Road>();
	private Set<Segment> segments = new HashSet<Segment>();
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	private ArrayList<Misc> polygons = new ArrayList<Misc>();
	private Trie trie = null;
	
	
	
	private Fringe fringe;
	
	private SearchNode finalNode;

	private boolean goal = false;
	private double costFromStart;
	private double costToGoal;
	private double totalCost;
	
	
	private int numSubtrees;

	private Node selectedNode = null;
	private Road selectedRoad = null;

	private List<Segment> segsRoute;
	private List<Road> roadsRoute;
	private List<Segment> startingSegIn;
	private List<Segment> startingSegOut;
	private Set<Road> selectedRoads = null;
	private ArrayList<Segment> selectedSegments = new ArrayList<Segment>();
	private Set<Road> roadsConnected = new HashSet<Road>();

	private Node targetNode = null;

	public static double max = Double.NEGATIVE_INFINITY;
	public static double min = Double.POSITIVE_INFINITY;
	public static double minLon = Double.POSITIVE_INFINITY;
	public static double maxLon = Double.NEGATIVE_INFINITY;

	public static Location origin;
	public static double WINDOW_SIZE = 400;
	public static double scale;
	
	// articulation points field
	private Set<Node> articulationPoints;
	private boolean artPtsToggle = false;



	/*
	 * Constructor. Open file loader.
	 * Set all data structures to new.
	 */
	public AucklandRoadMap(){
		this.selectedRoads = new HashSet<Road>();
		this.selectedSegments = new ArrayList<Segment>();
		this.startingSegIn = new ArrayList<Segment>();
		this.startingSegOut = new ArrayList<Segment>();
		this.roadsConnected = new HashSet<Road>();
		this.max = Double.NEGATIVE_INFINITY;
		this.min = Double.POSITIVE_INFINITY;
		this.minLon = Double.POSITIVE_INFINITY;
		this.maxLon = Double.NEGATIVE_INFINITY;
		this.trie = null;
		this.segsRoute = new ArrayList<Segment>();
		this.articulationPoints = new HashSet<Node>();
		
		this.fringe = new Fringe();

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

	/**
	 * Loads the restricions.tab file and applies the restrictions
	 */
	private void loadRestrictions(){

		//NodeId - RoadId - NodeId - RoadId - NodeId
		//the first is the node contains the first segment which is connected to the intersection
		//the roadId is where the segment is contained.
		//the middle node is the intersection
		//the next roadId is the road which you cannot turn from the previous road
		//the last nodeId contains the segment connecting intersection

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
			n.removeAp();
			if(n == this.selectedNode | n == this.targetNode){
				n.highlight();
			}
			n.draw(g,this.origin, this.scale);
			//System.out.println(n.getLocation().x + " " + n.getLocation().y);
		}
		if(this.artPtsToggle){
			for(Node n : this.articulationPoints){
				n.setAp();
				n.draw(g,  this.origin, this.scale);
			}
		}


		
		for(Segment seg : this.segments){
			seg.unHighlight();
			if(this.selectedSegments.contains(seg)){
				seg.highlight();
			}
			
			if(this.segsRoute.contains(seg)){
				seg.highlightRoute();
			}
			seg.draw(g, this.origin, this.scale);
		}
		
		for(Segment seg : this.segsRoute){
			seg.highlightRoute();
			seg.draw(g, this.origin, this.scale);
		}
		
		
		//System.out.println(this.segsRoute.size());

		





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
				this.selectedSegments = new ArrayList<Segment>();
				System.out.println("Node ID: " + entry.getKey());
				if(!this.goal){
					this.selectedNode = n;
				}
				else{
					this.targetNode = n;
				}
				this.roadsConnected = new HashSet<Road>();
				Set<Segment> segsIn = n.getSegIn();
				Set<Segment> segsOut = n.getSegOut();

				for(Segment seg : segsIn){

					if(!this.roadsConnected.contains(this.roads.get(seg.getRoadId()))){
						this.roadsConnected.add(this.roads.get(seg.getRoadId()));
					}
					if(!this.goal){
						this.startingSegIn.add(seg);
					}
					//for testing
					//this.selectedSegments.add(seg);

				}
				for(Segment segOut : segsOut){
					if(!this.roadsConnected.contains(this.roads.get(segOut.getRoadId()))){
						this.roadsConnected.add(this.roads.get(segOut.getRoadId()));
					}
					if(!this.goal){
						this.startingSegOut.add(segOut);
					}

					//for testing
					//this.selectedSegments.add(segOut);

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
						//if(!r.isOneWay()){
							//create new segment
							Segment seg2 = new Segment(r, length, node2, node1, location);
							node1.addSegmentIn(seg2);
							node2.addSegmentOut(seg2);
							this.segments.add(seg);
							r.addSegment(seg2);
						//}

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


	@Override
	protected void setStarting() {
		// TODO Auto-generated method stub
		this.goal = false;

	}


	@Override
	protected void setGoal() {
		// TODO Auto-generated method stub
		this.goal = true;

	}


	@Override
	protected void findShortestPath() {
		System.out.println("Finding shortest path");
		this.roadsRoute = new ArrayList<Road>();
		this.segsRoute = new ArrayList<Segment>();
		// TODO Auto-generated method stub
		//set all nodes to not visited
		//set all nodes pathfrom = null
		
		if(this.targetNode != null && this.selectedNode != null){
			for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()){
				Node n = entry.getValue();
				n.unVisit();
			}
			this.fringe = new Fringe();
			
			this.fringe.enqueue(this.selectedNode, null, 0, estimate(this.selectedNode, this.targetNode));
			
			while(!this.fringe.isEmpty()){
			
				SearchNode sn = this.fringe.dequeue();
		
				Node currentNode = sn.getNode();
				if(!currentNode.isVisited()){
					currentNode.visit();
					if(currentNode == this.targetNode){
						
						findRoute(sn);
						
						
						break;
						//exit
					}
					
					for(Node neigh : currentNode.getNeighbours()){
						if(!neigh.isVisited()){
							this.fringe.enqueue(neigh, sn, sn.getCostFromStart() + estimate(currentNode,neigh), estimate(currentNode,neigh) + estimate(this.targetNode, neigh));
						}
						
					}

				}
			}
			
			
		}
		
		this.getPath();
		
		
		
		

	}


	private void findRoute(SearchNode sn) {
		// TODO Auto-generated method stub
		
		Node cur = sn.getNode();
		SearchNode from = sn.getFrom();
		
		
		Segment path = sn.getPath();
		if(path != null){
			this.segsRoute.add(path);
			if(!this.roadsRoute.contains(path.getRoad())){
				this.roadsRoute.add(path.getRoad());
			}
			
		}
		if(from != null){
			findRoute(from);
			
		}
		
		
		
		this.redraw();

		
		
		
	}

	private double estimate(Node start, Node target) {
		// TODO Auto-generated method stub
		//euclidean??
		
		Location locStart = start.getLocation();
		Location locTarget = target.getLocation();
		
		double distance = locStart.distance(locTarget);
		
		return distance;
	}
	
	private double estimateFastest(Node start, Node target){
		//use d/v
		
		Location locStart = start.getLocation();
		Location locTarget = target.getLocation();
		
		double distance = locStart.distance(locTarget);
		
		return distance/60;
	}

	
	//uses time as cost.. so less time , smaller cost
	@Override
	protected void findFastestPath() {
		// TODO Auto-generated method stub
		System.out.println("Finding fastest path");
		this.roadsRoute = new ArrayList<Road>();
		this.segsRoute = new ArrayList<Segment>();
		// TODO Auto-generated method stub
		//set all nodes to not visited
		//set all nodes pathfrom = null
		
		if(this.targetNode != null && this.selectedNode != null){
			for(Map.Entry<Integer, Node> entry : this.nodes.entrySet()){
				Node n = entry.getValue();
				n.unVisit();
			}
			this.fringe = new Fringe();
			
			this.fringe.enqueue(this.selectedNode, null, 0, estimateFastest(this.selectedNode, this.targetNode));
			
			while(!this.fringe.isEmpty()){
			
				SearchNode sn = this.fringe.dequeue();
		
				Node currentNode = sn.getNode();
				if(!currentNode.isVisited()){
					currentNode.visit();
					if(currentNode == this.targetNode){
						
						findRoute(sn);
						
						
						break;
						//exit
					}
					
					
					for(Segment s : currentNode.getSegOut()){
						Node neigh = null;
						Road r = s.getRoad();
						double speed = r.getSpeedLimit();
						if(s.getNode1() == currentNode){
							neigh = s.getNode2();
						}
						else{
							neigh = s.getNode1();
						}
						
						if(!neigh.isVisited()){
							
							this.fringe.enqueue(neigh, sn, sn.getCostFromStart() + (s.getLength()/speed), (s.getLength()/speed) + estimateFastest(this.targetNode, neigh));
						}
					}
				}
			}
			
		}
		
		this.getPath();
		
	}
	
	//used to display road names from start to goal
	public void getPath(){
		double length = 0;
		double totLength = 0;
		double totTime = 0;
		if(!this.roadsRoute.isEmpty()){
			getTextOutputArea().setText("Route from start to finish: \n");
			for(int i = this.roadsRoute.size() - 1; i >= 0; i--){
				//first road
				totTime = totTime + (this.segsRoute.get(i).getLength()/this.roadsRoute.get(i).getSpeedLimit());
				
				totLength = totLength + this.segsRoute.get(i).getLength();
				if(i == this.roadsRoute.size() - 1){
					getTextOutputArea().append(this.roadsRoute.get(i).getName() + " : " + this.segsRoute.get(i).getLength() +"km \n");
				}
				else{
					String roadName = this.roadsRoute.get(i).getName();
					int roadId = this.roadsRoute.get(i).getId();
					//if its not similar to the previous road
					if(!roadName.equalsIgnoreCase(this.roadsRoute.get(i + 1).getName())){
						length = this.segsRoute.get(i).getLength();
						getTextOutputArea().append(roadName + " : " +length + "km \n");
						length = 0;
					}
					else{
						length = length + this.segsRoute.get(i).getLength();
					}
				}
			}
			getTextOutputArea().append("Total Length: " + totLength + "km \n");
			getTextOutputArea().append("Total Time: " + totTime + "hours");
		}
		
		
	}



	//when button is pressed. find articulation points
	@Override
	protected void findAp() {
		// TODO Auto-generated method stub
		//display articulation points if toggle is true
		this.artPtsToggle = !this.artPtsToggle;
		
		

			// find articulation points
			this.articulationPoints = ArticulationPointHelper.findArtPts(this.nodes.values());
			
		
		redraw();
		getTextOutputArea().setText("Articulation Points: " + this.articulationPoints.size());
		System.out.println("Articulation Points: " + this.articulationPoints.size());
		
		
	}
	
	
	

	
	

}

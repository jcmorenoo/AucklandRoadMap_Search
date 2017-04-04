import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author julian
 * Class which represents a Road.
 * A road contains segments and may contain multiple intersections
 *
 */
public class Road {
	
	private int id;
	private int type;
	private String name;
	private String city;
	private boolean oneWay;
	private int speedLimit;
	private int roadClass;
	private boolean notForCar;
	private boolean notForPede;
	private boolean notForBicy;
	private ArrayList<Segment> segments;
	
	public Road(int id, int type, String name, String city, boolean oneWay, int speedLimit, int roadClass, boolean notForCar, boolean notForPede, boolean notForBicy){
		this.id = id;
		this.type = type;
		this.name = name;
		this.city = city;
		this.oneWay = oneWay;
		this.speedLimit = speedLimit;
		this.roadClass = roadClass;
		this.notForCar = notForCar;
		this.notForPede = notForPede;
		this.notForBicy = notForBicy;
		this.segments = new ArrayList<Segment>();
	}
	
	public void addSegment(Segment seg){
		this.segments.add(seg);
	}
	
	public ArrayList<Segment> getSegments(){
		return this.segments;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getType(){
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public boolean isOneWay(){
		return this.oneWay;
	}
	
	public int getRoadClass(){
		return this.roadClass;
	}
	
	public boolean isNotForCar(){
		return this.notForCar;
	}
	
	public boolean isNotForPede(){
		return this.notForPede;
	}
	
	public boolean isNotForBicy(){
		return this.notForBicy;
	}
	
}

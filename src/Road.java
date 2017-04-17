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
	
	public double getSpeedLimit(){
//		   0 = 5km/h    
//				 1 = 20km/h   
//				   2 = 40km/h   
//				   3 = 60km/h   
//				   4 = 80km/h   
//				   5 = 100km/h  
//				   6 = 110km/h  
//				   7 = no limit
		
		if(this.speedLimit == 0){
			return 5;
		}
		else if(this.speedLimit == 1){
			return 20;
		}
		else if(this.speedLimit == 2){
					return 40;
				}
		else if(this.speedLimit == 3){
			return 60;
		}
		else if(this.speedLimit == 4){
			return 80;
		}
		else if(this.speedLimit == 5){
			return 100;
		}
		else if(this.speedLimit == 6){
			return 110;
		}
		else{
			return 110;
		}
		
		
	}
	
	
}

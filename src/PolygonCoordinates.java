import java.util.ArrayList;
/**
 * 
 * @author julian
 * 
 * Class representing coordinates of a polygon
 *
 */
public class PolygonCoordinates {

	private ArrayList<Location> coordinates;
	
	public PolygonCoordinates(ArrayList<Location> coordinates){
		this.coordinates = new ArrayList<Location>();
		this.coordinates = coordinates;
	}

	public ArrayList<Location> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Location> coordinates) {
		this.coordinates = coordinates;
	}
	
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * 
 * @author julian
 * Class representing the additional information such as buildings, parks, airports, etc..
 */
public class Misc {

	private ArrayList<PolygonCoordinates> coordinates;
	
	private int type;
	private String label;
	private int endLevel;
	private int cityId;
	
	
	public int getCityId() {
		return cityId;
	}


	public void setCityId(int cityId) {
		this.cityId = cityId;
	}


	public Misc(){
		this.coordinates = new ArrayList<PolygonCoordinates>();
	}


	public ArrayList<PolygonCoordinates> getCoordinates() {
		return coordinates;
	}


	public void setCoordinates(ArrayList<PolygonCoordinates> coordinates) {
		this.coordinates = coordinates;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public int getEndLevel() {
		return endLevel;
	}


	public void setEndLevel(int endLevel) {
		this.endLevel = endLevel;
	}
	
	public void draw(Graphics g, Location origin, double scale){
		
		
		
		g.setColor(Color.BLUE);
		if(this.type == 2){
			//suburb
			g.setColor(Color.GREEN);
		}
		else if(this.type == 5){
			//shopping center
			g.setColor(Color.LIGHT_GRAY);
		}
		else if(this.type == 7){
			//airport
			g.setColor(Color.LIGHT_GRAY);
		}
		else if(this.type == 8){
			//shopping
			g.setColor(Color.LIGHT_GRAY);
		}
		else if(this.type == 10){
			//uni
			g.setColor(Color.GRAY);
		}
		else if(this.type == 11){
			//Hospital
			g.setColor(Color.darkGray);
			
		}
		else if(this.type == 14){
			//Airfield
			g.setColor(Color.LIGHT_GRAY);
			
		}
		else if(this.type == 19){
			//Public Building
			g.setColor(Color.DARK_GRAY);
		}
		else if(this.type == 22){
			//Forest
			g.setColor(Color.GREEN);
		}
		else if(this.type == 23){
			//park
			g.setColor(Color.GREEN);
			
		}
		else if(this.type == 24){
			//golf course
			g.setColor(Color.GREEN);
			
		}
		else if(this.type == 25){
			//sports court
			g.setColor(Color.DARK_GRAY);
			
		}
		else if(this.type == 26){
			//cemetery
			g.setColor(Color.GREEN);
		}
		else if(this.type == 30){
			//reserve
			g.setColor(Color.GREEN);
		}
		else if(this.type == 40){
			//sea
			g.setColor(Color.BLUE);
		}
		else if(this.type == 60){
			//lake
			g.setColor(Color.cyan);
		}
		else if(this.type == 62){
			//lake
			g.setColor(Color.CYAN);
		}
		else if(this.type == 64){
			//red dot in by beach
			g.setColor(Color.RED);
		}
		else if(this.type == 65){
			//forest
			g.setColor(Color.GREEN);
		}
		else if(this.type == 69){
			//lake
			g.setColor(Color.CYAN);
		}
		else if(this.type == 71){
			//River
			g.setColor(Color.BLUE);
		}
		else if(this.type == 72){
			//river
			g.setColor(Color.BLUE);
		}
		else if(this.type == 80){
			g.setColor(Color.GREEN);
		}
		else{
			g.setColor(Color.ORANGE);
		}
		
		
		for(PolygonCoordinates polygonCoordinates : this.coordinates){
			int[] xPoints = new int[polygonCoordinates.getCoordinates().size()];
			int[] yPoints = new int[polygonCoordinates.getCoordinates().size()];
			int count = 0;
			while(count < polygonCoordinates.getCoordinates().size()){
				Point p = polygonCoordinates.getCoordinates().get(count).asPoint(origin, scale);
				xPoints[count] = p.x;
				yPoints[count] = p.y;
				count = count + 1;
			}
			Polygon polygon = new Polygon(xPoints, yPoints, polygonCoordinates.getCoordinates().size());
			g.fillPolygon(polygon);
		}
		
		
		
		
	}
	
	
	
	
}

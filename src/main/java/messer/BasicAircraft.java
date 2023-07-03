package messer;

import java.util.ArrayList;
import java.util.Date;

public class BasicAircraft {
	private String icao;
	private String operator;
	private Date posTime;
	private Coordinate coordinate;
	private Double speed;
	private Double trak;

	
	//TODO: Create constructor
	public BasicAircraft (String icao, String operator, Date posTime, Coordinate coordinate, double speed, double trak) {
		this.icao = icao;
		this.operator = operator;
		this.posTime = posTime;
		this.coordinate = coordinate;
		this.speed = speed;
		this.trak = trak;
	}

	//TODO: Create relevant getter methods
	public String getIcao() {
		return icao;
	}

	public String getOperator() {
		return operator;
	}

	public Date getPosTime() {
		return posTime;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public double getSpeed() {
		return speed;
	}

	public double getTrak() {
		return trak;
	}

	//TODO: Lab 4-6 return attribute names and values for table
	public static ArrayList<String> getAttributesNames()
	{
		ArrayList<String> attributes = new ArrayList<String>();

		//DONE: get titles of columns
		attributes.add("icao");
		attributes.add("operator");
		attributes.add("posTime");
		attributes.add("coordinate");
		attributes.add("speed");
		attributes.add("trak");
		
		return attributes;
	}

	public static ArrayList<Object> getAttributesValues(BasicAircraft ac)
	{
		ArrayList<Object> attributes = new ArrayList<Object>();
		
		//DONE: get values of one plane
		attributes.add(ac.icao);
		attributes.add(ac.operator);
		attributes.add(ac.posTime);
		attributes.add(ac.coordinate);
		attributes.add(ac.speed);
		attributes.add(ac.speed);

		return attributes;
	}

	//DONE: Overwrite toString() method to print fields
	@Override
	public String toString() {
		return "BasicAircraft [icao=" + icao + ", operator=" + operator + ", posTime=" + posTime
				+ ", coordinate=" + coordinate + ", speed=" + speed + ", trak =" + trak + "]";
	}

}

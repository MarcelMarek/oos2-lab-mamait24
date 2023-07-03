package senser;

import observer.SimpleObservable;
import org.json.JSONArray;

public class AircraftSentence extends SimpleObservable<JSONArray>
{
	String aircraft;
	JSONArray aircraftJson;

	//TODO: Create constructor
	public AircraftSentence() {
		this.aircraftJson= new JSONArray();
	}

	public AircraftSentence(String name) {
		this.aircraft = name;
	}

	public AircraftSentence(JSONArray name) {
		this.aircraftJson= name;
	}

	//TODO: Create relevant getter methods
	public String getAircraftSentence() {
		return aircraft;
	}

	public JSONArray getAircraftSentenceJson() {
		return aircraftJson;
	}
	
	//TODO: Overwrite toString() method to print our relevant fields
	@Override
	public String toString() {
		return aircraft.toString();
	}

	public void setAircraftSentence(JSONArray aircraft) {
		this.aircraftJson = aircraft;
		setChanged();
		notifyObservers(aircraft);
	}

}

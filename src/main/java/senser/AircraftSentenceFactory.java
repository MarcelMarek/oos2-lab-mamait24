package senser;

import org.json.JSONArray;

import java.util.ArrayList;

public class AircraftSentenceFactory
{
	public AircraftSentenceFactory(String str) 	{

		AircraftSentence Aircraft = new AircraftSentence(str);
	}
	public ArrayList<AircraftSentence> fromAircraftJson(JSONArray Array)
	{	
		ArrayList<AircraftSentence> aircraftList = new ArrayList<AircraftSentence>();


		//TODO: Get distinct aircrafts from the jsonAircraftList string
		for(int i = 0; i < Array.length(); i++) {
			JSONArray tempJson = (JSONArray)Array.get(i);
			AircraftSentence tempSentence = new AircraftSentence();
			tempSentence.setAircraftSentence(tempJson);
			aircraftList.add(tempSentence);
		}

		return aircraftList;
	}
}

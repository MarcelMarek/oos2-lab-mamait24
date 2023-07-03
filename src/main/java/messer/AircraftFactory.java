package messer;

import org.json.JSONArray;
import org.json.JSONException;
import senser.AircraftSentence;

import java.util.ArrayList;
import java.util.Date;

public class AircraftFactory {

	public BasicAircraft fromAircraftSentence ( AircraftSentence sentence ) {
		JSONArray aircraft = sentence.getAircraftSentenceJson();
		String icao = null;
		String operator = null;
		Date posTime = null;
		double longitude = 0;
		double latitude = 0;
		double speed = 0;
		double trak = 0;

		// ["3c6dc8", "EWG87U  ", "Germany", 1560500800, 1560500800, 9.6884,   48.8508,
		//   icao      operator              posTime                 longitude latitude
		//   3558.54, false, 197.81, 50.8, 13.98, null, 3695.7, "7716", false, 0]
		//                   speed   trak

		// TODO: Extract attributes from sentence using regex only (no String methods)

		BasicAircraft msg = new BasicAircraft(icao, operator, posTime, new Coordinate(latitude, longitude), speed, trak);
		
		return msg;
	}

	public static final ArrayList<AircraftSentence> createList(JSONArray array) {

		ArrayList<AircraftSentence> aircraftList = new ArrayList<AircraftSentence>();


		for(int i = 0; i < array.length(); i++) {
			Object element = array.get(i);
			if (element instanceof String) {
				try {
					JSONArray tempJson = new JSONArray((String) element);
					AircraftSentence tempSentence = new AircraftSentence();
					tempSentence.setAircraftSentence(tempJson);
					aircraftList.add(tempSentence);
				} catch (JSONException e) {
					// Handle the exception if the string cannot be parsed as a JSON array
				}
			} else if (element instanceof JSONArray) {
				JSONArray tempJson = (JSONArray) element;
				AircraftSentence tempSentence = new AircraftSentence();
				tempSentence.setAircraftSentence(tempJson);
				aircraftList.add(tempSentence);
			}
		}

		return aircraftList;
	}
}

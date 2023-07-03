package senser;

import jsonstream.*;
import messer.AircraftFactory;
import observer.SimpleObservable;
import org.json.JSONArray;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Senser extends SimpleObservable<AircraftSentence> implements Runnable {
	private static boolean lab1 = false;
	PlaneDataServer server;

	public Senser(PlaneDataServer server) {
		this.server = server;
	}

	private JSONArray getSentence() {
		String list = server.getPlaneListAsString();
		JSONArray jsonArr = new JSONArray(list);

		return jsonArr;
	}

	public void run() {
		ArrayList<AircraftSentence> jsonAircraftList;
		JSONArray aircraftList;

		//TODO: Create factory and display object
//		AircraftDisplay display = new AircraftDisplay();
		AircraftFactory factory = new AircraftFactory();

		while (true) {
			JSONArray aircraft;
			ArrayList<AircraftSentence> jsonAircraft;

			while (true) {
				aircraft = getSentence();

				if (aircraft.length() == 0) {
					continue;
				}

				jsonAircraft = factory.createList(aircraft);

				for (int i = 0; i < jsonAircraft.size(); i++) {
					//AircraftDisplay.display(jsonAircraft.get(i));
					setChanged();
					notifyObservers(jsonAircraft.get(i));
				}

			}
		}
	}
}

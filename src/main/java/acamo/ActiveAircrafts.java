package acamo;

import messer.BasicAircraft;
import observer.Observable;
import observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: create hash map and complete all operations
public class ActiveAircrafts implements Observer
{
	private HashMap<String, BasicAircraft> activeAircraft;    	// store the basic aircraft and use its Icao as key
												// replace K and V with the correct class names

	public ActiveAircrafts(){
		activeAircraft = new HashMap< String, BasicAircraft>();
	}

	public synchronized void store(String icao, BasicAircraft ac) {
		activeAircraft.put(icao, ac);
	}

	public synchronized void clear() {
		activeAircraft.clear();
	}

	public synchronized BasicAircraft retrieve(String icao) {
		return activeAircraft.get(icao);
	}

	public synchronized ArrayList<BasicAircraft> values () {
		return new ArrayList<BasicAircraft>(activeAircraft.values());
	}

	public String toString () {
		return activeAircraft.toString();
	}

	@Override
	// TODO: store arg in hashmap using the method above
	public void update(Observable o, Object arg) {
		BasicAircraft basicAircraft = (BasicAircraft) arg;
		activeAircraft.put(basicAircraft.getIcao(),basicAircraft);
	}
}
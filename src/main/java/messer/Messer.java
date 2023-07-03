package messer;

import observer.Observable;
import observer.Observer;
import observer.SimpleObservable;
import senser.AircraftSentence;

import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("deprecation")
public class Messer extends SimpleObservable<BasicAircraft> implements Observer<AircraftSentence>, Runnable
{
	private static boolean lab2 = false;
	private LinkedBlockingQueue<AircraftSentence> queue;
	
	public Messer()
	{
		this.queue = new LinkedBlockingQueue<AircraftSentence>();
	}


	public void run()
	{ 
		//TODO: Create factory and display object
		AircraftDisplay display = new AircraftDisplay();
		AircraftFactory factory = new AircraftFactory();

		while(true)
		{
			AircraftSentence sentence = null;
			try {
				sentence = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicAircraft msg = factory.fromAircraftSentence(sentence);

			// Display the message in Lab 2; disable for other labs
			if (lab2) display.display(msg);

			// Notify all observers
			setChanged();
			notifyObservers(msg);
		}
	}

	@Override
	public void update(Observable<AircraftSentence> observable, AircraftSentence newValue) {
		// TODO Auto-generated method stub
		queue.add(newValue);
	}

}

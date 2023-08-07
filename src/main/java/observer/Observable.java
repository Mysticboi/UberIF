package observer;

import java.util.ArrayList;
import java.util.Collection;

public class Observable {
	private final Collection<Observer> listeObservers;

	public Observable() {
		listeObservers = new ArrayList<>();
	}

	public void addObserver(Observer o){
		if(!listeObservers.contains(o)){
			listeObservers.add(o);
		}
	}

	public void notifyObservers(Object args){
		listeObservers.forEach(observer -> {
			observer.update(this, args);
		});
	}
}

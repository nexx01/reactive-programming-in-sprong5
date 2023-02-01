package observer;

//import java.u/til.Observer;

public interface Subject<T> {

    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObservers(T eveny);
}

package Observer;

public interface ObserverRegister {
    void addObserver(ObserverRegister observer);
    void removeObserver(ObserverRegister observer);
    void notifyObservers(String[] data);
    void update(String[] data);
}

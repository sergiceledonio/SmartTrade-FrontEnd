package Observer;

public interface ObserverUserData {
    void addObserver(ObserverUserData observer);
    void removeObserver(ObserverUserData observer);
    void notifyObservers(String[] data);
    void update(String[] data);
}

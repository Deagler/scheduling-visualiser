package internseason.scheduler.algorithm;

import internseason.scheduler.algorithm.event.AlgorithmEvent;
import internseason.scheduler.algorithm.event.AlgorithmEventListener;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;



public class SystemInformation implements AlgorithmEvent {

        //2 integer pros//schedules queued///schedules explore
    private IntegerProperty schedulesQueued;
    private IntegerProperty schedulesExplored;
    private List<AlgorithmEventListener> listeners;

    public SystemInformation() {
        this.schedulesQueued = new SimpleIntegerProperty();
        this.schedulesExplored = new SimpleIntegerProperty();
        this.listeners = new ArrayList<>();
    }

    public int getSchedulesQueued() {
        return schedulesQueued.get();
    }

    public IntegerProperty schedulesQueuedProperty() {
        return schedulesQueued;
    }

    public int getSchedulesExplored() {
        return schedulesExplored.get();
    }

    public IntegerProperty schedulesExploredProperty() {
        return schedulesExplored;
    }

    public void setSchedulesQueued(int schedulesQueued) {
        this.schedulesQueued.set(schedulesQueued);
    }

    public void setSchedulesExplored(int schedulesExplored) {
        this.schedulesExplored.set(schedulesExplored);
    }


    @Override
    public void fireSchedulesGenerated(Integer parentHashcode, List<Integer> childHashcodes) {
        for (AlgorithmEventListener listener : listeners) {
            listener.schedulesGenerated(parentHashcode, childHashcodes);
        }
    }

    public void addListener(AlgorithmEventListener listener) {
        this.listeners.add(listener);
    }

    public boolean removeListener(AlgorithmEventListener listener) {
        return this.listeners.remove(listener);
    }


}

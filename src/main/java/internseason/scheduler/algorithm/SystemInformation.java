package internseason.scheduler.algorithm;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class SystemInformation implements Observable {

        //2 integer pros//schedules queued///schedules explore
    private IntegerProperty schedulesQueued;
    private IntegerProperty schedulesExplored;
    private List<InvalidationListener> invalidationListenerList;

    public SystemInformation() {
        this.schedulesQueued = new SimpleIntegerProperty();
        this.schedulesExplored = new SimpleIntegerProperty();

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
    public void addListener(InvalidationListener listener) {
        invalidationListenerList.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerList.remove(listener);
    }


}

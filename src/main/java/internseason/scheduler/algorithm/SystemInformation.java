package internseason.scheduler.algorithm;

import internseason.scheduler.algorithm.event.AlgorithmEvent;
import internseason.scheduler.algorithm.event.AlgorithmEventListener;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Class that monitors and observes the behaviour and properties of an algorithm.
 */
public class SystemInformation implements AlgorithmEvent {

    private IntegerProperty schedulesQueued;
    private IntegerProperty schedulesExplored;
    private List<AlgorithmEventListener> listeners;


    public SystemInformation() {
        this.schedulesQueued = new SimpleIntegerProperty();
        this.schedulesExplored = new SimpleIntegerProperty();
        this.listeners = new ArrayList<>();
    }

    /** get schedules that are queued
     * @return queued schedules
     */
    public int getSchedulesQueued() {
        return schedulesQueued.get();
    }

    /** get schedule properties that are queued
     * @return queued schedules properties
     */
    public IntegerProperty schedulesQueuedProperty() {
        return schedulesQueued;
    }

    /** get explored schedules
     * @return explored schedules
     */
    public int getSchedulesExplored() {
        return schedulesExplored.get();
    }

    /** get explored schedules's property used to bind to components in GUI
     * @return interger property of the schedules explored
     */
    public IntegerProperty schedulesExploredProperty() {
        return schedulesExplored;
    }

    /**
     * Updates the number of schedules in the queue and fires a change event on the property.
     * @param schedulesQueued
     */
    public void setSchedulesQueued(int schedulesQueued) {
        this.schedulesQueued.set(schedulesQueued);
    }

    /**
     * Updates the number of schedules explored and fires a change event on the property
     * @param schedulesExplored
     */
    public void setSchedulesExplored(int schedulesExplored) {
        this.schedulesExplored.set(schedulesExplored);
    }


    /**
     * Fires an event to the AlgorithmEventListeners held by SystemInformation
     * indicating new partial schedules have been generated
     * @param parentHashcode A hashcode representing the parent partial schedule
     * @param childHashcodes A set of hashcodes that represent child partial schedules generated from the parent
     */
    @Override
    public void fireSchedulesGenerated(Integer parentHashcode, Set<Integer> childHashcodes) {
        for (AlgorithmEventListener listener : listeners) {
            listener.schedulesGenerated(parentHashcode, childHashcodes);
        }
    }

    /** Add a listener to a component to listen algorithm events
     * @param listener
     */
    public void addListener(AlgorithmEventListener listener) {
        this.listeners.add(listener);
    }

    /** Remove listener from component
     * @param listener
     * @return true if successful removal
     */
    public boolean removeListener(AlgorithmEventListener listener) {
        return this.listeners.remove(listener);
    }


}

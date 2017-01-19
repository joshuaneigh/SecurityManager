package application.tasks;

import javafx.application.Application;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The {@link Task} interface defines the behavior of each {@link Task}. These
 * {@link Task}s will be executed at launch and will carry out each of the
 * {@link Task}s required prior to primary execution of the {@link Application}.
 * {@link Task}s may implement this interface directly, but it is highly
 * recommended that the {@link AbstractTask} is extended instead.
 *
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 07 Dec 2016
 */
public interface Task extends Runnable {

	/**
	 * Starts the {@link Task}.
	 */
	public void start();

	/**
	 * Cancels the {@link Task}.
	 */
	public void cancel();

	/**
	 * Gets the progress as a percentage in the range [0, 1.0].
	 * 
	 * @return the progress of this {@link Task}
	 */
	public float getProgress();

	/**
	 * Gets and returns the {@link SimpleFloatProperty} of its progress.
	 * 
	 * @return the observable progress of this {@link Task}
	 */
	public SimpleFloatProperty progressProperty();

	/**
	 * Gets and returns the {@link String} message of the {@link Task} at its
	 * current state.
	 * 
	 * @return the {@link String} message
	 */
	public String getMessage();

	/**
	 * Gets and returns the {@link SimpleStringProperty} message of the
	 * {@link Task}.
	 * 
	 * @return the observable {@link String} message
	 */
	public SimpleStringProperty messageProperty();

	/**
	 * Gets and returns the current {@link TaskState} of the {@link Task}.
	 * 
	 * @return the current {@link TaskState} of this {@link Task}
	 */
	public TaskState getState();

	/**
	 * Gets and returns the {@link SimpleObjectProperty} of the
	 * {@link TaskState}.
	 * 
	 * @return the observable {@link TaskState}
	 */
	public SimpleObjectProperty<TaskState> stateProperty();

}

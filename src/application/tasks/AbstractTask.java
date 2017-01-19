package application.tasks;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The {@link AbstractTask} class implements {@link Task}.
 *
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 07 Dec 2016
 */
public abstract class AbstractTask implements Task {

	/** The observable {@link TaskState} of this {@link Task}. */
	private SimpleObjectProperty<TaskState> state;
	/** The observable {@link String} message of this {@link Task}. */
	private SimpleStringProperty message;
	/** The observable float progress of this {@link Task}. */
	private SimpleFloatProperty progress;

	/**
	 * Constructs a new {@link AbstractTask}.
	 */
	protected AbstractTask() {
		state = new SimpleObjectProperty<>(TaskState.SCHEDULED);
		message = new SimpleStringProperty("");
		progress = new SimpleFloatProperty(0);
	}

	/**
	 * Sets the progress as a percentage in the range [0, 1.0].
	 * 
	 * @param progress
	 *            the progress to set
	 */
	protected void setProgress(float progress) {
		this.progress.set(progress);
	}

	/**
	 * Sets the {@link String} message of the {@link Task} at the current state.
	 * 
	 * @param message
	 *            the message to set
	 */
	protected void setMessage(String message) {
		this.message.set(message);
	}

	/**
	 * Sets the current {@link TaskState} of the {@link Task}.
	 * 
	 * @param state
	 *            the {@link TaskState} to set
	 */
	protected void setState(TaskState state) {
		this.state.set(state);
	}

	@Override
	public void run() {
		start();
	}
	
	@Override
	public float getProgress() {
		return progress.get();
	}

	@Override
	public SimpleFloatProperty progressProperty() {
		return progress;
	}

	@Override
	public String getMessage() {
		return message.get();
	}

	@Override
	public SimpleStringProperty messageProperty() {
		return message;
	}

	@Override
	public TaskState getState() {
		return state.get();
	}

	@Override
	public SimpleObjectProperty<TaskState> stateProperty() {
		return state;
	}

}

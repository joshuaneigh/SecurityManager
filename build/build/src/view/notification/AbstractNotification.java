package view.notification;

import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class AbstractNotification implements Notification {

	private static int DEFAULT_TIMEOUT;
	private static Color DEFAULT_TEXT_COLOR;
	private static Color DEFAULT_BACKGROUND_COLOR;
	
	private String title;
	private String message;
	
	/**
	 * The icon to display with the Notification, if any. 
	 */
	private Image icon;

	/**
	 * The text color of the {@link Notification}. By default, this will be
	 * determined by the local style sheet.
	 */
	private Color textColor;
	
	/**
	 * The background color of the {@link Notification}. By default, this will be
	 * determined by the local style sheet.
	 */
	private Color backgroundColor;

	/**
	 * The number of seconds to show the {@link Notification} until it will be
	 * automatically dismissed. If the timeout is set to a negative value, the
	 * {@link Notification} will not timeout. If the value is set to zero, the
	 * {@link Notification} will not be displayed.
	 */
	private int timeout;

	protected AbstractNotification(final String title, final String message) {
		this(title, message, null);
	}
	
	protected AbstractNotification(final String title, final String message, final Image icon) {
		this.title = title;
		this.message = message;
		this.icon = icon;
		timeout = DEFAULT_TIMEOUT;
		textColor = DEFAULT_TEXT_COLOR;
		backgroundColor = DEFAULT_BACKGROUND_COLOR;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public Image getIcon() {
		return icon;
	}

	public Color getTextColor() {
		return textColor;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTitle(final String title) {
		Objects.requireNonNull(title);
		this.title = title;
	}

	public void setMessage(final String message) {
		Objects.requireNonNull(message);
		this.message = message;
	}

	public void setIcon(final Image icon) {
		Objects.requireNonNull(icon);
		this.icon = icon;
	}

	public void setTextColor(final Color textColor) {
		Objects.requireNonNull(textColor);
		this.textColor = textColor;
	}
	
	public void setBackgroundColor(final Color backgroundColor) {
		Objects.requireNonNull(backgroundColor);
		this.backgroundColor = backgroundColor;
	}
	
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

}

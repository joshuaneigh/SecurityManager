package view.notification;

import javafx.scene.paint.Color;

public interface Notification {

	public String getTitle();
	public String getMessage();
	public Color getTextColor();
	public Color getBackgroundColor();
	
	public void setTitle();
	public void setMessage();
	public void setTextColor();
	public void setBackgroundColor();
	
}

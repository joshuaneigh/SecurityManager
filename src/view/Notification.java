package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notification extends Application implements Initializable {

	public static final int SUCCESS = 0;
	public static final int INFO = 1;
	public static final int ERROR = 2;
	public static final int WARNING = 3;
	
	private static final Image SUCCESS_ICON;
	private static final Image INFO_ICON;
	private static final Image ERROR_ICON;
	private static final Image WARNING_ICON;
	
	@FXML private AnchorPane root;
	@FXML private Text title;
	@FXML private ImageView icon;
	@FXML private Text message;
	
	static {
		SUCCESS_ICON = new Image("/images/warning.png");
		INFO_ICON = new Image("/images/warning.png");;
		ERROR_ICON = new Image("/images/warning.png");;
		WARNING_ICON = new Image("/images/warning.png");;
	}
	
	@Override
	public void start(final Stage primaryStage) {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void show() {
		final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		final Stage stage = (Stage) root.getScene().getWindow();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		stage.setX(bounds.getMinX() + bounds.getWidth() - stage.getWidth() - 10);
		stage.setY(bounds.getMinY() + bounds.getHeight() - stage.getHeight() - 10);
	}
	
	public void setTitle(final String text) {
		title.setText(text);
	}
	
	public void setIcon(final Image image) {
		icon.setImage(image);
	}
	
	public void setMessage(final String text) {
		message.setText(text);
	}
	
	public void setMessageType(final int messageType) {
		switch (messageType) {
		case WARNING:
			setTitle("Warning");
			setIcon(WARNING_ICON);
			break;
		default:
			break;
		}
	}
	
	@FXML
	private void handleDismissNotification() {
		((Stage) root.getScene().getWindow()).close();
	}

}

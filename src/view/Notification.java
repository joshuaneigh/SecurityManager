package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Notification {

	public static final byte SUCCESS = 0;
	public static final byte INFO = 1;
	public static final byte ERROR = 2;
	public static final byte WARNING = 3;
	
	private static final String FXML_PATH;
	private static final Image SUCCESS_ICON;
	private static final Image INFO_ICON;
	private static final Image ERROR_ICON;
	private static final Image WARNING_ICON;
	
	@FXML private AnchorPane root;
	@FXML private Text title;
	@FXML private ImageView icon;
	@FXML private Text message;
	
	static {
		FXML_PATH = "./res/fxml/";
		SUCCESS_ICON = new Image("/images/success.png");
		INFO_ICON = new Image("/images/info.png");;
		ERROR_ICON = new Image("/images/error.png");;
		WARNING_ICON = new Image("/images/warning.png");;
	}
	
	public static void showNotification(final String message, final byte messageType) {
		final Stage stage = new Stage();
		try {
			final FXMLLoader loader = new FXMLLoader();
			final Scene scene = new Scene(loader.load(new FileInputStream(FXML_PATH + "Notification.fxml")));
			final Notification controller = loader.getController();
			controller.setMessageType(messageType);
			controller.setMessage(message);
			scene.setFill(null);
			scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
			stage.setScene(scene);
			controller.show();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void show() {
		final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		final Stage stage = (Stage) root.getScene().getWindow();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
		stage.setX(bounds.getMinX() + bounds.getWidth() - stage.getWidth() - 10);
		stage.setY(bounds.getMinY() + bounds.getHeight() - stage.getHeight() - 10);
		PauseTransition delay = new PauseTransition(Duration.seconds(5));
		delay.setOnFinished( event -> stage.close() );
		delay.play();
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
		case SUCCESS:
			setTitle("Success");
			setIcon(SUCCESS_ICON);
			break;
		case INFO:
			setTitle("Info");
			setIcon(INFO_ICON);
			break;
		case ERROR:
			setTitle("Error");
			setIcon(ERROR_ICON);
			break;
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
		Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(2000),
                       new KeyValue (root.getScene().getRoot().opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);   
        timeline.setOnFinished((ae) -> ((Stage) root.getScene().getWindow()).close()); 
        timeline.play();
	}

}

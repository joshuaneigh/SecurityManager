package view.scene;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class SceneController {

	private static final SceneController INSTANCE;
	
	private Stage primaryStage;
	private Object controller;
	
	static {
		INSTANCE = new SceneController();
	}
	
	private SceneController() {
		primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
	}
	
	public static void setScene(final String fxml) {
		final FXMLLoader loader = new FXMLLoader();
		try {
			final Scene scene = new Scene(loader.load(INSTANCE.getClass().getResource(fxml).openStream()));
			INSTANCE.controller = loader.getController();
			INSTANCE.primaryStage.setScene(scene);
			INSTANCE.primaryStage.show();
			centerOnScreen(INSTANCE.primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setStage(final Stage stage) {
		INSTANCE.primaryStage = stage;
	}
	
	public static Object getController() {
		return INSTANCE.controller;
	}
	
	private static void centerOnScreen(final Stage stage) {
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		stage.setX(bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2.0f);
		stage.setY(bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2.0f);
	}
	
}

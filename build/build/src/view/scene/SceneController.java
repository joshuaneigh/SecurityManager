package view.scene;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class SceneController {

	private static final SceneController INSTANCE;
	
	private final Stage primaryStage;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object getController() {
		return INSTANCE.controller;
	}
	
}

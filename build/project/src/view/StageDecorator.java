package view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class StageDecorator {

	private StageDecorator() {}
	
	public static void decorate(final Stage stage) {
		final AnchorPane root = new AnchorPane();
		final Button close = createCloseButton(stage);
		final Button maximize = createMaximizeButton(stage);
		final Button minimize = createMinimizeButton(stage);
		final FlowPane titlebar = new FlowPane(Orientation.HORIZONTAL);
		final ImageView icon = new ImageView(stage.getIcons().get(0));
		final Insets insets = new Insets(5, 10, 10, 5);
		final Scene content = stage.getScene();
		final Text text = new Text(stage.getTitle());
		
		titlebar.getChildren().add(icon);
		titlebar.getChildren().add(text);
		
		root.setPadding(insets);
		root.getChildren().add(titlebar);
		root.getChildren().add(text);
		root.getChildren().add(close);
		root.getChildren().add(maximize);
		root.getChildren().add(minimize);
		
		content.setRoot(root);
		stage.initStyle(StageStyle.UNDECORATED);
		
	}

	private static Button createMinimizeButton(final Stage stage) {
		final Button button = new Button("-");
		button.setOnAction(e -> stage.setIconified(true));
		return button;
	}

	private static Button createMaximizeButton(final Stage stage) {
		final Button button = new Button("[]");
		button.setOnAction(e -> stage.setMaximized(true));
		return button;
	}

	private static Button createCloseButton(final Stage stage) {
		final Button button = new Button("X");
		button.setOnAction(e -> stage.close());
		return button;
	}
	
	
	
}

package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PasswordPrompt {
	
	private static final String FXML_PATH = "./res/fxml/";
	
	private double mouseX;
	private double mouseY;
	
	@FXML private Text title;
	@FXML private PasswordField passwordField;
	
	public static String getPassword() {
		final Stage stage = new Stage();
		try {
			final FXMLLoader loader = new FXMLLoader();
			final Scene scene = new Scene(loader.load(new FileInputStream(FXML_PATH + "PasswordPrompt.fxml")));
			scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
			stage.setScene(scene);
			((PasswordPrompt) loader.getController()).title.setText("Enter Password");
			stage.setTitle("Enter Password");
			stage.getIcons().add(new Image(new FileInputStream("./res/images/icon.png")));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			return ((PasswordPrompt) loader.getController()).passwordField.getText();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	@FXML
	private void handleCancel() {
		passwordField.setText(null);
		((Stage) passwordField.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleOkay() {
		((Stage) passwordField.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleCloseWindow() {
		handleCancel();
	}
	
	@FXML
	private void handleDragStarted(MouseEvent event) {
	    mouseX = passwordField.getScene().getWindow().getX() - event.getScreenX();
	    mouseY = passwordField.getScene().getWindow().getY() - event.getScreenY();
	}

	@FXML
	private void handleDragged(MouseEvent event) {
		passwordField.getScene().getWindow().setX(event.getScreenX() + mouseX);
		passwordField.getScene().getWindow().setY(event.getScreenY() + mouseY);
	}
	
}

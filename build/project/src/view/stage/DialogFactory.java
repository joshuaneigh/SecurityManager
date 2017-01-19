package view.stage;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.Entry;

public final class DialogFactory {

	public static void informationDialog(final String message) {
		final Dialog dialog = loadFXML("InformationDialog.fxml");
		dialog.setMessage("Security Manager");
		dialog.setDescription(message);
		dialog.showAndWait();
	}
	
	public static void exceptionDialog(final Throwable exception) {
		final Dialog dialog = loadFXML("InformationDialog.fxml");
		dialog.setMessage("Security Manager");
		dialog.setDescription(exception.getMessage());
		dialog.showAndWait();
	}
	
	public static boolean confirmDialog(final String prompt) {
		final Dialog dialog = loadFXML("ConfirmationDialog.fxml");
		dialog.setMessage("Security Manager");
		dialog.setDescription(prompt);
		dialog.showAndWait();
		return (boolean) dialog.getResponse();
	}
	
	public static Entry entryDialog(final Entry edited) {
		return null;
	}
	
	public static File fileChooserDialog(File file) {
		return null;
	}
	
	private static Dialog loadFXML(final String fxml) {
		try {
			final FXMLLoader loader = new FXMLLoader();
			final Scene scene = new Scene(loader.load(DialogFactory.class.getResource(fxml).openStream()));
			final Dialog dialog = loader.getController();
			dialog.setScene(scene);
			return dialog;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

package view.stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Entry;

public final class DialogFactory {

	private static Stage OWNER;
	
	public static void informationDialog(final String message) {
		final Dialog dialog = loadFXML("InformationDialog.fxml");
		dialog.setMessage("Security Manager");
		dialog.setDescription(message);
		dialog.showAndWait();
	}
	
	public static void exceptionDialog(final Throwable exception) {
		final Dialog dialog = loadFXML("ExceptionDialog.fxml");
		final StringWriter desc = new StringWriter();
		final PrintWriter pw = new PrintWriter(desc);
		exception.printStackTrace(pw);
		dialog.setMessage(exception.getClass().getSimpleName() + " - Security Manager");
		dialog.setDescription(desc.toString());
		dialog.showAndWait();
	}
	
	public static boolean confirmDialog(final String prompt) {
		final Dialog dialog = loadFXML("ConfirmationDialog.fxml");
		dialog.setMessage("Security Manager");
		dialog.setDescription(prompt);
		return (boolean) dialog.getResponse();
	}
	
	public static Entry entryDialog(final Entry edited) {
		final Dialog dialog = loadFXML("EntryDialog.fxml");
		dialog.setMessage("Edit Entry - Security Manager");
		return (Entry) dialog.getEntry();
	}
	
	public static File fileChooserDialog(File file) {
		return null;
	}
	
	public static String passwordDialog(final String description) {
		final Dialog dialog = loadFXML("PasswordDialog.fxml");
		dialog.setDescription(description);
		return dialog.getString();
	}
	
	public static void setOwner(final Stage owner) {
		OWNER = owner;
	}
	
	private static Dialog loadFXML(final String fxml) {
		try {
			final FXMLLoader loader = new FXMLLoader();
			final Scene scene = new Scene(loader.load(DialogFactory.class.getResource(fxml).openStream()));
			final Dialog dialog = loader.getController();
			dialog.initOwner(OWNER);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setIcon("/view/image/icon-7.png");
			dialog.setScene(scene);
			return dialog;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

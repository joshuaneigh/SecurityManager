package view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Entry;
import model.PasswordGenerator;

public class NewEntry implements Initializable {
	
	@FXML private VBox root;
	@FXML private Text title;
	@FXML private TextField titleField;
	@FXML private TextField username;
	@FXML private TextField password;
	@FXML private TextField passwordConfirm;
	@FXML private TextArea notes;
	@FXML private TextField url;
	@FXML private DatePicker expires;
	
	private Entry editedEntry;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		passwordConfirm.setContextMenu(new ContextMenu());
	}
	
	public void setTitle(final String text) {
		title.setText(text);
	}
	
	@FXML
	private void handleOkay() {
		if (isValid()) {
			Main.addEntry(new Entry(titleField.getText(), username.getText(), password.getText(), 
					url.getText(), notes.getText(), expires.getValue()));
			if (editedEntry != null) {
				Main.removeEntry(editedEntry);
			}
			((Stage) root.getScene().getWindow()).close();
		}
	}
	
	@FXML
	private void handleCancel() {
		((Stage) root.getScene().getWindow()).close();
	}

	@FXML
	private void handleShowPassword() {
		final Tooltip tp = new Tooltip(password.getText());
		tp.show(password, root.getScene().getWindow().getX() + 121, root.getScene().getWindow().getY() + 94);
		Timeline timer = new Timeline();
		timer.setCycleCount(1);
		timer.getKeyFrames().add(new KeyFrame(Duration.millis(3000)));
		timer.setOnFinished((event) -> tp.hide());
		timer.play();
	}
	
	@FXML
	private void handleGeneratePassword() {
		final String pass = new String(PasswordGenerator.generatePswd(8, 16, 3, 2, 2));
		password.setText(pass);
		passwordConfirm.setText(pass);
	}
	
	@FXML
	private void handleCopyToClipboard() {
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(new StringSelection(password.getText()), null);
		password.requestFocus();
		password.selectAll();
	}
	
	@FXML
	private void handleKeyPressed(final KeyEvent event) {
		switch (event.getCode()) {
			case ENTER:
				if (!notes.isFocused()) {
					handleOkay();
				}
				break;
			case TAB:
				if (notes.isFocused()) {
					event.consume();
					if (event.isShiftDown()) {
						passwordConfirm.requestFocus();
					} else {
						url.requestFocus();
					}
				}
				break;
			default:
				break;
		}
	}
	
	@FXML
	private void handleCloseWindow() {
		((Stage) root.getScene().getWindow()).close();
	}
	
	public void setEntry(final Entry entry) {
		titleField.setText(entry.getTitle());
		username.setText(entry.getUsername());
		password.setText(entry.getPassword());
		passwordConfirm.setText(entry.getPassword());
		url.setText(entry.getUrl());
		notes.setText(entry.getNotes());
		expires.setValue(entry.getExpires());
		editedEntry = entry;
	}
	
	private boolean isValid() {
		
		if (titleField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Warning");
			alert.setContentText("Title cannot be empty.");
			alert.showAndWait();
			return false;
		} else if (username.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Warning");
			alert.setContentText("Username cannot be empty.");
			alert.showAndWait();
			return false;
		} else if (password.getText().length() < 8) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Warning");
			alert.setContentText("Password is less than 8 characters.");
			alert.showAndWait();
			return false;
		} else if (! password.getText().equals(passwordConfirm.getText())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Passwords do not match.");
			alert.showAndWait();
			return false;
		} else {
			return true;
		}
		
	}

}

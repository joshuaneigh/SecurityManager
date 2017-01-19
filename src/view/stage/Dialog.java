package view.stage;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Entry;
import util.security.EncryptedString;
import util.security.PasswordGenerator;

public class Dialog extends Stage implements Initializable {

	@FXML private ImageView icon;
	@FXML private Text message;
	@FXML private TextArea description;
	@FXML private TextField title;
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private PasswordField passwordConfirm;
	@FXML private TextArea notes;
	@FXML private TextField url;
	@FXML private DatePicker expires;
	
	private Object response;
	private double mouseX;
	private double mouseY;
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.APPLICATION_MODAL);
	}

	@FXML
	private void handleOkay() {
		response = true;
		close();
	}
	
	@FXML
	private void handleCancel() {
		response = false;
		close();
	}
	
	@FXML
	private void handleMoveStarted(MouseEvent event) {
	    mouseX = icon.getScene().getWindow().getX() - event.getScreenX();
	    mouseY = icon.getScene().getWindow().getY() - event.getScreenY();
	}

	@FXML
	private void handleMoved(MouseEvent event) {
		icon.getScene().getWindow().setX(event.getScreenX() + mouseX);
		icon.getScene().getWindow().setY(event.getScreenY() + mouseY);
	}
	
	@FXML
	private void handleQuit() {
		Platform.exit();
	}
	
	@FXML
	private void handleGeneratePassword() {
		final String pass = PasswordGenerator.generatePassword();
		password.setText(pass);
		passwordConfirm.setText(pass);
	}
	
	@FXML
	private void handleShowPassword() {
		final Skin<?> old = password.getSkin();
		password.setSkin(new TextField().getSkin());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		password.setSkin(old);
	}
	
	@FXML
	private void handleCopyToClipboard() {
		final StringSelection selection = new StringSelection(password.getText());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}
	
	@FXML
	private void handleKeyPressed(final KeyEvent event) {
		if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown() && notes.isFocused()) {
			url.requestFocus();
			event.consume();
		}
	}
	
	public void setIcon(final String path) {
		icon.setImage(new Image(getClass().getResourceAsStream(path)));
	}
	
	public void setDescription(final String message) {
		this.description.setText(message);
	}
	
	public void setMessage(final String title) {
		super.setTitle(title);
		this.message.setText(title);
	}
	
	protected Object getResponse() {
		showAndWait();
		return response;
	}
	
	protected Entry getEntry() {
		showAndWait();
		if ((boolean) response) {
			if (!password.getText().equals(passwordConfirm.getText())) {
				DialogFactory.informationDialog("Entered passwords do not match!");
				return null;
			}
			String p1;
			String p2;
			do {
				do {
					p1 = DialogFactory.passwordDialog("Please enter your encryption password.");
				} while (!isValidPassword(p1));
				p2 = DialogFactory.passwordDialog("Please confirm your password.");
			} while (!p1.equals(p2));
			final EncryptedString pass = new EncryptedString(password.getText(), p1);
			final Entry entry = new Entry(title.getText(), username.getText(), pass, url.getText(), notes.getText(), expires.getValue());
			return entry;
		} else {
			return null;
		}
	}
	
	protected String getString() {
		do {
			showAndWait();
		} while (!isValidPassword(password.getText()));
		return password.getText();
	}
	
	private boolean isValidPassword(final String pass) {
		if (pass == null || pass.length() == 0) {
			DialogFactory.informationDialog("Password cannot be null or empty.");
		} else if (pass.length() < 8) {
			DialogFactory.informationDialog("Password of insufficient length. Must be 8 characters long.");
		} else {
			return true;
		}
		
		return false;
	}
	
}

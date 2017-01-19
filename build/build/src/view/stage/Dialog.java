package view.stage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Dialog extends Stage implements Initializable {

	@FXML private Text title;
	@FXML private TextArea message;
	
	private Object response;
	
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
	
	public void setDescription(final String message) {
		this.message.setText(message);
	}
	
	public void setMessage(final String title) {
		super.setTitle(title);
		this.title.setText(title);
	}
	
	protected Object getResponse() {
		showAndWait();
		return response;
	}
	
}

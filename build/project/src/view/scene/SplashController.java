package view.scene;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class SplashController implements Initializable {
	
	@FXML public ProgressBar progress;
	@FXML public Text message;
	@FXML public Text numTasks;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert progress != null : "progress";
		assert message != null : "message";
		assert numTasks != null : "numTasks";
	}

}

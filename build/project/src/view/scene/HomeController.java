package view.scene;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Entry;
import view.stage.DialogFactory;

public class HomeController implements Initializable {

	@FXML private TableView<Entry> entryView;
	@FXML private TableColumn<Entry, String> title;
	@FXML private TableColumn<Entry, String> notes;
	@FXML private TableColumn<Entry, String> url;
	@FXML private TableColumn<Entry, String> username;
	@FXML private TableColumn<Entry, LocalDate> created;
	@FXML private TableColumn<Entry, LocalDate> modified;
	@FXML private TableColumn<Entry, LocalDate> expires;
	
	private boolean unsavedChangesMade;
	private ObservableList<Entry> entries;
	private File saveToFile;
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
		url.setCellValueFactory(new PropertyValueFactory<>("url"));
		username.setCellValueFactory(new PropertyValueFactory<>("username"));
		created.setCellValueFactory(new PropertyValueFactory<>("created"));
		modified.setCellValueFactory(new PropertyValueFactory<>("modified"));
		expires.setCellValueFactory(new PropertyValueFactory<>("expires"));
		
		entries = FXCollections.observableArrayList();
		entryView.setItems(entries);
		unsavedChangesMade = false;
		saveToFile = null;
	}

	@FXML
	private void handleNewEntry() {
		final Entry e = DialogFactory.entryDialog(null);
		if (e != null) {
			entries.add(e);
			unsavedChangesMade = true;
		}
	}
	
	@FXML
	private void handleNewFile() {
		if (unsavedChangesMade && DialogFactory.confirmDialog("Would you like to save your changes?")) {
			handleSaveFile();
		}
		
		unsavedChangesMade = true;
		entries.removeAll(entries);
		saveToFile = DialogFactory.fileChooserDialog(saveToFile);
		((Stage) entryView.getScene().getWindow()).setTitle(saveToFile.getName() + " - Security Manager");
	}
	
	@FXML
	private void handleOpenFile() {
		handleNewFile();
		entries.addAll(getFromFile(saveToFile));
		unsavedChangesMade = false;
	}
	
	@FXML
	private void handleImportFile() {
		final File f = DialogFactory.fileChooserDialog(saveToFile);
		if (f != null) {
			final List<Entry> list = getFromFile(f);
			if (!list.isEmpty()) {
				entries.addAll(list);
				unsavedChangesMade = true;
			}
		}
	}
	
	private List<Entry> getFromFile(final File f) {
		return null;
	}
	
	@FXML
	private void handleSaveFile() {
		//if not exists, call handleSaveAs()
		unsavedChangesMade = false;
	}
	
	@FXML
	private void handleSaveAs() {
		final File f = DialogFactory.fileChooserDialog(saveToFile);
		if (f != null) {
			saveToFile = f;
			handleSaveFile();
		}
	}
	
	@FXML
	private void handleCopyURL() {
		
	}
	
	@FXML
	private void handleCopyUsername() {
		
	}
	
	@FXML
	private void handleCopyPassword() {
		
	}
	
	@FXML
	private void handleDuplicateEntry() {
		
	}
	
	@FXML
	private void handleDeleteEntry() {
		
	}
	
	@FXML
	private void handleEditEntry() {
		
	}
	
	@FXML
	private void handleSelectAll() {
		
	}
	
	@FXML
	private void handleSelectNone() {
		
	}
	
	@FXML
	private void handleSelectNext() {
		
	}
	
	@FXML
	private void handleSelectPrevious() {
		
	}
	
	@FXML
	private void handleEditPreferences() {
		
	}
	
	@FXML
	private void handleHelp() {
		
	}
	
	@FXML
	private void handleAbout() {
		
	}
	
	@FXML
	private void handleQuit() {
		//if edits made, prompt handleSaveFile()
		Platform.exit();
	}
	
}

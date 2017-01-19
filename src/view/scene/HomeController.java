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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Entry;
import view.stage.DialogFactory;

public class HomeController implements Initializable {

	@FXML private ImageView icon;
	@FXML private Text stageTitle;
	@FXML private TableView<Entry> entryView;
	@FXML private TableColumn<Entry, String> title;
	@FXML private TableColumn<Entry, String> notes;
	@FXML private TableColumn<Entry, String> url;
	@FXML private TableColumn<Entry, String> username;
	@FXML private TableColumn<Entry, LocalDate> created;
	@FXML private TableColumn<Entry, LocalDate> modified;
	@FXML private TableColumn<Entry, LocalDate> expires;
	
	private ContextMenu contextMenu;
	private ObservableList<Entry> entries;
	private boolean unsavedChangesMade;
	private File saveToFile;
	private double mouseX;
	private double mouseY;
	
	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
		url.setCellValueFactory(new PropertyValueFactory<>("url"));
		username.setCellValueFactory(new PropertyValueFactory<>("username"));
		created.setCellValueFactory(new PropertyValueFactory<>("created"));
		modified.setCellValueFactory(new PropertyValueFactory<>("modified"));
		expires.setCellValueFactory(new PropertyValueFactory<>("expires"));
		
		icon.setImage(new Image(getClass().getResourceAsStream("/view/image/icon-7.png")));
		stageTitle.setText("Security Manager");
		entries = FXCollections.observableArrayList();
		entryView.setItems(entries);
		unsavedChangesMade = false;
		saveToFile = null;
		constructContextMenu();
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
		stageTitle.setText(saveToFile.getName() + " - Security Manager");
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
		copyToClipboard(entryView.getSelectionModel().getSelectedItem().getUrl());
	}
	
	@FXML
	private void handleCopyUsername() {
		copyToClipboard(entryView.getSelectionModel().getSelectedItem().getUsername());
	}
	
	@FXML
	private void handleCopyPassword() {
		copyToClipboard(entryView.getSelectionModel().getSelectedItem().getPassword().decrypt(DialogFactory.passwordDialog("Please enter your decryption password.")));
	}
	
	private void copyToClipboard(final String text) {
		
	}
	
	@FXML
	private void handleDuplicateEntry() {
		
	}
	
	@FXML
	private void handleDeleteEntry() {
		List<Entry> items = entryView.getSelectionModel().getSelectedItems();
		if (!items.isEmpty() && DialogFactory.confirmDialog("Are you sure you would like to delete the selected entries? Your changes cannot be undone.")) {
			entries.removeAll(items);
			unsavedChangesMade = true;
		}
	}
	
	@FXML
	private void handleEditEntry() {
		final Entry e = entryView.getSelectionModel().getSelectedItem();
		if (e != null) {
			final Entry clone = DialogFactory.entryDialog(e);
			if (!e.equals(clone)) {
				entries.remove(e);
				entries.add(clone);
				unsavedChangesMade = true;
			}
		}
	}
	
	@FXML
	private void handleSelectAll() {
		entryView.getSelectionModel().selectAll();
	}
	
	@FXML
	private void handleSelectNone() {
		entryView.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void handleSelectNext() {
		final int idx = entryView.getSelectionModel().getSelectedIndex();
		System.out.println(idx);
		if (idx < entries.size() - 1) {
			entryView.getSelectionModel().selectNext();
		} else if (idx == -1) {
			entryView.getSelectionModel().selectFirst();
		} else {
			entryView.getSelectionModel().selectLast();
		}
	}
	
	@FXML
	private void handleSelectPrevious() {
		final int idx = entryView.getSelectionModel().getSelectedIndex();
		if (idx > 0) {
			entryView.getSelectionModel().selectPrevious();
		} else if (idx == -1) {
			entryView.getSelectionModel().selectLast();
		} else {
			entryView.getSelectionModel().selectFirst();
		}
	}
	
	@FXML
	private void handleEditPreferences() {
		
	}
	
	@FXML
	private void handleHelp() {
		
	}
	
	@FXML
	private void handleAbout() {
		DialogFactory.informationDialog("Copyright 2017, Joshua Neighbarger\nhttps://www.system46.com/");
	}
	
	@FXML
	private void handleQuit() {
		if (unsavedChangesMade && DialogFactory.confirmDialog("Would you like to save your changes?")) {
			if (saveToFile == null) {
				handleSaveAs();
			} else {
				handleSaveFile();
			}
		}
		Platform.exit();
	}
	
	@FXML
	private void handleMoveStarted(final MouseEvent event) {
	    mouseX = icon.getScene().getWindow().getX() - event.getScreenX();
	    mouseY = icon.getScene().getWindow().getY() - event.getScreenY();
	}

	@FXML
	private void handleMoved(final MouseEvent event) {
		icon.getScene().getWindow().setX(event.getScreenX() + mouseX);
		icon.getScene().getWindow().setY(event.getScreenY() + mouseY);
	}
	
	private void constructContextMenu() {
		contextMenu = new ContextMenu();
		contextMenu.setAutoFix(true);
		contextMenu.setAutoHide(true);
		final MenuItem restore = new MenuItem("Restore");
		restore.setOnAction((e) -> handleRestore());
		final MenuItem move = new MenuItem("Move");
		move.setOnAction((e) -> {});
		final MenuItem size = new MenuItem("Size");
		size.setOnAction((e) -> {});
		final MenuItem minimize = new MenuItem("Minimize");
		minimize.setOnAction((e) -> handleMinimize());
		final MenuItem maximize = new MenuItem("Maximize");
		maximize.setOnAction((e) -> handleMaximize());
		final MenuItem close = new MenuItem("Close");
		close.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
		close.setOnAction((e) -> handleQuit());
		contextMenu.getItems().addAll(restore, move, size, minimize, maximize, new SeparatorMenuItem(), close);
	}
	
	@FXML
	private void handleStageContext(final MouseEvent event) {
		contextMenu.show(icon, event.getScreenX(), event.getScreenY());
	}
	
	@FXML
	private void handleMaximize() {
		if (((Stage) icon.getScene().getWindow()).isMaximized()) {
			handleRestore();
		} else {
			((Stage) icon.getScene().getWindow()).setMaximized(true);
		}
	}
	
	@FXML
	private void handleRestore() {
		((Stage) icon.getScene().getWindow()).setMaximized(false);
	}
	
	@FXML
	private void handleMinimize() {
		((Stage) icon.getScene().getWindow()).setIconified(true);
	}
	
}

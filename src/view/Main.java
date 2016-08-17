package view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Entry;

public class Main extends Application implements Initializable {
	
	private static String FXML_PATH;
	private static ObservableList<Entry> ENTRY_LIST;

	@FXML private Text searchClear;
	@FXML private Text searchIcon;
	@FXML private TextField searchField;
	@FXML private TableView<Entry> dataTable;
	@FXML private TableColumn<Entry, String> titleColumn;
	@FXML private TableColumn<Entry, String> userNameColumn;
	@FXML private TableColumn<Entry, String> urlColumn;
	@FXML private TableColumn<Entry, String> notesColumn;
	@FXML private TableColumn<Entry, LocalDate> expiresColumn;
	
	static {
		FXML_PATH = "./res/fxml/";
		ENTRY_LIST = FXCollections.observableArrayList();
	}

	static void addEntry(final Entry entry) {
		ENTRY_LIST.add(entry);
	}
	
	static void removeEntry(final Entry entry) {
		ENTRY_LIST.remove(entry);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchClear.visibleProperty().bind(Bindings.isNotEmpty(searchField.textProperty()));
		searchIcon.visibleProperty().bind(Bindings.isEmpty(searchField.textProperty()));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("title"));
		userNameColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
		urlColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("url"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("notes"));
		expiresColumn.setCellValueFactory(new PropertyValueFactory<Entry, LocalDate>("expires"));
		dataTable.setItems(ENTRY_LIST);
		
        FilteredList<Entry> filteredData = new FilteredList<>(ENTRY_LIST, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(entry -> {
            	if (newValue == null || newValue.isEmpty()) {
            		return true;
            	}
            	String lowerCaseFilter = newValue.toLowerCase();
                if (entry.getTitle().toLowerCase().contains(lowerCaseFilter) || entry.getUsername().toLowerCase().contains(lowerCaseFilter)
                		|| entry.getUrl().toLowerCase().contains(lowerCaseFilter) || entry.getNotes().toLowerCase().contains(lowerCaseFilter)
                		|| entry.getNotes().toLowerCase().contains(lowerCaseFilter) ) {
                    return true; 
                } else {
                	return false;
                }
            });
        });
        SortedList<Entry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());
        dataTable.setItems(sortedData);
		
		final ContextMenu menu = dataTable.getContextMenu();
		dataTable.setContextMenu(new ContextMenu());
		dataTable.setRowFactory(
			    new Callback<TableView<Entry>, TableRow<Entry>>() {
			  @Override
			  public TableRow<Entry> call(TableView<Entry> tableView) {
			    final TableRow<Entry> row = new TableRow<>();
			    row.contextMenuProperty().bind(
			      Bindings.when(Bindings.isNotNull(row.itemProperty()))
			      .then(menu)
			      .otherwise((ContextMenu)null));
			    return row;
			  }
			});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			final Scene scene = new Scene(new FXMLLoader().load(new FileInputStream(FXML_PATH + "Main.fxml")));
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		primaryStage.getIcons().add(new Image(new FileInputStream("./res/images/icon.png")));
		primaryStage.setTitle("SecurityManager");
		primaryStage.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest((event) -> {
//			if (!isSaved) {
				final ButtonType yes = new ButtonType("Yes");
				final ButtonType no = new ButtonType("No");
				final ButtonType cancel = new ButtonType("Cancel");
				final Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setContentText("Would you like to save your changes?");
				alert.getButtonTypes().setAll(yes, no, cancel);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == yes) {
					handleSaveFile();
				} else if (result.get() == cancel) {
					event.consume();
					return;
				}
				Platform.exit();
//			}
		});
		
	}
	
	@FXML
	private void handleClearSearch() {
		searchField.setText("");
	}
	
	@FXML
	private void handleNewEntry() {
		final Stage stage = new Stage();
		try {
			final Scene scene = new Scene(new FXMLLoader().load(new FileInputStream(FXML_PATH + "NewEntry.fxml")));
			scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("New Entry");
		stage.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	}
	
	@FXML
	private void handleEditEntry() {
		if (dataTable.getSelectionModel().getSelectedItem() != null) {
			final Stage stage = new Stage();
			try {
				final FXMLLoader loader = new FXMLLoader();
				final Scene scene = new Scene(loader.load(new FileInputStream(FXML_PATH + "NewEntry.fxml")));
				final NewEntry controller = loader.getController();
				controller.setEntry(dataTable.getSelectionModel().getSelectedItem());
				scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
				stage.setScene(scene);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit Entry");
			stage.show();
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
		}
	}
	
	@FXML
	private void handleOpenURL() {
		final Entry entry = dataTable.getSelectionModel().getSelectedItem();
		getHostServices().showDocument(entry.getUrl());
	}
	
	@FXML
	private void handleOpenFile() {
		final FileChooser fc = new FileChooser();
		fc.setTitle("Open Password File");
		final File file = fc.showOpenDialog(new Stage());
		if (file != null) {
			openFile(file);
		}
	}
	
	@FXML
	private void handleSaveFile() {
		final FileChooser fc = new FileChooser();
		fc.setTitle("Save Password File");
		final File file = fc.showSaveDialog(new Stage());
		if (file != null) {
			saveFile(file);
		}
	}
	
	@FXML
	private void handleHelp() {
		
	}
	
	@FXML
	private void handleMouseClicked(final MouseEvent event) {
		if (event.getClickCount() == 2) {
			handleEditEntry();
		}
	}
	
	@FXML
	private void handleDeleteEntry() {
		final Entry entry = dataTable.getSelectionModel().getSelectedItem();
		if (entry != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setContentText("Are you sure you want to delete this entry?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				removeEntry(entry);
			} 
		}
	}
	
	@FXML
	private void handleCopyToClipboard() {
		final Entry entry = dataTable.getSelectionModel().getSelectedItem();
		if (entry == null) {
			return;
		} else {
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(new StringSelection(entry.getPassword()), null);
		}
	}
	
	@FXML
	private void handleKeyPressed(final KeyEvent event) {
		switch (event.getCode()) {
		case DELETE: 
			handleDeleteEntry();
			break;
		case ENTER:
			handleEditEntry();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void openFile(final File file) {
		try {
			final FileInputStream fis = new FileInputStream(file);
			final ObjectInputStream ois = new ObjectInputStream(fis);
			List<Entry> list = (ArrayList<Entry>) ois.readObject();
			ois.close();
			ENTRY_LIST.removeAll(ENTRY_LIST);
			ENTRY_LIST.addAll(list);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void saveFile(final File file) {
		try {
			final FileOutputStream fos = new FileOutputStream(file);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new ArrayList<Entry>(ENTRY_LIST));
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(final String[] args) {
		launch(args);
	}

}

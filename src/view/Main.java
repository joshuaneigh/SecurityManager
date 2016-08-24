package view;

import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
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
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import model.Entry;
import model.FileEncryptor;
import model.Tray;

public class Main extends Application implements Initializable {
	
	private static final byte NORTH = 0;
	private static final byte NORTHEAST = 1;
	private static final byte EAST = 2;
	private static final byte SOUTHEAST = 3;
	private static final byte SOUTH = 4;
	private static final byte SOUTHWEST = 5;
	private static final byte WEST = 6;
	private static final byte NORTHWEST = 7;
	
	private static String FXML_PATH;
	private static ObservableList<Entry> ENTRY_LIST;
	private static Tray TRAY;
	
	private final ScheduledExecutorService scheduler;
	
	private double mouseX;
	private double mouseY;
	private byte resizeDirection;
	
	@FXML private FlowPane titlebar;
	@FXML private ImageView icon;
	@FXML private Text title;	
	@FXML private Text searchClear;
	@FXML private Text searchIcon;
	@FXML private TextField searchField;
	@FXML private Button maximizeButton;
	@FXML private Button editButton;
	@FXML private TableView<Entry> dataTable;
	@FXML private TableColumn<Entry, String> titleColumn;
	@FXML private TableColumn<Entry, String> userNameColumn;
	@FXML private TableColumn<Entry, String> urlColumn;
	@FXML private TableColumn<Entry, String> notesColumn;
	@FXML private TableColumn<Entry, LocalDate> expiresColumn;
	
	static {
		FXML_PATH = "./res/fxml/";
		ENTRY_LIST = FXCollections.observableArrayList();
		TRAY = new Tray("/images/tray.png", "SecurityManager");
	}

	public Main() {
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				checkExpirationDates();
			}
		}, 1L, TimeUnit.DAYS);
	}
	
	static void addEntry(final Entry entry) {
		ENTRY_LIST.add(entry);
	}
	
	static void removeEntry(final Entry entry) {
		ENTRY_LIST.remove(entry);
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			final Scene scene = new Scene(new FXMLLoader().load(new FileInputStream(FXML_PATH + "Main.fxml")));
			scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Platform.setImplicitExit(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setMinHeight(150);
		primaryStage.setMinWidth(475);
		primaryStage.setTitle("SecurityManager");
		primaryStage.getIcons().add(new Image(new FileInputStream("./res/images/icon.png")));
		primaryStage.setOnCloseRequest(e -> handleCloseRequest(e));
		primaryStage.show();
		centerStage(primaryStage);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTable();
		setupSearchbar();
		setupTray();
		setupTitlebar();
		
		editButton.disableProperty().bind(Bindings.isEmpty(dataTable.getSelectionModel().getSelectedItems()));
	}
	
	public void checkExpirationDates() {
		int expiredCounter = 0;
		String title = null;
		String date = null;
		for (final Entry entry : ENTRY_LIST) {
			if (entry.getExpires() != null && entry.getExpires().compareTo(LocalDate.now()) <= 0) {
				expiredCounter++;
				title = entry.getTitle();
				date = entry.getExpires().toString();
				// TODO: Set cell to red
			}
		}
		
		final String message;
		final byte messageType;
		if (expiredCounter > 1) {
			message = "Your password, " + title + ", and \n" + (expiredCounter - 1) + " others have expired.";
			messageType = Notification.WARNING;
		} else if (expiredCounter == 1) {
			message = "Your password, " + title + ",\nhas expired on " + date + '.';
			messageType = Notification.WARNING;
		} else {
			message = "All passwords are up to date.";
			messageType = Notification.SUCCESS;
		}

		Notification.showNotification(message, messageType);
		
	}
	
	@SuppressWarnings("unchecked")
	private void openFile(final File file) {
		try {
			FileEncryptor.decryptFile(file, PasswordPrompt.getPassword());
			final FileInputStream fis = new FileInputStream(new File(file.getAbsolutePath() + ".tmp"));
			final ObjectInputStream ois = new ObjectInputStream(fis);
			List<Entry> list = (ArrayList<Entry>) ois.readObject();
			ois.close();
			ENTRY_LIST.removeAll(ENTRY_LIST);
			ENTRY_LIST.addAll(list);
			checkExpirationDates();
		} catch (IOException | ClassNotFoundException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			FileEncryptor.deleteTempFile(file);
		}
	}
	
	private void saveFile(final File file) {
		try {
			final FileOutputStream fos = new FileOutputStream(file);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new ArrayList<Entry>(ENTRY_LIST));
			oos.close();
			FileEncryptor.encryptFile(file, PasswordPrompt.getPassword());
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void centerStage(final Stage primaryStage) {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}
	
	private void setupTitlebar() {
		title.setText("SecurityManager");
	}
	
	private void setupTray() {
		final PopupMenu menu = new PopupMenu("Popup");
		
		final MenuItem restore = new MenuItem("Restore...");
		restore.addActionListener(e -> Platform.runLater(() -> ((Stage) dataTable.getScene().getWindow()).show()));
		menu.add(restore);
		
		final MenuItem close = new MenuItem("Close");
		close.addActionListener(e -> handleCloseWindow());
		menu.add(close);
		
		TRAY.setPopupMenu(menu);
		TRAY.setToolTip(null);
	}
	
	private void setupTable() {
		titleColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("title"));
		userNameColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("username"));
		urlColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("url"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("notes"));
		expiresColumn.setCellValueFactory(new PropertyValueFactory<Entry, LocalDate>("expires"));
		
		dataTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue && !(dataTable.getScene().getFocusOwner() instanceof Button)) {
				dataTable.getSelectionModel().select(null);
			}
		});
		
		final ContextMenu menu = dataTable.getContextMenu();
		dataTable.setContextMenu(new ContextMenu());
		dataTable.setRowFactory(tableView -> {
			final TableRow<Entry> row = new TableRow<>();
			row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(menu).otherwise((ContextMenu) null));
			return row;
		});
	}
	
	private void setupSearchbar() {
		searchIcon.setScaleX(-1);
		searchIcon.visibleProperty().bind(Bindings.isEmpty(searchField.textProperty()));
		searchClear.visibleProperty().bind(Bindings.isNotEmpty(searchField.textProperty()));
        FilteredList<Entry> filteredData = new FilteredList<>(ENTRY_LIST, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(entry -> {
            	if (newValue == null || newValue.isEmpty()) {
            		return true;
            	}
            	String lowerCaseFilter = newValue.toLowerCase();
                if (entry.getTitle().toLowerCase().contains(lowerCaseFilter) || entry.getUsername().toLowerCase().contains(lowerCaseFilter)
                		|| entry.getUrl().toLowerCase().contains(lowerCaseFilter) || entry.getNotes().toLowerCase().contains(lowerCaseFilter)
                		|| entry.getNotes().toLowerCase().contains(lowerCaseFilter) || entry.getExpires().toString().contains(lowerCaseFilter)) {
                    return true; 
                } else {
                	return false;
                }
            });
        });
        SortedList<Entry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());
        dataTable.setItems(sortedData);
	}
	
	@FXML
	private void handleClearSearch() {
		searchField.setText("");
	}
	
	@FXML
	private void handleNewEntry() {
		final Stage stage = new Stage();
		try {
			final FXMLLoader loader = new FXMLLoader();
			final Scene scene = new Scene(loader.load(new FileInputStream(FXML_PATH + "NewEntry.fxml")));
			final NewEntry controller = loader.getController();
			controller.setTitle("New Entry");
			scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
			stage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		final Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.show();
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
				controller.setTitle("Edit Entry");
				controller.setEntry(dataTable.getSelectionModel().getSelectedItem());
				scene.getStylesheets().add((new File(FXML_PATH + "application.css")).toURI().toURL().toExternalForm());
				stage.setScene(scene);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
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
		fc.setTitle("Open File");
		fc.setInitialDirectory(new File("./data"));
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SecurityManager File", "*.smf"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		final File file = fc.showOpenDialog(dataTable.getScene().getWindow());
		if (file != null) {
			openFile(file);
		}
	}
	
	@FXML
	private void handleSaveFile() {
		final FileChooser fc = new FileChooser();
		fc.setTitle("Save File");
		fc.setInitialDirectory(new File("./data"));
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SecurityManager File", "*.smf"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		final File file = fc.showSaveDialog(dataTable.getScene().getWindow());
		if (file != null) {
			saveFile(file);
		}
	}
	
	@FXML
	private void handleHelp() {
		System.out.println("Help");
		try {
			Desktop.getDesktop().open(new File("./res/help.html"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		case ESCAPE:
			dataTable.getSelectionModel().select(null);
			break;
		default:
			break;
		}
	}
	
	@FXML
	private void handleMinimizeWindow() {
		((Stage) dataTable.getScene().getWindow()).hide();
	}
	
	@FXML
	private void handleMaximizeWindow() {
		final Stage stage = (Stage) dataTable.getScene().getWindow();
		stage.setMaximized(stage.isMaximized() ^ true);
		maximizeButton.setUnderline(stage.isMaximized());
	}
	
	@FXML
	private void handleCloseWindow() {
		Platform.runLater(() -> {
			final Window window = dataTable.getScene().getWindow();
			final WindowEvent windowEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST);
			dataTable.getScene().getWindow().fireEvent(windowEvent);
		});
	}
	
	@FXML
	private void handleCloseRequest(final WindowEvent e) {
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
			e.consume();
			return;
		}
		TRAY.hideIcon();
		Platform.exit();
		System.exit(0);
	}
	
	@FXML
	private void handleDragStarted(MouseEvent event) {
	    mouseX = dataTable.getScene().getWindow().getX() - event.getScreenX();
	    mouseY = dataTable.getScene().getWindow().getY() - event.getScreenY();
	}

	@FXML
	private void handleDragged(MouseEvent event) {
		dataTable.getScene().getWindow().setX(event.getScreenX() + mouseX);
		dataTable.getScene().getWindow().setY(event.getScreenY() + mouseY);
	}
	
	@FXML
	private void handleResizeStart(MouseEvent event) {
	    final int x = (int) event.getSceneX();
	    final int y = (int) event.getSceneY();
	    final int endX = (int) (dataTable.getScene().getWindow().getWidth() - x);
	    final int endY = (int) (dataTable.getScene().getWindow().getHeight() - y);
	    
	    if (y <= 10) {
	    	if (x <= 10) {
	    		resizeDirection = NORTHWEST;
	    	} else if (endX <= 10) {
	    		resizeDirection = NORTHEAST;
	    	} else if (y <= 5) {
	    		resizeDirection = NORTH;
	    	}
	    } else if (endY <= 10) {
	    	if (x <= 10) {
	    		resizeDirection = SOUTHWEST;
	    	} else if (endX <= 10) {
	    		resizeDirection = SOUTHEAST;
	    	} else if (endY <= 5) {
	    		resizeDirection = SOUTH;
	    	}
	    } else if (x <= 5) {
	    	resizeDirection = WEST;
	    } else {
	    	resizeDirection = EAST;
	    }
	    System.out.println(resizeDirection);
	    System.out.println(x + ", " + endX);
	    System.out.println(y + ", " + endY);
	}
	
	@FXML
	private void handleResizeEnd(MouseEvent event) {
		resizeDirection = -1;
	}
	
	@FXML
	private void handleResizeDrag(MouseEvent event) {
		final int dx = (int) (dataTable.getScene().getWindow().getX() - event.getScreenX());
		final int dy = (int) (dataTable.getScene().getWindow().getY() - event.getScreenY());
		final Scene scene = dataTable.getScene();
		final Stage stage = (Stage) scene.getWindow();

		if (resizeDirection == NORTHWEST || resizeDirection == NORTH || resizeDirection == NORTHEAST) {
			stage.setHeight(Math.max(scene.getHeight() + dy, stage.getMinHeight()));
			stage.setY(event.getScreenY());
		}
		if (resizeDirection == SOUTHWEST || resizeDirection == WEST || resizeDirection == NORTHWEST) {
			stage.setWidth(Math.max(scene.getWidth() + dx, stage.getMinWidth()));
			stage.setX(event.getScreenX());
		}
		if (resizeDirection == NORTHEAST || resizeDirection == EAST || resizeDirection == SOUTHEAST) {
			stage.setWidth(Math.max(event.getSceneX(), stage.getMinWidth()));
		}
		if (resizeDirection == SOUTHWEST || resizeDirection == SOUTH || resizeDirection == SOUTHEAST) {
			stage.setHeight(Math.max(event.getSceneY(), stage.getMinHeight()));
		}
	}
	
}

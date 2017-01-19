package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.tasks.FilesExistTask;
import application.tasks.Task;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.config.PropertiesManager;
import util.config.XMLProperties;
import util.security.PasswordGenerator;
import util.security.PasswordPolicy;
import view.scene.SceneController;
import view.scene.SplashController;
import view.stage.DialogFactory;

/**
 * This application is intended to replace its previous implementation, Security
 * Manager. The previous version did not store passwords securely, and left them
 * in memory after the password file had been unencrypted. Although no clients'
 * information was compromised, a complete remodel of the program is necessary.
 * 
 * This implementation aims to correct major security vulnerabilities found in
 * the previous instance as well as add some of the following features:
 * 
 * - Ability to use a fingerprint reader to encrypt and decrypt password files -
 * Store password files on a web server or mobile device to ensure
 * synchronization across devices - Utility to merge two or more password files
 * - May run as Windows Service to ensure that time-sensitive notifications are
 * always displayed and that password expiration is reported upon successful
 * login.
 * 
 * 
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 05 Dec 2016
 */
public class Main extends Application {

	private static final int PORT = 12345;
	private static final ServerSocket SOCKET;
	private static final String DUPLICATE_INSTANCE_MESSAGE = "Another instance is already running.";
	private static final List<Task> TASK_LIST;
	private static final String NUM_TASK_MESSAGE;

	private XMLProperties applicationProp;

	static {
		try {
			SOCKET = new ServerSocket(PORT);
		} catch (IOException e) {
			DialogFactory.informationDialog(DUPLICATE_INSTANCE_MESSAGE);
			throw new DuplicateInstanceException(DUPLICATE_INSTANCE_MESSAGE);
		}

		NUM_TASK_MESSAGE = "Task %1$d of %2$d";
		TASK_LIST = new ArrayList<>(Arrays.asList(new FilesExistTask()));
	}

	private void delegateTasks() {
		int i = 0;
		SplashController ctrl = (SplashController) SceneController.getController();

		for (Task t : TASK_LIST) {
			ctrl.numTasks.setText(String.format(NUM_TASK_MESSAGE, ++i, TASK_LIST.size()));
			ctrl.progress.progressProperty().bind(t.progressProperty());
			ctrl.message.textProperty().bind(t.messageProperty());
			Thread th = new Thread(t);
			th.setDaemon(true);
			th.start();
			t.start();
		}
	}

	private void loadConfig() {
		Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> DialogFactory.exceptionDialog(e));
		applicationProp = PropertiesManager.getXML("./config/application.xml");
		Platform.setImplicitExit(applicationProp.getBoolean("implicit-exit"));
		PasswordGenerator.setPolicy(applicationProp.getObject("password-policy", PasswordPolicy.class));
	}
	
	private void setupIcons(final Stage stage) {
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-0.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-1.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-2.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-3.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-4.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-5.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-6.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-7.png")));
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/icon-8.png")));
	}

	@Override
	public void start(final Stage primaryStage) {
		Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> DialogFactory.exceptionDialog(e));
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setTitle("Security Manager");
		
		setupIcons(primaryStage);
		SceneController.setStage(primaryStage);
		SceneController.setScene("Splash.fxml");
		
		new Thread(() -> {
			delegateTasks();
			Platform.runLater(() -> loadConfig());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Platform.runLater(() -> SceneController.setScene("Home.fxml"));
		}).start();
		
	}

	@Override
	public void stop() {
		try {
			SOCKET.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> DialogFactory.exceptionDialog(e));
		launch(args);
	}

}

package application.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.config.XMLProperties;
import util.security.PasswordPolicy;

public class FilesExistTask extends AbstractTask {

	private static final List<File> FILE_LIST;
	private static final List<String> PATH_LIST;
	
	static {
		PATH_LIST = new ArrayList<>(Arrays.asList("./config", "./data"));
		FILE_LIST = new ArrayList<>(Arrays.asList(new File("./config/application.xml")));
	}
	
	private void createFile(final File file) {
		if (file.getName().equals("application.xml")) {
			final XMLProperties prop = new XMLProperties();
			prop.setProperty("implicit-exit", true);
			prop.setProperty("password-policy", PasswordPolicy.NO_SPECIAL_EIGHT_TO_TWELVE.toString());
			try {
				prop.storeToXML(new FileOutputStream(file), "Application Settings");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void start() {
		setMessage("Searching files.");
		setState(TaskState.RUNNING);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (final String s : PATH_LIST) {
			if (!(new File(s).exists())) {
				try {
					Files.createDirectory(Paths.get(s));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (final File f : FILE_LIST) {
			if (!(f.exists())) {
				setMessage(String.format("Creating file %1$s.", f.getName()));
				createFile(f);
			}
			setProgress(getProgress() + 1.0f / FILE_LIST.size());
		}
		setMessage("All files found.");
		setState(TaskState.COMPLETED);
	}

	@Override
	public void cancel() {
		setState(TaskState.CANCELED);
	}

}

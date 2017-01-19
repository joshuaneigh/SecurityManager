package util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertiesManager {

	private static final PropertiesManager INSTANCE = new PropertiesManager();
	
	private final Map<String, XMLProperties> propMap;
	
	private PropertiesManager () {
		propMap = new HashMap<>();
	}
	
	private void loadFromXML(final String path) {
		try {
			final XMLProperties prop = new XMLProperties();
			prop.loadFromXML(new FileInputStream(path));
			INSTANCE.propMap.put(path, prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadAllFrom(final String dir) {
		for (final File f : new File(dir).listFiles()) {
			if (f.isFile()) {
				INSTANCE.loadFromXML(dir + '/' + f.getName());
			}
		}
	}
	
	public static XMLProperties getXML(final String path) {
		if (! INSTANCE.propMap.containsKey(path)) {
			INSTANCE.loadFromXML(path);
		} 
		return INSTANCE.propMap.get(path);
	}
	
	public static void revertXML(final String path) {
		INSTANCE.loadFromXML(path);
	}
	
	public static void putXML(final String path) {
		if (INSTANCE.propMap.containsKey(path)) {
			final XMLProperties prop = INSTANCE.propMap.get(path);
			try {
				prop.storeToXML(new FileOutputStream(new File(path)), path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException("The specified XMLProperties was never loaded.");
		}
	}
	
}

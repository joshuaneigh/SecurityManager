package model;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.swing.ImageIcon;

import view.Main;

/**
 * This class allows for ease in utilization of the {@link SystemTray} and the implementation of a {@link TrayIcon}. 
 * 
 * @author Joshua Neighbarger | jneigh@uw.edu
 */
public class Tray {
	
	/** The {@link TrayIcon} to be shown in the {@link SystemTray}. */
	private static TrayIcon trayIcon;
	
	/**
	 * Public constructor, which will show the {@link TrayIcon} in the {@link SystemTray} upon instantiation.
	 */
	public Tray() {
		showIcon();
	}
	
	/**
	 * Sets the {@link PopupMenu} of the {@link TrayIcon}.
	 * 
	 * @param popup the {@link PopupMenu} to set.
	 */
	public void setPopupMenu(final PopupMenu popup) {
		trayIcon.setPopupMenu(popup);
	}
	
	/**
	 * Sets the tooltip of the {@link TrayIcon}.
	 * 
	 * @param tooltip the {@link String} tooltip to set.
	 */
	public void setToolTip(final String tooltip) {
		trayIcon.setToolTip(tooltip);
	}
	
	/**
	 * Hides the {@link TrayIcon} from the {@link SystemTray}.
	 */
	public void hideIcon() {
		final SystemTray tray = SystemTray.getSystemTray();
		tray.remove(trayIcon);
	}
	
	/**
	 * Shows the {@link TrayIcon} in the {@link SystemTray}.
	 */
	public void showIcon() {
		if (! SystemTray.isSupported()) {
			System.err.println("System tray is not supported.");
			return;
		} 
		
		try {
			trayIcon = new TrayIcon(createIcon("/images/tray.png", "Tray Icon"));
			trayIcon.setImageAutoSize(true);
			final SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates and returns the {@link ImageIcon}on for use in the {@link SystemTray}.
	 * 
	 * @param path the path to the image
	 * @param desc the description of the image
	 * @return ImageIcon for TrayIcon
	 */
	private Image createIcon(final String path, final String desc) {
		URL url = Main.class.getResource(path);
		return new ImageIcon(url, desc).getImage();
	}
	
}

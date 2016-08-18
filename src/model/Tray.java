package model;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.swing.ImageIcon;

import view.Main;

public class Tray {
	
	private static TrayIcon trayIcon;
	
	public Tray() {
		showIcon();
	}
	
	public void setPopupMenu(final PopupMenu popup) {
		trayIcon.setPopupMenu(popup);
	}
	
	public void setToolTip(final String tooltip) {
		trayIcon.setToolTip(tooltip);
	}
	
	public void hideIcon() {
		final SystemTray tray = SystemTray.getSystemTray();
		tray.remove(trayIcon);
	}
	
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
	
	private Image createIcon(final String path, final String desc) {
		URL url = Main.class.getResource(path);
		return new ImageIcon(url, desc).getImage();
	}
	
}

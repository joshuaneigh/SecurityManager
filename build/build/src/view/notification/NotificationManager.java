/*
 * Security Manager
 * Copyright (C), Joshua Neighbarger, 2016
 */

package view.notification;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * This class will handle all {@link Notification}s for this application. This
 * class follows the Singleton design pattern to ensure that only one dock of
 * Notifications exist for this instance of the application.
 *
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 15 Nov 2016
 */
public class NotificationManager {

	/**
	 * The only instance of this.
	 */
	public static final NotificationManager INSTANCE;

	/**
	 * The maximum number of {@link Notification}s in this manager.
	 */
	private static final int MAX_NUM_NOTIFICATIONS;

	/**
	 * Holds all {@link Notification}s to be displayed.
	 */
	private final Queue<Notification> notifications;

	static {
		INSTANCE = new NotificationManager();
		/* TODO: Set max notifications in XML properties. */
		MAX_NUM_NOTIFICATIONS = 5;
	}

	/**
	 * Empty private constructor to prevent external instantiation of this.
	 */
	private NotificationManager() {
		notifications = new LinkedList<>();
	}

	/**
	 * Tries to add the {@link Notification} to the current manager. This
	 * feature exists for the instance that the client code wishes to set
	 * reminders for {@link Notification}s without needing to construct said
	 * {@link Notification} repeatedly.
	 * 
	 * @throws NullPointerException
	 *             if the passed {@link Notification} object is null
	 * @throws IllegalStateException
	 *             if the maximum number of {@link Notification}s has been
	 *             reached.
	 * @param n
	 *            the Notification object to add.
	 */
	public static void addNotification(final Notification n) {
		Objects.requireNonNull(n);
		if (getNumberNotifications() < MAX_NUM_NOTIFICATIONS) {
			INSTANCE.notifications.add(n);
		} else {
			throw new IllegalStateException("The maximmum number of Notifications has been reached.");
		}
	}

	public static void showNotification(final Notification n) {
		Objects.requireNonNull(n);
		if (!INSTANCE.notifications.contains(n)) {
			addNotification(n);
		}
	}

	/**
	 * Hides all {@link Notification}s from view.
	 */
	public static void hideAllNotifications() {

	}

	/**
	 * Dismisses all {@link Notification}s.
	 */
	public static void dismissAllNotifications() {
		INSTANCE.notifications.clear();
	}

	public static void dismissNotification() {
		// INSTANCE.notifications.dequeue();
	}

	public static void dismissNotification(final Notification n) {
		Objects.requireNonNull(n);
		if (INSTANCE.notifications.contains(n)) {
			INSTANCE.notifications.remove(n);
		} else {
			throw new IllegalStateException("The passed Notification could not be found by the manager.");
		}
	}

	/**
	 * Gets and returns the number of {@link Notification}s currently seen by
	 * the manager.
	 * 
	 * @return the int number of Notifications
	 */
	public static int getNumberNotifications() {
		return (INSTANCE.notifications.size());
	}

	/**
	 * 
	 * @throws NullPointerException
	 *             if the passed {@link NotificationLayout} is null.
	 * @param layout
	 */
	public static void setLayout(final NotificationLayout layout) {
		Objects.requireNonNull(layout);
	}

}

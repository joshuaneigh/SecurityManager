package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import util.security.EncryptedString;

/**
 * 
 *
 * @author Joshua Neighbarger | jneigh@uw.edu
 * @version 15 Dec 2016
 */
public class Entry {

	private final SimpleStringProperty title;
	private final SimpleStringProperty username;
	private final SimpleObjectProperty<EncryptedString> password;
	private final SimpleStringProperty url;
	private final SimpleStringProperty notes;
	private final SimpleObjectProperty<LocalDate> created;
	private final SimpleObjectProperty<LocalDate> modified;
	private final SimpleObjectProperty<LocalDate> expires;

	private Entry() {
		title = new SimpleStringProperty();
		username = new SimpleStringProperty();
		password = new SimpleObjectProperty<EncryptedString>();
		url = new SimpleStringProperty();
		notes = new SimpleStringProperty();
		created = new SimpleObjectProperty<>();
		modified = new SimpleObjectProperty<>();
		expires = new SimpleObjectProperty<>();

		setCreated(LocalDate.now());
		setModified(LocalDate.now());
	}

	public Entry(final String title, final String username, final EncryptedString password) {
		this();
		setTitle(title);
		setUsername(username);
		setPassword(password);
	}

	public Entry(final String title, final String username, final EncryptedString password, final String url,
			final String notes, final LocalDate expires) {
		this();
		setTitle(title);
		setUsername(username);
		setPassword(password);
		setUrl(url);
		setNotes(notes);
		setExpires(expires);
	}

	public void setTitle(final String title) {
		setModified(LocalDate.now());
		this.title.set(title);
	}

	public void setUsername(final String username) {
		setModified(LocalDate.now());
		this.username.set(username);
	}

	public void setPassword(final EncryptedString password) {
		setModified(LocalDate.now());
		this.password.set(password);
	}

	public void setUrl(final String url) {
		setModified(LocalDate.now());
		this.url.set(url);
	}

	public void setNotes(final String notes) {
		setModified(LocalDate.now());
		this.notes.set(notes);
	}

	private void setCreated(final LocalDate created) {
		this.created.set(created);
	}

	private void setModified(final LocalDate modified) {
		this.modified.set(modified);
	}

	public void setExpires(final LocalDate expires) {
		setModified(LocalDate.now());
		this.expires.set(expires);
	}

	public String getTitle() {
		return title.get();
	}

	public String getUsername() {
		return username.get();
	}

	public EncryptedString getPassword() {
		return password.get();
	}

	public String getUrl() {
		return url.get();
	}

	public String getNotes() {
		return notes.get();
	}

	public LocalDate getCreated() {
		return created.get();
	}

	public LocalDate getModified() {
		return modified.get();
	}

	public LocalDate getExpires() {
		return expires.get();
	}

	public SimpleStringProperty titleProperty() {
		return title;
	}

	public SimpleStringProperty usernameProperty() {
		return username;
	}

	public SimpleObjectProperty<EncryptedString> passwordProperty() {
		return password;
	}

	public SimpleStringProperty urlProperty() {
		return url;
	}

	public SimpleStringProperty notesProperty() {
		return notes;
	}

	public SimpleObjectProperty<LocalDate> createdProperty() {
		return created;
	}

	public SimpleObjectProperty<LocalDate> modifiedProperty() {
		return modified;
	}

	public SimpleObjectProperty<LocalDate> expiresProperty() {
		return expires;
	}

}

package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Entry implements Serializable {
	
	/**  */
	private static final long serialVersionUID = 4665585018550036619L;
	private SimpleStringProperty title;
	private SimpleStringProperty username;
	private SimpleStringProperty password;
	private SimpleStringProperty url;
	private SimpleStringProperty notes;
	private SimpleObjectProperty<LocalDate> expires;
	
	public Entry(final String title, final String username, final String password,
			final String url, final String notes, final LocalDate expires) {
		this.title = new SimpleStringProperty(title);
		this.username = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.url = new SimpleStringProperty(url);
		this.notes = new SimpleStringProperty(notes);
		this.expires = new SimpleObjectProperty<>(expires);
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(getTitle());
        oos.writeObject(getUsername());
        oos.writeObject(getPassword());
        oos.writeObject(getUrl());
        oos.writeObject(getNotes());
        oos.writeObject(getExpires());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        title = new SimpleStringProperty((String) ois.readObject());
        username = new SimpleStringProperty((String) ois.readObject());
        password = new SimpleStringProperty((String) ois.readObject());
        url = new SimpleStringProperty((String) ois.readObject());
        notes = new SimpleStringProperty((String) ois.readObject());
        expires = new SimpleObjectProperty<LocalDate>((LocalDate) ois.readObject());
    }
	
	public String getTitle() {
		return title.get();
	}
	
	public String getUsername() {
		return username.get();
	}
	
	public String getPassword() {
		return password.get();
	}
	
	public String getUrl() {
		return url.get();
	}
	
	public String getNotes() {
		return notes.get();
	}
	
	public LocalDate getExpires() {
		return expires.get();
	}
	
	public void setTitle(String text) {
		title.set(text);
	}

	public void setUsername(String text) {
		username.set(text);
	}

	public void setPassword(String text) {
		password.set(text);
	}

	public void setUrl(String text) {
		url.set(text);
	}

	public void setNotes(String text) {
		notes.set(text);
	}

	public void setExpires(LocalDate value) {
		expires.set(value);
	}
	
	

}

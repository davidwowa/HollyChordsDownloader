package action;

import java.io.IOException;

import bo.Song;
import data.Loader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LoadDataFromURLAction implements EventHandler<ActionEvent> {

	private TextField text;
	private ObservableList<Song> songs;
	private TableView<Song> table;

	public LoadDataFromURLAction(TextField text, ObservableList<Song> songs, TableView<Song> table) {
		this.text = text;
		this.songs = songs;
		this.table = table;
	}

	@Override
	public void handle(ActionEvent event) {
		Loader loader = new Loader();
		try {
			songs = loader.loadData(text.getText());
			table.setItems(songs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
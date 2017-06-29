package holychordsdownloader.action;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import holychordsdownloader.bo.Song;
import holychordsdownloader.data.Loader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LoadDataFromURLAction implements EventHandler<ActionEvent> {

	private Logger logger = LoggerFactory.getLogger(LoadDataFromURLAction.class);

	private TextField text;
	private ObservableList<Song> songs;
	private TableView<Song> table;

	public LoadDataFromURLAction(TextField text, ObservableList<Song> songs, TableView<Song> table) {
		if (logger.isDebugEnabled()) {
			logger.debug("create load data from URL action");
		}
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
			if (logger.isErrorEnabled()) {
				logger.error("error on refreshing data", e);
			}
		}
	}
}
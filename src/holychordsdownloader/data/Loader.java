package holychordsdownloader.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import holychordsdownloader.bo.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

public class Loader {

	private Logger logger = LoggerFactory.getLogger(Loader.class);

	public ObservableList<Song> loadData() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("load all data from holy chords");
		}

		Document site = Jsoup.connect("http://holychords.com/musics").get();

		ObservableList<Song> list = FXCollections.observableArrayList();

		if (logger.isDebugEnabled()) {
			logger.debug("parsing HTML...");
		}

		Elements elelemts = site.select("h1");

		if (elelemts != null) {
			for (Element currentElement : elelemts) {
				String href = currentElement.toString();
				if (href.contains(".mp3")) {

					Song song = new Song();

					String[] allElementsInHref = href.split("\"");
					String[] arrayWithTitleName = allElementsInHref[12].split(">");
					arrayWithTitleName = arrayWithTitleName[1].split("<");
					String clearedName = arrayWithTitleName[0].replaceAll("\\(", "");
					clearedName = clearedName.replaceAll("\\)", "");
					clearedName = clearedName.replaceAll(" ", "_");
					clearedName = clearedName.replaceAll("'", "");

					song.setName(clearedName);
					song.setUrl("http://holychords.com" + allElementsInHref[1]);
					song.setInterpreter("TODO");

					if (logger.isDebugEnabled()) {
						logger.debug("found song " + song.getName());
					}

					list.add(song);
				}
			}
			return list;
		} else {
			return null;
		}
	}

	public ObservableList<Song> loadData(String url) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("load all data from holy chords");
		}
		Document site = Jsoup.connect(url).get();

		ObservableList<Song> list = FXCollections.observableArrayList();

		if (logger.isDebugEnabled()) {
			logger.debug("parsing HTML...");
		}

		Elements elelemts = site.select("h1");

		if (elelemts != null) {
			for (Element currentElement : elelemts) {
				String href = currentElement.toString();
				if (href.contains(".mp3")) {

					Song song = new Song();

					String[] allElementsInHref = href.split("\"");
					String[] arrayWithTitleName = allElementsInHref[12].split(">");
					arrayWithTitleName = arrayWithTitleName[1].split("<");
					String clearedName = arrayWithTitleName[0].replaceAll("\\(", "");
					clearedName = clearedName.replaceAll("\\)", "");
					clearedName = clearedName.replaceAll(" ", "_");
					clearedName = clearedName.replaceAll("'", "");

					song.setName(clearedName);
					song.setUrl("http://holychords.com" + allElementsInHref[1]);
					song.setInterpreter("TODO");

					if (logger.isDebugEnabled()) {
						logger.debug("found song " + song.getName());
					}

					list.add(song);
				}
			}
			return list;
		} else {
			return null;
		}
	}

	public void download(String url, String path, String name, Text text) {
		String fileName = path + File.separator + name;
		if (logger.isDebugEnabled()) {
			logger.debug("downloading in " + fileName + " from " + url);
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					text.setText("Download " + fileName);
					FileUtils.copyURLToFile(new URL(url), new File(fileName));
					text.setText("Saved in " + fileName);
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error("error on file downloading from " + url, e);
					}
				}
			}
		};
		Platform.runLater(runnable);
	}
}
package data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bo.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Loader {

	public ObservableList<Song> loadData() throws IOException {
		Document site = Jsoup.connect("http://holychords.com/musics").get();

		ObservableList<Song> list = FXCollections.observableArrayList();

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

					list.add(song);
				}
			}
			return list;
		} else {
			return null;
		}
	}
}

package holychordsdownloader.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestJSoup {

	public static void main(String[] args) throws IOException {
		// byte[] encoded =
		// Files.readAllBytes(Paths.get("/Users/David/Desktop/m"));
		// String html = new String(encoded);

		Document site1 = Jsoup.connect("http://holychords.com/musics").get();
		// Document site1 =
		// Jsoup.connect("http://holychords.com/Виталий+Ефремочкин").get();
		// Document site2 = Jsoup.parse(html);

		StringBuilder builder = new StringBuilder();

		buldScript(site1, builder);
		// buldScript(site2, builder);
	}

	private static void buldScript(Document site, StringBuilder stringBuilder) {
		Elements elelemts = site.select("h1");

		if (elelemts != null) {
			for (Element currentElement : elelemts) {
				String href = currentElement.toString();
				if (href.contains(".mp3")) {
					String[] allElementsInHref = href.split("\"");
					String[] arrayWithTitleName = allElementsInHref[12].split(">");
					arrayWithTitleName = arrayWithTitleName[1].split("<");
					String clearedName = arrayWithTitleName[0].replaceAll("\\(", "");
					clearedName = clearedName.replaceAll("\\)", "");
					clearedName = clearedName.replaceAll(" ", "_");
					clearedName = clearedName.replaceAll("'", "");
					stringBuilder.append("wget ");
					stringBuilder.append("-O ");
					stringBuilder.append(clearedName);
					stringBuilder.append(".mp3");
					stringBuilder.append(" http://holychords.com");
					stringBuilder.append(allElementsInHref[1]);
					stringBuilder.append("\n");
				}
			}
			System.out.println(stringBuilder.toString());
		} else {
			System.out.println("no elemetns found");
		}
	}
}
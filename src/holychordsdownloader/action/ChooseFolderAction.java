package holychordsdownloader.action;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ChooseFolderAction implements EventHandler<ActionEvent> {

	private Logger logger = LoggerFactory.getLogger(ChooseFolderAction.class);

	private TextField text;
	private Stage stage;

	public ChooseFolderAction(TextField text, Stage stage) {
		if (logger.isDebugEnabled()) {
			logger.debug("create choose folder action");
		}
		this.text = text;
		this.stage = stage;
	}

	@Override
	public void handle(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("no directory for download selected");
			}
			// TODO Warning message on user
			text.setText("No Directory selected");
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("selected folder " + selectedDirectory.getAbsolutePath());
			}
			text.setText(selectedDirectory.getAbsolutePath());
		}
	}
}

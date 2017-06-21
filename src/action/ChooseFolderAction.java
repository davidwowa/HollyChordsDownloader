package action;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ChooseFolderAction implements EventHandler<ActionEvent> {

	private TextField text;
	private Stage stage;

	public ChooseFolderAction(TextField text, Stage stage) {
		this.text = text;
		this.stage = stage;
	}

	@Override
	public void handle(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory == null) {
			text.setText("No Directory selected");
		} else {
			text.setText(selectedDirectory.getAbsolutePath());
		}
	}
}

package holychordsdownloader.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import holychordsdownloader.action.ChooseFolderAction;
import holychordsdownloader.action.LoadDataFromURLAction;
import holychordsdownloader.bo.Song;
import holychordsdownloader.data.Loader;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Start extends Application {

	private static Logger logger = LoggerFactory.getLogger(Start.class);

	private ObservableList<Song> songs;
	private TableView<Song> commonTable = new TableView<>(songs);
	private Text messageField = new Text();

	private Loader loader = new Loader();

	public static void main(String[] args) {
		logger.info("start app holy chords");
		launch(args);
	}

	private void initData() {
		if (logger.isDebugEnabled()) {
			logger.debug("init app");
		}
		try {
			songs = loader.loadData();
		} catch (IOException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on data loading ", e);
			}
		}
	}

	@Override
	public void start(Stage primaryStage) {
		initData();
		if (logger.isDebugEnabled()) {
			logger.debug("init UI");
		}
		primaryStage.setTitle("HolyChords Downloader v.0.1");

		GridPane commonGrid = new GridPane();
		commonGrid.setAlignment(Pos.TOP_CENTER);
		commonGrid.setHgap(10);
		commonGrid.setVgap(10);
		commonGrid.setPadding(new Insets(25, 25, 25, 25));

		Scene commonScene = new Scene(commonGrid, 600, 600);

		// commonScene.getStylesheets().add("resources/css/button.css");
		commonScene.getStylesheets().add(Start.class.getResource("button.css").toExternalForm());

		Text scenetitle = new Text("Welcome to HolyChords Downloader v.0.1");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		commonGrid.add(scenetitle, 0, 0, 2, 1);

		Label labelDownloadFolder = new Label("Download Folder:");
		commonGrid.add(labelDownloadFolder, 0, 1);

		TextField fieldDownloadFolder = new TextField();
		commonGrid.add(fieldDownloadFolder, 1, 1);

		Button buttonChooseFolder = new Button("Choose Folder");
		buttonChooseFolder.getStyleClass().add("button_choose_folder");
		commonGrid.add(buttonChooseFolder, 2, 1);

		buttonChooseFolder.setOnAction(new ChooseFolderAction(fieldDownloadFolder, primaryStage));

		Label labelURL = new Label("URL:");
		commonGrid.add(labelURL, 0, 2);

		TextField fieldURL = new TextField();
		commonGrid.add(fieldURL, 1, 2);

		Button buttonLoad = new Button("Load");
		buttonLoad.getStyleClass().add("button_load_url");
		commonGrid.add(buttonLoad, 2, 2);

		buttonLoad.setOnAction(new LoadDataFromURLAction(fieldURL, songs, commonTable));

		commonTable.setEditable(false);

		// TableColumn<Song, String> colInterpreter = new
		// TableColumn<>("Interpreter");
		// colInterpreter.setCellValueFactory(new PropertyValueFactory<Song,
		// String>("interpreter"));

		TableColumn<Song, String> colName = new TableColumn<>("Name");
		colName.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));

		TableColumn<Song, String> colAction = new TableColumn<>("Action");
		// https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view
		colAction.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Song, String>, TableCell<Song, String>> cellFactory = //
				new Callback<TableColumn<Song, String>, TableCell<Song, String>>() {
					@Override
					public TableCell call(final TableColumn<Song, String> param) {
						final TableCell<Song, String> cell = new TableCell<Song, String>() {

							final Button buttonDownload = new Button("Download");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									buttonDownload.getStyleClass().add("button_download");
									buttonDownload.setOnAction((ActionEvent event) -> {
										Song song = getTableView().getItems().get(getIndex());

										String name = song.getName() + ".mp3";
										messageField.setText("download : " + name);
										loader.download(song.getUrl(), fieldDownloadFolder.getText(), name,
												messageField);
									});
									setGraphic(buttonDownload);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		colAction.setCellFactory(cellFactory);

		TableColumn<Song, String> colPlay = new TableColumn<>("Play");
		// https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view
		colPlay.setCellValueFactory(new PropertyValueFactory<>("DUMMY2"));

		Callback<TableColumn<Song, String>, TableCell<Song, String>> cellFactoryPlay = //
				new Callback<TableColumn<Song, String>, TableCell<Song, String>>() {
					@Override
					public TableCell call(final TableColumn<Song, String> param) {
						final TableCell<Song, String> cell = new TableCell<Song, String>() {
							Media pik;
							MediaPlayer mediaPlayer;

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {

									Button playButton = new Button("Play");
									playButton.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent event) {
											Song song = getTableView().getItems().get(getIndex());

											if (pik == null && mediaPlayer == null) {
												pik = new Media(song.getUrl());
												mediaPlayer = new MediaPlayer(pik);
											}

											if (Status.PLAYING != mediaPlayer.getStatus()) {
												if (logger.isDebugEnabled()) {
													logger.debug("playing " + song.getUrl());
												}
												messageField.setText("playing : " + song.getName());
												playButton.setText("Stop");
												mediaPlayer.play();
											} else {
												playButton.setText("Play");
												mediaPlayer.stop();
											}
										}
									});
									setGraphic(playButton);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		colPlay.setCellFactory(cellFactoryPlay);

		commonTable.setItems(songs);

		commonTable.getColumns().addAll(colName, colAction, colPlay);

		Label tableLabel = new Label("Files:");
		tableLabel.setFont(new Font("Arial", 14));

		VBox tableVBox = new VBox();
		tableVBox.setSpacing(5);
		tableVBox.setPadding(new Insets(10, 0, 0, 10));
		tableVBox.getChildren().addAll(tableLabel, commonTable);

		commonGrid.add(tableVBox, 0, 3, 3, 1);

		Button buttonDownloadAll = new Button("Download All Songs");
		buttonDownloadAll.getStyleClass().add("button_download_all");
		commonGrid.add(buttonDownloadAll, 0, 4);

		commonGrid.add(messageField, 0, 5);

		primaryStage.setScene(commonScene);
		primaryStage.show();
	}
}
package main;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.ChooseFolderAction;
import action.LoadDataFromURLAction;
import bo.Song;
import data.Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

	public static void main(String[] args) {
		logger.info("start");
		launch(args);
	}

	private void initData() {
		Loader loader = new Loader();
		try {
			songs = loader.loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		initData();
		primaryStage.setTitle("HolyChords Downloader v.0.1");

		GridPane commonGrid = new GridPane();
		commonGrid.setAlignment(Pos.TOP_CENTER);
		commonGrid.setHgap(10);
		commonGrid.setVgap(10);
		commonGrid.setPadding(new Insets(25, 25, 25, 25));

		Scene commonScene = new Scene(commonGrid, 1000, 700);

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
										System.out.println(song.getUrl() + "   " + song.getName());

										String name = song.getName() + ".mp3";

										download(song.getUrl(), fieldDownloadFolder.getText(), name, messageField);
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

		commonTable.setItems(songs);

		commonTable.getColumns().addAll(colName, colAction);

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

	private void download(String url, String path, String name, Text text) {
		String fileName = path + File.separator + name;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					text.setText("Download " + fileName);
					FileUtils.copyURLToFile(new URL(url), new File(fileName));
					text.setText("Saved in " + fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Platform.runLater(runnable);
	}
}
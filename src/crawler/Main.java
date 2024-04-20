package crawler;

import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	Utils utils = new Utils();
    private CrawlerOnePiece crawlerOnePiece;
    private TableView<Chapter> table;

    @Override
    public void start(Stage primaryStage) {
        final String URL = "https://www.nettruyenup.vn/truyen-tranh/dao-hai-tac-11402";
        crawlerOnePiece = new CrawlerOnePiece(URL);

        TableColumn<Chapter, String> chapNumberColumn = new TableColumn<>("ChapNumber");
        chapNumberColumn.setCellValueFactory(new PropertyValueFactory<>("chapNumber"));
        chapNumberColumn.setPrefWidth(200);
        TableColumn<Chapter, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setPrefWidth(700);

        table = new TableView<>();
        table.getColumns().add(chapNumberColumn);
        table.getColumns().add(urlColumn);

        Menu menu = new Menu("Menu");
        MenuItem item1 = new MenuItem("1. Get list chap");
        item1.setOnAction(event -> updateTable());
        MenuItem item2 = new MenuItem("2. Get all chap");
        item2.setOnAction(event -> {
            new Thread(() -> {
                for (int i = 0 ; i < crawlerOnePiece.getListChaps().size() ; i++) {
                    Chapter chapter = crawlerOnePiece.getListChaps().get(i);
                    crawlerOnePiece.saveFile(chapter);
                    System.out.println("Successfully crawl on chapter " + chapter.getChapNumber());
                    try { 
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        MenuItem item3 = new MenuItem("3. Get chap by chapID");
        item3.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Dialog");
            dialog.setHeaderText("Get chap by chapID");
            dialog.setContentText("Please enter the chapID:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(chapID -> {
                if (utils.checkIntInput(chapID) == true) {
                    int chapIdInt = Integer.parseInt(chapID);
                    if (chapIdInt >= 1 && chapIdInt <= crawlerOnePiece.getListChaps().size()) {
                        Chapter chapter = crawlerOnePiece.getChapterByChapNumber(chapID);
                        crawlerOnePiece.saveFile(chapter);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Download Complete");
                        alert.setHeaderText(null);
                        alert.setContentText("Downloaded chapter " + chapID);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid chapID. Please enter a number between 1 and " + crawlerOnePiece.getListChaps().size());
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid chapID. Please enter a number.");
                    alert.showAndWait();
                }
            });
        });

        MenuItem item4 = new MenuItem("4. Exit");
        item4.setOnAction(event -> System.exit(0));
        menu.getItems().addAll(item1, item2, item3, item4);

        // Create menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        VBox vbox = new VBox(menuBar, table);
        Scene scene = new Scene(vbox, 800, 600);

        primaryStage.setTitle("Crawler One Piece");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateTable() {
    	    try {
    	        // Get the list of chapters
    	        List<Chapter> chapters = crawlerOnePiece.getListChaps();
    	        ObservableList<Chapter> data = FXCollections.observableArrayList(chapters);
    	        table.setItems(data);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        System.out.println("Can't get the list of chapters");
    	    }
    	}

    public static void main(String[] args) {
        launch(args);
    }
}

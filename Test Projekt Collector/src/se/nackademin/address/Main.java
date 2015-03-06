package se.nackademin.address;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import se.nackademin.address.model.VinylRecords;
import se.nackademin.address.model.VinylWishList;
import se.nackademin.address.view.RecordEditDialogController;
import se.nackademin.address.view.VinylRecordController;
import se.nackademin.address.view.WishListEditController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	final ObservableList<VinylRecords> vinylRecordData = FXCollections.observableArrayList();
	final ObservableList<VinylWishList> wishListData = FXCollections.observableArrayList();


	public Main(){

		ResultSet rs = null;

		try(
				Connection conn = DriverManager.getConnection("jdbc:SQLite:db/collectionDB.db");
				Statement stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY); 
				Statement stm2 = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				) {

			rs = stm.executeQuery("select * from vinyl_wishlist");

			while(rs.next()){
				wishListData.add(new VinylWishList(rs.getString("album"), rs.getString("artist"), rs.getString("label"), rs.getString("release_year")));
			}

			rs = stm2.executeQuery(("select * from vinyl_record"));

			while(rs.next()){
				vinylRecordData.add(new VinylRecords(rs.getString("album"), rs.getString("artist"), rs.getString("label"), rs.getString("release_year"), rs.getString("album_cover")));
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public ObservableList<VinylRecords> getVinylRecordData() {
		return vinylRecordData;
	}
	public ObservableList<VinylWishList> getVinylWishListData() {
		return wishListData;
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Collection Library");	
		this.primaryStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
		//		this.primaryStage.getIcons().add(new Image(ClassLoader.getSystemResource("file-manager.png").toString()));

		initRootLayout();
		showRecordOverview();	
	}

	public void initRootLayout(){
		try {
			//Load the layout from FXML-File
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();

			//Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void showRecordOverview(){
		try {
			//Load the RecordOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/VinylRecordOverview.fxml"));
			TabPane recordOverview = (TabPane)loader.load();

			//Set RecordOverview in the center of root layout
			rootLayout.setCenter(recordOverview);
			VinylRecordController controller = loader.getController();
			controller.setMain(this);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public boolean showRecordAddDialog(VinylRecords vinylRecord){

		try{
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RecordAddDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Vinyl Record");
			dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setResizable(false);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the Record into the controller.
			RecordEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setVinylRecord(vinylRecord);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}	
	}

	public boolean showRecordAddWLDialog(VinylWishList wishList){

		try{
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/WishListEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Vinyl Record To Wish List");
			dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setResizable(false);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the Record into the controller.
			WishListEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setWishList(wishList);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}	
	}
	public boolean showRecordEditDialog(VinylRecords vinylRecord){

		try{
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RecordEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Vinyl Record");
			dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the Record into the controller.
			RecordEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setVinylRecord(vinylRecord);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}	
	}
	public boolean showWishListEditDialog(VinylWishList wishList){

		try{
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/WishListEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Wish List Record");
			dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the Record into the controller.
			WishListEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setWishList(wishList);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}	
	}
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}

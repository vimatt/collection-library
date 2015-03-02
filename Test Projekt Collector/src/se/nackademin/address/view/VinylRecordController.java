package se.nackademin.address.view;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;

import javax.xml.stream.events.StartDocument;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import se.nackademin.address.Main;
import se.nackademin.address.model.VinylRecords;
import se.nackademin.address.model.VinylWishList;


@SuppressWarnings("deprecation")
public class VinylRecordController {


	//Define Labels
	@FXML private Label albumLabel;
	@FXML private Label artistLabel;
	@FXML private Label recordLabelLabel;
	@FXML private Label releaseYearLabel;
	//static variables that can be reached from RecordEditDialogController
	public static String album = null;
	public static String artist = null;
	public static String recordlabel = null;
	public static String release = null;
	//Define Table
	@FXML TableView<VinylRecords> tableID;
	@FXML TableColumn<VinylRecords, String> albumColumn;
	@FXML TableColumn<VinylRecords, String> artistColumn;
	@FXML TableColumn<VinylRecords, String> recordLabelColumn;
	@FXML TableColumn<VinylRecords, String> releaseYearColumn;
	//Table for Wishlist
	@FXML TableView<VinylWishList> wl_tableID;
	@FXML TableColumn<VinylWishList, String> wl_albumColumn;
	@FXML TableColumn<VinylWishList, String> wl_artistColumn;
	@FXML TableColumn<VinylWishList, String> wl_recordLabelColumn;
	@FXML TableColumn<VinylWishList, String> wl_releaseYearColumn;
	//Define ImageView
	@FXML private ImageView albumCover;

	private DatabaseController dbController = new DatabaseController();
	private Main main;

	public VinylRecordController(){
	}

	@FXML
	public void initialize() {

		albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
		artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
		recordLabelColumn.setCellValueFactory(cellData -> cellData.getValue().recordLabelProperty());
		releaseYearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty());

		wl_albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
		wl_artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
		wl_recordLabelColumn.setCellValueFactory(cellData -> cellData.getValue().recordLabelProperty());
		wl_releaseYearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty());

		//Clear record Details
		showVinylRecordDetails(null);

		//Listen for selection changes and  show the record details when changed
		tableID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) 
				-> showVinylRecordDetails(newValue));

		tableID.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)){
					if(event.getClickCount() == 2){
						handleEditPerson();
					}
				}
			}
		});

		wl_tableID.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)){
					if(event.getClickCount() == 2){
						handleEditWishList();
					}
				}
			}
		});

	}

	public void setMain(Main main){
		this.main = main;
		tableID.setItems(main.getVinylRecordData());
		wl_tableID.setItems(main.getVinylWishListData());
	}

	private void showVinylRecordDetails(VinylRecords vinylRecord){
		if(vinylRecord != null){
			albumLabel.setText(vinylRecord.getAlbum());
			artistLabel.setText(vinylRecord.getArtist());
			recordLabelLabel.setText(vinylRecord.getRecordLabel());
			releaseYearLabel.setText(vinylRecord.getReleaseYear());	
			albumCover.setImage(vinylRecord.getAlbumCover());
			album = albumLabel.getText();
			artist = artistLabel.getText();
			recordlabel = recordLabelLabel.getText();
			release = releaseYearLabel.getText();
		}
		else{
			albumLabel.setText("");
			artistLabel.setText("");
			recordLabelLabel.setText("");
			releaseYearLabel.setText("");
		}
	}

	@FXML
	private void handleDeleteRecord() throws IOException{

		int selectedIndex = tableID.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			if(confirmDelete()){
				VinylRecords v = tableID.getItems().get(selectedIndex);
				String album = v.getAlbum();
				String artist = v.getArtist();

				dbController.deleteVinylFromDatabase(album, artist);
				tableID.getItems().remove(selectedIndex);
			}
		}
		else{
			Dialogs.create()
			.title("No selection")
			.masthead("No object selected")
			.message("Please selecet an object in the table")
			.showWarning();
		}
	}

	@FXML
	private void handleNewRecord() {
		VinylRecords tempRecord = new VinylRecords();
		boolean okClicked = main.showRecordAddDialog(tempRecord);
		if (okClicked) {
			main.getVinylRecordData().add(tempRecord);
		}
	}

	@FXML
	private void handleEditPerson() {
		VinylRecords selectedRecord = tableID.getSelectionModel().getSelectedItem();
		if (selectedRecord != null) {
			boolean okClicked = main.showRecordEditDialog(selectedRecord);
			if (okClicked) {
				showVinylRecordDetails(selectedRecord);
			}
		}
	}

	@FXML
	private void openWebpage() {
		int selectedIndex = tableID.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			VinylRecords r = tableID.getItems().get(selectedIndex);
			String album = r.getAlbum().replace(" ", "%20");
			String artist = r.getArtist().replace(" ", "%20");
			try 
			{
				Desktop.getDesktop().browse(new URL("http://www.allmusic.com/search/all/" + album + "%20" + artist).toURI());
			}           
			catch (Exception e) {}
		}
	}

	private boolean confirmDelete(){
		boolean confirm = false;

		Action response = Dialogs.create()
				.title("Confirm Dialog")
				.masthead("Delete Confirmation")
				.message("Are you sure you want to delete this entry?")
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
				.showConfirm();

		if (response == Dialog.ACTION_YES) {
			confirm = true;
		} else {
			confirm = false;
		}
		return confirm;
	}

	@FXML
	private void showEnlargedImage(){
		int selectedIndex = tableID.getSelectionModel().getSelectedIndex();
		//If there is no index selected, son't run any further code and return
		if(selectedIndex == -1)
			return;
		VinylRecords rec = tableID.getItems().get(selectedIndex);
		// Load the fxml file and create a new stage for the popup dialog.
		if(rec.getAlbumCoverString() != null ){

			try{

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/EnlargeImage.fxml"));
				AnchorPane pane = (AnchorPane) loader.load();

				Stage dialogStage = new Stage();
				dialogStage.setTitle(rec.getAlbum() + " - " + rec.getArtist());
				dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
				dialogStage.initModality(Modality.APPLICATION_MODAL);
				dialogStage.setResizable(false);
				dialogStage.setY(250);
				dialogStage.setX(450);
				Scene scene = new Scene(pane);
				dialogStage.setScene(scene);
				dialogStage.show();

				String imgStr = rec.getAlbumCoverString();
				Image img;
				if(imgStr.equals("C:/aVictor/git/eget-projekt-oop/Test Projekt Collector/resources/images/default image.png"))
					img = new Image(imgStr);
				else
					img = new Image("file:///" + imgStr);
				double imgH = img.getHeight();
				double imgW = img.getWidth();
				if(imgH > 600) imgH = 600;
				else if(imgH < 300) imgH = 300;
				dialogStage.setHeight(imgH + 30);
				if(imgW > 600) imgW = 600;
				else if(imgW < 300) imgW = 300;
				dialogStage.setWidth(imgW );
				ImageController controller = loader.getController();

				controller.setImage(img, dialogStage);

			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/*START OF WISHLIST METHODS*/


	@FXML
	private void handleDeleteWishList() throws IOException{
		int selectedIndex = wl_tableID.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			if(confirmDeleteWL()){
				wl_tableID.getItems().remove(selectedIndex);
			}
		}
		//If it's not a valid selection, i.e it's under 0 we show an error message
		else{
			Dialogs.create()
			.title("No selection")
			.masthead("No object selected")
			.message("Please selecet an object in the table")
			.showWarning();
		}
	}

	@FXML
	private void handleNewWish() {
		VinylWishList tempRecord = new VinylWishList();
		boolean okClicked = main.showRecordAddWLDialog(tempRecord);
		if (okClicked) {
			main.getVinylWishListData().add(tempRecord);
		}
	}

	@FXML
	private void handleEditWishList() {
		VinylWishList selectedRecord = wl_tableID.getSelectionModel().getSelectedItem();
		if (selectedRecord != null) {
			main.showWishListEditDialog(selectedRecord);
		}
	}

	private boolean confirmDeleteWL(){
		boolean confirm = false;

		Action response = Dialogs.create()
				.title("Confirm Dialog")
				.masthead("Delete Confirmation")
				.message("Are you sure you want to delete this entry?")
				.actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
				.showConfirm();

		if (response == Dialog.ACTION_YES) {
			confirm = true;
		} else {
			confirm = false;
		}
		return confirm;
	}
}

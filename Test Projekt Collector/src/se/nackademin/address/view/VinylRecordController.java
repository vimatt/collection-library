package se.nackademin.address.view;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import se.nackademin.address.Main;
import se.nackademin.address.model.VinylRecords;
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


@SuppressWarnings("deprecation")
public class VinylRecordController {


	//Define TextFields
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

	//Define ImageView
	@FXML private ImageView albumCover;

	private Main main;

	public VinylRecordController(){
	}

	@FXML
	public void initialize() {
		
		albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
		artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
		recordLabelColumn.setCellValueFactory(cellData -> cellData.getValue().recordLabelProperty());
		releaseYearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty());

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
	}


	public void setMain(Main main){
		this.main = main;
		tableID.setItems(main.getVinylRecordData());
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
		//If it's a valid selection we remove it and create a temporary file that we later 
		//rename and replace over the old file
		if(selectedIndex >= 0){
			if(confirmDelete()){
				String filename = "records.txt";
				Scanner s = null;
				try{
					s =  new Scanner(new BufferedReader(new FileReader(filename)));
					s.useDelimiter(";");
					String line;
					ArrayList<String>list = new ArrayList<String>();
					int cnt = 0;
					String fileName = "records.tmp";

					FileWriter fileWriter = new FileWriter(fileName);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					while(s.hasNext()){
						line = s.next();
						list.add(line);
						cnt++;
						if(cnt == 5){
							VinylRecords v = tableID.getItems().get(selectedIndex);
							if(list.get(0).equals(v.getAlbum()) && list.get(1).equals(v.getArtist()) 
									&& list.get(2).equals(v.getRecordLabel()) && list.get(3).equals(v.getReleaseYear()) 
									&& list.get(4).equals("file:///" + v.getAlbumCoverString()) || list.get(4).equals("")){

							} else {

								try {
									// Always wrap FileWriter in BufferedWriter.                          	            
									bufferedWriter.write(list.get(0)+";");
									bufferedWriter.write(list.get(1)+";");
									bufferedWriter.write(list.get(2)+";");
									bufferedWriter.write(list.get(3)+";");
									bufferedWriter.write(list.get(4)+";");

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							cnt = 0;
							list.clear();
						}             
					}
					bufferedWriter.close();			
				}
				finally{

					if(s != null){    				
						s.close();
						File file = new File(filename);
						file.delete();
						File newfile = new File("records.tmp");
						newfile.renameTo(new File("records.txt"));
					}
				}

				tableID.getItems().remove(selectedIndex);
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
}

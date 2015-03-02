package se.nackademin.address.view;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import se.nackademin.address.model.VinylRecords;
@SuppressWarnings("deprecation")


public class RecordEditDialogController {

	//Dialog to edit details of a Record.
	@FXML private TextField albumField;
	@FXML private TextField artistField;
	@FXML private TextField recordLabelField;
	@FXML private TextField releaseYearField;
	@FXML private TextField albumCoverField;
	@FXML private Button browseAlbumCover, okBtn;

	private Stage dialogStage;
	private VinylRecords vinylRecord;
	private boolean okClicked = false;
	private DatabaseController dbController = new DatabaseController();

	@FXML
	private void initialize() {
	}

	//Sets the stage of the dialog
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	//Sets the record to be edited in the dialog.	    
	public void setVinylRecord(VinylRecords vinylRecord) {
		this.vinylRecord = vinylRecord;

		albumField.setText(vinylRecord.getAlbum());
		artistField.setText(vinylRecord.getArtist());
		recordLabelField.setText(vinylRecord.getRecordLabel());
		releaseYearField.setText(vinylRecord.getReleaseYear());       
		albumCoverField.setText(vinylRecord.getAlbumCoverString());
	}

	//Returns true if the user clicked OK, false otherwise.	   
	public boolean isOkClicked() {
		return okClicked;
	}

	//called when the user clicks ok.	     
	@FXML
	private void handleOk() {
		if(isInputValid()){
			String album = albumField.getText();
			String artist = artistField.getText();
			String record = recordLabelField.getText();
			String release = releaseYearField.getText();
			String cover = albumCoverField.getText();

			vinylRecord.setAlbum(album);
			vinylRecord.setArtist(artist);
			vinylRecord.setRecordLabel(record);
			vinylRecord.setReleaseYear(release);
			if(cover != null){
				vinylRecord.setAlbumCover(cover.replace("\\", "/"));
				cover = cover.replace("\\", "/");
			}
			else{
				vinylRecord.setAlbumCover("");
				cover = "";
			}

			dbController.addVinylToDatabase(album, artist, record, release, cover);

			okClicked = true;
			dialogStage.close();	
		}
	}

	@FXML
	private void handleOkEdit() throws IOException {
		if(isInputValid()){
			String oldAlbum = vinylRecord.getAlbum();
			String oldArtist = vinylRecord.getArtist();
			String album = albumField.getText();
			String artist = artistField.getText();
			String rLabel = recordLabelField.getText();
			String release = releaseYearField.getText();
			String cover = albumCoverField.getText();

			vinylRecord.setAlbum(albumField.getText());
			vinylRecord.setArtist(artistField.getText());
			vinylRecord.setRecordLabel(recordLabelField.getText());
			vinylRecord.setReleaseYear(releaseYearField.getText());
			if(albumCoverField.getText() != null){
				vinylRecord.setAlbumCover(cover.replace("\\", "/"));
				cover = cover.replace("\\", "/");
			}
			else{
				vinylRecord.setAlbumCover("");
				cover = "";
			}

			dbController.updateVinylToDatabase(album, artist, rLabel, release, cover, oldAlbum, oldArtist);

			okClicked = true;
			dialogStage.close();
		}
	}

	//Method to choose a image file and set the text to it's location
	@FXML
	private void browseAlbumCover(){
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose An Album Cover");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			String imageDirectory = file.toString();
			albumCoverField.setText(imageDirectory);
		} 
	}

	//Called when the user clicks cancel.
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	//Validates the user input in the text fields, @return true if the input is valid     
	private boolean isInputValid() {
		String errorMessage = "";

		if (albumField.getText() == null || albumField.getText().length() == 0) {
			errorMessage += "Not a valid album name!\n"; 
		}
		if (artistField.getText() == null || artistField.getText().length() == 0) {
			errorMessage += "Not a valid artist name!\n"; 
		}
		if (recordLabelField.getText() == null || recordLabelField.getText().length() == 0) {
			errorMessage += "Not a valid record label!\n"; 
		}

		if (releaseYearField.getText() == null || releaseYearField.getText().length() == 0 || releaseYearField.getText().length() < 4
				|| releaseYearField.getText().length() > 4) {
			errorMessage += "Not a valid release year!\n"; 
		} 

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Dialogs.create()
			.title("Invalid Fields")
			.masthead("Please correct invalid fields")
			.message(errorMessage)
			.showError();
			return false;
		}
	}

	@FXML
	private void clearBrowse(){
		albumCoverField.setText("");
	}
}


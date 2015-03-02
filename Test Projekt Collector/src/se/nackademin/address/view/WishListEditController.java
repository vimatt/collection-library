package se.nackademin.address.view;

import java.io.IOException;

import org.controlsfx.dialog.Dialogs;

import se.nackademin.address.model.VinylWishList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class WishListEditController {
	
	
	@FXML private TextField albumField;
	@FXML private TextField artistField;
	@FXML private TextField recordLabelField;
	@FXML private TextField releaseYearField;
	
	private VinylWishList wishList;
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {

	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setWishList(VinylWishList wishList) {
		this.wishList = wishList;

		albumField.setText(wishList.getAlbum());
		artistField.setText(wishList.getArtist());
		recordLabelField.setText(wishList.getRecordLabel());
		releaseYearField.setText(wishList.getReleaseYear());       
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	
	
	@FXML
	private void handleOkEditWL() throws IOException {

		if(isInputValid()){

			wishList.setAlbum(albumField.getText());
			wishList.setArtist(artistField.getText());
			if(recordLabelField.getText() != null)
				wishList.setRecordLabel(recordLabelField.getText());
			else
				wishList.setRecordLabel("-");
			if(releaseYearField.getText() != null)
				wishList.setReleaseYear(releaseYearField.getText());
			else
				wishList.setReleaseYear("-");
				

			okClicked = true;
			dialogStage.close();
		}
	}
	
	private boolean isInputValid() {
		String errorMessage = "";

		if (albumField.getText() == null || albumField.getText().length() == 0) {
			errorMessage += "Not a valid album name!\n"; 
		}
		if (artistField.getText() == null || artistField.getText().length() == 0) {
			errorMessage += "Not a valid artist name!\n"; 
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
	private void handleCancel() {
		dialogStage.close();
	}

}

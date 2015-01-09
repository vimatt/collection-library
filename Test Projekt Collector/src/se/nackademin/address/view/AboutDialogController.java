package se.nackademin.address.view;

import javafx.fxml.FXML;

public class AboutDialogController {
	
	@FXML
	private void initialize(){
	}
	
	@FXML
	private void okClicked(){
		RootLayoutController.dialogStage.close();
	}
}

package se.nackademin.address.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
	
	@FXML private MenuItem exitItem;
	@FXML private MenuItem newItem;
	@FXML private MenuItem editItem;

	@FXML
	private void initialize(){
	}
	
	@FXML
	private void exit(){
		Platform.exit();
	}
}

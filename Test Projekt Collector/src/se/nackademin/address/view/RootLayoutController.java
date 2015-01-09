package se.nackademin.address.view;

import java.io.IOException;

import se.nackademin.address.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootLayoutController {
	
	@FXML private MenuItem exitItem;
	@FXML private MenuItem newItem;
	@FXML private MenuItem editItem;
	@FXML private MenuItem about;
	public static Stage dialogStage;
	
	@FXML
	private void initialize(){
	}
	
	@FXML
	private void exit(){
		Platform.exit();
	}
	
	@FXML
	private void showAboutDialog(){

		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AboutDialog.fxml"));
		AnchorPane pane = (AnchorPane) loader.load();

		dialogStage = new Stage();
		dialogStage.setTitle("About Collection Application");
		dialogStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setResizable(false);
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);
		dialogStage.show();
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

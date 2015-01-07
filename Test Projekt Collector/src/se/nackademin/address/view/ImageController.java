package se.nackademin.address.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageController {

	@FXML ImageView enlargedImage;

	@FXML
	private void initialize(){
	}

	public ImageController(){
	}

	public void setImage(Image img, Stage dialogStage){
		enlargedImage.fitWidthProperty().bind(dialogStage.widthProperty());
		enlargedImage.setImage(img);
	}
	public void setImgViewHeight(double h){
			enlargedImage.prefHeight(h);
	}
	public void setImgViewWidth(double w){
			enlargedImage.prefWidth(w);
	}


}

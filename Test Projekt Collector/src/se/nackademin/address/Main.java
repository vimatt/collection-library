package se.nackademin.address;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import se.nackademin.address.model.VinylRecords;
import se.nackademin.address.view.RecordEditDialogController;
import se.nackademin.address.view.VinylRecordController;
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
	
	//Create VinylRecord Data
	final ObservableList<VinylRecords> vinylRecordData = FXCollections.observableArrayList();

	public Main(){
        try {
        	String filename = "records.txt";
    		Scanner s = null;
    		try{
    			s =  new Scanner(new BufferedReader(new FileReader(filename)));
    			s.useDelimiter(";");//Decides which char that separates the input
              String line;
              ArrayList<String>list = new ArrayList<String>();
              int cnt = 0;
    			while(s.hasNext()){
    				line = s.next();
    				list.add(line);
    				cnt++;
                    if(cnt == 5){
                    	vinylRecordData.add(new VinylRecords(list.get(0),list.get(1),list.get(2),list.get(3), list.get(4)));
//                    	if(list.get(4) != null)
//                    		vinylRecord.setAlbumCover(albumCoverField.getText().replace("\\", "/"));
                    	cnt = 0;
                    	list.clear();
                    }
    			}
    		}finally{
    			if(s != null){
    				s.close();
    			}
    		}		
        }
        catch(FileNotFoundException e) {
            System.out.println("Unable to open file");				
        }
	}
	
	public ObservableList<VinylRecords> getVinylRecordData() {
        return vinylRecordData;
    }
	
	
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Collection Application");	
		this.primaryStage.getIcons().add(new Image("file:resources/images/file-manager.png"));
				
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
			//Load RecordOverview(NyMain)
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/VinylRecordOverview.fxml"));
			TabPane recordOverview = (TabPane)loader.load();
			
			//Set record overview in the center of root layout
			rootLayout.setCenter(recordOverview);
			
			VinylRecordController controller = loader.getController();
			controller.setMain(this);

		} catch(Exception e) {
			e.printStackTrace();
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
		dialogStage.setTitle("Add Vinyl Record");
		dialogStage.initModality(Modality.WINDOW_MODAL);
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
	public boolean showTestRecordEditDialog(VinylRecords vinylRecord){
		
		try{
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/TestRecordEditDialog.fxml"));
		AnchorPane page = (AnchorPane) loader.load();
		
		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Edit Vinyl Record");
		dialogStage.initModality(Modality.WINDOW_MODAL);
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
	
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}

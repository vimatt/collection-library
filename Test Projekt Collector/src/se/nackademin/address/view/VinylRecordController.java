package se.nackademin.address.view;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.controlsfx.dialog.Dialogs;
import se.nackademin.address.Main;
import se.nackademin.address.model.VinylRecords;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


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
		//If it's a valid selection we remove it and create a temporary file that we later rename and replace over the old file
		if(selectedIndex >= 0){
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
						&& list.get(4).equals("file:///" + v.getAlbumCoverString())){

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
		boolean okClicked = main.showRecordEditDialog(tempRecord);
		if (okClicked) {
			main.getVinylRecordData().add(tempRecord);
		}
	}

	@FXML
	private void handleEditPerson() {
		VinylRecords selectedRecord = tableID.getSelectionModel().getSelectedItem();
		if (selectedRecord != null) {
			boolean okClicked = main.showTestRecordEditDialog(selectedRecord);
			if (okClicked) {
				showVinylRecordDetails(selectedRecord);
			}
		}
	}

	public void openFileChooser(){

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose An Album Cover");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			try {
				BufferedImage bufferedImage = ImageIO.read(file);
				Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				albumCover.setImage(image);
//				record.setAlbumCover(image);

			} 
			catch (IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);			
			}	
		}
	}

	//	private void openFile(File file) {
	//		try {
	//			desktop.open(file);
	//		} catch (IOException ex) {
	//			Logger.getLogger(
	//					FileChooser.class.getName()).log(
	//							Level.SEVERE, null, ex
	//							);
	//		}
	//	}

}

package se.nackademin.address.view;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import se.nackademin.address.model.VinylRecords;
@SuppressWarnings("deprecation")

public class RecordEditDialogController {
	


		//Dialog to edit details of a person.
	
	    @FXML
	    private TextField albumField;
	    @FXML
	    private TextField artistField;
	    @FXML
	    private TextField recordLabelField;
	    @FXML
	    private TextField releaseYearField;

	    private Stage dialogStage;
	    private VinylRecords vinylRecord;
	    private boolean okClicked = false;

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
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
	    }
	    
	    //Returns true if the user clicked OK, false otherwise.	   
	    public boolean isOkClicked() {
	        return okClicked;
	    }

	    //called when the user clicks ok.	     
	    @FXML
	    private void handleOk() {
	    	String fileName = "records.txt";
            // Assume default encoding.
            FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(fileName,true);
	            // Always wrap FileWriter in BufferedWriter.
	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	            
	            bufferedWriter.write(albumField.getText()+";");
	            bufferedWriter.write(artistField.getText()+";");
	            bufferedWriter.write(recordLabelField.getText()+";");
	            bufferedWriter.write(releaseYearField.getText()+";");
	            bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        if (isInputValid()) {
	            vinylRecord.setAlbum(albumField.getText());
	            vinylRecord.setArtist(artistField.getText());
	            vinylRecord.setRecordLabel(recordLabelField.getText());
	            vinylRecord.setReleaseYear(releaseYearField.getText());
	         
	            okClicked = true;
	            dialogStage.close();
	        }
	    }
	    @FXML
	    private void handleOkEdit() throws IOException {
	    	//Save the fields in temporary variables
	    	
	    	String tempAlbum = albumField.getText();
	    	String tempArtist = artistField.getText();
	    	String tempRecord = recordLabelField.getText();
	    	String tempRelease = releaseYearField.getText();
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
					if(cnt == 4){
//						VinylRecords v = tableID.getItems().get(selectedIndex);
						if(list.get(0).equals(VinylRecordController.album) && list.get(1).equals(VinylRecordController.artist) && list.get(2).equals(VinylRecordController.recordlabel) && list.get(3).equals(VinylRecordController.release)){
							try {
								//Save the temporary variables over the old text                       	            
								bufferedWriter.write(tempAlbum+";");
								bufferedWriter.write(tempArtist+";");
								bufferedWriter.write(tempRecord+";");
								bufferedWriter.write(tempRelease+";");

							} catch (IOException e) {
								e.printStackTrace();
							}
	
						} else {

							try {
								// Always wrap FileWriter in BufferedWriter.                          	            
								bufferedWriter.write(list.get(0)+";");
								bufferedWriter.write(list.get(1)+";");
								bufferedWriter.write(list.get(2)+";");
								bufferedWriter.write(list.get(3)+";");

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
	        if (isInputValid()) {
	            vinylRecord.setAlbum(albumField.getText());
	            vinylRecord.setArtist(artistField.getText());
	            vinylRecord.setRecordLabel(recordLabelField.getText());
	            vinylRecord.setReleaseYear(releaseYearField.getText());
	         
	            okClicked = true;
	            dialogStage.close();
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
	            errorMessage += "No valid album name!\n"; 
	        }
	        if (artistField.getText() == null || artistField.getText().length() == 0) {
	            errorMessage += "No valid artist name!\n"; 
	        }
	        if (recordLabelField.getText() == null || recordLabelField.getText().length() == 0) {
	            errorMessage += "No valid record label!\n"; 
	        }

	        if (releaseYearField.getText() == null || releaseYearField.getText().length() == 0) {
	            errorMessage += "No valid release year!\n"; 
	        } else {
	            // try to parse the release date into an int.
	            try {
	                Integer.parseInt(releaseYearField.getText());
	            } catch (NumberFormatException e) {
	                errorMessage += "No valid release year (must be an integer)!\n"; 
	            }
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
	}


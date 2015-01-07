package se.nackademin.address.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class VinylRecords {


	private final StringProperty album;
	private final StringProperty artist;
	private final StringProperty recordLabel;
	private final StringProperty releaseYear;
	private Image albumCover;
	private String albumCoverString;

	public VinylRecords(){
		this(null, null, null,null, "");
	}


	public VinylRecords(String sAlbum, String sArtist, String recordLabel, String releaseYear, String albumCover ){
		this.album = new SimpleStringProperty(sAlbum);
		this.artist = new SimpleStringProperty(sArtist);
		this.recordLabel = new SimpleStringProperty(recordLabel);
		this.releaseYear = new SimpleStringProperty(releaseYear);
		if(!albumCover.equals("")){
			this.albumCover = new Image("file:///" + albumCover.replace("file:///", ""));
			this.albumCoverString = albumCover.replace("file:///", "");
		}
		else{
			this.albumCover = new Image("file:///C:/aVictor/git/eget-projekt-oop/Test Projekt Collector/resources/images/default image.png");
			this.albumCoverString = "C:/aVictor/git/eget-projekt-oop/Test Projekt Collector/resources/images/default image.png";
		}
	}

	public String getAlbum(){
		return album.get();
	}

	public void setAlbum(String v){
		album.set(v);
	}

	public StringProperty albumProperty(){
		return album;
	}

	public String getArtist(){
		return artist.get();
	}

	public void setArtist(String v){
		artist.set(v);
	}

	public StringProperty artistProperty(){
		return artist;
	}

	public String getRecordLabel(){
		return recordLabel.get();
	}

	public void setRecordLabel(String v){
		recordLabel.set(v);
	}

	public StringProperty recordLabelProperty(){
		return recordLabel;
	}

	public String getReleaseYear(){
		return releaseYear.get();
	}

	public void setReleaseYear(String v){
		releaseYear.set(v);
	}

	public StringProperty releaseYearProperty(){
		return releaseYear;
	}

	public Image getAlbumCover(){
		return albumCover;		
	}

	public void setAlbumCover(String img){
		if(img != null){
			albumCoverString = img;
			Image image = new Image("file:///" +img);
			albumCover = image;
		}
		else {
			Image image2 = new Image("file:///C:/aVictor/git/eget-projekt-oop/Test Projekt Collector/resources/images/default image.png");
			albumCover = image2;
		}
	}
	
	public String getAlbumCoverString(){
		return albumCoverString;
	}
}

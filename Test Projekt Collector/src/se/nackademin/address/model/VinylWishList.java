package se.nackademin.address.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VinylWishList {


	private final StringProperty album;
	private final StringProperty artist;
	private final StringProperty recordLabel;
	private final StringProperty releaseYear;

	public VinylWishList(){
		this(null, null, null,null);
	}


	public VinylWishList(String sAlbum, String sArtist, String recordLabel, String releaseYear){
		this.album = new SimpleStringProperty(sAlbum);
		this.artist = new SimpleStringProperty(sArtist);
		this.recordLabel = new SimpleStringProperty(recordLabel);
		this.releaseYear = new SimpleStringProperty(releaseYear);
		
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

	
}

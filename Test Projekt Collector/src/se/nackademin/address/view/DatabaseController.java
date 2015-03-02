package se.nackademin.address.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseController {

	ResultSet rs = null;
	private static Connection conn;

	public DatabaseController(){
		try {
			conn = DriverManager.getConnection("jdbc:SQLite:C:/aVictor/git/eget-projekt-oop/Test Projekt Collector/db/collectionDB.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addVinylToDatabase(String album, String artist, String rLabel, String releaseYear, String albumCover){
		try (
				PreparedStatement stm = conn.prepareStatement("INSERT INTO vinyl_record (album, artist, label, release_year, album_cover)" +
						"VALUES (?,?,?,?,?)", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				){

			stm.setString(1, album);
			stm.setString(2, artist);
			stm.setString(3, rLabel);
			stm.setString(4, releaseYear);
			stm.setString(5, albumCover);

			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateVinylToDatabase(String album, String artist, String rLabel, String releaseYear, String albumCover,
			String oldAlbum, String oldArtist){
		try (
				PreparedStatement stm = conn.prepareStatement("UPDATE vinyl_record SET album=?, artist=?, label=?, release_year=?, album_cover=?" +
						"WHERE album=? and artist=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				){

			stm.setString(1, album);
			stm.setString(2, artist);
			stm.setString(3, rLabel);
			stm.setString(4, releaseYear);
			stm.setString(5, albumCover);
			stm.setString(6, oldAlbum);
			stm.setString(7, oldArtist);

			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteVinylFromDatabase(String oldAlbum, String oldArtist){
		try (
				PreparedStatement stm = conn.prepareStatement("DELETE FROM vinyl_record WHERE album=? and artist=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				){

			stm.setString(1, oldAlbum);
			stm.setString(2, oldArtist);

			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

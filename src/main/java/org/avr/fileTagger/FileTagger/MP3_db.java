package org.avr.fileTagger.FileTagger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MP3_db {
	
	private String protocal = "jdbc:derby:";
	
	private Connection conn;
	
	private String insertTags = "insert into TAGS ( file_location , song_title , artist , album ) values ( ? , ? , ? , ? )";
	private PreparedStatement insTags;
	
	public MP3_db() {
		try {
			conn = DriverManager.getConnection(protocal + "derbyDB;create=true" );
			
			try {
				tablesExist();
			} catch (SQLException sqlEx) {
				createTables();
			}
			insTags = conn.prepareStatement(insertTags);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	private boolean tablesExist() throws SQLException {
		String sql = "select count(*) from tags";
		CallableStatement cs  = conn.prepareCall(sql);
		ResultSet rs = cs.executeQuery();
		return rs.next();
	}
	
	
	
	private void createTables() throws SQLException {
		StringBuffer str = new StringBuffer();
		str.append("create table tags ( ");
		str.append("	file_location varchar(255)");
		str.append("  , song_title    varchar(255)");
		str.append("  , artist        varchar(255)");
		str.append("  , album         varchar(255)");
		str.append("  , PRIMARY KEY (file_location)");
		str.append(")");
		
		Statement cs  = conn.createStatement();
		cs.execute(str.toString());
		
//		cs.close();
	}
	
	
	
	/**
	 * Insert tags into DB
	 * @param tags
	 */
	public void storeTags(MP3tags tags) throws SQLException {
		insTags.setString(1, tags.getFileLocation().trim() );
		insTags.setString(2, tags.getTitle().trim() );
		insTags.setString(3, tags.getArtist().trim() );
		insTags.setString(4, tags.getAlbum().trim() );
		
		insTags.executeUpdate();
	}
}

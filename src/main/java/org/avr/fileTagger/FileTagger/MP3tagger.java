package org.avr.fileTagger.FileTagger;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

public class MP3tagger {
	
	private MP3_db DB = new MP3_db();

	public static void main(String[] args) {
		MP3tagger x = new MP3tagger();
		x.startAtThisDirectory( args[0] , 0 );
	}

	private void startAtThisDirectory(String startDir , int counter) {
		int indentCount = counter;
		
		DirectoryStream<Path> stream = null;
		File file = null;
		try {
			stream = getDirectoryContents( startDir );
			
			for (Path path : stream) {
				file = new File(path.toString());
				if ( file.isDirectory() )
					startAtThisDirectory( file.toString() , indentCount++);
				else {
//					if ( isFileMP3(file))
						readFileTags(file);
				}
			}
		} catch (Exception ex) {
			System.out.println("FILE :  "+ file);
			ex.printStackTrace();
		}
	}
	
	
	
	

	/**
	 * Return all the directory elements 
	 * @param startDir
	 * @return
	 * @throws Exception
	 */
	private DirectoryStream<Path> getDirectoryContents(String startDir ) throws Exception {
		return Files.newDirectoryStream( FileSystems.getDefault().getPath(startDir) );		
	}
	
	
	
	/**
	 * prefix spaces and a hyphen 
	 * 
	 * @param x
	 * @return
	 */
	private String indent(int x) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < x; i++) {
			str.append(' ');
		}
		str.append("- ");
		return str.toString();
	}
	
	
	
	
	/**
	 * is the file extension MP3
	 * @param file
	 * @return
	 */
	private boolean isFileMP3(File file) {
		Optional<String> op = getFileExtension(file);
		if (op.isPresent() )
			return op.get().equalsIgnoreCase("mp3");
		return false;
	}
	private Optional<String> getFileExtension(File file) {
		return Optional.ofNullable( file.toString() )
				.filter(f -> f.contains(".") )
				.map(f -> f.substring( file.toString().lastIndexOf('.') +1));
	}
	
	
	
	
	
	
	private void readFileTags(File file) throws InvalidAudioFrameException, IOException, TagException, ReadOnlyFileException {
		System.out.println("Reading tags : "+  file.toString());
		
		try {
			AudioFile f = AudioFileIO.read(file);
			
//			if ( ((MP3File)f).hasID3v1Tag() ) System.out.println("It has V1 tags");
//			if ( ((MP3File)f).hasID3v2Tag() ) System.out.println("It has V2 tags");
			
			readTags(f);
		} catch (CannotReadException crEx) {
		}
	}
	
	
	
	private void readTags(AudioFile audioF) {
		Tag tag = audioF.getTag();
		
//		System.out.println( indent(4) +"         : "+ tag.getFieldCount());
//		System.out.println( indent(4) +"  Artist : "+ tag.getFirst(FieldKey.ARTIST));
//		System.out.println( indent(4) +"   Album : "+ tag.getFirst(FieldKey.ALBUM));
//		System.out.println( indent(4) +"   Title : "+ tag.getFirst(FieldKey.TITLE));
//		System.out.println( indent(4) +"   Track : "+ tag.getFirst(FieldKey.TRACK) +" of "+ tag.getFirst(FieldKey.TRACK_TOTAL));
		
		for (FieldKey fieldKey : FieldKey.values()) {
			System.out.println( fieldKey +" - "+ tag.getFirst(fieldKey) );
		}
		
		//   Write to DB
//		MP3tags tags = new MP3tags();
//		tags.setAlbum( tag.getFirst(FieldKey.ALBUM ) );
//		tags.setArtist( tag.getFirst(FieldKey.ARTIST ) );
//		tags.setTitle( tag.getFirst(FieldKey.TITLE ) );
//		tags.setFileLocation( audioF.getFile().getAbsolutePath() );
//		
//		try {
//			DB.storeTags(tags);
//		} catch (DerbySQLIntegrityConstraintViolationException duplicate) {
//			System.out.println("DUPLICATE : "+ audioF.getFile().getAbsolutePath() );
//		} catch (SQLException sqlEx) {
//			sqlEx.printStackTrace();
//		}
	}
}

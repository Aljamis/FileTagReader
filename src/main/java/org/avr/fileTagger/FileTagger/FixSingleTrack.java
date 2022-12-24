package org.avr.fileTagger.FileTagger;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class FixSingleTrack {

	public static void main(String[] args) {
		
		FixSingleTrack fix = new FixSingleTrack();
		fix.fixThisFile( args[0] );
		
	}
	
	
	
	private void fixThisFile(String fileName) {
		System.out.println( System.getProperty("java.version"));
		
		try {
			File file = new File( fileName ); 
			AudioFile f = AudioFileIO.read(file);
			
//			writeTags( f );
			readTags( f );
			
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException crEx) {
			crEx.printStackTrace();
		}
	}
	
	
	
	private void writeTags(AudioFile audioF) {
		Tag tag = audioF.getTag();
		try {
			tag.setField(FieldKey.ARTIST, "Adam Sandler");
			tag.deleteField(FieldKey.COVER_ART);
			audioF.commit();
		} catch (KeyNotFoundException | FieldDataInvalidException | CannotWriteException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void readTags(AudioFile audioF) {
		Tag tag = audioF.getTag();
		
		for (FieldKey fieldKey : FieldKey.values()) {
			try {
				if (!tag.getFirst(fieldKey).isEmpty())
					System.out.println( fieldKey +" - "+ tag.getFirst(fieldKey) );
			} catch (Exception ex) {
				System.out.println("\t\t "+ fieldKey);
			}
		}
	}
}

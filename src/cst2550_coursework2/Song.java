/*
 * Song class to hold the details about each Song
 */
package cst2550_coursework2;

import java.util.Objects;

/**
 *
 * @author Ammar Ali Moazzam
 * MISIS: M00696114
 */
public class Song {

    //variables for the class
    private String songTitle;
    private String artist;
    private String playingTime;
    private String videoFileName;

    //constructor with 4 parameters
    public Song(String songTitle, String artist, String playingTime, String videoFileName) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.playingTime = playingTime;
        this.videoFileName = videoFileName;
    }

    //default constructor
    Song() {
        
    }
    
    ////
    //Below are the getters and setter methods for accessing variables in this class
    ////
    
    //getter method to get the song title
    public String getSongTitle() {
        return songTitle;
    }

    //setter method to set the song title
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    //getter method to get the song artist
    public String getArtist() {
        return artist;
    }

    //setter method to set the song artist
    public void setArtist(String artist) {
        this.artist = artist;
    }

    //getter method to get the song playing time
    public String getPlayingTime() {
        return playingTime;
    }

    //setter method to set the song playing time
    public void setPlayingTime(String playingTime) {
        this.playingTime = playingTime;
    }

    //getter method to get the song video file name
    public String getVideoFileName() {
        return videoFileName;
    }

    //setter method to set the song video file name
    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    //tostring method to display details about the song onbject
    @Override
    public String toString() {
        return "Song{" + "songTitle=" + songTitle + ", artist=" + artist + ", playingTime=" + playingTime + ", videoFileName=" + videoFileName + '}';
    }

    //overriding the hashCode method
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    //overriding the equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Song other = (Song) obj;
        if (!Objects.equals(this.songTitle, other.songTitle)) {
            return false;
        }
        if (!Objects.equals(this.artist, other.artist)) {
            return false;
        }
        if (!Objects.equals(this.playingTime, other.playingTime)) {
            return false;
        }
        if (!Objects.equals(this.videoFileName, other.videoFileName)) {
            return false;
        }
        return true;
    }

}

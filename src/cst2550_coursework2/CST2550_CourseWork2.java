
/* This is a Karoake MediaPlayer application.This class contains the main method.
 * The Karoake Mediaplayer reads songs from a text file and populates a HashMap. The  
 * application also has an option to load songs from a different file. Moreover
 * the application functions as a regular media player with the option of 
 * storing songs into a playlist and playing them on a FIFO basis. The user
 * also has the ability to search for songs in the library. Songs can also
 * be deleted from the playlist. Another function is that the user can add 
 * songs to the library. These songs will be added in the tableView as well
 * as the song file. The application has a very intuitive GUI with styling 
 * done using Style.css class.
 */
package cst2550_coursework2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.*;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ammar Ali Moazzam MISIS: M00696114
 */
public class CST2550_CourseWork2 extends Application {

//Global variables to be used in the projecct.    
    private HashMap<String, Song> songData = new HashMap<String, Song>();
    private TableView librarySongs = new TableView();
    private TableView playlistSongs = new TableView();
    private Button loadSongs = new Button("Load Library");
    private Queue<Song> playList = new LinkedList<>();
    private MediaView mediaView = new MediaView();
    private Pane mediaPane = new Pane();
    private Button muteBtn = new Button("Mute");
    private Label timeLbl = new Label();
    private Slider progressBar = new Slider();
    private Button nextBtn = new Button(">>");
    private Button prevBtn = new Button("<<");
    private Button playBtn = new Button(" || ");
    private Label totalTimeLbl = new Label();
    private Slider volume = new Slider();
    private Label volumeLbl = new Label("Volume: 100");
    private Label songLbl = new Label("");
    private Media media;
    private MediaPlayer mediaPlayer;
    private Button playSongBtn = new Button("Play Songs");
    private boolean alreadyMute = false;
    private Button deleteBtn = new Button("Delete Song From Playlist");
    private Button addNewSongBtn = new Button("Add Song to Library");
    private boolean isClicked = false;
    private boolean repeat = false;
    private double totalTimePlayed = 0;
    private int volumeValue = 100;
    private static File file1;
    private static boolean fileChosen = false;

    @Override
    public void start(Stage primaryStage) {

        //creating and reading songs from the default file
        File file = new File("sampleSongData.txt");
        readFile(file);
        //calling the method to read songs from the file and populate the tableView.
        getSongs();

        //creating a file chooser
        FileChooser fileChooser = new FileChooser();

        //creating a textField to implement the searchBar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for Songs here ");
        //Assingning size and position to the textfield on the window
        searchBar.setLayoutX(5);
        searchBar.setLayoutY(5);
        searchBar.setMaxSize(400, 25);
        //setting the preffered size for the node
        searchBar.setPrefSize(380, 25);
        //setting id for custom styling using CSS
        searchBar.setId("searchBar");

        //creating a buuton for the search bar
        Button searchBtn = new Button("Search");
        //Assingning size and position to the button on the window
        searchBtn.setLayoutX(390);
        searchBtn.setLayoutY(5);
        //styling the button
        searchBtn.setBorder(new Border(new BorderStroke(Color.web("#006666"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //adding custom background
        searchBtn.setBackground(new Background(new BackgroundFill(Color.web("#262222"), CornerRadii.EMPTY, Insets.EMPTY)));
        //setting text color
        searchBtn.setTextFill(Color.WHITE);

        //creating a button for importing songs from a different file
        Button chooseFile = new Button("Import Songs");
        //Assingning size and position to the button on the window
        chooseFile.setLayoutX(5);
        chooseFile.setLayoutY(260);

        //Assingning position to the button on the window
        loadSongs.setLayoutX(360);
        loadSongs.setLayoutY(260);
        //setting the visibiity of the node
        loadSongs.setVisible(false);

        //Assingning size and position to the table view on the window
        librarySongs.setLayoutX(5);
        librarySongs.setLayoutY(35);
        //setting the visibility for the node
        librarySongs.setEditable(false);
        //setting the preffered size for the node
        librarySongs.setPrefSize(440, 220);

        //setting up column and colum names for the library table view
        TableColumn<String, Song> libraryNameColumn = new TableColumn<>("Song Title");
        libraryNameColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        TableColumn<String, Song> libraryArtistColumn = new TableColumn<>("Artist");
        libraryArtistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        TableColumn<String, Song> libraryDurationColumn = new TableColumn<>("Song Duration");
        libraryDurationColumn.setCellValueFactory(new PropertyValueFactory<>("playingTime"));
        TableColumn<String, Song> libraryVideoFileColumn = new TableColumn<>("Song File");
        libraryVideoFileColumn.setCellValueFactory(new PropertyValueFactory<>("videoFileName"));
        //messaage that will be displayed if the table view is empty
        librarySongs.setPlaceholder(new Label("Choose File to display songs from"));
        //adding all the columns
        librarySongs.getColumns().addAll(libraryNameColumn, libraryArtistColumn, libraryDurationColumn, libraryVideoFileColumn);

        //Assingning size to the columns of the table view
        libraryNameColumn.setPrefWidth(150);
        libraryArtistColumn.setPrefWidth(100);
        libraryDurationColumn.setPrefWidth(80);

        //creating a butoon to add songs to playlist
        Button addBtn = new Button("Add Song To Playlist");
        //Assingning position to the button on the window
        addBtn.setLayoutX(99);
        addBtn.setLayoutY(260);

        //Assingning position to the button on the window
        addNewSongBtn.setLayoutX(230);
        addNewSongBtn.setLayoutY(260);

        //creating a textfield to show the name of the playlist
        TextField playlistTF = new TextField("PlayList");
        //giving custom id for CSS styles
        playlistTF.setId("playlistTF");
        playlistTF.setEditable(false);
        //Assingning size and position to the textfield on the window
        playlistTF.setLayoutX(5);
        playlistTF.setLayoutY(5);
        //setting the maximum size for the node
        playlistTF.setMaxSize(420, 25);
        //setting the preffered size for the node
        playlistTF.setPrefSize(200, 25);

        //Assingning position to the button on the window
        playSongBtn.setLayoutX(370);
        playSongBtn.setLayoutY(235);

        //Assingning size and position to the TableView on the window
        playlistSongs.setLayoutX(5);
        playlistSongs.setLayoutY(35);
        //making the node ineditable
        playlistSongs.setEditable(false);
        //setting the preffered size for the node
        playlistSongs.setPrefSize(440, 195);

        //seting up columns and column names for the playlist table view
        TableColumn<String, Song> nameColumn = new TableColumn<>("Song Title");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        TableColumn<String, Song> artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        TableColumn<String, Song> durationColumn = new TableColumn<>("Song Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("playingTime"));
        TableColumn<String, Song> videoFileColumn = new TableColumn<>("Song File");
        videoFileColumn.setCellValueFactory(new PropertyValueFactory<>("videoFileName"));
        //message that will be displayed if the playlist is empty
        playlistSongs.setPlaceholder(new Label("Add Songs to PlayList"));
        //adding columns
        playlistSongs.getColumns().addAll(nameColumn, artistColumn, durationColumn, videoFileColumn);

        //Assingning size to tableview columns
        nameColumn.setPrefWidth(150);
        artistColumn.setPrefWidth(100);
        durationColumn.setPrefWidth(80);

        //Assingning size and position to the button on the window
        deleteBtn.setLayoutX(145);
        deleteBtn.setLayoutY(235);
        //adding custom styles to the button
        deleteBtn.setBorder(new Border(new BorderStroke(Color.web("#006666"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //setting custom background
        deleteBtn.setBackground(new Background(new BackgroundFill(Color.web("#262222"), CornerRadii.EMPTY, Insets.EMPTY)));
        //setting white font colour
        deleteBtn.setTextFill(Color.WHITE);

        //Assingning position to the button on the window
        playBtn.setLayoutX(300);
        playBtn.setLayoutY(40);

        //Assingning position to the button on the window
        nextBtn.setLayoutX(350);
        nextBtn.setLayoutY(40);

        //Assingning position to the button on the window
        prevBtn.setLayoutX(245);
        prevBtn.setLayoutY(40);

        //Assingning position to the button on the window
        muteBtn.setLayoutX(180);
        muteBtn.setLayoutY(40);

        //Assingning position and preffered size to the button on the window
        progressBar.setId("slider");
        progressBar.setLayoutX(5);
        progressBar.setLayoutY(15);
        //setting the preffered size for the node
        progressBar.setPrefSize(680, 20);

        //Assingning position to the label on the window
        timeLbl.setLayoutX(10);
        timeLbl.setLayoutY(0);

        //Assingning position to the label on the window
        totalTimeLbl.setLayoutX(650);
        totalTimeLbl.setLayoutY(0);

        //Assingning size and position to the button on the window
        volume.setLayoutX(435);
        volume.setLayoutY(50);
        //setting the preffered size for the node
        volume.setPrefSize(250, 20);

        //Assingning position to the label on the window
        volumeLbl.setLayoutX(440);
        volumeLbl.setLayoutY(35);

        //Event handler for search button
        searchBtn.setOnAction((ActionEvent e) -> {
            //the text from the search bar is taken and the search method is called
            String songName = searchBar.getText().toUpperCase();
            //calling the search method
            searchSongs(songName);
        });

        //Event handler for search bar textfield 
        searchBar.setOnKeyPressed((KeyEvent keyEvent) -> {
            //if the user hits enter on the text field the song name from the search bar
            //is taken and is passed to the search method
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String songName = searchBar.getText().toUpperCase();
                //calling the search method
                searchSongs(songName);
            }
        });

        //Event handler for load songs button
        loadSongs.setOnAction((ActionEvent e) -> {
            //clears all the songs from the library table view
            librarySongs.getItems().clear();
            //load songs from the hash map
            getSongs();
            //hides the button
            loadSongs.setVisible(false);
        });

        //Event handler for add song to playlist button
        addBtn.setOnAction((ActionEvent event) -> {
            //Calls method to add song to playlist
            addSongToPlaylist();
        });

        //Event handler for delete button
        deleteBtn.setOnAction((ActionEvent event) -> {
            //Calls the method to delete song from the playlist
            deleteSongFromPlayList();
        });

        //Event handler for choose file button
        chooseFile.setOnAction((ActionEvent event) -> {
            //opens up the file chooser
            file1 = fileChooser.showOpenDialog(primaryStage);
            if (file1 != null) {
                fileChosen = true;
                //clears the table view
                librarySongs.getItems().clear();
                //clearing the hashmap first
                songData.clear();
                //reads the data from the file
                readFile(file1);
                //populates the table view
                getSongs();

            }
        });

        //Event handler for play song button
        playSongBtn.setOnAction((ActionEvent event) -> {
            //checks if the playlist is empty
            if (!playList.isEmpty()) {
                //takes the first song from the queue and plays it
                playSong(playList.poll());
            } else {
                //opens up an alert box if the playlist is empty
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Empty Playlist");
                alert.setHeaderText("Please add songs to the playlist using the 'Add To PlayList' Button");
                alert.show();

                //adds custom styling to the alert box
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
            }
        });

        //creates custom Font 
        Font f1 = new Font("Cookie-Regular", 56);
        Font f2 = new Font("Cookie-Regular", 21);

        //Creates a new label and sets its position 
        Label newSongLbl = new Label("New Song");
        newSongLbl.setLayoutX(100);
        newSongLbl.setLayoutY(24);
        //setting custom font to the label
        newSongLbl.setFont(f1);

        //Creates a new label and sets its position 
        Label songTitleLbl = new Label("Song Title");
        songTitleLbl.setLayoutX(48);
        songTitleLbl.setLayoutY(119);
        //setting custom font to the label
        songTitleLbl.setFont(f2);

        //Creates a new label and sets its position 
        Label songArtistLbl = new Label("Song Artist");
        songArtistLbl.setLayoutX(48);
        songArtistLbl.setLayoutY(167);
        //setting custom font to the label
        songArtistLbl.setFont(f2);

        //Creates a new label and sets its position 
        Label songPlayTimeLbl = new Label("Song PlayTime");
        songPlayTimeLbl.setLayoutX(48);
        songPlayTimeLbl.setLayoutY(215);
        //setting custom font to the label
        songPlayTimeLbl.setFont(f2);

        //Creates a new label and sets its position 
        Label songFileNameLbl = new Label("Song FileName");
        songFileNameLbl.setLayoutX(48);
        songFileNameLbl.setLayoutY(263);
        //setting custom font to the label
        songFileNameLbl.setFont(f2);

        TextField songTitleTF = new TextField();
        songTitleTF.setPromptText("Enter song title here");
        songTitleTF.setLayoutX(150);
        songTitleTF.setLayoutY(121);
        //setting preffered size for the node
        songTitleTF.setPrefWidth(185);

        //Creates a new TextField and sets its position 
        TextField songArtistTF = new TextField();
        songArtistTF.setPromptText("Enter song artist name here");
        songArtistTF.setLayoutX(150);
        songArtistTF.setLayoutY(169);
        //setting preffered size for the node
        songArtistTF.setPrefWidth(185);

        //Creates a new TextField and sets its position 
        TextField songPlayTimeTF = new TextField();
        songPlayTimeTF.setPromptText("Enter song playing time in seconds");
        songPlayTimeTF.setLayoutX(150);
        songPlayTimeTF.setLayoutY(217);
        //setting preffered size for the node
        songPlayTimeTF.setPrefWidth(185);

        //Creates a new TextField and sets its position 
        TextField songFileNameTF = new TextField();
        songFileNameTF.setPromptText("Enter filename without .mp4 extensions");
        songFileNameTF.setLayoutX(150);
        songFileNameTF.setLayoutY(265);
        //setting preffered size for the node
        songFileNameTF.setPrefWidth(185);

        //Creates a new button for adding song to the library and sets its position 
        Button addSongBtn = new Button("Add Song To Library");
        addSongBtn.setLayoutX(71);
        addSongBtn.setLayoutY(322);
        //setting preffered size for the node
        addSongBtn.setPrefSize(229, 40);

        //creates a new stage
        Stage stage = new Stage();

        //Event handler for add new song button
        addNewSongBtn.setOnAction((ActionEvent event) -> {
            //checks if the add button is already clicked
            if (!isClicked) {
                //creates a new pane and adds nodes to it
                Pane addSongPane = new Pane();
                //settinf ID to be used for CSS styling
                addSongPane.setId("newSongPane");
                //adding nodes to the pane
                addSongPane.getChildren().addAll(newSongLbl, songTitleLbl, songArtistLbl, songPlayTimeLbl, songFileNameLbl,
                        songTitleTF, songArtistTF, songPlayTimeTF, songFileNameTF, addSongBtn);
                //creating a new scene
                Scene addSongScene = new Scene(addSongPane, 370, 400);
                //adding CSS style sheet to the pane
                addSongScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                //setting the scene to a new stage
                stage.setTitle("Add New Song");
                //setting the scene to the stage
                stage.setScene(addSongScene);
                //showing the stage
                stage.show();
                isClicked = true;

                //to add functionality to the close button
                stage.setOnCloseRequest((WindowEvent we) -> {
                    isClicked = false;
                });

            } else {
                //alert box in case the user clicks on the add to library button twice
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Already Open");
                //alert message
                alert.setHeaderText("Add window already open");
                alert.show();

                //adding custom style to the alert box
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
            }
        });

        //Event handler for add new song button
        addSongBtn.setOnAction((ActionEvent event) -> {

            //check to make sure that no text field is empty
            if ((!songTitleTF.getText().isEmpty()) && (!songArtistTF.getText().isEmpty()) && (!songPlayTimeTF.getText().isEmpty()) && (!songFileNameTF.getText().isEmpty())) {
                //creates a new song object
                Song song = new Song();
                //gets data from the text field and adds them to the song object
                song.setSongTitle(songTitleTF.getText());
                song.setArtist(songArtistTF.getText());
                //check to make sure that the time and filename are in correct format
                if (isCorrectTimeValue(songPlayTimeTF.getText()) && isCorrectFileName(songFileNameTF.getText())) {
                    //setting the playing time of the song
                    song.setPlayingTime(songPlayTimeTF.getText());
                    //setting the video file name of the song
                    song.setVideoFileName("test.mp4");

                    //clear all fields
                    songTitleTF.setText("");
                    songArtistTF.setText("");
                    songFileNameTF.setText("");
                    songPlayTimeTF.setText("");

                    //adds song to the hashmap
                    songData.put(song.getSongTitle().toUpperCase(), song);
                    //clearing the library
                    librarySongs.getItems().clear();
                    //populating the library
                    getSongs();
                    //closing the stage
                    stage.close();
                    isClicked = false;
                    //confirmation alert to notify the user about the success of the operation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Song Added Successfully");
                    //alert message
                    alert.setHeaderText("Song " + songTitleTF.getText() + " Added Successfully");
                    alert.show();

                    //styling the dialog pane
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("Style.css").toExternalForm());
                    //writes the song to the file
                    writeToFile(song);
                }
            } else {
                //shows an alert box if all the text fields are not filled in
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Data");
                //alert message
                alert.setHeaderText("Please fill in all the text fields");
                alert.show();

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
            }
        });

        //creates a pane for keeping the controls for the media player
        //sets its position and adds nodes to it
        Pane controlPane = new Pane();
        //setting the layout of the node
        controlPane.setLayoutX(2);
        controlPane.setLayoutY(450);
        //setting the preffered size for the node
        controlPane.setPrefSize(700, 60);
        //adding nodes to the pane
        controlPane.getChildren().addAll(progressBar, timeLbl, volume, volumeLbl, playBtn, nextBtn, muteBtn, totalTimeLbl, prevBtn);
        //setting id to the pane
        controlPane.setId("controlPane");

        //creates a pane for keeping the media player
        //sets its position and adds nodes to it
        mediaPane.setLayoutX(5);
        mediaPane.setLayoutY(5);
        //setting the preffered size for the node
        mediaPane.setPrefSize(700, 480);
        //adding nodes to the pane
        mediaPane.getChildren().addAll(controlPane, songLbl);

        //creates a pane for keeping the songs library and associated nodes
        //sets its position and adds nodes to it
        Pane libraryPane = new Pane();
        libraryPane.setLayoutX(715);
        libraryPane.setLayoutY(5);
        //setting the preffered size for the node
        libraryPane.setPrefSize(310, 300);
        //adding nodes to the pane
        libraryPane.getChildren().addAll(searchBar, searchBtn, librarySongs, addBtn, loadSongs, chooseFile, addNewSongBtn);

        //creates a pane for keeping the playlist and associated nodes
        //sets its position and adds nodes to it
        Pane playlistPane = new Pane();
        playlistPane.setLayoutX(715);
        playlistPane.setLayoutY(320);
        //setting the preffered size for the node
        playlistPane.setPrefSize(310, 265);
        //adding nodes to the pane
        playlistPane.getChildren().addAll(playlistSongs, playlistTF, deleteBtn, playSongBtn);

        //creates a pane for keeping all the other panes
        //sets its position and adds nodes to it
        Pane root = new Pane();
        //adding nodes to the pane
        root.getChildren().addAll(mediaPane, libraryPane, playlistPane);
        //setiing custom background
        root.setBackground(new Background(new BackgroundFill(Color.web("#262222"), CornerRadii.EMPTY, Insets.EMPTY)));

        //creates a scene 
        Scene scene = new Scene(root, 1170, 595);
        //adds CSS style sheets to the scene
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        //setting stage title
        primaryStage.setTitle("Karaoke Player");
        //seting scene to the stage
        primaryStage.setScene(scene);
        //showing the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //method to read songs from the file
    public void readFile(File file) {
        try {
            //Creating Scanner instnace to read the songs file 
            Scanner input = new Scanner(file);

            //using scanner to read each line of the file
            while (input.hasNextLine()) {
                String line = input.nextLine();
                //splitting the lines into different parts wherever there is a tab
                String[] lineData = line.split("\\t");
                //storing the details into a song object
                Song song = new Song(lineData[0], lineData[1], lineData[2], lineData[3]);
                //adding the song object to the hash map
                songData.put(lineData[0].toUpperCase(), song);
            }

        } catch (Exception ex) {
            //alert box to be prompted if an error occurs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Error");
            //error messege
            alert.setHeaderText("Please choose a correct file");
            alert.show();

            //Giving custom styling to the alert box
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("Style.css").toExternalForm());
        }

    }

    //method to search for a particular song in the library
    public void searchSongs(String name) {
        //clears the table view
        librarySongs.getItems().clear();

        //loops the hashmap
        for (Entry<String, Song> s : songData.entrySet()) {
            //if it finds something that matches with the search critera
            if (s.getKey().startsWith(name)) {
                //add that item/song to the table view
                Song searchedSong = s.getValue();
                //adding items to the library
                librarySongs.getItems().add(new Song(searchedSong.getSongTitle(), searchedSong.getArtist(), searchedSong.getPlayingTime(), searchedSong.getVideoFileName()));
                loadSongs.setVisible(true);
            } else {
                //if the searched song is not present in the library, the following message is shown
                librarySongs.setPlaceholder(new Label("Song: " + name + " does not exist in the library"));
                loadSongs.setVisible(true);
            }
        }
    }

    //method to populate the library table view based on the entries in hash map
    public void getSongs() {
        songData.forEach((k, v) -> {
            librarySongs.getItems().add(new Song(v.getSongTitle(), v.getArtist(), v.getPlayingTime(), v.getVideoFileName()));
        });
    }

    //method to add songs to the playlist
    public void addSongToPlaylist() {
        //gets the selected song 
        Song song = (Song) librarySongs.getSelectionModel().getSelectedItem();
        //checks if the song is null
        if (librarySongs.getSelectionModel().getSelectedItem() != null) {
            //checks to make sure that the playlist doesnt already cotains this song
            if (!playList.contains(song)) {
                //adds song to the playlist queue
                playList.add(song);
                //adds song to the  table view
                playlistSongs.getItems().add(new Song(song.getSongTitle(), song.getArtist(), song.getPlayingTime(), song.getVideoFileName()));
            } else {
                //alerts the user if the song already exists in the playlist
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song already exists in playlist");
                //alert message
                alert.setHeaderText("Please select another song and then press the 'Add To PlayList' Button");
                alert.show();

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
            }
        } else {
            //alerts the user if no song from the library is selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No song selected");
            //alert message
            alert.setHeaderText("Please select a song and then press the 'Add To PlayList' Button");
            alert.show();

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("Style.css").toExternalForm());
        }
    }

    //method to delete song playlist
    public void deleteSongFromPlayList() {
        //gets the selected song
        Song song = (Song) playlistSongs.getSelectionModel().getSelectedItem();
        //checks if the song is null
        if (song != null) {
            //removes song from the playlist queue
            playList.remove(song);
            //removes song from the table view
            playlistSongs.getItems().remove(song);
        } else {
            //alerts the user if no song is selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Song Selected");
            //alert message
            alert.setHeaderText("Please select a song to the deleted from the playlist");
            alert.show();

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("Style.css").toExternalForm());

        }
    }

    //method for playing a song
    public void playSong(Song song) {

        playSongBtn.setVisible(false);

        //check if the song is repeating
        if (repeat == false) {
            //remove the song from the tableview
            playlistSongs.getItems().remove(0);
            totalTimePlayed = 0.0;
        }

        //assing position to the song label
        songLbl.setLayoutX(10);
        songLbl.setLayoutY(430);
        //set text of the label
        songLbl.setText(song.getSongTitle());
        //Creating Translate Transition 
        TranslateTransition translateTransition = new TranslateTransition();
        //Setting the duration of the transition  
        translateTransition.setDuration(Duration.millis(10000));
        //Setting the node for the transition 
        translateTransition.setNode(songLbl);
        //Setting the value of the transition along the x axis. 
        translateTransition.setByX(450 - song.getSongTitle().length());
        translateTransition.setFromX(10);
        //Setting the cycle count for the transition 
        translateTransition.setCycleCount(50);
        //Setting auto reverse value to false 
        translateTransition.setAutoReverse(false);
        //Playing the animation 
        translateTransition.play();
        //creating a new media with the song video file
        media = new Media(Paths.get(song.getVideoFileName()).toUri().toString());
        //creates a new media player
        mediaPlayer = new MediaPlayer(media);
        //creates a new media view to display the media
        mediaView = new MediaView(mediaPlayer);
        //sets the position and size of the media view
        mediaView.setLayoutX(5);
        mediaView.setLayoutY(5);
        mediaView.setFitHeight(650);
        mediaView.setFitWidth(690);
        //adds the media view to the media pane
        mediaPane.getChildren().addAll(mediaView);
        //plays the media
        mediaPlayer.play();

        //creates a binding property for the time label
        timeLbl.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    //gets the current play time of the media
                    Duration playTime = mediaPlayer.getCurrentTime().add(Duration.seconds(totalTimePlayed));
                    //formats the string
                    return String.format("%02d:%02d:%02d", (int) playTime.toHours(), (int) playTime.toMinutes() % 60, (int) playTime.toSeconds() % 60);
                }, mediaPlayer.currentTimeProperty()));

        //defines what needs to be done when the media player is in a ready state
        mediaPlayer.setOnReady(() -> {
            //gets total duration of the song
            int totalTime = Integer.parseInt(song.getPlayingTime());
            //sets the total time on the total time label
            totalTimeLbl.setText(String.format("%02d:%02d", (totalTime / 60), (totalTime % 60)));
            //setting min value to the progress bar
            progressBar.setMin(0.0);
            progressBar.setValue(0.0);

            //setting max value of the progressBar
            double maxTime = Math.round((totalTime) * 100.0) / 100.0;
            progressBar.setMax((maxTime));

            //listener to change the value of the progress bar according to time
            mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                progressBar.setValue(newValue.toSeconds() + totalTimePlayed);
            });

            //event handler to seek to a duration using the progress bar
            progressBar.setOnMousePressed((MouseEvent event) -> {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue() - totalTimePlayed));
            });

        });

        //if the volume is not mute set it to previous volume level
        if (!alreadyMute) {
            volume.setValue(volumeValue);
            mediaPlayer.setVolume(volumeValue);
        } else {
            //if the volume is mute set it to 0
            volume.setValue(0);
            mediaPlayer.setVolume(0);
            alreadyMute = true;
            volumeValue = (int) volume.getValue();
        }

        //changing volume with volume slider
        volume.valueProperty().addListener((Observable observable) -> {
            //set volume
            mediaPlayer.setVolume(volume.getValue() / 100);
            volumeLbl.setText("Volume: " + Math.round(volume.getValue()));
            volumeValue = (int) volume.getValue();

            //if the volume is muted
            if (alreadyMute) {
                //set volume
                mediaPlayer.setVolume(volume.getValue() / 100);
                volumeLbl.setText("Volume: " + Math.round(volume.getValue()));
                volumeValue = (int) volume.getValue();
                //set mute to false
                mediaPlayer.setMute(false);
                alreadyMute = false;
                //change button text
                muteBtn.setText("Mute");
            }
        });

        //to change the song if its duration is less than the media duration
        mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            //checks if the song duration is less than the media duration
            if (newValue.toSeconds() + totalTimePlayed >= Integer.parseInt(song.getPlayingTime())) {
                //stops the song and frees the resources
                mediaPlayer.stop();
                //dispose the player
                mediaPlayer.dispose();
                //remove media view from the pane
                mediaPane.getChildren().remove(mediaView);
                //stop the transition
                translateTransition.stop();
                songLbl.setText("");
                //set repeat to false
                repeat = false;

                //check if the playlist is empty
                if (!playList.isEmpty()) {
                    //play the next song from the playlist
                    playSong(playList.poll());
                }
            }
        });

        //defines what happens on end of media and loops the video if media time < songPlayingTime
        mediaPlayer.setOnEndOfMedia(() -> {
            //total time played gets incremented
            totalTimePlayed = totalTimePlayed + media.getDuration().toSeconds();

            //check to see if the song needs to be played again
            if (Integer.parseInt(song.getPlayingTime()) > totalTimePlayed) {
                //sets repeat tot true
                repeat = true;
                //stops the media player
                mediaPlayer.stop();
                //disposes the player
                mediaPlayer.dispose();
                //removes the media view from the pane
                mediaPane.getChildren().remove(mediaView);
                //plays the song again
                playSong(song);
            } else {
                //sets repeat to false
                repeat = false;
                //stops the media player
                mediaPlayer.stop();
                //disposes the player
                mediaPlayer.dispose();
                //removes the media view from the pane
                mediaPane.getChildren().remove(mediaView);

                //check to see if the playlist is empty
                if (!(playList.isEmpty())) {
                    //play next song
                    playSong(playList.poll());
                } else {
                    //show the play song button
                    playSongBtn.setVisible(true);
                }
            }

        });

        //event handler for the play button
        playBtn.setOnAction((ActionEvent e) -> {
            Status status = mediaPlayer.getStatus();
            //if the song is playing
            if (status == status.PLAYING) {
                //change button text
                playBtn.setText(" || ");
                // If the song is being played
                if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    mediaPlayer.play();
                } else {
                    // Pausing the player
                    mediaPlayer.pause();
                    playBtn.setText(" > ");
                }
            } // If the song is stopped or paused
            if (status == Status.HALTED || status == Status.STOPPED || status == Status.PAUSED || status == Status.READY) {
                //play the song
                mediaPlayer.play();
                //change button text
                playBtn.setText(" || ");
            }
        });

        //event handler for the mute button
        muteBtn.setOnAction((ActionEvent event) -> {
            //if the volume is already muted
            if (alreadyMute == true) {
                muteBtn.setText("Mute");
                //set the volume to max volume
                mediaPlayer.setMute(false);
                mediaPlayer.setVolume(100);
                volume.setValue(100);
                volumeValue = (int) volume.getValue();
                alreadyMute = false;
            } else {
                //change button text
                muteBtn.setText("Unmute");
                //mute the volume
                mediaPlayer.setMute(true);
                volume.setValue(0);
                volumeValue = (int) volume.getValue();
                alreadyMute = true;
            }
        });

        //event handler for the next button
        nextBtn.setOnAction((ActionEvent event) -> {
            //takes the next song from queue and plays it
            if (!playList.isEmpty()) {
                //setting repeat to false
                repeat = false;
                //stops the current song
                mediaPlayer.stop();
                //disposes the player
                mediaPlayer.dispose();
                //removes the media view from the pane
                mediaPane.getChildren().remove(mediaView);
                //change button text
                playBtn.setText(" || ");
                //stops the transtion
                translateTransition.stop();
                //plays the next song
                playSong(playList.poll());
            } else {
                //change label text
                songLbl.setText("");
                //display an alert to the user if the playlist is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("EMPTY PLAYLIST");
                //alert message
                alert.setHeaderText("No next song to play. Playlist is empty");
                alert.show();

                //custom styling to the dialog box
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
            }
        });

        //event handler for the previous button 
        prevBtn.setOnAction((ActionEvent event) -> {
            //sets the song to begin agian from the start
            mediaPlayer.seek(Duration.ZERO);
            progressBar.setValue(0);
        });

        //listener to control the visibility of the play song button
        mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (!(newValue == Status.PLAYING) && !(newValue == Status.PAUSED) && playList.isEmpty()) {
                playSongBtn.setVisible(true);
            } else {
                playSongBtn.setVisible(false);
            }
        });

    }

    //method to check if the correct time value has been input by the user
    public boolean isCorrectTimeValue(String time) {
        //checks to make sure that the string contains only numbers
        if (time.matches("[0-9]+")) {
            int timeInt = Integer.parseInt(time);
            if (timeInt <= 900) {
                return true;
            } else {
                //display an alert to the user if the time value is greater than 15 minutes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect PlayingTime Value");
                //alert message
                alert.setHeaderText("PLaying time can not exceed 15 minutes/900sec");
                alert.show();

                //custom styling to the dialog box
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("Style.css").toExternalForm());
                return false;
            }

        } else {
            //display an alert to the user if the value is not correct
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect PlayingTime Value");
            //alert message
            alert.setHeaderText("Please enter time in seconds\nOnly Integers are accepted\nDont include 's' or 'sec'");
            alert.show();

            //custom styling to the dialog box
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("Style.css").toExternalForm());
            return false;
        }

    }

    //method to check if the correct time value has been input bu the user
    public static boolean isCorrectTimeValue1(String time) {
        //checks to make sure that the string contains only numbers
        if (time.matches("[0-9]+")) {
            int timeInt = Integer.parseInt(time);
            if (timeInt <= 900) {
                return true;
            } else {
                //display an alert to the user if the time value is greater than 15 minutes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect PlayingTime Value");
                //alert message
                alert.setHeaderText("PLaying time can not exceed 15 minutes/900sec");
                alert.show();

                return false;
            }

        } else {
            //display an alert to the user if the value is not correct
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect PlayingTime Value");
            //alert message
            alert.setHeaderText("Please enter time in seconds\nOnly Integers are accepted\nDont include 's' or 'sec'");
            alert.show();

            return false;
        }

    }

    //method to check if the filename is correct ie: without any extensions
    public static boolean isCorrectFileName1(String fileName) {
        //checks to make sure that the string does not contain any '.'
        if (!(fileName.contains("."))) {
            return true;
        } else {
            //alert the user if the wrong filename format is given
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect FileName");
            //alert message
            alert.setHeaderText("Please enter filename without any extensions");
            alert.show();

            return false;
        }
    }

    //method to check if the filename is correct ie: without any extensions
    public boolean isCorrectFileName(String fileName) {
        //checks to make sure that the string does not contain any '.'
        if (!(fileName.contains("."))) {
            return true;
        } else {
            //alert the user if the wrong filename format is given
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect FileName");
            //alert message
            alert.setHeaderText("Please enter filename without any extensions");
            alert.show();

            //custom styling to the dialog box
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("Style.css").toExternalForm());
            return false;
        }
    }

    //method to add song to the sampleSongData.txt file
    public static boolean writeToFile(Song song) {
        FileWriter fileWriter = null;
        try {
            //check to see if a new file has been loaded
            if (fileChosen == true) {
                fileWriter = new FileWriter(file1, true);
            } else {
                fileWriter = new FileWriter("sampleSongData.txt", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //creates a buffered writer to write the newly added song to the file
        try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.newLine();
            //separates the details of the song by tab
            writer.write(song.getSongTitle() + "\t" + song.getArtist() + "\t" + song.getPlayingTime() + "\t" + "test.mp4");
            return true;
        } catch (IOException e) {
            e.getMessage();
            return false;
        }
    }

}

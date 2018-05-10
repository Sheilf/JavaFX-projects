package muse;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

//MediaplayerBar object: Handles media events with buttons & slider events 
public class MediaplayerBar extends VBox
{
    //mediaplayer objects from MPlayer
    MediaPlayer player;
    Mplayer mediaPlayer;
    
    //Container objects
    HBox progressSlider = new HBox();
    HBox buttonControls = new HBox();
    HBox tracks = new HBox();
    HBox videoControls = new HBox();
    HBox volumeControls = new HBox();
    
    //Objects with event listeners
    Slider progressbar = new Slider();            
    Slider volumeRocker= new Slider();           
    Button playbtn = new Button("||");           
    Button fforward = new Button(">>");           
    Button  previousTrack= new Button("|<<<");    
    Button bplay = new Button("<<");             
    Button  nextTrack= new Button(">>>|"); 

    //ui labels
    Label volumeValue = new Label();
    Label volumeLabel =  new  Label("Volume ");
    
    public MediaplayerBar(MediaPlayer player){     
        //copy local player from MPlayer
        this.player=player;
        
        //positioning & hierarchy
        buttonControls.setAlignment(Pos.CENTER);                   
        buttonControls.setPadding(new Insets(5,10,5,10));
        
        volumeRocker.setPrefHeight(70);            
        volumeRocker.setMinWidth(30);          
        volumeRocker.setValue(100);        
        
        volumeValue.setText(Double.toString(volumeRocker.getValue()));
        volumeValue.setTextFill(Color.DARKGREY);
        volumeLabel.setTextFill(Color.DARKGREY);
        
        progressbar.setMinWidth(30);
        playbtn.setPrefWidth(35);   
    
        // place items in containers
        tracks.getChildren().addAll(previousTrack, nextTrack);
        tracks.setAlignment(Pos.CENTER);
        tracks.setSpacing(15);
        
        videoControls.getChildren().addAll(bplay, playbtn, fforward);
        videoControls.setAlignment(Pos.CENTER);
        videoControls.setSpacing(10);
        
        volumeControls.getChildren().addAll(volumeLabel,volumeRocker,volumeValue);
        volumeControls.setAlignment(Pos.CENTER);
        volumeControls.setSpacing(10);
        
        buttonControls.getChildren().addAll(tracks, videoControls, volumeControls);      
        buttonControls.setSpacing(40);
        
        progressSlider.getChildren().addAll(progressbar);
        progressSlider.setAlignment(Pos.CENTER);
        progressSlider.setSpacing(10);
        progressbar.showTickMarksProperty().set(true);
        volumeRocker.showTickMarksProperty().set(true);

        //binds to keep view intact
        progressbar.prefWidthProperty().bind(progressSlider.widthProperty().subtract(120));
        volumeControls.prefWidthProperty().bind(buttonControls.widthProperty().multiply(0.4));
        volumeLabel.prefWidthProperty().bind(volumeControls.widthProperty().multiply(0.25));
        volumeRocker.prefWidthProperty().bind(volumeControls.widthProperty().multiply(0.5));
        volumeValue.prefWidthProperty().bind(volumeControls.widthProperty().multiply(0.25));
        
        getChildren().addAll(progressSlider, buttonControls);
        
        
        //SET OF EVENTS & AUXILIARY METHODS
        //*Note: Invalidation calls will occur
        //on methods that influence the sliders current value
        //For example: dragging the time slider or changing volume.
        
        
        playbtn.setOnAction(new EventHandler<ActionEvent>() {
            // action listener of play and pause button 
            @Override
            public void handle(ActionEvent event) {
                MediaPlayer.Status status = player.getStatus();     //getting current status of mediaplayer paused or playing              
                if(status==MediaPlayer.Status.PLAYING){             //checking player is playing song 
                    pause(player);                                  //change state of player to pause state 
                }
                else if(status==MediaPlayer.Status.PAUSED) {        // check if mediaplayer status is paused 
                    resume(player);                                 //then resume
                }
            }
        });
        
        fforward.setOnAction(new EventHandler<ActionEvent>(){       
            //faasforward button add duration of song with 5 secs  
            @Override
            public void handle(ActionEvent event) {
                fastForward();      //fastforward method call
            }
        });
        
        bplay.setOnAction(new EventHandler<ActionEvent>() {         
            //rewind action listener 
            @Override
            public void handle(ActionEvent event) {
                reWindBackward();  
            }
        });
        
        player.currentTimeProperty().addListener(new InvalidationListener(){  
            //action listener of slider that moves slider according to track duration 
            @Override  
            public void invalidated(Observable observable) {    
                updateProgressBar(); 
            }
            
            /*
                Note on InvalidationListener & void invalidated(Observable)
                https://docs.oracle.com/javase/8/javafx/api/javafx/beans/InvalidationListener.html
            
                "An ObservableValue generates two types of events: 
                 change events and invalidation events. 
                 A change event indicates that the value has changed.
                 An invalidation event is generated, if the current value is not valid anymore.
           
            */
        });
        
        progressbar.valueProperty().addListener(new InvalidationListener(){  
            //action listener of track progress bar 
            @Override
            public void invalidated(Observable observable)
            {
                if(progressbar.isPressed()){                
                    skipSomepart();
                }    
            }
        });
        
        volumeRocker.valueProperty().addListener(new InvalidationListener() {   
            // volume progress bar to change volume  
            @Override
            public void invalidated(Observable observable) {
                if(volumeRocker.isPressed()){                       
                    adjustVolume();
                }    
            }
        });
        
        nextTrack.setOnAction(new EventHandler<ActionEvent>() {      
            //next track button listener that plays next track from files list 
            @Override
            public void handle(ActionEvent event) {
                if(MainMedia.currentFileIndex==MainMedia.fileList.size()-1) {               //check if current file is last amoung files list 
                    player.pause();                                                         // pause the current track
                    mediaPlayer= new Mplayer(MainMedia.fileList.get(0).getAbsolutePath());  //call mPlayer class to play first or 0 index file from filelist
                    //MainMedia.fileList.get(0) gives zero index of arraylist
                    Scene scene = new Scene(mediaPlayer, 800, 600);
                    MainMedia.primaryStage.setScene(scene);
                    scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());     //change scene to newer scene
                    MainMedia.currentFileIndex  = 0;                                        //MainMedia.currentFileIndex + 1;
                }             
                else {
                    player.pause();                                                         // if file is not at last index then first pause the current file 
                    // Mplayer instance call that plays next track from list
                    mediaPlayer= new Mplayer(MainMedia.fileList.get(MainMedia.currentFileIndex+1).getAbsolutePath());   
                    //MainMedia.currentFileIndex+1) is index of current file in files list that is passsed to MainMedia.fileList.get() 
                    //which returns file instance and path of this file is passed to Mplayer construtor
                    Scene scene = new Scene(mediaPlayer, 800, 600);
                    MainMedia.primaryStage.setScene(scene);
                    scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
                    MainMedia.currentFileIndex  = MainMedia.currentFileIndex + 1;
                }
            }
        });
        
        previousTrack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //previous track action listener
                if(MainMedia.currentFileIndex==0){      //check if current file is zero index of file list 
                    player.pause();                      //pause current track
                    mediaPlayer= new Mplayer(MainMedia.fileList.get(MainMedia.fileList.size()-1).getAbsolutePath());  
                    //get index of last file from list and pass it to Mplayer class
                    Scene scene = new Scene(mediaPlayer, 800, 600);
                    MainMedia.primaryStage.setScene(scene);
                    scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
                    //change stage of frame
                    MainMedia.currentFileIndex  = MainMedia.fileList.size()-1;
                }
                else {
                    //else pause track and pass path of previous index file to Mplayer
                    player.pause();     
                    mediaPlayer= new Mplayer(MainMedia.fileList.get(MainMedia.currentFileIndex-1).getAbsolutePath());
                    Scene scene = new Scene(mediaPlayer, 800, 600);
                    MainMedia.primaryStage.setScene(scene);
                    scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());    //change stage of primary stage
                    MainMedia.currentFileIndex  = MainMedia.currentFileIndex - 1;
                }
            }
        });            
    }
    
    public void pause(MediaPlayer player) {  
        //pause state listner call 
        player.pause();  
        playbtn.setText(">");
    }
   
    public void resume(MediaPlayer player) {
        player.play();                  
        playbtn.setText("||");
    }
   
    public void updateProgressBar() {            
        // when track is played this function update postion of slider head
        //https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html#runLater-java.lang.Runnable-
        Platform.runLater(new Runnable() {
            @Override
            public void run() {     
                // this will calculates total percentage of duration that is played
                //and moves head according to value of percentage 
                progressbar.setValue(player.getCurrentTime().toMillis()/player.getTotalDuration().toMillis()*100);  
            }
        });
    }
    
   
   public void skipSomepart() {              
        //method to update  track when slider is draged
        //this check how much value of slider is changed then multiplies that value with duration 
        player.seek(player.getMedia().getDuration().multiply(progressbar.getValue()/100));    
   }
   
   public void adjustVolume() {     
        //method that changes volume when it is changed from silder
        player.setVolume(volumeRocker.getValue()/100);     //sets volume of player according to changed value of volume slider
        volumeValue.setText(Double.toString(Math.round(volumeRocker.getValue())));
    }
   
   public void fastForward() {                   
        //fastforward add 5 secs in current duration of songs and player.seek take current time + 5 secs and fatsforward song duration 
        player.seek(player.getCurrentTime().add(Duration.seconds(5)));
   }
   
   public void reWindBackward() {
        //rewindbackward subtracts 5 secs from current duration of songs and player.seek take current time - 5 secs and rewinds song  
        player.seek(player.getCurrentTime().subtract(Duration.seconds(5)));
   }
}

package muse;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

public class MediaplayerList extends VBox{
        ListView<String> listview ;
        MediaPlayer player;
        Button addFile;
        
    public MediaplayerList(MediaPlayer player) {
        //for adds file name to list 
//        for(int i =0;i<MainMedia.fileList.size();i++) {
//            //in if condition we check if name of file is already in names list then yes then we discard else we add this name into names list of files
//            if(!MainMedia.names.contains(MainMedia.fileList.get(i).getName())) {
//                MainMedia.names.add(MainMedia.fileList.get(i).getName());
//            }          
//        }
        this.player=player;
        addFile=new Button("+");        
        addFile.setStyle("-fx-border-color: azure");//File choosed button
        listview=  new ListView();                          //listview that holds names of availale files 
        listview.getItems().addAll(MainMedia.names);        //adds names list in listview
        listview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);    //setting listview selection mode to single file selection 
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(5,10,5,10));
        getChildren().add(listview);                        // add listview and file chooser button in scene
        getChildren().add(addFile);
        setSpacing(5);
 
        addFile.setOnAction(new EventHandler<ActionEvent>() {   
            //add files or file chooser button listener
            @Override
            public void handle(ActionEvent event) {
                // filechooser instance
                FileChooser filechooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter =  new FileChooser.ExtensionFilter("*.mp3","(*.mp3)","*.MP3","(*.MP3)"); //adding filters to file chooser
                filechooser.getExtensionFilters().add(extFilter);
                //open dialog box when add file button is hit
                File selectedFile= filechooser.showOpenDialog(null);
                
                if(selectedFile!=null) {
                    //if selected file is not null then populate this file into file list
                
                    if(MainMedia.fileList.isEmpty()) {
                        MainMedia.fileList.add(selectedFile);
                        // and add file name in file names list
                        MainMedia.names.add(selectedFile.getName());
                        // add file name into listview
                        listview.getItems().add(selectedFile.getName());
                        Mplayer player = new Mplayer(MainMedia.fileList.get(0).getAbsolutePath());
                        //change scene of primary stage
                        Scene scene = new Scene(player, 800, 600);
                        MainMedia.primaryStage.setScene(scene);
                        scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
                    }
                
                    else {
                        MainMedia.fileList.add(selectedFile);
                        // and add file name in file names list
                        MainMedia.names.add(selectedFile.getName());
                        // add file name into listview
                        listview.getItems().add(selectedFile.getName());
                    }
                }
             
            }
        });
        
        listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                if(event.getClickCount() == 2 && listview.getItems().isEmpty() == false) {
                    //get index of selected item from listview
                    int req=listview.getSelectionModel().getSelectedIndex();
                    //pause current track 
                    player.pause();     
                    //MainMedia.fileList.get(req).getAbsolutePath() gives path of file name selected from listview
                    Mplayer player = new Mplayer(MainMedia.fileList.get(req).getAbsolutePath());
                    //change scene of primary stage
                    Scene scene = new Scene(player, 800, 600);
                    MainMedia.primaryStage.setScene(scene);
                    scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
                }
            }
        }); 
    }  
}

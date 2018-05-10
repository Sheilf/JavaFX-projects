package muse;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 *
 * @author Sheilfer
 */
public class MediaMenu extends VBox{

    VBox menu = new VBox();
    MenuBar menuBar = new MenuBar();
    Menu menuFile = new Menu("File");
    Menu menuPlay = new Menu("Play");
    Menu menuEdit = new Menu("Edit");
    Menu menuView = new Menu("View");
    Menu menuHelp = new Menu("Help");
    MenuItem menuAdd = new MenuItem("Add...         ");
    MenuItem menuExit = new MenuItem("Exit          ");
    MenuItem playPause = new MenuItem("Play/Pause       ");
    MenuItem menuStop = new MenuItem("Stop          ");
    MenuItem forward = new MenuItem("Skip Forward       ");
    MenuItem back = new MenuItem("Skip Back         ");
    Menu menuVolume = new Menu("Volume");
    MenuItem volumeUp = new MenuItem("Increase        ");
    MenuItem volumeDown = new MenuItem("Decrease        ");
    MenuItem mute = new MenuItem("Mute      ");
    Menu skin = new Menu("Preset Skins          ");
    RadioMenuItem skinDefault = new RadioMenuItem("Default");
    RadioMenuItem skinAzure = new RadioMenuItem("Azure");
    RadioMenuItem skinShadow = new RadioMenuItem("Shadow");
    RadioMenuItem skinGrass = new RadioMenuItem("Grass");
    RadioMenuItem skinGold = new RadioMenuItem("Gold");
    ToggleGroup group = new ToggleGroup();
    CheckMenuItem menubarView = new CheckMenuItem("Menu Bar       ");
    CheckMenuItem playlist = new CheckMenuItem("Playlist          ");
    CheckMenuItem controls = new CheckMenuItem("Controls           ");
    MenuItem contact = new MenuItem("Contact");
    MenuItem about = new MenuItem("About");

    
    public MediaMenu(Text songData, MediaPlayer player, ImageView thumbnail, VBox image, MediaplayerBar bar, MediaplayerList list) { 
        
        setAlignment(Pos.TOP_LEFT);
        setSpacing(5);
        menuBar.getMenus().addAll(menuFile, menuPlay, menuEdit, menuView, menuHelp);
        getChildren().add(menuBar);
        
        group.getToggles().add(skinDefault);
        group.getToggles().add(skinAzure);
        group.getToggles().add(skinShadow);
        group.getToggles().add(skinGrass);
        group.getToggles().add(skinGold);
        menuFile.getItems().addAll(menuAdd, menuExit);
        menuPlay.getItems().addAll(playPause, menuStop, forward, back, menuVolume);
        menuEdit.getItems().addAll(skin);
        menuView.getItems().addAll(menubarView, playlist, controls);
        menuHelp.getItems().addAll(contact, about);
        menuVolume.getItems().addAll(volumeUp, volumeDown, mute);
        skin.getItems().addAll(skinDefault, skinAzure, skinShadow, skinGrass, skinGold);
        chooseSelected();

              
        menuAdd.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuAdd.setOnAction((ActionEvent e) -> {
            FileChooser filechooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =  new FileChooser.ExtensionFilter("*.mp3","(*.mp3)","*.MP3","(*.MP3)");
            filechooser.getExtensionFilters().add(extFilter);
            File selectedFile= filechooser.showOpenDialog(null);
            if (selectedFile!=null) {
                if (MainMedia.fileList.isEmpty()) {
                    MainMedia.fileList.add(selectedFile);
                    MainMedia.names.add(selectedFile.getName());
                    list.listview.getItems().add(selectedFile.getName());
                    Mplayer player1 = new Mplayer(MainMedia.fileList.get(0).getAbsolutePath());
                    Scene scene = new Scene(player1, 800, 600);
                        MainMedia.primaryStage.setScene(scene);
                        scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
                } else {
                    MainMedia.fileList.add(selectedFile);
                    
                    MainMedia.names.add(selectedFile.getName());
                
                    list.listview.getItems().add(selectedFile.getName());
                }
            } 
        });
        
        menuExit.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        menuExit.setOnAction((ActionEvent e) -> {
            System.exit(0); 
        });
        playPause.setAccelerator(KeyCombination.keyCombination("Shift+Space"));
        playPause.setOnAction((ActionEvent e) -> {
            MediaPlayer.Status status = player.getStatus();
            if(status==MediaPlayer.Status.PLAYING)
            {
                bar.pause(player);
            }
            else if(status==MediaPlayer.Status.PAUSED)
            {
                bar.resume(player);
            } 
        });
        menuStop.setAccelerator(KeyCombination.keyCombination("Shift+Q"));
        menuStop.setOnAction((ActionEvent e) -> {
            bar.pause(player);
            player.seek(Duration.ZERO); 
        });
        forward.setAccelerator(KeyCombination.keyCombination("Shift+Right"));
        forward.setOnAction((ActionEvent e) -> {
            bar.fastForward();
        });
        back.setAccelerator(KeyCombination.keyCombination("Shift+Left"));
        back.setOnAction((ActionEvent e) -> {
            bar.reWindBackward();
        });
        volumeUp.setAccelerator(KeyCombination.keyCombination("Shift+Up"));
        volumeUp.setOnAction((ActionEvent e) -> {
            double index = bar.volumeRocker.getValue();
            bar.volumeRocker.setValue(index + 5);
            bar.volumeValue.setText(Double.toString(Math.round(bar.volumeRocker.getValue())) + "%");
            player.setVolume(player.getVolume() + 0.05);

        });
        volumeDown.setAccelerator(KeyCombination.keyCombination("Shift+Down"));
        volumeDown.setOnAction((ActionEvent e) -> {
            double index = bar.volumeRocker.getValue();
            bar.volumeRocker.setValue(index - 5);
            bar.volumeValue.setText(Double.toString(Math.round(bar.volumeRocker.getValue())) + "%");
            player.setVolume(player.getVolume() - 0.05);
        });
        mute.setAccelerator(KeyCombination.keyCombination("Shift+M"));
        mute.setOnAction((ActionEvent e) -> {
            if(player.isMute() == true) {
                player.setMute(false);
            }
            else {
                player.setMute(true);
            } 
        });
        
        skinDefault.setOnAction((ActionEvent e) -> { 
            MainMedia.currentStyleIndex = 0;
            skinDefault.setDisable(false);
            chooseSelected();
            songData.setFill(Color.BLACK);
            menuBar.setStyle("-fx-background-color:whitesmoke;");
            bar.setStyle("-fx-background-color:whitesmoke;");
            list.setStyle("-fx-background-color:whitesmoke;");
            image.setStyle("-fx-background-color:whitesmoke;");
            list.listview.setStyle("-fx-background-color:whitesmoke;");
            list.listview.setStyle("-fx-control-inner-background:whitesmoke;");
     
        });
        skinAzure.setOnAction((ActionEvent e) -> {
            MainMedia.currentStyleIndex = 1;
            skinDefault.setDisable(false);
            chooseSelected();
            songData.setFill(Color.WHITE);
            menuBar.setStyle("-fx-background-color: #00376D;");
            list.setStyle("-fx-background-color:#00284E;");
            image.setStyle("-fx-background-color:#00284E;");
            list.listview.setStyle("-fx-background-color: #00376D;");
            list.listview.setStyle("-fx-control-inner-background:#00376D;");
            bar.setStyle("-fx-background-color: #00376D;");
        });
        skinGrass.setOnAction((ActionEvent e) -> {
            MainMedia.currentStyleIndex = 2;
            skinDefault.setDisable(false);
            chooseSelected();
            songData.setFill(Color.WHITE);
            menuBar.setStyle("-fx-background-color:#005527;");
            bar.setStyle("-fx-background-color:#005527;");
            list.setStyle("-fx-background-color:#001F0F;");
            image.setStyle("-fx-background-color:#001F0F;");
            list.listview.setStyle("-fx-background-color:#001F0F;");
            list.listview.setStyle("-fx-control-inner-background:#005527;");
        });
        skinShadow.setOnAction((ActionEvent e) -> {
            MainMedia.currentStyleIndex = 3;
            skinDefault.setDisable(false);
            chooseSelected();
            songData.setFill(Color.WHITE);
            menuBar.setStyle("-fx-background-color:rgba(27,27,27,0.8);");
            bar.setStyle("-fx-background-color:rgba(27,27,27,0.8);");
            list.setStyle("-fx-background-color:rgba(0,0,0,1);");
            image.setStyle("-fx-background-color:rgba(0,0,0,1);");
            list.listview.setStyle("-fx-background-color:rgba(0,0,0,1);");
            list.listview.setStyle("-fx-control-inner-background:rgba(27,27,27,0.8);");
            
        });
        skinGold.setOnAction((ActionEvent e) -> { 
            MainMedia.currentStyleIndex = 4;
            skinDefault.setDisable(false);
            chooseSelected();
            songData.setFill(Color.BLACK);
            menuBar.setStyle("-fx-background-color:gold;");
            bar.setStyle("-fx-background-color:gold;");
            list.setStyle("-fx-background-color:#FDC300;");
            image.setStyle("-fx-background-color:#FDC300;");
            list.listview.setStyle("-fx-background-color:#FDC300;");
            list.listview.setStyle("-fx-control-inner-background:gold;");
        });
        menubarView.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
        menubarView.setOnAction((ActionEvent e) -> {
            if(menuBar.isVisible() == true) {
                menuBar.setVisible(false);
            }
            else {
                menuBar.setVisible(true);
            } 
        });
        playlist.setAccelerator(KeyCombination.keyCombination("Ctrl+2"));
        playlist.setOnAction((ActionEvent e) -> {
            if(list.isVisible() == true) {
                list.setVisible(false);
                list.listview.setVisible(false);
                list.addFile.setVisible(false);
                thumbnail.setFitHeight(thumbnail.getFitHeight() + 150);
                thumbnail.setFitWidth(thumbnail.getFitWidth() + 150);
            }
            else {
                list.setVisible(true);
                list.listview.setVisible(true);
                list.addFile.setVisible(true);
                thumbnail.setFitHeight(thumbnail.getFitHeight() - 150);
                thumbnail.setFitWidth(thumbnail.getFitWidth() - 150);
            } 
        });
        controls.setAccelerator(KeyCombination.keyCombination("Ctrl+3"));
        controls.setOnAction((ActionEvent e) -> {
            if(bar.isVisible() == true) {
                bar.setVisible(false);
                thumbnail.setFitHeight(thumbnail.getFitHeight() + 100);
                thumbnail.setFitWidth(thumbnail.getFitWidth() + 100);
            }
            else {
                bar.setVisible(true);
                thumbnail.setFitHeight(thumbnail.getFitHeight() - 100);
                thumbnail.setFitWidth(thumbnail.getFitWidth() - 100);
            } 
        });
        contact.setOnAction((ActionEvent e) -> {
            Stage window = new Stage();
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Text text1 = new Text("Want to contact us?");
            Text text2 = new Text("Contact us through our emails!");
            Text text3 = new Text("XXXX@neiu.edu");
            
            vbox.getChildren().addAll(text1, text2, text3);
            window.setScene(new Scene(vbox, 350, 150));
            window.setTitle("Contact");
            window.show(); 
        });
        about.setOnAction((ActionEvent e) -> {
            Stage window = new Stage();
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Text text1 = new Text("Media Player v0.1 BETA");
            Text text2 = new Text("Created by:");
            Text text3 = new Text("Adeel Farah, Farooqui Osman, Hare Tomasz, Omoh Oshioke, and Zepeda Sheilfer.");
            
            vbox.getChildren().addAll(text1, text2, text3);
            window.setScene(new Scene(vbox, 600, 100));
            window.setTitle("About");
            window.show(); 
        });
    }
    private void chooseSelected(){
        if(MainMedia.currentStyleIndex == 0){
              skinDefault.setDisable(true);
              skinAzure.setDisable(false);
              skinGrass.setDisable(false);
              skinShadow.setDisable(false);      
              skinGold.setDisable(false);
        }
        else if(MainMedia.currentStyleIndex == 1){
              skinDefault.setDisable(false);
              skinAzure.setDisable(true);
              skinShadow.setDisable(false);
              skinGrass.setDisable(false);
              skinGold.setDisable(false);
        }
        else if(MainMedia.currentStyleIndex == 2){
              skinDefault.setDisable(false);
              skinAzure.setDisable(false);
              skinGrass.setDisable(true);
              skinShadow.setDisable(false);
              skinGold.setDisable(false);
        }
        else if(MainMedia.currentStyleIndex == 3){
              skinDefault.setDisable(false);
              skinAzure.setDisable(false);
              skinGrass.setDisable(false);
              skinShadow.setDisable(true);
              skinGold.setDisable(false);                             
        }
        else if(MainMedia.currentStyleIndex == 4){
              skinDefault.setDisable(false);
              skinAzure.setDisable(false);
              skinGrass.setDisable(false);
              skinShadow.setDisable(false);
              skinGold.setDisable(true);              
        }
    }
}





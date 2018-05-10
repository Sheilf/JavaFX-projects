package muse;

import java.io.File;
import javafx.collections.MapChangeListener;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;



//MPlayer object: Parent Media Objects instantiated here
public class Mplayer extends BorderPane {
    
    //file path string
    String path ;
    File file;  
    
    //Java Media parameters
    Media mediaFile;    
    MediaPlayer player;  
    MediaView view ; 
    
    //Developer designed Media elements
    MediaplayerBar bar; //Controls on bottom
    MediaplayerList medialist; //listview instance 
    MediaMenu menuBar; //Menu items on top
    
    //Song display content
    VBox image;
    ImageView thumbnail;
    Text songData;
    String songMeta = "";

    public Mplayer() {
        //initial launch
        file=new File("src\\media\\song.mp3");
     
        //instantiate displays
        songData = new Text();
        thumbnail = new ImageView();
        image = new VBox();
        image.getChildren().addAll(thumbnail, songData);
        image.setAlignment(Pos.CENTER); 
        image.setSpacing(15);
      
        //instantiate the file path
        //pass it into a java Media object
        String path = file.getAbsolutePath();                   
        mediaFile= new Media(new File(path).toURI().toString());
        player= new MediaPlayer(mediaFile);     
        view = new MediaView(player);        
        
        //then pass into developer Media objects.
        bar= new MediaplayerBar(player);      
        medialist= new MediaplayerList(player); 
        menuBar= new MediaMenu(songData, player, thumbnail, image, bar, medialist);
    
        //BorderPane superclass positions
        setTop(menuBar);                       
        setCenter(image);                      
        setBottom(bar); 
        setRight(medialist);
  

    }
    
    public Mplayer(String p) { 
        //constructor called after songs are up and running.
        file=new File(p);   
        if(!MainMedia.fileList.contains(file)){
            MainMedia.fileList.add(file);
        }
        
         //instantiate and style song displays
        songData = new Text();
        thumbnail = new ImageView();
        thumbnail.setFitHeight(300);
        thumbnail.setFitWidth(300);
        thumbnail.setPreserveRatio(true);
        
        image = new VBox();
        image.getChildren().addAll(thumbnail, songData);
        image.setAlignment(Pos.CENTER);
        image.setSpacing(15);
       
        //Init string file path, pass into Java Media objects
        String path = file.getAbsolutePath();                    
        mediaFile= new Media(new File(path).toURI().toString()); 
        player= new MediaPlayer(mediaFile);           
        view = new MediaView(player);   
        
        //developer media objects
        bar= new MediaplayerBar(player);              
        medialist= new MediaplayerList(player);        
        menuBar= new MediaMenu(songData, player, thumbnail, image, bar, medialist);

        //called and determined by static integer.
        chooseMediaSkin();
        setTop(menuBar);                       
        setCenter(image);                       
        setBottom(bar);                         
        setRight(medialist);                   

        player.play();                     

        //Process to acquire song meta data
        //Docs
        //https://docs.oracle.com/javase/8/javafx/api/javafx/collections/MapChangeListener.html
        //https://docs.oracle.com/javafx/2/api/javafx/scene/media/Media.html
        mediaFile.getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Object> ch) {
                if (ch.wasAdded()) {
                    handleMetadata(ch.getKey(), ch.getValueAdded());
                    /*
                        Process is called when a change occurs to an ObservableMap.
                        mediaFile is a Media Object. The interface defines getMetaData
                        which returns an ObservableMap.
                        
                        If the song contains no meta data, the ObservableMap
                        returned is empty.
                    
                    */
                }
            }
        }); 
    }
    private void handleMetadata(String key,Object value)  {
     if (key.equals("album")) {
      songMeta+="Album: " + value.toString() + "\n";
      
    } if (key.equals("artist")) {
            songMeta+="Artist: "+ value.toString() + "\n";
            
    } if (key.equals("title")) {
            songMeta+="Title: "+value.toString() + "\n";
            
    } if (key.equals("year")) {
      songData.setText(value.toString());
            songMeta+="Release: "+ value.toString() + "\n";
            
    } if (key.equals("image")) {
      thumbnail.setImage((Image)value);
    }
        songData.setText(songMeta);
        //songData.setStyle("-fx-effect: dropshadow(one-pass-box, black, 1, 1.0, 1, 1);");
    }    
  
    private void chooseMediaSkin(){     
        if(MainMedia.currentStyleIndex == 0){
            menuBar.menuBar.setStyle("-fx-background-color:whitesmoke;");
            bar.setStyle("-fx-background-color:whitesmoke;");
            medialist.setStyle("-fx-background-color:whitesmoke;");
            image.setStyle("-fx-background-color:whitesmoke;");
            setStyle("-fx-background-color: whitesmoke;");
            medialist.listview.setStyle("-fx-control-inner-background:whitesmoke;");
            songData.setFill(Color.BLACK);
        }
        else if(MainMedia.currentStyleIndex == 1){
            //azure theme
            medialist.setStyle("-fx-background-color:00284E;");
            image.setStyle("-fx-background-color:#00284E;");
            medialist.listview.setStyle("-fx-control-inner-background:#00376D;");
            bar.setStyle("-fx-background-color: #00376D;");
            menuBar.menuBar.setStyle("-fx-background-color: #00376D;");
            setStyle("-fx-background-color: #00284E");
            songData.setFill(Color.WHITE);
   
        }
        else if(MainMedia.currentStyleIndex == 2){
            //grass
            menuBar.menuBar.setStyle("-fx-background-color:#005527;");
            bar.setStyle("-fx-background-color:#005527;");
            medialist.setStyle("-fx-background-color:#001F0F;");
            image.setStyle("-fx-background-color:#001F0F;");
            medialist.listview.setStyle("-fx-control-inner-background:#005527");
            songData.setFill(Color.WHITE);
        }
        else if(MainMedia.currentStyleIndex == 3){
            //shadow
            menuBar.menuBar.setStyle("-fx-background-color:rgba(27,27,27,0.8);");
            bar.setStyle("-fx-background-color:rgba(27,27,27,0.8);");
            medialist.setStyle("-fx-background-color:rgba(0,0,0,1);");
            image.setStyle("-fx-background-color:rgba(0,0,0,1);");
            medialist.listview.setStyle("-fx-control-inner-background:rgba(27,27,27,0.8);");
            songData.setFill(Color.WHITE);
        }
        else if(MainMedia.currentStyleIndex == 4){
            //golden
            menuBar.menuBar.setStyle("-fx-background-color:gold;");
            bar.setStyle("-fx-background-color:gold;");
            medialist.setStyle("-fx-background-color:#FDC300;");
            image.setStyle("-fx-background-color:#FDC300;");
            medialist.listview.setStyle("-fx-control-inner-background:gold;");
            songData.setFill(Color.BLACK);
        }
        image.setAlignment(Pos.CENTER);
    }
}

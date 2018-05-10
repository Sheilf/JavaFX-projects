package muse;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author Sheilfer
 */
public class MainMedia extends Application {

    //statics managed throughout application functions
    public static Stage primaryStage;
    public static ArrayList<File> fileList;
    public static ArrayList<String> names;
    
    
    //Index for song list & media skin list
    public static int currentFileIndex;
    public static int currentStyleIndex;
    @Override
    
    public void start(Stage primaryStage) {
        //set statics
        fileList= new ArrayList<File>();    
        names= new ArrayList<String>();
        this.primaryStage=primaryStage;
        
        //construct Media elements
        Mplayer player = new Mplayer();  
        Scene scene = new Scene(player, 800, 600);  
               
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("app-icon.png")));
        primaryStage.setTitle("Muse"); 
        scene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
        primaryStage.setScene(scene); 
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }  
}

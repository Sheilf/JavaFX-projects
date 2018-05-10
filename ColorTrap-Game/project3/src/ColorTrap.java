


package project3;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;
    import java.util.Random;
    import javafx.animation.KeyFrame;
    import javafx.animation.Timeline;
    import javafx.application.Application;
    import static javafx.application.Application.launch;
    import javafx.event.EventHandler;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.stage.Stage;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.text.*;
    import javafx.util.Duration;


    public class ColorTrap extends Application
    {
        private Scene scene;
        private BorderPane borderPane;
        private Text txtCountDown = new Text(); 
        private Timeline timeline;
        private final int TIMER = 15;
        private int count = 0;
        /**********************
        ***********************
        ***********************
        **********************/
    
        
        //Borderpane containers
        private VBox topPane = new VBox();
        private VBox centerPane = new VBox();
        private HBox bottomPane = new HBox();       
        
        
        
        //**Try to bind 1 row pane with a fixed size to the centerPane
        //Center inner container for optionWords
        private FlowPane flowPaneParent = new FlowPane();
  
            
        //increment value when user selects correct option
        private int scoreCount = 0;
        
        
        //Game text nodes
        private Text trapWord = new Text();
        private Text optionWord1 = new Text();
        private Text optionWord2 = new Text();
        private Text optionWord3 = new Text();
        private Text optionWord4 = new Text();
        private Text optionWord5 = new Text();
        private Text optionWord6 = new Text();
        private Text optionWord7 = new Text();
        
        
        
        private final Text[] optionWords = {
            optionWord1,
            optionWord2,
            optionWord3,
            optionWord4,
            optionWord5,
            optionWord6, 
            optionWord7
        };
        
        //trapWord seeds to be displayed randomly. 
        //Index in trapWordList && colorTrapWordList MUST be organized the same way.
        //used in TrapWord method
        
        //**Possible alternative: 
            //Key:Value pair
            //String:Color pair
        
        
        private String[] trapWordList = {
            "ORANGE",
            "PURPLE",
            "BLACK",
            "BLUE",
            "YELLOW",
            "RED",
            "BROWN"
        };
            
        
        
        private Color[]  colorTrapWordList = {
            Color.ORANGE,
            Color.PURPLE,
            Color.BLACK,
            Color.BLUE,
            Color.YELLOW,
            Color.RED,
            Color.BROWN
        };
        
        
        
        //optionWord seeds to be shuffled and displayed
        private List <Color> colorOptionWordList = new ArrayList();
        private List <String> optionWordList = new ArrayList();
        
        
        
        //Bottom status text nodes
        private Text scoreTxtStatus = new Text("Score: " + scoreCount + "    ");
        private Text timerTxt = new Text("Time Remaining: ");
        
        
        
        //End game nodes: Button and Score nodes
        VBox endGameBox = new VBox();
        Button playAgainBtn = new Button("Play again");
        Text yourScoreView = new Text("Your score: " + scoreCount);
        
        
        
        //initial seed for Trap Word Method
        private int textSeed = (int) (Math.random() * 7);
        private int colorSeed = (int)(Math.random() * 7);
        
        
        
        //Extra credut nodes
        private Color[] bgColors = {
            Color.BEIGE, 
            Color.BURLYWOOD,
            Color.PINK,
            Color.GOLD,
            Color.LAVENDER,
            Color.CYAN,
            
        };
        
        
        private Timeline bgColorTimeline = new Timeline();
        private List<Color> bgColorList = new ArrayList();
        private final int quarterSecond = 250;
        
        
     
        @Override
        public void start(Stage primaryStage)
        {
            borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: lightgrey");
            scene = new Scene(borderPane, 600, 300);
            primaryStage.setMinHeight(300);
            primaryStage.setMinWidth(600);
            initializeGame();
            startPlay();
            primaryStage.setTitle("Color Trap");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        
        /*EC 
        */
        public void startPlay()
        {
            chooseTrapWordAndColor();
            colorNameOptions();
            
            for(int i =0; i < bgColors.length; i++){
                bgColorList.add(bgColors[i]);
            }
            
            
            count = TIMER;
            txtCountDown.setText(TIMER + "");
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000), e -> {

                        if(count >= 0)
                        {
                            txtCountDown.setText(count + "");
                            count--;
                        }
                        else
                        {
                            endOfGame();
                        }
                    }));
            timeline.setCycleCount(TIMER + 2);
            timeline.play();
            
            bgColorTimeline = new Timeline(new KeyFrame(
                    Duration.millis(quarterSecond), e -> {
                        Random randomColor = new Random();
                        Collections.shuffle(bgColorList, randomColor);
                            if(count >= 0){
                                CornerRadii rad = new CornerRadii(0); //set border srtroke
                                Color color = bgColorList.get(0);  //select color index 1 on shuffle
                                borderPane.setBackground(new Background(new BackgroundFill(color, rad, new Insets(0))));
                                
                                
                            }else{
                                Duration.millis(0);
                                bgColorTimeline.stop();
                            }
                    }
            ));
            
            bgColorTimeline.setCycleCount(Timeline.INDEFINITE);
            bgColorTimeline.play();
        }

        public void endOfGame()
        {
            //display score and button.
            yourScoreView.setText("Your score: "+ scoreCount);
            endGameBox.getChildren().addAll(yourScoreView, playAgainBtn);

            
            //empty game screen
           
            trapWord.setText("");
            
            for(int i = 0; i < optionWords.length;i++){
                optionWords[i].setText("");
            }
            
            scoreTxtStatus.setText("");
            timerTxt.setText("");
            txtCountDown.setText("");


            playAgainBtn.setOnMouseClicked((MouseEvent e) -> {
                //reset score and score status
                //startPlay() will call methods to set up content again and empty out endGame screen.
                scoreCount = 0;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");
                timerTxt.setText("Time Remaining: ");
                startPlay();
            });


        }

        public void checkChoice(Text choice)
        {
            /*
            yellow: 0xffff00ff
            Brown: 0xa52a2aff
            Blue: 0x0000ffff
            Black: 0x000000ff
            Red: 0xff0000ff
            Purple: 0x800080ff
            Orange: 0xffa500ff
                        
            Process: get the getFill code of trap and get the text code from clicked option.
            If they're equal, increment the score count node.
            
            SOPL(node.getFill()) returns a Paint object. Possible solution: use key/value pairs
            
            
            */
            if(trapWord.getFill().toString().equals("0xffff00ff") && choice.getText().equals("YELLOW")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");
            }else if(trapWord.getFill().toString().equals("0x000000ff") && choice.getText().equals("BLACK")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");   
            }
            else if(trapWord.getFill().toString().equals("0x0000ffff") && choice.getText().equals("BLUE")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");   
            }else if(trapWord.getFill().toString().equals("0xa52a2aff") && choice.getText().equals("BROWN")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");
            }else if(trapWord.getFill().toString().equals("0xff0000ff") && choice.getText().equals("RED")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");  
            }else if(trapWord.getFill().toString().equals("0xffa500ff") && choice.getText().equals("ORANGE")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");
            }else if(trapWord.getFill().toString().equals("0x800080ff") && choice.getText().equals("PURPLE")){
                scoreCount++;
                scoreTxtStatus.setText("Score: " + scoreCount + "    ");  
            }


            //Do NOT add any code after this comment
            //Choose a new trap word and options list
            chooseTrapWordAndColor();
            colorNameOptions();
        }
        public void chooseTrapWordAndColor()
        {
           /* Color list
                Likelyness of a seed is 1/length
                So the probability that they're both selected is pretty low.. 
                so in theory the recursion is pretty cheap.
            
                Both arrays are lined up the same way so the seed integer
                corresponds respectively.
            
            order:
            private String[] trapWordList = {"ORANGE", "PURPLE", "BLACK", "BLUE", "YELLOW", "RED", "BROWN"};
            */

             
             //range 0..6
             textSeed = (int) (Math.random() * 7);
             colorSeed = (int) (Math.random() * 7); 
             
             if(textSeed==colorSeed){
                 chooseTrapWordAndColor();
             }
             
             
             trapWord.setText(trapWordList[textSeed]);
             trapWord.setFill(colorTrapWordList[colorSeed]);
            
        }
        public void colorNameOptions()
        {
            /* Color list
            order:
            
            private String[] trapWordList = {"ORANGE", "PURPLE", "BLACK", "BLUE", "YELLOW", "RED", "BROWN"};
            
            We cannot use the arrays we used for trapWord because trapWord specifically depends
            on the lists being respective to one another's order.
            
            We'll use ArrayList to shuffle and make stuff expandable.

            originally wanted to hardcode the values but that would require like 600 lines of code.
            Arrays don't work with random integers since the length is size 7, so you're pretty much
            always gaurunteed duplicates.
            
            So we can use a Collection and List data structure. The process is essentially this:
            
            Collection is good to use here since it deals with disorder well
            and makes the List structure extendable. A bag of 50 seeds is no less complicated
            than a bag of 7 seeds.

            You have a bag of text and color seeds.  You 'shake' (shuffle) the bag
            to produce a random set of the given colors.
            
            Then you set the text after the shuffle process and you'll get a random
            version of all colors without duplicates.
            
            */

                    
            endGameBox.getChildren().clear();
         
            Random randomSeed = new Random();
            Collections.shuffle(optionWordList, randomSeed);
            Collections.shuffle(colorOptionWordList, randomSeed);
            
            for(int i = 0; i < optionWords.length; i++){
                
                optionWords[i].setText(optionWordList.get(i));
                optionWords[i].setFill(colorOptionWordList.get(i));
            }
                   
        }
        public void initializeGame()
        {
            
            //Some stuff here is looped. Some isn't for more direct control.
            
           for(int i = 0; i < trapWordList.length; i++){
               optionWordList.add(trapWordList[i]);
               colorOptionWordList.add(colorTrapWordList[i]);
           }
           
           
           //Center the borderpane parents
           topPane.setAlignment(Pos.CENTER);
           centerPane.setAlignment(Pos.CENTER);
           bottomPane.setAlignment(Pos.CENTER);
           flowPaneParent.setAlignment(Pos.CENTER);     
           endGameBox.setAlignment(Pos.CENTER);
           

           trapWord.setFont(Font.font("MARKER FELT", 60));
           yourScoreView.setFont(Font.font("MARKER FELT", 40));
           
           for(int i = 0; i < optionWords.length; i++){
               optionWords[i].setFont(Font.font("MARKER FELT",40));
           }
           
           
           //Bottom nodes, no special text
           scoreTxtStatus.setFont(Font.font(20));
           timerTxt.setFont(Font.font(20));
           txtCountDown.setFont(Font.font(20));
           
           

           flowPaneParent.setHgap(10);
           

           endGameBox.getChildren().addAll(yourScoreView, playAgainBtn);
           flowPaneParent.getChildren().addAll(endGameBox, optionWord1, optionWord2, optionWord3, optionWord4, optionWord5, optionWord6, optionWord7);
    

           //flowPaneParent.setStyle("-fx-background-color: pink;");
           
           centerPane.getChildren().addAll(flowPaneParent);
           bottomPane.getChildren().addAll(scoreTxtStatus, timerTxt, txtCountDown);
           topPane.getChildren().add(trapWord);
           
           
           borderPane.setCenter(centerPane);
           borderPane.setTop(topPane);
           borderPane.setBottom(bottomPane);

           //size respective positions
           centerPane.prefHeightProperty().bind(scene.heightProperty().multiply(0.55));
           topPane.prefHeightProperty().bind(scene.heightProperty().multiply(0.35));
           bottomPane.prefHeightProperty().bind(scene.heightProperty().multiply(0.10));
           
                //center...................
                 flowPaneParent.setMaxWidth(350);
                 endGameBox.setMaxWidth(350);
                 endGameBox.setMaxHeight(200);
                 

           //foreach optionWord clicked, do checkChoice
            for (Text optionWord : optionWords) {
                optionWord.setOnMouseClicked((MouseEvent e) -> {
                    checkChoice(optionWord);
                });
            }
            
            //Test border pane positions
            //topPane.setStyle("-fx-background-color: blue;");
            //centerPane.setStyle("-fx-background-color: green;");
            //bottomPane.setStyle("-fx-background-color: orange;");
        }
        public static void main(String[] args)
        {
            launch(args);
        }
    }
/*
Test leap: Year 2020
       Year 2024

2020 february:  (26..31), (1, SATURDAY ..29), (1..7)
2024 february:  (28..31), (1, THURSDAY,.. 29), (1..9)

Test new year counts:
2018 -> 2019 december, january: (Dec end: 31 monday.. count to 5)
                                (Jan start: Tueday, show 30,31, end month Thurs end count on 9)

2017 -> 2018:  Dec end Sunday, count to 6
               Jan start Monday, show 31, end on 10

*/


/**
 *
 * @author sheilfer
 */
package calendar;

import java.time.DayOfWeek;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.GregorianCalendar;
import javafx.event.ActionEvent;


public class Calendar extends Application
{
    private final BorderPane containerPane = new BorderPane();
    private final GridPane monthPane = new GridPane();
    private Scene scene;
    private final LocalDateTime date = LocalDateTime.now();
    /****
     * 
     * 
     */
    GregorianCalendar calendarObject = new GregorianCalendar(); 
    private StackPane dayGridPane;

    
    //List sets
    private final List <String> monthList = new ArrayList();
    private final Text[] daySet = {
        new Text("Sunday"),
        new Text("Monday"),
        new Text("Tuesday"),
        new Text("Wednesday"),
        new Text("Thursday"),
        new Text("Friday"),
        new Text("Saturday")
    };

    private final String[] daySetIndex = {
        "SUNDAY",
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY"
    };

    private final String[] months = {
        "JANUARY",
        "FEBRUARY",
        "MARCH",
        "APRIL",
        "MAY",
        "JUNE",
        "JULY",
        "AUGUST",
        "SEPTEMBER",
        "OCTOBER",
        "NOVEMBER",
        "DECEMBER"
    };

    //buttons set
    private final Button previousButton = new Button("<");
    private final Button nextButton = new Button(">");
    private final Button todayButton = new Button("Today");

    //counts
    private int dayNameIndex = 0;
    private int currentMonth;  
    private int currentYear;
    private int currentDay;
    private int monthBeforeCurrent;
    private int monthAfterCurrent;   

    private int dayCounts;
    private int priorMonthCounts;
    private LocalDate getFirstDay = LocalDate.now();
  
    //color text switch
    private boolean isOnStapleMonth;
    private boolean isDayGray;
    
    //displays
    private DayOfWeek dayName;    
    private Circle redCircle;
    private final Text displayMonth = new Text();
    private final Text displayYear = new Text();
    private Text dayNumberText;
    
    //containers
    private final HBox displayContainer = new HBox();
    private final HBox buttonContainer = new HBox();
    private final HBox topPane = new HBox();    


    @Override
    public void start(Stage primaryStage)
    {
        scene = new Scene(containerPane, 900, 650);
        setupCalendarPane();

        primaryStage.setTitle("Calendar");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);
        primaryStage.setScene(scene);
        primaryStage.show();        
    }

    public void setupCalendarPane()
    {        
            
        monthList.addAll(Arrays.asList(months)); //add array Jan..Dec
          
        //get firsts
        currentYear = date.getYear();
        currentMonth = date.getMonthValue();
        currentDay = date.getDayOfMonth(); 
        isOnStapleMonth = true;

        //init display
        displayMonth.setText(monthList.get(currentMonth-1)+ "");
        displayYear.setText(date.getYear() + "");
        displayMonth.setStyle("-fx-font-size: 24;");
        displayYear.setStyle("-fx-font-size: 24;");
        
        //Placing & position display
        buttonContainer.getChildren().addAll(previousButton, todayButton, nextButton);
        displayContainer.getChildren().addAll(displayMonth, displayYear);
        
        buttonContainer.setSpacing(10);
        displayContainer.setSpacing(10);
        buttonContainer.setPadding(new Insets(15,20,0,10));
        displayContainer.setPadding(new Insets(10,10,0,20));
           
        //split top in even halves
        buttonContainer.prefWidthProperty().bind(scene.widthProperty().multiply(0.50));
        displayContainer.prefWidthProperty().bind(scene.widthProperty().multiply(0.50));
        
        //place top
        topPane.getChildren().addAll(displayContainer, buttonContainer); 
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        displayContainer.setAlignment(Pos.TOP_LEFT);
        
        //place parent
        monthPane.setPadding(new Insets(15));
        containerPane.setCenter(monthPane);
        containerPane.setTop(topPane);
        
        
        
        fillUpMonth();
    }

    public void fillUpMonth()
    {
        //iterative counts
        int daysInPrior = 0;
        int days = 0;
        int daysInNext = 0;
        dayCounts = 1;

        //iterative switches
        boolean isInMonthSubset = false;
        boolean isOnStapleYear = currentYear==date.getYear();
        boolean isLeapYear = calendarObject.isLeapYear(currentYear);

        //seperate block of code into method
        getFirstDay = getCurrentMonthFirstDay();
        dayName = getFirstDay.getDayOfWeek();
        //System.out.println(dayName);

        //capitalized...
        for(int i=0; i < daySetIndex.length; i++){
           if(dayName.toString().equals(daySetIndex[i]))
                dayNameIndex = i;
        }

        //bad idea for DRY here
        //handle days in month

        if(     
                monthBeforeCurrent == 4 ||
                monthBeforeCurrent == 6 || 
                monthBeforeCurrent == 9 || 
                monthBeforeCurrent == 11
        )
            daysInPrior = 30;
        else if(
                monthBeforeCurrent == 1 ||
                monthBeforeCurrent == 3 ||
                monthBeforeCurrent == 5 ||
                monthBeforeCurrent == 7 ||
                monthBeforeCurrent == 8 ||
                monthBeforeCurrent == 10 || 
                monthBeforeCurrent == 12
        )
            daysInPrior = 31;
        else if(!isLeapYear)
            daysInPrior = 28;
        else //least likely switch
            daysInPrior = 29;

        priorMonthCounts = daysInPrior - (dayNameIndex - 1);
        
        
        if(
                currentMonth == 4 ||
                currentMonth == 6 || 
                currentMonth == 9 || 
                currentMonth == 11
        )   //then
            days = 30;
        else if(
                currentMonth == 1 || 
                currentMonth == 3 ||
                currentMonth == 5 ||
                currentMonth == 7 ||
                currentMonth == 8 ||
                currentMonth == 10 ||
                currentMonth == 12
        )
            days = 31;
        else if(!isLeapYear)
            days = 28;
        else 
            days = 29; 


        //month set 3: monthBeforeCurrent <- current month <- next month
        if(
                monthAfterCurrent == 4 ||
                monthAfterCurrent == 6 || 
                monthAfterCurrent == 9 ||
                monthAfterCurrent == 11
        ) 
            daysInNext= 30;
        else if(
                monthAfterCurrent == 1 ||
                monthAfterCurrent == 3 ||
                monthAfterCurrent == 5 ||
                monthAfterCurrent == 7 || 
                monthAfterCurrent == 8 ||
                monthAfterCurrent == 10 ||
                monthAfterCurrent == 12
        )
            daysInNext = 31;
        else if(!isLeapYear)
            daysInNext = 28;
        else
            daysInNext = 28;

       for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                //styles
                dayGridPane = new StackPane();
                dayGridPane.setStyle("-fx-border-color: blue");

                
                //texts
                Text priorMonthGrayDayText = new Text(priorMonthCounts + "");
                dayNumberText = new Text(dayCounts + "");

                if(i == 0){ 
                    //place day names on row 0
                    dayGridPane.getChildren().add(daySet[j]);
                    dayGridPane.prefHeightProperty().bind(scene.heightProperty().divide(14));
                }else{
                    //place numbers star to end
                    dayGridPane.prefWidthProperty().bind(scene.widthProperty().divide(7));
                    dayGridPane.prefHeightProperty().bind(scene.heightProperty().divide(7));
                        
                        //if current day, in actual month, on current year, place red circle
                        if(dayCounts == currentDay && isOnStapleMonth && isOnStapleYear){
                            redCircle = new Circle(15);
                            redCircle.setFill(Color.RED);
                            dayNumberText.setStroke(Color.WHITE);
                            dayGridPane.getChildren().add(redCircle);
                        }
                        
                        //handle gray text
                        if(isInMonthSubset || i == 1 && j == dayNameIndex){
                            isInMonthSubset = true;
                            
                            //handle gray prior
                            if(dayCounts < days){
                                if(isDayGray){
                                    dayNumberText.setFill(Color.GRAY);
                                }
                                dayCounts++;
                            }else if(dayCounts == days){ 
                                //handle gray next
                                isDayGray = true;
                                isOnStapleMonth = false;
                                dayCounts = 1;
                            }
                            //place day, gray or black
                            dayGridPane.getChildren().add(dayNumberText);
                        }else{ 
                            //handle gray prior
                            priorMonthGrayDayText.setFill(Color.GRAY);    
                            priorMonthCounts++;
                            dayGridPane.getChildren().add(priorMonthGrayDayText);
                        }  
                }//end parent if/else, end of for J loop
                monthPane.add(dayGridPane, j, i);                
            }
        }      
    
        
        previousButton.setOnAction((ActionEvent e) -> {
            //Button process

            //check for start/end
            //clear circle and month
            //set new parent counts
            System.out.println(e);
            if(currentMonth - 1 > 0){ 
                currentMonth = currentMonth - 1;
            }else{
                currentYear--;
                currentMonth = 12;
            }
            monthPane.getChildren().clear();  
            iterateCalendar();

        });

        nextButton.setOnAction(e  -> {

            if(currentMonth + 1 < 13){
                currentMonth = currentMonth + 1;
            }else{
                currentYear++;
                currentMonth = 1;
            }
            
            monthPane.getChildren().clear();   
            iterateCalendar();
        }); 

        todayButton.setOnAction(e -> {
            
        

            currentMonth = date.getMonthValue();
            displayMonth.setText(monthList.get(currentMonth - 1));
            
            currentYear = date.getYear();
            displayYear.setText(currentYear + "");
            
            isDayGray = false;          
            isOnStapleMonth = true;
            monthPane.getChildren().clear();     
            fillUpMonth();
        });        
    }

    public LocalDate getCurrentMonthFirstDay(){
        //possible loop
        //for i.. 
        //count is 1
        //-> if month = (month of 30) || (month of 31) || month of (28) || leap
        // add +30, +31, +28, +29, use days In Next in if Else to Dry out properly
        switch (currentMonth) {
            case 1:
                //System.out.println(getFirstDay.withDayOfYear(1));
                //System.out.println(getFirstDay.withDayOfYear(1).withYear(currentYear));
                return getFirstDay = getFirstDay.withDayOfYear(1).withYear(currentYear);
            case 2:
                return getFirstDay = getFirstDay.withDayOfYear(32).withYear(currentYear);

            case 3:
                return  getFirstDay = getFirstDay.withDayOfYear(60).withYear(currentYear);

            case 4:
                return getFirstDay = getFirstDay.withDayOfYear(91).withYear(currentYear);

            case 5:
                return getFirstDay = getFirstDay.withDayOfYear(121).withYear(currentYear);

            case 6:
                return getFirstDay = getFirstDay.withDayOfYear(152).withYear(currentYear);
                
            case 7:
                return getFirstDay = getFirstDay.withDayOfYear(182).withYear(currentYear);
                
            case 8:
                return getFirstDay = getFirstDay.withDayOfYear(213).withYear(currentYear);
           
            case 9:
                return getFirstDay = getFirstDay.withDayOfYear(244).withYear(currentYear);

            case 10:
                return getFirstDay = getFirstDay.withDayOfYear(274).withYear(currentYear);

            case 11:
                return getFirstDay = getFirstDay.withDayOfYear(305).withYear(currentYear);

            case 12:
                return getFirstDay = getFirstDay.withDayOfYear(335).withYear(currentYear);

            default:
               return getFirstDay = getFirstDay.withDayOfYear(1).withYear(currentYear);
        }
    }
    public void iterateCalendar(){
                    
        if(currentMonth - 1 > 0){
             monthBeforeCurrent = currentMonth - 1;
        }else{
            monthBeforeCurrent = 12;
        }
        if(currentMonth + 1 < 13){
            monthAfterCurrent = currentMonth + 1;
        }else{
            monthAfterCurrent = 1;
        }          
        
        displayYear.setText(currentYear + "");
        displayMonth.setText(monthList.get(currentMonth - 1));
        isDayGray = false;

        isOnStapleMonth = currentMonth == date.getMonthValue();            
        fillUpMonth();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}


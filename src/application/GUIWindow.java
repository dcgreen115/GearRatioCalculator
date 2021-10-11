package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUIWindow {
    
    private Calculator calc;
    
    private Stage stage = null;
    private BorderPane border;
    private GridPane leftGrid;
    
    private Button loadDefaultsButton;
    private Button loadConfigButton;
    private Button saveConfigButton;
    private Button updateGraphButton;
    private ToggleButton showFullGraphButton;
    
    private Label maximumRPMLabel;
    private Label minimumRPMLabel;
    private Label shiftRPMLabel;
    private Label tireDiameterLabel;
    private Label rimDiameterLabel;
    private Label tireWidthLabel;
    private Label tireAspectRatioLabel;
    private Label finalDriveRatioLabel;
    private Label frontSprocketTeethLabel;
    private Label rearSprocketTeethLabel;
    //Label updateGraphErrorLabel;
    
    private TextField maximumRPMField;
    private TextField minimumRPMField;
    private TextField shiftRPMField;
    private TextField tireDiameterField;
    private TextField rimDiameterField;
    private TextField tireWidthField;
    private TextField tireAspectRatioField;
    private TextField finalDriveRatioField;
    private TextField frontSprocketTeethField;
    private TextField rearSprocketTeethField;
    private TextField firstGearRatioField;
    private TextField secondGearRatioField;
    private TextField thirdGearRatioField;
    private TextField fourthGearRatioField;
    private TextField fifthGearRatioField;
    private TextField sixthGearRatioField;
    
    private boolean showFullGraph = false;
    private boolean speedGraphIsShown = true;
    String pathToLastConfigFile;
    
    public GUIWindow(Stage primaryStage) {
        calc = new Calculator(new Parser("configs/defaults.json"));
        
        
        try {
            primaryStage.setTitle("GearRatioCalc");
            primaryStage.getIcons().add(new Image("file:assets/gear3.png"));
            
            border = new BorderPane();
            border.setTop(addTopButtons());
            border.setLeft(addLeftInputBoxes());
            border.setCenter(addSpeedGraph());
            border.setBottom(addBottomButtons());
            
            //Point p = MouseInfo.getPointerInfo().getLocation();
            //int mouseXPos = p.x;
            //int mouseYPos = p.y;
            //Bounds chartBounds = speedChart.getBoundsInParent();
            
            Scene scene  = new Scene(border,1200,750);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();
            
            setButtonAssignments();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private ScatterChart<Number, Number> addSpeedGraph() {
        
        NumberAxis speedXAxis = new NumberAxis();
        NumberAxis speedYAxis = new NumberAxis();
        speedXAxis.setLabel("Ground Speed (mph)");
        speedYAxis.setLabel("Engine RPM");
        
        ScatterChart<Number,Number> speedChart = new ScatterChart<Number,Number>(speedXAxis,speedYAxis);
        speedChart.setTitle("Engine RPM vs. Ground Speed Per Gear");
        
        
        //Get data for the possible speeds in each gear and add those points to the scatter plot
        for (int gear = 0; gear < calc.getNumGears(); gear++) {
//            for (long rpm = 0; rpm <= calc.getParser().getMaxRPM() - calc.getParser().getMinRPM(); rpm += calc.getParser().getRPMStepSize()) {
//                rpmVsSpeedPerGear.getData().add(new XYChart.Data<Number, Number>(
//                    calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getParser().getRPMStepSize())], 
//                    rpm + calc.getParser().getMinRPM()));
//            }
            
            long graphStartRPM;
            switch (gear) {
                
                
                case 0:
                    System.out.println(gear);
                    Series<Number, Number> firstGear = new XYChart.Series<Number, Number>();
                    for (long rpm = 0; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        firstGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    firstGear.setName("1st Gear  ");
                    speedChart.getData().add(firstGear);
                    break;
                    
                case 1:
                    System.out.println(gear);
                    Series<Number, Number> secondGear = new XYChart.Series<Number, Number>();
                    
                    graphStartRPM = 0;
                    if (!showFullGraph) {
                        graphStartRPM = calc.calculateRPMAtSpeed(gear, calc.getMaxSpeeds()[gear - 1]) - calc.getMinRPM();
                    }
                    
                    for (long rpm = graphStartRPM; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        secondGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    secondGear.setName("2nd Gear  ");
                    speedChart.getData().add(secondGear);
                    break;
                    
                case 2:
                    System.out.println(gear);
                    Series<Number, Number> thirdGear = new XYChart.Series<Number, Number>();
                    
                    graphStartRPM = 0;
                    if (!showFullGraph) {
                        graphStartRPM = calc.calculateRPMAtSpeed(gear, calc.getMaxSpeeds()[gear - 1]) - calc.getMinRPM();
                    }
                    
                    for (long rpm = graphStartRPM; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        thirdGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    thirdGear.setName("3rd Gear  ");
                    speedChart.getData().add(thirdGear);
                    break;
                    
                case 3:
                    System.out.println(gear);
                    Series<Number, Number> fourthGear = new XYChart.Series<Number, Number>();
                    
                    graphStartRPM = 0;
                    if (!showFullGraph) {
                        graphStartRPM = calc.calculateRPMAtSpeed(gear, calc.getMaxSpeeds()[gear - 1]) - calc.getMinRPM();
                    }
                    
                    for (long rpm = graphStartRPM; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        fourthGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    fourthGear.setName("4th Gear  ");
                    speedChart.getData().add(fourthGear);
                    break;
                    
                case 4:
                    System.out.println(gear);
                    Series<Number, Number> fifthGear = new XYChart.Series<Number, Number>();
                    
                    graphStartRPM = 0;
                    if (!showFullGraph) {
                        graphStartRPM = calc.calculateRPMAtSpeed(gear, calc.getMaxSpeeds()[gear - 1]) - calc.getMinRPM();
                    }
                    
                    for (long rpm = graphStartRPM; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        fifthGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    fifthGear.setName("5th Gear  ");
                    speedChart.getData().add(fifthGear);
                    break;
                    
                case 5:
                    System.out.println(gear);
                    Series<Number, Number> sixthGear = new XYChart.Series<Number, Number>();
                    
                    graphStartRPM = 0;
                    if (!showFullGraph) {
                        graphStartRPM = calc.calculateRPMAtSpeed(gear, calc.getMaxSpeeds()[gear - 1]) - calc.getMinRPM();
                    }
                    
                    for (long rpm = graphStartRPM; rpm <= calc.getMaxRPM() - calc.getMinRPM(); rpm += calc.getRPMStepSize()) {
                        sixthGear.getData().add(new XYChart.Data<Number, Number>(
                            //Speed
                            calc.getSpeedsPerGear()[gear][(int) (rpm / calc.getRPMStepSize())], 
                            //RPM
                            rpm + calc.getMinRPM()));
                    }
                    sixthGear.setName("6th Gear  ");
                    speedChart.getData().add(sixthGear);
                    break;
                    
            }
        }
        return speedChart;
    }
    
    private HBox addTopButtons() {
        HBox topButtons = new HBox();
        topButtons.setPadding(new Insets(15, 12, 15, 12));
        topButtons.setSpacing(10);
        
        loadDefaultsButton = new Button("Load Defaults");
        loadDefaultsButton.setPrefSize(100, 20);
        topButtons.getChildren().add(loadDefaultsButton);
        
        loadConfigButton = new Button("Load Config");
        loadConfigButton.setPrefSize(100, 20);
        
        saveConfigButton = new Button("Save Config");
        saveConfigButton.setPrefSize(100, 20);
        topButtons.getChildren().addAll(loadConfigButton, saveConfigButton);
        
        return topButtons;
    }
    
    private AnchorPane addLeftInputBoxes() {
        
        AnchorPane leftInputBoxes = new AnchorPane();
        leftGrid = new GridPane();
        leftGrid.setHgap(10);
        leftGrid.setVgap(10);
        leftGrid.setPadding(new Insets(0, 10, 0, 10));
        updateGraphButton = new Button("Update Graph");
        
        maximumRPMLabel = new Label("Maximum RPM:");
        maximumRPMLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        maximumRPMField = new TextField();
        maximumRPMField.setPromptText("Enter a whole number");
        leftGrid.add(maximumRPMLabel, 0, 0);
        leftGrid.add(maximumRPMField, 1, 0);
        
        minimumRPMLabel = new Label("Minimum RPM:");
        minimumRPMLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        minimumRPMField = new TextField();
        minimumRPMField.setPromptText("Enter a whole number");
        leftGrid.add(minimumRPMLabel, 0, 1);
        leftGrid.add(minimumRPMField, 1, 1);
        
        shiftRPMLabel = new Label("Shift RPM:");
        shiftRPMLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        shiftRPMField = new TextField();
        shiftRPMField.setPromptText("Currently unused");
        leftGrid.add(shiftRPMLabel, 0, 2);
        leftGrid.add(shiftRPMField, 1, 2);
        
        Separator separator1 = new Separator();
        leftGrid.add(separator1, 0, 3);
        
        tireDiameterLabel = new Label("Overall Tire Diameter:");
        tireDiameterLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        tireDiameterField = new TextField();
        tireDiameterField.setPromptText("Enter value in inches");
        leftGrid.add(tireDiameterLabel, 0, 4);
        leftGrid.add(tireDiameterField, 1, 4);
        
        rimDiameterLabel = new Label("Rim Diameter:");
        rimDiameterLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        rimDiameterField = new TextField();
        rimDiameterField.setPromptText("Enter value in inches");
        leftGrid.add(rimDiameterLabel, 0, 5);
        leftGrid.add(rimDiameterField, 1, 5);
        
        tireWidthLabel = new Label("Tire Width:");
        tireWidthLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        tireWidthField = new TextField();
        tireWidthField.setPromptText("Enter value in mm");
        leftGrid.add(tireWidthLabel, 0, 6);
        leftGrid.add(tireWidthField, 1, 6);
        
        tireAspectRatioLabel = new Label("Tire Aspect Ratio:");
        tireAspectRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        tireAspectRatioField = new TextField();
        tireAspectRatioField.setPromptText("Enter a whole number");
        leftGrid.add(tireAspectRatioLabel, 0, 7);
        leftGrid.add(tireAspectRatioField, 1, 7);
        
        Separator separator2 = new Separator();
        leftGrid.add(separator2, 0, 8);
        
        finalDriveRatioLabel = new Label("Final Drive Ratio:");
        finalDriveRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        finalDriveRatioField = new TextField();
        finalDriveRatioField.setPromptText("Enter a decimal number");
        leftGrid.add(finalDriveRatioLabel, 0, 9);
        leftGrid.add(finalDriveRatioField, 1, 9);
        
        frontSprocketTeethLabel = new Label("Front Sprocket Teeth:");
        frontSprocketTeethLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        frontSprocketTeethField = new TextField();
        frontSprocketTeethField.setPromptText("Enter a whole number");
        leftGrid.add(frontSprocketTeethLabel, 0, 10);
        leftGrid.add(frontSprocketTeethField, 1, 10);
        
        rearSprocketTeethLabel = new Label("Rear Sprocket Teeth:");
        rearSprocketTeethLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
        rearSprocketTeethField = new TextField();
        rearSprocketTeethField.setPromptText("Enter a whole number");
        leftGrid.add(rearSprocketTeethLabel, 0, 11);
        leftGrid.add(rearSprocketTeethField, 1, 11);
        
        Separator separator3 = new Separator();
        leftGrid.add(separator3, 0, 12);
        
        int rowsUsed = 12;
        
        for (int gear = 1; gear <= calc.getNumGears(); gear++) {
            switch (gear) {
                case 1:
                    Label firstGearRatioLabel = new Label("1st Gear Ratio:");
                    firstGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    firstGearRatioField = new TextField();
                    firstGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(firstGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(firstGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 13;
                    break;
                case 2:
                    Label secondGearRatioLabel = new Label("2nd Gear Ratio:");
                    secondGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    secondGearRatioField = new TextField();
                    secondGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(secondGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(secondGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 14;
                    break;
                case 3:
                    Label thirdGearRatioLabel = new Label("3rd Gear Ratio:");
                    thirdGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    thirdGearRatioField = new TextField();
                    thirdGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(thirdGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(thirdGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 15;
                    break;
                case 4:
                    Label fourthGearRatioLabel = new Label("4th Gear Ratio:");
                    fourthGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    fourthGearRatioField = new TextField();
                    fourthGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(fourthGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(fourthGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 16;
                    break;
                case 5:
                    Label fifthGearRatioLabel = new Label("5th Gear Ratio:");
                    fifthGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    fifthGearRatioField = new TextField();
                    fifthGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(fifthGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(fifthGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 17;
                    break;
                case 6:
                    Label sixthGearRatioLabel = new Label("6th Gear Ratio:");
                    sixthGearRatioLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 15));
                    sixthGearRatioField = new TextField();
                    sixthGearRatioField.setPromptText("Enter a decimal number");
                    leftGrid.add(sixthGearRatioLabel, 0, rowsUsed + 1);
                    leftGrid.add(sixthGearRatioField, 1, rowsUsed + 1);
                    rowsUsed = 18;
                    break;
            }
        }
        
        Separator separator4 = new Separator();
        leftGrid.add(separator4, 0, rowsUsed + 1);
        rowsUsed++;
        
        leftGrid.add(updateGraphButton, 0, rowsUsed + 1);
        
        
        
        leftInputBoxes.getChildren().add(leftGrid);
        return leftInputBoxes;
    }
    
    private HBox addBottomButtons() {
        HBox bottomButtons = new HBox();
        bottomButtons.setPadding(new Insets(15, 12, 15, 12));
        bottomButtons.setSpacing(10);
        
        showFullGraphButton = new ToggleButton("Show Full Graph");
        
        bottomButtons.getChildren().add(showFullGraphButton);
        
        return bottomButtons;
    }
    
    public void loadDefaultConfig() {
        Parser defaults = new Parser();
        maximumRPMField.setText(String.valueOf(defaults.getMaxRPM()));
        minimumRPMField.setText(String.valueOf(defaults.getMinRPM()));
        tireDiameterField.setText(String.valueOf(defaults.getUserDefinedTireDiameter()));
        rimDiameterField.setText(String.valueOf(defaults.getRimDiameter()));
        tireWidthField.setText(String.valueOf(defaults.getTireWidth()));
        tireAspectRatioField.setText(String.valueOf(defaults.getTireAspectRatio()));
        finalDriveRatioField.setText(String.valueOf(defaults.getFinalDriveRatio()));
        frontSprocketTeethField.setText(String.valueOf(defaults.getFrontSprocketTeeth()));
        rearSprocketTeethField.setText(String.valueOf(defaults.getRearSprocketTeeth()));
        firstGearRatioField.setText(String.valueOf(defaults.getGearRatios()[0]));
        secondGearRatioField.setText(String.valueOf(defaults.getGearRatios()[1]));
        thirdGearRatioField.setText(String.valueOf(defaults.getGearRatios()[2]));
        fourthGearRatioField.setText(String.valueOf(defaults.getGearRatios()[3]));
        fifthGearRatioField.setText(String.valueOf(defaults.getGearRatios()[4]));
        
        calc.setMaxRPM(11000);
        calc.setMinRPM(2000);
        calc.setShiftRPM(10900);
        calc.setUserDefinedTireDiameter(16.0);
        calc.setRimDiameter(0.0);
        calc.setTireWidth(0);
        calc.setTireAspectRatio(0);
        calc.setFinalDriveRatio(2.23);
        calc.setFrontSprocketTeeth(12);
        calc.setRearSprocketTeeth(38);
        calc.setGearRatio(0,  2.42);
        calc.setGearRatio(1,  1.73);
        calc.setGearRatio(2,  1.31);
        calc.setGearRatio(3,  1.05);
        calc.setGearRatio(4,  0.84);
        calc.setGearRatio(5,  0.00);
        updateGraph();
    }
    
    private void setButtonAssignments() {
        loadDefaultsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                loadDefaultConfig();
            }
        });
        loadConfigButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                loadConfig();
            }
        });
        updateGraphButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetTextColor();
                if (getEnteredValues()) {
                    updateGraph();
                }
            }
        });
        ToggleGroup group = new ToggleGroup();
        showFullGraphButton.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    //Button is not pressed
                    showFullGraph = false;
                }
                else {
                    //Button is pressed
                    showFullGraph = true;
                }
                border.setCenter(addSpeedGraph());
            }
        });
    }
    
    public void updateGraph() {
        if (speedGraphIsShown) {
            calc.calculateSpeedsPerGear();
            border.setCenter(addSpeedGraph());
        }
        else { //Torque graph is shown
            //Add torque graph
        }
    }
    
    private void resetTextColor() {
        maximumRPMLabel.setTextFill(Color.BLACK);
        minimumRPMLabel.setTextFill(Color.BLACK);
        tireDiameterLabel.setTextFill(Color.BLACK);
        rimDiameterLabel.setTextFill(Color.BLACK);
        tireWidthLabel.setTextFill(Color.BLACK);
        tireAspectRatioLabel.setTextFill(Color.BLACK);
        finalDriveRatioLabel.setTextFill(Color.BLACK);
        frontSprocketTeethLabel.setTextFill(Color.BLACK);
        rearSprocketTeethLabel.setTextFill(Color.BLACK);
    }
    
    private void loadConfig() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Parser parser = new Parser(file.getAbsolutePath());
            maximumRPMField.setText(String.valueOf(parser.getMaxRPM()));
            minimumRPMField.setText(String.valueOf(parser.getMinRPM()));
            tireDiameterField.setText(String.valueOf(parser.getUserDefinedTireDiameter()));
            rimDiameterField.setText(String.valueOf(parser.getRimDiameter()));
            tireWidthField.setText(String.valueOf(parser.getTireWidth()));
            tireAspectRatioField.setText(String.valueOf(parser.getTireAspectRatio()));
            finalDriveRatioField.setText(String.valueOf(parser.getFinalDriveRatio()));
            frontSprocketTeethField.setText(String.valueOf(parser.getFrontSprocketTeeth()));
            rearSprocketTeethField.setText(String.valueOf(parser.getRearSprocketTeeth()));
            firstGearRatioField.setText(String.valueOf(parser.getGearRatios()[0]));
            secondGearRatioField.setText(String.valueOf(parser.getGearRatios()[1]));
            thirdGearRatioField.setText(String.valueOf(parser.getGearRatios()[2]));
            fourthGearRatioField.setText(String.valueOf(parser.getGearRatios()[3]));
            fifthGearRatioField.setText(String.valueOf(parser.getGearRatios()[4]));
            try {
                FileWriter lastUsedConfigFile = new FileWriter("configs/lastusedconfig.txt");
                lastUsedConfigFile.write(pathToLastConfigFile);
                lastUsedConfigFile.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        } 
    }
    
    private boolean getEnteredValues() {
        boolean errorsPresent = false;
        boolean userDefinedTireDiameterIsSet = false;
        
        //Update Maximum RPM
        if (maximumRPMField.getText().length() != 0) {
            calc.setMaxRPM(Long.parseLong(maximumRPMField.getText()));
        }
        else {
            maximumRPMLabel.setTextFill(Color.RED);
            errorsPresent = true;
        }
        
        //Update Minimum RPM
        if (minimumRPMField.getText().length() != 0) {
            calc.setMinRPM(Long.parseLong(minimumRPMField.getText()));
        }
        else {
            minimumRPMLabel.setTextFill(Color.RED);
            errorsPresent = true;
        }
        
        //Update Tire Diameter
        if (tireDiameterField.getText().length() != 0) {
            calc.setUserDefinedTireDiameter(Double.parseDouble(tireDiameterField.getText()));
            userDefinedTireDiameterIsSet = true;
        }
        else {
            calc.setUserDefinedTireDiameter(0.0);
            tireDiameterLabel.setTextFill(Color.RED);
            errorsPresent = true;
        }
        
        //Update Rim Diameter
        if (rimDiameterField.getText().length() != 0) {
            calc.setRimDiameter(Double.parseDouble(rimDiameterField.getText()));
        }
        else if (!userDefinedTireDiameterIsSet) {
            rimDiameterLabel.setTextFill(Color.RED);
        }
        
        //Update Tire Width
        if (tireWidthField.getText().length() != 0) {
            calc.setTireWidth(Long.parseLong(tireWidthField.getText()));
        }
        else if (!userDefinedTireDiameterIsSet) {
            tireWidthLabel.setTextFill(Color.RED);
        }
        
        //Update Tire Aspect Ratio
        if (tireAspectRatioField.getText().length() != 0) {
            calc.setTireAspectRatio(Long.parseLong(tireAspectRatioField.getText()));
        }
        else if (!userDefinedTireDiameterIsSet) {
            tireAspectRatioLabel.setTextFill(Color.RED);
        }
        
        //Update Final Drive Ratio
        if (finalDriveRatioField.getText().length() != 0) {
            calc.setFinalDriveRatio(Double.parseDouble(finalDriveRatioField.getText()));
        }
        else {
            finalDriveRatioLabel.setTextFill(Color.RED);
        }
        
        //Update Front Sprocket Teeth
        if (frontSprocketTeethField.getText().length() != 0) {
            calc.setFrontSprocketTeeth(Long.parseLong(frontSprocketTeethField.getText()));
        }
        else {
            frontSprocketTeethLabel.setTextFill(Color.RED);
        }
        
        //Update Rear Sprocket Teeth
        if (rearSprocketTeethField.getText().length() != 0) {
            calc.setRearSprocketTeeth(Long.parseLong(rearSprocketTeethField.getText()));
        }
        else {
            rearSprocketTeethLabel.setTextFill(Color.RED);
        }
        
        //Update First Gear Ratio
        if (firstGearRatioField != null && firstGearRatioField.getText().length() != 0) {
            calc.setGearRatio(0, Double.parseDouble(firstGearRatioField.getText()));
        }
        else if (firstGearRatioField != null) {
            calc.setGearRatio(0, 0.0);
        }
        
        //Update Second Gear Ratio
        if (secondGearRatioField != null && secondGearRatioField.getText().length() != 0) {
            calc.setGearRatio(1, Double.parseDouble(secondGearRatioField.getText()));
        }
        else if (secondGearRatioField != null) {
            calc.setGearRatio(1, 0.0);
        }
        
        //Update Third Gear Ratio
        if (thirdGearRatioField != null && thirdGearRatioField.getText().length() != 0) {
            calc.setGearRatio(2, Double.parseDouble(thirdGearRatioField.getText()));
        }
        else if (thirdGearRatioField != null) {
            calc.setGearRatio(2, 0.0);
        }
        
        //Update Fourth Gear Ratio
        if (fourthGearRatioField != null && fourthGearRatioField.getText().length() != 0) {
            calc.setGearRatio(3,  Double.parseDouble(fourthGearRatioField.getText()));
        }
        else if (fourthGearRatioField != null) {
            calc.setGearRatio(3, 0.0);
        }
        
        //Update Fifth Gear Ratio
        if (fifthGearRatioField != null && fifthGearRatioField.getText().length() != 0) {
            calc.setGearRatio(4,  Double.parseDouble(fifthGearRatioField.getText()));
        }
        else if (fifthGearRatioField != null) {
            calc.setGearRatio(4,  0.0);
        }
        
        //Update Sixth Gear Ratio
        if (sixthGearRatioField != null && sixthGearRatioField.getText().length() != 0) {
            calc.setGearRatio(5, Double.parseDouble(sixthGearRatioField.getText()));
        }
        else if (sixthGearRatioField != null) {
            calc.setGearRatio(5, 0.0);
        }
        return !errorsPresent;
    }
    
}

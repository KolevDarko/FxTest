package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
//import javafx.scene.image.Image;*************************************
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.sourceforge.plantuml.SourceStringReader;
import sample.Sequence.ActionFields;
import sample.Sequence.SequenceDiagram;
import sample.UseCase.Action;
import sample.UseCase.Actor;
import sample.UseCase.Case;
import sample.UseCase.UseCaseDiagram;

import java.awt.Image;
import java.awt.image.BufferedImage;


import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Sample application that shows examples of the different layout panes
 * provided by the JavaFX layout API.
 * The resulting UI is for demonstration purposes only and is not interactive.
 */


public class Main extends Application {
    private int sequenceDiagramsCount;
    private int x, y;
    int howMuch;
    private ArrayList<ArrayList<TextField>> allFieldsUseCase;
    private ArrayList<TextField> allActorsSequence;
    private List<TextField> newActions = new ArrayList<TextField>();
    private TextField ac1, ac2;
    private List<Integer> actionsCounter = new ArrayList<Integer>();
    private ComboBox numActions = new ComboBox();
    private ArrayList<Case> cases;
    private FlowPane rightPics;
    private GridPane gPane;
    private AnchorPane aPane;
    private BorderPane border;
    private Scene scene;
    private TextArea bigText;
    private List<ActionFields> sequenceActionsFields = new ArrayList<ActionFields>();
    private DatabaseOperations db;
    private GridPane grid;
    private ArrayList<Label> nounsLabels;
    private ArrayList<Label> verbsLabels;
    private TextField toCopyInto;
    private ArrayList<Label> allLabels;
    private GridPane gridLabels;

    public Main() {
        allFieldsUseCase = new ArrayList<ArrayList<TextField>>();
        allActorsSequence = new ArrayList<TextField>();
        sequenceDiagramsCount = 0;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(Main.class, args);
    }

    @Override
    public void start(final Stage stage) {


// Use a border pane as the root for scene
        border = new BorderPane();

        HBox hbox = addHBox();
        MenuBar menuBar = new MenuBar();
        Menu menuDiagram = new Menu("Diagram");
        MenuItem useCase = new MenuItem("Use Case");
        MenuItem activity = new MenuItem("Activity");
        MenuItem sequence = new MenuItem("Sequence");
        menuDiagram.getItems().addAll(useCase, activity, sequence);

        menuBar.getMenus().add(menuDiagram);
        border.setTop(menuBar);
        rightPics = addFlowPane();
        border.setRight(rightPics);

        useCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                allFieldsUseCase = new ArrayList<ArrayList<TextField>>();
                aPane = addAnchorPane(createBottomButtonsUseCase());
                refreshView(stage);
            }
        });

        sequence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                allActorsSequence = new ArrayList<TextField>();
                aPane = addAnchorPane(createBottomButtonsSequence());
                refreshView(stage);
            }
        });

        border.setCenter(aPane);

        scene = new Scene(border);
        border.setPrefWidth(600);
        border.setPrefHeight(400);
        stage.setScene(scene);
        stage.setTitle("Text to UML");

        stage.show();
        db = new DatabaseOperations("words.db");
    }


    private void createSequenceDiagram() {
        SequenceDiagram sequenceDiagram = new SequenceDiagram();

        sequenceDiagram.setActors(allActorsSequence);
        sequenceDiagram.setSequences(sequenceActionsFields);

        diagramToImage(sequenceDiagram.toString(), "sequence");

    ResourceBundle.clearCache();

    }


    private void refreshView(Stage stage){

        border.setCenter(aPane);
        stage.show();
    }

/*
 * Creates an HBox with two buttons for the top region
 */

    private HBox addHBox() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize(100, 20);

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize(100, 20);

        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }




    /*
     * Creates a horizontal flow pane with eight icons in four rows
     */
    private FlowPane addFlowPane() {

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(170); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        return flow;
    }

    private void addImageToFlowPane(FlowPane fPane, String name){
        Image image;
        try {
            image = ImageIO.read(new File("src/sample/diagrams/" + name + ".png"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            ImageView img = new ImageView(new javafx.scene.image.Image(in));

            fPane.getChildren().add(img);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Creates an anchor pane using the provided grid and an HBox with buttons
     *
     * @param grid Grid to anchor to the top of the anchor pane
     */
    private AnchorPane addAnchorPane(HBox hb) {
        final AnchorPane anchorpane = new AnchorPane();
        grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        x = y = 0;

        for (int i = 1; i < 10; i++) {
            numActions.getItems().add(i);
        }
        numActions.setValue((int) 1);

        y++;
        grid.add(numActions, x, y, 2, 1);

        y++;
        x = 0;

        bigText = new TextArea();
        bigText.setPrefRowCount(10);
        bigText.setPrefColumnCount(200);
        bigText.setWrapText(true);
        bigText.setPrefWidth(300);

        Button parseText = new Button("Parse text");
        Button editText = new Button("Edit text");
        gridLabels = new GridPane();

        editText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bigText.setVisible(true);
                gridLabels.setVisible(false);
            }
        });

        parseText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridLabels.setHgap(5.0);
                gridLabels.setVgap(5.0);
                nounsLabels = new ArrayList<Label>();
                verbsLabels = new ArrayList<Label>();
                allLabels = new ArrayList<Label>();
                gridLabels = new GridPane();
                Label wordLabel;
                int x,y;

                x=y=0;
                String[] words = splitTextIntoWords(bigText.getText());
                for(String word: words){
                    wordLabel = new Label(word);
                    allLabels.add(wordLabel);
                    if(db.searchNoun(word.toLowerCase())){
                        nounsLabels.add(wordLabel);
                    }
                    if(db.searchVerb(word.toLowerCase())){
                        verbsLabels.add(wordLabel);
                    }
                    gridLabels.add(wordLabel, y++, x);
                    if(y == 7) {
                        y = 0;
                        x++;
                    }
                }
                gridLabels.setGridLinesVisible(true);
                anchorpane.getChildren().add(gridLabels);
                AnchorPane.setTopAnchor(gridLabels, 5.0);
                bigText.setVisible(false);
                assignEventsToLabels();
            }
        });

        anchorpane.getChildren().addAll(grid, hb, bigText, parseText, editText);
        // Anchor buttons to bottom right, anchor grid to top
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 10.0);
        AnchorPane.setTopAnchor(grid, 220.0);
        AnchorPane.setTopAnchor(bigText, 5.0);
        AnchorPane.setTopAnchor(parseText, 200.0);
        AnchorPane.setTopAnchor(editText, 200.0);
        AnchorPane.setRightAnchor(parseText, 10.0);
        AnchorPane.setRightAnchor(editText, 50.0);


        return anchorpane;
    }

    private void assignEventsToLabels() {
        for(final Label lbl: allLabels){
               lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       if(toCopyInto != null) {
                           toCopyInto.setText(toCopyInto.getText() + " " + lbl.getText());
                       }
                   }
               });
           }
    }

    public void highlightVerbs(){
        for(Label lbl: verbsLabels){
            lbl.setStyle("-fx-background-color: forestgreen;");
        }
    }
    public void unHighlightVerbs(){
        for(Label lbl: verbsLabels){
            lbl.setStyle("");
        }
    }

    public void highlightNouns(){
        for(Label lbl: nounsLabels){
            lbl.setStyle("-fx-background-color: mediumpurple;");
        }
    }

    public void unHighlightNouns(){
        for(Label lbl: nounsLabels){
            lbl.setStyle("");
        }
    }

    public String[] splitTextIntoWords(String text) {
        String forbidden = ".,!?:;";
        String[] results = text.split(" ");
        for (int i = 0; i < results.length; i++) {
            String lastOne = String.valueOf(results[i].charAt(results[i].length()-1));
            if(forbidden.contains(lastOne.substring(0))){
                results[i] = results[i].substring(0,results[i].length()-1);
            }
        }
        return results;
    }

    private HBox createBottomButtonsUseCase(){
        Button buttonAdd = new Button("Add");
        Button buttonPrint = new Button("Print");
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<TextField> tempList = new ArrayList<TextField>();
                howMuch = Integer.parseInt(numActions.getValue().toString());

                ++y;
                x = 0;
                ac1 = new TextField();
                ac1.setPromptText("Actor1");
                ac2 = new TextField();
                ac2.setPromptText("Actor2");


                ac1.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue){
                            toCopyInto = ac1;
                            highlightNouns();
                        }else{
                            toCopyInto = null;
                            unHighlightNouns();
                        }
                    }
                });
                ac2.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue){
                            toCopyInto = ac2;
                            highlightNouns();
                        }else{
                            toCopyInto = null;
                            unHighlightNouns();
                        }
                    }
                });

                tempList.add(ac1);
                tempList.add(ac2);

                grid.add(ac1, x++, y);

                TextField act;

                for (int i = 0; i < howMuch; i++) {
                    act = new TextField();
                    act.setPromptText("Action");
                    final TextField finalAct = act;
                    act.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(newValue){
                                toCopyInto = finalAct;
                                highlightVerbs();
                            }else{
                                toCopyInto = null;
                                unHighlightVerbs();
                            }
                        }
                    });
                    tempList.add(act);
                    grid.add(act, x++, y);
//                    newActions.add(act);
                }
//                actionsCounter.add(howMuch);
                grid.add(ac2, x++, y);
                allFieldsUseCase.add(tempList);

            }
        });
        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createUseCaseDiagram();
                showImages(allFieldsUseCase.size(), "diagram");
            }
        });
        HBox hb = new HBox();

        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonAdd, buttonPrint);
        return hb;
    }

    private HBox createBottomButtonsSequence(){
        Button buttonAddActors = new Button("Add Actors");
        Button buttonAddAction = new Button("Add Action");
        Button buttonPrint = new Button("Print");
        buttonAddActors.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                howMuch = Integer.parseInt(numActions.getValue().toString());

                ++y;
                x = 0;

                TextField actor;
                for (int i = 0; i < howMuch; i++) {
                    actor = new TextField();
                    actor.setPromptText("Actor Name");
                    final TextField finalActor = actor;
                    actor.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            toCopyInto = finalActor;
                            highlightNouns();
                        }
                    });
                    actor.focusedProperty().addListener(new ChangeListener<Boolean>()
                    {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
                        {
                            if (newPropertyValue)
                            {
                                toCopyInto = finalActor;
                                highlightNouns();
                            }
                            else
                            {
                                toCopyInto = null;
                                unHighlightNouns();
                            }
                        }
                    });
                    allActorsSequence.add(actor);
                    grid.add(actor, x, y);
                    x+=2;
                }
            }
        });

        buttonAddAction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ComboBox actionType,left,right;
                left = new ComboBox();
                right = new ComboBox();
                actionType = new ComboBox();

                for (TextField actor: allActorsSequence) {
                    left.getItems().add(actor.getText());
                    right.getItems().add(actor.getText());
                }

                actionType.getItems().add("->");
                actionType.getItems().add("-->");
                actionType.getItems().add("->>");

                final TextField actionText = new TextField();
                actionText.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue){
                            toCopyInto = actionText;
                            highlightVerbs();
                        }else{
                            toCopyInto = null;
                            unHighlightVerbs();
                        }
                    }
                });

                x=0;
                y++;
                grid.add(left, x++, y);
                grid.add(actionType, x++, y);
                grid.add(actionText, x++, y);
                grid.add(right, x++, y);

                sequenceActionsFields.add(new ActionFields(actionText, left, right, actionType));


            }
        });



        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createSequenceDiagram();
                addImageToFlowPane(rightPics, "sequence");
            }
        });

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonAddActors, buttonAddAction, buttonPrint);
        return hb;
    }

    private void showImages(int numDiagrams, String name) {
        for(int i=0;i < numDiagrams; i++)
        addImageToFlowPane(this.rightPics, name + Integer.toString(i));

    }

    private void createUseCaseDiagram() {
     cases = new ArrayList<Case>();
     int prefix = 0;
     TextField ac1, ac2;
     Actor a1, a2;
     ArrayList<Action> actions;

        // pomini go sekoj red na TextFields
        for (int j = 0; j < allFieldsUseCase.size(); j++) {
            ArrayList<TextField> iterList = allFieldsUseCase.get(j);

            actions = new ArrayList<Action>();
            prefix++;
            ac1 = iterList.get(0);
            ac2 = iterList.get(1);

            prefix = allFieldsUseCase.indexOf(iterList);
            a1 = new Actor(ac1.getText(), "actor1" + prefix);
            a2 = new Actor(ac2.getText(), "actor2" + prefix);


            String report = ac1.getText() + " : ";
            for (int i = 2; i < iterList.size(); i++) {
                Action a = new Action(iterList.get(i).getText(), "action" + prefix + i);
                actions.add(a);
            }
            Case c = new Case(a1, a2, actions);
            UseCaseDiagram diagram = new UseCaseDiagram("Diagram number " + j, c);
            String namePostfix = "diagram" + Integer.toString(j);
            diagramToImage(diagram.toString(), namePostfix);
        }

        ResourceBundle.clearCache();
//        System.out.println(diagram);
    }

    private boolean diagramToImage(String source, String name) {
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(source);
        try {
// Write the first image to "png"
            String desc = reader.generateImage(png);

// Return a null string if no generation
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in writing to png");
        }

        byte[] data = png.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        try {
            BufferedImage image = ImageIO.read(input);

            if(ImageIO.write(image, "png", new File("src/sample/diagrams/" + name + ".png"))){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    private void setToCopyInto(String text){
        toCopyInto.setText(text);
    }

}

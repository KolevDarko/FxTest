package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import sample.Sequence.ActionFlow;
import sample.Sequence.SequenceDiagram;
import sample.UseCase.Action;
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
    private ArrayList<UseCaseDiagram> useCaseDiagrams;
    private SequenceDiagram sequenceDiagram;
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
    private List<ActionFlow> sequenceActionsFields = new ArrayList<ActionFlow>();
    private DatabaseOperations db;
    private GridPane grid;
    private ArrayList<Label> nounsLabels;
    private ArrayList<Label> verbsLabels;
    private TextField toCopyInto;
    private ArrayList<Label> allLabels;
    private GridPane gridLabels;
    private ScrollPane sp, sp2;
    private AnchorPane anchorpane;

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
        sp2 = new ScrollPane();
        sp2.setContent(rightPics);
        useCase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                allFieldsUseCase = new ArrayList<ArrayList<TextField>>();

                aPane = addAnchorPane(createBottomButtonsUseCase());
                sp = new ScrollPane();
                sp.setContent(aPane);
                refreshView(stage);
            }
        });

        sequence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                allActorsSequence = new ArrayList<>();

                aPane = addAnchorPane(createBottomButtonsSequence());
                sp = new ScrollPane();
                sp.setContent(aPane);
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
//        sequenceDiagram.setActors(allActorsSequence);
//        sequenceDiagram.setSequences(sequenceActionsFields);

        diagramToImage(sequenceDiagram.toString(), "sequence");

    ResourceBundle.clearCache();

    }


    private void refreshView(Stage stage){
        border.setCenter(sp);
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
    private AnchorPane addAnchorPane(Button[] buttons) {
        anchorpane = new AnchorPane();
        Button hb, parseText, editText;
        hb = buttons[0];
        editText = buttons[1];
        parseText = buttons[2];


        editText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gridLabels.setVisible(false);
                bigText.setVisible(true);
            }
        });

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        x = y = 0;

        bigText = new TextArea();
        bigText.setPrefRowCount(10);
        bigText.setPrefColumnCount(300);
        bigText.setWrapText(true);
        bigText.setPrefWidth(500);



        anchorpane.getChildren().addAll(grid, bigText, hb, parseText, editText);
        AnchorPane.setTopAnchor(grid, 220.0);
        AnchorPane.setTopAnchor(bigText, 5.0);
        AnchorPane.setLeftAnchor(bigText, 20.0);

        AnchorPane.setTopAnchor(parseText, 170.);
        AnchorPane.setTopAnchor(editText, 170.0);
        AnchorPane.setTopAnchor(hb, 170.0);

        AnchorPane.setRightAnchor(parseText, 50.0);
        AnchorPane.setRightAnchor(editText, 150.0);
        AnchorPane.setRightAnchor(hb, 250.0);


        return anchorpane;
    }

    private void addActorToGrid(final TextField actorField, int i, int j) {
        grid.add(actorField, i, j);
        actorField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    toCopyInto = actorField;
                    highlightNouns();
                }else{
                    unHighlightNouns();
                }
            }
        });
    }

    private void addActionToGrid(final TextField actionField, int i, int j) {
        grid.add(actionField, i, j);
        actionField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    toCopyInto = actionField;
                    highlightVerbs();
                }else{
                    unHighlightVerbs();
                }
            }
        });
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

    private Button[] createBottomButtonsUseCase(){
        Button[] buttons = new Button[3];
        Button buttonPrint = new Button("Print");

        Button parseText = new Button("Parse text");
        Button editText = new Button("Edit text");
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
                IntelligentDiagram diag = new IntelligentDiagram(bigText.getText());
                diag.extractDataUseCase();
                useCaseDiagrams = diag.useCaseDiagrams;

                gridLabels = new GridPane();
                gridLabels.setHgap(20);
                gridLabels.setVgap(10);
                gridLabels.setAlignment(Pos.CENTER);
                nounsLabels = new ArrayList<>();
                verbsLabels = new ArrayList<>();
                allLabels = new ArrayList<>();

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
                anchorpane.getChildren().add(gridLabels);
                AnchorPane.setTopAnchor(gridLabels, 15.0);
                AnchorPane.setLeftAnchor(gridLabels, 30.0);
                bigText.setVisible(false);
                assignEventsToLabels();
                x=y=0;
                for(UseCaseDiagram diagram: useCaseDiagrams){
                    for(Case c: diagram.cases){
                        y=0;
                        grid.add(c.caseBox,  y++,x);

                        grid.add(new Separator(), y++, x);

                        grid.add(c.leftA.actorBox, y++, x);
                        addActorToGrid(c.leftA.actorField, y++, x);
                        for(Action a: c.actionsL){
                            grid.add(new Label("-->"), y++, x);
                            grid.add(a.actionBox, y++, x);
                            addActionToGrid(a.actionField, y++, x);
                        }

                        grid.add(new Label("-->"), y++, x);
                        grid.add(c.rightA.actorBox, y++, x);
                        addActorToGrid(c.rightA.actorField, y, x++);
                        y=1;
                        grid.add(new Label("Use Case Number: " + useCaseDiagrams.indexOf(diagram)), y, x++, 4, 1);
                    }
                }
            }
        });

        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createUseCaseDiagram();
                showImages(useCaseDiagrams.size(), "diagram");
            }
        });
        buttons[0] = buttonPrint;
        buttons[1] = editText;
        buttons[2] = parseText;
        return buttons;
    }

    private Button[] createBottomButtonsSequence(){
        Button[] buttons = new Button[3];
        Button editText = new Button("Edit Text");
        Button parseText = new Button("Parse Text");
        Button buttonPrint = new Button("Print");
        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createSequenceDiagram();
                addImageToFlowPane(rightPics, "sequence");
            }
        });
        buttons[0] = buttonPrint;
        //todo implement
        buttons[1] = editText;
        buttons[2] = parseText;



        parseText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IntelligentDiagram diag = new IntelligentDiagram(bigText.getText());
                sequenceDiagram = diag.extractDataSequence();

                gridLabels = new GridPane();
                gridLabels.setHgap(20);
                gridLabels.setVgap(10);
                gridLabels.setAlignment(Pos.CENTER);
                nounsLabels = new ArrayList<>();
                verbsLabels = new ArrayList<>();
                allLabels = new ArrayList<>();

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
                anchorpane.getChildren().add(gridLabels);
                AnchorPane.setTopAnchor(gridLabels, 15.0);
                AnchorPane.setLeftAnchor(gridLabels, 30.0);
                bigText.setVisible(false);
                assignEventsToLabels();
                grid.add(new Label("Sequence diagram"), 2, 0, 4, 1);
                x=1;
                for(ActionFlow seq: sequenceDiagram.sequences){
                    y=0;
                    grid.add(seq.actionBox, y++, x);
                    grid.add(new Separator(), y++, x);

                    addActorToGrid(seq.aLeft, y++, x);
                    grid.add(new Label("-->"), y++, x);
                    addActionToGrid(seq.ActionText, y++, x);
                    grid.add(new Label("-->"), y++, x);
                    addActorToGrid(seq.aRight, y++, x++);
                }
            }
        });

        return buttons;
    }

    private void showImages(int numDiagrams, String name) {
        for(int i=0;i < numDiagrams; i++)
        addImageToFlowPane(this.rightPics, name + Integer.toString(i));

    }

    private void createUseCaseDiagram() {


   /*  cases = new ArrayList<Case>();
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
            a1 = new Actor(ac1.getText());
            a2 = new Actor(ac2.getText());


            String report = ac1.getText() + " : ";
            for (int i = 2; i < iterList.size(); i++) {
                Action a = new Action(iterList.get(i).getText());
                actions.add(a);
            }
            Case c = new Case(a1, a2, actions);
            UseCaseDiagram diagram = new UseCaseDiagram("Diagram number " + j, c);
            String namePostfix = "diagram" + Integer.toString(j);
            diagramToImage(diagram.toString(), namePostfix);
        }*/
//        int num=0;
        for(int i=0;i < useCaseDiagrams.size(); i++){
            UseCaseDiagram diagram = useCaseDiagrams.get(i);
            System.out.println(diagram.toString());
            diagramToImage(diagram.toString(), "diagram" + i);
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

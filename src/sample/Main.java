package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sourceforge.plantuml.SourceStringReader;
import sample.Sequence.ActionFields;
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
    final ScrollBar sc = new ScrollBar();
    private int actionCount;
    private int x, y;
    int howMuch;
    private ArrayList<ArrayList<TextField>> allFieldsUseCase, allFieldsSequence;
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
    private List<ActionFields> sequenceActions = new ArrayList<ActionFields>();

    public Main() {
        allFieldsUseCase = new ArrayList<ArrayList<TextField>>();
        allFieldsSequence = new ArrayList<ArrayList<TextField>>();
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
                gPane = addMyGridPaneUseCase();
                aPane = addAnchorPaneUseCase(gPane);
                refreshView(stage);
            }
        });

        sequence.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                allFieldsSequence = new ArrayList<ArrayList<TextField>>();
                gPane = addMyGridPaneSequence();
                aPane = addAnchorPaneSequence(gPane);
                refreshView(stage);
            }
        });

        border.setCenter(aPane);

        scene = new Scene(border);

        stage.setScene(scene);
        stage.setTitle("Text to UML");

        stage.show();
    }


    private AnchorPane addAnchorPaneSequence(final GridPane grid) {
        numActions.setValue((int) 1);
        AnchorPane anchorpane = new AnchorPane();

        Button buttonAddActors = new Button("Add Actors");
        Button buttonAddAction = new Button("Add Action");
        Button buttonPrint = new Button("Print");
        buttonAddActors.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<TextField> tempList = new ArrayList<TextField>();
                howMuch = Integer.parseInt(numActions.getValue().toString());

                ++y;
                x = 0;

                TextField actor;
                for (int i = 0; i < howMuch; i++) {
                    actor = new TextField();
                    actor.setPromptText("Actor Name");
                    tempList.add(actor);
                    grid.add(actor, x, y);
                    x+=2;
                }
                allFieldsSequence.add(tempList);

            }
        });

        buttonAddAction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<TextField> tempList = tempList = allFieldsSequence.get(0);

                ComboBox actionType,left,right;
                left = new ComboBox();
                right = new ComboBox();
                actionType = new ComboBox();

                for (TextField actor: tempList) {
                    left.getItems().add(actor.getText());
                    right.getItems().add(actor.getText());
                }
                actionType.getItems().add("->");
                actionType.getItems().add("-->");
                actionType.getItems().add("->>");

                TextField actionText = new TextField();
                x=0;
                y++;
                grid.add(left, x++, y);
                grid.add(actionType, x++, y);
                grid.add(actionText, x++, y);
                grid.add(right, x++, y);

                sequenceActions.add(new ActionFields(actionText, left, right, actionType));


            }
        });



        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                createUseCaseDiagram();
//                showImages(allFieldsUseCase.size());
            }
        });

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonAddActors, buttonAddAction, buttonPrint);

        anchorpane.getChildren().addAll(grid, hb);
        // Anchor buttons to bottom right, anchor grid to top
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        return anchorpane;
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
     * Uses a stack pane to create a help icon and adds it to the right side of an HBox
     *
     * @param hb HBox to add the stack to
     */
    private void addStackPane(HBox hb) {

        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                        new Stop(0, Color.web("#4977A3")),
                        new Stop(0.5, Color.web("#B0C6DA")),
                        new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);
        // Add offset to right for question mark to compensate for RIGHT
        // alignment of all nodes
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));

        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);

    }

    /*
     * Creates a grid for the center region with four columns and three rows
     */


    private GridPane addMyGridPaneUseCase() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        x = y = 0;

        TextArea bigText = new TextArea();
        bigText.setPrefRowCount(10);
        bigText.setPrefColumnCount(200);
        bigText.setWrapText(false);
        bigText.setPrefWidth(300);
        GridPane.setHalignment(bigText, HPos.CENTER);
        grid.add(bigText, x, y, 3, 1);

//        populating combo box with number of actions
        for (int i = 1; i < 10; i++) {
            numActions.getItems().add(i);

        }

        y++;
        grid.add(numActions, x, y, 2, 1);

        y++;
        x = 0;

//        grid.setGridLinesVisible(true);
        return grid;
    }

    private GridPane addMyGridPaneSequence() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        x = y = 0;

        bigText = new TextArea();
        bigText.setPrefRowCount(10);
        bigText.setPrefColumnCount(200);
        bigText.setWrapText(false);
        bigText.setPrefWidth(300);
        GridPane.setHalignment(bigText, HPos.CENTER);
        grid.add(bigText, x, y, 3, 1);

//        populating combo box with number of actions
        for (int i = 1; i < 10; i++) {
            numActions.getItems().add(i);

        }

        y++;
        grid.add(numActions, x, y, 2, 1);
        y++;
        x = 0;

//        grid.setGridLinesVisible(true);
        return grid;

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

    private void addImageToFlowPane(FlowPane fPane, String num){
        Image image;
        try {
            image = ImageIO.read(new File("src/sample/diagrams/diagram" + num + ".png"));
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
     * Creates a horizontal (default) tile pane with eight icons in four rows
     */
    private TilePane addTilePane() {

        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefColumns(2);
        tile.setStyle("-fx-background-color: DAE6F3;");

        ImageView pages[] = new ImageView[8];
//        for (int i = 0; i < 8; i++) {
//            pages[i] = new ImageView(
//                    new Image(Main.class.getResourceAsStream(
//                            "graphics/chart_" + (i + 1) + ".png")));
//            tile.getChildren().add(pages[i]);
//        }

        return tile;
    }

    /*
     * Creates an anchor pane using the provided grid and an HBox with buttons
     *
     * @param grid Grid to anchor to the top of the anchor pane
     */
    private AnchorPane addAnchorPaneUseCase(final GridPane grid) {
        numActions.setValue((int) 1);
        AnchorPane anchorpane = new AnchorPane();

        Button buttonAdd = new Button("Add");
        Button buttonPrint = new Button("Print");
        //todo extract action, actors values from fields
        // create actor1, action .... , actor2
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

                tempList.add(ac1);
                tempList.add(ac2);

                grid.add(ac1, x++, y);

                TextField act;
                for (int i = 0; i < howMuch; i++) {
                    act = new TextField();
                    act.setPromptText("Action");
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
                showImages(allFieldsUseCase.size());
            }
        });

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonAdd, buttonPrint);

        anchorpane.getChildren().addAll(grid, hb);
        // Anchor buttons to bottom right, anchor grid to top
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        return anchorpane;
    }

    private void showImages(int numDiagrams) {
        for(int i=0;i < numDiagrams; i++)
        addImageToFlowPane(this.rightPics, Integer.toString(i));

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
//                report += " --> " + iterList.get(i).getText();
            }
            Case c = new Case(a1, a2, actions);
            UseCaseDiagram diagram = new UseCaseDiagram("Diagram number " + j, c);
            String namePostfix = Integer.toString(j);
            diagramToImage(diagram.toString(), namePostfix);


//            cases.add(c);
//            report += ac2.getText();
//            System.out.println(report);
        }
//        UseCaseDiagram diagram = new UseCaseDiagram("Prv Fraerski Diagram", cases);

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

            if(ImageIO.write(image, "png", new File("src/sample/diagrams/diagram" + name + ".png"))){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    private void addActionBox() {
        HBox hb = new HBox();
        hb.getChildren().add(new Text("Zdravooo"));

    }
    //TODO create scrollbar
    //todo import plantuml
    //todo import earlier classes for drawing


}

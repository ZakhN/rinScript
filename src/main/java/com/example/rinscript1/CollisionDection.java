package com.example.rinscript1;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CollisionDection extends Application {
    final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();
    Group gp = new Group();
    ArrayList<StackPane> stackPanes = new ArrayList<>();

    Scopes scopes = new Scopes();

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
//        Scene scene = new Scene(root);

        Scene sc = new Scene(gp, 700, 300);

        Button createVarDefBtn = new Button();
        createVarDefBtn.setText("create var def");

        Button createMathOperationBtn = new Button();
        createMathOperationBtn.setText("create math operation");

        Button createFunction = new Button();
        createFunction.setText("create function");

        GridPane btnGrid = new GridPane();
        btnGrid.addRow(1, createVarDefBtn, createMathOperationBtn, createFunction);

        setOnCreateMathOperation(createMathOperationBtn);
        setOnCreateVarDef(createVarDefBtn);
        setOnCreateFuction(createFunction);

        gp.getChildren().add(btnGrid);

        stage.setScene(sc);
        stage.show();
    }

    private void setOnCreateVarDef(Button createVarDefBtn) {
        createVarDefBtn.setOnAction(event -> {
            Rectangle varDef = createVarDef();

            StackPane stack = new StackPane();
            StackPane.setMargin(varDef, new Insets(8, 8, 8, 8));
            stack.getChildren().addAll(varDef);

            Label varDefId = new Label(" Var def id: ");
            Label varDefType = new Label(" Var def type: ");
            Label varDefValue = new Label(" Var def value: ");

            ChoiceBox<String> dropdown = new ChoiceBox<>();
            for (var val: Types.values()) {
                dropdown.getItems().add(val.toString());
            }

            TextField newtf1 = new TextField();
            TextField newtf2 = new TextField();
            GridPane grid = new GridPane();
            grid.addRow(1, varDefId, newtf1);
            grid.addRow(2, varDefValue, newtf2);
            grid.addRow(3, varDefType, dropdown);

            stack.getChildren().add(grid);

            StackPane.setMargin(grid, new Insets(10, 10, 10, 10));

            makeDraggable(stack);
            addToRectCollection(stack);

            gp.getChildren().add(stack);
        });
    }

    private void setOnCreateMathOperation(Button createMathOperationBtn) {
        createMathOperationBtn.setOnAction(event -> {
            var mathOperation = createMathOperation();

            StackPane stack1 = new StackPane();
            StackPane.setMargin(mathOperation, new Insets(8, 8, 8, 8));
            stack1.getChildren().add(mathOperation);

            makeDraggable(stack1);
            addToRectCollection(stack1);

            gp.getChildren().add(stack1);
        });
    }

    private void setOnCreateFuction(Button createFunction) {
        createFunction.setOnAction(event -> {
            var function = createFunction();

            StackPane stack1 = new StackPane();
            StackPane.setMargin(function, new Insets(8, 8, 8, 8));
            stack1.getChildren().add(function);

            makeDraggable(stack1);
            addToRectCollection(stack1);

            gp.getChildren().add(stack1);
        });
    }

    private void addToRectCollection(StackPane r) {
        stackPanes.add(r);
        r.setOnMouseDragged((MouseEvent event) -> {
            double deltaX = event.getSceneX() - mousePosition.get().getX();
            double deltaY = event.getSceneY() - mousePosition.get().getY();
            r.setLayoutX(r.getLayoutX() + deltaX);
            r.setLayoutY(r.getLayoutY() + deltaY);
            mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
            //Test collision for this pane with all other ones
            testCollision(r, deltaX, deltaY);
        });
    }

    private void testCollision(StackPane r, double deltaX, double deltaY) {
        //Loop through all stackPanes
        for (StackPane r2 : stackPanes) {
            if (r2 == r) //except r
                continue;
            //test intersection with r
            var intersect = r.getBoundsInParent().intersects(r2.getBoundsInParent());
            if (intersect) {
                //Style intersecting pane
                r2.setStyle("-fx-background-color: #6a5246");
                r.setStyle("-fx-background-color: #6a5246");


//                Group g = new Group();
//                g.getChildren().add(r);
//                g.getChildren().add(r2);
//                gp.getChildren().add(g);

                r.setLayoutX(r.getLayoutX() - deltaX);
                r.setLayoutY(r.getLayoutY() - deltaY);

                var node1 =  r.getChildren().get(0);
                var node2 =  r2.getChildren().get(0);

                if (node1.getClass().getName().equals("com.example.rinscript1.VarDef") && node2.getClass().getName().equals("com.example.rinscript1.Function")) {
                    var varDef = (VarDef) node1;
                    var func = (Function) node2;

                    scopes.varDefs.put(func.getName(), varDef);
                } else if (node1.getClass().getName().equals("com.example.rinscript1.Function") && node2.getClass().getName().equals("com.example.rinscript1.VarDef")) {
                    var func = (Function) node1;
                    var varDef = (VarDef) node2;

                    scopes.varDefs.put(func.getName(), varDef);
                }


            } else {
                //Unstyle
                r2.setStyle("-fx-background-color: #ffffff");
                r.setStyle("-fx-background-color: #ffffff");
            }
        }
    }

    private void makeDraggable(StackPane ractangleToSetDragged) {
        ractangleToSetDragged.setOnMousePressed((MouseEvent event) -> mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY())));

        ractangleToSetDragged.setOnMouseDragged((MouseEvent event) -> {
            double deltaX = event.getSceneX() - mousePosition.get().getX();
            double deltaY = event.getSceneY() - mousePosition.get().getY();
            ractangleToSetDragged.setLayoutX(ractangleToSetDragged.getLayoutX() + deltaX);
            ractangleToSetDragged.setLayoutY(ractangleToSetDragged.getLayoutY() + deltaY);
            mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
        });
    }

    public VarDef createVarDef() {
        VarDef varDef = new VarDef();
        varDef.setFill(Color.RED);

        varDef.setX(0);
        varDef.setY(60);
        varDef.setWidth(250);
        varDef.setHeight(100);

        varDef.setVarDefName("var def test");

        return varDef;
    }

    public MathOperation createMathOperation() {
        MathOperation rect = (MathOperation)createRect(0, 100);
        rect.setFill(Color.BLUE);
        return rect;
    }

    public Function createFunction() {
        Function function = new Function();
        function.setFill(Color.GREEN);

        function.setX(0);
        function.setY(60);
        function.setWidth(300);
        function.setHeight(100);

        function.setName("test");

        return function;
    }

    public Rectangle createRect(int x, int width) {
        var rect = new Rectangle();
        rect.setX(x);
        rect.setY(60);
        rect.setWidth(width);
        rect.setHeight(100);

        return rect;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
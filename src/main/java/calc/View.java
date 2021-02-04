package calc;

import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class View {

    private final Stage primaryStage;

    /**
     * The TextField for the input.
     */
    private final TextField inputTextField;

    /**
     * "Berechnen" Button
     */
    private final Button calculateButton;

    /**
     * Label, that shows the input as it is.
     */
    private final Label rawInputLabel;

    /**
     * Label, that shows either the parsed and serialized input or the CalcException
     * message.
     */
    private final Label serializedInputLabel;

    /**
     * Label, that shows either the parsed and evaluated input or the CalcException
     * message.
     */
    private final Label outputLabel;

    /**
     * Table and its columns for the current variables.
     */
    private TableView<Map.Entry<String, Integer>> variablesTableView;
    private TableColumn<Map.Entry<String, Integer>, String> nameCol;
    private TableColumn<Map.Entry<String, Integer>, Integer> valCol;

    /**
     * History of all input. First is newest.
     */
    private final ListView<String> historyListView;

    private Model model;

    /**
     * Create a new View.
     * 
     * @param primaryStage
     */
    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SWEng - U12 - Niklas Naumann, Jonas Havemann");

        // Initialize all Elements
        inputTextField = new TextField();
        calculateButton = new Button("Berechnen");
        rawInputLabel = new Label("");
        serializedInputLabel = new Label("");
        outputLabel = new Label("");

        variablesTableView = new TableView<>();
        variablesTableView.setEditable(false);
        // CONSTRAINED_RESIZE_POLICY has the consequence that only 2 columns are shown.
        variablesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Specify, how the columns are updated later.
        nameCol = new TableColumn<Map.Entry<String, Integer>, String>("Name");
        nameCol.setSortable(false);
        nameCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Entry<String, Integer>, String> param) {
                        return new SimpleStringProperty(param.getValue().getKey());
                    }
                });
        valCol = new TableColumn<Map.Entry<String, Integer>, Integer>("Wert");
        valCol.setSortable(false);
        valCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Entry<String, Integer>, Integer>, ObservableValue<Integer>>() {

                    @Override
                    public ObservableValue<Integer> call(CellDataFeatures<Entry<String, Integer>, Integer> param) {
                        return new SimpleIntegerProperty(param.getValue().getValue()).asObject();
                    }

                });

        variablesTableView.getColumns().add(nameCol);
        variablesTableView.getColumns().add(valCol);

        historyListView = new ListView<>();

        BorderPane root = new BorderPane();

        HBox topBox = new HBox();
        HBox midBox = new HBox();
        HBox botBox = new HBox();

        VBox botLeftBox = new VBox();
        VBox botRightBox = new VBox();

        topBox.getChildren().addAll(this.inputTextField, this.calculateButton);

        VBox midLeft = new VBox();
        VBox midRight = new VBox();

        Text input = new Text("Eingabe:");
        Text serialized = new Text("Formatiert:");
        Text output = new Text("Ausgabe:");

        midLeft.getChildren().addAll(input, serialized, output);
        midLeft.setPadding(new Insets(0, 2, 0, 0));
        midRight.getChildren().addAll(this.rawInputLabel, this.serializedInputLabel, this.outputLabel);

        midBox.getChildren().addAll(midLeft, midRight);

        botLeftBox.getChildren().addAll(new Label("Variablen"), this.variablesTableView);
        botLeftBox.setPadding(new Insets(0, 2, 0, 0));
        botRightBox.getChildren().addAll(new Label("Historie"), this.historyListView);
        botRightBox.setPadding(new Insets(0, 0, 0, 2));

        botBox.getChildren().addAll(botLeftBox, botRightBox);

        VBox topWithSeparator = new VBox();
        VBox midWithSeparator = new VBox();

        Separator s1 = new Separator(Orientation.HORIZONTAL);
        Separator s2 = new Separator(Orientation.HORIZONTAL);
        Insets topBotInset = new Insets(5, 0, 5, 0);
        s1.setPadding(topBotInset);
        s2.setPadding(topBotInset);

        topWithSeparator.getChildren().addAll(topBox, s1);
        midWithSeparator.getChildren().addAll(midBox, s2);

        root.setTop(topWithSeparator);
        root.setCenter(midWithSeparator);
        root.setBottom(botBox);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void update(Model model) {
        this.rawInputLabel.setText(this.model.getInput());
        this.model.getExceptionMessage().ifPresentOrElse(exceptionMessage -> {
            if (this.model.getParserThrewExceptionMessage()) {
                this.serializedInputLabel.setText(exceptionMessage);
                this.outputLabel.setText("");
            } else {
                this.serializedInputLabel.setText(this.model.getSerializedExpression());
                this.outputLabel.setText(exceptionMessage);
            }
            this.historyListView.getItems().add(0, this.model.getInput());
        }, () -> {
            this.serializedInputLabel.setText(this.model.getSerializedExpression());
            this.outputLabel.setText(String.valueOf(this.model.getEvaluatedExpression()));
            this.model.getParsedVariable().ifPresentOrElse((v -> this.historyListView.getItems().add(0, v)),
                    () -> historyListView.getItems().add(0, this.model.getSerializedExpression()));
            ObservableList<Map.Entry<String, Integer>> items = FXCollections
                    .observableArrayList(this.model.getVariables().entrySet());
            variablesTableView.setItems(items);
        });
    }

    public Button getCalculateButton() {
        return this.calculateButton;
    }

    public String getInputTextField() {
        return this.inputTextField.getText();
    }

    public void setModel(Model model) {
        this.model = model;
        model.attach(this);
    }
}

package pkj;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
public final class DictionaryPane extends Pane
{
    private Button insertBtn;
    private Button searchBtn;
    private Button deleteBtn;
    private Label sizeLabel;
    private Label heightLabel;
    private Label heightresLabel;
    private Label sizeResLabel;
    private TextField txtField;
    private HBox upperHbox;
    private StackPane middlebox;
    private HBox lowerHbox;
    private BorderPane container;
    private ListView<RedBlackTree<String>.Node<String>> words;
    private ObservableList<RedBlackTree<String>.Node<String>> wordsList;

    {
        this.insertBtn = new Button("Insert");
        this.insertBtn.setTooltip(new Tooltip("will Insert the user input"));
        this.insertBtn.setOnAction( e -> this.insertAction() );


        this.searchBtn = new Button("Search");
        this.searchBtn.setTooltip(new Tooltip("Will search for user given input"));
        this.searchBtn.setOnAction( e -> this.searchAction() );
        


        this.deleteBtn = new Button("Delete");
        this.deleteBtn.setTooltip(new Tooltip("Will delete user given input if exists"));
        this.deleteBtn.setOnAction( e -> this.deleteAction() );

        this.sizeLabel = new Label("No. of Words: ");
        this.sizeLabel.setTooltip(new Tooltip("Represents the total number of words loaded"));
        this.sizeLabel.requestFocus();

        this.sizeResLabel = new Label(Integer.toString(Client.getInstance().getDictionarySize()));
        this.sizeLabel.setGraphic(sizeResLabel);
        this.sizeLabel.setContentDisplay(ContentDisplay.RIGHT);
        
        this.heightLabel = new Label("Height of the tree: ");
        this.heightLabel.setTooltip(new Tooltip("Represents the height of the Tree"));
        this.heightLabel.requestFocus();
        
        this.heightresLabel = new Label(Integer.toString(Client.getInstance().getDictionaryheight()));
        this.heightLabel.setGraphic(heightresLabel);
        this.heightLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.heightLabel.setAlignment(Pos.CENTER_RIGHT);

        this.txtField = new TextField();
        this.txtField.setTooltip(new Tooltip("A text field for input of words"));
        this.txtField.setPromptText("Input word for search, insertion or deletion");
        this.txtField.setPrefWidth(300);
        this.txtField.setFocusTraversable(false);
        this.txtField.setOnMouseClicked(e ->  textAction() );


        this.wordsList = FXCollections.observableArrayList(Client.getInstance().entryView());
		this.words = new ListView<>(wordsList);
        this.words.setOnMouseClicked( e -> selectionAction() );
        words.setOrientation(Orientation.VERTICAL);
        words.setPrefSize(120, 100);

        this.upperHbox = new HBox();
        this.middlebox = new StackPane();
        this.lowerHbox = new HBox();
        this.container = new BorderPane();
        middlebox.getChildren().add(words);


        this.deleteBtn.disableProperty().bind(Bindings.isEmpty(txtField.textProperty()).and(this.words.getSelectionModel().selectedItemProperty().isNull()));
        this.searchBtn.disableProperty().bind(Bindings.isEmpty(txtField.textProperty()));
        this.insertBtn.disableProperty().bind(Bindings.isEmpty(txtField.textProperty()));
    }

    public DictionaryPane()
    {

        upperHbox.setPadding(new Insets(30));
        upperHbox.getChildren().addAll(txtField,sizeLabel);
        upperHbox.setSpacing(20);
        upperHbox.setAlignment(Pos.CENTER);
        



        lowerHbox.getChildren().addAll(searchBtn, insertBtn, deleteBtn,heightLabel);
        lowerHbox.setAlignment(Pos.CENTER);
        lowerHbox.setSpacing(20);
        lowerHbox.setPadding(new Insets(30));


        container.setTop(upperHbox);
        container.setCenter(middlebox);
        container.setBottom(lowerHbox);


    }

    private void selectionAction()
    {
        this.txtField.setText("");
    }

    private void textAction()
    {
        this.words.getSelectionModel().clearSelection();
    }

    private void insertAction()
    {
        boolean operation = Client.getInstance().insertWord(txtField.getText().trim());
        if(operation)
        {
            this.words.getItems().clear();
            this.words.getItems().addAll(Client.getInstance().entryView());
            this.sizeResLabel.setText(Integer.toString(Client.getInstance().getDictionarySize()));
            this.heightresLabel.setText(Integer.toString(Client.getInstance().getDictionaryheight()));
            new Alert(Alert.AlertType.INFORMATION, "Insertion Successful").showAndWait();
        }else
            new Alert(Alert.AlertType.ERROR, "Item Already Exists, Insertion Failed").showAndWait();
    }

    private void searchAction()
    {
        if(Client.getInstance().lookUpWord(txtField.getText().trim()))
            new Alert(Alert.AlertType.INFORMATION, "Exists").showAndWait();
        else
            new Alert(Alert.AlertType.ERROR, "Doesn't Exist").showAndWait();

    }

    private void deleteAction()
    {
        boolean operation = txtField.getText().isEmpty() ?
                            Client.getInstance().removeWord(this.words.getSelectionModel().getSelectedItem().toString().trim().split("\\s+")[0].toLowerCase())
                            :Client.getInstance().removeWord(txtField.getText().trim().toLowerCase());
        if(operation)
        {
            this.words.getItems().clear();
            this.words.getItems().addAll(Client.getInstance().entryView());
            this.sizeResLabel.setText(Integer.toString(Client.getInstance().getDictionarySize()));
            this.heightresLabel.setText(Integer.toString(Client.getInstance().getDictionaryheight()));
            new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully").showAndWait();
        }else
            new Alert(Alert.AlertType.ERROR, "Item Doesn't Exist, Deletion Failed").showAndWait();
    }

    public BorderPane getContainer()
    {
        return this.container;
    }

}
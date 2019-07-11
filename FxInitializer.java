package pkj;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
public final class FxInitializer{
    
    private static FxInitializer instance = null;
    private FxInitializer(Stage primaryStage, int width, int height){
        init(primaryStage,width,height);
    }
    
    static{
        System.out.println("Initiating -- Load Dictionary --");
        System.out.println(Client.getInstance().loadDictionary() ? "Loaded Successfully, initiating App" : "Failed to Load, Terminating");
    }

    public static FxInitializer getInstance(Stage PrimaryStage, int width, int height)
    {
        if(instance == null)
            return instance = new FxInitializer(PrimaryStage, width, height);
        else
            return instance;
    }

    public void init(Stage primaryStage, int width, int height)
    {
        BorderPane pane = new DictionaryPane().getContainer();
        Scene scene = new Scene(pane,width,height);
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(scene);
        
    }
}
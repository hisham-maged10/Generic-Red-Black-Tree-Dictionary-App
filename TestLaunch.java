package pkj;
import javafx.application.Application;
import javafx.stage.Stage;
public class TestLaunch extends Application
{

    @Override
    public void start(Stage primaryStage) {
        FxInitializer.getInstance(primaryStage,600,300);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        Application.launch();
    }
}
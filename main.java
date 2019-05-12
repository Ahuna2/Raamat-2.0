import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    public static void main(String[] args) throws IOException {
        //MainCycle.runtime();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Reader reader = new Reader();
    }
}

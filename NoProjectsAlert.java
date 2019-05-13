import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NoProjectsAlert {

    public static void display() {
        //ALERT, MIS TEAVITAB, ET PROJEKE EI LEIDU NING LÕPETAB PROGRAMMI TÖÖ
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("No projects found");
        window.setWidth(200);
        window.setHeight(130);

        Label label = new Label("\nNo projects found!\n\n");

        Button button = new Button("Ok");
        button.setOnAction(e -> {
            window.close();
            System.exit(0);
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setTitle("No projects");
        window.showAndWait();
    }
}

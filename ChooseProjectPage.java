import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

class ChooseProjectPage {

    static Raamat chooseProject(File projectsFolder) throws FileNotFoundException, UnsupportedEncodingException {
        //LOOB UUE AKNA RAAMATU VALIKUKS NING TAGASTAB VALITUD RAAMATU.

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Choose a project");
        window.setWidth(200);
        //Akna default suurus suureneb iga uue valiku korral.
        int height = 50;

        VBox layout = new VBox(13);
        layout.setAlignment(Pos.BASELINE_LEFT);
        layout.setPadding(new Insets(10));

        HBox heading = new HBox(10);
        Label label = new Label("Choose a project:");
        heading.getChildren().add(label);
        heading.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().add(heading);

        //Kõik eksisteerivad raamatud.
        File[] allProjects = projectsFolder.listFiles();
        File[] valitud = new File[1];
        for (File el : allProjects) {
            Button button = new Button(el.getName());
            button.setOnAction(e -> {
                valitud[0] = el;
                window.close();
            });
            layout.getChildren().add(button);
            height+= 56;
        }

        window.setHeight(height);
        window.setResizable(false);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        //Edasi liigume alles siis, kui mingi raamat sai valitud.
        window.showAndWait();

        return MainCycle.loadProject(valitud[0]);
    }
}

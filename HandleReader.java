import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HandleReader {

    public static void handle(Raamat raamat, String openChapterId, double x, double y) throws IOException {

        Chapter openChapter = findChapter(raamat, openChapterId);

        Stage window = new Stage();
        window.setTitle(raamat.getTitle());
        VBox layout = new VBox();

        String chapterText = loadChapter(raamat, openChapter);

        TextArea chapter = new TextArea();
        chapter.setWrapText(true);
        chapter.setEditable(false);
        chapter.setText(chapterText);
        chapter.setStyle("-fx-font-size: 1.2em;");
        chapter.prefWidthProperty().bind(layout.widthProperty());
        chapter.prefHeightProperty().bind(layout.heightProperty());
        chapter.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        layout.getChildren().add(chapter);
        layout.setPrefSize(x, y);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

        boolean[] alreadyShown = new boolean[1];
        alreadyShown[0] = false;

        ScrollBar tvScrollBar = (ScrollBar) chapter.lookup(".scroll-bar:vertical");
        if (tvScrollBar.getWidth() == 20.0 && !alreadyShown[0]) {
            choice(openChapter, window, layout);
            alreadyShown[0] = true;
        }
        tvScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 1.00 && !alreadyShown[0]) {
                choice(openChapter, window, layout);
                alreadyShown[0] = true;
            }
        });
    }

    private static String loadChapter(Raamat raamat, Chapter chapter) throws IOException{

        int chapterIndex = raamat.getAllChapters().indexOf(chapter);

        String chapterText = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader
                (new FileInputStream(raamat.getAllChapters().get(chapterIndex).getDestination()), "UTF-8"))){
            in.readLine();
            in.readLine();
            String line;
            while ((line = in.readLine()) != null) {
                chapterText += "/&/"+ line + " ";
            }
        }
        chapterText = chapterText.replace("/&/", "\n    ").replace("  ", " ");
        if (chapterText.startsWith("null")) {
            chapterText = chapterText.substring(4);
        }
        if (chapterText.endsWith("null ")) {
            chapterText = chapterText.substring(0, chapterText.length() - 5);
        }
        return chapterText;
    }

    private static Chapter findChapter(Raamat raamat, String chapterId) {
        for (Chapter el : raamat.getAllChapters()) {
            String choiceId = el.getChapterID();
            int beginningIndex = el.getChapterID().indexOf("0");
            choiceId = choiceId.substring(beginningIndex);

            if (choiceId.equals(chapterId)) {
                return el;
            }
        }
        return null;
    }

    private static void choice(Chapter chapter, Stage window, VBox layout) {

        List<Choice> choices = chapter.getFollowingChoices();
        HBox choice = new HBox(30);

        for (Choice el : choices) {
            Button button = new Button(el.getChoiceText());
            button.setOnAction(e -> {
                try {
                    handle(chapter.getParent(), el.getChoiceID(), window.getWidth(), window.getHeight());
                    window.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            button.setAlignment(Pos.CENTER);
            choice.getChildren().add(button);
        }

        choice.setPadding(new Insets(5,5,5,5));
        choice.setAlignment(Pos.CENTER);
        layout.getChildren().add(choice);
    }
}

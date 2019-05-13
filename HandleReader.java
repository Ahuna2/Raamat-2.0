import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HandleReader {

    public static void handle(Raamat raamat, String openChapterId, double x, double y) throws IOException {
        //PEATÜKK EKRAANIL, LÕPPU KERIDES (VÕI KUI KERIDA POLE VAJADUST) ILMUVAD VALIKUD.

        Chapter openChapter = findChapter(raamat, openChapterId);

        Stage window = new Stage();
        window.setTitle(raamat.getTitle());
        VBox layout = new VBox();

        //Peatüki tekst toore sõnena.
        String chapterText = loadChapter(openChapter);

        //Peatüki teksti desain.
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

        //Kui valikud on juba ilmunud, väldib uue tekkimist.
        boolean[] alreadyShown = new boolean[1];

        //Kui kerida pole võimalik, näita valikuid.
        ScrollBar tvScrollBar = (ScrollBar) chapter.lookup(".scroll-bar:vertical");
        if (tvScrollBar.getWidth() == 20.0) {
            ChoiceBar.choice(openChapter, window, layout);
            alreadyShown[0] = true;
        }
        //Jälgib, kas kerimine on jõudnud lõppu.
        tvScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 1.00 && !alreadyShown[0]) {
                ChoiceBar.choice(openChapter, window, layout);
                alreadyShown[0] = true;
            }
        });
    }

    private static String loadChapter(Chapter chapter) throws IOException{
    //LEIAB UUE PEATÜKI TEKSTI, MIDA KUVADA

        int chapterIndex = chapter.getParent().getAllChapters().indexOf(chapter);

        //Peatüki sisu pika sõnena.
        String chapterText = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader
                (new FileInputStream(chapter.getParent().getAllChapters().get(chapterIndex).getDestination()), StandardCharsets.UTF_8))){
            in.readLine();
            in.readLine();
            String line;
            while ((line = in.readLine()) != null) {
                chapterText += "/&/"+ line + " ";
            }
        }
        //Peatüki sisu puhastamine.
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
        //LEIAB VALIKU ID JÄRGI UUE PEATÜKI
        //tekst 1[0] -> (valik 3[0.3] -> tekst 3[0.3]) -> (valik n[0.3.n] -> tekst n[0.3.n]).

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
}

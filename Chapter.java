import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Chapter {

    private String title;
    private File destination;
    private String chapterID;
    private Raamat Parent;
    private ArrayList<Choice> followingChoices = new ArrayList<>();


    Chapter(File destination, Raamat raamat) throws FileNotFoundException {
        //CONSTRUCTOR KUI AVATAKSE OLEMASOLEV PROJEKT
        this.destination = destination;
        Scanner scanner = new Scanner(destination);
        String[] headLine = scanner.nextLine().split(": ");                                                       //võtab sisse peatüki tekstifaili esimese rea
        this.chapterID = headLine[0];
        this.title = headLine[1].strip();
        this.Parent = raamat;
        this.Parent.addChapter(this);
    }

    Chapter(Raamat raamat, String chapterTitle) throws IOException {
        //CONSTRUCTOR KUI UUS CHAPTER ON RAAMATU ESIMENE
        this.title = chapterTitle;
        this.Parent = raamat;
        this.chapterID = "0";
        this.destination = new File(Parent.getDestination().getPath() + System.getProperty("file.separator") +
                "Chapters" + System.getProperty("file.separator") + this.title + ".txt");
        this.destination.createNewFile();
        System.out.println("First chapter created.");
        PrintWriter writer = new PrintWriter(this.destination, StandardCharsets.UTF_8);
        writer.println(this.chapterID + ": " + this.title.toUpperCase() +
                "\r\n=============================================================");
        writer.close();
    }

    Chapter(Raamat raamat, ArrayList<Choice> previousChoices, String title) throws IOException {
        //CONSTRUCTOR KUI LUUAKSE UUS MITTEESIMENE CHAPTER
        this.title = title;
        this.Parent = raamat;
        this.destination = new File(Parent.getDestination().getPath() + System.getProperty("file.separator") +
                "Chapters" + System.getProperty("file.separator") + this.title + ".txt");
        this.chapterID = previousChoices.get(0).getChoiceID();
        this.destination.createNewFile();
        System.out.println("Chapter created.");
        PrintWriter writer = new PrintWriter(this.destination, StandardCharsets.UTF_8);
        writer.println(this.chapterID + ": " + this.title.toUpperCase() +
                "\r\n=============================================================");
        writer.close();
    }

    void openChapter() throws IOException {
        //AVAB PEATÜKI NOTEPADIS
        //TODO ära ava notepadis
        Desktop.getDesktop().open(this.destination);
    }

    void addFollowingChoice(Choice choice) {
        this.followingChoices.add(choice);
    }

    String getChapterID() {
        return this.chapterID;
    }

    ArrayList<Choice> getFollowingChoices() {
        return this.followingChoices;
    }

    File getDestination() {
        return this.destination;
    }

    File getParentDestination() {
        return Parent.getDestination();
    }

    Raamat getParent() {
        return this.Parent;
    }

    @Override
    public String toString() {
        return this.getChapterID() + ", " + this.title + ": "
                + this.getFollowingChoices();
    }
}

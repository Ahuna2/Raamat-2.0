import java.io.*;
import java.util.Objects;

public class Choice {

    private String choiceText;
    private String choiceID;                                                                                            //mis chapteriga ühendab
    private Chapter Parent;

    public Choice(String choiceID, String choiceText, Chapter parent) {
        //CONSTRUCTOR OLEMASOLEVA VALIKU SISSELUGEMISEKS
        int beginningIndex = choiceID.indexOf("0");
        choiceID = choiceID.substring(beginningIndex);
        this.choiceID = choiceID;
        this.choiceText = choiceText;
        this.Parent = parent;
        this.Parent.addFollowingChoice(this);
    }

    public Choice(Chapter parent, String choiceText) throws IOException {
        //CONSTRUCTOR UUE VALIKU LOOMISEKS
        this.choiceText =  choiceText;
        this.Parent = parent;
        this.choiceID = Parent.getChapterID() + "." + (Parent.getFollowingChoices().size() + 1);
        Parent.addFollowingChoice(this);
        addChoiceToAllChoices();
    }

    public void addChoiceToAllChoices() throws IOException {
        //LISAB LOODUD VALIKU PROJEKTI VALIKUTE FAILI LÕPPU
        File choicesTxt = new File(Parent.getParentDestination().getPath() +
                System.getProperty("file.separator") + "All Choices.txt");
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(choicesTxt, true)));
        String newChoice = this.choiceID + ";" + this.Parent.getChapterID() + ";" + this.choiceText;
        writer.println(newChoice);
        writer.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice choice = (Choice) o;
        return Objects.equals(choiceID, choice.choiceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choiceID);
    }

    public String getChoiceText() {
        return this.choiceText;
    }

    public String getChoiceID() {
        return this.choiceID;
    }

    @Override
    public String toString() {
        return this.choiceID + ": " + this.getChoiceText();
    }
}


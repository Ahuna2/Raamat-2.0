import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Raamat {

    private String title;
    private File destination;
    private ArrayList<Chapter> allChapters = new ArrayList<>();

    public Raamat(File destination) {
        //CONSTRUCTOR OLEMASOLEVA RAAMATU LAADIMISEKS
        this.destination = destination;
        this.title = destination.getName();
    }

    public Raamat() throws IOException {
        //CONSTRUCTOR UUE RAAMATU LOOMISEKS
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nA name for the project: ");
        this.title = scanner.nextLine().strip();
        File savedProjectsFolder = new File(System.getProperty("user.dir") +
                System.getProperty("file.separator") + "Saved Projects");
        if (!savedProjectsFolder.exists()) {
            savedProjectsFolder.mkdir();
        }
        this.destination = new File(savedProjectsFolder.getPath() + System.getProperty("file.separator") +
                this.title);
        for (int i = 1; !this.destination.mkdir(); i++) {
            this.destination = new File(savedProjectsFolder.getPath() + System.getProperty("file.separator") +
                    this.title);
            this.destination = new File(this.destination.getPath() + "(" + i + ")");
        }
        new File(this.destination.getPath() + System.getProperty("file.separator") + "Chapters").mkdir();
        File allChoices = new File(this.destination.getPath() +
                System.getProperty("file.separator") + "All Choices.txt");
        allChoices.createNewFile();
    }

    public void deleteChapter(Chapter chapter) {
        //KUSTUTAB PEATÜKI
        this.allChapters.remove(chapter);System.out.println("Are you sure? " +
                "This deletes the chapter along with any associated choices and cannot be reversed");
        System.out.println("\n1 - Yes");
        System.out.println("2 - No");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine();
            try {
                if(Integer.parseInt(userInput) == 1) {
                    System.out.println("Successfully deleted");
                    this.allChapters.remove(chapter);
                    chapter.getDestination().delete();
                    break;
                }
                else if (Integer.parseInt(userInput) == 2) {
                    break;
                }
                else {
                    System.out.println("Invalid input");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    public void listChapters() {
        //PRINDIB KÕIK LEIDUVAD PEATÜKID
        File allChaptersFolder = new File(this.destination.getPath() +
                System.getProperty("file.separator") + "Chapters");
        File[] allChapters = allChaptersFolder.listFiles();
        for (int i = 0; i < allChapters.length; i++) {
            System.out.println((i + 1) + " - " + allChapters[i].getName().replace(".txt", ""));
        }
        System.out.println("X - Return");
    }

    public void addChapter(Chapter chapter) {
        this.allChapters.add(chapter);
    }

    public File getDestination() {
        return this.destination;
    }

    public ArrayList<Chapter> getAllChapters() {
        return this.allChapters;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        String valjund = this.title + "\n";
        for (Chapter el : this.getAllChapters()) {
            valjund+= el + "\n";
        }
        return valjund;
    }
}

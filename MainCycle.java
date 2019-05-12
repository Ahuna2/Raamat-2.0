import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainCycle {


    public static void runtime() throws IOException {
        //Peamine runtime, mis jookseb kuni saab kasutajalt käskluse töö lõpetamiseks

        while (true) {

            System.out.println("\n1 - Choose an existing project.");
            System.out.println("2 - Create a new project.");
            System.out.println("X - Exit the program.");

            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            try {
                if (Integer.parseInt(userInput) == 1) {
                    openExistingProject();
                }
                else if (Integer.parseInt(userInput) == 2) {
                    createNewProject();
                }
            }
            catch (NumberFormatException e) {
                if (userInput.equalsIgnoreCase("x")) {
                    System.out.println("\nExiting...");
                    break;
                }
                else {
                    System.out.println("Unknown command.");
                }
            }
        }
    }

    private static void openExistingProject() throws IOException {
        //AVAB OLEMASOLEVA PROJEKTI
        File savedProjectsFolder = new File(System.getProperty("user.dir") +
                System.getProperty("file.separator") + "Saved Projects");
        if (!savedProjectsFolder.exists() || savedProjectsFolder.listFiles().length == 0) {                             //kui projektikausta ei eksisteeri, suunab kasutaja uut looma
            System.out.println("\nNo projects yet!");

            while (true) {
                System.out.println("Would you like to create one?");
                System.out.println("\n1 - Yes");
                System.out.println("2 - No");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                try {
                    if (Integer.parseInt(userInput) == 1) {
                        createNewProject();
                        break;
                    } else if (Integer.parseInt(userInput) == 2) {
                        break;
                    }
                }
                catch (NumberFormatException|NullPointerException e) {
                    System.out.println("Unknown command.");
                }
            }

        } else {                                                                                                        //kui projektide kaust eksisteerib

            File[] allSavedProjects = savedProjectsFolder.listFiles();
            System.out.println();
            for (int i = 1; i < allSavedProjects.length + 1; i++) {
                System.out.println(i + " - " + allSavedProjects[i - 1].getName().replace(".txt", ""));
            }
            System.out.println("X - Return");
            Scanner scanner = new Scanner(System.in);
            File openedProject = null;
            while (true) {
                String userInput = scanner.nextLine();
                try {
                    if (userInput.equalsIgnoreCase("x")) {
                        break;
                    }
                    openedProject = allSavedProjects[Integer.parseInt(userInput) - 1];
                    break;
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Invalid input");
                }
            }
            if (openedProject == null) {
                runtime();
            }
            Raamat openProject = loadProject(openedProject);
            Chapter openChapter = handleChooseChapterDecision(openProject);
            if (openChapter == null) {
                runtime();
            }
            handleChapterDecision(openProject, openChapter);
        }
    }

    static Raamat loadProject(File projectFolder) throws FileNotFoundException, UnsupportedEncodingException {
        //Olemasoleva projekti klassidena sisselugemiseks
        Raamat openRaamat = new Raamat(projectFolder);                                                                  //loob raamatu

        File chapterFolder = new File(projectFolder.getPath() + System.getProperty("file.separator") +         //lisab raamatule peatükid
                "Chapters");

        for (File chapterFile : chapterFolder.listFiles()) {
            Chapter chapter = new Chapter(chapterFile, openRaamat);
            loadChoices(chapter, projectFolder);                                                                        //lisab peatükkidele valikud
        }
        return openRaamat;
    }

    private static void loadChoices(Chapter chapter, File projectFolder) throws FileNotFoundException, UnsupportedEncodingException {
        //LOOB IGALE VALIKULE KLASSI NING LINGIB NEED PEATÜKKDIEGA
        File allChoicesFile = new File(projectFolder.getPath() + System.getProperty("file.separator") +
                "All Choices.txt");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (new FileInputStream(allChoicesFile), "UTF-8"));

            String choiceRaw;
            while ((choiceRaw = in.readLine()) != null) {

                int beginningIndex = choiceRaw.indexOf("0");
                choiceRaw = choiceRaw.substring(beginningIndex);
                String[] choice = choiceRaw.split(";");
                if (choice[1].equals(chapter.getChapterID())) {
                    new Choice(choice[0], choice[2], chapter);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Chapter handleChooseChapterDecision(Raamat raamat) throws IOException {
        //VALIB CHAPTERI MILLEGA EDASI TEGUTSEDA
        System.out.println("\n1 - Select an existing chapter");
        System.out.println("2 - Create a new chapter");
        System.out.println("X - Return");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String userInput = scanner.nextLine();
            try {
                if (Integer.parseInt(userInput) == 1) {
                    System.out.println();
                    raamat.listChapters();
                    Chapter openChapter;
                    while (true) {
                        System.out.println("\nWhich chapter?");//TODO kontroll kas kaustas on faile
                        userInput = scanner.nextLine();
                        try {
                            openChapter = raamat.getAllChapters().get(Integer.parseInt(userInput) - 1);
                            return openChapter;
                        }
                        catch (NumberFormatException| IndexOutOfBoundsException|NullPointerException e) {
                            if (userInput.equalsIgnoreCase("X")) {
                                break;
                            }
                            else {
                                System.out.println("Invalid input.");
                            }
                        }
                    }
                    break;
                } else if (Integer.parseInt(userInput) == 2) {
                    //TODO
                    return createNewChapter(raamat);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                if (userInput.equalsIgnoreCase("x")) {
                    break;
                } else {
                    System.out.println("Invalid input.");
                }
            }
        }
        return null;
    }

    private static void handleChapterDecision(Raamat raamat, Chapter chapter) throws IOException {
        //Laeb peatükile kuuluvad valikud failist "All Choices" peatüki arraysse

        while (true) {

            System.out.println("\n1 - Open the chapter");
            System.out.println("2 - Delete the chapter");
            System.out.println("3 - Add a choice");
            System.out.println("4 - Modify a choice");
            System.out.println("5 - Delete a choice");
            System.out.println("X - Return");

            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            try {
                if (Integer.parseInt(userInput) == 1) {
                    chapter.openChapter();
                }
                else if (Integer.parseInt(userInput) == 2) {
                    raamat.deleteChapter(chapter);
                    break;
                }
                else if (Integer.parseInt(userInput) == 3) {
                    System.out.println("\nThe choice offered:");
                    Scanner scanner1 = new Scanner(System.in);
                    String userInputChoice = scanner1.nextLine();
                    new Choice(chapter, userInputChoice);
                }
                else if (Integer.parseInt(userInput) == 4) {
                    System.out.println("Which one?\n");
                    for (int i = 0; i < chapter.getFollowingChoices().size(); i++) {
                        System.out.println((i + 1) + " - " + chapter.getFollowingChoices().get(i).getChoiceID() +
                                ": " + chapter.getFollowingChoices().get(i).getChoiceText());
                    }//TODO proof it
                    userInput = scanner.nextLine();
                    System.out.println("The updated choice:");

                    while (true) {
                        userInput = scanner.nextLine();
                        //TODO
                    }
                }
            }
            catch (NumberFormatException e) {
                if (userInput.equalsIgnoreCase("x")) {
                    break;
                }
                else {
                    System.out.println("Unknown command");
                }
            }
        }
    }

    private static void createNewProject() throws IOException {
        //LOOB UUE PROJEKTI
        Raamat openRaamat = new Raamat();
        Chapter openChapter = null;
        while (openChapter == null) {
            openChapter = handleChooseChapterDecision(openRaamat);
        }
        handleChapterDecision(openRaamat, openChapter);
    }

    private static Chapter createNewChapter(Raamat raamat) throws IOException {
        //LOOB UUE PEATÜKI
        System.out.println("\nA title for it:");
        Scanner scanner = new Scanner(System.in);
        String chapterTile = scanner.nextLine().strip();
        if (raamat.getAllChapters().isEmpty()) {
            return new Chapter(raamat, chapterTile);
        }
        else {
            int i = 0;
            System.out.println("\nFor which choice?\n");
            ArrayList<Choice> choiceOptions = new ArrayList<>();
            for (Chapter el : raamat.getAllChapters()) {
                for (Choice el2 : el.getFollowingChoices()) {
                    if (!choiceOptions.contains(el2)) {
                        choiceOptions.add(el2);
                        System.out.println((i+1) + " - " + el2.getChoiceID() + ": " + el2.getChoiceText());
                        //      System.out.println(el.getChapterID() + " | " + el.getTitle());
                        i++;
                    }
                }
            }

            System.out.println("X - Return");
            while (true) {
                String userInput = scanner.nextLine();
                try {
                    if (Integer.parseInt(userInput) - 1 < choiceOptions.size()) {
                        Choice choice = choiceOptions.get(Integer.parseInt(userInput) - 1);

                        ArrayList<Choice> similarChoices = new ArrayList<>();
                        for (Choice el : choiceOptions) {
                            if (choice.getChoiceID().equals(el.getChoiceID())) {
                                similarChoices.add(el);
                            }
                        }
                        return new Chapter(raamat, similarChoices, chapterTile);
                    }
                    else {
                        System.out.println("Illegal input");
                    }
                } catch (NumberFormatException|IndexOutOfBoundsException e) {
                    if (userInput.equalsIgnoreCase("x")) {
                        return null;
                    }
                    else {
                        System.out.println("Illegal input");
                    }
                }
            }
        }
    }
}

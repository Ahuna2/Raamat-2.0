import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;

public class Reader {

    private File projectsFolder = new File(System.getProperty("user.dir")
            + System.getProperty("file.separator") + "Saved Projects");
    private Scene window;


    public Reader() throws IOException {
        if (!this.projectsFolder.exists()) {
            this.projectsFolder.mkdir();
            NoProjectsAlert.display();
        }
        else if (this.projectsFolder.listFiles().length == 0) {
            NoProjectsAlert.display();
        }
        else {
            Raamat openProject = ChooseProjectPage.chooseProject(this.projectsFolder);
            HandleReader.handle(openProject, "0", 500, 500);
        }
    }


}
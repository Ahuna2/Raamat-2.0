import java.io.File;
import java.io.IOException;

class Reader {


    Reader() throws IOException {
        //OTSUSTAB, KAS LIIGUB PROJEKTIVALIKU JUURDE VÕI ANNAB NOPROJECTSALERT ALERDI

        //Kui "Saved Projects" folder ei eksisteeri, anna alert ja sulgu.
        File projectsFolder = new File(System.getProperty("user.dir")
                + System.getProperty("file.separator") + "Saved Projects");

        if (!projectsFolder.exists()) {
            projectsFolder.mkdir();
            NoProjectsAlert.display();
        }
        //Kui "Saved Projects" folder on tühi, anna alert ja sulgu.
        else if (projectsFolder.listFiles().length == 0) {
            NoProjectsAlert.display();
        }
        //Muidu liigu valiku juurde.
        else {
            Raamat openProject = ChooseProjectPage.chooseProject(projectsFolder);
            HandleReader.handle(openProject, "0", 500, 500);
        }
    }
}

import Robe.Person;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller class of the FAQPage
 */
public class RecapController {

    @FXML
    private ListView<String> recapList1;

    @FXML
    private ListView<String> recapList2;

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        String temp;
        List<String> workersList = new ArrayList<>();
        List<String> holidaysList = new ArrayList<>();
        int i=0;
        // ► ♥ ♦
        for(Person p : Main.calendario.getWorkers()) {
            temp = "♥ " + p.getName() + " " + p.getSurname() + " ♥\n";
            temp = temp.concat((Main.calendario.getNumberOfTurns(p.getName(),p.getSurname())-p.getNights()) + " + " + p.getNights());       // number of working turns, divided by day or night
            workersList.add(temp);
            temp = "♥ " + p.getName() + " " + p.getSurname() + " ♥\n";
            temp = temp.concat("Ferie: " + p.printHolidays());
            holidaysList.add(temp);
        }
        recapList1.setItems(FXCollections.observableList(workersList));
        recapList2.setItems(FXCollections.observableList(holidaysList));
    }

    /**
     * This method change the scene to the FrontPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showCalendar(Event event) throws IOException {
        new Main().changeScene("fxml/FrontPage.fxml");
    }

    /**
     * This method change the scene to the WorkerPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showWorkers(Event event) throws IOException {
        new Main().changeScene("fxml/WorkersPage.fxml");
    }

    /**
     * This method change the scene to the NewWorkerPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void newWorker(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/NewWorkerPage.fxml");
    }

    /**
     * This method change the scene to the RemoveWorkerPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void removeWorker(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/RemoveWorkerPage.fxml");
    }

    /**
     * This method save the changes done to the worker for future usage
     */
    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        Main.calendario.saveWorkers();
    }

}

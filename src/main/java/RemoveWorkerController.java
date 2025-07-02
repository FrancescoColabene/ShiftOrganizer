import Robe.Person;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller class of the RemoveWorkerController
 */
public class RemoveWorkerController {

    private boolean done;

    private List<String> workersList;

    @FXML
    private ListView<String> workers;

    @FXML
    private Button confirmationButton;

    @FXML
    private Text popUpText;

    @FXML
    private Button backButton;


    public void initialize(){
        done = false;
        workersList = new ArrayList<>();
        for (Person p : Main.calendario.getWorkers()){
            workersList.add(p.getFullname());
        }
        workers.setItems(FXCollections.observableArrayList(workersList));
    }

    @FXML
    void showButton(MouseEvent event) {
        confirmationButton.setDisable(false);
    }

    @FXML
    void removeWorker(ActionEvent event) throws IOException {
        if(!done) {
            done = true;
            workers.setVisible(false);
            confirmationButton.setText("Cancella un altro dipendente");
            try{
                String[] tempString = workersList.get(workers.getSelectionModel().getSelectedIndex()).split(" ");
                if(Main.calendario.removeWorker(tempString[0],tempString[1])){
                    popUpText.setText("Dipendente rimosso con successo!");
                }
            } catch (IndexOutOfBoundsException e) {
                    popUpText.setText("Devi selezionare un dipendente!");
            }
            popUpText.setVisible(true);
        } else {
            new Main().changeScene("fxml/RemoveWorkerPage.fxml");
        }
    }

    @FXML
    void returnToFront(ActionEvent event) throws IOException {
        Main.calendario.saveWorkers();
        new Main().changeScene("fxml/FrontPage.fxml");
    }
}

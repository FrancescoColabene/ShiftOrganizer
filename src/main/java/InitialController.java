import Robe.Calendario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import Giornate.Months;

import java.io.IOException;
import java.util.*;

/**
 * This is the controller class of the InitialPage
 */
public class InitialController {

    @FXML
    private ChoiceBox<String> pickMonth;

    @FXML
    private TextField pickYear;

    @FXML
    private ChoiceBox<String> pickSchool;

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        //System.out.println(System.getProperty("user.dir"));
        List<String> list = new ArrayList<>();
        for (Months m : Months.values()){
            list.add(m.toString());
        }
        pickMonth.setItems(FXCollections.observableArrayList(list));
        pickMonth.setValue("Gennaio");
        pickSchool.setItems(FXCollections.observableArrayList("No","Si"));
        pickSchool.setValue("Si");
    }

    /**
     * This method is called upon pressing the "Continue" button. It creates the calendar for the main - this part is meme tbh
     * It also changes the scene
     * @param event
     * @throws IOException if the creation of the calendar creates errors
     */
    @FXML
    void loadCalendar(ActionEvent event) throws IOException {
        Main temp = new Main();
        int year;
        try{
            year = Integer.parseInt(pickYear.getText());
        } catch (NumberFormatException e) {
            return;
        }
        int month = Months.valueOf(pickMonth.getValue()).getMonth();

        boolean res = pickSchool.getValue().equals("Si");

        // The init variable is used to differentiate the source of information:
        // init==1 takes the resources from the "resources" directory inside the file (aka used in the IDe)
        // init==0 takes the resources from the directory "workers" from the root, and creates it if it doesn't exist
        Main.calendario = new Calendario(0,year,month, res);

        temp.changeScene("fxml/SupervisionPage.fxml");
    }
}
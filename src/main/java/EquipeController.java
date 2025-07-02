
import Giornate.DayLav;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller class of the EquipePage
 */
public class EquipeController {

    private boolean done = false;

    @FXML
    private Button confirmationButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> equipeDays;

    @FXML
    private Text popUpText;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Text textRed;

    @FXML
    private Button backButton;

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        List<String> tempList = new ArrayList<>();
        for (int i=1; i < Main.calendario.getNumberDays() + 1 ; i++){
            DayLav tempDay = Main.calendario.getDay(i);
            if(tempDay.isEquipe()){
                tempList.add(tempDay.getName() +  " " + tempDay.getNumber());
            }
        }
        equipeDays.setItems(FXCollections.observableArrayList(tempList));
        datePicker.setValue(LocalDate.of(Main.calendario.getYear(), Main.calendario.getMonth(), 1));
    }

    /**
     * This method makes the button usable
     * @param event
     */
    @FXML
    void showButton(ActionEvent event) {
        confirmationButton.setDisable(false);
    }

    /**
     * This method is called upon pressing the "confirm" button, and adds or remove an equipe. If done==false that means the user has already choose a day, so it reloads the scene
     * @param event
     * @throws IOException
     */
    @FXML
    void setEquipe(ActionEvent event) throws IOException {
        if(!done) {
            LocalDate date = datePicker.getValue();
            clearScreen();
            if (Main.calendario.setEquipe(date.getDayOfMonth())) {
                popUpText.setText("Equipe del " + date.getDayOfMonth() + " aggiunta con successo!");
            } else {
                popUpText.setText("Equipe del " + date.getDayOfMonth() + " rimossa con successo!");
            }
            popUpText.setVisible(true);
            confirmationButton.setText("Aggiungi un'altra equipe");
            done = true;
        } else {
            new Main().changeScene("fxml/EquipePage.fxml");
        }
    }

    /**
     * This method advance the program into the next scene, which is the frontpage
     * @param event
     * @throws IOException
     */
    @FXML
    void returnToFront(ActionEvent event) throws IOException {

        new Main().changeScene("fxml/FrontPage.fxml");
    }

    /**
     * This method clears every object on screen apart from the buttons
     */
    private void clearScreen(){
        text1.setVisible(false);
        text2.setVisible(false);
        datePicker.setVisible(false);
        equipeDays.setVisible(false);
        textRed.setVisible(false);
    }
}
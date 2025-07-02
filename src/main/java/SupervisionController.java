import Giornate.DayLav;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller class of the SupervisionPage
 */
public class SupervisionController {

    private boolean done = false;

    @FXML
    private AnchorPane selectPane;

    @FXML
    private Button confirmationButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> supervisionDays;

    @FXML
    private Text popUpText;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> startingH;

    @FXML
    private ComboBox<String> startingM;

    @FXML
    private ComboBox<String> endingH;

    @FXML
    private ComboBox<String> endingM;

    @FXML
    private Text textRed;

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        List<String> tempList = new ArrayList<>();
        for (int i=1; i < Main.calendario.getNumberDays() + 1; i++){
            DayLav tempDay = Main.calendario.getDay(i);
            if(tempDay.isSupervision()){
                //tempList.add(tempDay.getName() +  " " + tempDay.getNumber());
                String sMin;
                if(tempDay.getSMinuteSuper() < 10) {
                    sMin = "0" + tempDay.getSMinuteSuper();
                } else {
                    sMin = String.valueOf(tempDay.getSMinuteSuper());
                }
                String eMin;
                if(tempDay.getEMinuteSuper() < 10) {
                    eMin = "0" + tempDay.getEMinuteSuper();
                } else {
                    eMin = String.valueOf(tempDay.getEMinuteSuper());
                }
                tempList.add(tempDay.getName() + " " + tempDay.getNumber() + " " +
                        tempDay.getSHourSuper() + ":" + sMin + "-" + tempDay.getEHourSuper() + ":" + eMin );

            }
        }

        supervisionDays.setItems(FXCollections.observableArrayList(tempList));
        datePicker.setValue(LocalDate.of(Main.calendario.getYear(), Main.calendario.getMonth(), 1));

        //setting choiceboxs
        List<String> hourList = new ArrayList<>();
        hourList.add("00");
        for(int i=1;i<24;i++){
            if(i<10){
                hourList.add("0"+i);
            } else hourList.add(String.valueOf(i));
        }
        startingH.setItems(FXCollections.observableArrayList(hourList));
        endingH.setItems(FXCollections.observableArrayList(hourList));
        List<String> minuteList = new ArrayList<>();
        minuteList.add("00");
        for(int i=1;i<60;i++){
            if(i<10){
                minuteList.add("0"+i);
            } else minuteList.add(String.valueOf(i));
        }
        startingM.setItems(FXCollections.observableArrayList(minuteList));
        endingM.setItems(FXCollections.observableArrayList(minuteList));
        startingH.setValue("10");
        endingH.setValue("12");
        startingM.setValue("00");
        endingM.setValue("00");

        startingH.setVisibleRowCount(6);
        startingM.setVisibleRowCount(10);
        endingH.setVisibleRowCount(6);
        endingM.setVisibleRowCount(10);
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
     * This method is called upon pressing the "confirm" button, and adds or remove a supervision. If done==false that means the user has already choose a day, so it reloads the scene
     * @param event
     * @throws IOException
     */
    @FXML
    void setSupervision(ActionEvent event) throws IOException {
        if(!done) {
            LocalDate date = datePicker.getValue();
            clearScreen();
            int hs = Integer.parseInt(startingH.getValue());
            int ms = Integer.parseInt(startingM.getValue());
            int hf = Integer.parseInt(endingH.getValue());
            int mf = Integer.parseInt(endingM.getValue());
            if (Main.calendario.setSupervision(date.getDayOfMonth(), hs, ms, hf, mf)) {
                popUpText.setText("Supervisione del " + date.getDayOfMonth() + " aggiunta con successo!");
            } else {
                popUpText.setText("Supervisione del " + date.getDayOfMonth() + " rimossa con successo!");
            }
            popUpText.setVisible(true);
            done = true;
            confirmationButton.setText("Modifica supervisioni");
            backButton.setText("Vai alle equipe");
        } else {
            new Main().changeScene("fxml/SupervisionPage.fxml");
        }
    }

    /**
     * This method advance the program into the next scene
     * @param event
     * @throws IOException
     */
    @FXML
    void returnToFront(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/EquipePage.fxml");
    }

    /**
     * This method clears every object on screen apart from the buttons
     */
    private void clearScreen(){

        // la prossima volta tutto in anchor pane e metto quello invisibile!

        text1.setVisible(false);
        text2.setVisible(false);
        supervisionDays.setVisible(false);
        selectPane.setVisible(false);
        textRed.setVisible(false);
    }
}

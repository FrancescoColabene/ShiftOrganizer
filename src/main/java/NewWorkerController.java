import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * This is the controller class of the NewControllerPage
 */
public class NewWorkerController {

    private int done=0;

    @FXML
    private TextField hours;

    @FXML
    private Text hoursText;

    @FXML
    private TextField name;

    @FXML
    private Text nameText;

    @FXML
    private TextField nightHours;

    @FXML
    private Text nightText;

    @FXML
    private TextField surname;

    @FXML
    private Text surnameText;

    @FXML
    private Text popUpText;

    @FXML
    private Button confirmationButton;


    @FXML
    private GridPane gridPanel;

    @FXML
    private Button backButton;

    @FXML
    private RadioButton noCheck;

    @FXML
    private RadioButton yesCheck;

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        ToggleGroup group = new ToggleGroup();
        yesCheck.setToggleGroup(group);
        yesCheck.setSelected(true);
        noCheck.setToggleGroup(group);
    }

    /**
     * This method tries to create a new worker based on the given information. After doing that, pressing the button again will load again the NewWorkerPage.
     * If the creation doesn't go through, pressing the button again will make you modify the information
     * @param event
     * @throws IOException
     */
    @FXML
    void saveWorker(ActionEvent event) throws IOException {
        if(done==0) {
            clearScreen();
            try{
                Float.parseFloat(hours.getText());
                Float.parseFloat(nightHours.getText());
            } catch (NumberFormatException e){
                popUpText.setText("C'è qualcosa di sbagliato!");
                confirmationButton.setText("Riprova");
                popUpText.setVisible(true);
                done = 2;
                return;
            }
            if(name.getText().equals("") || surname.getText().equals("") || hours.getText().equals("") || nightHours.getText().equals("")){
                popUpText.setText("C'è qualcosa di sbagliato!");
                popUpText.setVisible(true);
                done = 2;
                return;
            }
            if (Main.calendario.addWorker(name.getText(), surname.getText(), Integer.parseInt(hours.getText()), Float.parseFloat(nightHours.getText()), !noCheck.isSelected())) {
                popUpText.setText("Dipendente aggiunto correttamente!");
            } else {
                popUpText.setText("Esiste già una persona con questo nome e cognome!");
            }
            popUpText.setVisible(true);
            done = 1;
            confirmationButton.setText("Aggiungi un altro dipendente");
        }
        else if(done == 1){
            new Main().changeScene("fxml/NewWorkerPage.fxml");
        }
        else {
            confirmationButton.setText("Conferma");
            paintScreen();
            popUpText.setVisible(false);
            done = 0;
        }
    }

    /**
     * This method change the scene to the FrontPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void returnToFront(ActionEvent event) throws IOException {
        Main.calendario.saveWorkers();
        new Main().changeScene("fxml/FrontPage.fxml");
    }

    /**
     * This method is used to set the yes radioButton to true
     */
    @FXML
    void yesClick(ActionEvent event){
        yesCheck.setSelected(true);
    }

    /**
     * This method is used to set the no radioButton to true
     */
    @FXML
    void noClick(ActionEvent event){
        noCheck.setSelected(true);
    }

    /**
     * This method set everything except buttons to not visible
     */
    private void clearScreen(){
        gridPanel.setVisible(false);
    }

    /**
     * This method set everything except buttons to visible
     */
    private void paintScreen(){
        gridPanel.setVisible(true);
    }
}
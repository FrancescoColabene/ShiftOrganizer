import Giornate.Bands;
import Giornate.DayLav;
import Robe.Person;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the controller class of the SupervisionPage
 */
public class FrontController {

    private List<String> workersList;

    @FXML
    private AnchorPane calendarInfo;

    @FXML
    private AnchorPane workerPanel;

    @FXML
    private ListView<String> workersCal;

    @FXML
    private AnchorPane calendarPane;

    @FXML
    private List<Text> dayNames;

    @FXML
    private Text assignedHours;

    @FXML
    private Text totalHours;

    @FXML
    private Text turns;

    @FXML
    private Text nightHours;

    @FXML
    private Text doesNightText;

    @FXML
    private List<Text> matrixTurns;

    @FXML
    private Text supervisionDate;

    @FXML
    private Text equipeDate;

    @FXML
    private AnchorPane popupWindow;

    @FXML
    private Text popupText;

    @FXML
    private GridPane gridInfo;

    private final Font normalFont = Font.loadFont(getClass().getResourceAsStream("font/ubuntuMono.ttf"), 13);

    private final Font boldFont = Font.loadFont(getClass().getResourceAsStream("font/ubuntuMonoBold.ttf"), 13);


    // AZZURRO EQUIPE ARANCIONE SUPERVISIONE

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){

        //InputStream is = getClass().getResourceAsStream("/font/ubuntuMono.tff");
        Font font = Font.loadFont("Lucida console", 10);

        workersList = new ArrayList<>();
        for (Person p : Main.calendario.getWorkers()){
            workersList.add(p.getFullname());
        }
        workersCal.setItems(FXCollections.observableArrayList(workersList));
        String name = "";
        for(int i=1; i<Main.calendario.getNumberDays()+1 ; ++i ){
            name = Main.calendario.getDay(i).getName().toString();
            if(name.equals("SABATO") || name.equals("DOMENICA")){
                dayNames.get(i-1).setFill(Color.DARKRED);
            }
            dayNames.get(i-1).setText(name);
        }
        updateCalendar(new ActionEvent());

        // li setto tutti disabilitati fino al click della lista
        for(Text text : matrixTurns){
            text.setDisable(true);
        }

        for (Text t : matrixTurns){
            t.setFont(normalFont);
        }

        // TODO METODO IN CALENDARIO PER SUPERVISIONE!
        boolean flag1=true;
        boolean flag2=true;
        for (int i=1; i < Main.calendario.getNumberDays() + 1 ; i++){
            DayLav tempDay = Main.calendario.getDay(i);
            if(tempDay.isSupervision()){
                flag1=false;
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
                supervisionDate.setText(tempDay.getName() + " " + tempDay.getNumber() + "\n" +
                                        tempDay.getSHourSuper() + ":" + sMin + "\n" +
                                        "-" + "\n" +
                                        tempDay.getEHourSuper() + ":" + eMin );
            }
            if(tempDay.isEquipe()){
                if(flag2){
                    equipeDate.setText(tempDay.getName() + " " + tempDay.getNumber());
                    flag2=false;
                } else {
                    String temp = equipeDate.getText();
                    temp = temp.concat("\n" + tempDay.getName() + " " + tempDay.getNumber());
                    equipeDate.setText(temp);
                }
            }
        }
        if(flag1){
            supervisionDate.setText("Supervisione non assegnata");
            supervisionDate.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 16));
            supervisionDate.setFill(Color.DARKRED);
        }
        if(flag2){
            equipeDate.setText("Equipe non assegnate");
            equipeDate.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 16));
            equipeDate.setFill(Color.DARKRED);
        }
        String s = supervisionDate.getText();
        if(Main.calendario.getSchool()) supervisionDate.setText(s + "\n\nScuola: SI" );
        else supervisionDate.setText(s + "\nScuola: NO" );

        popupWindow.setDisable(true);
    }

    /**
     * This method change the scene to the RecapPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showSummary(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/RecapPage.fxml");
    }

    /**
     * This method start the automatic assignment of the calendar
     */
    @FXML
    void startScheduling(ActionEvent event){
        Main.calendario.doThings();
        updateCalendar(new ActionEvent());
        updateInfo();
    }

    /**
     * This method change the scene to the WorkersPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showWorkers(Event event) throws IOException {
        new Main().changeScene("fxml/WorkersPage.fxml");
    }

    /**
     * This method makes the workerPanel visible and updates its values. It also enables the usage of the calendar assignment
     */
    @FXML
    void showWorkerInfo(MouseEvent event){
        updateInfo();
        workerPanel.setVisible(true);

        for(int i=0;i<Main.calendario.getNumberDays()*5;i++){
            matrixTurns.get(i).setDisable(false);
        }
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
     * This method save the changes done to the workers
     * @throws IOException
     */
    @FXML
    void saveChanges(ActionEvent event) throws IOException {
        Main.calendario.saveWorkers();
    }

    //@FXML
    //void addEquipe(ActionEvent event) throws IOException {
    //    new Main().changeScene("EquipePage.fxml");
    //}

    //@FXML
    //void addSupervision(ActionEvent event) throws IOException {
    //    new Main().changeScene("SupervisionPage.fxml");
    //}

    /**
     * This method is used to assign a turn to the selected person by clicking on the wanted turn
     * @param event
     */
    @FXML
    void assignTurn(MouseEvent event){
        Text temp = (Text) event.getSource();
        String id = temp.getId();
        int day, day2, band;
        day = id.charAt(4)-48;
        day2 = id.charAt(5)-48;
        if(day2 == 22){
            band = id.charAt(6)-48;
        } else {
            band = id.charAt(7)-48;
            day = day*10+day2;
        }
        if(Main.calendario.setWorker(Objects.requireNonNull(getSelectedPerson()), day, Bands.bandOf(band)) == -1){
            calendarPane.setDisable(true);
            popupText.setText(getSelectedPerson().getFullname());
            popupWindow.setDisable(false);
            popupWindow.setVisible(true);
            return;
        }
        updateCalendar(new ActionEvent());
        updateInfo();
    }

    /**
     * This method removes the popup visible when trying to assign a turn while a person can't work on a certain date or turn
     * @param event
     */
    @FXML
    void closePopup(MouseEvent event){
        calendarPane.setDisable(false);
        popupWindow.setDisable(true);
        popupWindow.setVisible(false);
    }

    /**
     * This method creates an image of the calendar and saves it in the root of the program
     */
    @FXML
    public void exportCalendar(){
        // 978x560 x5 -> 4980x2800, modificati per tagliare meglio l'immagine

        for(Text t : matrixTurns){
            t.setFont(Font.font("Lucida console", 10.7));
            t.setUnderline(false);
        }

        SnapshotParameters sp = new SnapshotParameters();
        sp.setTransform(Transform.scale(5,5));
        workersCal.setVisible(false);
        gridInfo.setVisible(false);
        WritableImage snapshot = calendarPane.snapshot(sp,null);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot,null);
        image = image.getSubimage(0,0,4805,3500);
        File file = new File((System.getProperty("user.dir") + "\\Calendario.png"));
        try {
            ImageIO.write(image,"png", file);
            //ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
        } catch (IOException e){
            System.out.println("AHHH L'IMMAGINE NON SI STAMPA");
        }
        workersCal.setVisible(true);
        gridInfo.setVisible(true);
    }

    /**
     * This method updates the calendar, and its called when the page is loaded and everytime something change: a turn, the selected person or when compiling.
     * @param event
     */
    @FXML
    void updateCalendar(ActionEvent event){
        Person tempPerson;
        for(int i=1;i<Main.calendario.getNumberDays()+1;i++){
            for (Bands band : Bands.values()) {
                tempPerson = Main.calendario.getWorkerFromCode(Main.calendario.getDay(i).getCode(band));
                int selected = (i-1)*5 + band.getNumber()-1;
                if(!(tempPerson.getCode() == 0)){
                    matrixTurns.get(selected).setText(tempPerson.getShortName());
                    if(tempPerson.equals(getSelectedPerson()))
                    {
                        matrixTurns.get(selected).setUnderline(true);
                        matrixTurns.get(selected).setFont(boldFont);
                    }
                    else {
                        matrixTurns.get(selected).setUnderline(false);
                        matrixTurns.get(selected).setFont(normalFont);
                    }
                } else {
                    matrixTurns.get(selected).setUnderline(false);
                    matrixTurns.get(selected).setText("________");
                    matrixTurns.get(selected).setFont(normalFont);
                }
            }
        }
    }

    /**
     * This method change the scene to the NewWorkerPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    private void FAQ(Event event) throws IOException {
        new Main().changeScene("fxml/FAQPage.fxml");
    }

    /**
     * This method return the selected person in the workersList
     * @return the person selected in the ListView "workersList"
     */
    private Person getSelectedPerson(){
        try {
            String[] tempString = workersList.get(workersCal.getSelectionModel().getSelectedIndex()).split(" ");
            return Main.calendario.getWorkerFromName(tempString[0],tempString[1]);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * This method updates the worker's information, and it's called everytime they can change
     */
    private void updateInfo(){
        Person temp = getSelectedPerson();
        if(temp == null){
            return;
        }
        assignedHours.setText(String.valueOf(temp.getActualHours()));
        totalHours.setText(String.valueOf(temp.getTotalHours()));
        turns.setText(Main.calendario.getNumberOfTurns(temp.getName(), temp.getSurname())-temp.getNights() + "+" + temp.getNights());
        nightHours.setText(String.valueOf(temp.getNightHours()));
        if(temp.doesNights()) doesNightText.setText("SI");
        else doesNightText.setText("NO");
        updateCalendar(new ActionEvent());
    }
}
import Giornate.Bands;
import Giornate.WeekDays;
import Robe.Person;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the controller class of the WorkersPage
 */
public class WorkersController {

    private final boolean[] modify = {false, false};

    private List<String> workersList;

    @FXML
    private ListView<String> workers;

    @FXML
    private AnchorPane workerInfo;

    @FXML
    private Text assignedHours;

    @FXML
    private TextField weekHours;

    @FXML
    private TextField nightHours;

    @FXML
    private Text assignedNights;

    @FXML
    private Button weekHoursButton;

    @FXML
    private Button nightHoursButton;

    @FXML
    private AnchorPane tipsPanel;

    @FXML
    private List<Text> availabilityCells;

    @FXML
    private List<Rectangle> availabilityColors;

    @FXML
    private List<Text> holidayCells;

    @FXML
    private List<Rectangle> holidaysColors;

    @FXML
    private ComboBox<String> nightBar;

    //todo prima schermata con nomi in grande al centro -> poi quella che c'è ora

    /**
     * This method is called upon the creation of the class, and prepare the starting info
     */
    public void initialize(){
        workersList = new ArrayList<>();
        for (Person p : Main.calendario.getWorkers()){
            workersList.add(p.getFullname());
        }
        workers.setItems(FXCollections.observableArrayList(workersList));
        int calendarMatrix=0, effectiveDay=2;
        WeekDays temp = Main.calendario.getDay(1).getName();
        switch (temp) {
            case LUNEDI -> {
                availabilityCells.get(0).setText("1");
                holidayCells.get(0).setText("1");
                //calendarMatrix = 0;
            }
            case MARTEDI -> {
                availabilityCells.get(1).setText("1");
                holidayCells.get(1).setText("1");
                calendarMatrix = 1;
            }
            case MERCOLEDI -> {
                availabilityCells.get(2).setText("1");
                holidayCells.get(2).setText("1");
                calendarMatrix = 2;
            }
            case GIOVEDI -> {
                availabilityCells.get(3).setText("1");
                holidayCells.get(3).setText("1");
                calendarMatrix = 3;
            }
            case VENERDI -> {
                availabilityCells.get(4).setText("1");
                holidayCells.get(4).setText("1");
                calendarMatrix = 4;
            }
            case SABATO -> {
                availabilityCells.get(5).setText("1");
                holidayCells.get(5).setText("1");
                calendarMatrix = 5;
            }
            case DOMENICA -> {
                availabilityCells.get(6).setText("1");
                holidayCells.get(6).setText("1");
                calendarMatrix = 6;
            }
        }
        calendarMatrix=calendarMatrix+1;
        int maxDay = Main.calendario.getNumberDays();
        for (; calendarMatrix < availabilityCells.size()-1 && effectiveDay <= maxDay; calendarMatrix++) {
            availabilityCells.get(calendarMatrix).setText(String.valueOf(effectiveDay));
            holidayCells.get(calendarMatrix).setText(String.valueOf(effectiveDay));
            effectiveDay++;
        }
        List<String> nightList = new ArrayList<>(Arrays.asList("SI","NO"));
        nightBar.setItems(FXCollections.observableArrayList(nightList));
    }

    /**
     * This method change the scene to the FrontPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showCalendar() throws IOException {
        new Main().changeScene("fxml/FrontPage.fxml");
    }

    //todo rimuovere anche da fxml
    @FXML
    void showWorkers(Event event) {
        clear();
        workers.setVisible(true);
    }

    /**
     * This method shows on screen every information of the selected person
     */
    @FXML
    void showWorkerInfo(Event event){
        updateInfo();
        clearColors();
        Person tempPerson = getSelectedPerson();
        if (tempPerson == null) return;
        for(int i=0; i < 41 ; i++){
            if(!availabilityCells.get(i).getText().equals("")){
                int x = Integer.parseInt(availabilityCells.get(i).getText());
                for (Bands band : Bands.values()) {
                    if(!tempPerson.hasAvailability(x, band)){
                        colorBandRectangle(i,band);
                    }
                }
                if(!tempPerson.hasHoliday(x)){
                    colorDayRectangle(i);
                }
            }
        }
        workerInfo.setVisible(true);
    }

    /**
     * This method modify every availability of a certain day: if at least one is not taken, it assigns every one of the day, otherwise it clears everything.
     * @param event is used to understand which number is being pressed
     */
    @FXML
    void changeAllAvailability(MouseEvent event) {
        Person tempPerson = getSelectedPerson();
        if (tempPerson == null) return;
        Text tempText = (Text) event.getSource();
        int day=0;
        try{
            day = Integer.parseInt(tempText.getText());
        } catch (NumberFormatException e){
            return;
        }
        String temp = tempText.getId();
        int row, col, firstRect, counter = 0;
        row = temp.charAt(4) - 48;
        col = temp.charAt(5) - 48;
        firstRect = ((row - 1) * 7 + col) * 5;
        for (int i = 0; i < 5; i++) {
            if (availabilityColors.get(firstRect + i).getFill().equals(Color.WHITE)) {
                tempPerson.modifyAvailability(Integer.parseInt(tempText.getText()), Bands.bandOf(i+1), Main.calendario);
                availabilityColors.get(firstRect + i).setFill(Color.RED);
                counter++;
            }
        }
        if (counter == 0) {
            for (int i = 0; i < 5; i++) {
                tempPerson.modifyAvailability(day, Bands.bandOf(i+1), Main.calendario);
                availabilityColors.get(firstRect + i).setFill(Color.WHITE);
            }
        }
    }

    /**
     * This method assign or remove an availability by pressing on it. White -> no availability, Red -> availability taken
     * @param event is used to understand which number is being pressed
     */
    @FXML
    void changeAvailability(MouseEvent event){
        Person tempPerson = getSelectedPerson();
        if (tempPerson == null) return;
        Rectangle rect = (Rectangle) event.getSource();
        String temp = rect.getId();
        int row, col, band, matrix, day;
        row = temp.charAt(4)-48;
        col = temp.charAt(5)-48;
        band = temp.charAt(7)-48;
        matrix = (row-1)*7+col;
        //System.out.println("row:" + row + " col:" + col + " band:" + band + " matrix:" + matrix);
        try {
            day = Integer.parseInt(availabilityCells.get(matrix).getText());
        } catch (NumberFormatException ignore){
            return;
        }
        if(temp.length() == 8){
            //disponibilità
            if(tempPerson.modifyAvailability(day, Bands.bandOf(band), Main.calendario)){
                rect.setFill(Color.RED);
            } else {
                rect.setFill(Color.WHITE);
            }
        } else {
            //ferie
            if(tempPerson.modifyHolidays(day, Main.calendario)){
                rect.setFill(Color.RED);
            } else {
                rect.setFill(Color.WHITE);
            }

        }
    }

    /**
     * This method is called upon moving the mouse over the "?", and shows some tips to the user
     */
    @FXML
    void showTips(MouseEvent event) {
        tipsPanel.setVisible(true);
    }

    /**
     * This method is called upon moving the mouse away from "?", and cancels the tips from the screen
     */
    @FXML
    void clearTips(MouseEvent event) {
        tipsPanel.setVisible(false);
    }

    /**
     * This method is called upon pressing a button and does two things:
     * pressing the "modifica" button makes the textfield editable, you can now change the week hours of the worker
     * pressing the "conferma" button makes the textfield not editable, and tries to set the new week hours. If the input is wrong, it changes back to the old ones
     *
     * its called totalHours because of previous interaction of the code
     */
    @FXML
    void modifyTotalHours(MouseEvent event){
        if(!modify[0]){
            weekHoursButton.setText("Conferma");
            weekHoursButton.setTextFill(Color.GREEN);
            weekHours.setEditable(true);
        }
        else {
            weekHoursButton.setText("Modifica");
            weekHoursButton.setTextFill(Color.BLACK);
            weekHours.setEditable(false);
            Person tempPerson = getSelectedPerson();
            if (tempPerson == null) return;
            try {
                tempPerson.setWeekHours(Float.parseFloat(weekHours.getText()), Main.calendario.getNumberDays());
            } catch (NumberFormatException e) {
                weekHours.setText(String.valueOf(tempPerson.getTotalHours()));
            }
        }
        modify[0] = !modify[0];
    }

    /**
     * This method is called upon pressing a button and does two things:
     * pressing the "modifica" button makes the textfield editable, you can now change the night hours of the worker
     * pressing the "conferma" button makes the textfield not editable, and tries to set the new night hours. If the input is wrong, it changes back to the old ones
     */
    @FXML
    void modifyNightHours(MouseEvent event){
        if(!modify[1]){
            nightHoursButton.setText("Conferma");
            nightHoursButton.setTextFill(Color.GREEN);
            nightHours.setEditable(true);
        }
        else {
            nightHoursButton.setText("Modifica");
            nightHoursButton.setTextFill(Color.BLACK);
            nightHours.setEditable(false);
            Person tempPerson = getSelectedPerson();
            if (tempPerson == null) return;
            try{
                tempPerson.setNightHours(Float.parseFloat(nightHours.getText()));
                updateInfo();
            }
            catch (NumberFormatException e){
                nightHours.setText(String.valueOf(tempPerson.getNightHours()));
            }
        }
        modify[1] = !modify[1];
    }

    /**
     * This method modifies the setting about the nights: if its value is "NO", the person won't be selected to work at nights by the program
     */
    @FXML
    void modifyNight(MouseEvent event){
        if (getSelectedPerson() == null) return;
        getSelectedPerson().setNights(nightBar.getValue().equals("SI"));
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
     * This method change the scene to the removeWorkerPage
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

    /**
     * This method change the scene to the RecapPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showSummary(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/RecapPage.fxml");
    }

    /**
     * This method change the scene to the FAQPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    private void FAQ(Event event) throws IOException {
        new Main().changeScene("fxml/FAQPage.fxml");
    }

    //todo useless, da cancellare
    private void clear(){
        workers.setVisible(false);
        workerInfo.setVisible(false);
    }

    /**
     * This method colors a rectangle to red, signaling that the turn is taken as availability
     * @param i index of the day to color
     * @param band to actually color
     */
    private void colorBandRectangle(int i, Bands band){
        availabilityColors.get(i*5 + band.getNumber()-1).setFill(Color.RED);
    }

    /**
     * This method colors a rectangle to red, signaling that the day is taken as holiday
     * @param i index of the day to color
     */
    private void colorDayRectangle(int i){
        holidaysColors.get(i).setFill(Color.RED);
    }

    /**
     * This method makes every rectangle white, and it's used when changing person to recolor every turn/day
     */
    private void clearColors(){
        for (Rectangle temp : availabilityColors){
            temp.setFill(Color.WHITE);
        }
        for (Rectangle temp : holidaysColors){
            temp.setFill(Color.WHITE);
        }
    }

    /**
     * This method returns the selected Person in the workersList
     * @return the selected Person in the ListView
     */
    private Person getSelectedPerson(){
        try {
            String[] tempString = workersList.get(workers.getSelectionModel().getSelectedIndex()).split(" ");
            return Main.calendario.getWorkerFromName(tempString[0],tempString[1]);
        } catch (IndexOutOfBoundsException ignore) {
            return null;
        }
    }

    /**
     * This method updates the side information of the worker
     */
    private void updateInfo(){
        Person tempPerson = getSelectedPerson();
        if (tempPerson == null) return;
        assignedHours.setText(String.valueOf(tempPerson.getActualHours()));
        weekHours.setText(String.valueOf(tempPerson.getWeekHours()));
        assignedNights.setText(String.valueOf(tempPerson.getNights()));
        nightHours.setText(String.valueOf(tempPerson.getNightHours()));
        if (tempPerson.doesNights()) nightBar.setValue("SI");
        else nightBar.setValue("NO");
    }
}

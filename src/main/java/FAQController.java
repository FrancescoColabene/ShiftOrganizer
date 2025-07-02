import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * This is the controller class of the FAQPage
 */
public class FAQController {

    @FXML
    private Text mainText;

    /**
     * This method is called upon the creation of the class, and writes on screen some tips and information about the program
     */
    public void initialize(){
        String string = """
                Questa sezione è ancora work in progress, per adesso contiene solamente alcuni consigli. Per esempio:
                La supervisione e le equipe possono essere solamente inserite all'avvio del programma. Se le hai dimenticate, comparirà un messaggio nella pagina del calendario, e le ore relative a supervisioni/equipe non saranno contate nel totale!
                Vale la stessa cosa per i giorni scolastici, infatti selezionare "Si" alla scuola nella prima pagina farà si che tutti i giorni infrasettimanali abbiano un solo turno assegnato di mattina. Viceversa, segnare "No" farà assegnare al programma tutti i turni.
                Per poter premere conferma nella pagina delle supervisioni e delle equipe, bisogna necessariamente scegliere una data (quindi cliccare sulla finestra di fianco alla scritta "Giorno" e selezionare un giorno diverso dal primo).
                Se si è sbagliato ad inserire la data di una supervisione o di un equipe, basterà premere "Modifica supervisioni" oppure "Modifica equipe" e reimmettere la stessa data.
                Questo stesso procedimento si può utilizzare per inserire ulteriori equipe (si possono inserire anche più supervisioni, quindi stai attenta!).
                Nella sezione dei lavoratori, nel grafico delle disponibilità, premendo sul numero del giorno si assegna direttamente tutta la giornata (e ripremendolo, si cancella tutta).
                Si può vedere un riassunto dei turni assegnati e delle ferie di ogni persona andando in "Calendario ➞ Mostra il riepilogo".
                Si possono assegnare turni a mano prima e dopo aver completato l'assegnamento automatico. Nella schermata del calendario, bisogna premere sulla persona e successivamente sul turno che si vuole assegnare: puoi premere sia su un turno già assegnato (e verrà sostituito) oppure su un turno vuoto.
                Il programma ti bloccherà solamente se il turno che stai provando ad assegnare fa parte delle disponibilità o ferie del dipendente. In questo caso non verrà assegnato il turno, e bisogna premere sulla finestra che compare al centro dello schermo per continuare.
                Per iniziare l'assegnamento automatico dei turni, bisogna selezionare "Calendario ➞ Avvia l'assegnamento automatico" dalla pagina del calendario!
                Per salvare il calendario, premere "Calendario ➞ Esporta il calendario" nella schermata del calendario: troverai un'immagine nella cartella.
                """;
        mainText.setText(string);
    }

    /**
     * This method change the scene to the WorkerPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showWorkers() throws IOException {
        new Main().changeScene("fxml/WorkersPage.fxml");
    }

    /**
     * This method change the scene to the FrontPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showCalendar() throws IOException {
        new Main().changeScene("fxml/FrontPage.fxml");
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
     * This method change the scene to the RecapPage
     * @throws IOException when it fails to find the file
     */
    @FXML
    void showSummary(ActionEvent event) throws IOException {
        new Main().changeScene("fxml/RecapPage.fxml");
    }
}
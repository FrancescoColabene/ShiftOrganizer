# Shift Organizer

Implementazione di un organizzatore di turni mensile che permette di assegnare manualmente turni, ferie e disponibilità, oltre all'assegnamento automatico che segue regole precise.

## 📌 Descrizione

Il progetto comprende una serie di funzionalità per la gestione dei turni e delle ore lavorative del personale:
- Impostare anno e mese, oltre a poter definire se è periodo scolastico o meno (cambio delle politiche di assegnamento turni).
- Impostare giorno e orario delle supervisioni, che vengono calcolate per tutti.
- Impostare il giorno delle equipe (orario fisso), che vengono calcolate per tutti.
- Aggiungere e modificare lavoratori, con ore settimanali personali e ore aggiuntive notturne. È possibile salvare i lavoratori per non doverli reinserire ad ogni utilizzo.
- Aggiungere disponibilità (turni in cui un lavoratore non può essere assegnato) e ferie (giorni in cui il lavoratore non può essere assegnato).
- Impostare manualmente i turni ai lavoratori.
- Assegnamento dei turni rimasti ai lavoratori in modo automatico, seguendo vincoli prestabiliti.
- Esportazione della tabella dei turni in immagine in format png.
- Schermata di riepilogo con turni e ferie assegnate ai singoli lavoratori.
- Schermata FAQ con consigli generali sull'utilizzo dell'applicazione.

## 📷 Immagini
Tutte le funzionalità sono accessibili tramite diverse pagine: le più importanti sono mostrate sotto.

### Pagina del calendario
![Calendario](Immagini/PaginaPrincipale.png 'Pagina del calendario')

### Modifica di un lavoratore e assegnamento disponibilità / ferie
![Lavoratori](Immagini/Lavoratore.png 'Modifica lavoratore')

### Calendario esportato
![Calendario](Immagini/Calendario.png 'Calendario')
I turni vuoti sono turni che non è possibile assegnare con i vincoli prestabiliti e i turni precedentemente assegnati.

## 🛠 Tecnologie e Strumenti

- **Java 17** - linguaggio principale del progetto
- **Jetbrains IntelliJ** - IDE utilizzato per lo sviluppo del progetto
- **JavaFX and Scenebuilder** - framework grafico e applicazione usata per la realizzazione della GUI
- **JSON** - salvataggio delle informazioni dei lavoratori


## 📁 Struttura del Progetto

```
.
├── Immagini                    # Cartella con immagini delle pagine
├── META-INF                    # Cartella generata da IntelliJ
├── jar                         # Eseguibile 
├── src/main                    # Sorgente 
├── target                      # Cartella generata da IntelliJ
├── dependency-reduced-pom.xml  # File generato da IntelliJ
├── pom.xml                     # File generato da IntelliJ
└── README.md                   # Questo file
```

## 👤 Autore

Progetto sviluppato da [Francesco Colabene](https://github.com/FrancescoColabene).

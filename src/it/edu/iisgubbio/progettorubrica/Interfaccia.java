package it.edu.iisgubbio.progettorubrica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox; 
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Interfaccia extends Application {
	ListView<Contatto> rubrica = new ListView<>();
	ArrayList<Contatto> elencoCompleto = new ArrayList<>();
	
	Label lNomeContatto = new Label("Nome contatto: ");
	TextField tNomeContatto = new TextField();
	Label lCognomeContatto = new Label("Cognome contatto: ");
	TextField tCognomeContatto = new TextField();
	Label lTipoContatto = new Label("Tipo di contatto: ");
	ComboBox<String> cbTipoContatto = new ComboBox<>();
	Label lNumeroContatto = new Label("Numero contatto: ");
	TextField tNumeroContatto = new TextField();
	Label lEmailContatto = new Label("Email contatto: ");
	TextField tEmailContatto = new TextField();
	
	Button bCercaContatto = new Button("Cerca Contatto");
	Button bAggiungiContatto = new Button("Aggiungi Contatto");
	
	Contatto contattoSelezionato = null;

	@Override
	public void start(Stage finestra) throws Exception {
		GridPane griglia = new GridPane();
		griglia.setVgap(25);
		griglia.setHgap(500);
		griglia.setPadding(new Insets(50, 50, 50, 50));
		
		cbTipoContatto.getItems().addAll("PERSONALE", "LAVORO");
		cbTipoContatto.setValue("PERSONALE"); 	
		
		cbTipoContatto.getSelectionModel().selectedItemProperty().addListener((proprieta, vecchioValore, nuovoValore) -> {
			if (nuovoValore != null) {
				if (nuovoValore.equals("PERSONALE")) {
					lEmailContatto.setText("Email contatto: ");
				} else {
					lEmailContatto.setText("Organizzazione contatto: ");
				}
			}
		});
		
		griglia.add(lNomeContatto, 0, 0);	
		griglia.add(tNomeContatto, 1, 0);
		griglia.add(lCognomeContatto, 0, 1);	
		griglia.add(tCognomeContatto, 1, 1);
		griglia.add(lTipoContatto, 0, 2);	
		griglia.add(cbTipoContatto, 1, 2);
		griglia.add(lNumeroContatto, 0, 3);	
		griglia.add(tNumeroContatto, 1, 3);
		griglia.add(lEmailContatto, 0, 4);	
		griglia.add(tEmailContatto, 1, 4);
		griglia.add(bCercaContatto, 0, 5);	
		griglia.add(bAggiungiContatto, 1, 5);
		griglia.add(rubrica, 0, 6, 2, 1);	
		rubrica.setMaxWidth(Integer.MAX_VALUE);
		
		rubrica.setOnMouseClicked(eventoClick -> {
			if (eventoClick.getClickCount() == 2 && rubrica.getSelectionModel().getSelectedItem() != null) {
				contattoSelezionato = rubrica.getSelectionModel().getSelectedItem();
				
				tNomeContatto.setText(contattoSelezionato.getNome());
				tCognomeContatto.setText(contattoSelezionato.getCognome());
				tNumeroContatto.setText(contattoSelezionato.getNumeroTelefono());
				tEmailContatto.setText(contattoSelezionato.getExtra());
				
				cbTipoContatto.setValue(contattoSelezionato.getTipo().toUpperCase());
				bAggiungiContatto.setText("Salva Modifica");
			}
		});

		Scene scenaPrincipale = new Scene(griglia);
		finestra.setTitle("Rubrica");
		finestra.setScene(scenaPrincipale);
		finestra.show();
		
		bAggiungiContatto.getStyleClass().add("btn-aggiungi");
		scenaPrincipale.getStylesheets().add(getClass().getResource("Interfaccia.css").toExternalForm());
		
		try (BufferedReader lettoreDiRighe = new BufferedReader(new FileReader("rubrica.csv"))) {
			String rigaLetta;
			while ((rigaLetta = lettoreDiRighe.readLine()) != null) {
				String[] pezziDati = rigaLetta.split(";");
				if (pezziDati.length >= 5) {
					String tipoContatto = pezziDati[0];
					String nomeContatto = pezziDati[1];
					String cognomeContatto = pezziDati[2];
					String telefonoContatto = pezziDati[3];
					String datoExtraContatto = pezziDati[4];

					Contatto nuovoContatto;
					if (tipoContatto.equalsIgnoreCase("PERSONALE")) {
						nuovoContatto = new ContattoPersonale(nomeContatto, cognomeContatto, telefonoContatto, datoExtraContatto);
					} else {
						nuovoContatto = new ContattoLavoro(nomeContatto, cognomeContatto, telefonoContatto, datoExtraContatto);
					}
					
					rubrica.getItems().add(nuovoContatto);
					elencoCompleto.add(nuovoContatto);
				}
			}
		} catch (FileNotFoundException eccezioneMancanzaFile) {
			System.out.println("Archivio non trovato. Sarà creato al primo inserimento.");
		} catch (IOException eccezioneErroreLettura) {
			System.out.println("Errore durante la lettura dell'archivio.");
		}
		
		bCercaContatto.setOnAction(eventoCerca -> cercaContatto());
		bAggiungiContatto.setOnAction(eventoAggiungi -> aggiungiContatto());
	}
	
	public void cercaContatto() {
		String cercaNome = tNomeContatto.getText().toLowerCase();
		String cercaCognome = tCognomeContatto.getText().toLowerCase();
		String cercaTipo = cbTipoContatto.getValue().toLowerCase();
		String cercaNumero = tNumeroContatto.getText().toLowerCase();
		String cercaExtra = tEmailContatto.getText().toLowerCase();
		
		rubrica.getItems().clear();
		for (Contatto contattoCorrente : elencoCompleto) {
			String nomeCorrente = contattoCorrente.getNome().toLowerCase();
			String cognomeCorrente = contattoCorrente.getCognome().toLowerCase();
			String tipoCorrente = contattoCorrente.getTipo().toLowerCase();
			String numeroCorrente = contattoCorrente.getNumeroTelefono().toLowerCase();
			String extraCorrente = contattoCorrente.getExtra().toLowerCase();
			
			if (nomeCorrente.startsWith(cercaNome) && 
				cognomeCorrente.startsWith(cercaCognome) && 
				tipoCorrente.startsWith(cercaTipo) && 
				numeroCorrente.startsWith(cercaNumero) && 
				extraCorrente.startsWith(cercaExtra)) {
				rubrica.getItems().add(contattoCorrente);
			}
		}
	}
	
	public void aggiungiContatto() {
		String tipoInput = cbTipoContatto.getValue();
		String nomeInput = tNomeContatto.getText().trim();
		String cognomeInput = tCognomeContatto.getText().trim();
		String telInput = tNumeroContatto.getText().trim();
		String extraInput = tEmailContatto.getText().trim();

		if (nomeInput.isEmpty() || telInput.isEmpty()) {
			Alert avvisoDatiMancanti = new Alert(Alert.AlertType.ERROR);
			avvisoDatiMancanti.setTitle("Dati Mancanti");
			avvisoDatiMancanti.setHeaderText(null);
			avvisoDatiMancanti.setContentText("Nome e Numero di telefono sono obbligatori!");
			avvisoDatiMancanti.showAndWait();
			return;
		}

		if (!telInput.matches("\\d{9,10}")) {
			Alert avvisoNumeroErrato = new Alert(Alert.AlertType.ERROR);
			avvisoNumeroErrato.setTitle("Numero non valido");
			avvisoNumeroErrato.setHeaderText(null);
			avvisoNumeroErrato.setContentText("Il numero deve contenere solo cifre ed essere lungo 9 o 10 numeri!");
			avvisoNumeroErrato.showAndWait();
			return;
		}

		if (extraInput.isEmpty()) {
			String messaggioErrore = tipoInput.equals("PERSONALE") ? "L'email è obbligatoria!" : "L'organizzazione è obbligatoria!";
			Alert avvisoExtraMancante = new Alert(Alert.AlertType.ERROR);
			avvisoExtraMancante.setTitle("Dato mancante");
			avvisoExtraMancante.setHeaderText(null);
			avvisoExtraMancante.setContentText(messaggioErrore);
			avvisoExtraMancante.showAndWait();
			return;
		}

		if (tipoInput.equals("PERSONALE") && !extraInput.contains("@")) {
			Alert avvisoEmailErrata = new Alert(Alert.AlertType.ERROR);
			avvisoEmailErrata.setTitle("Email non valida");
			avvisoEmailErrata.setHeaderText(null);
			avvisoEmailErrata.setContentText("L'email deve contenere il simbolo @");
			avvisoEmailErrata.showAndWait();
			return;
		}

		if (contattoSelezionato != null) {
			elencoCompleto.remove(contattoSelezionato);
			contattoSelezionato = null; 
			bAggiungiContatto.setText("Aggiungi Contatto"); 
		}

		Contatto nuovoContatto;
		if (tipoInput.equals("PERSONALE")) {
			nuovoContatto = new ContattoPersonale(nomeInput, cognomeInput, telInput, extraInput);
		} else {
			nuovoContatto = new ContattoLavoro(nomeInput, cognomeInput, telInput, extraInput);
		}

		elencoCompleto.add(nuovoContatto);

		try (PrintWriter scrittoreFile = new PrintWriter("rubrica.csv")) {
			for (Contatto contattoCatalogo : elencoCompleto) {
				scrittoreFile.println(contattoCatalogo.getTipo().toUpperCase() + ";" + contattoCatalogo.getNome() + ";" + contattoCatalogo.getCognome() + ";" + contattoCatalogo.getNumeroTelefono() + ";" + contattoCatalogo.getExtra());
			}

			rubrica.getItems().clear();
			rubrica.getItems().addAll(elencoCompleto);

			tNomeContatto.clear();
			tCognomeContatto.clear();
			tNumeroContatto.clear();
			tEmailContatto.clear();

			Alert avvisoSuccesso = new Alert(Alert.AlertType.INFORMATION);
			avvisoSuccesso.setTitle("Successo");
			avvisoSuccesso.setHeaderText(null);
			avvisoSuccesso.setContentText("Dati salvati ed elenco aggiornato con successo!");
			avvisoSuccesso.showAndWait();

		} catch (IOException eccezioneErroreScrittura) {
			Alert avvisoErroreFile = new Alert(Alert.AlertType.ERROR);
			avvisoErroreFile.setTitle("Errore file");
			avvisoErroreFile.setHeaderText(null);
			avvisoErroreFile.setContentText("Errore nel salvataggio dei dati sul file CSV!");
			avvisoErroreFile.showAndWait();
		}
	}
	
	public static void main(String[] argomentiAvvio) {
		launch(argomentiAvvio);
	}
}
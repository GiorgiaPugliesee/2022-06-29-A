/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	this.txtResult.clear();
    	Album a = this.cmbA1.getValue();
    	
    	if(a == null) {
    		this.txtResult.setText("Selezionare un album.");
    		return;
    	}
    	
    	List<Album> adiacenti = this.model.getAdiacenti(a);
    	
    	for(Album al : adiacenti) {
    		this.txtResult.appendText(al + "; bilancio: " + al.getBilancio() + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	
    	String input = this.txtX.getText();
    	int inputNum = 0;
    	try {
    		inputNum = Integer.parseInt(input);
    		
    		Album source = this.cmbA1.getValue();
    		Album target = this.cmbA2.getValue();
    		
    		if(source == null || target == null) {
    			this.txtResult.setText("Inserire degli album.");
    			return;
    		}

    		List<Album> path = this.model.buildPath(source, target, inputNum);
    		
    		if(path.isEmpty()) {
    			this.txtResult.setText("Non è stato trovato nessun percorso.");
    			return;
    		} 
    		
    		for(Album al : path) {
    			this.txtResult.appendText(al + "\n");
    		}
        	
        	
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore valido.");
    		return;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String input = this.txtN.getText();
    	int inputNum = 0;
    	try {
    		inputNum = Integer.parseInt(input);
    		this.model.creaGrafo(inputNum);
    		
    		this.txtResult.appendText("Il grafo è stato creato correttamente.\n");
        	this.txtResult.appendText("Vertici: " + this.model.getVertex() + "\n");
        	this.txtResult.appendText("Archi: " + this.model.getEdge() + "\n");

        	this.cmbA1.getItems().addAll(this.model.allVertex());
        	this.cmbA2.getItems().addAll(this.model.allVertex());
        	
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore valido.");
    		return;
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    	
    	
    }
}

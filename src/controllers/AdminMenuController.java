package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMenuController {
    
    @FXML
    private Button dashboardButton;
    
    @FXML
    private Button gestionNotesButton;
    
    @FXML
    private Button listeEtudiantsButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private AnchorPane contentPane;
    
    public void initialize() {
        // Charger le dashboard par défaut
        openDashboard();
    }
    
    @FXML
    private void openDashboard() {
        loadContent("/views/Dashboard.fxml");
    }
    
    @FXML
    private void openGestionNotes() {
        loadContent("/views/GestionNotes.fxml");
    }
    
    @FXML
    private void openListeEtudiants() {
        loadContent("/views/ListeEtudiants.fxml");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Scene loginScene = new Scene(loginView, 600, 400);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Connexion");
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de retourner à la page de connexion.");
        }
    }
    
    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            
            // Configurer le contenu pour remplir tout l'espace disponible
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            
            // Vider et ajouter le nouveau contenu
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de charger la page demandée.");
        }
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
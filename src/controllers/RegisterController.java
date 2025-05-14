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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Student;

import java.io.IOException;

public class RegisterController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private TextField matriculeField;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String matricule = matriculeField.getText();
        
        // Validation des champs
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            nom.isEmpty() || prenom.isEmpty() || matricule.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur d'inscription", "Veuillez remplir tous les champs.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Erreur d'inscription", "Les mots de passe ne correspondent pas.");
            return;
        }
        
        if (Student.studentExists(username) || username.equals("admin")) {
            showAlert(AlertType.ERROR, "Erreur d'inscription", "Ce nom d'utilisateur existe déjà.");
            return;
        }
        
        // Création du nouvel étudiant
        Student newStudent = new Student(username, password, nom, prenom, matricule);
        Student.addStudent(newStudent);
        
        showAlert(AlertType.INFORMATION, "Inscription réussie", "Votre compte a été créé avec succès.");
        
        // Retour à la page de login
        backToLogin(event);
    }
    
    @FXML
    private void backToLogin(ActionEvent event) {
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
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
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
import models.Admin;
import models.Student;

import java.io.IOException;

public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    // Initialisation avec un admin par défaut et quelques étudiants pour les tests
    public void initialize() {
        // Créer l'admin (singleton)
        new Admin();
        
        // Ajouter quelques étudiants pour les tests
        if (!Student.studentExists("etudiant1")) {
            Student student1 = new Student("etudiant1", "pass1", "Dupont", "Jean", "E001");
            Student.addStudent(student1);
        }
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur de connexion", "Veuillez remplir tous les champs.");
            return;
        }
        
        // Vérifier si c'est l'admin
        if (Admin.isAdmin(username)) {
            Admin admin = new Admin();
            if (admin.authenticate(username, password)) {
                loadAdminDashboard(event);
                return;
            }
        } 
        // Sinon, vérifier si c'est un étudiant
        else if (Student.studentExists(username)) {
            Student student = Student.getStudentByUsername(username);
            if (student.authenticate(username, password)) {
                loadStudentDashboard(event, student);
                return;
            }
        }
        
        showAlert(AlertType.ERROR, "Erreur de connexion", "Nom d'utilisateur ou mot de passe incorrect.");
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
            Parent registerView = loader.load();
            Scene registerScene = new Scene(registerView, 600, 400);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Inscription Étudiant");
            stage.setScene(registerScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }
    
    private void loadAdminDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashboard.fxml"));
            Parent adminView = loader.load();
            Scene adminScene = new Scene(adminView, 800, 600);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Tableau de Bord Admin");
            stage.setScene(adminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de charger le tableau de bord admin.");
        }
    }
    
    private void loadStudentDashboard(ActionEvent event, Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/StudentDashboard.fxml"));
            Parent studentView = loader.load();
            
            StudentController controller = loader.getController();
            controller.initData(student);
            
            Scene studentScene = new Scene(studentView, 800, 600);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Tableau de Bord Étudiant");
            stage.setScene(studentScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible de charger le tableau de bord étudiant.");
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